package io.erisdev.vpncontrolpanelbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vpn")
@Getter
@Setter
public class VpnProperties {

    private final Guest guest = new Guest();
    private final Admin admin = new Admin();
    private String defaultDestination;
    private String ccdDirectory;
    private String server;
    private String port;

    @Setter
    @Getter
    public static class Guest {
        private IpRange ipRange;
        private String subnet;
    }

    @Setter
    @Getter
    public static class Admin {
        private IpRange ipRange;
        private String subnet;
    }

    @Setter
    @Getter
    public static class IpRange {
        private String start;
        private String end;
    }
}
