package com.nia.data.linkage.ip.perf.mapper.nia;

import com.nia.data.linkage.ip.perf.vo.equip.NodeMstVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface NiaEquipMapper {
    ArrayList<NodeMstVo> selectNodeList();
}
