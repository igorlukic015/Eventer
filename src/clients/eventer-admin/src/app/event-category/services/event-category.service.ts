import {Injectable} from "@angular/core";
import {PagedResponse, PageRequest} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";


@Injectable({providedIn: 'root'})
export class EventCategoryService {
  private baseApiUrl: string = 'http://localhost:9002';
  private eventCategoryRoute: string = 'api/v1/event-category';

  constructor(private httpClient: HttpClient) {
  }

  public getEventCategories(pageable: PageRequest): Observable<PagedResponse> {
    let {size, page, sort} = pageable

    const params: HttpParams = new HttpParams().set('size', size).set('page', page);

    // const token: string = '';

    if (sort !== null) {
      params.set('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    }

    return this.httpClient.get<PagedResponse>(`${this.baseApiUrl}/${this.eventCategoryRoute}`, {params: params }, );
  }
}
