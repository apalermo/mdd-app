import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { unauthGuard } from './core/guards/unauth.guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [unauthGuard],
    loadComponent: () =>
      import('./pages/home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'login',
    canActivate: [unauthGuard],
    loadComponent: () =>
      import('./pages/auth/login/login.component').then(
        (m) => m.LoginComponent
      ),
  },
  {
    path: 'register',
    canActivate: [unauthGuard],
    loadComponent: () =>
      import('./pages/auth/register/register.component').then(
        (m) => m.RegisterComponent
      ),
  },
  {
    path: 'me',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/me/me.component').then((m) => m.MeComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
