import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../../models/user.interface';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  public isLogged = false;
  public sessionInformation: { token: string; user?: User } | undefined;

  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(token: string, user: User): void {
    this.sessionInformation = { token, user };
    this.isLogged = true;
    this.next();

    localStorage.setItem('token', token);
  }

  public logOut(): void {
    this.sessionInformation = undefined;
    this.isLogged = false;
    this.next();

    localStorage.removeItem('token');
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
}
