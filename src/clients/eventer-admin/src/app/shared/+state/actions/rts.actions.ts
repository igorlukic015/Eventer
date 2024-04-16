import {createAction, props} from "@ngrx/store";
import {EventCategory} from "../../../event-category/contracts/interfaces";
import {Event} from "../../../event/contracts/interfaces";

export const updateEntity = createAction(
  '[Entity] Update Entity]',
  (entity: string, actionType: string, value: Event | EventCategory | null) => ({
    entity,
    actionType,
    value
  }));

export const subscribeToChanges = createAction('[Entity] Subscribe To Changes');
