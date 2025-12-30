import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { SessionService } from '../services/session.service';

export const unauthGuard: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  if (sessionService.isLogged()) {
    router.navigate(['/me']); // TODO: Redirect to /articles once the feature is implemented
    return false;
  }

  return true;
};
