import {Component} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {NgClass} from "@angular/common";
import {eventCategoryUrlKey, eventUrlKey} from "../../contracts/statics";

@Component({
  selector: 'eventer-admin-navbar',
  standalone: true,
  imports: [
    RouterLink,
    RouterOutlet,
    NgClass
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  public activeLink: number;

  constructor(private readonly router: Router) {
    const key = this.router.url.split('/')[1];

    this.activeLink =
      key === eventCategoryUrlKey ? 0 : key === eventUrlKey ? 1 : -1;
  }
}
