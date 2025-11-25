import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, take } from 'rxjs';
import { environment } from '../../environments/environment';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private userLoaded = false;

  constructor(private http: HttpClient) {
    // Ne pas charger l'utilisateur ici pour éviter la dépendance circulaire
  }

  // Méthode à appeler pour initialiser l'utilisateur si un token existe
  initializeUser(): void {
    if (this.userLoaded) {
      return;
    }
    
    const token = this.getToken();
    console.log('AuthService initializeUser - Token présent:', !!token);
    if (token) {
      this.loadCurrentUser();
    }
    this.userLoaded = true;
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
    return this.http.put<User>(`${this.baseUrl}/users/me`, userData)
      .pipe(
        tap(user => this.currentUserSubject.next(user))
      );
  }

  private loadCurrentUser(): void {
    //Take 1 pour éviter les abonnements multiples (fuites mémoire)
    console.log('AuthService - Chargement de l\'utilisateur...');
    this.getMe().pipe(take(1)).subscribe({
      next: (user) => console.log('AuthService - Utilisateur chargé:', user),
      error: (err) => {
        console.error('AuthService - Erreur lors du chargement de l\'utilisateur:', err);
        this.logout();
      }
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
