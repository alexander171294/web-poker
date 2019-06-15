import { TestBed } from '@angular/core/testing';

import { LobbyService } from './lobby.service';

describe('LobbyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LobbyService = TestBed.get(LobbyService);
    expect(service).toBeTruthy();
  });
});
