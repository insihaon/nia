package com.kt.ipms.scheduler.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codej.base.property.FileStorageProperties;
import com.codej.base.utils.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ipmsTask {

    @Autowired(required=false)
    private FileUtil fileUtil;
    @Autowired
    private FileStorageProperties fileStorageProperties;
    
    
    //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    // 매일 오전 1시에 실행
    @Scheduled(cron = "0 0 11 * * ?") 
    public void sendHeartbeat() {
        try {
            String dir = fileStorageProperties.getExcelUploadDir();
            fileUtil.DeleteOldFiles(dir);
        } catch (Exception e) {
            log.error("sendHeartbeat error: {}", e.getMessage());
        }
    }
   
}
