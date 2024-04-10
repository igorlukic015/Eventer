import {Routes} from "@angular/router";
import {EventCategoryMainComponent} from "./components/event-category-main/event-category-main.component";
import {EventCategoryCreateComponent} from "./components/event-category-create/event-category-create.component";

export const routes: Routes = [
  {path: "", component: EventCategoryMainComponent},
  {path: "create", component: EventCategoryCreateComponent}
];
