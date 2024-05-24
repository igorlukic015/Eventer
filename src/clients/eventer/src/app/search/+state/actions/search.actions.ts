import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {EventCategory, EventData} from "../../contracts/interfaces";

export const searchActions = createActionGroup({
  source: 'search',
  events: {
    'Get event categories': emptyProps(),
    'Get event categories success': props<{categories: EventCategory[]}>(),
    'Get event categories fail' : props<{error: string}>(),
    'Get events': emptyProps(),
    'Get events success': props<{pagedResponse: PagedResponse}>(),
    'Get events fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Update selected event id': props<{id: number}>(),
    'Create event success': props<{createdEvent: EventData}>(),
    'Update event success': props<{updatedEvent: EventData}>(),
    'Delete event success': props<{id: number}>(),
    'Create event category success': props<{createdCategory: EventCategory}>(),
    'Update event category success': props<{updatedCategory: EventCategory}>(),
    'Delete event category success': props<{id: number}>(),
    'Update filter categories': props<{categoryId: number, isAdding: boolean}>(),
    'Update filter conditions': props<{condition: string, isAdding: boolean}>(),
    'Default action': emptyProps(),
  }
})
