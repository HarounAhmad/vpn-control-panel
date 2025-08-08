package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.ClientStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientStatusRepository extends JpaRepository<ClientStatusEntity, String> {
}
