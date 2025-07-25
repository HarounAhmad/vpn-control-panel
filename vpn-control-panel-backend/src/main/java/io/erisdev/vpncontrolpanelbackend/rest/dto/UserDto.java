package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserDto {
    private String username;
    private Set<String> roles;
}
