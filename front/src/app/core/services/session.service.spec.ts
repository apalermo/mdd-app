import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { User } from '../../models/user.interface';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(SessionService);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with user not logged in', () => {
    expect(service.isLogged()).toBeFalsy();
    expect(service.user()).toBeUndefined();
  });

  it('should log in user and store token', () => {
    const token = 'fake-jwt-token';

    service.logIn(token);

    expect(service.isLogged()).toBeTruthy();
    expect(localStorage.getItem('token')).toBe(token);
  });

  it('should update user information', () => {
    const mockUser: User = {
      id: 1,
      email: 'test@test.com',
      name: 'Tester',
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
    };

    service.updateUser(mockUser);

    expect(service.user()).toEqual(mockUser);
    expect(service.isLogged()).toBeTruthy();
  });

  it('should log out user and clear storage', () => {
    localStorage.setItem('token', 'fake-token');
    service.user.set({ id: 1 } as User);

    service.logOut();

    expect(service.user()).toBeUndefined();
    expect(service.isLogged()).toBeFalsy();
    expect(localStorage.getItem('token')).toBeNull();
  });
});
