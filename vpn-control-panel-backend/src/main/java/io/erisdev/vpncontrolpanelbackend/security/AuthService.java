package io.erisdev.vpncontrolpanelbackend.security;


import io.erisdev.vpncontrolpanelbackend.model.AuditLog;
import io.erisdev.vpncontrolpanelbackend.model.AuditLogAction;
import io.erisdev.vpncontrolpanelbackend.rest.dto.LoginRequestDto;
import io.erisdev.vpncontrolpanelbackend.rest.dto.UserDto;
import io.erisdev.vpncontrolpanelbackend.security.util.JwtUtil;
import io.erisdev.vpncontrolpanelbackend.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;

    private final JwtUtil jwtUtil;
    private final AuditLogService auditLogService;


    public void login(LoginRequestDto loginRequest, HttpServletResponse response, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isBlank()) {
            ipAddress = request.getRemoteAddr();
        }
        try {
            var auth = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authManager.authenticate(auth);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(600) // 10 minutes
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            auditLogService.LogAction(
                    AuditLog.builder()
                            .action(AuditLogAction.LOGIN_SUCCESS)
                            .performedBy(userDetails.getUsername())
                            .details("User logged in successfully from IP: " + ipAddress)
                            .timestamp(Instant.now())
                            .build()
            );
        } catch (BadCredentialsException e) {
            auditLogService.LogAction(
                    AuditLog.builder()
                            .action(AuditLogAction.LOGIN_FAILURE)
                            .performedBy(loginRequest.getUsername())
                            .details("Failed login attempt from IP: " + ipAddress + ". Reason: " + e.getMessage())
                            .timestamp(Instant.now())
                            .build()
            );
            throw e;

        }

    }

    public UserDto getCurrentUser(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDto responseDto = new UserDto(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .collect(Collectors.toSet())
        );
        return responseDto;
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie clearCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", clearCookie.toString());
    }

}
