import {Routes} from '@angular/router';
import {LayoutMainComponent} from "./shared/components/layout-main/layout-main.component";
import {provideState} from "@ngrx/store";
import {eventCategoryFeatureKey, eventCategoryReducer} from "./event-category/+state/reducers/event-category.reducers";
import {provideEffects} from "@ngrx/effects";
import {EventCategoryEffects} from "./event-category/+state/effects/event-category.effects";
import {LoginMainComponent} from "./login/components/login-main/login-main.component";
import {EventMainComponent} from "./event/components/event-main/event-main.component";
import {eventCategoryUrlKey, eventUrlKey, loginUrlKey} from "./shared/contracts/statics";

export const routes: Routes = [
  {
    path: eventCategoryUrlKey,
    loadChildren: () => import('./event-category/event-category.routes').then((m) => m.routes),
    providers: [
      provideState(eventCategoryFeatureKey, eventCategoryReducer),
      provideEffects(EventCategoryEffects),
    ]
  },
  {path: eventUrlKey, component: EventMainComponent},
  {path: loginUrlKey, component: LoginMainComponent},
  {path: '', pathMatch: 'full', redirectTo: 'login'}
];
