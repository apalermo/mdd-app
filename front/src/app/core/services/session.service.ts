import { Injectable, signal, computed, inject } from '@angular/core';
import { User } from '../../models/user.interface';
import { Observable, tap } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly TOKEN_KEY = 'token';
  private readonly userService = inject(UserService);

  private $token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));

  public user = signal<User | undefined>(undefined);

  public isLogged = computed(() => !!this.$token());

  public logIn(token: string): Observable<User> {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.$token.set(token);
    return this.userService.me().pipe(tap((user) => this.user.set(user)));
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
