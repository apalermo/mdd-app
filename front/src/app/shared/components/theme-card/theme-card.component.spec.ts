import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ThemeCardComponent } from './theme-card.component';
import { Component } from '@angular/core';
import { By } from '@angular/platform-browser';

@Component({
  standalone: true,
  imports: [ThemeCardComponent],
  template: `
    <app-theme-card id="1" title="Java" description="Desc">
      <button action-button>Test Button</button>
    </app-theme-card>
  `,
})
class TestHostComponent {}

describe('ThemeCardComponent', () => {
  it('should project the action button correctly', () => {
    const fixture = TestBed.createComponent(TestHostComponent);
    fixture.detectChanges();

    const projectedBtn = fixture.debugElement.query(
      By.css('button[action-button]')
    );
    expect(projectedBtn).toBeTruthy();
    expect(projectedBtn.nativeElement.textContent).toBe('Test Button');
  });
});
