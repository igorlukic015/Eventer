import {Injectable} from "@angular/core";
import {createEffect, Actions, ofType} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {userActions} from "../actions/user.actions";
import {catchError, map, mergeMap, of, tap, withLatestFrom} from "rxjs";
import {UserService} from "../../services/user.service";
import {User} from "../../contracts/interfaces";
import {ToastrService} from "ngx-toastr";
import {selectAllUsers, selectPageRequest} from "../reducers/user.reducers";
import {defaultPageSize} from "../../../shared/contracts/statics";

@Injectable()
export class UserEffects {
  getAllUsers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(userActions.getAllUsers),
      mergeMap(action => (
        this.userService.getAll().pipe(
          map((users: User[]) => userActions.getAllUsersSuccess({users})),
          catchError((error) => of(userActions.getAllUsersFail({error})))
        )
      ))
    )
  );

  getUsers$ = createEffect(() => {
      return this.actions$.pipe(
        ofType(userActions.getUsers),
        withLatestFrom(
          this.store.select(selectPageRequest),
          this.store.select(selectAllUsers)
        ),
        map(([action, pageRequest, allUsers]) => {
          console.log(pageRequest)
          let skip = (pageRequest.page+1) * defaultPageSize;
          let filtered = allUsers.filter(user => user.username.toUpperCase().includes(pageRequest.searchTerm));
          filtered = filtered.slice(0, skip);
          return userActions.getUsersSuccess({filtered});
        })
      );
    }
  );

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          userActions.getAllUsersFail
        ),
        tap((action: any) => {
          if (action?.error?.detail !== undefined) {
            this.toastrService.error(action.error.detail);
            return;
          }
          this.toastrService.error('Unknown error');
        })
      ),
    {dispatch: false}
  );

  constructor(
    private store: Store,
    private readonly actions$: Actions,
    private readonly userService: UserService,
    private readonly toastrService: ToastrService
  ) {
  }
}
