package io.erisdev.vpncontrolpanelbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "client_config")
@NoArgsConstructor
@AllArgsConstructor
public class ClientConfig {
    @Id
    private String cn;

    @Column(columnDefinition = "text")
    private String config;
}
