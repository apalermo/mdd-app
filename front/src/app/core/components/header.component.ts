import { Component, inject, input, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { SessionService } from '../services/session.service';
interface NavigationLink {
  label: string;
  url: string;
}
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  public sessionService = inject(SessionService);
  public router = inject(Router);

  public readonly showNavigation = input<boolean>(true);

  public readonly navLinks: NavigationLink[] = [
    // { label: 'Articles', url: '/articles' }, // TODO: Décommenter quand la feature Articles sera prête
    // { label: 'Thèmes', url: '/themes' },  // TODO: Décommenter quand la feature Themes sera prête
  ];

  public isMobileMenuOpen = signal(false);

  public toggleMobileMenu(): void {
    this.isMobileMenuOpen.update((isOpen) => !isOpen);
  }

  public closeMenu(): void {
    this.isMobileMenuOpen.set(false);
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/']);
    this.closeMenu();
  }
}
