import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Article } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {
  isMobileMenuOpen = false;
  articles: Article[] = [];
  sortBy: 'date' | 'title' | 'author' = 'date';

  constructor(
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  private loadArticles(): void {
    this.apiService.getArticles().subscribe({
      next: (articles) => {
        this.articles = articles;
        this.sortArticles();
      },
      error: (error) => {
        console.error('Erreur lors du chargement des articles', error);
      }
    });
  }

  trackByArticleId(index: number, article: Article): number {
    return article.id;
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  onNavigateToArticles(): void {
    // Déjà sur la page articles
    this.isMobileMenuOpen = false;
  }

  onNavigateToThemes(): void {
    // TODO: Naviguer vers la page thèmes quand elle sera créée
    this.router.navigate(['/themes']);
  }

  onNavigateToProfile(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/profile']);
  }

  onCreateArticle(): void {
    this.router.navigate(['/articles/create']);
  }

  onArticleClick(articleId: number): void {
    this.router.navigate(['/articles', articleId]);
  }

  onSort(sortType: 'date' | 'title' | 'author'): void {
    this.sortBy = sortType;
    this.sortArticles();
  }

  private sortArticles(): void {
    switch (this.sortBy) {
      case 'date':
        this.articles.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
        break;
      case 'title':
        this.articles.sort((a, b) => a.title.localeCompare(b.title));
        break;
      case 'author':
        this.articles.sort((a, b) => a.author.localeCompare(b.author));
        break;
    }
  }
}
