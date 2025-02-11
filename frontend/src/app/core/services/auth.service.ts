import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Router} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authStatus = new BehaviorSubject<boolean>(false);
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus()
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, {email, password}).pipe(
      tap((res: any) => {
        this.setToken(res.accessToken);
        this.authStatus.next(true);
        this.router.navigate(['/']);
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  getAuthStatus(): Observable<boolean> {
    return this.authStatus.asObservable();
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    this.authStatus.next(false);
    this.router.navigate(['/auth/login']);
  }

  private setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  private checkAuthStatus(): void {
    const token = this.getToken();
    this.authStatus.next(!!token);
  }
}
