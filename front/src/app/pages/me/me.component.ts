import { Component, OnInit, inject, DestroyRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);
  private readonly destroyRef = inject(DestroyRef);

  public user = this.sessionService.user;

  ngOnInit(): void {
    this.userService
      .me()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (userData) => {
          this.sessionService.updateUser(userData);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération du profil', err);
        },
      });
  }
}
