import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {EventCategory, EventCategoryCreate} from "../../contracts/interfaces";

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
    'Delete event category fail': props<{error: string}>(),
    'Create event category': props<{ newCategory: EventCategoryCreate }>(),
    'Create event category success': props<{createdCategory: EventCategory}>(),
    'Create event category fail': props<{error: string}>(),
    'Update event category': props<{updatedCategory: EventCategory}>(),
    'Update event category success': props<{updatedCategory: EventCategory}>(),
    'Update event category fail': props<{error: string}>(),
    'Update selected category id': props<{id: number}>(),
    'Default action': emptyProps(),
  }
})
