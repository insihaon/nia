package com.nia.alarm.ip.preprocessor.mapper;

import com.nia.alarm.ip.preprocessor.vo.euqipment.NodeInfoVo;
import com.nia.alarm.ip.preprocessor.vo.euqipment.PortMstVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface EquipmentMapper {

    NodeInfoVo selectNodeMst(HashMap<String, String> map);
    PortMstVo selectPortMst(HashMap<String, String> map);
}
