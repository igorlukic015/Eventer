import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";

export const userActions = createActionGroup({
  source: 'user',
  events: {
    'Get users': emptyProps(),
    'Get users success': props<{pagedResponse: PagedResponse}>(),
    'Get users fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Delete user': props<{id: number}>(),
    'Delete user success': props<{id: number}>(),
    'Delete user fail': props<{error: string}>(),
    'Default action': emptyProps(),
  }
});
