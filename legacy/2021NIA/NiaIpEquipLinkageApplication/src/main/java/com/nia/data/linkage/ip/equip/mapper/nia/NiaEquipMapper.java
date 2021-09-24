package com.nia.data.linkage.ip.equip.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaEquipMapper {
    void insertCvnmsResourceIf(HashMap<String, Object> objectHashMap);
    void insertCvnmsResource(HashMap<String, Object> objectHashMap);
    void deleteCvnmsResourceIf();
    void deleteCvnmsResource();
}
