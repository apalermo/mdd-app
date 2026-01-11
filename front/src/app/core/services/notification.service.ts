import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  public message = signal<string | null>(null);

  public show(msg: string) {
    this.message.set(msg);
    setTimeout(() => this.message.set(null), 5000);
  }
}
