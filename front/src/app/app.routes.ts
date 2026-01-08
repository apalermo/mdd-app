import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { unauthGuard } from './core/guards/unauth.guard';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './core/layouts/auth-layout/auth-layout.component';

export const routes: Routes = [
  {
    path: '',
    canActivate: [unauthGuard],
    loadComponent: () =>
      import('./pages/home/home.component').then((m) => m.HomeComponent),
  },

  {
    path: '',
    component: AuthLayoutComponent,
    canActivate: [unauthGuard],
    children: [
      {
        path: 'login',
        loadComponent: () =>
          import('./pages/auth/login/login.component').then(
            (m) => m.LoginComponent
          ),
      },
      {
        path: 'register',
        loadComponent: () =>
          import('./pages/auth/register/register.component').then(
            (m) => m.RegisterComponent
          ),
      },
    ],
  },

  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'articles',
        children: [
          {
            path: '',
            loadComponent: () =>
              import('./pages/articles/articles.component').then(
                (m) => m.ArticlesComponent
              ),
          },
          {
            path: 'create',
            loadComponent: () =>
              import(
                './pages/articles/article-create/article-create.component'
              ).then((m) => m.ArticleCreateComponent),
          },
          {
            path: ':id',
            loadComponent: () =>
              import(
                './pages/articles/article-detail/article-detail.component'
              ).then((m) => m.ArticleDetailComponent),
          },
        ],
      },
      {
        path: 'me',
        loadComponent: () =>
          import('./pages/me/me.component').then((m) => m.MeComponent),
      },
      {
        path: 'themes',
        loadComponent: () =>
          import('./pages/themes/themes.component').then(
            (m) => m.ThemesComponent
          ),
      },
    ],
  },

  {
    path: '**',
    redirectTo: '',
  },
];
