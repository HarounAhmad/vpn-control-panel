package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import io.erisdev.vpncontrolpanelbackend.repository.VpnClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VpnClientService {
    private final VpnClientRepository vpnClientRepository;

    public List<VpnClient> findAll() {
        return vpnClientRepository.findAll();
    }

    public VpnClient findByCn(String cn) {
        return vpnClientRepository.findById(cn).orElse(null);
    }


    public VpnClient createClient(VpnClient client) {
        client.setCreatedAt(Instant.now());
        client.setRevoked(false);
        return vpnClientRepository.save(client);
    }

    public VpnClient updateDestinations(String cn, Set<String> newDestinations) {
        VpnClient client = findByCn(cn);
        if (client != null) {
            client.setAllowedDestinations(newDestinations);
            return vpnClientRepository.save(client);
        }
        return null;
    }

    public void revokeClient(String cn) {
        VpnClient client = vpnClientRepository.findById(cn).orElseThrow();
        client.setRevoked(true);
        vpnClientRepository.save(client);
    }

    public void deleteClient(String cn) {
        vpnClientRepository.deleteById(cn);
    }

}
