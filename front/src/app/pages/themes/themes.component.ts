import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

export interface Theme {
  id: number;
  title: string;
  description: string;
  isSubscribed: boolean;
}

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  isMobileMenuOpen = false;
  
  themes: Theme[] = [
    {
      id: 1,
      title: 'Angular',
      description: 'Tout sur le framework Angular et son écosystème',
      isSubscribed: true
    },
    {
      id: 2,
      title: 'React',
      description: 'Découvrez React et ses meilleures pratiques',
      isSubscribed: false
    },
    {
      id: 3,
      title: 'Vue.js',
      description: 'Le framework progressif pour construire des interfaces utilisateur',
      isSubscribed: false
    },
    {
      id: 4,
      title: 'TypeScript',
      description: 'JavaScript typé pour des applications plus robustes',
      isSubscribed: true
    },
    {
      id: 5,
      title: 'Node.js',
      description: 'Le runtime JavaScript côté serveur',
      isSubscribed: false
    },
    {
      id: 6,
      title: 'Spring Boot',
      description: 'Framework Java pour créer des applications robustes',
      isSubscribed: false
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    this.isMobileMenuOpen = false;
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
    // TODO: Navigate to profile page when created
    console.log('Navigation vers profil à implémenter');
  }

  toggleSubscription(theme: Theme): void {
    theme.isSubscribed = !theme.isSubscribed;
  }

  trackByThemeId(index: number, theme: Theme): number {
    return theme.id;
  }
}
