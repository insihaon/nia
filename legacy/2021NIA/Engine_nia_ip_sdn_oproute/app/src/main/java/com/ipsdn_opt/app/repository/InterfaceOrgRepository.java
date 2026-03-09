package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ipsdn_opt.app.model.InterfaceOrg;

public interface InterfaceOrgRepository extends JpaRepository<InterfaceOrg, Long> {
    @Query(value = "call updateVlanInterfaceFromIpsdn();", nativeQuery = true)
    void updateVlanInterfaceFromIpsdn();
    @Query(value = "call updateAgencyInterfaceFromIpsdn();", nativeQuery = true)
    void updateAgencyInterfaceFromIpsdn();
}
