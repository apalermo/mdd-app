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

  const userSignal = signal<User | undefined>(mockUser);
  const mockSessionService = {
    user: userSignal,
    updateUser: vi.fn(),
  };

  const mockUserService = {
    me: vi.fn().mockReturnValue(
      of({
        ...mockUser,
        subscriptions: [
          ...mockUser.subscriptions,
          { id: 2, title: 'Angular', description: 'New sub' },
        ],
      })
    ),
  };

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

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch and display themes via AsyncPipe', () => {
    expect(mockThemeService.getThemes).toHaveBeenCalled();

    const cards = fixture.debugElement.queryAll(By.css('.theme-card'));
    expect(cards.length).toBe(3);

    const firstTitle = cards[0].query(By.css('h3')).nativeElement.textContent;
    expect(firstTitle).toContain('Java');
  });

  it('should distinguish between subscribed and non-subscribed themes', () => {
    const javaCard = fixture.debugElement.queryAll(By.css('.theme-card'))[0];
    const javaBtn = javaCard.query(By.css('button'));

    expect(javaBtn.nativeElement.textContent).toContain('Déjà abonné');
    expect(javaBtn.nativeElement.disabled).toBe(true);

    const angularCard = fixture.debugElement.queryAll(By.css('.theme-card'))[1];
    const angularBtn = angularCard.query(By.css('button'));

    expect(angularBtn.nativeElement.textContent).toContain("S'abonner");
    expect(angularBtn.nativeElement.disabled).toBe(false);
  });

  it('should subscribe and refresh user session when clicking "S\'abonner"', () => {
    const angularCard = fixture.debugElement.queryAll(By.css('.theme-card'))[1];
    const angularBtn = angularCard.query(By.css('button'));

    angularBtn.nativeElement.click();

    expect(mockThemeService.subscribeToTheme).toHaveBeenCalledWith(2);
    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.updateUser).toHaveBeenCalled();
  });
});
