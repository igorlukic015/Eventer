import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {Event} from "../../contracts/interfaces";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {eventActions} from "../actions/event.actions";
import {EventCategory} from "../../../event-category/contracts/interfaces";

const adapter: EntityAdapter<Event> = createEntityAdapter<Event>();

export interface EventState extends EntityState<Event> {
  isLoading: boolean;
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
  selectedEventId: number;
  categories: EventCategory[];
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
  selectedEventId: 0,
  categories: []
});


const eventFeature = createFeature({
  name: 'event',
  reducer: createReducer(
    initialState,
    on(eventActions.getEvents, (state) => ({
      ...state, isLoading: true
    })),
    on(eventActions.getEventsSuccess, (state, {pagedResponse}) => (
      adapter.setAll(pagedResponse.content.map((e: any) => ({...e, date: new Date(Date.parse(e.date))})),
        {...state, isLoading:false, totalPages: pagedResponse.totalPages})
    )),
    on(eventActions.getEventsFail, (state, {error}) => ({
      ...state, isLoading: false
    })),
    on(eventActions.updatePageNumber, (state, {currentPage}) => ({
      ...state, pageRequest: {...state.pageRequest, page: currentPage}
    })),
    on(eventActions.updateSearchTerm, (state, {searchTerm}) => ({
      ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
    })),
    on(eventActions.getCategoriesSuccess, (state, {pagedResponse}) => ({
      ...state, categories: pagedResponse.content
    })),
    on(eventActions.updateSelectedEventId, (state, {id}) => ({
      ...state, selectedEventId: id
    }))
    // on(rtsActions.updateEntity, (state, {payload}) => {
    //   if (payload.actionType === ActionType.created && payload.entityType === ListenedEntity.event) {
    //     return adapter.addOne(payload.data, state);
    //   }
    //   else if (payload.actionType === ActionType.updated && payload.entityType === ListenedEntity.event) {
    //     return adapter.setOne(payload.data, state);
    //   }
    //   else if (payload.actionType === ActionType.deleted && payload.entityType === ListenedEntity.event) {
    //     return adapter.removeOne(payload.data, state);
    //   }
    //
    //   return ({...state});
    // })
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
  selectSelectedEventId,
  selectCategories
} = eventFeature;
