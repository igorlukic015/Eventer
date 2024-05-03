import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {PagedResponse, PageRequest} from "../../shared/contracts/interfaces";
import {Observable} from "rxjs";
import {baseApiUrl} from "../../shared/contracts/statics";

@Injectable({providedIn: "root"})
export class AdminService {
  private readonly adminRoute = "api/v1/admin";

  constructor(private httpClient: HttpClient) {
  }

  public getAdmins(pageable: PageRequest): Observable<PagedResponse> {
    let {size, page, searchTerm, sort} = pageable

    let params: HttpParams = new HttpParams().set('size', size).set('page', page);

    if (sort) {
      params = params.append('sort', `${sort.attributeNames.join(',')},${sort.sortDirection}`);
    }

    if (searchTerm) {
      params = params.append('searchTerm', searchTerm);
    }

    return this.httpClient.get<PagedResponse>(`${baseApiUrl}/${this.adminRoute}`, {params});
  }

  public deleteAdmin(id: number) {
    return this.httpClient.delete<void>(`${baseApiUrl}/${this.adminRoute}/${id}`)
  }
}
