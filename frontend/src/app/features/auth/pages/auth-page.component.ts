import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterOutlet} from '@angular/router';
import {filter} from 'rxjs';

@Component({
  selector: 'app-auth-page',
  templateUrl: './auth-page.component.html',
  styleUrls: ['./auth-page.component.scss'],
  imports: [
    RouterOutlet,
    RouterLink,
  ]
})
export class AuthPageComponent implements OnInit {
  currentRoute: string = ''
  constructor(private router: Router) { }
  ngOnInit() {
    this.currentRoute = this.router.url
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.currentRoute = event.url;
      });
  }
}
