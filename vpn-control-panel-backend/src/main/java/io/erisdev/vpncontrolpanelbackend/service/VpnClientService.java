package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.CCD;
import io.erisdev.vpncontrolpanelbackend.model.ClientType;
import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import io.erisdev.vpncontrolpanelbackend.repository.CCDRepository;
import io.erisdev.vpncontrolpanelbackend.repository.VpnClientRepository;
import io.erisdev.vpncontrolpanelbackend.rest.dto.IpRangeDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VpnClientService {
    private final VpnClientRepository vpnClientRepository;
    private final CCDRepository ccdRepository;
    private final VpnProperties vpnProperties;

    public List<VpnClient> findAll() {
        return vpnClientRepository.findAll();
    }

    public VpnClient findByCn(String cn) {
        return vpnClientRepository.findById(cn).orElse(null);
    }


    public VpnClient createClient(VpnClientDTO client) {
        VpnClient existingClient = findByCn(client.getCn());
        if (existingClient != null) {
            throw new IllegalArgumentException("Client with CN " + client.getCn() + " already exists.");
        }
        CCD ccd = ccdRepository.save(createCCD(client));
        VpnClient newClient = new VpnClient();
        newClient.setCn(client.getCn());
        newClient.setDescription(client.getDescription());
        newClient.setAllowedDestinations(client.getAllowedDestinations());
        newClient.setCreatedAt(Instant.now());
        newClient.setRevoked(false);
        newClient.setCcd(ccd);
        return vpnClientRepository.save(newClient);
    }

    private CCD createCCD(VpnClientDTO client) {
        return new CCD(
                client.getCn(),
                client.getAssignedIp()
        );
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

    public Set<IpRangeDTO> getIpRange() {
        VpnProperties.Guest guest = vpnProperties.getGuest();
        VpnProperties.Admin admin = vpnProperties.getAdmin();
        return Set.of(
            new IpRangeDTO(guest.getIpRange().getStart(), guest.getIpRange().getEnd(), ClientType.GUEST),
            new IpRangeDTO(admin.getIpRange().getStart(), admin.getIpRange().getEnd(), ClientType.ADMIN)
        );

    }
}
