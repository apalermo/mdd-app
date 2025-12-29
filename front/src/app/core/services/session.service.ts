import { Injectable, signal, computed } from '@angular/core';
import { User } from '../../models/user.interface';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly TOKEN_KEY = 'token';

  public user = signal<User | undefined>(undefined);
  public isLogged = computed(
    () => !!this.user() || !!localStorage.getItem(this.TOKEN_KEY)
  );

  public logIn(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  public updateUser(user: User): void {
    this.user.set(user);
  }

  public logOut(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.user.set(undefined);
  }

  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
}
