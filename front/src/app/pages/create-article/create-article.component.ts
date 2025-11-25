import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { take } from 'rxjs';
import { ApiService } from '../../services/api.service';
import { Theme } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss'],
  standalone: false
})
export class CreateArticleComponent implements OnInit {
  articleForm!: FormGroup;
  isMobileMenuOpen = false;

  themes: Theme[] = [];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private location: Location,
    private apiService: ApiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      themeName: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]]
    });

    this.loadThemes();
  }

  private loadThemes(): void {
    this.apiService.getThemes()
      .pipe(take(1))
      .subscribe({
        next: (themes) => {
          this.themes = themes;
        },
        error: (error) => {
          console.error('Erreur lors du chargement des thèmes', error);
        }
      });
  }

  onSubmit(): void {
    if (this.articleForm.valid) {
      const formData = this.articleForm.value;
      const themeName = formData.themeName.trim();

      // Chercher si le thème existe déjà
      const existingTheme = this.themes.find(
        theme => theme.title.toLowerCase() === themeName.toLowerCase()
      );

      const payload: any = {
        title: formData.title,
        content: formData.content
      };

      // Si le thème existe, on utilise son ID, sinon on crée d'abord le thème
      if (existingTheme) {
        payload.topicId = existingTheme.id;
        this.createArticleWithPayload(payload);
      } else {
        // Créer d'abord le thème puis l'article
        this.apiService.createTopic({ title: themeName, description: '' })
          .pipe(take(1))
          .subscribe({
            next: (newTheme) => {
              payload.topicId = newTheme.id;
              this.createArticleWithPayload(payload);
            },
            error: (error) => {
              console.error('Erreur lors de la création du thème', error);
              alert('Erreur lors de la création du thème');
            }
          });
      }
    }
  }

  private createArticleWithPayload(payload: any): void {
    this.apiService.createArticle(payload)
      .pipe(take(1))
      .subscribe({
        next: (article) => {
          console.log('Article créé:', article);
          alert('Article créé avec succès !');
          this.router.navigate(['/articles']);
        },
        error: (error) => {
          console.error('Erreur lors de la création de l\'article', error);
          alert('Erreur lors de la création de l\'article');
        }
      });
  }

  onGoBack(): void {
    this.location.back();
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
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
}
