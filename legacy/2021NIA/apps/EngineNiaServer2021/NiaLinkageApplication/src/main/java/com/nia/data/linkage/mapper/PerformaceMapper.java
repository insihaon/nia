package com.nia.data.linkage.mapper;

import com.nia.data.linkage.vo.performance.PerformaceCollectTimeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface PerformaceMapper {
    PerformaceCollectTimeVo selectPerformaceCollectTime();
    void updatePerformaceCollectTime(PerformaceCollectTimeVo performaceCollectTimeVo);
    void insertPerformace(HashMap<String, Object> map);
}
