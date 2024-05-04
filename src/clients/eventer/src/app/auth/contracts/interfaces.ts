export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}
