import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {baseApiUrl} from "../../shared/contracts/statics";
import {ProfileUpdateRequest, User} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class UserService {
  private readonly userPathUrl = "api/v1/user";

  constructor(private httpClient: HttpClient) {
  }

  public getProfileData(){
    return this.httpClient.get<User>(`${baseApiUrl}/${this.userPathUrl}`);
  }

  public uploadAvatar(formData: FormData) {
    return this.httpClient.post<User>(`${baseApiUrl}/${this.userPathUrl}/upload`, formData)
  }

  public updateProfileData(request: ProfileUpdateRequest) {
    return this.httpClient.put<User>(`${baseApiUrl}/${this.userPathUrl}`, request);
  }
}
