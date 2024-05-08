import { Routes } from '@angular/router';
import {loginUrlKey, registerUrlKey} from "./shared/contracts/statics";
import {LoginMainComponent} from "./auth/components/login-main/login-main.component";
import {loginGuard} from "./shared/guards/login.guard";
import {RegisterMainComponent} from "./auth/components/register-main/register-main.component";
import {SearchMainComponent} from "./search/components/search-main/search-main.component";
import {authGuard} from "./shared/guards/auth.guard";
import {provideState} from "@ngrx/store";
import {searchFeatureKey, searchReducer} from "./search/+state/reducers/search.reducers";
import {provideEffects} from "@ngrx/effects";
import {SearchEffects} from "./search/+state/effects/search.effects";

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
    path: '',
    component: SearchMainComponent,
    providers: [
      provideState(searchFeatureKey, searchReducer),
      provideEffects(SearchEffects)
    ],
    canActivate: [authGuard]
  }
];
