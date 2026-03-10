package com.nia.ems.linkage.service;


import com.nia.ems.linkage.vo.performance.PerformaceVo;

public interface PerformanceService {
    PerformaceVo pasingPerformanceMsg(StringBuffer sbPerformanceData);
    void performanceDataCheck();
}
