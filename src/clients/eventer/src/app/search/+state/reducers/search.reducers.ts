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
    on(searchActions.getEventCategoriesSuccess, (state, {pagedResponse}) => ({
      ...state, categories: pagedResponse.content
    })),
    on(searchActions.updateSelectedEventId, (state, {id}) => ({
      ...state, selectedEventId: id
    })),
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
