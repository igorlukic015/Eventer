import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {Event} from '../../contracts/interfaces';

export const eventActions = createActionGroup({
  source: 'event',
  events: {
    'Get events': emptyProps(),
    'Get events success': props<{pagedResponse: PagedResponse}>(),
    'Get events fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Get categories': emptyProps(),
    'Get categories success': props<{pagedResponse: PagedResponse}>(),
    'Get categories fail': props<{error: string}>(),
    'Create event': props<{formData: FormData}>(),
    'Create event success': props<{createdEvent: Event}>(),
    'Create event fail': props<{error: string}>(),
  }
})
