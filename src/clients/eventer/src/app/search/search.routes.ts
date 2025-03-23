import {SearchDetailsComponent} from "./components/search-details/search-details.component";
import {SearchMainComponent} from "./components/search-main/search-main.component";
import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: "", component: SearchMainComponent},
  {path: "details", component: SearchDetailsComponent},
]
