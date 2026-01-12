import { Component, input } from '@angular/core';

@Component({
  selector: 'app-theme-card',
  standalone: true,
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent {
  public id = input.required<number | string>();
  public title = input.required<string>();
  public description = input.required<string>();
}
