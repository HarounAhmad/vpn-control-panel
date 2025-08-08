package io.erisdev.vpncontrolpanelbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatus {
    @JsonProperty("cn")  private String cn;
    @JsonProperty("real_ip") private String realIp;
    @JsonProperty("vpn_ip")  private String vpnIp;
    @JsonProperty("bytes_in")  private long bytesIn;
    @JsonProperty("bytes_out") private long bytesOut;
    @JsonProperty("connected_since_epoch") private long connectedSinceEpoch;
    @JsonProperty("connected_since") private String connectedSince;
    private String status;

}
