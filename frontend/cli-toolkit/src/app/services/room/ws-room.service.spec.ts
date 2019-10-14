import { TestBed } from '@angular/core/testing';

import { WsRoomService } from './ws-room.service';

describe('WsRoomService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WsRoomService = TestBed.get(WsRoomService);
    expect(service).toBeTruthy();
  });
});
