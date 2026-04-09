package com.nia.data.linkage.ip.perf.mapper.nia;

import com.nia.data.linkage.ip.perf.vo.perf.PerfVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface NiaPerfMapper {
    String selectPerfYdKey(String key);
    void insertPerf(HashMap<String, Object> objectHashMap);
    void updatePerfYdKey(HashMap<String, String> strHashMap);
}
