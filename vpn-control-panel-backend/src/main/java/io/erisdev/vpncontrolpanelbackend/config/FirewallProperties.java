package io.erisdev.vpncontrolpanelbackend.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "firewall")
@Getter
@Setter
public class FirewallProperties {
    private String clientRulesFile;
    private String nftConfigFile;

}
