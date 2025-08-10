package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientStatusDto {
    private String cn;
    private String realIp;
    private String vpnIp;
    private long bytesIn;
    private long bytesOut;
    private long connectedSinceEpoch;
    private String connectedSince;
    private String lastSeen;
    private String status;

}
