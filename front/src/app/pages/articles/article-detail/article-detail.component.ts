import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { ArticleService } from '../../../core/services/article.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss'],
})
export class ArticleDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly articleService = inject(ArticleService);

  private readonly articleId = this.route.snapshot.paramMap.get('id');

  public readonly article = toSignal(
    this.articleService.detail(this.articleId!),
    { initialValue: undefined }
  );
}
