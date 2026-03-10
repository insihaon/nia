package com.kt.ipms.legacy.opermgmt.loginmgmt.service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.opermgmt.grantmgmt.adapter.GrantMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.adapter.MenuMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.adapter.UserMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import nets.ldap.ADUtilSSL;

@Component
@Transactional
public class LoginMgmtTxService {

	@Lazy @Autowired
	private UserMgmtAdapterService userMgmtAdapterService;

	@Lazy @Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;

	@Lazy @Autowired
	private GrantMgmtAdapterService grantMgmtAdapterService;

	@Lazy @Autowired
	private MenuMgmtAdapterService menuMgmtAdapterService;

	@Lazy @Autowired
	private ConfigPropertieService configPropertieService;

	private final static int MAX_MENU_LEVEL = 4;
	
	
	@Transactional (readOnly = true)
	public void setHostInfomation(HttpServletRequest request, LoginInfoVo loginInfoVo)  {
		try {
			String localHost = InetAddress.getLocalHost().toString();
			StringTokenizer st = new StringTokenizer(localHost, "/");
			String host = st.nextToken();
			String ip = st.nextToken();
			loginInfoVo.setsConnIP(request.getRemoteAddr());
			loginInfoVo.setSapplSrvrId(host);		
			
			PrintLogUtil.printLog("LOGIN USER_ID [" + loginInfoVo.getSuserId()+"]");
			PrintLogUtil.printLog("getRemoteAddr [" + request.getRemoteAddr()+"]");
			PrintLogUtil.printLog("getRemoteHost [" + request.getRemoteHost()+"]");
			PrintLogUtil.printLog("getRemoteUser [" + request.getRemoteUser()+"]");
			PrintLogUtil.printLog("localhost    host [" + host+"]");
			PrintLogUtil.printLog("localhost   ip [" + ip+"]");

		} catch (Exception e) {
			// HOST 정보 설정이 실패하였습니다.
			PrintLogUtil.printLog("LOGIN setHostInfomation  EXCEPTION START ");
			PrintLogUtil.printLog(e.toString());
			PrintLogUtil.printLog("LOGIN setHostInfomation  EXCEPTION END ");
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional (readOnly = true)
	public boolean isLdapSuccess(LoginInfoVo loginInfoVo)  {
		boolean isLdapSuccess = false;
		try {
			//ADUtilSSL  ldapCheck = new ADUtilSSL();
			//	Boolean ldapRtn = false;
			String  host = configPropertieService.getString("Ldap.host");
			String port  = configPropertieService.getString("Ldap.port");
			String baseDn = configPropertieService.getString("Ldap.baseDN");
			String connID =  configPropertieService.getString("Ldap.connId");
			String connPWD =  configPropertieService.getString("Ldap.connPwd");
			
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO host ["+host+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO port ["+port+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO baseDn ["+baseDn+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO connID ["+connID+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO connPWD ["+connPWD+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO userId ["+loginInfoVo.getSuserId()+"]");
			PrintLogUtil.printLogInfo("LOGIN LDAP CONN INFO userPW ["+loginInfoVo.getsUserPw()+"]");
			isLdapSuccess  = ADUtilSSL.auth_basic_try(host, port, baseDn, loginInfoVo.getSuserId(), loginInfoVo.getsUserPw(), connID, connPWD, true);
			//isLdapSuccess = true;
		} catch (Exception e) {
			// LDAP 연동이 실패하였습니다.	
			PrintLogUtil.printLogInfo("LOGIN isLdapSuccess  EXCEPTION START ");
			PrintLogUtil.printLogInfo("[" + e.getMessage()+"]"+e.toString());
			PrintLogUtil.printLogInfo("LOGIN isLdapSuccess  EXCEPTION END ");
			
		}
		return isLdapSuccess;
	}
	
	@Transactional (readOnly = true)
	public TbUserBasVo selectUserBasInfo(TbUserBasVo tbUserBasVo)  {
		TbUserBasVo resultVo = null;
		try {
			resultVo = userMgmtAdapterService.selectTbuserBas(tbUserBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 정보 조회 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void insertUserConnHist(LoginInfoVo loginInfoVo)  {
		try {
			userMgmtAdapterService.insertTbUserConnHist(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 정속 정보 입력 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional (readOnly = true)
	public TbUserBasVo selectUserAdmrGrade(TbUserBasVo tbUserBasVo)  {
		TbUserBasVo resultVo = null;
		try {
			resultVo = userMgmtAdapterService.selectTbuserBas(tbUserBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 정보 조회 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	@Transactional (readOnly = true)
	public String selectUserGradeAdmr(LoginInfoVo loginInfoVo)  {
		String rtnVal = "";
		try {
			TbUserAuthTxnVo searchVo = new TbUserAuthTxnVo();
			searchVo.setSuserId(loginInfoVo.getSuserId());
			rtnVal = grantMgmtAdapterService.selectUserGradeAdmr(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			//  사용자 관리자 권한 정보 조회
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return rtnVal;
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public boolean isUserIpValidation(LoginInfoVo loginInfoVo)  {
		boolean isValidation = false;
		try {
			
			isValidation = userMgmtAdapterService.isUserIpValidation(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 IP 유효성 체크 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
		return isValidation;
	}
	
	@Transactional (readOnly = true)
	public void setGradeLevelInfo(LoginInfoVo loginInfoVo)  {
		try {
			String userGradeCd = loginInfoVo.getSuserGradeCd();
			TbLvlBasListVo resultListVo = null;
			if (userGradeCd.equals(CommonCodeUtil.USER_GRADE_A)) {
				TbLvlBasVo searchVo = new TbLvlBasVo();
				resultListVo = orgMgmtAdapterService.selectListSvcLine(searchVo);
			} else if (userGradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
				/*TbAdmrApvTxnVo searchVo =  new TbAdmrApvTxnVo();
				searchVo.setSuserId(loginInfoVo.getSuserId());
				resultListVo = grantMgmtAdapterService.selectListSvcLine(searchVo);	 */	
				TbUserAuthTxnVo searchVo = new TbUserAuthTxnVo();
				searchVo.setSuserId(loginInfoVo.getSuserId());
				resultListVo = grantMgmtAdapterService.selectListUserSvcLineList(searchVo);
			} else if (userGradeCd.equals(CommonCodeUtil.USER_GRADE_C) || userGradeCd.equals(CommonCodeUtil.USER_GRADE_O)) {
				/*TbOperTeamAuthTxnVo searchVo =  new TbOperTeamAuthTxnVo();
				searchVo.setSposDeptOrgId(loginInfoVo.getSposDeptOrgId());
				resultListVo = grantMgmtAdapterService.selectTeamSvcLineList(searchVo); */
				TbUserAuthTxnVo searchVo = new TbUserAuthTxnVo();
				searchVo.setSuserId(loginInfoVo.getSuserId());
				resultListVo = grantMgmtAdapterService.selectListUserSvcLineList(searchVo);
			}
			loginInfoVo.setUserAuthListVo(resultListVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 계위정보 설정 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional (readOnly = true)
	public void setMenuInfoByAuth(LoginInfoVo loginInfoVo)  {
		try {
			TbMenuBasListVo resultListVo = new TbMenuBasListVo();
			String usergrade = loginInfoVo.getSuserGradeCd();
			
			for (int i=1; i < MAX_MENU_LEVEL; i++) {
				TbMenuBasVo searchVo = new TbMenuBasVo();
				searchVo.setNmenuLvlSeq(i);
				searchVo.setSmenuUseYn("Y");
				if(!usergrade.equals(CommonCodeUtil.USER_GRADE_A)) {
					searchVo.setSuserGradeCd(usergrade);
				}
				TbMenuBasListVo tempListVo = menuMgmtAdapterService.selectListTbMenuBas(searchVo);
				if (tempListVo != null && tempListVo.getTotalCount() > 0) {
					if (i == 1) {
						resultListVo.setFirMenuBasVos(tempListVo.getTbMenuBasVos());
						resultListVo.setfTotalCount(tempListVo.getTotalCount());
					} else if (i == 2) {
						resultListVo.setSecMenuBasVos(tempListVo.getTbMenuBasVos());
						resultListVo.setsTotalConut(tempListVo.getTotalCount());
					} else if (i == 3) {
						resultListVo.setThrmenuBasVos(tempListVo.getTbMenuBasVos());
						resultListVo.settTotalCount(tempListVo.getTotalCount());
					}
				}
			}
			loginInfoVo.setMenuBasListVo(resultListVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 메뉴정보 설정 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void updateUserConnHist(LoginInfoVo loginInfoVo)  {
		try {
			userMgmtAdapterService.updateTbUserConnHist(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 정속 정보 업데이트 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void updateUserbas(TbUserBasVo tbUserBasVo)  {
		try {
			userMgmtAdapterService.updateTbuserBas(tbUserBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 정속 정보 업데이트 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
	}

	/* 사용자 일일 로그인 체크 */
	@Transactional (propagation = Propagation.REQUIRED)
	public boolean checkTodayLogin(LoginInfoVo loginInfoVo) {
		boolean isValidation = false;
		try {	
			isValidation = userMgmtAdapterService.checkTodayLogin(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 금일 로그인 체크 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
		return isValidation;
	}

	/* 사용자 전화번호 검색 */
	@Transactional (propagation = Propagation.REQUIRED)
	public Map<String, Object> selectUserPhoneNo(LoginInfoVo loginInfoVo) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap = userMgmtAdapterService.selectUserPhoneNo(loginInfoVo);
		return retMap;
	}

	public int insertApiHistory(LoginInfoVo loginInfoVo, String apitype) {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		
//		if(apitype=="SEND"){
		if(apitype.equals("SEND")){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			paraMap.put("suserId", loginInfoVo.getSuserId());
			paraMap.put("sapiType", "SEND");
		}else{
			paraMap.put("suserId", loginInfoVo.getSuserId());
			paraMap.put("sapiType", "CONFIRM");
		}
		
		userMgmtAdapterService.insertApiHistory(paraMap);
		int tmp = 0;
		tmp = Integer.parseInt(paraMap.get("napi_hist_mst_seq").toString());
		
		return tmp;
	}

	public void updateApiHistory(Map<String, Object> paraMap) {
		userMgmtAdapterService.updateApiHistory(paraMap);
	}

	public void insertOtpLogin(Map<String, Object> paraMap) {
		userMgmtAdapterService.insertOtpLogin(paraMap);
	}

	public Map<String, Object> selectOtpLogin(Map<String, Object> paraMap) {
		return userMgmtAdapterService.selectOtpLogin(paraMap);
	}

	public void updateOtpLogin(Map<String, Object> paraMap) {
		userMgmtAdapterService.updateOtpLogin(paraMap);
	}
	
	@Transactional (readOnly = true)
	public Integer checkIPLogin(LoginInfoVo loginInfoVo) {
		return userMgmtAdapterService.checkIPLogin(loginInfoVo);
	}
}
