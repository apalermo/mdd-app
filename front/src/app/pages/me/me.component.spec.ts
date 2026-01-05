import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
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

  it('should load user and sync form', () => {
    expect(mockUserService.me).toHaveBeenCalled();
    expect(component.form.getRawValue()).toEqual({
      name: 'Dev',
      email: 't@t.com',
      password: '',
    });
  });

  it('should refresh data when clicking unsubscribe', () => {
    const btn = fixture.debugElement.query(By.css('.unsubscribe-btn'));
    expect(btn).toBeTruthy();

    if (btn) {
      vi.clearAllMocks();
      mockThemeService.unsubscribe.mockReturnValue(of(void 0));
      mockUserService.me.mockReturnValue(of(mockUser));

      btn.nativeElement.click();

      expect(mockThemeService.unsubscribe).toHaveBeenCalledWith(10);
      expect(mockUserService.me).toHaveBeenCalled();

      expect(component.successMsg()).toBe('Désabonnement pris en compte.');
    }
  });

  it('should call update and show success message', () => {
    const newName = 'Dev Updated';
    component.form.controls['name'].setValue(newName);

    component.submit();
    fixture.detectChanges();

    expect(mockUserService.update).toHaveBeenCalledWith(
      expect.objectContaining({
        name: newName,
        email: 't@t.com',
      })
    );
    expect(mockSessionService.updateUser).toHaveBeenCalled();

    expect(component.successMsg()).toContain('enregistrées');

    const successMsg = fixture.debugElement.query(By.css('.msg-success'));
    expect(successMsg).toBeTruthy();
  });

  it('should display error message if update fails', () => {
    mockUserService.update.mockReturnValueOnce(
      throwError(() => new Error('Error'))
    );

    component.submit();
    fixture.detectChanges();

    expect(component.errorMsg()).toContain('Une erreur est survenue');
  });

  it('should not call update and show error message if form is invalid', () => {
    component.form.controls['email'].setValue('');

    component.submit();
    fixture.detectChanges();

    expect(mockUserService.update).not.toHaveBeenCalled();

    expect(component.errorMsg()).toBe(
      'Veuillez vérifier les champs du formulaire.'
    );

    const errorMsg = fixture.debugElement.query(By.css('.msg-error'));
    expect(errorMsg).toBeTruthy();
  });
});
