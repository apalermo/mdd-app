import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { SessionService } from '../../core/services/session.service';
import { provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';
import { signal } from '@angular/core';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  // On crée le signal à l'extérieur pour pouvoir le modifier dans les tests
  const isLoggedSignal = signal(true);

  const mockSessionService = {
    isLogged: isLoggedSignal,
    logOut: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        provideRouter([]),
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;

    // Reset du signal à true avant chaque test
    isLoggedSignal.set(true);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // --- TEST DE LOGIQUE UI (Existant) ---
  it('should toggle mobile menu visibility', () => {
    expect(component.isMobileMenuOpen()).toBe(false);

    component.toggleMobileMenu();
    expect(component.isMobileMenuOpen()).toBe(true);

    fixture.detectChanges(); // Mise à jour du DOM
    const nav = fixture.debugElement.query(By.css('.navigation'));
    expect(nav.classes['active']).toBe(true);

    component.closeMenu();
    expect(component.isMobileMenuOpen()).toBe(false);
  });

  // --- TEST D'INTEGRATION ROUTER (Nouveau) ---
  it('should have correct href attributes', () => {
    const links = fixture.debugElement.queryAll(By.css('a'));

    // On vérifie simplement si l'attribut href contient ce qu'on veut
    const hrefs = links.map((l) => l.nativeElement.getAttribute('href'));

    expect(hrefs).toContain('/articles');
    expect(hrefs).toContain('/themes');
    expect(hrefs).toContain('/me');
  });

  // --- TEST DE LOGOUT (Existant) ---
  it('should call logOut and navigate to root when logout link is clicked', () => {
    const navigateSpy = vi.spyOn(component['router'], 'navigate');
    const logoutLink = fixture.debugElement.query(By.css('.logout-link'));

    expect(logoutLink).toBeTruthy(); // Vérifie que le lien existe
    logoutLink.nativeElement.click();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });

  // --- TEST ETAT DECONNECTÉ (Nouveau & Important) ---
  it('should NOT show navigation or burger menu when logged out', () => {
    // 1. On simule la déconnexion
    isLoggedSignal.set(false);
    fixture.detectChanges(); // On met à jour le HTML

    // 2. On vérifie que les éléments sensibles ont disparu
    const nav = fixture.debugElement.query(By.css('.navigation'));
    const burger = fixture.debugElement.query(By.css('.burger-btn'));

    // Ils ne devraient pas être là (null)
    expect(nav).toBeNull();
    expect(burger).toBeNull();
  });
});
