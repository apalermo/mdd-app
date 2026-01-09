import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ArticleService } from '../../../core/services/article.service';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Article, ArticleComment } from '../../../models/article.interface';
import { switchMap, map, filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [RouterLink, DatePipe, ReactiveFormsModule],
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss'],
})
export class ArticleDetailComponent {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly articleService = inject(ArticleService);

  public readonly article = signal<Article | undefined>(undefined);

  public readonly commentForm = this.fb.nonNullable.group({
    content: ['', [Validators.required, Validators.minLength(2)]],
  });

  constructor() {
    this.route.paramMap
      .pipe(
        map((params) => params.get('id')),
        filter((id): id is string => !!id),
        switchMap((id) => this.articleService.detail(id)),
        takeUntilDestroyed()
      )
      .subscribe((data) => this.article.set(data));
  }

  public onSubmitComment(): void {
    const currentArticle = this.article();

    if (this.commentForm.valid && currentArticle) {
      const { content } = this.commentForm.getRawValue();

      this.articleService
        .addComment(currentArticle.id.toString(), content)
        .subscribe((newComment: ArticleComment) => {
          this.article.update((current) => {
            if (!current) return current;
            return {
              ...current,
              comments: [...current.comments, newComment],
            };
          });
          this.commentForm.reset();
        });
    }
  }
}
