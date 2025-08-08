package io.erisdev.vpncontrolpanelbackend.vpn.util;

import io.erisdev.vpncontrolpanelbackend.model.ClientStatus;

import java.util.List;

public interface StatusSource {
    List<ClientStatus> read() throws Exception;
}
