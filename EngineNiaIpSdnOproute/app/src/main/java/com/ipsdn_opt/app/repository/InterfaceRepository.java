package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ipsdn_opt.app.model.Interface;

public interface InterfaceRepository extends JpaRepository<Interface, Long> {
    @Query(value = "call findInterfaceByNotNullAddress(); ", nativeQuery = true)
    List<Interface> findInterfaceByNotNullAddress();

    @Query(value = "call findInterfaceAll(); ", nativeQuery = true)
    List<Interface> findInterfaceAll();
}
