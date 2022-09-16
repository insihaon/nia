package com.nia.ip.sdn.linkage.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaLinkMapper {
    int insertLinkYd(HashMap<String, Object> hashMap);
    void insertLink();
    void deleteLinkYd();
    void deleteLink();
}
