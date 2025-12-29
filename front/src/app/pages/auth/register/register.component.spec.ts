import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../core/services/auth.service';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../../models/auth.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;

  const mockAuthService = { register: vi.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: mockAuthService },
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

  it('should call authService and navigate to login on success', () => {
    const navigateSpy = vi.spyOn(router, 'navigate');
    const validRequest: RegisterRequest = {
      email: 'new@test.com',
      name: 'NewUser',
      password: 'password123',
    };

    component.registerForm.setValue(validRequest);
    mockAuthService.register.mockReturnValue(of({}));

    component.onSubmit();

    expect(mockAuthService.register).toHaveBeenCalledWith(validRequest);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    expect(component.errorMessage()).toBeUndefined();
  });

  it('should display duplicate error on 400 Bad Request', () => {
    component.registerForm.setValue({
      email: 'taken@test.com',
      name: 'Taken',
      password: '123',
    });
    mockAuthService.register.mockReturnValue(
      throwError(() => ({ status: 400 }))
    );

    component.onSubmit();

    expect(component.errorMessage()).toBe(
      "Ce nom d'utilisateur ou cet e-mail est déjà utilisé."
    );
  });

  it('should display generic error on other failures', () => {
    component.registerForm.setValue({
      email: 'bug@test.com',
      name: 'Bug',
      password: '123',
    });
    mockAuthService.register.mockReturnValue(
      throwError(() => ({ status: 500 }))
    );

    component.onSubmit();

    expect(component.errorMessage()).toBe(
      "Une erreur est survenue lors de l'inscription. Réessayez plus tard."
    );
  });
});
