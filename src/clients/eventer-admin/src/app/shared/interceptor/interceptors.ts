import {HttpEvent, HttpHandlerFn, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {RealTimeService} from "../services/real-time.service";
import {userUrlKey} from "../contracts/statics";

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const token = localStorage.getItem('token');

  if (!token || req.url.includes(userUrlKey)) {
    return next(req);
  }

  const req1 = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${token}`),
  });

  return next(req1);
}


export function tokenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const router = inject(Router);
  const rts = inject(RealTimeService);

  return next(req).pipe(
    catchError(err => {
      if (![401, 403].includes(err.status)) {
        return throwError(() => err);
      }

      localStorage.removeItem('token');
      localStorage.removeItem('role');
      rts.closeConnection();
      router.navigate(['login'])

      return throwError(() => ({...err, error:{detail: "Session expired. Please log in again."}}))
  }));
}
