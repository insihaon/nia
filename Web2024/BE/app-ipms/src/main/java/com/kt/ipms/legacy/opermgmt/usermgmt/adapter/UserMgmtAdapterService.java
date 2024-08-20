package com.kt.ipms.legacy.opermgmt.usermgmt.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

@Component
public class UserMgmtAdapterService {
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	@Transactional(readOnly = true)
	public TbUserBasVo selectTbuserBas(TbUserBasVo searchVo) {
		return userMgmtService.selectTbuserBas(searchVo);
	}
	@Transactional (propagation = Propagation.REQUIRED)
	public int updateTbuserBas(TbUserBasVo searchVo) {
		return userMgmtService.updateTbuserBas(searchVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void insertTbUserConnHist(LoginInfoVo loginInfoVo)  {
		userMgmtService.insertTbUserConnHist(loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void updateTbUserConnHist(LoginInfoVo loginInfoVo)  {
		userMgmtService.updateTbUserConnHist(loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public boolean isUserIpValidation(LoginInfoVo loginInfoVo)  {
		return userMgmtService.isUserIpValidation(loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public boolean checkTodayLogin(LoginInfoVo loginInfoVo) {
		return userMgmtService.checkTodayLogin(loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public Map<String, Object> selectUserPhoneNo(LoginInfoVo loginInfoVo) {
		return userMgmtService.selectUserPhoneNo(loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int insertApiHistory(Map<String, Object> paraMap) {
		return userMgmtService.insertApiHistory(paraMap);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void updateApiHistory(Map<String, Object> paraMap) {
		userMgmtService.updateApiHistory(paraMap);
	}
	
	public void insertOtpLogin(Map<String, Object> paraMap) {
		userMgmtService.insertOtpLogin(paraMap);
	}
	public Map<String, Object> selectOtpLogin(Map<String, Object> paraMap) {
		return userMgmtService.selectOtpLogin(paraMap);
	}
	public void updateOtpLogin(Map<String, Object> paraMap) {
		userMgmtService.updateOtpLogin(paraMap);
	}
	
	@Transactional(readOnly = true)
	public Integer checkIPLogin(LoginInfoVo loginInfoVo) {
		return userMgmtService.checkIPLogin(loginInfoVo);
	}
}
