package io.erisdev.vpncontrolpanelbackend.rest.dto;

import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import lombok.Data;

@Data
public class VpnClientResponseDTO {
    String downloadLink;
    VpnClient client;

}
