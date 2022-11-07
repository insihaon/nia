package com.nia.ip.country.code.linkage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SflowMapper {
    int selectSflowSeq();
    void insertSflowData(SflowCollectVo sflowCollectVo);
}
