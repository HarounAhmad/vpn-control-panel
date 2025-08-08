package io.erisdev.vpncontrolpanelbackend.vpn.util;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import lombok.RequiredArgsConstructor;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.Closeable;
import java.io.IOException;

@RequiredArgsConstructor
public final class SshHandle implements Closeable {
    private final SSHClient ssh;

    public static SshHandle connect(VpnProperties vpnProperties)  throws Exception {
        SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(vpnProperties.getAgent().getHost(), vpnProperties.getAgent().getPort());
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
