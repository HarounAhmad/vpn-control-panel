package io.erisdev.vpncontrolpanelbackend.rest.dto;

import io.erisdev.vpncontrolpanelbackend.model.ClientType;
import lombok.Data;

import java.util.Set;

@Data
public class VpnClientDTO {
    private String cn;
    private String description;
    private Set<String> allowedDestinations;
    private String assignedIp;
    private ClientType type;
    private String os;
}
