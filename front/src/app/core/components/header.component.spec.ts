import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { SessionService } from '../../core/services/session.service';
import { provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';
import { signal } from '@angular/core';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

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

    isLoggedSignal.set(true);
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle mobile menu visibility', () => {
    expect(component.isMobileMenuOpen()).toBe(false);

    component.toggleMobileMenu();
    expect(component.isMobileMenuOpen()).toBe(true);

    fixture.detectChanges();
    const nav = fixture.debugElement.query(By.css('#main-navigation'));
    expect(nav.classes['active']).toBe(true);

    component.closeMenu();
    expect(component.isMobileMenuOpen()).toBe(false);
  });

  it('should have correct href attributes', () => {
    const links = fixture.debugElement.queryAll(By.css('a'));
    const hrefs = links.map((l) => l.nativeElement.getAttribute('href'));

    expect(hrefs).toContain('/articles');
    expect(hrefs).toContain('/themes');
    expect(hrefs).toContain('/me');
  });

  it('should call logOut and navigate to root when logout button is clicked', () => {
    const navigateSpy = vi.spyOn(component['router'], 'navigate');
    const logoutBtn = fixture.debugElement.query(
      By.css('button[aria-label="Se déconnecter de l\'application"]')
    );

    expect(logoutBtn).toBeTruthy();
    logoutBtn.nativeElement.click();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });

  it('should NOT show navigation or burger menu when logged out', () => {
    isLoggedSignal.set(false);
    fixture.detectChanges();

    const nav = fixture.debugElement.query(By.css('#main-navigation'));
    const burger = fixture.debugElement.query(
      By.css('button[aria-label="Menu principal"]')
    );

    expect(nav).toBeNull();
    expect(burger).toBeNull();
  });
});
