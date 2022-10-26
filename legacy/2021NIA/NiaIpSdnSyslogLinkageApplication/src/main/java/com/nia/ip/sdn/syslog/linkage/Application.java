package com.nia.ip.sdn.syslog.linkage;

import com.nia.ip.sdn.syslog.linkage.service.IpSdnSyslogService;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
@MapperScan(value = {"com.nia.ip.sdn.syslog.linkage.*"})
public class Application implements CommandLineRunner {

    @Autowired
    IpSdnSyslogService ipSdnSyslogService;

    @Autowired
    private SyslogDataVo syslogDataVo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}