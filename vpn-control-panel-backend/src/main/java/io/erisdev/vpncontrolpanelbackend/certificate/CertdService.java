package io.erisdev.vpncontrolpanelbackend.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertdService {
    private final UnixSocketClient client;

    public CertdResponse health() {
        return client.call(CertdRequest.op(CertdOp.HEALTH));
    }
}

