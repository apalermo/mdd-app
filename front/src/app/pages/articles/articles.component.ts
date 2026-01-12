import { Component, inject, signal, computed } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArticleService } from '../../core/services/article.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-articles',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss'],
})
export class ArticlesComponent {
  private readonly articleService = inject(ArticleService);

  public readonly sortBy = signal<'date' | 'title'>('date');
  public readonly sortDirection = signal<'asc' | 'desc'>('desc');

  private readonly rawArticles = toSignal(this.articleService.all(), {
    initialValue: [],
  });

  public readonly articles = computed(() => {
    const list = [...this.rawArticles()];
    const criteria = this.sortBy();
    const direction = this.sortDirection();

    return list.sort((a, b) => {
      let comparison = 0;
      if (criteria === 'date') {
        comparison =
          new Date(a.created_at).getTime() - new Date(b.created_at).getTime();
      } else {
        comparison = a.title.localeCompare(b.title);
      }
      return direction === 'asc' ? comparison : -comparison;
    });
  });

  public toggleSortCriteria(): void {
    this.sortBy.update((current) => (current === 'date' ? 'title' : 'date'));
  }

  public toggleDirection(): void {
    this.sortDirection.update((current) =>
      current === 'asc' ? 'desc' : 'asc'
    );
  }
}
