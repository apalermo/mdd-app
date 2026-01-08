import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticleCreateComponent } from './article-create.component';
import { ArticleService } from '../../../core/services/article.service';
import { ThemeService } from '../../../core/services/theme.service';
import { provideRouter, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { vi } from 'vitest';

describe('ArticleCreateComponent', () => {
  let component: ArticleCreateComponent;
  let fixture: ComponentFixture<ArticleCreateComponent>;
  let router: Router;

  const mockArticleService = {
    create: vi.fn(),
  };

  const mockThemeService = {
    getThemes: vi.fn().mockReturnValue(of([{ id: 1, title: 'Java' }])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleCreateComponent, ReactiveFormsModule],
      providers: [
        provideRouter([]),
        { provide: ArticleService, useValue: mockArticleService },
        { provide: ThemeService, useValue: mockThemeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleCreateComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should initialize the form with empty values', () => {
    expect(component.articleForm).toBeDefined();
    expect(component.articleForm.get('title')?.value).toBe('');
  });

  it('should load themes on initialization', () => {
    expect(mockThemeService.getThemes).toHaveBeenCalled();
    expect(component.themes()?.length).toBe(1);
  });

  it('should call articleService.create and navigate on valid submit', () => {
    const articlePayload = {
      title: 'Nouveau titre',
      theme_id: 1,
      content: 'Contenu de test',
    };

    const navigateSpy = vi.spyOn(router, 'navigate');

    component.articleForm.setValue(articlePayload);
    mockArticleService.create.mockReturnValue(of(articlePayload));

    component.onSubmit();

    expect(mockArticleService.create).toHaveBeenCalledWith(articlePayload);
    expect(navigateSpy).toHaveBeenCalledWith(['/articles']);
  });

  it('should have an invalid form when fields are empty', () => {
    component.articleForm.setValue({ title: '', theme_id: null, content: '' });
    expect(component.articleForm.valid).toBeFalsy();
  });
});
