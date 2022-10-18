package com.nia.data.linkage.ai.mapper.ipsdn;

import com.nia.data.linkage.ai.vo.ipsdn.factor.NodeFactorVo;
import com.nia.data.linkage.ai.vo.ipsdn.resource.InterfaceVo;
import com.nia.data.linkage.ai.vo.ipsdn.resource.LinkVo;
import com.nia.data.linkage.ai.vo.ipsdn.resource.NodeVo;
import com.nia.data.linkage.ai.vo.ipsdn.traffic.LinkTrafficVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface IpsdnDataMapper {
    ArrayList<NodeVo> selectNodeLink();
    ArrayList<InterfaceVo> selectInterfaceLink();
    ArrayList<LinkVo> selectLink();
    ArrayList<NodeFactorVo> selectNodeFactorLink(int dataKey);
    ArrayList<LinkTrafficVo> selectTrafficList(int dataKey);
}
