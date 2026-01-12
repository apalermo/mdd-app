import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BackButtonComponent } from './back-button.component';
import { provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';

describe('BackButtonComponent', () => {
  let component: BackButtonComponent;
  let fixture: ComponentFixture<BackButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BackButtonComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(BackButtonComponent);
    component = fixture.componentInstance;
  });

  it('should render the correct link and aria-label', () => {
    fixture.componentRef.setInput('destination', '/test-path');
    fixture.componentRef.setInput('label', 'Retour test');
    fixture.detectChanges();

    const link = fixture.debugElement.query(By.css('a'));
    expect(link.nativeElement.getAttribute('href')).toBe('/test-path');
    expect(link.nativeElement.getAttribute('aria-label')).toBe('Retour test');
  });
});
