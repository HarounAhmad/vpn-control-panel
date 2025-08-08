package io.erisdev.vpncontrolpanelbackend.vpn.util;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.ClientStatus;
import io.erisdev.vpncontrolpanelbackend.repository.ClientStatusRepository;
import io.erisdev.vpncontrolpanelbackend.service.ClientStatusService;
import io.erisdev.vpncontrolpanelbackend.vpn.StatusReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class StatusPoller {
    private final StatusReader statusReader;
    private final VpnProperties cfg;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ClientStatusService clientStatusService;

    @Scheduled(fixedDelayString = "#{${vpn.agent.poll-seconds:5} * 1000}")
    public void poll() {
        if (running.getAndSet(true)) {
            log.warn("Status poll already running, skipping this cycle");
            return;
        }
        try {
            log.info("Starting status poll");
            List<ClientStatus> clientStatusList = statusReader.getStatus();
            clientStatusService.upsertAll(clientStatusList);
            log.info("Status poll completed successfully");
        } catch (Exception e) {
            log.error("Error during status poll", e);
        } finally {
            running.set(false);
        }
    }
}
