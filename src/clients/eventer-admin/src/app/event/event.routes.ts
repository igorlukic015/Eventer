import {Routes} from "@angular/router";
import {EventMainComponent} from "./components/event-main/event-main.component";
import {EventCreateComponent} from "./components/event-create/event-create.component";

export const routes: Routes = [
  {path: "", component: EventMainComponent},
  {path: "create", component: EventCreateComponent},
  {path: "update", component: EventCreateComponent},
]
