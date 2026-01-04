import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../models/user.interface';
import { signal } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser: User = {
    id: 1,
    email: 't@t.com',
    name: 'Dev',
    subscriptions: [{ id: 10, title: 'Java', description: '' }],
    created_at: '',
    updated_at: '',
  };

  const mockUserService = { me: vi.fn().mockReturnValue(of(mockUser)) };
  const mockThemeService = { unsubscribe: vi.fn().mockReturnValue(of(void 0)) };
  const mockSessionService = {
    user: signal<User | undefined>(undefined),
    updateUser: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        provideRouter([]),
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ThemeService, useValue: mockThemeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user info via AsyncPipe', () => {
    expect(mockUserService.me).toHaveBeenCalled();

    const nameEl = fixture.debugElement.query(By.css('.info-value'));
    expect(nameEl.nativeElement.textContent).toContain('Dev');
  });

  it('should refresh data when clicking unsubscribe', () => {
    expect(mockUserService.me).toHaveBeenCalledTimes(1);

    const btn = fixture.debugElement.query(By.css('.unsubscribe-btn'));
    btn.nativeElement.click();

    expect(mockThemeService.unsubscribe).toHaveBeenCalledWith(10);
    expect(mockUserService.me).toHaveBeenCalledTimes(2); // Le refresh$ a déclenché un nouvel appel
  });
});
