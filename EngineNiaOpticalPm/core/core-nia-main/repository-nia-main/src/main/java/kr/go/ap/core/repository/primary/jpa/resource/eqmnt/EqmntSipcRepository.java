package kr.go.ap.core.repository.primary.jpa.resource.eqmnt;

import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntSipcEntity;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.key.EqmntSipcKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EqmntSipcRepository extends JpaRepository<EqmntSipcEntity, EqmntSipcKey>{
    @Transactional
    void deleteByEqmntSipcKey_Tid(String tid);

}
