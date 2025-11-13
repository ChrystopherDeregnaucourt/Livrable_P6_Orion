import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

interface Theme {
  id: number;
  name: string;
}

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {
  articleForm!: FormGroup;
  isMobileMenuOpen = false;
  
  // Mock themes - À remplacer par un appel API
  themes: Theme[] = [
    { id: 1, name: 'JavaScript' },
    { id: 2, name: 'TypeScript' },
    { id: 3, name: 'Angular' },
    { id: 4, name: 'React' },
    { id: 5, name: 'Vue.js' }
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      theme: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit(): void {
    if (this.articleForm.valid) {
      const formData = this.articleForm.value;
      console.log('Article à créer:', formData);
      
      // TODO: Appel API pour créer l'article
      // this.articleService.createArticle(formData).subscribe(...)
      
      // Redirection vers la liste des articles
      this.router.navigate(['/articles']);
    }
  }

  onGoBack(): void {
    this.location.back();
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    // TODO: Appel API de déconnexion
    this.router.navigate(['/login']);
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

