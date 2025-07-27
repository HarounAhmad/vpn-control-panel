package io.erisdev.vpncontrolpanelbackend.rest;

import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.service.VpnClientFirewallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{clientCn}/rules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SYSADMIN')")
public class VpnClientFirewallController {

    private final VpnClientFirewallService firewallService;

    @GetMapping
    public List<NftRule> listRules(@PathVariable String clientCn) {
        return firewallService.listClientRules(clientCn);
    }

    @PostMapping
    public ResponseEntity<NftRule> addRule(@PathVariable String clientCn, @RequestBody NftRule rule, Authentication authentication) {
        if (!rule.getClientCn().equals(clientCn)) {
            throw new IllegalArgumentException("Client CN in path and body must match");
        }
        return ResponseEntity.ok(firewallService.addClientRule(clientCn, rule.getSrcIp(), rule.getDstIp(), rule.getProtocol(), rule.getDstPort(), authentication.getName()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRule(@PathVariable String clientCn, @RequestBody NftRule rule, Authentication authentication) {
        if (!rule.getClientCn().equals(clientCn)) {
            throw new IllegalArgumentException("Client CN in path and body must match");
        }
        firewallService.removeClientRule(clientCn, rule.getSrcIp(), rule.getProtocol(), rule.getDstPort(), authentication.getName());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replaceRules(@PathVariable String clientCn, @RequestBody List<NftRule> rules) {
        // Validate CN matches for all rules
        boolean allMatch = rules.stream().allMatch(r -> r.getClientCn().equals(clientCn));
        if (!allMatch) {
            throw new IllegalArgumentException("All rules must have the same client CN as the path variable");
        }
        firewallService.replaceClientRules(clientCn, rules);
    }
}