import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";

export const searchActions = createActionGroup({
  source: 'search',
  events: {
    'Get event categories': emptyProps(),
    'Get event categories success': props<{pagedResponse: PagedResponse}>(),
    'Get event categories fail' : props<{error: string}>(),
    'Get events': emptyProps(),
    'Get events success': props<{pagedResponse: PagedResponse}>(),
    'Get events fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Update selected event id': props<{id: number}>(),
    'Default action': emptyProps(),
  }
})
