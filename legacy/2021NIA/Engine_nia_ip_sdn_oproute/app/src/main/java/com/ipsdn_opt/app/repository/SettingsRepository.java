package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    
}
