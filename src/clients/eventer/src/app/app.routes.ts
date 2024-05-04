import { Routes } from '@angular/router';
import {loginUrlKey, registerUrlKey} from "./shared/contracts/statics";
import {LoginMainComponent} from "./login/components/login-main/login-main.component";
import {loginGuard} from "./shared/guards/login.guard";
import {RegisterMainComponent} from "./register/components/register-main/register-main.component";

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
  {path: '', pathMatch: 'full', redirectTo: 'login'}
];
