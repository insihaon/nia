package com.nia.ip.sdn.linkage.mapper.linkage;

import com.nia.ip.sdn.linkage.vo.NodeFactorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageNodeFactorMapper {
    NodeFactorVo selectMaxId();
    ArrayList<NodeFactorVo> selectNodeFactorList(int id);
}
