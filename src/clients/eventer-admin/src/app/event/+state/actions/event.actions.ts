import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {Event, EventCreate, EventUpdate} from '../../contracts/interfaces';

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
    'Create event': props<{formData: FormData, data: EventCreate}>(),
    'Create event success': props<{createdEvent: Event}>(),
    'Create event fail': props<{error: string}>(),
    'Update event': props<{formData: FormData, data: EventUpdate}>(),
    'Update event success': props<{updatedEvent: Event}>(),
    'Update event fail': props<{error: string}>(),
    'Delete event': props<{id: number}>(),
    'Delete event success': props<{id: number}>(),
    'Delete event fail': props<{error: string}>(),
    'Update selected event id': props<{id: number}>(),
    'Default action': emptyProps(),
  }
})
