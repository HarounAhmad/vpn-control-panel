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
        private Boolean enableGeneration = false;
        private Boolean enableRevocation = false;
        private Boolean enableRenewal = false;
        private Boolean enableUpload = false;
        private Boolean enableDownload = false;
        private Boolean useSsh = false;
        private Socket socket = new Socket();
    }

    @Getter
    @Setter
    public static class Socket {
        private String path;
        private Ssh ssh = new Ssh();
        private int timeout = 1000;
    }
    @Getter
    @Setter
    public static class Ssh {
        private String user;
        private String password;
        private String host;
        private int port;
    }
}
