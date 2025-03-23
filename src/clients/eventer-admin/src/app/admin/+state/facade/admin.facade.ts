import {Injectable} from "@angular/core";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {PageRequest} from "../../../shared/contracts/interfaces";
import * as adminFeature from "../reducers/admin.reducers";
import {Admin, Register} from "../../contracts/interfaces";
import {adminActions} from "../actions/admin.actions";

@Injectable()
export class AdminFacade {
  items$: Observable<Admin[]> = this.store.pipe(select(adminFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(adminFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(adminFeature.selectTotalElements));
  loading$: Observable<boolean> = this.store.pipe(select(adminFeature.selectIsLoading));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(adminFeature.selectPageRequest));

  constructor(private readonly store: Store) {
  }

  registerAdmin(newAdmin: Register) {
    this.store.dispatch(adminActions.registerAdmin({newAdmin}))
  }

  loadAdmins() {
    this.store.dispatch(adminActions.getAdmins());
  }

  updatePageNumber(currentPage: number) {
    this.store.dispatch(adminActions.updatePageNumber({currentPage}));
  }

  updateSearchTerm(searchTerm: string) {
    this.store.dispatch(adminActions.updateSearchTerm({searchTerm}))
  }

  deleteAdmin(id: number) {
    this.store.dispatch(adminActions.deleteAdmin({id}));
  }
}
