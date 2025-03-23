import {Component, OnInit} from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {ActionBarComponent} from "../../../shared/components/action-bar/action-bar.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {EventFacade} from "../../+state/facade/event.facade";
import {takeUntil} from "rxjs";
import {EventListComponent} from "../event-list/event-list.component";

@Component({
  selector: 'eventer-admin-event-main',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavBarComponent,
    ActionBarComponent,
    EventListComponent
  ],
  providers: [EventFacade],
  templateUrl: './event-main.component.html',
  styleUrl: './event-main.component.css'
})
export class EventMainComponent extends DestroyableComponent implements OnInit {
  constructor(private readonly eventFacade: EventFacade) {
    super();
  }

  onSearchTermChanged(searchTerm: string) {
    this.eventFacade.updateSearchTerm(searchTerm);
  }

  ngOnInit(): void {
    this.eventFacade.pageRequest$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => {
        this.eventFacade.loadEvents();
      });
  }
}
