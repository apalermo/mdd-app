import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionService } from './core/services/session.service';
import { UserService } from './core/services/user.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  private readonly sessionService = inject(SessionService);
  private readonly userService = inject(UserService);

  public ngOnInit(): void {
    this.tryRestoreSession();
  }

  private tryRestoreSession(): void {
    if (this.sessionService.isLogged()) {
      this.userService.me().subscribe({
        next: (user) => {
          this.sessionService.updateUser(user);
        },
        error: () => {
          this.sessionService.logOut();
        },
      });
    }
  }
}
