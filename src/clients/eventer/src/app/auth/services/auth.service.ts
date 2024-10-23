import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LoginRequest, LoginResponse, PasswordResetRequest, RegisterRequest} from "../contracts/interfaces";
import {baseApiUrl} from "../../shared/contracts/statics";

@Injectable()
export class AuthService {
  private readonly authPathUrl = 'api/v1/auth';
  private readonly userPathUrl = 'api/v1/user';

  constructor(private httpClient: HttpClient) {}

  public authenticate(loginRequest: LoginRequest) {
    return this.httpClient.post<LoginResponse>(`${baseApiUrl}/${this.authPathUrl}/authenticate`, loginRequest);
  }

  public register(registerRequest: RegisterRequest) {
    return this.httpClient.post<any>(`${baseApiUrl}/${this.authPathUrl}/register`, registerRequest);
  }

  public requestPasswordReset(email: string) {
    return this.httpClient.patch<any>(`${baseApiUrl}/${this.userPathUrl}/request-reset/${email}`, {})
  }

  public resetPassword(resetRequest: PasswordResetRequest) {
    return this.httpClient.patch<any>(`${baseApiUrl}/${this.userPathUrl}/reset`, resetRequest)
  }
}
