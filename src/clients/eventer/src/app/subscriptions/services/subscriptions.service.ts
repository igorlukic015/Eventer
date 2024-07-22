import {HttpClient, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {baseApiUrl} from "../../shared/contracts/statics";
import {EventCategory} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class SubscriptionsService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';
  private readonly subscriptionsRoute = 'api/v1/subscription'

  constructor(private httpClient: HttpClient) {
  }

  // public getEvents(pageable: ExtendedSearchPageRequest): Observable<PagedResponse> {
  //   let {size, page, searchTerm, sort} = pageable;
  //
  //   let params: HttpParams = new HttpParams().set('size', size).set('page', page);
  //
  //   if (sort) {
  //     params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
  //   }
  //
  //   if (searchTerm) {
  //     params = params.append('searchTerm', searchTerm);
  //   }
  //
  //   if (pageable.categoryIds.length > 0) {
  //     const categoriesQuery = pageable.categoryIds.join(";");
  //     params = params.append('categories', categoriesQuery);
  //   }
  //
  //   if (pageable.weatherConditions.length > 0) {
  //     const conditionsQuery = pageable.weatherConditions.join(";");
  //     params = params.append('conditions', conditionsQuery);
  //   }
  //
  //   return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventRoute}`, {params }, );
  // }

  getEventCategories() {
    return this.httpClient.get<EventCategory[]>(`${baseApiUrl}/${this.categoriesRoute}`);
  }

  getSubscribedCategoryIds() {
    return this.httpClient.get<number[]>(`${baseApiUrl}/${this.subscriptionsRoute}/subscribed-categories`);
  }

  subscribeToEvent(eventId: number) {
    const body = {entityType: 'Event', entityId: eventId};

    return this.httpClient.post<boolean>(`${baseApiUrl}/${this.subscriptionsRoute}`, body);
  }

  subscribeToCategory(categoryId: number) {
    const body = {entityType: 'EventCategory', entityId: categoryId};

    return this.httpClient.post<boolean>(`${baseApiUrl}/${this.subscriptionsRoute}`, body);
  }
}
