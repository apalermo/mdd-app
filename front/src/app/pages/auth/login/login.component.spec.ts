import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';
import { SessionService } from '../../../core/services/session.service';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginRequest } from '../../../models/auth.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;

  const mockAuthService = { login: vi.fn() };
  const mockSessionService = { logIn: vi.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService, log user and navigate on success', () => {
    const navigateSpy = vi.spyOn(router, 'navigate');
    const validRequest: LoginRequest = {
      identifier: 'admin',
      password: 'password123',
    };
    const mockToken = 'jwt-token-xyz';

    component.loginForm.setValue(validRequest);
    mockAuthService.login.mockReturnValue(of({ token: mockToken }));

    component.onSubmit();

    expect(mockAuthService.login).toHaveBeenCalledWith(validRequest);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockToken);
    expect(component.errorMessage()).toBeUndefined();
    expect(navigateSpy).toHaveBeenCalledWith(['/me']);
  });

  it('should set error message on 401 Unauthorized', () => {
    const navigateSpy = vi.spyOn(router, 'navigate');
    component.loginForm.setValue({ identifier: 'wrong', password: 'wrong' });
    mockAuthService.login.mockReturnValue(throwError(() => ({ status: 401 })));

    component.onSubmit();

    expect(component.errorMessage()).toBe('Identifiants incorrects.');
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalled();
  });

  it('should set generic error message on server error (500)', () => {
    component.loginForm.setValue({ identifier: 'bug', password: 'bug' });
    mockAuthService.login.mockReturnValue(throwError(() => ({ status: 500 })));

    component.onSubmit();

    expect(component.errorMessage()).toBe(
      'Le serveur ne répond pas. Réessayez plus tard.'
    );
  });
});
