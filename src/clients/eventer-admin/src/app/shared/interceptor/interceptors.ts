import {HttpEvent, HttpHandlerFn, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const token = localStorage.getItem('token');

  if (!token) {
    return next(req);
  }

  const req1 = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${token}`),
  });

  return next(req1);
}
