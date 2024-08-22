package com.kt.ipms.legacy.cmn.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.cmn.vo.SmtpVo;

@Mapper
public interface CommonDao {

	public int selectIpVersionValidation(String ipAddress);

	public Date selectSysDate();
	
	public int insertEmailLog(SmtpVo smtpVo);
}
