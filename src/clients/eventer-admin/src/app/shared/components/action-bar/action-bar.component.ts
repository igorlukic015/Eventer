import {Component, EventEmitter, Output} from '@angular/core';
import {debounceTime, Subject} from "rxjs";

@Component({
  selector: 'eventer-admin-action-bar',
  standalone: true,
  imports: [],
  templateUrl: './action-bar.component.html',
  styleUrl: './action-bar.component.css'
})
export class ActionBarComponent {

  @Output()
  searchTermChanged: EventEmitter<string> = new EventEmitter<string>();

  private debouncer: Subject<string> = new Subject<string>();

  constructor() {
    this.debouncer.pipe(debounceTime(300)).subscribe(value => {
      this.searchTermChanged.emit(value);
    })
  }

  handleInput($event: any) {
    this.debouncer.next($event.target.value);
  }
}
