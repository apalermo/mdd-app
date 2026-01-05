import { Theme } from './theme.interface';

export interface User {
  id: number;
  email: string;
  name: string;
  subscriptions: Theme[];
  created_at: string;
  updated_at: string;
}
