import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {catchError, map, mergeMap, of, switchMap, tap, withLatestFrom} from "rxjs";
import {adminActions} from "../actions/admin.actions";
import {selectPageRequest} from "../reducers/admin.reducers";
import {AdminService} from "../../services/admin.service";

@Injectable()
export class AdminEffects {
  getAdmins$ = createEffect(() =>
    this.actions$.pipe(
      ofType(adminActions.getAdmins),
      withLatestFrom(this.store.select(selectPageRequest)),
      mergeMap(([action, pageRequest]) => (
        this.adminService.getAdmins(pageRequest).pipe(
          map((pagedResponse) => adminActions.getAdminsSuccess({pagedResponse})),
          catchError((error) => of(adminActions.getAdminsFail(error)))
        )
      ))
    )
  );

  deleteAdmin$ = createEffect(() =>
    this.actions$.pipe(
      ofType(adminActions.deleteAdmin),
      switchMap((action) => (
        this.adminService.deleteAdmin(action.id).pipe(
          map(() => {
            this.toastrService.success("Successfully deleted");
            return adminActions.deleteAdminSuccess({id: action.id});
          }),
          catchError((error) => of(adminActions.deleteAdminFail(error)))
        )
      ))
    )
  );

  registerAdmin$ = createEffect(() =>
    this.actions$.pipe(
      ofType(adminActions.registerAdmin),
      switchMap((action) => (
        this.adminService.registerAdmin(action.newAdmin).pipe(
          map((admin) => {
            this.toastrService.success("Successfully registered");
            this.router.navigate(['/', 'admin']);
            return adminActions.registerAdminSuccess({admin})
          })
        )
      ))
    )
  )

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          adminActions.getAdminsFail,
          adminActions.deleteAdminFail,
          adminActions.registerAdminFail
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
    private readonly router: Router,
    private readonly adminService: AdminService,
    private readonly toastrService: ToastrService
  ) {
  }
}
