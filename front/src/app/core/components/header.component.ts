import {
  Component,
  inject,
  input,
  signal,
  viewChild,
  ElementRef,
  HostListener,
} from '@angular/core';
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

  private burgerButton = viewChild<ElementRef<HTMLButtonElement>>('burgerBtn');
  private navContainer = viewChild<ElementRef<HTMLElement>>('navContainer');

  public readonly showNavigation = input<boolean>(true);
  public readonly navLinks: NavigationLink[] = [
    { label: 'Articles', url: '/articles' },
    { label: 'Thèmes', url: '/themes' },
  ];

  public isMobileMenuOpen = signal(false);

  /**
   * Enhanced A11Y: Implements Focus Trap and Escape key support for mobile navigation
   */
  @HostListener('window:keydown', ['$event'])
  public handleKeyDown(event: KeyboardEvent): void {
    if (!this.isMobileMenuOpen()) return;

    if (event.key === 'Escape') {
      this.closeMenu();
      return;
    }

    if (event.key !== 'Tab') return;

    const navElement = this.navContainer()?.nativeElement;
    const burgerElement = this.burgerButton()?.nativeElement;
    if (!navElement || !burgerElement) return;

    const focusableInNav = Array.from(
      navElement.querySelectorAll<HTMLElement>(
        'a, button, [tabindex]:not([tabindex="-1"])'
      )
    );

    // Include burger toggle in the cycle for full keyboard control
    const allFocusable = [burgerElement, ...focusableInNav];
    const firstElement = allFocusable[0];
    const lastElement = allFocusable[allFocusable.length - 1];

    if (event.shiftKey) {
      if (document.activeElement === firstElement) {
        lastElement.focus();
        event.preventDefault();
      }
    } else {
      if (document.activeElement === lastElement) {
        firstElement.focus();
        event.preventDefault();
      }
    }
  }

  public toggleMobileMenu(): void {
    this.isMobileMenuOpen.update((isOpen) => !isOpen);

    if (this.isMobileMenuOpen()) {
      setTimeout(() => {
        const firstLink = this.navContainer()?.nativeElement.querySelector('a');
        firstLink?.focus();
      }, 100);
    } else {
      this.burgerButton()?.nativeElement.focus();
    }
  }

  public closeMenu(): void {
    if (this.isMobileMenuOpen()) {
      this.isMobileMenuOpen.set(false);
      this.burgerButton()?.nativeElement.focus();
    }
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/']);
    this.closeMenu();
  }
}
