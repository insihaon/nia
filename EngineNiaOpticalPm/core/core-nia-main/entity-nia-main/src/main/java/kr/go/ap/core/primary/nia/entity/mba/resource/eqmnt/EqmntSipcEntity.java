package kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.key.EqmntSipcKey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TB_EQUIP_SIPC_TMP", schema = "NIA")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EqmntSipcEntity {

    @EmbeddedId
    private EqmntSipcKey eqmntSipcKey;

    public String getTid() {
        return eqmntSipcKey.getTid();
    }

    public String getSystemName() {
        return eqmntSipcKey.getSystemName();
    }


    public static EqmntSipcEntity of(String tid, String systemName) {
        return new EqmntSipcEntity(new EqmntSipcKey(tid, systemName));
    }


}

