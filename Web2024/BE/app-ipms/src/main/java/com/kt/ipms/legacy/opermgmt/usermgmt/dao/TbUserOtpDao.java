package com.kt.ipms.legacy.opermgmt.usermgmt.dao;

import java.util.Map;

import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;

public interface TbUserOtpDao {

	public Map<String,Object> checkTodayLogin(LoginInfoVo loginInfoVo);

	public Map<String, Object> selectUserPhoneNo(LoginInfoVo loginInfoVo);

	public int insertApiHistory(Map<String, Object> paraMap);

	public void updateApiHistory(Map<String, Object> paraMap);

	public void insertOtpLogin(Map<String, Object> paraMap);

	public Map<String, Object> selectOtpLogin(Map<String, Object> paraMap);

	public void updateOtpLogin(Map<String, Object> paraMap);

}
