import {Observable} from "rxjs";
import {select, Store} from "@ngrx/store";
import {Injectable} from "@angular/core";
import {EventCategory} from "../../contracts/interfaces";
import * as eventCategoryFeature from "../reducers/event-category.reducers";
import {eventCategoryActions} from "../actions/event-category.actions";
import {PageRequest} from "../../../shared/contracts/interfaces";

@Injectable()
export class EventCategoryFacade {
  items$: Observable<EventCategory[]> = this.store.pipe(select(eventCategoryFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(eventCategoryFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(eventCategoryFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(eventCategoryFeature.selectIsLoading));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(eventCategoryFeature.selectPageRequest));

  constructor(private readonly store: Store) {
  }

  loadEventCategories() {
    this.store.dispatch(eventCategoryActions.getEventCategories());
  }

  updatePageNumber(currentPage: number) {
    this.store.dispatch(eventCategoryActions.updatePageNumber({currentPage}));
  }

  updateSearchTerm(searchTerm: string) {
    this.store.dispatch(eventCategoryActions.updateSearchTerm({searchTerm}))
  }
}
