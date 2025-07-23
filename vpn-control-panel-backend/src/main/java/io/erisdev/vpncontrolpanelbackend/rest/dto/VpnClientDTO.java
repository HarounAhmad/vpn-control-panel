package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.Data;

import java.util.Set;

@Data
public class VpnClientDTO {
    private String cn; // Common Name
    private String description;
    private Set<String> allowedDestinations;
    private String assignedIp; // Assigned IP address
}
