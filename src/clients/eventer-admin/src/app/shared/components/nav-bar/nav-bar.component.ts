import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {eventCategoryUrlKey, eventUrlKey} from "../../contracts/statics";
import {NgClass} from "@angular/common";
import {RealTimeService} from "../../services/real-time.service";

@Component({
  selector: 'eventer-admin-nav-bar',
  standalone: true,
  imports: [
    NgClass,
    RouterLink
  ],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  public activeLink: number;

  constructor(private readonly router: Router, private readonly rts: RealTimeService) {
    const key = this.router.url.split('/')[1];

    this.activeLink =
      key === eventCategoryUrlKey ? 0 : key === eventUrlKey ? 1 : -1;
  }


  handleLogoutClick($event: any){
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.rts.closeConnection();
    this.router.navigate(['login'])
  }
}
