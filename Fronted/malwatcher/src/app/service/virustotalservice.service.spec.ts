import { TestBed } from '@angular/core/testing';

import { VirustotalserviceService } from './virustotalservice.service';

describe('VirustotalserviceService', () => {
  let service: VirustotalserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VirustotalserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
