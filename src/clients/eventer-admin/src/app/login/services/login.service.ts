import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LoginRequest, LoginResponse} from "../contracts/interfaces";
import {baseApiUrl} from "../../shared/contracts/statics";

@Injectable()
export class LoginService {
  private readonly authPathUrl = 'api/v1/auth';

  constructor(private httpClient: HttpClient) {}

  public authenticate(loginRequest: LoginRequest) {
    return this.httpClient.post<LoginResponse>(`${baseApiUrl}/${this.authPathUrl}/authenticate`, loginRequest);
  }
}
