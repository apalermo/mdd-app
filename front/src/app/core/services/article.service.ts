import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Article, ArticleRequest } from '../../models/article.interface';

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

  public create(articleRequest: ArticleRequest): Observable<void> {
    return this.http.post<void>(this.pathService, articleRequest);
  }

  public addComment(id: string, content: string): Observable<void> {
    return this.http.post<void>(`${this.pathService}/${id}/comments`, {
      content,
    });
  }
}
