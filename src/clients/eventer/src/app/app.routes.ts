import { Routes } from '@angular/router';
import {
  forgotPasswordResetUrlKey,
  forgotPasswordUrlKey,
  loginUrlKey,
  profileUrlKey,
  registerUrlKey,
  subscriptionsUrlKey
} from "./shared/contracts/statics";
import {LoginMainComponent} from "./auth/components/login-main/login-main.component";
import {loginGuard} from "./shared/guards/login.guard";
import {RegisterMainComponent} from "./auth/components/register-main/register-main.component";
import {authGuard} from "./shared/guards/auth.guard";
import {provideState} from "@ngrx/store";
import {searchFeatureKey, searchReducer} from "./search/+state/reducers/search.reducers";
import {provideEffects} from "@ngrx/effects";
import {SearchEffects} from "./search/+state/effects/search.effects";
import {UserProfileComponent} from "./profile/components/user-profile/user-profile.component";
import {SubscriptionsComponent} from "./subscriptions/components/subscriptions/subscriptions.component";
import {ForgotPasswordMainComponent} from "./auth/components/forgot-password/forgot-password-main/forgot-password-main.component";
import {
  ForgotPasswordResetComponent
} from "./auth/components/forgot-password/forgot-password-reset/forgot-password-reset.component";

export const routes: Routes = [
  {
    path: registerUrlKey,
    component: RegisterMainComponent,
    canActivate:[loginGuard]
  },
  {
    path: loginUrlKey,
    component: LoginMainComponent,
    canActivate: [loginGuard]
  },
  {
    path: forgotPasswordUrlKey,
    component: ForgotPasswordMainComponent,
    canActivate:[loginGuard]
  },
  {
    path: forgotPasswordResetUrlKey,
    component: ForgotPasswordResetComponent,
    canActivate:[loginGuard]
  },
  {
    path: profileUrlKey,
    component: UserProfileComponent,
    canActivate: [authGuard]
  },
  {
    path: subscriptionsUrlKey,
    component: SubscriptionsComponent,
    canActivate: [authGuard]
  },
  {
    path: '',
    loadChildren: () => import('./search/search.routes').then(m => m.routes),
    providers: [
      provideState(searchFeatureKey, searchReducer),
      provideEffects(SearchEffects)
    ],
    canActivate: [authGuard]
  }
];
