package com.nia.ai.traffic.preprocessor.mapper;

import com.nia.ai.traffic.preprocessor.vo.anomalous.AttAiTcaTrafficCheckModelVo;
import com.nia.ai.traffic.preprocessor.vo.anomalous.OverTrafficInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface AttAiTcaMapper {
    ArrayList<AttAiTcaTrafficCheckModelVo> selectAttAiTcaTrafficCheckModel();
    OverTrafficInfoVo findAttAiOverTraffic(AttAiTcaTrafficCheckModelVo vo);
    void insertTbAttAiTcaModel(HashMap hashMap);
}
