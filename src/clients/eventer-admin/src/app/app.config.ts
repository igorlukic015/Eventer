import {ApplicationConfig, isDevMode} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideState, provideStore} from '@ngrx/store';
import {provideEffects} from '@ngrx/effects';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {eventCategoryFeatureKey, eventCategoryReducer} from "./event-category/+state/reducers/event-category.reducers";
import {provideHttpClient} from "@angular/common/http";
import {EventCategoryEffects} from "./event-category/+state/effects/event-category.effects";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideStore(),
    provideState(eventCategoryFeatureKey, eventCategoryReducer),
    provideEffects(EventCategoryEffects),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: true,
      trace: false,
      traceLimit: 75
    })
  ]
};
