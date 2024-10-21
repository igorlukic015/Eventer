import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {User} from "../../contracts/interfaces";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {userActions} from "../actions/user.actions";
import {eventCategoryActions} from "../../../event-category/+state/actions/event-category.actions";

const adapter: EntityAdapter<User> = createEntityAdapter<User>();

export interface UserState extends EntityState<User> {
  totalPages: number;
  totalElements: number;
  allUsers: User[];
  pageRequest: PageRequest;
}

const initialState: UserState = adapter.getInitialState({
  totalElements: 0,
  totalPages: 0,
  allUsers: [],
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
    initialState,
    on(userActions.getAllUsersSuccess, (state, {users}) => (
      adapter.setAll(users.slice(0, defaultPageSize), {...state, allUsers: users})
    )),
    on(userActions.getUsersSuccess, (state, {filtered}) => (
      adapter.setAll(filtered, {...state})
    )),
    on(userActions.updatePageNumber, (state, {currentPage}) => ({
      ...state, pageRequest: {...state.pageRequest, page: currentPage}
    })),
    on(userActions.updateSearchTerm, (state, {searchTerm}) => ({
      ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
    })),
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
  selectPageRequest,
  selectAllUsers,
} = userFeature;
