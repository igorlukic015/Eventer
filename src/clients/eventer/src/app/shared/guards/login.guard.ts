import {CanActivateFn, Router, UrlTree} from "@angular/router";
import {inject} from "@angular/core";

export const loginGuard: CanActivateFn = (route, state): UrlTree | boolean => {
  const router = inject(Router);

  const token = localStorage.getItem('token');

  if (token) {
    return router.createUrlTree(['/']);
  }
  return true;
};
