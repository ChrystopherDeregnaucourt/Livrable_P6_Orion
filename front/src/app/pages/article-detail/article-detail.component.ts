import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

export interface Article {
  id: number;
  title: string;
  date: Date;
  author: string;
  theme: string;
  content: string;
}

export interface Comment {
  id: number;
  username: string;
  content: string;
}

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  isMobileMenuOpen = false;
  newComment = '';
  
  article: Article = {
    id: 1,
    title: 'Titre de l\'article sélectionné',
    date: new Date(),
    author: 'Auteur',
    theme: 'Thème',
    content: 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'
  };

  comments: Comment[] = [
    {
      id: 1,
      username: 'username',
      content: 'contenu du commentaire'
    }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    // TODO: Récupérer l'ID de l'article depuis la route et charger les données
    // const articleId = this.route.snapshot.paramMap.get('id');
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
    this.router.navigate(['/themes']);
  }

  onNavigateToProfile(): void {
    this.isMobileMenuOpen = false;
    // TODO: Navigate to profile page when created
    console.log('Navigation vers profil à implémenter');
  }

  onGoBack(): void {
    this.location.back();
  }

  onSubmitComment(): void {
    if (this.newComment && this.newComment.trim().length > 0) {
      const newCommentObj: Comment = {
        id: this.comments.length + 1,
        username: 'Utilisateur actuel', // TODO: Récupérer le username de l'utilisateur connecté
        content: this.newComment.trim()
      };
      this.comments.push(newCommentObj);
      this.newComment = '';
    }
  }

  trackByCommentId(index: number, comment: Comment): number {
    return comment.id;
  }
}
