import {Component} from '@angular/core';
import {AuthService} from '../../../../core/services/auth.service';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-auth-controls',
  templateUrl: './auth-controls.component.html',
  styleUrls: ['./logout.component.scss'],
  imports: [
    AsyncPipe,
  ]
})
export class AuthControlsComponent {
  isAuthenticated$: Observable<boolean>;

  constructor(private authService: AuthService, private router: Router) {
    this.isAuthenticated$ = authService.isAuthenticated$
  }

  logout(): void {
    this.authService.logout()
  }

  navigateToLogin(): void {
    this.router.navigate(['/auth/login']);
  }
}
