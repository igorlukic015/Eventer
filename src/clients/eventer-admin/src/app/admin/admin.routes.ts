import {Routes} from "@angular/router";
import {AdminMainComponent} from "./components/admin-main/admin-main.component";
import {AdminCreateComponent} from "./components/admin-create/admin-create.component";

export const routes: Routes = [
  {path: "", component: AdminMainComponent},
  {path: "create", component: AdminCreateComponent}
]
