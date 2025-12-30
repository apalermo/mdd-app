import { Component, DestroyRef, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RegisterRequest } from '../../../models/auth.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly sessionService = inject(SessionService);

  public errorMessage = signal<string | undefined>(undefined);

  public registerForm = this.fb.group({
    name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]],
  });

  public onSubmit(): void {
    if (this.registerForm.valid) {
      const registerRequest = this.registerForm.value as RegisterRequest;

      this.authService
        .register(registerRequest)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (response) => {
            this.sessionService.logIn(response.token);
            this.errorMessage.set(undefined);
            this.router.navigate(['/me']);
          },
          error: (err) => {
            if (err.status === 400) {
              this.errorMessage.set(
                "Ce nom d'utilisateur ou cet e-mail est déjà utilisé."
              );
            } else {
              this.errorMessage.set(
                "Une erreur est survenue lors de l'inscription. Réessayez plus tard."
              );
            }
          },
        });
    }
  }
}
