import { Component, OnInit, inject } from '@angular/core';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);

  // On expose le signal du service directement au template
  public user = this.sessionService.user;

  ngOnInit(): void {
    // L'appel passera par l'intercepteur qui injectera le Token
    this.userService.me().subscribe({
      next: (userData) => {
        // Mise à jour du signal dans le service de session
        this.sessionService.updateUser(userData);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération du profil', err);
      },
    });
  }
}
