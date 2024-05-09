import {HttpClient, HttpParams} from "@angular/common/http";
import {PagedResponse, PageRequest} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {baseApiUrl} from "../../shared/contracts/statics";
import {EventCategory} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class SearchService {
  private readonly eventRoute = 'api/v1/event';
  private readonly categoriesRoute = 'api/v1/event-category';

  constructor(private httpClient: HttpClient) {
  }

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
    // const size = 10;
    // const page = 0;
    // const searchTerm = '';
    //
    // let params: HttpParams = new HttpParams().set('size', size).set('page', page);
    //
    // if (searchTerm) {
    //   params = params.append('searchTerm', searchTerm);
    // }

    return this.httpClient.get<EventCategory[]>(`${baseApiUrl}/${this.categoriesRoute}`);
  }
}
