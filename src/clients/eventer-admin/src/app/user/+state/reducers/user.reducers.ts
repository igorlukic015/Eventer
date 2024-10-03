import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {User} from "../../contracts/interfaces";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer} from "@ngrx/store";

const adapter: EntityAdapter<User> = createEntityAdapter<User>();

export interface UserState extends EntityState<User> {
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
}

const initialState: UserState = adapter.getInitialState({
  totalElements: 0,
  totalPages: 0,
  pageRequest: {
    page: 0,
    size: defaultPageSize,
    searchTerm: '',
    sort: {
      attributeNames: ['id'],
      sortDirection: SortDirection.ascending
    }
  },
});

const userFeature = createFeature({
  name: 'user',
  reducer: createReducer(
    initialState
  )
});

export const {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal
} = adapter.getSelectors(userFeature.selectUserState);

export const {
  name: userFeatureKey,
  reducer: userReducer,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest
} = userFeature;
