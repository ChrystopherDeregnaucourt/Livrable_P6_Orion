import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApiService } from '../../services/api.service';
import { ArticleDetail, Comment } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss'],
  standalone: false
})
export class ArticleDetailComponent implements OnInit, OnDestroy {
  isMobileMenuOpen = false;
  newComment = '';

  article: ArticleDetail | null = null;
  comments: Comment[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private apiService: ApiService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    if (articleId) {
      this.loadArticle(articleId);
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadArticle(articleId: number): void {
    this.apiService.getArticle(articleId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (article) => {
          this.article = article;
          this.comments = article.comments || [];
        },
        error: (error) => {
          console.error('Erreur lors du chargement de l\'article', error);
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
    this.router.navigate(['/themes']);
  }

  onNavigateToProfile(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/profile']);
  }

  onGoBack(): void {
    this.location.back();
  }

  onSubmitComment(): void {
    if (this.newComment && this.newComment.trim().length > 0 && this.article) {
      this.apiService.addComment(this.article.id, this.newComment.trim())
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (newComment) => {
            this.comments.push(newComment);
            this.newComment = '';
            console.log('Commentaire ajouté:', newComment);
          },
          error: (error) => {
            console.error('Erreur lors de l\'ajout du commentaire:', error);
          }
        });
    }
  }

  trackByCommentId(index: number, comment: Comment): number {
    return comment.id;
  }
}
