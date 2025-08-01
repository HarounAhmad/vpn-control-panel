package io.erisdev.vpncontrolpanelbackend.repository;

import io.erisdev.vpncontrolpanelbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
