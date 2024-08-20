package com.kt.ipms.legacy.opermgmt.usermgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnListVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Component
public class UserMgmtService {
		
	@Autowired
	UserMgmtTxService userMgmtTxService;
	
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());
	
	@Transactional(readOnly = true)
	public TbUserBasListVo selectListTbUserBas(TbUserBasVo searchVo)  {
		
		TbUserBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			List<TbUserBasVo> resultList = userMgmtTxService.selectListPageTbUserBasVo(searchVo);
			int totalCount = userMgmtTxService.countListPageTbUserBasVo(searchVo);
			resultListVo = new TbUserBasListVo();
			resultListVo.setTbUserBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 기본정보내역"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbUserBasListVo selectSearchTbUserBas(TbUserBasVo searchVo)  {
		
		TbUserBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			List<TbUserBasVo> resultList = userMgmtTxService.selectListTbUserBasVo(searchVo);
			int totalCount = userMgmtTxService.countListPageTbUserBasVo(searchVo);
			resultListVo = new TbUserBasListVo();
			resultListVo.setTbUserBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 검색"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbUserBasVo selectTbuserBas(TbUserBasVo searchVo){
	
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		TbUserBasVo resultVo = null;
		try
		{
			resultVo = userMgmtTxService.selectTbUserBasVo(searchVo);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 상세정보"});
		}
		return resultVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTbuserBas(TbUserBasVo tbUserBasVo){
		
		int result;
		if (tbUserBasVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			result = userMgmtTxService.updateTbUserBasVo(tbUserBasVo);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 기본정보"});
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public TbUserConnHistListVo selectListTbUserConnHist(TbUserConnHistVo searchVo)  {
		
		TbUserConnHistListVo resultListVo = null;
		
		try {
			
			List<TbUserConnHistVo> resultList = userMgmtTxService.selectListPageTbUserConnHistVo(searchVo);
			int totalCount = userMgmtTxService.countListPageTbUserConnHistVo(searchVo);
			resultListVo = new TbUserConnHistListVo();
			resultListVo.setTbUserConnHistVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 접속이력내역"});
		}
		return resultListVo;
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void insertTbUserConnHist(LoginInfoVo loginInfoVo)  {
		try
		{
			userMgmtTxService.insertTbUserConnHist(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 접속이력내역"});
		}
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void updateTbUserConnHist(LoginInfoVo loginInfoVo)  {
		try
		{
			userMgmtTxService.updateTbUserConnHist(loginInfoVo);	
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 접속이력내역"});
		}
	}
	
	
	@Transactional(readOnly = true)
	public TbUserHndsetApyTxnListVo selectListTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo searchVo){
		
		TbUserHndsetApyTxnListVo resultListVo = null;
		
		try {
			
			List<TbUserHndsetApyTxnVo> resultList = userMgmtTxService.selectListTbUserHndsetApyTxnVo(searchVo);
			int totalCount = userMgmtTxService.countListPageTbUserHndsetApyTxnVo(searchVo);
			resultListVo = new TbUserHndsetApyTxnListVo();
			resultListVo.setTbUserHndsetApyTxnVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 접속이력내역"});
		}
		return resultListVo;
	}	
	
	@Transactional (propagation = Propagation.REQUIRED)
	public TbUserHndsetApyTxnListVo insertTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		TbUserHndsetApyTxnListVo resultListVo = null;
		try
		{
			int dupCheckVal = 0;
			int resultVal = 0;
			String strMsgCd = "";
			
			TbUserHndsetApyTxnVo dupCheckVo = new TbUserHndsetApyTxnVo();
			dupCheckVo.setSuserId(tbUserHndsetApyTxnVo.getSuserId());
			dupCheckVo.setSuserHndsetId(tbUserHndsetApyTxnVo.getSuserHndsetId());
			dupCheckVal = userMgmtTxService.countListPageTbUserHndsetApyTxnVo(dupCheckVo);
			
			if(dupCheckVal > 0)
			{
				strMsgCd= "DUP";
				resultVal = dupCheckVal;
			}
			else {
				resultVal  = userMgmtTxService.insertTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);	
				strMsgCd = CommonCodeUtil.SUCCESS_MSG;
			}
			
			if(resultVal > 0 ){
				TbUserHndsetApyTxnVo searchVo = new TbUserHndsetApyTxnVo();
				searchVo.setSuserId(tbUserHndsetApyTxnVo.getSuserId());
				List<TbUserHndsetApyTxnVo> resultList = userMgmtTxService.selectListTbUserHndsetApyTxnVo(searchVo);
				int totalCount = userMgmtTxService.countListPageTbUserHndsetApyTxnVo(searchVo);
				resultListVo = new TbUserHndsetApyTxnListVo();
				resultListVo.setTbUserHndsetApyTxnVos(resultList);
				resultListVo.setTotalCount(totalCount);
				resultListVo.setCommonMsg(strMsgCd);
			}		
				
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 단말등록"});
		}
		
		return resultListVo;
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public TbUserHndsetApyTxnListVo deleteTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		TbUserHndsetApyTxnListVo resultListVo = null;
		try
		{
			int resultVal = 0;
			resultVal  = userMgmtTxService.deleteTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
			
			if(resultVal > 0 ){
				TbUserHndsetApyTxnVo searchVo = new TbUserHndsetApyTxnVo();
				searchVo.setSuserId(tbUserHndsetApyTxnVo.getSuserId());
				List<TbUserHndsetApyTxnVo> resultList = userMgmtTxService.selectListTbUserHndsetApyTxnVo(searchVo);
				int totalCount = userMgmtTxService.countListPageTbUserHndsetApyTxnVo(searchVo);
				resultListVo = new TbUserHndsetApyTxnListVo();
				resultListVo.setTbUserHndsetApyTxnVos(resultList);
				resultListVo.setTotalCount(totalCount);
			}
				
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 단말삭제"});
		}
		
		return resultListVo;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isUserIpValidation(LoginInfoVo loginInfoVo){
		
		boolean isIpCheckSuccess = false;
		if (loginInfoVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			int  checkCnt = 0;
			TbUserHndsetApyTxnVo  checkUserVo = new TbUserHndsetApyTxnVo();
			checkUserVo.setSuserId(loginInfoVo.getSuserId());
			checkCnt = 	userMgmtTxService.countListPageTbUserHndsetApyTxnVo(checkUserVo);
			
			if(checkCnt > 0) {
				
				checkUserVo.setSuserId(loginInfoVo.getSuserId());
				checkUserVo.setSuserHndsetId(loginInfoVo.getsConnIP());				
				checkCnt = 	userMgmtTxService.countListPageTbUserHndsetApyTxnVo(checkUserVo);
				
				if(checkCnt == 0 ) {
					isIpCheckSuccess = false;
				}
				else
				{
					isIpCheckSuccess = true;
				}
			}
			else {
				TbUserHndsetApyTxnVo insertVo = new TbUserHndsetApyTxnVo();
				insertVo.setSuserId(loginInfoVo.getSuserId());
				insertVo.setSuserHndsetId(loginInfoVo.getsConnIP());
				insertVo.setShndsetUseSttusCd("UI0002");
				insertVo.setScomment("최초등록");
				insertVo.setShndsetApvUserId(loginInfoVo.getSuserId());
				insertVo.setScreateId(loginInfoVo.getSuserId());
				insertVo.setSmodifyId(loginInfoVo.getSuserId());
				userMgmtTxService.insertTbUserHndsetApyTxnVo(insertVo);
				isIpCheckSuccess = true;
			}
			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자  허용 IP 정보"});
		}
		return isIpCheckSuccess;
	}
	
	/**
	 * Email Search
	 * @param searchVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public String selectEmail (TbUserBasVo searchVo){
	
		String email = "";
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			email = userMgmtTxService.selectEmail(searchVo);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용자 상세정보"});
		}
		return email;
	}

	public boolean checkTodayLogin(LoginInfoVo loginInfoVo) {
		boolean isTodayCheck = false;
		
		if (loginInfoVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			int  checkCnt = 0;
			Map<String,Object> tempMap = userMgmtTxService.checkTodayLogin(loginInfoVo);
			checkCnt = Integer.parseInt(tempMap.get("login_cnt").toString());
			
			if(checkCnt == 0 ) {
				isTodayCheck = false;
			}else{
				isTodayCheck = true;
			}
			
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"사용자 로그인 성공 정보 검색에 실패하였습니다"});
		}
		
		return isTodayCheck;
		
	}

	public Map<String, Object> selectUserPhoneNo(LoginInfoVo loginInfoVo) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		
		try{
			retMap = userMgmtTxService.selectUserPhoneNo(loginInfoVo);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.INFO.00054", new String[]{"사용자 휴대폰 정보 검색에 실패하였습니다"});
		}
		
		return retMap;
	}

	public int insertApiHistory(Map<String, Object> paraMap) {
		int retInt = 0;
		try{
			retInt = userMgmtTxService.insertApiHistory(paraMap);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"API 히스토리 등록에 실패하였습니다"});
		}
		
		return retInt;
	}

	public void updateApiHistory(Map<String, Object> apiRetMap) {
		
		try{
			userMgmtTxService.updateApiHistory(apiRetMap);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"API 히스토리 수정에 실패하였습니다"});
		}
		
	}

	public void insertOtpLogin(Map<String, Object> paraMap) {
		try{
			userMgmtTxService.insertOtpLogin(paraMap);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"API 히스토리 수정에 실패하였습니다"});
		}
	}

	public Map<String, Object> selectOtpLogin(Map<String, Object> paraMap) {
		try{
			return userMgmtTxService.selectOtpLogin(paraMap);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"API 히스토리 수정에 실패하였습니다"});
		}
	}

	public void updateOtpLogin(Map<String, Object> paraMap) {
		try{
			userMgmtTxService.updateOtpLogin(paraMap);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"API 히스토리 수정에 실패하였습니다"});
		}
	}
	
	@Transactional(readOnly = true)
	public Integer checkIPLogin(LoginInfoVo loginInfoVo) {
		return userMgmtTxService.checkIPLogin(loginInfoVo);
	}
}
