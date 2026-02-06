package com.nia.ip.country.code.linkage;

import com.nia.ip.country.code.linkage.mapper.CountryMapper;
import com.nia.ip.country.code.linkage.service.CountryWebService;
import com.nia.ip.country.code.linkage.vo.country.CountryVo;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@EnableEncryptableProperties
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    @Qualifier("CountryWebService")
    private CountryWebService countryWebService;

    @Override
    public void run(String... args) throws Exception {
        List<CountryVo> countryVoList = countryMapper.selectIpCountryCodeList();

        for(CountryVo countryVo : countryVoList){
            countryWebService.sendData(countryVo.getIpAddr());
        }
    }
}