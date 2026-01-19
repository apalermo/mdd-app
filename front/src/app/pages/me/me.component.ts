import { Component, inject, DestroyRef } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BehaviorSubject, switchMap, tap } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { NotificationService } from '../../core/services/notification.service';
import { UserUpdateRequest } from '../../models/user-update-request.interface';
import { ThemeCardComponent } from '../../shared/components/theme-card/theme-card.component';

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, ThemeCardComponent],
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent {
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);
  private readonly themeService = inject(ThemeService);
  private readonly notificationService = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly fb = inject(FormBuilder);

  public form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: [
      '',
      [
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
        ),
      ],
    ],
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
    }),
  );

  public submit(): void {
    if (this.form.valid) {
      const val = this.form.getRawValue();
      const request: UserUpdateRequest = {
        name: val.name,
        email: val.email,
        password: val.password || undefined,
      };

      this.userService.update(request).subscribe((updatedUser) => {
        this.notificationService.show(
          'Vos modifications ont été enregistrées !',
        );
        this.sessionService.updateUser(updatedUser);
      });
    }
  }

  public unsubscribe(themeId: number): void {
    this.themeService
      .unsubscribe(themeId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.refresh$.next();
        this.notificationService.show('Désabonnement pris en compte.');
      });
  }
}
