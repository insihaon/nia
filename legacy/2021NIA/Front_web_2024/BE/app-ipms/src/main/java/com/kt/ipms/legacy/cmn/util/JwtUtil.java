package com.kt.ipms.legacy.cmn.util;



import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.ipms.provider.IpmsJwtTokenProvider;
import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.opermgmt.grantmgmt.adapter.GrantMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.loginmgmt.service.LoginMgmtTxService;

@Component
public class JwtUtil {

	@Autowired
	private IpmsJwtTokenProvider  ipmsJwtTokenProvider;
	@Autowired
	private GrantMgmtAdapterService  grantMgmtAdapterService;
	@Autowired
	private LoginMgmtTxService  loginMgmtTxService;
	
	@Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;
	
	
	public void setNewSession(HttpServletRequest request, Object user)  {
		// invalidSession(request);
		// HttpSession session = request.getSession(true);
		// session.setAttribute("user", user);
	}
	
	public void invalidSession(HttpServletRequest request)  {
		// HttpSession session = request.getSession(false);
		// if (session != null) {
		// 	session.removeAttribute("user");
		// 	session.invalidate();
		// }
	}
	
	public  LoginInfoVo getSessionVO(HttpServletRequest request)  {
		if (request == null) throw new  ServiceException("CMN.HIGH.00001");
		String token = ipmsJwtTokenProvider.resolveToken(request);
		if (token != null) return (LoginInfoVo) ipmsJwtTokenProvider.getIpmsUserDetails(token);
		else return null;
	}
	
	public  String getUserId(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSuserId();
		else return null;
	}
	
	public  String getUserNm(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSuserNm();
		else return null;
	}
	
	
	public  String getUserDeptOrgCd(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSposDeptOrgId();
		else return null;
	}
	
	public  String getUserDeptOrgNm(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSposDeptOrgNm();
		else return null;
	}
	
	public  String getUserMphonNo(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSuserMphonNo();
		else return null;
	}
	
	public  String getUserEmailAddr(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSuserEmailAdr();
		else return null;
	}
	
	public  String getUserGradeCd(HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getSuserGradeCd();
		else return null;
	}
	
	public  TbMenuBasListVo getMenuList (HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if (sessionVO != null) return sessionVO.getMenuBasListVo();
		else return null;
	}
	
	public TbLvlBasListVo getSvcLineList (HttpServletRequest request)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		if(sessionVO != null) {
			loginMgmtTxService.setGradeLevelInfo(sessionVO);
			return sessionVO.getUserAuthListVo();
		}
		// if (sessionVO != null) return sessionVO.getUserAuthListVo();
		else return null;
	}
	
	
	public TbLvlBasListVo getCenterList (HttpServletRequest request, TbLvlBasVo tbLvlBasVo)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		
		TbLvlBasListVo  tbLvlBasListVo = null;
		
		if(sessionVO != null )
		{
		
			if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_A))  /*관리자 */
			{				
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistCenter(tbLvlBasVo);
			}				
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_S)) /*시스템 관리자*/
			{				
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistCenter(tbLvlBasVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_N)) /*망관리자 */
			{
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistCenter(tbLvlBasVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_C))  /* 센터 관리자 */
			{
				/*
				TbOperTeamAuthTxnVo searchVo =  new TbOperTeamAuthTxnVo();
				searchVo.setSposDeptOrgId(sessionVO.getSposDeptOrgId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListTeamCenterList(searchVo); */
				
				TbUserAuthTxnVo searchVo =  new TbUserAuthTxnVo();
				searchVo.setSuserId(sessionVO.getSuserId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListUserCenterList(searchVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_O)) /* 일반 운용자  */
			{
				/*TbOperTeamAuthTxnVo searchVo =  new TbOperTeamAuthTxnVo();
				searchVo.setSposDeptOrgId(sessionVO.getSposDeptOrgId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListTeamCenterList(searchVo); */
				TbUserAuthTxnVo searchVo =  new TbUserAuthTxnVo();
				searchVo.setSuserId(sessionVO.getSuserId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListUserCenterList(searchVo);
			}
				
		}
		
		if (sessionVO != null) return tbLvlBasListVo;
		else return null;
	}
	
	
	public TbLvlBasListVo getNodeList (HttpServletRequest request , TbLvlBasVo tbLvlBasVo)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		
		TbLvlBasListVo  tbLvlBasListVo = null;
		
		if(sessionVO != null )
		{
		
			if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_A))  /*관리자 */
			{
				
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistNode(tbLvlBasVo);
			}				
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_S)) /*시스템 관리자*/
			{
				
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistNode(tbLvlBasVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_N)) /*망관리자 */
			{
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistNode(tbLvlBasVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_C))  /* 센터 관리자 */
			{
				tbLvlBasListVo =  orgMgmtAdapterService.selectlistNode(tbLvlBasVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_O)) /* 일반 운용자  */
			{
				/*TbOperTeamAuthTxnVo searchVo =  new TbOperTeamAuthTxnVo();
				searchVo.setSposDeptOrgId(sessionVO.getSposDeptOrgId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListTeamNodeList(searchVo); */
				
				TbUserAuthTxnVo searchVo =  new TbUserAuthTxnVo();
				searchVo.setSuserId(sessionVO.getSuserId());
				searchVo.setTbLvlBasVo(tbLvlBasVo);
				tbLvlBasListVo = grantMgmtAdapterService.selectListUserNodeList(searchVo);
			}
				
		}
		
		if (sessionVO != null) return tbLvlBasListVo;
		else return null;
	}
	
	public TbLvlMstListVo getLvlSeqList (HttpServletRequest request , TbLvlMstVo tbLvlMstVo)  {
		LoginInfoVo sessionVO = getSessionVO(request);
		
		TbLvlMstListVo  resultListVo = null;
		
		if(sessionVO != null )
		{
		
			if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_A))  /*관리자 */
			{				
				resultListVo =  orgMgmtAdapterService.selectListTbLvlMstVo(tbLvlMstVo);
			}				
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_S)) /*시스템 관리자*/
			{				
				resultListVo =  orgMgmtAdapterService.selectListTbLvlMstVo(tbLvlMstVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_N)) /*망관리자 */
			{
				tbLvlMstVo.setSearchWrd(sessionVO.getSuserId());
				resultListVo =  orgMgmtAdapterService.selectListMstSeqBySvcLine(tbLvlMstVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_C))  /* 센터 관리자 */
			{
				tbLvlMstVo.setSearchWrd(sessionVO.getSuserId());
				resultListVo =  orgMgmtAdapterService.selectListMstSeqByCenter(tbLvlMstVo);
			}
			else if (sessionVO.getSuserGradeCd().equals(CommonCodeUtil.USER_GRADE_O)) /* 일반 운용자  */
			{
				tbLvlMstVo.setSearchWrd(sessionVO.getSuserId());
				resultListVo = orgMgmtAdapterService.selectListMstSeqByOper(tbLvlMstVo);
			}
				
		}
		
		if (sessionVO != null) return resultListVo;
		else return null;
	}
	
	public BigInteger getLvlMstSeq (HttpServletRequest request , TbLvlBasVo tbLvlBasVo)  {
		BigInteger returnVal = null ;
		
		TbLvlBasVo resultVo = orgMgmtAdapterService.selectTbLvlBas(tbLvlBasVo);
		
		if(resultVo !=  null)
		{			
			returnVal = resultVo.getNmaxSeq();
		}
		
		return  returnVal;
	}
	
	public BigInteger getLvlBasSeq(HttpServletRequest request, TbLvlBasVo tbLvlBasVo) {
		BigInteger returnVal = null ;
		
		TbLvlBasVo resultVo = orgMgmtAdapterService.selectTbLvlBas(tbLvlBasVo);
		
		if(resultVo !=  null)
		{			
			returnVal = resultVo.getNlvlBasSeq();
		}
		
		return  returnVal;
	}
	
	
}
