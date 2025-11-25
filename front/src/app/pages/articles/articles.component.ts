import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, filter } from 'rxjs/operators';
import { ApiService } from '../../services/api.service';
import { Article } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss'],
  standalone: false
})
export class ArticlesComponent implements OnInit, OnDestroy {
  isMobileMenuOpen = false;
  articles: Article[] = [];
  sortBy: 'date' | 'title' | 'author' = 'date';
  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Vérifier si l'utilisateur est déjà chargé avec ses abonnements
    const currentUser = this.authService.getCurrentUser();
    
    if (currentUser && currentUser.subscriptions) {
      // Charger les articles immédiatement
      this.loadArticlesForUser(currentUser);
    } else {
      // Charger l'utilisateur depuis l'API puis charger les articles
      this.authService.getMe()
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (user) => this.loadArticlesForUser(user),
          error: (error) => console.error('Erreur lors du chargement de l\'utilisateur:', error)
        });
    }
    
    // S'abonner aux changements de l'utilisateur (ex: après abonnement/désabonnement)
    this.authService.currentUser$
      .pipe(
        filter(user => user !== null && user.subscriptions !== undefined),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (user) => this.loadArticlesForUser(user)
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadArticlesForUser(user: any): void {
    this.apiService.getArticles()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (articles) => {
          // Filtrer les articles pour ne garder que ceux des thèmes auxquels l'utilisateur est abonné
          if (user.subscriptions && user.subscriptions.length > 0) {
            const subscribedTopicIds = user.subscriptions.map((sub: any) => sub.id);
            this.articles = articles.filter(article => subscribedTopicIds.includes(article.topicId));
          } else {
            this.articles = [];
          }
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
        this.articles.sort((a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime());
        break;
      case 'title':
        this.articles.sort((a, b) => a.title.localeCompare(b.title));
        break;
      case 'author':
        this.articles.sort((a, b) => a.authorName.localeCompare(b.authorName));
        break;
    }
  }
}
