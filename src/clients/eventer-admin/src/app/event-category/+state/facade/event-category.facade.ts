import {Observable} from "rxjs";
import {select, Store} from "@ngrx/store";
import {Injectable} from "@angular/core";
import {EventCategory} from "../../contracts/interfaces";
import {SortDirection} from "../../../shared/contracts/models";
import {PageRequest} from "../../../shared/contracts/interfaces";
import * as eventCategoryFeature from "../reducers/event-category.reducers";
import {eventCategoryActions} from "../actions/event-category.actions";

@Injectable()
export class EventCategoryFacade {
  items$: Observable<EventCategory[]> = this.store.pipe(select(eventCategoryFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(eventCategoryFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(eventCategoryFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(eventCategoryFeature.selectIsLoading));

  constructor(private readonly store: Store) {
  }

  loadEventCategories() {
    const pageRequest: PageRequest = {
      page: 0,
      size: 10,
      sort: {sortDirection: SortDirection.ascending, attributeNames: ['name']}
    }
    this.store.dispatch(eventCategoryActions.getEventCategories({pageRequest}));
  }
}
