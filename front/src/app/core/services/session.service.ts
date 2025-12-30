import { Injectable, signal, computed } from '@angular/core';
import { User } from '../../models/user.interface';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly TOKEN_KEY = 'token';

  private $token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));

  public user = signal<User | undefined>(undefined);

  public isLogged = computed(() => !!this.$token());

  public logIn(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.$token.set(token);
  }

  public updateUser(user: User): void {
    this.user.set(user);
  }

  public logOut(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.$token.set(null);
    this.user.set(undefined);
  }

  public getToken(): string | null {
    return this.$token();
  }
}
