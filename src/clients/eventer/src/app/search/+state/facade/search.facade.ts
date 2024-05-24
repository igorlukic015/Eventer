import {Observable} from "rxjs";
import {EventCategory, EventData} from "../../contracts/interfaces";
import {select, Store} from "@ngrx/store";
import {Injectable} from "@angular/core";
import * as searchFeature from "../reducers/search.reducers"
import {PageRequest} from "../../../shared/contracts/interfaces";
import {searchActions} from "../actions/search.actions";

@Injectable()
export class SearchFacade {
  events$: Observable<EventData[]> = this.store.pipe(select(searchFeature.selectEvents));
  categories$: Observable<EventCategory[]> = this.store.pipe(select(searchFeature.selectCategories));
  totalPages$: Observable<number> = this.store.pipe(select(searchFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(searchFeature.selectTotalElements));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(searchFeature.selectPageRequest));
  selectedEventId$: Observable<number> = this.store.pipe(select(searchFeature.selectSelectedEventId));

  constructor(private readonly store: Store) {
  }

  loadEvents() {
    this.store.dispatch(searchActions.getEvents());
  }

  loadCategories() {
    this.store.dispatch(searchActions.getEventCategories());
  }

  updatePageNumber(currentPage: number) {
    this.store.dispatch(searchActions.updatePageNumber({currentPage}));
  }

  updateSearchTerm(searchTerm: string) {
    this.store.dispatch(searchActions.updateSearchTerm({searchTerm}))
  }

  updateSelectedEventId(id: number) {
    this.store.dispatch(searchActions.updateSelectedEventId({id}));
  }

  updateFilterCategories(categoryId: number, isAdding: boolean) {
    this.store.dispatch(searchActions.updateFilterCategories({categoryId, isAdding}))
  }

  updateFilterConditions(condition: string, isAdding: boolean) {
    this.store.dispatch(searchActions.updateFilterConditions({condition, isAdding}))
  }
}
