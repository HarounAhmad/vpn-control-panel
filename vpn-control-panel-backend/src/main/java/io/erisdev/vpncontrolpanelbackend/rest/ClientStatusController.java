package io.erisdev.vpncontrolpanelbackend.rest;

import io.erisdev.vpncontrolpanelbackend.rest.dto.ClientStatusDto;
import io.erisdev.vpncontrolpanelbackend.service.ClientStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('SYSADMIN')")
@RequestMapping("/api/v1/client-status")
@RequiredArgsConstructor
public class ClientStatusController {

    private final ClientStatusService clientStatusService;

    @GetMapping("/get")
    public ResponseEntity<List<ClientStatusDto>> get() {
        return ResponseEntity.ok(clientStatusService.get());
    }

    @GetMapping("/get/{cn}")
    public ResponseEntity<ClientStatusDto> getById(@PathVariable String cn) {
        ClientStatusDto status = clientStatusService.findById(cn);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

}

