package com.nia.ai.traffic.preprocessor.service;

import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficListVo;

public interface NiaAnomalousTrafficHdlService {
    void niaAnomalousTrafficeHdlProcessor(AnomalousTrafficListVo perfListVo);
}
