import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Router} from '@angular/router';
import {Token} from '../models/token.model';
import {AuthStatus} from '../models/auth-status.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authStatusSubject = new BehaviorSubject<AuthStatus>(AuthStatus.NOT_AUTHENTICATED);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {
    const token = this.loadTokenFromStorage();
    if (token) {
      const isExpired = this.isTokenExpired(token.expiresAt);
      this.authStatusSubject.next(isExpired ? AuthStatus.EXPIRED : AuthStatus.AUTHENTICATED);
      this.isAuthenticatedSubject.next(true)
    } else {
      this.authStatusSubject.next(AuthStatus.NOT_AUTHENTICATED);
      this.isAuthenticatedSubject.next(false)
    }
  }

  login(email: string, password: string): Observable<Token> {
    return this.http.post<Token>(`${this.apiUrl}/login`, { email, password }, { withCredentials: true }).pipe(
      tap((res: Token) => {
        this.setToken(res);
        this.setAuth(AuthStatus.AUTHENTICATED)
        this.router.navigate(['/']);
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  private isTokenExpired(expiresAt: Date): boolean {
    const currentDate = new Date();
    return currentDate > expiresAt;
  }

  checkAuthExpiration(): void {
    const token = this.loadTokenFromStorage();
    if (token) {
      const isExpired = this.isTokenExpired(token.expiresAt);
      this.authStatusSubject.next(isExpired ? AuthStatus.EXPIRED : AuthStatus.AUTHENTICATED);
    } else {
      this.authStatusSubject.next(AuthStatus.NOT_AUTHENTICATED);
    }
  }

  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.getValue();
  }

  // For interceptor
  getAuthStatus(): AuthStatus {
    return this.authStatusSubject.getValue();
  }

  getToken(): string | null {
    return localStorage.getItem('token') || null;
  }

  private loadTokenFromStorage(): Token | null {
    const token = localStorage.getItem('token');
    const expiresAt = localStorage.getItem('expiresAt');
    return token && expiresAt ? { accessToken: token, expiresAt: new Date(expiresAt) } : null;
  }

  private cleanToken(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresAt');
  }

  logout(): void {
    this.cleanToken();
    this.setAuth(AuthStatus.NOT_AUTHENTICATED)
    this.router.navigate(['/auth/login']);
  }

  private setToken(token: Token): void {
    localStorage.setItem('token', token.accessToken);
    localStorage.setItem('expiresAt', token.expiresAt.toString());
  }

  private setAuth(authStatus: AuthStatus) : void {
    this.authStatusSubject.next(authStatus);
    this.isAuthenticatedSubject.next(authStatus===AuthStatus.AUTHENTICATED)
  }

  refreshToken(): Observable<Token> {
    return this.http.post<Token>(`${this.apiUrl}/refresh`, {}, { withCredentials: true }).pipe(
      tap((newToken) => {
        this.setToken(newToken);
        this.setAuth(AuthStatus.AUTHENTICATED)
      })
    );
  }
}
