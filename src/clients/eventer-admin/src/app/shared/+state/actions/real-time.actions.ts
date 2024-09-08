import {createAction} from "@ngrx/store";
import {UpdateEntityData} from "../../contracts/interfaces";

export const updateEntity = createAction(
  '[Entity] Update Entity',
  (payload: UpdateEntityData) => ({
    payload
  }));

export const subscribeToChanges = createAction('[Entity] Subscribe To Changes');
