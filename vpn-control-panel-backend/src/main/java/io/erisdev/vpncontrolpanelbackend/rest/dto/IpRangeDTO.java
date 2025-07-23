package io.erisdev.vpncontrolpanelbackend.rest.dto;

import io.erisdev.vpncontrolpanelbackend.model.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpRangeDTO {
    private String start;
    private String end;
    private ClientType clientType;
}
