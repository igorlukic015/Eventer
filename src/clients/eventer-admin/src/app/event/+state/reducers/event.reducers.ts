import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {IEvent} from "../../contracts/interfaces";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {eventActions} from "../actions/event.actions";


const adapter: EntityAdapter<IEvent> = createEntityAdapter<IEvent>();

export interface EventState extends EntityState<IEvent> {
  isLoading: boolean;
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
  selectedEventId: number;
}

const initialState: EventState = adapter.getInitialState({
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
  selectedEventId: 0
});


const eventFeature = createFeature({
  name: 'event',
  reducer: createReducer(
    initialState,
    on(eventActions.getEvents, (state) => ({
      ...state, isLoading: true
    })),
    on(eventActions.getEventsSuccess, (state, {pagedResponse}) => (
      adapter.setAll(pagedResponse.content, {...state, isLoading:false, totalPages: pagedResponse.totalPages})
    )),
    on(eventActions.getEventsFail, (state, {error}) => ({
      ...state, isLoading: false
    })),
    on(eventActions.updatePageNumber, (state, {currentPage}) => ({
      ...state, pageRequest: {...state.pageRequest, page: currentPage}
    })),
    on(eventActions.updateSearchTerm, (state, {searchTerm}) => ({
      ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
    }))
  )
})

export const {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal
} = adapter.getSelectors(eventFeature.selectEventState);

export const {
  name: eventFeatureKey,
  reducer: eventReducer,
  selectIsLoading,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
  selectSelectedEventId
} = eventFeature;
