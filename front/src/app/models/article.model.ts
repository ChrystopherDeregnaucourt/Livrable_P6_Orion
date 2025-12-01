import { Comment } from './comment.model';

export interface Article {
  id: number;
  title: string;
  content: string;
  authorId: number;
  authorName: string;
  topicId: number;
  topicTitle: string;
  created_at: string;
}

export interface ArticleDetail extends Article {
  comments?: Comment[];
}

export interface CreateArticlePayload {
  topicId: number;
  title: string;
  content: string;
}