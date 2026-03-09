package com.nia.data.linkage.service;

import com.nia.data.linkage.vo.performance.PerformaceCollectTimeVo;
import com.nia.data.linkage.vo.performance.json.PerformaceJonDataVo;


public interface PerformanceService {

    void getPerformanceData();

    void setPerformanceData(PerformaceJonDataVo performaceJonDataVo, PerformaceCollectTimeVo performaceCollectTimeVo);
}
