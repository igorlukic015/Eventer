import {createActionGroup, props} from "@ngrx/store";
import {PagedResponse, PageRequest} from "../../../shared/contracts/interfaces";

export const eventCategoryActions = createActionGroup({
  source: 'eventCategory',
  events: {
    'Get event categories': props<{pageRequest: PageRequest}>(),
    'Get event categories success': props<{pagedResponse: PagedResponse}>(),
    'Get event categories fail' : props<{error: string}>()
  }
})
