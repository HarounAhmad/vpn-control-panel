package io.erisdev.vpncontrolpanelbackend.vpn;

import io.erisdev.vpncontrolpanelbackend.model.OsTypes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class ClientConfigGenerator {
    public static String generateConfig(Map<String, String> values) throws IOException {
        if (values.get("os").equals(OsTypes.LINUX.name())) {
            return generateLinuxConfig(values);
        } else if (values.get("os").equals(OsTypes.WINDOWS.name())) {
            return generateWindowsConfig(values);
        } else {
            throw new IllegalArgumentException("Unsupported OS type: " + values.get("os"));
        }

    }

    private static String generateWindowsConfig(Map<String, String> values) {
        return "";
    }

    private static String generateLinuxConfig(Map<String, String> values) throws IOException {
        var template = Files.readString(
                new ClassPathResource("templates/client-linux.ovpn.template").getFile().toPath(),
                StandardCharsets.UTF_8
        );
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;

    }
}
