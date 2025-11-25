import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApiService } from '../../services/api.service';
import { Theme } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss'],
  standalone: false
})
export class ThemesComponent implements OnInit, OnDestroy {
  isMobileMenuOpen = false;

  themes: Theme[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadThemes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadThemes(): void {
    this.apiService.getThemes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (themes) => {
          this.themes = themes;
        },
        error: (error) => {
          console.error('Erreur lors du chargement des thèmes', error);
        }
      });
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    this.isMobileMenuOpen = false;
    this.authService.logout();
    this.router.navigate(['/']);
  }

  onNavigateToArticles(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/articles']);
  }

  onNavigateToThemes(): void {
    this.isMobileMenuOpen = false;
    // Already on themes page
  }

  onNavigateToProfile(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/profile']);
  }

  toggleSubscription(theme: Theme): void {
    const wasSubscribed = theme.subscribed;
    
    if (wasSubscribed) {
      this.apiService.unsubscribeFromTopic(theme.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            theme.subscribed = false;
            // Recharger l'utilisateur pour mettre à jour ses abonnements
            this.authService.getMe().pipe(takeUntil(this.destroy$)).subscribe();
          },
          error: (error) => {
            console.error('Erreur lors du désabonnement:', error);
          }
        });
    } else {
      this.apiService.subscribeToTopic(theme.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            theme.subscribed = true;
            // Recharger l'utilisateur pour mettre à jour ses abonnements
            this.authService.getMe().pipe(takeUntil(this.destroy$)).subscribe();
          },
          error: (error) => {
            console.error('Erreur lors de l\'abonnement:', error);
          }
        });
    }
  }

  trackByThemeId(index: number, theme: Theme): number {
    return theme.id;
  }
}
