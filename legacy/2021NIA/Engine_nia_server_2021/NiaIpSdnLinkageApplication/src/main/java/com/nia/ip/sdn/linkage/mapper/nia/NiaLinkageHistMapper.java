package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaLinkageHistMapper {
    String selectLinkageKey(String keyName);
    void updateLinkageHist(HashMap<String, String> hashMap);
}
