import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { NotificationService } from '../../core/services/notification.service';
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

  const mockUserService = {
    me: vi.fn().mockReturnValue(of(mockUser)),
    update: vi.fn().mockReturnValue(of(mockUser)),
  };

  const mockThemeService = { unsubscribe: vi.fn().mockReturnValue(of(void 0)) };

  const mockSessionService = {
    user: signal<User | undefined>(undefined),
    updateUser: vi.fn(),
  };

  const mockNotificationService = {
    show: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        provideRouter([]),
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ThemeService, useValue: mockThemeService },
        { provide: NotificationService, useValue: mockNotificationService },
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

  it('should load user and sync form', () => {
    expect(mockUserService.me).toHaveBeenCalled();
    expect(component.form.getRawValue()).toEqual({
      name: 'Dev',
      email: 't@t.com',
      password: '',
    });
  });

  it('should refresh data and show notification when clicking unsubscribe', () => {
    const btn = fixture.debugElement.query(By.css('.unsubscribe-btn'));
    expect(btn).toBeTruthy();

    if (btn) {
      vi.clearAllMocks();
      mockThemeService.unsubscribe.mockReturnValue(of(void 0));
      mockUserService.me.mockReturnValue(of(mockUser));

      btn.nativeElement.click();

      expect(mockThemeService.unsubscribe).toHaveBeenCalledWith(10);
      expect(mockUserService.me).toHaveBeenCalled();
      expect(mockNotificationService.show).toHaveBeenCalledWith(
        'Désabonnement pris en compte.'
      );
    }
  });

  it('should call update and show notification on success', () => {
    const newName = 'Dev Updated';
    component.form.controls['name'].setValue(newName);
    component.form.markAsDirty();

    component.submit();

    expect(mockUserService.update).toHaveBeenCalledWith(
      expect.objectContaining({
        name: newName,
        email: 't@t.com',
      })
    );
    expect(mockSessionService.updateUser).toHaveBeenCalled();
    expect(mockNotificationService.show).toHaveBeenCalledWith(
      'Vos modifications ont été enregistrées !'
    );
  });
});
