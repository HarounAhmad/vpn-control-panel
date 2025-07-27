package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.model.CCD;
import io.erisdev.vpncontrolpanelbackend.model.VpnClient;
import io.erisdev.vpncontrolpanelbackend.rest.dto.VpnClientDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuditContext {

    public String getUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    public String getClientIp(HttpServletRequest request) {
        var forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null) ? forwarded : request.getRemoteAddr();
    }

    public Map<String, String> clientRule(String clientCn, String srcIp, String dstIp, String protocol, int dstPort) {
        return Map.of(
                "clientCn", clientCn,
                "srcIp", srcIp,
                "dstIp", dstIp,
                "protocol", protocol,
                "dstPort", String.valueOf(dstPort)
        );
    }

    public Map<String, String> ccd(CCD client) {
        return Map.of(
            "clientCN", client.getCn(),
            "ip", client.getIp(),
            "subnet", client.getSubnet()
        );
    }

    public Map<String, String> vpnClient(VpnClientDTO client) {
        return Map.of(
                "cn", client.getCn(),
                "description", client.getDescription(),
                "assignedIp", client.getAssignedIp(),
                "allowedIps", String.join(", ", client.getAllowedDestinations()),
                "os", client.getOs()
        );
    }


    public Map<String, String> basicDetails(String entityId) {
        return Map.of("entityId", entityId);
    }

    public Map<String, String> diff(String field, String oldValue, String newValue) {
        return Map.of(
                "field", field,
                "oldValue", oldValue,
                "newValue", newValue
        );
    }

    public Map<String, String> fullUpdate(String entityId, Map<String, String> fieldDiffs) {
        Map<String, String> map = new HashMap<>(fieldDiffs);
        map.put("entityId", entityId);
        return map;
    }

    public Map<String, String> loginDetails(HttpServletRequest request, boolean success, String statusMessage) {
        return Map.of(
                "ip", getClientIp(request),
                "status", success ? "SUCCESS" : "FAILURE",
                "username", success ? getUsername() : "",
                "userAgent", request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "Unknown User Agent",
                "reason", success ? "Login successful" : "Login failed: " + statusMessage

        );
    }

    public Map<String, String> loginDetails(HttpServletRequest request, boolean success, String statusMessage, String username) {
        return Map.of(
                "ip", getClientIp(request),
                "status", success ? "SUCCESS" : "FAILURE",
                "username", success ? getUsername() : username,
                "userAgent", request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "Unknown User Agent",
                "reason", success ? "Login successful" : "Login failed: " + statusMessage

        );
    }

    public Map<String, String> downloadDetails(String fileName, HttpServletRequest request) {
        return Map.of(
                "file", fileName,
                "ip", getClientIp(request)
        );
    }

    public Map<String, String> revocationDetails(String certId, String reason) {
        return Map.of(
                "certId", certId,
                "reason", reason
        );
    }


}


