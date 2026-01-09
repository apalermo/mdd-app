import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
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

  public readonly registerForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]],
  });

  public onSubmit(): void {
    if (this.registerForm.valid) {
      const registerRequest = this.registerForm.getRawValue();

      this.authService
        .register(registerRequest)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((response) => {
          this.sessionService.logIn(response.token);
          this.router.navigate(['/articles']);
        });
    }
  }
}
