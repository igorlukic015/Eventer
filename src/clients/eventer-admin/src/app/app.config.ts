import {ApplicationConfig, isDevMode} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideStore} from "@ngrx/store";
import {authInterceptor} from "./shared/interceptor/interceptors";
import { provideToastr } from 'ngx-toastr';
import {provideAnimations} from "@angular/platform-browser/animations";
import {provideEffects} from "@ngrx/effects";
import * as rtsEffects from "./shared/+state/effects/real-time.effects";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    provideRouter(routes),
    provideStore(),
    provideEffects(rtsEffects),
    provideAnimations(),
    provideToastr(),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: false,
      trace: false,
      traceLimit: 75
    })
  ]
};
