import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { SessionService } from '../../../core/services/session.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';

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

  public readonly loginForm = this.fb.nonNullable.group({
    identifier: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(3)]],
  });

  public onSubmit(): void {
    if (this.loginForm.valid) {
      const loginRequest = this.loginForm.getRawValue();

      this.authService
        .login(loginRequest)
        .pipe(
          switchMap((response) => this.sessionService.logIn(response.token)),
          takeUntilDestroyed(this.destroyRef)
        )
        .subscribe(() => {
          this.router.navigate(['/articles']);
        });
    }
  }
}
