package kr.go.ap.core.repository.primary.jpa.linkage.pm;


import kr.go.ap.core.primary.nia.entity.linkage.pm.PmEntity;
import kr.go.ap.core.primary.nia.entity.linkage.pm.key.PmKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PmRepository extends JpaRepository<PmEntity, PmKey> {
}
