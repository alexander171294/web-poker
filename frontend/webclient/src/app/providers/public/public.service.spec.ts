import { TestBed } from '@angular/core/testing';

import { PublicService } from './public.service';

describe('PublicService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PublicService = TestBed.get(PublicService);
    expect(service).toBeTruthy();
  });
});
