package com.nia.data.linkage.ip.perf.mapper.linkage;

import com.nia.data.linkage.ip.perf.vo.perf.PerfVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkagePerfMapper {
    PerfVo selectMaxIntTimestamp();
    ArrayList<PerfVo> selectPerfList(int intTimestamp);
}
