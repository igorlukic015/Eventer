import {Routes} from '@angular/router';

export const routes: Routes = [
  {path: "event-category", loadChildren: () => import('./event-category/event-category.routes').then((m) => m.routes)},
];
