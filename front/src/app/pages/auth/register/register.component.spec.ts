import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../core/services/auth.service';
import { Router, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { RegisterRequest } from '../../../models/auth.interface';
import { SessionService } from '../../../core/services/session.service';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;

  const mockAuthService = { register: vi.fn() };
  const mockSessionService = { logIn: vi.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
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

  it('should call authService, log user automatically and navigate to /articles', () => {
    const navigateSpy = vi.spyOn(router, 'navigate');
    const validRequest: RegisterRequest = {
      email: 'new@test.com',
      name: 'NewUser',
      password: 'Password123!',
    };
    const mockResponse = { token: 'new-token-123' };
    component.registerForm.setValue(validRequest);

    mockAuthService.register.mockReturnValue(of(mockResponse));
    mockSessionService.logIn.mockReturnValue(
      of({ id: 1, email: 'new@test.com' }),
    );

    component.onSubmit();

    expect(mockAuthService.register).toHaveBeenCalledWith(validRequest);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockResponse.token);
    expect(navigateSpy).toHaveBeenCalledWith(['/articles']);
  });

  it('should have a link to login page', () => {
    const loginLink = fixture.debugElement.query(
      By.css('a[routerLink="/login"]'),
    );
    expect(loginLink).toBeTruthy();
  });
});
