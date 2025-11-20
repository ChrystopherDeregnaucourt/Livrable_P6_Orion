import { Comment } from './comment.model';

export interface Article {
  id: number;
  title: string;
  content: string;
  author: string;
  date: string;
  theme: string;
}

export interface ArticleDetail extends Article {
  comments: Comment[];
}

export interface CreateArticlePayload {
  themeId: number;
  title: string;
  content: string;
}