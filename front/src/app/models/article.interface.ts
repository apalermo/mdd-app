import { Theme } from './theme.interface';

export interface Article {
  id: number;
  title: string;
  content: string;
  author_name: string;
  theme: Pick<Theme, 'id' | 'title'>;
  created_at: string;
  comments: ArticleComment[];
}

export interface ArticleComment {
  author_name: string;
  content: string;
  created_at: string;
}

export interface ArticleRequest {
  title: string;
  content: string;
  theme_id: number;
}
