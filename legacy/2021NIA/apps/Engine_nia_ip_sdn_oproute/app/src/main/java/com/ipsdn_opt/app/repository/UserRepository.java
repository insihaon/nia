package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginid(@Param("loginid") String loginid);
    boolean existsByLoginid(@Param("loginid") String loginid);
    void deleteByLoginid(@Param("loginid") String loginid);
    
    @Query(value = "update user set logindatetime = now() where id = :id", nativeQuery = true)
    void updateLogindatetimeById(@Param("id") long id);
}
