import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {Admin, Register} from "../../contracts/interfaces";

export const adminActions = createActionGroup({
  source: 'admin',
  events: {
    'Get admins': emptyProps(),
    'Get admins success': props<{pagedResponse: PagedResponse}>(),
    'Get admins fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Delete admin': props<{id: number}>(),
    'Delete admin success': props<{id: number}>(),
    'Delete admin fail': props<{error: string}>(),
    'Register admin': props<{newAdmin: Register}>(),
    'Register admin success': props<{admin: Admin}>(),
    'Register admin fail': props<{error: string}>(),
    'Default action': emptyProps(),
  }
})
