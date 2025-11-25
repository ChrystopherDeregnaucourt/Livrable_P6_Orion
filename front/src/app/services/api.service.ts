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
    return this.http.get<Article[]>(`${this.baseUrl}/posts`);
  }

  getArticle(id: number): Observable<ArticleDetail> {
    return this.http.get<ArticleDetail>(`${this.baseUrl}/posts/${id}`);
  }

  createArticle(payload: CreateArticlePayload): Observable<ArticleDetail> {
    return this.http.post<ArticleDetail>(`${this.baseUrl}/posts`, payload);
  }

  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.baseUrl}/topics`);
  }

  createTopic(payload: { title: string; description: string }): Observable<Theme> {
    return this.http.post<Theme>(`${this.baseUrl}/topics`, payload);
  }

  subscribeToTopic(topicId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/me/subscriptions/${topicId}`, {});
  }

  unsubscribeFromTopic(topicId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/users/me/subscriptions/${topicId}`);
  }

  addComment(articleId: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/posts/${articleId}/comments`, { content });
  }
}
