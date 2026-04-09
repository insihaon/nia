package com.nia.ip.country.code.linkage.mapper;

import com.nia.ip.country.code.linkage.vo.country.CountryVo;
import com.nia.ip.country.code.linkage.vo.web.WhoisVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CountryMapper {
    List<CountryVo> selectIpCountryCodeList();
    void updateIpCountryCode(WhoisVo whoisVo);
}
