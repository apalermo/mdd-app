import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { SessionService } from '../services/session.service';

export const unauthGuard: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  if (sessionService.isLogged()) {
    router.navigate(['/me']); // penser à rediriger vers la page des articles quand elle sera créée
    return false;
  }

  return true;
};
