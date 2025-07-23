package io.erisdev.vpncontrolpanelbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vpn_clients")
public class VpnClient {

    @Id
    private String cn; // Common Name used as primary key

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_destinations", joinColumns = @JoinColumn(name = "client_cn"))
    @Column(name = "destination")
    private Set<String> allowedDestinations;

    private boolean revoked;

    private Instant createdAt;

    private Instant lastSeen;

    @OneToOne
    private CCD ccd; // CCD information for the client

}
