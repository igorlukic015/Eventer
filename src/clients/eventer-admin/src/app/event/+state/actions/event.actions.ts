import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";

export const eventActions = createActionGroup({
  source: 'event',
  events: {
    'Get events': emptyProps(),
    'Get events success': props<{pagedResponse: PagedResponse}>(),
    'Get events fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
  }
})
