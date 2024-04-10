import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";

export const eventCategoryActions = createActionGroup({
  source: 'eventCategory',
  events: {
    'Get event categories': emptyProps(),
    'Get event categories success': props<{pagedResponse: PagedResponse}>(),
    'Get event categories fail' : props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Delete event category': props<{id: number}>(),
    'Delete event category success': props<{id: number}>(),
    'Delete event category fail': props<{error: string}>()
  }
})
