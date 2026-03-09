package kr.go.ap.core.primary.nia.entity.linkage.pm.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmKey {

    @Column(name = "sysname")
    private String sysname;
    @Column(name = "port")
    private String port;
}
