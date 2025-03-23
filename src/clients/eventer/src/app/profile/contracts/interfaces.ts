import {Image} from "../../shared/contracts/interfaces";

export interface User {
  id: number;
  username: string;
  name: string;
  city: string;
  profileImage: Image
}

export interface ProfileUpdateRequest {
  id: number;
  username: string;
  name: string;
  city: string;
}
