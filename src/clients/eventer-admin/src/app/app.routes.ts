import {Routes} from '@angular/router';
import {provideState} from "@ngrx/store";
import {eventCategoryFeatureKey, eventCategoryReducer} from "./event-category/+state/reducers/event-category.reducers";
import {provideEffects} from "@ngrx/effects";
import {EventCategoryEffects} from "./event-category/+state/effects/event-category.effects";
import {LoginMainComponent} from "./login/components/login-main/login-main.component";
import {EventMainComponent} from "./event/components/event-main/event-main.component";
import {adminUrlKey, eventCategoryUrlKey, eventUrlKey, loginUrlKey} from "./shared/contracts/statics";
import {authGuard} from "./shared/guards/auth.guard";
import {LayoutMainComponent} from "./shared/components/layout-main/layout-main.component";
import {roleGuard} from "./shared/guards/role.guard";
import {loginGuard} from "./shared/guards/login.guard";

export const routes: Routes = [
  {
    path: eventCategoryUrlKey,
    loadChildren: () => import('./event-category/event-category.routes').then((m) => m.routes),
    providers: [
      provideState(eventCategoryFeatureKey, eventCategoryReducer),
      provideEffects(EventCategoryEffects),
    ],
    canActivate: [authGuard]
  },
  {path: eventUrlKey, component: EventMainComponent, canActivate: [authGuard]},
  {path: adminUrlKey, component: LayoutMainComponent,
    canActivate: [authGuard, roleGuard],
    data:{expectedRoles: ["superadmin"]}},
  {
    path: loginUrlKey,
    component: LoginMainComponent,
    canActivate: [loginGuard]
  },
  {path: '', pathMatch: 'full', redirectTo: 'login'}
];
