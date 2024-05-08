import {ApplicationConfig, isDevMode} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideToastr} from "ngx-toastr";
import {provideAnimations} from "@angular/platform-browser/animations";
import {authInterceptor, tokenInterceptor} from "./shared/interceptors/interceptors";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideEffects} from "@ngrx/effects";
import * as rtsEffects from "./shared/+state/effects/real-time.effects";
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {provideStore} from "@ngrx/store";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor, tokenInterceptor])),
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
