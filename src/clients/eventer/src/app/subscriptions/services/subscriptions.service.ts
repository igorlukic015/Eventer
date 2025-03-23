import {HttpClient, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {baseApiUrl} from "../../shared/contracts/statics";
import {EventCategory} from "../contracts/interfaces";
import {PagedResponse} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class SubscriptionsService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';
  private readonly subscriptionsRoute = 'api/v1/subscription'

  constructor(private httpClient: HttpClient) {
  }

  public getEvents(pageNumber: number): Observable<PagedResponse> {
    const size = 10

    let params: HttpParams = new HttpParams().set('size', size).set('page', pageNumber);

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventRoute}/subscribed`, {params}, );
  }

  getEventCategories() {
    return this.httpClient.get<EventCategory[]>(`${baseApiUrl}/${this.categoriesRoute}`);
  }

  getSubscribedCategoryIds() {
    return this.httpClient.get<number[]>(`${baseApiUrl}/${this.subscriptionsRoute}/subscribed-categories`);
  }

  subscribeToCategory(categoryId: number) {
    const body = {entityType: 'EventCategory', entityId: categoryId};

    return this.httpClient.post<boolean>(`${baseApiUrl}/${this.subscriptionsRoute}`, body);
  }
}
