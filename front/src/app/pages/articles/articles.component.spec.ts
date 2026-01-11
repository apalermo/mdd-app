import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticlesComponent } from './articles.component';
import { ArticleService } from '../../core/services/article.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';

describe('ArticlesComponent', () => {
  let component: ArticlesComponent;
  let fixture: ComponentFixture<ArticlesComponent>;

  const mockArticles = [
    {
      id: 1,
      title: 'Comprendre les Signals en Angular',
      content: 'Une nouvelle façon de gérer la réactivité.',
      author_name: 'Alice Dev',
      theme_title: 'Angular',
      created_at: '2026-01-08',
    },
  ];

  const mockArticleService = {
    all: vi.fn().mockReturnValue(of(mockArticles)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticlesComponent],
      providers: [
        provideRouter([]),
        { provide: ArticleService, useValue: mockArticleService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticlesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should display development articles in the list', () => {
    expect(mockArticleService.all).toHaveBeenCalled();

    const cards = fixture.debugElement.queryAll(By.css('.article-card'));
    expect(cards.length).toBe(1);

    const title = cards[0].query(By.css('h3')).nativeElement.textContent;
    expect(title).toContain('Comprendre les Signals en Angular');
  });

  it('should have accessible links for each article', () => {
    const firstCard = fixture.debugElement.query(By.css('.article-card'));
    expect(firstCard.nativeElement.tagName).toBe('A');
    expect(firstCard.nativeElement.getAttribute('aria-label')).toContain(
      'Comprendre les Signals en Angular'
    );
  });
});
