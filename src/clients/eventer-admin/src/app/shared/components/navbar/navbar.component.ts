import {Component} from '@angular/core';
import {RouterLink, RouterOutlet} from "@angular/router";
import {NgClass} from "@angular/common";

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

  constructor() {
    this.activeLink = 0;
  }

  public onLinkClicked($event: MouseEvent, linkIndex: number) {
    this.activeLink = linkIndex;
  }
}
