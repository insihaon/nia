package com.nia.ems.linkage.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;

@Mapper
public interface MbaMapper {
    void setPerformancePreprocessor(Timestamp timeStamp);
}
