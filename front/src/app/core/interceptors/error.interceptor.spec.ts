import { TestBed } from '@angular/core/testing';
import {
  HttpClient,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { errorInterceptor } from './error.interceptor';
import { NotificationService } from '../services/notification.service';

describe('errorInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let notificationService: NotificationService;

  beforeEach(() => {
    const mockNotificationService = {
      show: vi.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([errorInterceptor])),
        provideHttpClientTesting(),
        { provide: NotificationService, useValue: mockNotificationService },
      ],
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    notificationService = TestBed.inject(NotificationService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should show notification on 400+ errors', () => {
    const errorMessage = 'Erreur personnalisée du serveur';

    httpClient.get('/test').subscribe({
      error: (err) => expect(err).toBeTruthy(),
    });

    const req = httpMock.expectOne('/test');
    req.flush(
      { message: errorMessage },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(notificationService.show).toHaveBeenCalledWith(errorMessage);
  });

  it('should show default message if no message in error body', () => {
    httpClient.get('/test').subscribe({
      error: (err) => expect(err).toBeTruthy(),
    });

    const req = httpMock.expectOne('/test');
    req.flush(null, { status: 500, statusText: 'Server Error' });

    expect(notificationService.show).toHaveBeenCalledWith(
      'Une erreur technique est survenue.'
    );
  });
});
