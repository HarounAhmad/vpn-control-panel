package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.CCD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCDRepository extends JpaRepository<CCD, String> {

}
