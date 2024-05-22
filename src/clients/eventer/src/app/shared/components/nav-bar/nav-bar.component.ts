import {Component, EventEmitter, OnInit, Output, signal, WritableSignal} from '@angular/core';
import {Router} from "@angular/router";
import {RealTimeService} from "../../services/real-time.service";
import {debounceTime, Subject} from "rxjs";

@Component({
  selector: 'eventer-nav-bar',
  standalone: true,
  imports: [
  ],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent implements OnInit {
  @Output()
  searchTermChanged: EventEmitter<string> = new EventEmitter<string>();

  private debouncer: Subject<string> = new Subject<string>();

  profileImage: WritableSignal<string> = signal("https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg");

  constructor(private readonly router: Router,
              private readonly rts: RealTimeService
  ) {
    this.debouncer.pipe(debounceTime(300)).subscribe(value => {
      this.searchTermChanged.emit(value);
    })
  }

  handleSearch($event: any) {
    this.debouncer.next($event.target.value);
  }

  handleLogoutClick($event: any){
    localStorage.removeItem('token');
    this.rts.closeConnection();
    this.router.navigate(['login'])
  }

  ngOnInit() {
    const image = localStorage.getItem('profileImageUrl');

    if (image) {
      this.profileImage.set(image)
    }
  }
}
