import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ArticleService } from '../../../core/services/article.service';
import { ThemeService } from '../../../core/services/theme.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { ArticleRequest } from '../../../models/article.interface';

@Component({
  selector: 'app-article-create',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './article-create.component.html',
  styleUrls: ['./article-create.component.scss'],
})
export class ArticleCreateComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly articleService = inject(ArticleService);
  private readonly themeService = inject(ThemeService);

  public readonly themes = toSignal(this.themeService.getThemes(), {
    initialValue: [],
  });

  public readonly articleForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(5)]],
    theme_id: [null as number | null, Validators.required],
    content: ['', [Validators.required, Validators.minLength(10)]],
  });

  public onSubmit(): void {
    if (this.articleForm.valid) {
      const request = this.articleForm.getRawValue() as ArticleRequest;

      this.articleService.create(request).subscribe(() => {
        this.router.navigate(['/articles']);
      });
    }
  }
}
