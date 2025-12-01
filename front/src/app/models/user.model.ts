export interface User {
  id: number;
  username: string;
  email: string;
  createdAt?: string | Date;
  updatedAt?: string | Date;
  subscriptions?: Array<{
    id: number;
    title: string;
    description: string;
  }>;
}

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: {
    id: number;
    username: string;
    email: string;
  };
}
