import {createActionGroup, emptyProps, props} from "@ngrx/store";
import {PagedResponse} from "../../../shared/contracts/interfaces";
import {User} from "../../contracts/interfaces";

export const userActions = createActionGroup({
  source: 'user',
  events: {
    'Get users': emptyProps(),
    'Get users success': props<{filtered: User[]}>(),
    'Get users fail': props<{error: string}>(),
    'Get all users': emptyProps(),
    'Get all users success': props<{users: User[]}>(),
    'Get all users fail': props<{error: string}>(),
    'Update page number': props<{currentPage: number}>(),
    'Update search term': props<{searchTerm: string}>(),
    'Delete user': props<{id: number}>(),
    'Delete user success': props<{id: number}>(),
    'Delete user fail': props<{error: string}>(),
    'Default action': emptyProps(),
  }
});
