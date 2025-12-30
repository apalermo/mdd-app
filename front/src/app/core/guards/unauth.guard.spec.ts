import { TestBed } from '@angular/core/testing';
import {
  CanActivateFn,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { unauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';

describe('unauthGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => unauthGuard(...guardParameters));

  let router: Router;
  let sessionService: SessionService;

  const mockSessionService = {
    isLogged: vi.fn(),
  };

  const mockRouter = {
    navigate: vi.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
      ],
    });

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should allow navigation if user is NOT logged in', () => {
    mockSessionService.isLogged.mockReturnValue(false);

    const result = executeGuard(
      {} as ActivatedRouteSnapshot,
      {} as RouterStateSnapshot
    );

    expect(result).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should redirect to /me and block navigation if user IS logged in', () => {
    mockSessionService.isLogged.mockReturnValue(true);

    const result = executeGuard(
      {} as ActivatedRouteSnapshot,
      {} as RouterStateSnapshot
    );

    expect(result).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/me']); // TODO: Redirect to /articles once the feature is implemented
  });
});
