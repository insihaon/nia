package com.nia.ai.traffic.preprocessor.mapper;

import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TrafficMapper {

    void insertAnomalousTraffic(HashMap<String, String> map);
    void insertNoxiousTraffic(HashMap<String, String> map);
    void insertAiAnomalous(AnomalousTrafficVo anomalousTrafficVo);
    void insertAiNoxious(NoxiousTrafficVo noxiousTrfficVo);

//    SDN
    int insertSdnTraffic(HashMap<String, String> map);
    void insertAiSdnTraffic(SdnTrafficVo sdnTrafficVo);
    int insertNodeFactor(HashMap<String, String> map);
    void insertAiNodeFactor(NodeFactorVo nodeFactorVo);

    int selectAnomalousTrafficCheck(HashMap<String, String> map);
    int selectNoxiousTrafficCheck(HashMap<String, String> map);

//    SDN
    int selectSdnTrafficCheck(HashMap<String, String> map);
    int selectNodeFactorCheck(HashMap<String, String> map);


}
