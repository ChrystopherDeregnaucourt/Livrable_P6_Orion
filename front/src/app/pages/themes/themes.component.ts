import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Theme } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  isMobileMenuOpen = false;

  themes: Theme[] = [];

  constructor(
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadThemes();
  }

  private loadThemes(): void {
    this.apiService.getThemes().subscribe({
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
      this.apiService.unsubscribeFromTopic(theme.id).subscribe({
        next: () => {
          theme.subscribed = false;
          console.log('Désabonné du thème:', theme.name);
        },
        error: (error) => {
          console.error('Erreur lors du désabonnement:', error);
        }
      });
    } else {
      this.apiService.subscribeToTopic(theme.id).subscribe({
        next: () => {
          theme.subscribed = true;
          console.log('Abonné au thème:', theme.name);
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
