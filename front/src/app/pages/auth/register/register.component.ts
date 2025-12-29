import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { RegisterRequest } from '../../../models/auth.interface';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  public errorMessage = signal<string | undefined>(undefined);

  public registerForm = this.fb.group({
    name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]],
  });

  public onSubmit(): void {
    if (this.registerForm.valid) {
      const registerRequest = this.registerForm.value as RegisterRequest;

      this.authService.register(registerRequest).subscribe({
        next: () => {
          this.errorMessage.set(undefined);
          this.router.navigate(['/login']);
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
