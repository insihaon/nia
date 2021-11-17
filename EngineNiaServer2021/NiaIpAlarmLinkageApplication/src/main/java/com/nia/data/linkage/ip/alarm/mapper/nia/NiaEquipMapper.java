package com.nia.data.linkage.ip.alarm.mapper.nia;

import com.nia.data.linkage.ip.alarm.vo.equip.NodeMstVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface NiaEquipMapper {
    ArrayList<NodeMstVo> selectNodeList();
}
