package com.nia.ai.per.ap.mapper;

import com.nia.ai.per.ap.vo.TopologyDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TopologyMapper {
    TopologyDataVo selectTopology(HashMap<String, String> map);

}
