package com.kt.ipms.legacy.ipmgmt.linkmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstVo;

@Mapper
public interface TbIpLinkMstDao {

	public List<TbIpLinkMstVo> selectListPageIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int countListPageIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo);
	
	public List<CommonCodeVo> selectLoadOfficeList(TbIpLinkMstVo tbIpLinkMstVo);
	
	public List<TbIpLinkMstVo> selectOfficeList(TbIpLinkMstVo tbIpLinkMstVo);
	
	public TbIpLinkMstVo selectTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int insertTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int countTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int updateTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int upateTbIpAllocMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
	public int deleteTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo);
	
}
