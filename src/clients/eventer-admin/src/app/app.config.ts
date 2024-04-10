import {ApplicationConfig, isDevMode} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideStore} from "@ngrx/store";
import {authInterceptor} from "./shared/interceptor/interceptors";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    provideRouter(routes),
    provideStore(),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: false,
      trace: false,
      traceLimit: 75
    })
  ]
};
