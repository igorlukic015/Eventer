import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'eventer-nav-bar',
  standalone: true,
  imports: [],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent implements OnInit {
  profileImage: WritableSignal<string> = signal("https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg");

  constructor(private readonly router: Router,
              // private readonly rts: RealTimeService
  ) {
  }


  handleLogoutClick($event: any){
    localStorage.removeItem('token');
    // this.rts.closeConnection();
    this.router.navigate(['login'])
  }

  ngOnInit() {
    const image = localStorage.getItem('profileImageUrl');

    if (image) {
      this.profileImage.set(image)
    }
  }
}
