package io.erisdev.vpncontrolpanelbackend;

import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRuleIO;
import io.erisdev.vpncontrolpanelbackend.model.User;
import io.erisdev.vpncontrolpanelbackend.model.UserRole;
import io.erisdev.vpncontrolpanelbackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class VpnControlPanelBackendApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(VpnControlPanelBackendApplication.class, args);
    }
    @PostConstruct
    public void initTestUsers() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(UserRole.SYSADMIN))
                    .build();
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("auditor").isEmpty()) {
            User user = User.builder()
                    .username("auditor")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(UserRole.AUDITOR))
                    .build();
            userRepository.save(user);
        }
    }
}
