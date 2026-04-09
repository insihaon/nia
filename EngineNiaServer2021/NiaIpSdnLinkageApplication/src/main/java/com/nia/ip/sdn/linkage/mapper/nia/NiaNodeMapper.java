package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaNodeMapper {
    int insertNodeYd(HashMap<String, Object> hashMap);
    void insertNode();
    void deleteNodeYd();
    void deleteNode();
}
