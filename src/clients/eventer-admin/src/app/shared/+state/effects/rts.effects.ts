import {Actions, createEffect, ofType} from "@ngrx/effects";
import {inject} from "@angular/core";
import {subscribeToChanges, updateEntity} from "../actions/rts.actions";
import {mergeMap, of, switchMap} from "rxjs";
import {RealTimeService} from "../../services/real-time.service";

export const subscribeToChanges$ = createEffect(
  (actions$ = inject(Actions), realTimeService = inject(RealTimeService)) => {
    return actions$.pipe(
      ofType(subscribeToChanges),
      switchMap((action) =>
        realTimeService.subscribeToChanges().pipe(
          mergeMap((message) => {
            console.log(message);
            return of(updateEntity('EventCategory', 'delete', null))
          })
        )
      )
    )
  },
  { functional: true }
)
