package com.nia.engine.mapper;

import com.nia.engine.vo.sdn.factor.NodeFactorInfoVo;
import com.nia.engine.vo.sdn.factor.NodeFactorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NodeFactorMapper {

    NodeFactorInfoVo selectNodeFactor(HashMap<String, String> map);


    void updateNodeFactor(HashMap<String,String> map);
}
