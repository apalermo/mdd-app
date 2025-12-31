import { TestBed } from '@angular/core/testing';
import { ThemeService } from './theme.service';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { Theme } from '../../models/theme.interface';

describe('ThemeService', () => {
  let service: ThemeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ThemeService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(ThemeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all themes via GET /api/themes', () => {
    const mockThemes: Theme[] = [{ id: 1, title: 'Java', description: 'Cool' }];

    service.getThemes().subscribe((themes) => {
      expect(themes.length).toBe(1);
      expect(themes).toEqual(mockThemes);
    });

    const req = httpMock.expectOne('api/themes');
    expect(req.request.method).toBe('GET');
    req.flush(mockThemes);
  });

  it('should subscribe to a theme via POST /api/themes/:id/subscribe', () => {
    const themeId = 1;

    service.subscribe(themeId).subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/themes/${themeId}/subscribe`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should unsubscribe from a theme via DELETE /api/themes/:id/unsubscribe', () => {
    const themeId = 1;

    service.unsubscribe(themeId).subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/themes/${themeId}/unsubscribe`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
