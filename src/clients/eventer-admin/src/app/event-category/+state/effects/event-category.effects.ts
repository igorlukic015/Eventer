import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {EventCategoryService} from "../../services/event-category.service";
import {catchError, map, mergeMap, of, tap} from "rxjs";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {eventCategoryActions} from "../actions/event-category.actions";

@Injectable()
export class EventCategoryEffects {
  getEventCategories$ = createEffect(() =>
    this.actions$.pipe(
      ofType(eventCategoryActions.getEventCategories),
      mergeMap((action) => (
        this.eventCategoryService.getEventCategories(action.pageRequest).pipe(
          map((pagedResponse: PagedResponse) => eventCategoryActions.getEventCategoriesSuccess({pagedResponse})),
          catchError((error) => of(eventCategoryActions.getEventCategoriesFail(error)))
        )
      ))
    )
  );

  showErrorToast$ = createEffect(() =>
      this.actions$.pipe(
        ofType(eventCategoryActions.getEventCategoriesFail),
        tap((action: any) => {
          if (action.error !== undefined) {
            // TODO: Show error notification
            alert(action.error);
          }
        })
      ),
    {dispatch: false} // TODO: See what happens without this
  );

  constructor(
    private actions$: Actions,
    private readonly eventCategoryService: EventCategoryService
  ) {
  }
}
