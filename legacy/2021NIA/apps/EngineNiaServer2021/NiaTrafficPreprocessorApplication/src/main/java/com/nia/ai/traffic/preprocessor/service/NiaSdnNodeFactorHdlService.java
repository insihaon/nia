package com.nia.ai.traffic.preprocessor.service;

import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorListVo;

public interface NiaSdnNodeFactorHdlService {
    void niaSdnNodeFactorHdlProcessor(NodeFactorListVo nodeFactorListVo);
}
