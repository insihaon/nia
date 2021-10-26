package com.nia.data.linkage.ai.mapper.ip;

import com.nia.data.linkage.ai.vo.ip.equip.IpBackboneLinkVo;
import com.nia.data.linkage.ai.vo.ip.equip.IpNodeInfoVo;
import com.nia.data.linkage.ai.vo.ip.equip.IpPortMstVo;
import com.nia.data.linkage.ai.vo.ip.perf.PerfVo;
import com.nia.data.linkage.ai.vo.ip.sflow.SflowLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface IpDataMapper {
    ArrayList<PerfVo> selectPerfList(long intTimestamp);
    ArrayList<SflowLogVo> selectSflowLogList(String dateRegDate);
    ArrayList<IpNodeInfoVo> selectNodeMst();
    ArrayList<IpPortMstVo> selectPortMst();
    ArrayList<IpBackboneLinkVo> selectBackboneLink();
}
