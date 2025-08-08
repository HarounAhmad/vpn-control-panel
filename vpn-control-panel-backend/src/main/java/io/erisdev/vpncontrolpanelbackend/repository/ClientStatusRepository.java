package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.ClientStatusDatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientStatusRepository extends JpaRepository<ClientStatusDatabaseEntity, String> {
}
