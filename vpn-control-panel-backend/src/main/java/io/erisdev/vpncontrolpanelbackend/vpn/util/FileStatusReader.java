package io.erisdev.vpncontrolpanelbackend.vpn.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.ClientStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;


@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "vpn.agent.status-mode", havingValue = "file")
public class FileStatusReader implements StatusSource {

    private static final ObjectMapper M = new ObjectMapper();
    private final VpnProperties vpnProperties;

    @Override
    public List<ClientStatus> read() throws Exception {
        return getStatus();
    }

    public List<ClientStatus> getStatus() throws Exception {
        var path = vpnProperties.getAgent().getStatusPath();
        var bytes = Files.readAllBytes(Path.of(path));
        var json = new String(bytes, StandardCharsets.UTF_8);
        return M.readValue(json, M.getTypeFactory().constructCollectionType(List.class, ClientStatus.class));
    }

}
