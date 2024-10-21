import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../contracts/interfaces";
import {userApiUrl} from "../../shared/contracts/statics";

@Injectable({providedIn: 'root'})
export class UserService {
  private readonly userRoute = 'api/v1/user';

  constructor(private httpClient: HttpClient) {
  }

  public getAll(): Observable<User[]> {
    return this.httpClient.get<User[]>(`${userApiUrl}/${this.userRoute}/get-all`);
  }
}
