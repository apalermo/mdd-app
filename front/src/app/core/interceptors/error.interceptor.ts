import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject, isDevMode } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

/**
 * Global error interceptor.
 * Catches HTTP errors to provide technical console logs and
 * triggers user notifications via the NotificationService.
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (isDevMode()) {
        console.error(`[Front Log] Error at ${req.url}:`, error);
      }

      let errorMessage = 'Une erreur inattendue est survenue.';

      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      }

      notificationService.show(errorMessage);

      return throwError(() => error);
    })
  );
};
