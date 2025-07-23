package io.erisdev.vpncontrolpanelbackend.rest;

import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import io.erisdev.vpncontrolpanelbackend.rest.dto.IpRangeDTO;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientDTO;
import io.erisdev.vpncontrolpanelbackend.service.VpnClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{cn}")
    public ResponseEntity<VpnClient> getOne(@PathVariable String cn) {
        return ResponseEntity.ok(service.findByCn(cn));
    }

    @PostMapping
    public VpnClient create(@RequestBody VpnClientDTO client) {
        return service.createClient(client);
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
