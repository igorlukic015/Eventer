import {Injectable} from "@angular/core";
import {act, Actions, createEffect, ofType} from "@ngrx/effects";
import {EventCategoryService} from "../../services/event-category.service";
import {catchError, filter, map, mergeMap, Observable, of, switchMap, tap, withLatestFrom} from "rxjs";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {eventCategoryActions} from "../actions/event-category.actions";
import {Store} from "@ngrx/store";
import {selectPageRequest} from "../reducers/event-category.reducers";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {updateEntity} from "../../../shared/+state/actions/real-time.actions";
import {ActionType, ListenedEntity} from "../../../shared/contracts/models";

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
          map(_ => {
            this.toastrService.success('Successfully deleted');
            return eventCategoryActions.deleteEventCategorySuccess({id: action.id})
          }),
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
          map(createdCategory => {
            this.toastrService.success('Successfully created');
            this.router.navigate(['/', 'event-category']);
            return eventCategoryActions.createEventCategorySuccess({createdCategory})
          }),
          catchError(error => of(eventCategoryActions.createEventCategoryFail(error)))
        )
      ))
    )
  )

  updateEventCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventCategoryActions.updateEventCategory),
      switchMap((action) => (
        this.eventCategoryService.updateEventCategory(action.updatedCategory).pipe(
          map(updatedCategory => {
            this.toastrService.success('Successfully created');
            this.router.navigate(['/', 'event-category']);
            return eventCategoryActions.updateEventCategorySuccess({updatedCategory})
          }),
          catchError(error => of(eventCategoryActions.updateEventCategoryFail(error)))
        )
      ))
    )
  )

  updateListenedEventCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateEntity),
      filter((action) => action.payload.entityType === ListenedEntity.eventCategory),
      map((action) => {
        if (action.payload.actionType === ActionType.created) {
          return eventCategoryActions.createEventCategorySuccess({createdCategory: action.payload.data});
        }
        else if (action.payload.actionType === ActionType.updated) {
          return eventCategoryActions.updateEventCategorySuccess({updatedCategory: action.payload.data})
        }
        else if (action.payload.actionType === ActionType.deleted) {
          return eventCategoryActions.deleteEventCategorySuccess({id: action.payload.data});
        }
        return of();
      })
    )
  )

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          eventCategoryActions.getEventCategoriesFail,
          eventCategoryActions.deleteEventCategoryFail,
          eventCategoryActions.createEventCategoryFail,
          eventCategoryActions.updateEventCategoryFail
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
    private store: Store,
    private readonly actions$: Actions,
    private readonly router: Router,
    private readonly eventCategoryService: EventCategoryService,
    private readonly toastrService: ToastrService
  ) {
  }
}
