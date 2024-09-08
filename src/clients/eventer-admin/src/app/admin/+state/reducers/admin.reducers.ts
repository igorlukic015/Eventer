import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {Admin} from "../../contracts/interfaces";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {defaultPageSize} from "../../../shared/contracts/statics";
import {SortDirection} from "../../../shared/contracts/models";
import {createFeature, createReducer, on} from "@ngrx/store";
import {adminActions} from "../actions/admin.actions";

const adapter: EntityAdapter<Admin> = createEntityAdapter<Admin>();

export interface AdminState extends EntityState<Admin> {
  isLoading: boolean;
  totalPages: number;
  totalElements: number;
  pageRequest: PageRequest;
}

const initialState: AdminState = adapter.getInitialState({
  isLoading: false,
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
})

const adminFeature = createFeature({
    name: 'admin',
    reducer: createReducer(
      initialState,
      on(adminActions.getAdmins, (state) => ({
        ...state, isLoading: true
      })),
      on(adminActions.getAdminsSuccess, (state, {pagedResponse}) => (
        adapter.setAll(pagedResponse.content, {...state, isLoading: false})
      )),
      on(adminActions.getAdminsFail, (state, {error}) => ({
        ...state, isLoading: false
      })),
      on(adminActions.updatePageNumber, (state, {currentPage}) => ({
        ...state, pageRequest: {...state.pageRequest, page: currentPage}
      })),
      on(adminActions.updateSearchTerm, (state, {searchTerm}) => ({
        ...state, pageRequest: {...state.pageRequest, searchTerm: searchTerm}
      })),
      on(adminActions.deleteAdminSuccess, (state, {id}) => (
        adapter.removeOne(id, state)
      )),
      on(adminActions.registerAdminSuccess, (state, {admin}) => (
        adapter.addOne(admin, state)
      ))
    )
  }
)

export const {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal
} = adapter.getSelectors(adminFeature.selectAdminState);

export const {
  name: adminFeatureKey,
  reducer: adminReducer,
  selectIsLoading,
  selectTotalPages,
  selectTotalElements,
  selectPageRequest,
} = adminFeature;
