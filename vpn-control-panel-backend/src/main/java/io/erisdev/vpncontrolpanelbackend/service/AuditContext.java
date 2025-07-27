package io.erisdev.vpncontrolpanelbackend.service;

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
                "username", getUsername(),
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


