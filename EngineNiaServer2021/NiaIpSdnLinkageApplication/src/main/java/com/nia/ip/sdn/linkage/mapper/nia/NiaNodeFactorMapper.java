package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaNodeFactorMapper {
    void insertNodeFactor(HashMap<String, Object> hashMap);
}
