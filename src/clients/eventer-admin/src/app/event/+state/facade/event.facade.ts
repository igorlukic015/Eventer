import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Event, EventCreate} from "../../contracts/interfaces";
import {select, Store} from "@ngrx/store";
import * as eventFeature from "../reducers/event.reducers";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {eventActions} from "../actions/event.actions";
import {EventCategory} from "../../../event-category/contracts/interfaces";

@Injectable()
export class EventFacade {
  items$: Observable<Event[]> = this.store.pipe(select(eventFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(eventFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(eventFeature.selectIsLoading));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(eventFeature.selectPageRequest));
  selectedEventId$: Observable<number> = this.store.pipe(select(eventFeature.selectSelectedEventId));
  categories$: Observable<EventCategory[]> = this.store.pipe(select(eventFeature.selectCategories));

  constructor(private readonly store: Store) {
  }

  loadEvents() {
    this.store.dispatch(eventActions.getEvents());
  }

  loadCategories() {
    this.store.dispatch(eventActions.getCategories());
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

  createEvent(formData: FormData, data: EventCreate) {
    this.store.dispatch(eventActions.createEvent({formData, data}));
  }
  //
  updateEvent(formData: FormData) {
    this.store.dispatch(eventActions.updateEvent({formData}));
  }
  //
  updateSelectedEventId(id: number) {
    this.store.dispatch(eventActions.updateSelectedEventId({id}));
  }
}
