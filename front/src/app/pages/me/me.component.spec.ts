import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { provideRouter, Router } from '@angular/router';
import { of, Subject, throwError } from 'rxjs';
import { User } from '../../models/user.interface';
import { signal } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: Router;

  const mockUserService = {
    me: vi.fn(),
  };

  const mockThemeService = {
    unsubscribe: vi.fn().mockReturnValue(of(void 0)),
  };

  const userSignal = signal<User | undefined>(undefined);
  const mockSessionService = {
    user: userSignal,
    updateUser: vi.fn((u: User) => userSignal.set(u)),
    logOut: vi.fn(),
  };

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    name: 'SuperDev',
    subscriptions: [
      { id: 10, title: 'Java', description: 'Java is cool' },
      { id: 20, title: 'Angular', description: 'Angular is hot' },
    ],
    created_at: new Date().toISOString(),
    updated_at: new Date().toISOString(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        provideRouter([]),
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ThemeService, useValue: mockThemeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    userSignal.set(undefined);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init and update session', () => {
    mockUserService.me.mockReturnValue(of(mockUser));
    fixture.detectChanges();

    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.updateUser).toHaveBeenCalledWith(mockUser);
  });

  it('should display loading message when user data is null (pending request)', () => {
    userSignal.set(undefined);
    const pendingRequest$ = new Subject<User>();
    mockUserService.me.mockReturnValue(pendingRequest$);

    fixture.detectChanges();

    const loadingElement = fixture.debugElement.query(By.css('.loading'));
    expect(loadingElement).toBeTruthy();
    expect(loadingElement.nativeElement.textContent).toContain(
      'Chargement de vos informations...'
    );
  });

  it('should handle error when fetching user data fails', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
    mockUserService.me.mockReturnValue(throwError(() => new Error('Oups')));

    fixture.detectChanges();

    expect(consoleSpy).toHaveBeenCalled();
    consoleSpy.mockRestore();
  });

  it('should display the subscription list when user has subscriptions', () => {
    userSignal.set(mockUser);
    mockUserService.me.mockReturnValue(of(mockUser));

    fixture.detectChanges();

    const items = fixture.debugElement.queryAll(By.css('.subscription-item'));

    expect(items.length).toBe(2);
    expect(items[0].nativeElement.textContent).toContain('Java');
    expect(items[1].nativeElement.textContent).toContain('Angular');
  });

  it('should call unsubscribe service when "Se désabonner" button is clicked', () => {
    userSignal.set(mockUser);
    mockUserService.me.mockReturnValue(of(mockUser));
    fixture.detectChanges();

    const unsubscribeBtn = fixture.debugElement.query(
      By.css('.unsubscribe-btn')
    );

    expect(unsubscribeBtn).toBeTruthy();
    unsubscribeBtn.nativeElement.click();

    expect(mockThemeService.unsubscribe).toHaveBeenCalledWith(10);
  });
});
