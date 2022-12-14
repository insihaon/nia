package com.nia.ai.traffic.preprocessor.service;

import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficListVo;

public interface NiaSdnTrafficHdlService {
    void niaSdnTrafficeHdlProcessor(SdnTrafficListVo sdnTrafficListVo);
}
