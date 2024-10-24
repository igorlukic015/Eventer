import {Injectable} from "@angular/core";
import {PagedResponse, PageRequest} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {EventCategory, EventCategoryCreate} from "../contracts/interfaces";
import {baseApiUrl} from "../../shared/contracts/statics";

@Injectable({providedIn: 'root'})
export class EventCategoryService {
  private readonly eventCategoryRoute = 'api/v1/event-category';

  constructor(private httpClient: HttpClient) {
  }

  public getEventCategories(pageable: PageRequest): Observable<PagedResponse> {
    let {size, page, searchTerm, sort} = pageable

    let params: HttpParams = new HttpParams().set('size', size).set('page', page);

    if (sort) {
      params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    }

    if (searchTerm) {
      params = params.append('searchTerm', searchTerm);
    }

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventCategoryRoute}`, {params });
  }

  public deleteEventCategory(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${baseApiUrl}/${this.eventCategoryRoute}/${id}`);
  }

  public createEventCategory(newCategory: EventCategoryCreate) {
    return this.httpClient.post<EventCategory>(`${baseApiUrl}/${this.eventCategoryRoute}`, newCategory);
  }

  updateEventCategory(updatedCategory: EventCategory) {
    return this.httpClient.put<EventCategory>(`${baseApiUrl}/${this.eventCategoryRoute}`, updatedCategory);
  }
}
