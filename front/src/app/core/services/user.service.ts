import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../models/user.interface';
import { UserUpdateRequest } from '../../models/user-update-request.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly http = inject(HttpClient);

  private readonly pathService = '/api/users';

  public me(): Observable<User> {
    return this.http.get<User>(`${this.pathService}/me`);
  }

  public update(userForm: UserUpdateRequest): Observable<User> {
    return this.http.put<User>(`${this.pathService}/me`, userForm);
  }
}
