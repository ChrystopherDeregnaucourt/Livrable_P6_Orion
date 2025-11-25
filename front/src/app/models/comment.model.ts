export interface Comment {
  id: number;
  postId: number;
  authorId: number;
  authorName: string;
  content: string;
  created_at: string;
}
