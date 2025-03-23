import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {CommentData, EventCategory, EventData} from "../../contracts/interfaces";

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
    'Get comments' : emptyProps(),
    'Get comments success' : props<{pagedResponse: PagedResponse}>(),
    'Get comments fail': props<{error: string}>(),
    'Create comment' : props<{text: string, eventId: number}>(),
    'Create comment success': props<{createdComment: CommentData}>(),
    'Create comment fail': props<{error: string}>(),
    'Update comment': props<{text:string, commentId: number}>(),
    'Update comment success' : props<{updatedComment: CommentData}>(),
    'Update comment fail': props<{error: string}>(),
    'Delete comment': props<{commentId: number}>(),
    'Delete comment success' : props<{id: number}>(),
    'Delete comment fail': props<{error: string}>(),
    'Default action': emptyProps(),
  }
})
