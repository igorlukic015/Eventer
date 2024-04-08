import {createActionGroup, props} from "@ngrx/store";
import {PagedResponse, PageRequest} from "../../../shared/contracts/interfaces";
import {eventCategoryFeatureKey} from "../reducers/event-category.reducers";

export const eventCategoryActions = createActionGroup({
  source: eventCategoryFeatureKey,
  events: {
    'Get event categories': props<{pageRequest: PageRequest}>(),
    'Get event categories success': props<{pagedResponse: PagedResponse}>(),
    'Get event categories fail' : props<{error: string}>()
  }
})
