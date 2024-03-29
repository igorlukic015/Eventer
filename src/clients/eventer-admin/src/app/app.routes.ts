import {Routes} from '@angular/router';
import {LayoutMainComponent} from "./shared/layout-main/layout-main.component";

export const routes: Routes = [
  {path: 'event-category', loadChildren: () => import('./event-category/event-category.routes').then((m) => m.routes)},
  {path: 'event', component: LayoutMainComponent},
  {path: '', pathMatch: 'full', redirectTo: 'event-category'}
];
