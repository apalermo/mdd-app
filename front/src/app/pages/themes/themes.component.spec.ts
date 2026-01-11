import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ThemesComponent } from './themes.component';
import { ThemeService } from '../../core/services/theme.service';
import { SessionService } from '../../core/services/session.service';
import { UserService } from '../../core/services/user.service';
import { of } from 'rxjs';
import { Theme } from '../../models/theme.interface';
import { User } from '../../models/user.interface';
import { signal } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('ThemesComponent', () => {
  let component: ThemesComponent;
  let fixture: ComponentFixture<ThemesComponent>;

  const mockThemes: Theme[] = [
    { id: 1, title: 'Java', description: 'Tout sur Java' },
    { id: 2, title: 'Angular', description: 'Tout sur Angular' },
    { id: 3, title: 'Docker', description: 'Tout sur Docker' },
  ];

  const mockUser: User = {
    id: 1,
    email: 'dev@test.com',
    name: 'Dev',
    subscriptions: [{ id: 1, title: 'Java', description: 'Tout sur Java' }],
    created_at: '',
    updated_at: '',
  };

  const mockThemeService = {
    getThemes: vi.fn().mockReturnValue(of(mockThemes)),
    subscribeToTheme: vi.fn().mockReturnValue(of(void 0)),
  };

  const mockSessionService = {
    user: signal<User | undefined>(mockUser),
    updateUser: vi.fn(),
  };
  const mockUserService = { me: vi.fn().mockReturnValue(of(mockUser)) };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ThemesComponent],
      providers: [
        { provide: ThemeService, useValue: mockThemeService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ThemesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch and display themes via AsyncPipe', () => {
    expect(mockThemeService.getThemes).toHaveBeenCalled();
    const cards = fixture.debugElement.queryAll(By.css('.theme-card'));
    expect(cards.length).toBe(3);
    const firstTitle = cards[0].query(By.css('h2')).nativeElement.textContent;
    expect(firstTitle).toContain('Java');
  });

  it('should render themes with accessible aria-labelledby', () => {
    const firstTitle = fixture.debugElement.query(By.css('h2')).nativeElement;
    const firstCard = fixture.debugElement.query(
      By.css('.theme-card')
    ).nativeElement;
    expect(firstCard.getAttribute('aria-labelledby')).toBe(firstTitle.id);
  });

  it('should distinguish between subscribed and non-subscribed themes', () => {
    const javaBtn = fixture.debugElement.query(
      By.css('button[aria-label*="Java"]')
    );
    expect(javaBtn.nativeElement.textContent).toContain('Déjà abonné');
    expect(javaBtn.nativeElement.disabled).toBe(true);

    const angularBtn = fixture.debugElement.query(
      By.css('button[aria-label*="Angular"]')
    );
    expect(angularBtn.nativeElement.textContent).toContain("S'abonner");
    expect(angularBtn.nativeElement.disabled).toBe(false);
  });

  it('should handle subscription process', () => {
    const angularBtn = fixture.debugElement.query(
      By.css('button[aria-label*="Angular"]')
    );
    angularBtn.nativeElement.click();
    expect(mockThemeService.subscribeToTheme).toHaveBeenCalledWith(2);
    expect(mockUserService.me).toHaveBeenCalled();
  });

  it('should show empty message when no themes are returned', () => {
    mockThemeService.getThemes.mockReturnValue(of([]));
    component.themes$ = mockThemeService.getThemes();
    fixture.detectChanges();
    const emptyMsg = fixture.debugElement.nativeElement.textContent;
    expect(emptyMsg).toContain('Aucun thème disponible');
  });
});
