import {PageRequest} from "../../../shared/contracts/interfaces";
import {EventCategory, EventData} from "../../contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {searchActions} from "../actions/search.actions";

export interface SearchState {
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
  selectedEventId: number;
  categories: EventCategory[];
  events: EventData[];
}

const initialState: SearchState = {
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
  selectedEventId: 0,
  categories: [],
  events: []
}

const searchFeature = createFeature({
  name: 'search',
  reducer: createReducer(
    initialState,
    on(searchActions.getEvents, (state) => ({
      ...state
    })),
    on(searchActions.getEventsSuccess, (state, {pagedResponse}) => ({
      ...state,
      events: pagedResponse.content.map((e: any) => ({...e, date: new Date(Date.parse(e.date))})),
      totalPages: pagedResponse.totalPages,
      totalElements: pagedResponse.totalElements
    })),
    on(searchActions.getEventsFail, (state, {error}) => ({
      ...state, isLoading: false
    })),
    on(searchActions.updatePageNumber, (state, {currentPage}) => ({
      ...state, pageRequest: {...state.pageRequest, page: currentPage}
    })),
    on(searchActions.updateSearchTerm, (state, {searchTerm}) => ({
      ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
    })),
    on(searchActions.getEventCategoriesSuccess, (state, {categories}) => ({
      ...state, categories: categories
    })),
    on(searchActions.updateSelectedEventId, (state, {id}) => ({
      ...state, selectedEventId: id
    })),
    on(searchActions.deleteEventSuccess, (state, {id}) => ({
      ...state, totalElements: state.totalElements -1, events: state.events.filter(e => e.eventId !== id)
    })),
    on(searchActions.createEventSuccess, (state, {createdEvent}) => ({
      ...state, totalElements: state.totalElements +1, events: [...state.events, createdEvent]
    })),
    on(searchActions.updateEventSuccess, (state, {updatedEvent}) => ({
      ...state, events: [...state.events.filter((e) => e.eventId !== updatedEvent.eventId), updatedEvent]
    })),
    on(searchActions.deleteEventCategorySuccess, (state, {id}) => ({
      ...state, totalElements: state.totalElements -1, categories: state.categories.filter(e => e.categoryId !== id)
    })),
    on(searchActions.createEventCategorySuccess, (state, {createdCategory}) => ({
      ...state, totalElements: state.totalElements +1, categories: [...state.categories, createdCategory]
    })),
    on(searchActions.updateEventCategorySuccess, (state, {updatedCategory}) => ({
      ...state, categories: [...state.categories.filter((c) => c.categoryId !== updatedCategory.categoryId), updatedCategory]
    }))
  )
});

export const {
  name: searchFeatureKey,
  reducer: searchReducer,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
  selectSelectedEventId,
  selectCategories,
  selectEvents
} = searchFeature;
