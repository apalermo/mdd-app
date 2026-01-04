import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme } from '../../models/theme.interface';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private pathService = 'api/themes';

  constructor(private httpClient: HttpClient) {}

  public getThemes(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(this.pathService);
  }

  public subscribeToTheme(themeId: number): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/${themeId}/subscribe`,
      {}
    );
  }

  public unsubscribe(themeId: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.pathService}/${themeId}/unsubscribe`
    );
  }
}
