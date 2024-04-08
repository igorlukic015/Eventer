import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {EventCategory} from "../../contracts/interfaces";
import {createFeature, createReducer, on} from "@ngrx/store";
import {eventCategoryActions} from "../actions/event-category.actions";

const adapter: EntityAdapter<EventCategory> = createEntityAdapter<EventCategory>();

export interface EventCategoryState extends EntityState<EventCategory> {
  isLoading: boolean;
  error: string | null;
  totalPages: number;
  totalElements: number;
}

const initialState: EventCategoryState = adapter.getInitialState({
  isLoading: false,
  error: null,
  totalPages: 0,
  totalElements: 0
})

const eventCategoryFeature = createFeature({
  name: 'eventCategory',
  reducer: createReducer(
    initialState,
    on(eventCategoryActions.getEventCategories, (state) => ({
        ...state, isLoading: true
    })),
    on(eventCategoryActions.getEventCategoriesSuccess, (state, {pagedResponse}) => (
      adapter.setMany(pagedResponse.content, {...state, isLoading: false})
    )),
    on(eventCategoryActions.getEventCategoriesFail, (state, {error}) => ({
      ...state, isLoading: false, error: error
    }))
  )
});

export const {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal} = adapter.getSelectors(eventCategoryFeature.selectEventCategoryState);

export const {
  name: eventCategoryFeatureKey,
  reducer: eventCategoryReducer,
  selectIsLoading,
  selectError,
  selectTotalPages,
  selectTotalElements
} = eventCategoryFeature;
