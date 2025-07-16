package io.erisdev.vpncontrolpanelbackend.firewall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NftRule {
    private String clientCn;
    private String srcIp;
    private String dstIp;
    private String protocol;
    private int dstPort;
}