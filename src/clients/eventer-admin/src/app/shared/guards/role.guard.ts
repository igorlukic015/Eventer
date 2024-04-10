import {CanActivateFn} from "@angular/router";

export const roleGuard: CanActivateFn = (route, state) => {
  const expectedRoles: string[] = route.data["expectedRoles"];

  const role = localStorage.getItem('role');

  if (!role || !expectedRoles || expectedRoles.includes(role)) {
    return false;
  }
  return true;
};
