import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { Theme } from '../../models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
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
      themeId: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]]
    });

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

  onSubmit(): void {
    if (this.articleForm.valid) {
      const formData = this.articleForm.value;

      this.apiService.createArticle({
        themeId: Number(formData.themeId),
        title: formData.title,
        content: formData.content
      }).subscribe({
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
