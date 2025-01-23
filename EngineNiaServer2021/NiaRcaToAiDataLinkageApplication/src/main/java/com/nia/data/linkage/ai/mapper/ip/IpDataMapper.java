package com.nia.data.linkage.ai.mapper.ip;

import com.nia.data.linkage.ai.vo.ip.alarm.IpAlarmVo;
import com.nia.data.linkage.ai.vo.ip.equip.*;
import com.nia.data.linkage.ai.vo.ip.perf.PerfVo;
import com.nia.data.linkage.ai.vo.ip.sflow.SflowLogVo;
import com.nia.data.linkage.ai.vo.ip.traffic.AttTrafficVo;
import com.nia.data.linkage.ai.vo.ip.traffic.NttSflowVo;
import com.nia.data.linkage.ai.vo.sop.SopVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface IpDataMapper {
    ArrayList<IpAlarmVo> selectAlarmList(int interrIdx);
    ArrayList<PerfVo> selectPerfList(long intTimestamp);
    ArrayList<SflowLogVo> selectSflowLogList(String dateRegDate);
    ArrayList<IpNodeInfoVo> selectNodeMst();
    ArrayList<IpPortMstVo> selectPortMst();
    ArrayList<IpBackboneLinkVo> selectBackboneLink();
    ArrayList<IpCvnmsResourceVo> selectCvnmsResourceList();
    ArrayList<IpCvnmsResourceIfVo> selectCvnmsResourceIfList();


    ArrayList<AttTrafficVo> selectAttTrafficList();
    ArrayList<AttTrafficVo> selectAttTraffic03List();

    ArrayList<NttSflowVo> selectNttSflowList();
    ArrayList<NttSflowVo> selectNttSflowHistList();

    ArrayList<SopVo> selectSopList(String sopId);




}
