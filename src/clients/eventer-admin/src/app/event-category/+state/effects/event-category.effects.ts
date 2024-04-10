import {Injectable} from "@angular/core";
import {act, Actions, createEffect, ofType} from "@ngrx/effects";
import {EventCategoryService} from "../../services/event-category.service";
import {catchError, map, mergeMap, of, switchMap, tap, withLatestFrom} from "rxjs";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {eventCategoryActions} from "../actions/event-category.actions";
import {Store} from "@ngrx/store";
import {selectPageRequest} from "../reducers/event-category.reducers";
import {ToastrService} from "ngx-toastr";

@Injectable()
export class EventCategoryEffects {
  getEventCategories$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventCategoryActions.getEventCategories),
      withLatestFrom(this.store.select(selectPageRequest)),
      mergeMap(([action, pageRequest]) => (
        this.eventCategoryService.getEventCategories(pageRequest).pipe(
          map((pagedResponse: PagedResponse) => eventCategoryActions.getEventCategoriesSuccess({pagedResponse})),
          catchError((error) => of(eventCategoryActions.getEventCategoriesFail(error)))
        )
      ))
    )
  );

  deleteEventCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventCategoryActions.deleteEventCategory),
      switchMap(action => (
        this.eventCategoryService.deleteEventCategory(action.id).pipe(
          map(_ => eventCategoryActions.deleteEventCategorySuccess({id: action.id})),
          catchError((error) => of(eventCategoryActions.deleteEventCategoryFail(error)))
        )
      ))
    )
  );

  showErrorToast$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        eventCategoryActions.getEventCategoriesFail,
        eventCategoryActions.deleteEventCategoryFail),
      tap((action: any) => {
        if (action.error !== undefined) {
          this.toastrService.error(action.error.detail);
        }
      })
    ),
  {dispatch: false} // TODO: See what happens without this
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private readonly eventCategoryService: EventCategoryService,
    private readonly toastrService: ToastrService
  ) {
  }
}
