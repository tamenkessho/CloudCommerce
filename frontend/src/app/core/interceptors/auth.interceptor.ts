import {inject} from '@angular/core';
import {HttpEvent, HttpHandlerFn, HttpRequest,} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {AuthStatus} from '../models/auth-status.model';

export const authInterceptor= (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const authService = inject(AuthService)
  const authStatus = authService.getAuthStatus()
  if (request.url.includes('/refresh')) {
    return next(request);
  }
  authService.checkAuthExpiration();

  if (authStatus === AuthStatus.AUTHENTICATED) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${authService.getToken()}`
      }
    });
  } else if (authStatus === AuthStatus.EXPIRED){
    return authService.refreshToken().pipe(
      switchMap((token) => {
        const refreshedRequest = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token.accessToken}`
          }
        });
        return next(refreshedRequest);
      }),
      catchError((error) => {
        authService.logout();
        return throwError(() => error);
      })
    );
  }
  return next(request);
}
