package io.erisdev.vpncontrolpanelbackend.certificate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.erisdev.vpncontrolpanelbackend.config.CertificateProperties;
import lombok.RequiredArgsConstructor;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
final class UnixSocketClient {
    private final ObjectMapper om;
    private final CertificateProperties properties;

    CertdResponse call(CertdRequest req) {
        if (!properties.getOperations().getEnabled()) {
            throw new RuntimeException("vpn-certd operations are not enabled");
        }
        if (req == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        if (req.op == null || req.op.isBlank()) {
            throw new IllegalArgumentException("request operation cannot be null or blank");
        }
        var socketPath = Path.of(properties.getOperations().getSocket().getPath());
        var readTimeoutMs = properties.getOperations().getSocket().getTimeout();
        try (AFUNIXSocket s = AFUNIXSocket.newInstance()) {
            s.connect(new AFUNIXSocketAddress(socketPath.toFile()));
            s.setSoTimeout(Math.max(1, readTimeoutMs));

            try (var w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {
                w.write(om.writeValueAsString(req));
                w.write('\n');
                w.flush();
            }

            try (var r = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8))) {
                var line = r.readLine();
                if (line == null) throw new IOException("empty response");
                var resp = om.readValue(line, CertdResponse.class);
                if (!resp.ok()) throw new IOException(resp.err);
                return resp;
            }
        } catch (IOException e) {
            throw new RuntimeException("vpn-certd call failed", e);
        }
    }
}