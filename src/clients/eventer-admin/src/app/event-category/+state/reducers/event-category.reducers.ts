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
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
  selectedCategoryId: number;
}

const initialState: EventCategoryState = adapter.getInitialState({
  isLoading: false,
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
  selectedCategoryId: 0
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
    on(eventCategoryActions.createEventCategorySuccess, (state, {createdCategory}) => (
      adapter.addOne(createdCategory, state)
    )),
    on(eventCategoryActions.updateEventCategorySuccess, (state, {updatedCategory}) => (
      adapter.setOne(updatedCategory, state)
    )),
    on(eventCategoryActions.deleteEventCategorySuccess, (state, {id}) => (
      adapter.removeOne(id, state)
    )),
    on(eventCategoryActions.updateSelectedCategoryId, (state, {id}) => ({
      ...state, selectedCategoryId: id
    }))
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
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
  selectSelectedCategoryId
} = eventCategoryFeature;
