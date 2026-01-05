import { Component, inject, DestroyRef, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BehaviorSubject, switchMap, tap } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { UserUpdateRequest } from '../../models/user-update-request.interface';

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [RouterLink, AsyncPipe, ReactiveFormsModule],
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent {
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);
  private readonly themeService = inject(ThemeService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly fb = inject(FormBuilder);

  public successMsg = signal<string | null>(null);
  public errorMsg = signal<string | null>(null);

  public form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.minLength(8)]],
  });

  private refresh$ = new BehaviorSubject<void>(void 0);

  public user$ = this.refresh$.pipe(
    switchMap(() => this.userService.me()),
    tap((user) => {
      this.sessionService.updateUser(user);
      this.form.patchValue({
        name: user.name,
        email: user.email,
        password: '',
      });
    })
  );

  public submit(): void {
    this.successMsg.set(null);
    this.errorMsg.set(null);

    if (this.form.invalid) {
      this.errorMsg.set('Veuillez vérifier les champs du formulaire.');
      return;
    }

    const val = this.form.getRawValue();
    const request: UserUpdateRequest = {
      name: val.name,
      email: val.email,
      password: val.password || undefined,
    };

    this.userService.update(request).subscribe({
      next: (updatedUser) => {
        this.successMsg.set('Vos modifications ont été enregistrées !');
        this.sessionService.updateUser(updatedUser);
      },
      error: () => {
        this.errorMsg.set('Une erreur est survenue lors de la sauvegarde.');
      },
    });
  }

  public unsubscribe(themeId: number): void {
    this.themeService
      .unsubscribe(themeId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.refresh$.next();
          this.successMsg.set('Désabonnement pris en compte.');
        },
        error: (err) => console.error('Erreur', err),
      });
  }
}
