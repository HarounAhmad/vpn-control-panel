package io.erisdev.vpncontrolpanelbackend.rest;


import io.erisdev.vpncontrolpanelbackend.rest.dto.AuditLogDto;
import io.erisdev.vpncontrolpanelbackend.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLogDto>> listAll() {
        return ResponseEntity.ok(auditLogService.getAuditLogs());
    }

}
