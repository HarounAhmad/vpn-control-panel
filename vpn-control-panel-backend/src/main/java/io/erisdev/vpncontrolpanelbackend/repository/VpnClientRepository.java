package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VpnClientRepository extends JpaRepository<VpnClient, String> {
}
