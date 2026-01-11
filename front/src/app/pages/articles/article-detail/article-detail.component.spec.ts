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
    addComment: vi.fn(),
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
            snapshot: { paramMap: convertToParamMap({ id: '1' }) },
            paramMap: of(convertToParamMap({ id: '1' })),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch and display article details with correct content and semantics', () => {
    expect(mockArticleService.detail).toHaveBeenCalledWith('1');

    const title = fixture.debugElement.query(By.css('h1')).nativeElement
      .textContent;
    expect(title).toContain('Les Design Patterns en Java');

    const meta = fixture.debugElement.nativeElement.textContent;
    expect(meta).toContain('Jean Dev');

    const comments = fixture.debugElement.queryAll(By.css('.comment-item'));
    expect(comments.length).toBe(1);
    expect(comments[0].nativeElement.textContent).toContain(
      'Très instructif, merci !'
    );

    const themeTag = fixture.debugElement.query(By.css('.theme-tag'));
    expect(themeTag.nativeElement.getAttribute('aria-label')).toBe(
      'Thématique : Java'
    );

    const commentsSection = fixture.debugElement.query(
      By.css('section.comments-section')
    );
    expect(commentsSection.nativeElement.getAttribute('aria-labelledby')).toBe(
      'comments-title'
    );
  });

  it('should call addComment, update state and reset form on successful accessible submit', () => {
    const newComment = {
      author_name: 'Moi',
      content: 'Super !',
      created_at: '2026-01-08',
    };
    component.article.set(mockArticle);

    component.commentForm.controls.content.setValue('Super !');
    mockArticleService.addComment.mockReturnValue(of(newComment));

    fixture.detectChanges();
    const submitBtn = fixture.debugElement.query(
      By.css('button[aria-label="Envoyer le commentaire"]')
    );
    submitBtn.nativeElement.click();

    expect(mockArticleService.addComment).toHaveBeenCalled();
    expect(component.article()?.comments).toContain(newComment);
    expect(component.commentForm.get('content')?.value).toBe('');
  });

  it('should set aria-invalid on comment textarea when invalid and touched', () => {
    const textarea = fixture.debugElement.query(
      By.css('textarea#comment-content')
    ).nativeElement;

    component.commentForm.get('content')?.markAsTouched();
    component.commentForm.get('content')?.setValue('');
    fixture.detectChanges();

    expect(textarea.getAttribute('aria-invalid')).toBe('true');
  });
});
