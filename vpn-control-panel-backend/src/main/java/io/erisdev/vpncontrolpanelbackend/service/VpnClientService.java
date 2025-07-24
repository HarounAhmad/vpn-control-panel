package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.config.VpnProperties;
import io.erisdev.vpncontrolpanelbackend.model.*;
import io.erisdev.vpncontrolpanelbackend.repository.AuditLogRepository;
import io.erisdev.vpncontrolpanelbackend.repository.CCDRepository;
import io.erisdev.vpncontrolpanelbackend.repository.ClientConfigRepository;
import io.erisdev.vpncontrolpanelbackend.repository.VpnClientRepository;
import io.erisdev.vpncontrolpanelbackend.rest.dto.IpRangeDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientResponseDTO;
import io.erisdev.vpncontrolpanelbackend.vpn.CCDWriter;
import io.erisdev.vpncontrolpanelbackend.vpn.ClientConfigGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VpnClientService {
    private static final String SERVER_IP = "server_ip";
    private static final String SERVER_PORT = "server_port";
    private static final String CA_PATH = "ca_path";
    private static final String CERT_PATH = "cert_path";
    private static final String KEY_PATH = "key_path";
    private static final String OS = "os";
    private static final String TLS_CRYPT_PATH = "tls_crypt_path";
    private static final String CA_CRT = "ca.crt";
    private static final String CRT = ".crt";
    private static final String KEY = ".key";
    private static final String TA_KEY = "ta.key";

    private static final String DOWNLOAD_LINK_TEMPLATE = "/api/v1/clients/%s/config/download";
    private final VpnClientRepository vpnClientRepository;
    private final CCDRepository ccdRepository;
    private final ClientConfigRepository clientConfigRepository;
    private final VpnProperties vpnProperties;
    private final CCDWriter ccdWriter;

    private final AuditLogService auditLogService;

    public List<VpnClient> findAll() {
        return vpnClientRepository.findAll();
    }

    public VpnClient findByCn(String cn) {
        return vpnClientRepository.findById(cn).orElse(null);
    }


    public VpnClientResponseDTO createClient(VpnClientDTO client) throws IOException {
        VpnClient existingClient = findByCn(client.getCn());
        if (existingClient != null) {
            throw new IllegalArgumentException("Client with CN " + client.getCn() + " already exists.");
        }
        if (client.getAllowedDestinations() == null || client.getAllowedDestinations().isEmpty()) {
            client.setAllowedDestinations(Set.of(vpnProperties.getDefaultDestination()));
        }
        CCD ccd = ccdRepository.save(createCCD(client));
        ccdWriter.writeCCD(ccd);
        VpnClient newClient = new VpnClient();
        newClient.setCn(client.getCn());
        newClient.setDescription(client.getDescription());
        newClient.setAllowedDestinations(client.getAllowedDestinations());
        newClient.setCreatedAt(Instant.now());
        newClient.setRevoked(false);
        newClient.setCcd(ccd);

        String config = ClientConfigGenerator.generateConfig(
                Map.of(
                        SERVER_IP, vpnProperties.getServer(),
                        SERVER_PORT, vpnProperties.getPort(),
                        CA_PATH, CA_CRT,
                        CERT_PATH, client.getCn() + CRT,
                        KEY_PATH, client.getCn() + KEY,
                        OS, OsTypes.LINUX.name(),
                        TLS_CRYPT_PATH, TA_KEY));

        clientConfigRepository.save(new ClientConfig(
                client.getCn(),
                config
        ));

        var response = new VpnClientResponseDTO();
        response.setClient(vpnClientRepository.save(newClient));
        response.setDownloadLink(generateDownloadLink(response.getClient()));
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.CREATE)
                        .entityType(VpnClient.class.getSimpleName())
                        .entityId(newClient.getCn())
                        .details("Created VPN client with CN: " + newClient.getCn())
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(null)
                        .newValue(newClient.toString())
                        .build()
        );



        return response;
    }

    private String generateDownloadLink(VpnClient client) {
        return String.format(DOWNLOAD_LINK_TEMPLATE, client.getCn());
    }

    public byte[] downloadClientConfig(String cn) throws IOException {
        VpnClient client = findByCn(cn);
        if (client == null) {
            throw new IllegalArgumentException("Client with CN " + cn + " does not exist.");
        }
        var config = clientConfigRepository.findById(cn)
                .orElseThrow(() -> new IllegalArgumentException("Client config for CN " + cn + " does not exist."))
                .getConfig();

        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.DOWNLOAD)
                        .entityType(ClientConfig.class.getSimpleName())
                        .entityId(cn)
                        .details("Downloaded VPN client config for CN: " + cn)
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(null)
                        .newValue(config)
                        .build()
        );

        return config.getBytes(StandardCharsets.UTF_8);
    }

    private CCD createCCD(VpnClientDTO client) {
        var adminIpRange = vpnProperties.getAdmin().getIpRange();

        int clientLastOctet = parseLastOctet(client.getAssignedIp());
        int adminStart = parseLastOctet(adminIpRange.getStart());
        int adminEnd = parseLastOctet(adminIpRange.getEnd());

        boolean isAdmin = clientLastOctet >= adminStart && clientLastOctet <= adminEnd;

        String subnet = isAdmin
                ? vpnProperties.getAdmin().getSubnet()
                : vpnProperties.getGuest().getSubnet();
        CCD ccd = new CCD(
                client.getCn(),
                client.getAssignedIp(),
                subnet
        );
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.CREATE)
                        .entityType(CCD.class.getSimpleName())
                        .entityId(ccd.getCn())
                        .details("Created CCD for: " + ccd.getCn())
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(null)
                        .newValue(ccd.toString())
                        .build()
        );

        return ccd;
    }

    private Integer parseLastOctet(String ip) {
        String[] parts = ip.split("\\.");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    public VpnClient updateDestinations(String cn, Set<String> newDestinations) {
        VpnClient client = findByCn(cn);
        if (client != null) {
            client.setAllowedDestinations(newDestinations);

            auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.UPDATE)
                        .entityType(VpnClient.class.getSimpleName())
                        .entityId(cn)
                        .details("Updated allowed destinations for client: " + cn)
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(client.getAllowedDestinations().toString())
                        .newValue(newDestinations.toString())
                        .build());

            return vpnClientRepository.save(client);
        }
        return null;
    }

    public void revokeClient(String cn) {
        VpnClient client = vpnClientRepository.findById(cn).orElseThrow();
        client.setRevoked(true);

        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.REVOKE)
                        .entityType(VpnClient.class.getSimpleName())
                        .entityId(cn)
                        .details("Revoked VPN client with CN: " + cn)
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(client.toString())
                        .newValue("Revoked")
                        .build()
        );

        vpnClientRepository.save(client);
    }

    public void deleteClient(String cn) {

        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.DELETE)
                        .entityType(VpnClient.class.getSimpleName())
                        .entityId(cn)
                        .details("Deleted VPN client with CN: " + cn)
                        .performedBy("system") // Replace with actual user if available
                        .timestamp(Instant.now())
                        .oldValue(vpnClientRepository.findById(cn).map(VpnClient::toString).orElse(null))
                        .newValue("Deleted")
                        .build()
        );
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

    public String getConfig() {
        return "ClientConfigGenerator.generateConfig()";
    }
}
