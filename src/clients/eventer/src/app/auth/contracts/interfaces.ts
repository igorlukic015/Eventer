export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  userProfileImageUrl: string;
}

export interface RegisterRequest {
  name: string;
  username: string;
  password: string;
  city: string;
}

export interface PasswordResetRequest {
  email: string;
  password: string;
  code: string;
}
