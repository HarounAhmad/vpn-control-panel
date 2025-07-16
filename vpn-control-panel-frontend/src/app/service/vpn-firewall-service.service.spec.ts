import { TestBed } from '@angular/core/testing';

import { VpnFirewallServiceService } from './vpn-firewall-service.service';

describe('VpnFirewallServiceService', () => {
  let service: VpnFirewallServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VpnFirewallServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
