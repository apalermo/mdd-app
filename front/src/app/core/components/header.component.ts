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

  @HostListener('window:keydown', ['$event'])
  public handleKeyDown(event: KeyboardEvent): void {
    if (!this.isMobileMenuOpen() || event.key !== 'Tab') return;

    const element = this.navContainer()?.nativeElement;
    if (!element) return;

    const focusableElements = element.querySelectorAll<HTMLElement>(
      'a, button, [tabindex]:not([tabindex="-1"])'
    );
    const firstElement = focusableElements[0];
    const lastElement = focusableElements[focusableElements.length - 1];

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
