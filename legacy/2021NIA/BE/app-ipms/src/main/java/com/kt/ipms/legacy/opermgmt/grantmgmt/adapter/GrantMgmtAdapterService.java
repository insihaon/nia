package com.kt.ipms.legacy.opermgmt.grantmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.grantmgmt.service.GrantMgmtService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;


@Component
@Transactional
public class GrantMgmtAdapterService {
	
	
	@Autowired
	GrantMgmtService grantMgmtService;

	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectTeamSvcLineList(TbOperTeamAuthTxnVo searchVo)  {		
		return grantMgmtService.selectTeamSvcLineList(searchVo);	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListTeamCenterList(TbOperTeamAuthTxnVo searchVo)  {		
		return grantMgmtService.selectListTeamCenterList(searchVo);	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListTeamNodeList(TbOperTeamAuthTxnVo searchVo)  {		
		return grantMgmtService.selectListTeamNodeList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public TbLvlBasListVo selectListSvcLine(TbAdmrApvTxnVo searchVo)   {
		return grantMgmtService.selectListSvcLine(searchVo);
	}

	@Transactional(readOnly = true)
	public String selectUserGradeAdmr(TbUserAuthTxnVo searchVo)   {
		//return grantMgmtService.selectUserGradeAdmr(searchVo);
		
		return grantMgmtService.selectUserGrade(searchVo);
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserSvcLineList(TbUserAuthTxnVo searchVo)  {		
		return grantMgmtService.selectListUserSvcLineList(searchVo);	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserCenterList(TbUserAuthTxnVo searchVo)  {		
		return grantMgmtService.selectListUserCenterList(searchVo);	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserNodeList(TbUserAuthTxnVo searchVo)  {		
		return grantMgmtService.selectListUserNodeList(searchVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void  deleteTbUserAuthTxnByLvlBasSeq(TbUserAuthTxnVo searchVo)  {		
		 grantMgmtService.deleteTbUserAuthTxnByLvlBasSeq(searchVo);	
	}
	
	
}
