package com.nia.data.linkage.ip.sflow.mapper.linkage;

import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
import com.nia.data.linkage.ip.sflow.vo.sflow.SflowProtocolLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageSflowMapper {
    ArrayList<SflowLogVo> selectSflowLogList(String interrIdx);
    ArrayList<SflowProtocolLogVo> selectSflowProtocolLogList(int interrIdx);
}
