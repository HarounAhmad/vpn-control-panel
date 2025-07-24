package io.erisdev.vpncontrolpanelbackend.rest;

import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import io.erisdev.vpncontrolpanelbackend.rest.dto.IpRangeDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientResponseDTO;
import io.erisdev.vpncontrolpanelbackend.service.VpnClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class VpnClientController {

    private final VpnClientService service;


    @GetMapping
    public List<VpnClient> listAll() {
        return service.findAll();
    }

    @GetMapping("/config")
    public String getConfig() {
        return service.getConfig();
    }

    @GetMapping("/{cn}")
    public ResponseEntity<VpnClient> getOne(@PathVariable String cn) {
        return ResponseEntity.ok(service.findByCn(cn));
    }

    @PostMapping
    public VpnClientResponseDTO create(@RequestBody VpnClientDTO client) throws IOException {
        return service.createClient(client);
    }

    @GetMapping("/{cn}/config/download")
    public ResponseEntity<byte[]> downloadConfig(@PathVariable String cn) throws IOException {
        byte[] content = service.downloadClientConfig(cn);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + cn + ".ovpn");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PutMapping("/{cn}/destinations")
    public VpnClient updateDestinations(@PathVariable String cn, @RequestBody List<String> destinations) {
        return service.updateDestinations(cn, new HashSet<>(destinations));
    }

    @PostMapping("/{cn}/revoke")
    public ResponseEntity<Void> revoke(@PathVariable String cn) {
        service.revokeClient(cn);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cn}")
    public ResponseEntity<Void> delete(@PathVariable String cn) {
        service.deleteClient(cn);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranges")
    public ResponseEntity<Set<IpRangeDTO>> getIpRange() {
        return ResponseEntity.ok(service.getIpRange());
    }

}
