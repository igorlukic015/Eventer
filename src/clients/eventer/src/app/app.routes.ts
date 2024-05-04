import { Routes } from '@angular/router';
import {loginUrlKey} from "./shared/contracts/statics";
import {LoginMainComponent} from "./login/components/login-main/login-main.component";
import {loginGuard} from "./shared/guards/login.guard";

export const routes: Routes = [
  {
    path: loginUrlKey,
    component: LoginMainComponent,
    canActivate: [loginGuard]
  },
  {path: '', pathMatch: 'full', redirectTo: 'login'}
];
