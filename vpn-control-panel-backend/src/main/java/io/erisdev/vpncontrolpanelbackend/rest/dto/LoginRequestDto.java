package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
