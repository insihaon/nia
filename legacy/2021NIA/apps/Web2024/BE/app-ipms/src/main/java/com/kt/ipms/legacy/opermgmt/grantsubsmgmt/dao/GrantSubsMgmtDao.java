package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantVo;

@Mapper
public interface GrantSubsMgmtDao {
	public List<TbUserGrantVo> selectTbUserGrantList (TbUserGrantVo tbUserGrantVo);
	
	public int countListPageTbUserGrantVo (TbUserGrantVo tbUserGrantVo);
	
	public int insertTbUserGrant(TbUserAuthTxnSubVo insertVo);
	
	public int insertTbUserAuthTxnSub(TbUserAuthTxnSubVo insertVo);
	
	public List<TbUserAuthTxnSubVo> selectListTbUserAuthTxnSubVo(TbUserAuthTxnVo searchVo);
	
	public int countListTbUserAuthTxnSubVo(TbUserAuthTxnVo searchVo);
	
	public String selectNrequestTypeCd(TbUserAuthTxnVo searchVo);
	
	public int deleteGrant(TbUserGrantVo searchVo);
	
	public int deleteTbUserAuthTxnSubVo(TbUserGrantVo searchVo);
	
	public int updateGrant(TbUserAuthTxnListVo tbUserAuthTxnListVo);
	
	public TbUserGrantVo selectGrantVo(TbUserGrantVo tbUserGrantVo);
}
