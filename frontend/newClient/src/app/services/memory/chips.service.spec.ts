import { TestBed } from '@angular/core/testing';

import { ChipsService } from './chips.service';

describe('ChipsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ChipsService = TestBed.get(ChipsService);
    expect(service).toBeTruthy();
  });
});
