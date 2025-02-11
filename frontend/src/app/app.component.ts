import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {AuthControlsComponent} from './features/auth/components/auth-controls/auth-controls.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthControlsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Cloud Commerce';
}
