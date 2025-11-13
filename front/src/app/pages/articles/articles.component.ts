import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface Article {
  id: number;
  title: string;
  content: string;
  author: string;
  date: Date;
}

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {
  isMobileMenuOpen = false;
  articles: Article[] = [];
  sortBy: 'date' | 'title' | 'author' = 'date';

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadMockArticles();
  }

  private loadMockArticles(): void {
    // Données de test en attendant l'API
    this.articles = [
      {
        id: 1,
        title: 'Titre de l\'article',
        content: 'Content lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled...',
        author: 'Auteur',
        date: new Date('2024-01-15')
      },
      {
        id: 2,
        title: 'Titre de l\'article',
        content: 'Content lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled...',
        author: 'Auteur',
        date: new Date('2024-01-20')
      },
      {
        id: 3,
        title: 'Titre de l\'article',
        content: 'Content lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled...',
        author: 'Auteur',
        date: new Date('2024-01-10')
      },
      {
        id: 4,
        title: 'Titre de l\'article',
        content: 'Content lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled...',
        author: 'Auteur',
        date: new Date('2024-01-25')
      }
    ];
    this.sortArticles();
  }

  trackByArticleId(index: number, article: Article): number {
    return article.id;
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    // TODO: Implémenter la déconnexion
    console.log('Déconnexion');
    this.router.navigate(['/home']);
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
    // TODO: Naviguer vers la page profil quand elle sera créée
    console.log('Navigation vers Profil');
    this.isMobileMenuOpen = false;
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
        this.articles.sort((a, b) => b.date.getTime() - a.date.getTime());
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
