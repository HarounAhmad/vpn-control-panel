package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.ClientConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientConfigRepository extends JpaRepository<ClientConfig, String> {
}
