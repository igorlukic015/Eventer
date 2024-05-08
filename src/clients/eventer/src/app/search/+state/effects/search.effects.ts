import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {catchError, map, mergeMap, of, tap, withLatestFrom} from "rxjs";
import {selectPageRequest} from "../reducers/search.reducers";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {searchActions} from "../actions/search.actions";
import {SearchService} from "../../services/search.service";
import {updateEntity} from "../../../shared/+state/actions/real-time.actions";
import {ActionType, ListenedEntity} from "../../../shared/contracts/models";

@Injectable()
export class SearchEffects {
  getEvents$ = createEffect(() =>
    this.actions$.pipe(
      ofType(searchActions.getEvents),
      withLatestFrom(this.store.select(selectPageRequest)),
      mergeMap(([action, pageRequest]) =>
        this.eventService.getEvents(pageRequest).pipe(
          map((pagedResponse: PagedResponse) => searchActions.getEventsSuccess({pagedResponse})),
          catchError((error) => of(searchActions.getEventsFail(error)))
        )
      )
    )
  );

  getCategories$ = createEffect(() =>
    this.actions$.pipe(
      ofType(searchActions.getEventCategories),
      mergeMap((action) =>
        this.eventService.getEventCategories().pipe(
          map((pagedResponse: PagedResponse) => searchActions.getEventCategoriesSuccess({pagedResponse})),
          catchError((error) => of(searchActions.getEventCategoriesFail(error)))
        )
      )
    )
  );

  updateListenedEvent$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateEntity),
      map((action) => {
        if (action.payload.actionType === ActionType.created) {
          return action.payload.entityType === ListenedEntity.event ?
            searchActions.createEventSuccess({createdEvent: action.payload.data}) :
            searchActions.createEventCategorySuccess({createdCategory: action.payload.data});
        } else if (action.payload.actionType === ActionType.updated) {
          return action.payload.entityType === ListenedEntity.event ?
            searchActions.updateEventSuccess({updatedEvent: action.payload.data}) :
            searchActions.updateEventCategorySuccess({updatedCategory: action.payload.data});
        } else if (action.payload.actionType === ActionType.deleted) {
          return action.payload.entityType === ListenedEntity.event ?
            searchActions.deleteEventSuccess({id: action.payload.data}) :
            searchActions.deleteEventCategorySuccess({id: action.payload.data});
        }
        return searchActions.defaultAction();
      })
    )
  )

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(
          searchActions.getEventsFail,
          searchActions.getEventCategoriesFail,
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
    private readonly eventService: SearchService,
    private readonly toastrService: ToastrService,
    private readonly router: Router,
  ) {
  }
}
