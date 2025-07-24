package io.erisdev.vpncontrolpanelbackend.vpn;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.CCD;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CCDWriter {
    private static final String RULE_TEMPLATE = "ifconfig-push %s %s";
    private final VpnProperties vpnProperties;


    private Path ccdDirectory;

    @PostConstruct
    private void init() {
        ccdDirectory = Path.of(vpnProperties.getCcdDirectory());
    }

    public void writeCCD(CCD ccd) {
        Path file = ccdDirectory.resolve(ccd.getCn());
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(String.format(RULE_TEMPLATE, ccd.getIp(), ccd.getSubnet()));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write CCD file for " + ccd.getCn(), e);
        }
    }

    public void writeAll(List<CCD> ccdList) {
        for (CCD ccd : ccdList) {
            writeCCD(ccd);
        }
    }

    public void deleteCCD(String cn) {
        Path file = ccdDirectory.resolve(cn);
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete CCD file for " + cn, e);
        }
    }

}
