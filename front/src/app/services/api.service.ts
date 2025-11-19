import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Article, ArticleDetail, CreateArticlePayload, Comment, Theme } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.baseUrl}/articles`);
  }

  getArticle(id: number): Observable<ArticleDetail> {
    return this.http.get<ArticleDetail>(`${this.baseUrl}/articles/${id}`);
  }

  createArticle(payload: CreateArticlePayload): Observable<ArticleDetail> {
    return this.http.post<ArticleDetail>(`${this.baseUrl}/articles`, payload);
  }

  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.baseUrl}/topics`);
  }

  subscribeToTopic(topicId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/topics/${topicId}/subscribe`, {});
  }

  unsubscribeFromTopic(topicId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/topics/${topicId}/subscribe`);
  }

  addComment(articleId: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/articles/${articleId}/comments`, { content });
  }
}
