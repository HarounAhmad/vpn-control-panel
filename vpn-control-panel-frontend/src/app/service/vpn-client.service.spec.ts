import { TestBed } from '@angular/core/testing';

import { VpnClientService } from './vpn-client.service';

describe('VpnClientService', () => {
  let service: VpnClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VpnClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
