import {Component, EventEmitter, Input, OnInit, Output, signal, WritableSignal} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {RealTimeService} from "../../services/real-time.service";
import {debounceTime, filter, Subject} from "rxjs";

@Component({
  selector: 'eventer-nav-bar',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent implements OnInit {
  @Input()
  showSearch: boolean = true;

  @Output()
  searchTermChanged: EventEmitter<string> = new EventEmitter<string>();

  private debouncer: Subject<string> = new Subject<string>();

  profileImage: WritableSignal<string> = signal("assets/default_avatar.png");

  constructor(private readonly router: Router,
              private readonly rts: RealTimeService
  ) {
    this.debouncer.pipe(
      debounceTime(300),
      filter(value => value?.length >= 2 || value.length == 0)
    ).subscribe(value => {
      this.searchTermChanged.emit(value);
    })
  }

  handleSearch($event: any) {
    this.debouncer.next($event.target.value);
  }

  handleLogoutClick($event: any){
    localStorage.removeItem('token');
    localStorage.removeItem('profileImageUrl')
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
