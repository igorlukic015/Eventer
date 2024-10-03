import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {User} from "../../contracts/interfaces";
import {select, Store} from "@ngrx/store";
import * as userFeature from "../reducers/user.reducers";
import {PageRequest} from "../../../shared/contracts/interfaces";
import {userActions} from "../actions/user.actions";

@Injectable()
export class UserFacade {
  items$: Observable<User[]> = this.store.pipe(select(userFeature.selectAll));
  totalPages$: Observable<number> = this.store.pipe(select(userFeature.selectTotalPages));
  totalElements$: Observable<number> = this.store.pipe(select(userFeature.selectTotalElements));
  pageRequest$: Observable<PageRequest> = this.store.pipe(select(userFeature.selectPageRequest));

  constructor(private readonly store: Store) {
  }

  loadAdmins() {
    this.store.dispatch(userActions.getUsers());
  }

  updatePageNumber(currentPage: number) {
    this.store.dispatch(userActions.updatePageNumber({currentPage}));
  }

  updateSearchTerm(searchTerm: string) {
    this.store.dispatch(userActions.updateSearchTerm({searchTerm}))
  }

  deleteUser(id: number) {
    this.store.dispatch(userActions.deleteUser({id}));
  }
}
