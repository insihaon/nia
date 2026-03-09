package kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EqmntSipcKey {
    @Column(name = "tid")
    private String tid;
    @Column(name = "sysname")
    private String systemName;
}