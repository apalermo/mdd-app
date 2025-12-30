import { Component, DestroyRef, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { SessionService } from '../../../core/services/session.service';
import { LoginRequest } from '../../../models/auth.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  public errorMessage = signal<string | undefined>(undefined);

  public loginForm = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(3)]],
  });

  public onSubmit(): void {
    if (this.loginForm.valid) {
      const loginRequest = this.loginForm.value as LoginRequest;

      this.authService
        .login(loginRequest)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (response) => {
            this.sessionService.logIn(response.token);

            this.errorMessage.set(undefined);

            this.router.navigate(['/me']); // TODO: Redirect to /articles once the feature is implemented
          },
          error: (err) => {
            if (err.status === 401) {
              this.errorMessage.set('Identifiants incorrects.');
            } else {
              this.errorMessage.set(
                'Le serveur ne répond pas. Réessayez plus tard.'
              );
            }
          },
        });
    }
  }
}
