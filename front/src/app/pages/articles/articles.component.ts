import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { ArticleService } from '../../core/services/article.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-articles',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss'],
})
export class ArticlesComponent {
  private readonly articleService = inject(ArticleService);

  private readonly articlesRaw = toSignal(this.articleService.all(), {
    initialValue: [],
  });

  public sortBy = signal<'date' | 'title'>('date');

  public articles = computed(() => {
    const list = [...this.articlesRaw()];
    return list.sort((a, b) => {
      if (this.sortBy() === 'date') {
        return (
          new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
        );
      }
      return a.title.localeCompare(b.title);
    });
  });

  public updateSort(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as 'date' | 'title';
    this.sortBy.set(value);
  }
}
