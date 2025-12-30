export interface RegisterRequest {
  email: string;
  name: string;
  password: string;
}

export interface LoginRequest {
  identifier: string; // l'email ou pseudo (name)
  password: string;
}

export interface AuthResponse {
  token: string;
}
