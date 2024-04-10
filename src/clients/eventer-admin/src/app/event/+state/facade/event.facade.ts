import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {IEvent} from "../../contracts/interfaces";
import {select, Store} from "@ngrx/store";
import * as eventFeature from "../reducers/event.reducers";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {eventActions} from "../actions/event.actions";

@Injectable()
export class EventFacade {
  items$: Observable<IEvent[]> = this.store.pipe(select(eventFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(eventFeature.selectIsLoading));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(eventFeature.selectPageRequest));
  selectedCategoryId$: Observable<number> = this.store.pipe(select(eventFeature.selectSelectedEventId));

  constructor(private readonly store: Store) {
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
