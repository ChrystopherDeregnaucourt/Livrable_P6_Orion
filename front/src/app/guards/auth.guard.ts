import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard  {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // Initialiser l'utilisateur si nécessaire
    this.authService.initializeUser();
    
    const token = this.authService.getToken();
    console.log('AuthGuard - Token présent:', !!token);
    
    // Si pas de token, rediriger immédiatement
    if (!token) {
      console.log('AuthGuard - Pas de token, redirection vers /login');
      return this.router.createUrlTree(['/login']);
    }
    
    // Si l'utilisateur est déjà chargé, autoriser l'accès
    const currentUser = this.authService.getCurrentUser();
    console.log('AuthGuard - Utilisateur actuel:', currentUser);
    if (currentUser) {
      console.log('AuthGuard - Utilisateur déjà chargé, accès autorisé');
      return true;
    }
    
    // Sinon, vérifier le token auprès du serveur
    console.log('AuthGuard - Vérification du token auprès du serveur...');
    return this.authService.getMe().pipe(
      map(() => {
        console.log('AuthGuard - Token valide, accès autorisé');
        return true;
      }),
      catchError((err) => {
        console.error('AuthGuard - Token invalide:', err);
        this.authService.logout();
        return of(this.router.createUrlTree(['/login']));
      })
    );
  }
}
