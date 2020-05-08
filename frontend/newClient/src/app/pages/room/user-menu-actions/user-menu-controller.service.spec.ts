import { TestBed } from '@angular/core/testing';

import { UserMenuControllerService } from './user-menu-controller.service';

describe('UserMenuControllerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserMenuControllerService = TestBed.get(UserMenuControllerService);
    expect(service).toBeTruthy();
  });
});
