import {Routes} from '@angular/router';
import {provideState} from "@ngrx/store";
import {eventCategoryFeatureKey, eventCategoryReducer} from "./event-category/+state/reducers/event-category.reducers";
import {provideEffects} from "@ngrx/effects";
import {EventCategoryEffects} from "./event-category/+state/effects/event-category.effects";
import {LoginMainComponent} from "./login/components/login-main/login-main.component";
import {adminUrlKey, eventCategoryUrlKey, eventUrlKey, loginUrlKey, userUrlKey} from "./shared/contracts/statics";
import {authGuard} from "./shared/guards/auth.guard";
import {roleGuard} from "./shared/guards/role.guard";
import {loginGuard} from "./shared/guards/login.guard";
import {eventFeatureKey, eventReducer} from "./event/+state/reducers/event.reducers";
import {EventEffects} from "./event/+state/effects/event.effects";
import {adminFeatureKey, adminReducer} from "./admin/+state/reducers/admin.reducers";
import {Role} from "./shared/contracts/models";
import {AdminEffects} from "./admin/+state/effects/admin.effects";
import {userFeatureKey, userReducer} from "./user/+state/reducers/user.reducers";
import {UserEffects} from "./user/+state/effects/user.effects";

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
  {
    path: eventUrlKey,
    loadChildren: () => import('./event/event.routes').then((m) => m.routes),
    providers: [
      provideState(eventFeatureKey, eventReducer),
      provideEffects(EventEffects)
    ],
    canActivate: [authGuard]
  },
  {
    path: userUrlKey,
    loadChildren: () => import('./user/user.routes').then((m) => m.routes),
    providers: [
      provideState(userFeatureKey, userReducer),
      provideEffects(UserEffects)
    ],
    canActivate: [authGuard]
  },
  {
    path: adminUrlKey,
    loadChildren: () => import('./admin/admin.routes').then((m) => m.routes),
    providers: [
      provideState(adminFeatureKey, adminReducer),
      provideEffects(AdminEffects)
    ],
    canActivate: [authGuard, roleGuard],
    data: {expectedRoles: [Role.administrator]}
  },
  {
    path: loginUrlKey,
    component: LoginMainComponent,
    canActivate: [loginGuard]
  },
  {path: '', pathMatch: 'full', redirectTo: 'login'}
];
