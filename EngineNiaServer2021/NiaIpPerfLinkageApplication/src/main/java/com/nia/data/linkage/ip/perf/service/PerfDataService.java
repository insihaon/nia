package com.nia.data.linkage.ip.perf.service;

import com.nia.data.linkage.ip.perf.vo.perf.PerfVo;

import java.util.ArrayList;

public interface PerfDataService {

    void getPerfData();
    void insertPerfData(ArrayList<PerfVo> perfVoList);
}
