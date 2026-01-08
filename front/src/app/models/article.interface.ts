export interface Article {
  id: number;
  title: string;
  content: string;
  author_name: string;
  theme_title: string;
  created_at: string;
  comments?: Comment[];
}

export interface Comment {
  author_name: string;
  content: string;
  created_at: string;
}

export interface ArticleRequest {
  title: string;
  content: string;
  theme_id: number;
}
