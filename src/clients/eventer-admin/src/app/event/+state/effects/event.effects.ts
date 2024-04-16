import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {catchError, filter, map, mergeMap, of, tap, withLatestFrom} from "rxjs";
import {eventActions} from "../actions/event.actions";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {EventService} from "../../services/event.service";
import {selectPageRequest} from "../reducers/event.reducers";
import {updateEntity} from "../../../shared/+state/actions/rts.actions";

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

  lala = createEffect(() =>
    this.actions$.pipe(
      ofType(updateEntity),
      filter((action) => action.entity === 'Event'),
      tap((action: any) => {
        console.log('update entity in effects');
        console.log(action);
      }),
    ),
    {dispatch: false}
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
    private readonly router: Router,
    private readonly eventService: EventService,
    private readonly toastrService: ToastrService
  ) {
  }
}
