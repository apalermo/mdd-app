import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ArticleService } from '../../../core/services/article.service';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Article } from '../../../models/article.interface';
import { switchMap, map, filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ArticleComment } from '../../../models/article.interface';

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

  public readonly commentForm = this.fb.group({
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
      .subscribe({
        next: (data) => this.article.set(data),
        error: (err) => console.error(err),
      });
  }

  public onSubmitComment(): void {
    const currentArticle = this.article();

    if (this.commentForm.valid && currentArticle) {
      const content = this.commentForm.get('content')?.value!;

      this.articleService
        .addComment(currentArticle.id.toString(), content)
        .subscribe({
          next: (newComment: ArticleComment) => {
            this.article.update((current) => {
              if (!current) return current;
              return {
                ...current,
                comments: [...current.comments, newComment],
              };
            });
            this.commentForm.reset();
          },
          error: (err) => console.error(err),
        });
    }
  }
}
