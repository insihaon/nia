package com.kt.ipms.legacy.ipmgmt.tracemgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpHostMstVo;

@Mapper
public interface TbTraceIPHostMstDao {
	
	public List<TbTraceIPVo> selectTraceHostInfo(TbTraceIPVo tbTraceIPVo);
	
	public TbTraceIpHostMstVo selectTraceDetailHostInfo(TbTraceIpHostMstVo tbTraceIpHostMstVo);
	
	public TbTraceIpAssignMstVo selectTraceDetailAssignInfo(TbTraceIpAssignMstVo tbTraceIpAssignMstVo);

}
