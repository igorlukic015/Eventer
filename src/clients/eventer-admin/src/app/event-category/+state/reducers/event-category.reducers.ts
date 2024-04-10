import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {EventCategory} from "../../contracts/interfaces";
import {createFeature, createReducer, on} from "@ngrx/store";
import {eventCategoryActions} from "../actions/event-category.actions";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {SortDirection} from "../../../shared/contracts/models";
import {defaultPageSize} from "../../../shared/contracts/statics";

const adapter: EntityAdapter<EventCategory> = createEntityAdapter<EventCategory>();

export interface EventCategoryState extends EntityState<EventCategory> {
  isLoading: boolean;
  // error: string | null;
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
}

const initialState: EventCategoryState = adapter.getInitialState({
  isLoading: false,
  // error: null,
  totalPages: 0,
  totalElements: 0,
  pageRequest: {
    page: 0,
    size: defaultPageSize,
    searchTerm: '',
    sort: {
      attributeNames: ['id'],
      sortDirection: SortDirection.ascending
    }
  },
})

const eventCategoryFeature = createFeature({
  name: 'eventCategory',
  reducer: createReducer(
    initialState,
    on(eventCategoryActions.getEventCategories, (state) => ({
      ...state, isLoading: true
    })),
    on(eventCategoryActions.getEventCategoriesSuccess, (state, {pagedResponse}) => (
      adapter.setAll(pagedResponse.content, {...state, isLoading: false, totalPages: pagedResponse.totalPages})
    )),
    on(eventCategoryActions.getEventCategoriesFail, (state, {error}) => ({
      ...state, isLoading: false
    })),
    on(eventCategoryActions.updatePageNumber, (state, {currentPage}) => ({
      ...state, pageRequest: {...state.pageRequest, page: currentPage}
    })),
    on(eventCategoryActions.updateSearchTerm, (state, {searchTerm}) => ({
      ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
    })),
    on(eventCategoryActions.deleteEventCategorySuccess, (state, {id}) => (
      adapter.removeOne(id, state)
    )),
    on(eventCategoryActions.createEventCategorySuccess, (state, {createdCategory}) => (
      adapter.addOne(createdCategory, state)
    ))
  )
});

export const {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal
} = adapter.getSelectors(eventCategoryFeature.selectEventCategoryState);

export const {
  name: eventCategoryFeatureKey,
  reducer: eventCategoryReducer,
  selectIsLoading,
  // selectError,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
} = eventCategoryFeature;
