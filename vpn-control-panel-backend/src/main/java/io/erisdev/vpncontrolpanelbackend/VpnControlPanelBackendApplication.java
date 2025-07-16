package io.erisdev.vpncontrolpanelbackend;

import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRuleIO;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public class VpnControlPanelBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpnControlPanelBackendApplication.class, args);
    }

}
