import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {Router} from "@angular/router";
import {EventFacade} from "../../+state/facade/event.facade";
import {takeUntil, withLatestFrom} from "rxjs";
import {Event} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-event-list',
  standalone: true,
  imports: [
      TablePaginatorComponent
  ],
  templateUrl: './event-list.component.html',
  styleUrl: './event-list.component.css'
})
export class EventListComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
  public events: WritableSignal<Event[]> = signal([]);
  public checkedRow: WritableSignal<number> = signal(0);

  constructor(private readonly router: Router,
              private readonly eventFacade: EventFacade) {
    super();
  }

  onDeleteClicked($event: void){
    if (this.checkedRow() !== 0) {
      this.eventFacade.deleteEvent(this.checkedRow());
    }
  }

  openUpdate($event: any, eventId: number) {
    this.eventFacade.updateSelectedEventId(eventId);
    this.router.navigate(['event',  'update']);
  }

  pageChanged(currentPage: number): void {
    this.eventFacade.updatePageNumber(currentPage);
  }

  handleCheckClick($event: any, eventId: number) {
    if (this.checkedRow() === eventId) {
      this.checkedRow.set(0);
      return;
    }

    this.checkedRow.set(eventId);
  }

  ngOnInit() {
    this.eventFacade.items$.pipe(
      withLatestFrom(this.eventFacade.totalPages$),
      takeUntil(this.destroyed$)
    ).subscribe(([items, total]) => {
      this.events.set(items);
      this.totalPages.set(total);
    })
  }
}
