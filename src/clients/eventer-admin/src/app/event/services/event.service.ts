import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {PagedResponse, PageRequest} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {baseApiUrl} from "../../shared/contracts/statics";
import {Event, EventCreate, EventUpdate} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class EventService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';

  constructor(private httpClient: HttpClient) {}

  public getEvents(pageable: PageRequest): Observable<PagedResponse> {
    let {size, page, searchTerm, sort} = pageable;

    let params: HttpParams = new HttpParams().set('size', size).set('page', page);

    if (sort) {
      params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    }

    if (searchTerm) {
      params = params.append('searchTerm', searchTerm);
    }

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.eventRoute}`, {params: params }, );
  }

  getEventCategories() {
    const size = 10;
    const page = 0;
    const searchTerm = '';
    const sort = null;

    let params: HttpParams = new HttpParams().set('size', size).set('page', page);

    // if (sort) {
    //   params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    // }

    if (searchTerm) {
      params = params.append('searchTerm', searchTerm);
    }

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.categoriesRoute}`, {params: params }, );
  }

  createEvent(data: EventCreate, savedImages: string[]): Observable<Event> {
    return this.httpClient.post<Event>(`${baseApiUrl}/${this.eventRoute}`, {...data, savedImages});
  }

  uploadImages(formData: FormData): Observable<string[]> {
    return this.httpClient.post<string[]>(`${baseApiUrl}/${this.eventRoute}/upload`, formData);
  }

  updateEvent(data: EventUpdate, savedImages: string[]): Observable<Event> {
    return this.httpClient.put<Event>(`${baseApiUrl}/${this.eventRoute}`, {...data, savedImages});
  }
}
