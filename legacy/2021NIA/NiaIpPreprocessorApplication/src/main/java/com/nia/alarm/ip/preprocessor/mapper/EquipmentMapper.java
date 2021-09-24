package com.nia.alarm.ip.preprocessor.mapper;

import com.nia.alarm.ip.preprocessor.vo.euqipment.EquipInfoVo;
import com.nia.alarm.ip.preprocessor.vo.euqipment.EquipPortVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface EquipmentMapper {

    EquipPortVo selectEquipPort(HashMap<String, String> map);
    EquipInfoVo selectEquipInfo(HashMap<String, String> map);
}
