package io.erisdev.vpncontrolpanelbackend.vpn.util;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import lombok.RequiredArgsConstructor;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public final class SshHandle implements Closeable {
    private final SSHClient ssh;

    public static SshHandle connect(VpnProperties vpnProperties)  throws Exception {
        SSHClient ssh = new SSHClient();
        try (InputStream is = SshHandle.class.getResourceAsStream("/ssh/known_hosts")) {
            if (is == null) {
                throw new IllegalStateException("Resource /ssh/known_hosts not found on classpath");
            }
            ssh.addHostKeyVerifier(new OpenSSHKnownHosts(new InputStreamReader(is, StandardCharsets.UTF_8)));
        }
        ssh.connect(vpnProperties.getAgent().getSsh().getHost(), vpnProperties.getAgent().getSsh().getPort());
        ssh.authPassword(vpnProperties.getAgent().getSsh().getUser(), vpnProperties.getAgent().getSsh().getPassword());
        return new SshHandle(ssh);
    }
    public SSHClient client() { return ssh; }

    @Override
    public void close() throws IOException {
        try { ssh.disconnect(); } catch (Exception ignore) {}
        try { ssh.close(); } catch (Exception ignore) {}
    }
}
