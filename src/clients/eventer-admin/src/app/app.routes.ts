import {Routes} from '@angular/router';
import {LayoutMainComponent} from "./shared/components/layout-main/layout-main.component";
import {provideState} from "@ngrx/store";
import {eventCategoryFeatureKey, eventCategoryReducer} from "./event-category/+state/reducers/event-category.reducers";
import {provideEffects} from "@ngrx/effects";
import {EventCategoryEffects} from "./event-category/+state/effects/event-category.effects";

export const routes: Routes = [
  {
    path: 'event-category',
    loadChildren: () => import('./event-category/event-category.routes').then((m) => m.routes),
    providers: [
      provideState(eventCategoryFeatureKey, eventCategoryReducer),
      provideEffects(EventCategoryEffects),
    ]
  },
  {path: 'event', component: LayoutMainComponent},
  {path: '', pathMatch: 'full', redirectTo: 'event-category'}
];
