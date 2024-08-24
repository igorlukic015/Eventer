import {CommentData, EventCategory, EventData, ExtendedSearchPageRequest} from "../../contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {searchActions} from "../actions/search.actions";

export interface SearchState {
  totalPages: number;
  totalElements: number;
  pageRequest: ExtendedSearchPageRequest;
  selectedEventId: number;
  categories: EventCategory[];
  events: EventData[];
  comments: CommentData[];
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
    },
    categoryIds: [],
    weatherConditions: []
  },
  selectedEventId: 0,
  categories: [],
  events: [],
  comments: []
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
      ...state, events: [...state.events.map(e => (e.eventId === updatedEvent.eventId) ? updatedEvent : e)]
    })),
    on(searchActions.deleteEventCategorySuccess, (state, {id}) => ({
      ...state, totalElements: state.totalElements -1, categories: state.categories.filter(e => e.categoryId !== id)
    })),
    on(searchActions.createEventCategorySuccess, (state, {createdCategory}) => ({
      ...state, totalElements: state.totalElements +1, categories: [...state.categories, createdCategory]
    })),
    on(searchActions.updateEventCategorySuccess, (state, {updatedCategory}) => ({
      ...state, categories: [...state.categories.map((c) => c.categoryId === updatedCategory.categoryId ? updatedCategory : c)]
    })),
    on(searchActions.updateFilterCategories, (state, {categoryId, isAdding}) => ({
        ...state, pageRequest: {...state.pageRequest, categoryIds: isAdding ? [...state.pageRequest.categoryIds, categoryId] : state.pageRequest.categoryIds.filter(id => id !== categoryId)}
    })),
    on(searchActions.updateFilterConditions, (state, {condition, isAdding}) => ({
      ...state, pageRequest: {...state.pageRequest, weatherConditions: isAdding ? [...state.pageRequest.weatherConditions, condition] : state.pageRequest.weatherConditions.filter(con => con !== condition)}
    })),
    on(searchActions.createCommentSuccess, (state, {createdComment}) => ({
      ...state, comments: [createdComment, ...state.comments]
    })),
    on(searchActions.getCommentsSuccess, (state, {pagedResponse}) => ({
      ...state, comments: pagedResponse.content
    })),
    on(searchActions.updateCommentSuccess, (state, {updatedComment}) => ({
      ...state, comments: [...state.comments.map((c) => c.id === updatedComment.id ? updatedComment : c)]
    })),
    on(searchActions.deleteCommentSuccess, (state, {id}) => ({
      ...state, comments: state.comments.filter((c) => c.id !== id)
    }))
  )
})

export const {
  name: searchFeatureKey,
  reducer: searchReducer,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
  selectSelectedEventId,
  selectCategories,
  selectEvents,
  selectComments
} = searchFeature;
