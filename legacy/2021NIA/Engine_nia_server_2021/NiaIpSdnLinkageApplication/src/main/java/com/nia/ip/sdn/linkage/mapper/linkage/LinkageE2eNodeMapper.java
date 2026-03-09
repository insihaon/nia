package com.nia.ip.sdn.linkage.mapper.linkage;

import com.nia.ip.sdn.linkage.vo.E2eNodeVo;
import com.nia.ip.sdn.linkage.vo.NodeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageE2eNodeMapper {
    ArrayList<E2eNodeVo> selectE2eNodeList();
}
