import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticleDetailComponent } from './article-detail.component';
import { ArticleService } from '../../../core/services/article.service';
import { of } from 'rxjs';
import {
  ActivatedRoute,
  convertToParamMap,
  provideRouter,
} from '@angular/router';
import { By } from '@angular/platform-browser';

describe('ArticleDetailComponent', () => {
  let component: ArticleDetailComponent;
  let fixture: ComponentFixture<ArticleDetailComponent>;

  const mockArticle = {
    id: 1,
    title: 'Les Design Patterns en Java',
    content: 'Une analyse profonde des patterns Singleton et Factory.',
    author_name: 'Jean Dev',
    theme: { id: 1, title: 'Java' },
    created_at: '2026-01-08',
    comments: [
      {
        author_name: 'Alice',
        content: 'Très instructif, merci !',
        created_at: '2026-01-08',
      },
    ],
  };

  const mockArticleService = {
    detail: vi.fn().mockReturnValue(of(mockArticle)),
    addComment: vi.fn().mockReturnValue(of(void 0)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleDetailComponent],
      providers: [
        provideRouter([]),
        { provide: ArticleService, useValue: mockArticleService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({ id: '1' }),
            },
            paramMap: of(convertToParamMap({ id: '1' })),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch and display article details and comments', () => {
    expect(mockArticleService.detail).toHaveBeenCalledWith('1');

    const title = fixture.debugElement.query(By.css('h2')).nativeElement
      .textContent;
    expect(title).toContain('Les Design Patterns en Java');

    const meta = fixture.debugElement.nativeElement.textContent;
    expect(meta).toContain('Jean Dev');
    expect(meta).toContain('Java');

    const comments = fixture.debugElement.queryAll(By.css('.comment-item'));
    expect(comments.length).toBe(1);
    expect(comments[0].nativeElement.textContent).toContain('Très instructif');
  });

  it('should call addComment, update local state and reset form on success ', () => {
    const newComment = {
      author_name: 'Moi',
      content: 'Super !',
      created_at: '2026-01-08',
    };
    component.article.set(mockArticle);

    component.commentForm.controls.content.setValue('Super !');

    mockArticleService.addComment.mockReturnValue(of(newComment));

    component.onSubmitComment();

    fixture.detectChanges();

    expect(component.article()?.comments).toContain(newComment);
    expect(component.article()?.comments.length).toBe(2);

    expect(component.commentForm.get('content')?.value).toBe('');
  });
});
