import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Récupérer le token au démarrage si disponible
    const token = this.getToken();
    if (token) {
      this.loadCurrentUser();
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          this.saveToken(response.token);
          this.currentUserSubject.next(response.user);
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/register`, userData)
      .pipe(
        tap(response => {
          this.saveToken(response.token);
          this.currentUserSubject.next(response.user);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  getMe(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/auth/me`)
      .pipe(
        tap(user => this.currentUserSubject.next(user))
      );
  }

  updateMe(userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/auth/me`, userData)
      .pipe(
        tap(user => this.currentUserSubject.next(user))
      );
  }

  private loadCurrentUser(): void {
    this.getMe().subscribe({
      error: () => this.logout()
    });
  }

  private saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
}
