package com.kt.ipms.legacy.opermgmt.grantmgmt.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;

@Component
public class GrantMgmtService {	
	
	
	@Autowired
	GrantMgmtTxService grantMgmtTxService;
	
	@Transactional(readOnly = true)
	public  TbOperTeamAuthTxnListVo selectOperTeamAuthTxnList(TbOperTeamAuthTxnVo searchVo)   {
		
		TbOperTeamAuthTxnListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbOperTeamAuthTxnVo>  resultVos = grantMgmtTxService.selectListPageTbOperTeamAuthTxnVo(searchVo);
			int totalCount		=	grantMgmtTxService.countListPageTbOperTeamAuthTxnVo(searchVo);
			resultListVo = new TbOperTeamAuthTxnListVo();
			resultListVo.setTbOperTeamAuthTxnVos(resultVos);
			resultListVo.setTotalCount(totalCount);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용조직 권한내역"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectTeamSvcLineList(TbOperTeamAuthTxnVo searchVo)   {
		
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListTeamSvcLineList(searchVo);
			
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(teamSvcLineVos);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 서비스망 "});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListTeamCenterList(TbOperTeamAuthTxnVo searchVo)   {
			
			TbLvlBasListVo resultListVo = null;
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			try
			{
				List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListTeamCenterList(searchVo);
				
				resultListVo = new TbLvlBasListVo();
				resultListVo.setTbLvlBasVos(teamSvcLineVos);
			}
			catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 본부"});
			}
			return resultListVo;
		}

	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListTeamNodeList(TbOperTeamAuthTxnVo searchVo)   {
		
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListTeamNodeList(searchVo);
			
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(teamSvcLineVos);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 노드"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListSvcLine(TbAdmrApvTxnVo searchVo)   {
		
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListSvcLine(searchVo);
			
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(teamSvcLineVos);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 서비스망"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public  String selectUserGradeAdmr(TbAdmrApvTxnVo searchVo)   {
		
		String result = "";
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			result =  grantMgmtTxService.selectUserGradeAdmr(searchVo);			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{" 관리자 사용자 등급 "});
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public  String selectUserGrade(TbUserAuthTxnVo searchVo)   {
		
		String result = "";
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			result =  grantMgmtTxService.selectUserGrade(searchVo);			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{" 관리자 사용자 등급 "});
		}
		return result;
	}

	@Transactional(readOnly = true)
	public TbAdmrApvTxnListVo selectListTbAdmrApvTxnVo(TbAdmrApvTxnVo searchVo)  {
		
		TbAdmrApvTxnListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			List<TbAdmrApvTxnVo> resultList = grantMgmtTxService.selectListPageTbAdmrApvTxnVo(searchVo);
			int totalCount = grantMgmtTxService.countListPageTbAdmrApvTxnVo(searchVo);
			resultListVo = new TbAdmrApvTxnListVo();
			resultListVo.setTbAdmrApvTxnVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"관리자 권한내역"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertAdmrAutTxn(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		
		int result;
		try
		{
			
			result = grantMgmtTxService.countListPageTbAdmrApvTxnVo(tbAdmrApvTxnVo) ;
			
			if(result == 0)
			{					
				String userGrade = grantMgmtTxService.selectUserGradeAdmr(tbAdmrApvTxnVo);
				
				if(userGrade.equals(CommonCodeUtil.USER_GRADE_A))
				{
					throw new ServiceException("APP.INFO.00041");
				}
				else
				{	
					if(tbAdmrApvTxnVo.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_A))
					{
						TbAdmrApvTxnVo deleteVo =  new TbAdmrApvTxnVo();
						deleteVo.setSuserId(tbAdmrApvTxnVo.getSuserId());
						deleteVo.setSuserGradeCd(CommonCodeUtil.USER_GRADE_N);
						
						result  =  grantMgmtTxService.deleteAdmrAutTxn(deleteVo);			
					}
					result  =  grantMgmtTxService.insertAdmrAutTxn(tbAdmrApvTxnVo);	
					
					TbUserBasVo updateUserVo = new TbUserBasVo();
					updateUserVo.setSuserId(tbAdmrApvTxnVo.getSuserId());
					updateUserVo.setSipmsUserYn("Y");
					updateUserVo.setSmodifyId(tbAdmrApvTxnVo.getSmodifyId());
					result = grantMgmtTxService.updateUserBas(updateUserVo);
				}				
			}
			else if (result >=0)
			{
				throw new ServiceException("APP.INFO.00040");
			}
			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"관리자 권한"});
		}
		
		return result;
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteAdmrAutTxn(TbAdmrApvTxnListVo tbAdmrApvTxnListVo)  {
		
		int result=0;
		try
		{
			if(tbAdmrApvTxnListVo.getTbAdmrApvTxnVos() != null  && tbAdmrApvTxnListVo.getTbAdmrApvTxnVos().size() != 0)
			{
				int cnt =  tbAdmrApvTxnListVo.getTbAdmrApvTxnVos().size();
				for(int i=0; i < cnt; i++)
				{
					TbAdmrApvTxnVo  deleteVo = tbAdmrApvTxnListVo.getTbAdmrApvTxnVos().get(i);
					result = grantMgmtTxService.deleteAdmrAutTxn(deleteVo) ;
				}
			}		
			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"관리자 권한"});
		}
		
		return result;
		
	}
	
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserSvcLineList(TbUserAuthTxnVo searchVo)   {
		
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListUserSvcLineList(searchVo);
			
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(teamSvcLineVos);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 서비스망 "});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserCenterList(TbUserAuthTxnVo searchVo)   {
			
			TbLvlBasListVo resultListVo = null;
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			try
			{
				List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListUserCenterList(searchVo);
				
				resultListVo = new TbLvlBasListVo();
				resultListVo.setTbLvlBasVos(teamSvcLineVos);
			}
			catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 본부"});
			}
			return resultListVo;
		}

	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListUserNodeList(TbUserAuthTxnVo searchVo)   {
		
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbLvlBasVo>  teamSvcLineVos = grantMgmtTxService.selectListUserNodeList(searchVo);
			
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(teamSvcLineVos);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용자권한 노드"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbUserAuthTxnListVo selectUserAuthTxnList(TbUserAuthTxnVo searchVo)	{
		
		TbUserAuthTxnListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbUserAuthTxnVo>	resultVos	=	grantMgmtTxService.selectListPageTbUserAuthTxnVo(searchVo);
			int totalCount	=	grantMgmtTxService.countListPageTbUserAuthTxnVo(searchVo);
			resultListVo	=	new	TbUserAuthTxnListVo();
			resultListVo.setTbUserAuthTxnVos(resultVos);
			resultListVo.setTotalCount(totalCount);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 권한내역"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbUserAuthTxnListVo selectDetailUserAuthTxn(TbUserAuthTxnVo searchVo)	{
		
		TbUserAuthTxnListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			TbUserBasVo userBasVo = new TbUserBasVo();
			userBasVo.setSuserId(searchVo.getSuserId());
			userBasVo =  grantMgmtTxService.selectUserBasVo(userBasVo);
			
			List<TbUserAuthTxnVo>	resultVos	=	grantMgmtTxService.selectListTbUserAuthTxnVo(searchVo);
			int totalCount	=	grantMgmtTxService.countListTbUserAuthTxnVo(searchVo);
			resultListVo	=	new	TbUserAuthTxnListVo();
			resultListVo.setTbUserAuthTxnVos(resultVos);
			resultListVo.setTotalCount(totalCount);
			resultListVo.setSuserId(userBasVo.getSuserId());
			resultListVo.setSuserNm(userBasVo.getSuserNm());
			resultListVo.setTypeFlag(searchVo.getTypeFlag());
			resultListVo.setSuserGradeCd(userBasVo.getSuserGradeCd());
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 권한 상세내역"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteTbUserAuthTxnVo(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		if(tbUserAuthTxnListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			grantMgmtTxService.deleteTbUserAuthListVo(tbUserAuthTxnListVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 권한삭제"});
		}
	
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteTbUserAuthTxnByLvlBasSeq(TbUserAuthTxnVo tbUserAuthTxnVo){
		if(tbUserAuthTxnVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			grantMgmtTxService.deleteTbUserAuthByLvlBasSeq(tbUserAuthTxnVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 권한삭제"});
		}
	
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertTbUserAuthTxnVo(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		if(tbUserAuthTxnListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
				grantMgmtTxService.deleteTbUserAuthByUserId(tbUserAuthTxnListVo);
				
				if(tbUserAuthTxnListVo.getTbUserAuthTxnVos() != null) {
					List<TbUserAuthTxnVo> insertVos = tbUserAuthTxnListVo.getTbUserAuthTxnVos();
					int iCnt = insertVos.size();
					
					for(int i=0; i<iCnt; i++){
						TbUserAuthTxnVo insertVo = insertVos.get(i);
						
						if(insertVo.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_A)){
							grantMgmtTxService.insertTbUserAuthTxnVo(insertVo);
						}else
						{
							if(insertVo.getTbLvlBasVo() != null){
								TbLvlBasVo lvlBasVo = grantMgmtTxService.selectTbLvlBasVo(insertVo.getTbLvlBasVo());
								if( lvlBasVo != null && lvlBasVo.getNlvlBasSeq() != null ){
									insertVo.setNlvlBasSeq(lvlBasVo.getNlvlBasSeq());
									grantMgmtTxService.insertTbUserAuthTxnVo(insertVo);
								}
							}
						}
					}
				}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 권한등록"});
		}
	
	}

}
