import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {select, Store} from "@ngrx/store";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {catchError, map, mergeMap, of, switchAll, switchMap, tap, withLatestFrom} from "rxjs";
import {selectPageRequest} from "../reducers/search.reducers";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {searchActions} from "../actions/search.actions";
import {SearchService} from "../../services/search.service";
import {updateEntity} from "../../../shared/+state/actions/real-time.actions";
import {ActionType, ListenedEntity} from "../../../shared/contracts/models";
import * as searchFeature from "../reducers/search.reducers";

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
          map((categories ) => searchActions.getEventCategoriesSuccess({categories})),
          catchError((error) => of(searchActions.getEventCategoriesFail(error)))
        )
      )
    )
  );

  getComments$ = createEffect(() =>
    this.actions$.pipe(
      ofType(searchActions.getComments),
      withLatestFrom(this.store.pipe(select(searchFeature.selectSelectedEventId))),
      mergeMap(([action, eventId]) =>
        this.eventService.getComments(eventId).pipe(
          map((pagedResponse) => searchActions.getCommentsSuccess({pagedResponse})),
          catchError((error) => of(searchActions.getCommentsFail(error)))
        )
      )
    )
  );

  createComment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(searchActions.createComment),
      switchMap((action) =>
        this.eventService.createComment(action.text, action.eventId).pipe(
          map(createdComment => {
            this.toastrService.success("Comment created successfully");
            return searchActions.createCommentSuccess({createdComment});
          })
        )
      )
    )
  )

  updateListenedEvent$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateEntity),
      map((action) => {
        console.log(action)
        if (action.payload.actionType === ActionType.created) {
          return action.payload.entityType === ListenedEntity.event ?
            searchActions.createEventSuccess({createdEvent: {...action.payload.data, eventId: action.payload.data.id}}) :
            searchActions.createEventCategorySuccess({createdCategory: {...action.payload.data, categoryId: action.payload.data.id}});
        } else if (action.payload.actionType === ActionType.updated) {
          return action.payload.entityType === ListenedEntity.event ?
            searchActions.updateEventSuccess({updatedEvent: {...action.payload.data, eventId: action.payload.data.id}}) :
            searchActions.updateEventCategorySuccess({updatedCategory: {...action.payload.data, categoryId: action.payload.data.id}});
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
          searchActions.getCommentsFail,
          searchActions.createCommentFail
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
