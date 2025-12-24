import { Injectable, signal, computed } from '@angular/core';
import { User } from '../../models/user.interface';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly TOKEN_KEY = 'token';

  // État réactif en mémoire
  public user = signal<User | null>(null);
  public isLogged = computed(
    () => !!this.user() || !!localStorage.getItem(this.TOKEN_KEY)
  );

  /**
   * Initialise la session avec le token et nettoie l'ancien utilisateur en mémoire
   */
  public logIn(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Met à jour les informations utilisateur en mémoire après l'appel /me
   */
  public updateUser(user: User): void {
    this.user.set(user);
  }

  /**
   * Déconnexion complète : nettoie le storage et la mémoire
   */
  public logOut(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.user.set(null);
  }

  /**
   * Récupère le token pour l'intercepteur
   */
  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
}
