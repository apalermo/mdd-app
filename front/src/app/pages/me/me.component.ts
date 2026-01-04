import { Component, inject, DestroyRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BehaviorSubject, switchMap, tap } from 'rxjs';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [RouterLink, AsyncPipe],
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent {
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);
  private readonly themeService = inject(ThemeService);
  private readonly destroyRef = inject(DestroyRef);

  private refresh$ = new BehaviorSubject<void>(void 0);

  public user$ = this.refresh$.pipe(
    switchMap(() => this.userService.me()),
    tap((user) => this.sessionService.updateUser(user))
  );

  public unsubscribe(themeId: number): void {
    this.themeService
      .unsubscribe(themeId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.refresh$.next();
        },
        error: (err) => {
          console.error('Erreur lors du désabonnement', err);
        },
      });
  }
}
