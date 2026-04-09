package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipsdn_opt.app.model.RouterInfo;

@Repository
public interface RouterInfoRepository extends JpaRepository<RouterInfo, Long> {
    @Query(value = "call findRouterInfoByRouterName(:routername); ", nativeQuery = true)
    RouterInfo findRouterInfoByRouterName(@Param("routername") String routername);
}
