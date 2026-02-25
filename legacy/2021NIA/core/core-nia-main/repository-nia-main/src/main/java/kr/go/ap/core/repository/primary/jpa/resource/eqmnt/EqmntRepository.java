package kr.go.ap.core.repository.primary.jpa.resource.eqmnt;

import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EqmntRepository extends JpaRepository<EqmntEntity, String> {
    List<EqmntEntity> findByModelContaining(String model);

}
