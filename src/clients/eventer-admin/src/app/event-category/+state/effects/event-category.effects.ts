import {Injectable} from "@angular/core";
import {act, Actions, createEffect, ofType} from "@ngrx/effects";
import {EventCategoryService} from "../../services/event-category.service";
import {catchError, map, mergeMap, Observable, of, switchMap, tap, withLatestFrom} from "rxjs";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {eventCategoryActions} from "../actions/event-category.actions";
import {Store} from "@ngrx/store";
import {selectPageRequest} from "../reducers/event-category.reducers";
import {ToastrService} from "ngx-toastr";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {Router} from "@angular/router";

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

  createEventCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventCategoryActions.createEventCategory),
      switchMap((action) => (
        this.eventCategoryService.createEventCategory(action.newCategory).pipe(
          map(createdCategory =>
              eventCategoryActions.createEventCategorySuccess({createdCategory}),
          ),
          catchError(error => of(eventCategoryActions.createEventCategoryFail(error)))
        )
      ))
    )
  )

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          eventCategoryActions.getEventCategoriesFail,
          eventCategoryActions.deleteEventCategoryFail,
          eventCategoryActions.createEventCategoryFail
        ),
        tap((action: any) => {
          if (action?.error?.detail !== undefined) {
            this.toastrService.error(action.error.detail);
            return;
          }
          this.toastrService.error(action.statusText);
        })
      ),
    {dispatch: false}
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private readonly eventCategoryService: EventCategoryService,
    private readonly toastrService: ToastrService
  ) {
  }
}
