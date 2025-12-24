import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  private readonly router = inject(Router);

  public navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  public navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}
