import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ArticleService } from './article.service';
import { Article, ArticleRequest } from '../../models/article.interface';

describe('ArticleService', () => {
  let service: ArticleService;
  let httpMock: HttpTestingController;

  const mockArticle: Article = {
    id: 1,
    title: 'Test',
    content: 'Content',
    author_name: 'Dev',
    theme: {
      id: 1,
      title: 'Java',
    },
    created_at: '',
    comments: [],
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ArticleService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(ArticleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch all articles (GET)', () => {
    service
      .all()
      .subscribe((articles) => expect(articles).toEqual([mockArticle]));
    const req = httpMock.expectOne('api/articles');
    expect(req.request.method).toBe('GET');
    req.flush([mockArticle]);
  });

  it('should fetch article detail (GET)', () => {
    service
      .detail('1')
      .subscribe((article) => expect(article).toEqual(mockArticle));
    const req = httpMock.expectOne('api/articles/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockArticle);
  });

  it('should create an article (POST)', () => {
    const request: ArticleRequest = {
      title: 'New',
      content: '...',
      theme_id: 1,
    };
    service.create(request).subscribe();
    const req = httpMock.expectOne('api/articles');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush(null);
  });

  it('should add a comment (POST)', () => {
    const content = 'Great article!';
    service.addComment('1', content).subscribe();
    const req = httpMock.expectOne('api/articles/1/comments');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ content });
    req.flush(null);
  });
});
