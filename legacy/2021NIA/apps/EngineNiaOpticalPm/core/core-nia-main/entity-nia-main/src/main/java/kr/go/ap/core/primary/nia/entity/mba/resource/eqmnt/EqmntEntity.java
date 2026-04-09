package kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "TB_EQUIP_MST", schema = "NIA")
@Getter
public class EqmntEntity {

    @Id
    private String sysname;
    private String tid;
    private String model;

}
