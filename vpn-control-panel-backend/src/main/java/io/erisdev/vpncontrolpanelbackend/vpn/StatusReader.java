package io.erisdev.vpncontrolpanelbackend.vpn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.ClientStatus;
import io.erisdev.vpncontrolpanelbackend.rest.dto.ClientStatusDto;
import io.erisdev.vpncontrolpanelbackend.vpn.util.SshHandle;
import lombok.RequiredArgsConstructor;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.SFTPClient;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Component
public class StatusReader {

    private final VpnProperties vpnProperties;
    private static final ObjectMapper M = new ObjectMapper();

    public List<ClientStatus> getStatus() {
        try (SshHandle h = SshHandle.connect(vpnProperties)) {
            String json = readViaSftp(h.client(), vpnProperties.getAgent().getStatusPath());
            if (json == null || json.isBlank() || json.trim().equals("null")) return List.of();
            return M.readValue(json, new TypeReference<List<ClientStatus>>() {});
        } catch (Exception e) {
            throw new RuntimeException("status read failed", e);
        }
    }

    private String readViaSftp(SSHClient ssh, String path) throws Exception {
        try (SFTPClient sftp = ssh.newSFTPClient(); RemoteFile rf = sftp.open(path)) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            long off = 0;
            byte[] tmp = new byte[65536];
            int n;
            while ((n = rf.read(off, tmp, 0, tmp.length)) > 0) {
                buf.write(tmp, 0, n);
                off += n;
            }
            return buf.toString(StandardCharsets.UTF_8);
        }
    }
}
