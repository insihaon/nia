package com.kt.ipms.legacy.cmn.dao;

import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.SmtpVo;

public interface CommonDao {

	public int selectIpVersionValidation(String ipAddress);

	public Date selectSysDate();
	
	public int insertEmailLog(SmtpVo smtpVo);
}
