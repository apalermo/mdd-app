import { Component, inject, DestroyRef } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ThemeService } from '../../core/services/theme.service';
import { SessionService } from '../../core/services/session.service';
import { UserService } from '../../core/services/user.service';
import { Theme } from '../../models/theme.interface';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-themes',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss'],
})
export class ThemesComponent {
  private readonly themeService = inject(ThemeService);
  private readonly sessionService = inject(SessionService);
  private readonly userService = inject(UserService);
  private readonly destroyRef = inject(DestroyRef);

  public themes$: Observable<Theme[]> = this.themeService.getThemes();

  public isSubscribed(themeId: number): boolean {
    const user = this.sessionService.user();
    if (!user || !user.subscriptions) {
      return false;
    }
    return user.subscriptions.some((sub) => sub.id === themeId);
  }

  public subscribe(themeId: number): void {
    this.themeService
      .subscribeToTheme(themeId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.refreshSession();
        },
        error: (err) => {
          console.error("Erreur lors de l'abonnement:", err);
        },
      });
  }

  private refreshSession(): void {
    this.userService
      .me()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (updatedUser) => this.sessionService.updateUser(updatedUser),
        error: (err) =>
          console.error('Impossible de rafraîchir le profil', err),
      });
  }
}
