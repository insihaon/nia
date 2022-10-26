package com.nia.ip.sdn.sflow.linkage.mapper;

import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowCollectVo;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface SflowMapper {
    int selectSflowSeq();
    void insertSflowData(SflowCollectVo sflowCollectVo);
}
