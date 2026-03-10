package kr.go.ap.core.repository.primary.jpa.resource.eqmnt;

import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntSipcEntity;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.key.EqmntSipcKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EqmntSipcRepository extends JpaRepository<EqmntSipcEntity, EqmntSipcKey>{
    void deleteByEqmntSipcKey_Tid(String tid);

}
