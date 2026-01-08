import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  ArticleComment,
  Article,
  ArticleRequest,
} from '../../models/article.interface';

@Injectable({ providedIn: 'root' })
export class ArticleService {
  private readonly http = inject(HttpClient);
  private readonly pathService = 'api/articles';

  public all(): Observable<Article[]> {
    return this.http.get<Article[]>(this.pathService);
  }

  public detail(id: string): Observable<Article> {
    return this.http.get<Article>(`${this.pathService}/${id}`);
  }

  public create(articleRequest: ArticleRequest): Observable<Article> {
    return this.http.post<Article>(this.pathService, articleRequest);
  }

  public addComment(id: string, content: string): Observable<ArticleComment> {
    return this.http.post<ArticleComment>(
      `${this.pathService}/${id}/comments`,
      {
        content,
      }
    );
  }
}
