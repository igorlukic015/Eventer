import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Event} from "../../contracts/interfaces";
import {select, Store} from "@ngrx/store";
import * as eventFeature from "../reducers/event.reducers";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {eventActions} from "../actions/event.actions";
import {subscribeToChanges} from "../../../shared/+state/actions/rts.actions";

@Injectable()
export class EventFacade {
  items$: Observable<Event[]> = this.store.pipe(select(eventFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(eventFeature.selectIsLoading));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(eventFeature.selectPageRequest));
  selectedCategoryId$: Observable<number> = this.store.pipe(select(eventFeature.selectSelectedEventId));

  constructor(private readonly store: Store) {
  }

  subscribeToChanges() {
    this.store.dispatch(subscribeToChanges());
  }

  loadEvents() {
    this.store.dispatch(eventActions.getEvents());
  }

  updatePageNumber(currentPage: number) {
    this.store.dispatch(eventActions.updatePageNumber({currentPage}));
  }

  updateSearchTerm(searchTerm: string) {
    this.store.dispatch(eventActions.updateSearchTerm({searchTerm}))
  }

  deleteEvent(id: number) {
    // this.store.dispatch(eventActions.deleteEvent({id}));
  }

  // createEvent(newEvent: IEventCreate) {
  //   this.store.dispatch(eventActions.createEvent({newEvent}));
  // }
  //
  // updateEvent(updatedEvent: IEvent) {
  //   this.store.dispatch(eventActions.updateEvent({updatedEvent}));
  // }
  //
  updateSelectedEventId(id: number) {
    // this.store.dispatch(eventActions.updateSelectedEventId({id}));
  }
}
