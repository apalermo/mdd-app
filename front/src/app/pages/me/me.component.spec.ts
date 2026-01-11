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
    subscriptions: [{ id: 10, title: 'Java', description: 'Desc Java' }],
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
  const mockNotificationService = { show: vi.fn() };

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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user data and sync form', () => {
    const nameInput = fixture.debugElement.query(By.css('input#profile-name'));
    expect(nameInput.nativeElement.value).toBe('Dev');
    const label = fixture.debugElement.query(
      By.css('label[for="profile-name"]')
    );
    expect(label).toBeTruthy();
  });

  it('should disable save button if form is invalid or pristine', () => {
    const saveBtn = fixture.debugElement.query(
      By.css('button[type="submit"]')
    ).nativeElement;
    expect(saveBtn.disabled).toBe(true);
    component.form.controls['name'].setValue('ab');
    component.form.markAsDirty();
    fixture.detectChanges();
    expect(saveBtn.disabled).toBe(true);
  });

  it('should call update and show notification on success', () => {
    component.form.controls['name'].setValue('Dev Updated');
    component.form.markAsDirty();
    fixture.detectChanges();

    const saveBtn = fixture.debugElement.query(
      By.css('button[aria-label="Enregistrer les modifications du profil"]')
    );
    saveBtn.nativeElement.click();

    expect(mockUserService.update).toHaveBeenCalled();
    expect(mockNotificationService.show).toHaveBeenCalledWith(
      'Vos modifications ont été enregistrées !'
    );
  });

  it('should unsubscribe and show notification', () => {
    const unsubBtn = fixture.debugElement.query(
      By.css('button[aria-label="Se désabonner du thème Java"]')
    );
    unsubBtn.nativeElement.click();
    expect(mockThemeService.unsubscribe).toHaveBeenCalledWith(10);
    expect(mockNotificationService.show).toHaveBeenCalledWith(
      'Désabonnement pris en compte.'
    );
  });

  it('should display empty state message when no subscriptions', () => {
    mockUserService.me.mockReturnValue(of({ ...mockUser, subscriptions: [] }));
    component['refresh$'].next();
    fixture.detectChanges();
    const noSubsMsg = fixture.debugElement.query(By.css('.no-subs'));
    expect(noSubsMsg.nativeElement.textContent).toContain(
      "Vous n'êtes abonné à aucun thème"
    );
  });
});
