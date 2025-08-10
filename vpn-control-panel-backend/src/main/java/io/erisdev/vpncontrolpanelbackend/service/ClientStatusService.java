package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.model.ClientStatus;
import io.erisdev.vpncontrolpanelbackend.model.ClientStatusEntity;
import io.erisdev.vpncontrolpanelbackend.repository.ClientStatusRepository;
import io.erisdev.vpncontrolpanelbackend.rest.dto.ClientStatusDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientStatusService {

    private final ClientStatusRepository repo;


    public List<ClientStatusDto> get() {
        return repo.findAll().stream()
                .map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public ClientStatusDto findById(String cn) {
        Optional<ClientStatusEntity> entityOpt = repo.findById(cn);
        if (entityOpt.isEmpty()) return ClientStatusDto.builder().status("OFFLINE").build();
        return mapEntityToDto(entityOpt.get());
    }


    private ClientStatusDto mapEntityToDto(ClientStatusEntity entity) {
        return ClientStatusDto.builder()
                .cn(entity.getCn())
                .realIp(entity.getRealIp())
                .vpnIp(entity.getVpnIp())
                .bytesIn(entity.getBytesIn())
                .bytesOut(entity.getBytesOut())
                .connectedSinceEpoch(entity.getConnectedSinceEpoch())
                .connectedSince(entity.getConnectedSince())
                .status(entity.getStatus())
                .lastSeen(entity.getLastSeen())
                .build();
    }


    @Transactional
    public void upsertAll(List<ClientStatus> incoming) {
        if (incoming == null || incoming.isEmpty()) return;

        Map<String, ClientStatus> inById = incoming.stream()
                .collect(Collectors.toMap(ClientStatus::getCn, Function.identity(), (a, b)->b));

        List<String> ids = new ArrayList<>(inById.keySet());
        Map<String, ClientStatusEntity> existing = repo.findAllById(ids).stream()
                .collect(Collectors.toMap(ClientStatusEntity::getCn, Function.identity()));

        List<ClientStatusEntity> toSave = new ArrayList<>(incoming.size());
        for (ClientStatus s : incoming) {
            ClientStatusEntity e = existing.get(s.getCn());
            if (e == null) {
                e = new ClientStatusEntity();
                e.setCn(s.getCn());
            }
            e.setRealIp(s.getRealIp());
            e.setVpnIp(s.getVpnIp());
            e.setBytesIn(s.getBytesIn());
            e.setBytesOut(s.getBytesOut());
            e.setConnectedSinceEpoch(s.getConnectedSinceEpoch());
            e.setConnectedSince(s.getConnectedSince());
            e.setStatus(s.getStatus());
            toSave.add(e);
        }
        List<ClientStatusEntity> activeClients = checkActive(toSave);

        repo.saveAll(activeClients);
    }

    @Transactional
    protected List<ClientStatusEntity> checkActive(List<ClientStatusEntity> incoming) {
        var online = incoming.stream()
                .map(c -> normalize(c.getCn()))
                .filter(s -> s != null && !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());

        var clients = repo.findAll();

        for (var client : clients) {
            var cn = normalize(client.getCn());
            boolean found = cn != null && online.contains(cn);

            if (found) {
                client.setStatus("ONLINE");
                client.setLastSeen(java.time.Instant.now().toString());
            } else {
                client.setStatus("OFFLINE");
                if (client.getLastSeen() == null) {
                    client.setLastSeen(java.time.Instant.now().toString());
                }
            }
        }

        return clients;
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase(java.util.Locale.ROOT);
    }

}
