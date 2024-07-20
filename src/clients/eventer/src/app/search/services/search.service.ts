import {HttpClient, HttpParams} from "@angular/common/http";
import {Forecast, PagedResponse} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {baseApiUrl, forecastApiUrl} from "../../shared/contracts/statics";
import {CommentData, EventCategory, ExtendedSearchPageRequest} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class SearchService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';
  private readonly commentsRoute = 'api/v1/comment';
  private readonly forecastRoute = 'api/v1/forecast';
  private readonly subscriptionsRoute = 'api/v1/subscription'

  constructor(private httpClient: HttpClient) {
  }

  public getEvents(pageable: ExtendedSearchPageRequest): Observable<PagedResponse> {
    let {size, page, searchTerm, sort} = pageable;

    let params: HttpParams = new HttpParams().set('size', size).set('page', page);

    if (sort) {
      params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    }

    if (searchTerm) {
      params = params.append('searchTerm', searchTerm);
    }

    if (pageable.categoryIds.length > 0) {
      const categoriesQuery = pageable.categoryIds.join(";");
      params = params.append('categories', categoriesQuery);
    }

    if (pageable.weatherConditions.length > 0) {
      const conditionsQuery = pageable.weatherConditions.join(";");
      params = params.append('conditions', conditionsQuery);
    }

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventRoute}`, {params }, );
  }

  getEventCategories() {
    return this.httpClient.get<EventCategory[]>(`${baseApiUrl}/${this.categoriesRoute}`);
  }

  createComment(text: string, eventId: number) {
    return this.httpClient.post<CommentData>(`${baseApiUrl}/${this.commentsRoute}`, {text, eventId})
  }

  getComments(eventId: number) {
    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.commentsRoute}/${eventId}`)
  }

  getForecast(location: string, date: string) {
    let params: HttpParams = new HttpParams().set('city', location).set('date', date);

    return this.httpClient.get<Forecast>(`${forecastApiUrl}/${this.forecastRoute}`, {params})
  }

  getIsEventSubscribed(selectedEventId: number) {
    return this.httpClient.get<boolean>(`${baseApiUrl}/${this.subscriptionsRoute}/${selectedEventId}`);
  }

  subscribeToEvent(eventId: number) {
    const body = {entityType: 'Event', entityId: eventId};

    return this.httpClient.post<boolean>(`${baseApiUrl}/${this.subscriptionsRoute}`, body);
  }
}
