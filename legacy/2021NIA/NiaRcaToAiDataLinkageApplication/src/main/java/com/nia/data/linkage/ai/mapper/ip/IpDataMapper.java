package com.nia.data.linkage.ai.mapper.ip;

import com.nia.data.linkage.ai.vo.ip.equip.IpBackboneLinkVo;
import com.nia.data.linkage.ai.vo.ip.equip.IpNodeInfoVo;
import com.nia.data.linkage.ai.vo.ip.equip.IpPortMstVo;
import com.nia.data.linkage.ai.vo.ip.perf.PerfVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface IpDataMapper {
    ArrayList<PerfVo> selectPerfList(long intTimestamp);
    ArrayList<IpNodeInfoVo> selectNodeMst();
    ArrayList<IpPortMstVo> selectPortMst();
    ArrayList<IpBackboneLinkVo> selectBackboneLink();
}
