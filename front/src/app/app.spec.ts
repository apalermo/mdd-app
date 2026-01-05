import { ComponentFixture, TestBed } from '@angular/core/testing';
import { App } from './app';
import { provideRouter } from '@angular/router';
import { SessionService } from './core/services/session.service';
import { UserService } from './core/services/user.service';
import { of, throwError } from 'rxjs';

describe('App', () => {
  let component: App;
  let fixture: ComponentFixture<App>;

  const mockSessionService = {
    isLogged: vi.fn(),
    updateUser: vi.fn(),
    logOut: vi.fn(),
  };

  const mockUserService = {
    me: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(App);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init if user is logged in', () => {
    mockSessionService.isLogged.mockReturnValue(true);
    const mockUser = { id: 1, email: 'test', name: 'test', subscriptions: [] };
    mockUserService.me.mockReturnValue(of(mockUser));

    fixture.detectChanges();

    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.updateUser).toHaveBeenCalledWith(mockUser);
  });

  it('should logout if user fetch fails (e.g. invalid token)', () => {
    mockSessionService.isLogged.mockReturnValue(true);
    mockUserService.me.mockReturnValue(throwError(() => new Error('401')));

    fixture.detectChanges();

    expect(mockSessionService.logOut).toHaveBeenCalled();
  });

  it('should NOT fetch user data if user is NOT logged in', () => {
    mockSessionService.isLogged.mockReturnValue(false);

    fixture.detectChanges();

    expect(mockUserService.me).not.toHaveBeenCalled();
  });
});
