import {Component, Input} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {eventCategoryUrlKey, eventUrlKey, userUrlKey} from "../../contracts/statics";
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
  @Input()
  public isAdmin: boolean = false;

  public activeLink: number;

  constructor(private readonly router: Router, private readonly rts: RealTimeService) {
    const key = this.router.url.split('/')[1];

    if (key === eventCategoryUrlKey) {
      this.activeLink = 0;
    } else if (key === eventUrlKey) {
      this.activeLink = 1;
    } else if (key === userUrlKey) {
      this.activeLink = 2;
    } else{
      this.activeLink = -1;
    }
  }


  handleLogoutClick($event: any){
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.rts.closeConnection();
    this.router.navigate(['login'])
  }
}
