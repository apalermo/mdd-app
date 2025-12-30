import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a login button with correct link', () => {
    const loginBtn = fixture.debugElement.query(
      By.css('button[routerLink="/login"]')
    );
    expect(loginBtn).toBeTruthy();
  });

  it('should have a register button with correct link', () => {
    const registerBtn = fixture.debugElement.query(
      By.css('button[routerLink="/register"]')
    );
    expect(registerBtn).toBeTruthy();
  });
});
