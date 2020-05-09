import { TestBed } from '@angular/core/testing';

import { ActionMenuFriendsService } from './action-menu-friends.service';

describe('ActionMenuFriendsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ActionMenuFriendsService = TestBed.get(ActionMenuFriendsService);
    expect(service).toBeTruthy();
  });
});
