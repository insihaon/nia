package com.nia.data.linkage.ip.equip.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaEquipMapper {
    void insertCvnmsResourceIf(HashMap<String, Object> objectHashMap);
    void insertCvnmsResource(HashMap<String, Object> objectHashMap);
    void fcResourceIfYd();
    void fcResourceNodeYd();
    void fcCreateUserOrganYd();
    void deleteCvnmsResourceIf();
    void deleteCvnmsResource();
    void updateLinkageHist(HashMap<String, String> strHashMap);
}
