import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {ToastrService} from "ngx-toastr";
import {catchError, filter, map, mergeMap, of, switchMap, tap, withLatestFrom} from "rxjs";
import {eventActions} from "../actions/event.actions";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {EventService} from "../../services/event.service";
import {selectPageRequest} from "../reducers/event.reducers";
import {updateEntity} from "../../../shared/+state/actions/real-time.actions";
import {ActionType, ListenedEntity} from "../../../shared/contracts/models";
import {Router} from "@angular/router";

@Injectable()
export class EventEffects {
  getEvents$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventActions.getEvents),
      withLatestFrom(this.store.select(selectPageRequest)),
      mergeMap(([action, pageRequest]) =>
        this.eventService.getEvents(pageRequest).pipe(
            map((pagedResponse: PagedResponse) => eventActions.getEventsSuccess({pagedResponse})),
            catchError((error) => of(eventActions.getEventsFail(error)))
        )
      )
    )
  );

  getCategories$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventActions.getCategories),
      mergeMap((action) =>
        this.eventService.getEventCategories().pipe(
          map((pagedResponse: PagedResponse) => eventActions.getCategoriesSuccess({pagedResponse})),
          catchError((error) => of(eventActions.getCategoriesFail(error)))
        )
      )
    )
  );

  createEventCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventActions.createEvent),
      switchMap((action) => (
        this.eventService.createEvent(action.formData).pipe(
          map(createdEvent => {
            this.toastrService.success('Successfully created');
            this.router.navigate(['/', 'event']);
            return eventActions.createEventSuccess({createdEvent});
          }),
          catchError(error => of(eventActions.createEventFail(error)))
        )
      ))
    )
  )

  updateListenedEvent$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateEntity),
      filter((action) => action.payload.entityType === ListenedEntity.event),
      map((action) => {
        if (action.payload.actionType === ActionType.created) {
          return eventActions.createEventSuccess({createdEvent: action.payload.data});
        }
        // else if (action.payload.actionType === ActionType.updated) {
        //   return eventActions.updateEventCategorySuccess({updatedCategory: action.payload.data})
        // }
        // else if (action.payload.actionType === ActionType.deleted) {
        //   return eventActions.deleteEventCategorySuccess({id: action.payload.data});
        // }
        return of();
      })
    )
  )

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          eventActions.getEventsFail
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
    private readonly eventService: EventService,
    private readonly toastrService: ToastrService,
    private readonly router: Router,
  ) {
  }
}
