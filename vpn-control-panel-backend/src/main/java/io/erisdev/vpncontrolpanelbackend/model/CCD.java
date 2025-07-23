package io.erisdev.vpncontrolpanelbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ccd")
public class CCD {

    @Id
    private String cn; // Common Name used as primary key
    private String ip;
}
