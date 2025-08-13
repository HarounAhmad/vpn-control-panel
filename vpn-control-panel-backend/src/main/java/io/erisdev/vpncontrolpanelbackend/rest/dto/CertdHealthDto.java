package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.Data;

@Data
public class CertdHealthDto {
    private String serial;
    private String notAfter;
}
