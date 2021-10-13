package com.nia.data.linkage.ip.sflow.mapper.nia;

import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface NiaSflowMapper {
    String selectSflowYdKey(String key);
    ArrayList<SflowLogVo> selectSflowLogList(int interrIdx);
    void insertSflowLog(HashMap<String, Object> objectHashMap);
    void insertSflowProtocolLog(HashMap<String, Object> objectHashMap);
    void updateSflowYdKey(HashMap<String, String> strHashMap);
}
