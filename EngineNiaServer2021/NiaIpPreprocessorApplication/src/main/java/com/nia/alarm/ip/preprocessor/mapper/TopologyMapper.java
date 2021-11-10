package com.nia.alarm.ip.preprocessor.mapper;

import com.nia.alarm.ip.preprocessor.vo.topology.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TopologyMapper {

    TopologyDataVo selectTopologyList(HashMap<String, String> map);
    UniTopologyVo selectUniTopologyList(HashMap<String, String> map);
}
