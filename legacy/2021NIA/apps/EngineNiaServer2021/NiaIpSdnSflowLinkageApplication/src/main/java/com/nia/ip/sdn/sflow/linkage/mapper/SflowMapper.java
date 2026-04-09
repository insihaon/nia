package com.nia.ip.sdn.sflow.linkage.mapper;

import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowCollectVo;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface SflowMapper {
    long selectCurrentSflowSeq();
    void updateLinkageHist(HashMap<String, String> hashMap);
    int insertSflowDataBatch(List<SflowCollectVo> list);
}
