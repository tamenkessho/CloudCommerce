import {Routes} from '@angular/router';
import {authGuard} from './core/guards/auth.guard';
import {redirectIfAuthenticatedGuard} from './core/guards/redirect-if-authenticated.guard';

export const routes: Routes = [
  { path: '',
    canActivate: [authGuard],
    loadChildren: () => import('./features/products/product.routes')
      .then(m => m.routes) },
  { path: 'auth',
    canActivate: [redirectIfAuthenticatedGuard],
    loadChildren: () => import('./features/auth/auth.routes')
      .then(a => a.routes) },
];
