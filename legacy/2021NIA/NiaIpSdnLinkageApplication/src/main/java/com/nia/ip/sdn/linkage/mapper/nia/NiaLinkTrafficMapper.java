package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaLinkTrafficMapper {
    void insertLinkTraffic(HashMap<String, Object> hashMap);
}
