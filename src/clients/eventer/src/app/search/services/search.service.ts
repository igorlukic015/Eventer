import {HttpClient, HttpParams} from "@angular/common/http";
import {PagedResponse} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {baseApiUrl} from "../../shared/contracts/statics";
import {EventCategory, ExtendedSearchPageRequest} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class SearchService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';

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

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventRoute}`, {params: params }, );
  }

  getEventCategories() {
    return this.httpClient.get<EventCategory[]>(`${baseApiUrl}/${this.categoriesRoute}`);
  }
}
