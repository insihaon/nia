package com.nia.ai.traffic.preprocessor.mapper;

import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrfficVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TrafficMapper {

    void insertAnomalousTraffic(HashMap<String, String> map);
    void insertNoxiousTraffic(HashMap<String, String> map);
    void insertAiAnomalous(AnomalousTrafficVo anomalousTrafficVo);
    void insertAiNoxious(NoxiousTrfficVo noxiousTrfficVo);

    int selectAnomalousTrafficCheck(HashMap<String, String> map);
    int selectNoxiousTrafficCheck(HashMap<String, String> map);
}
