package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaE2eNodeMapper {
    int insertE2eNodeYd(HashMap<String, Object> hashMap);
    void insertE2eNode();
    void deleteE2eNodeYd();
    void deleteE2eNode();
}
