import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { UserService } from '../../core/services/user.service';
import { SessionService } from '../../core/services/session.service';
import { provideRouter } from '@angular/router';
import { of, Subject } from 'rxjs';
import { User } from '../../models/user.interface';
import { signal } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUserService = {
    me: vi.fn(),
  };

  const userSignal = signal<User | undefined>(undefined);
  const mockSessionService = {
    user: userSignal,
    updateUser: vi.fn((u: User) => userSignal.set(u)),
  };

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    name: 'SuperDev',
    created_at: new Date().toISOString(),
    updated_at: new Date().toISOString(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        provideRouter([]),
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userSignal.set(undefined);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init and update session', () => {
    mockUserService.me.mockReturnValue(of(mockUser));
    fixture.detectChanges();

    expect(mockUserService.me).toHaveBeenCalled();
    expect(mockSessionService.updateUser).toHaveBeenCalledWith(mockUser);
  });

  it('should display loading message when user data is null (pending request)', () => {
    userSignal.set(undefined);

    const pendingRequest$ = new Subject<User>();
    mockUserService.me.mockReturnValue(pendingRequest$);

    fixture.detectChanges();

    const loadingElement = fixture.debugElement.query(By.css('.loading'));
    expect(loadingElement).toBeTruthy();
    expect(loadingElement.nativeElement.textContent).toContain(
      'Chargement de vos informations...'
    );
  });
});
