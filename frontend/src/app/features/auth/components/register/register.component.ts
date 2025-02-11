import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../../core/services/auth.service';
import {Router} from '@angular/router';
import {passwordMatchValidator} from '../../../../shared/validators/password-match.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  imports: [
    ReactiveFormsModule
  ]
})
export class RegisterComponent {
  registerForm: FormGroup;
  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required, Validators.minLength(4)]],
      lastName: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validators: passwordMatchValidator });
  }

  public onSubmit() {
    if (this.registerForm.valid) {
      const { email, firstName, lastName, password } = this.registerForm.value;
      this.authService.register({email, firstName, lastName, password}).subscribe({
        next: () => this.router.navigate(['/auth/login']),
        error: (err:any) => console.error('Register error:', err)
      });
    }
  }
}
