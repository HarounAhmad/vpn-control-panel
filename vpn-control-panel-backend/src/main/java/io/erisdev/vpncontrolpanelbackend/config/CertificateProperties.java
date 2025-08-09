package io.erisdev.vpncontrolpanelbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "certificate")
@Getter
@Setter
public class CertificateProperties {

    private final Operations operations = new Operations();

    @Getter
    @Setter
    public static class Operations {
        private Boolean enabled = false;
    }
}
