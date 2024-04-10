import {Routes} from "@angular/router";
import {EventCategoryMainComponent} from "./components/event-category-main/event-category-main.component";
import {EventCategoryCreateComponent} from "./components/event-category-create/event-category-create.component";
import {EventCategoryUpdateComponent} from "./components/event-category-update/event-category-update.component";

export const routes: Routes = [
  {path: "", component: EventCategoryMainComponent},
  {path: "create", component: EventCategoryCreateComponent},
  {path: "update", component: EventCategoryUpdateComponent}
];
