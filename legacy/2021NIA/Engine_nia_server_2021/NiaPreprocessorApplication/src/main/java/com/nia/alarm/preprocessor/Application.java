package com.nia.alarm.preprocessor;

import com.nia.alarm.preprocessor.service.BootSettingService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication(scanBasePackages = "com.nia.alarm.preprocessor")
public class Application implements CommandLineRunner {


    @Autowired
    @Qualifier("BootSettingService")
    private BootSettingService bootSettingService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        bootSettingService.init();
    }
}