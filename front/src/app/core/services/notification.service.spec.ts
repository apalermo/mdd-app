import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationService);
  });

  it('should set message and clear it after 5 seconds', fakeAsync(() => {
    service.show('Test Message');
    expect(service.message()).toBe('Test Message');

    tick(5000);
    expect(service.message()).toBeNull();
  }));
});
