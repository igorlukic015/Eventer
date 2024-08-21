import {Actions, createEffect, ofType} from "@ngrx/effects";
import {inject} from "@angular/core";
import {subscribeToChanges, updateEntity} from "../actions/real-time.actions";
import {mergeMap, of, switchMap} from "rxjs";
import {RealTimeService} from "../../services/real-time.service";
import {Message} from "../../contracts/interfaces";

export const subscribeToChanges$ = createEffect(
  (actions$ = inject(Actions), realTimeService = inject(RealTimeService)) => {
    console.log('sub')

    return actions$.pipe(
      ofType(subscribeToChanges),
      switchMap((action) =>
        realTimeService.subscribeToChanges().pipe(
          mergeMap((message: Message) => {
            console.log('received message')

            return of(
              updateEntity({
                  actionType: message.action,
                  entityType: message.entityType,
                  data: message.data
                }
              )
            )
          })
        )
      )
    )
  },
  { functional: true }
)
