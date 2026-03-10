package com.kt.ipms.legacy.cmn.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.dao.CommonDao;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;

@Component
@Transactional
public class CommonService {
	
	@Lazy @Autowired
	private CommonDao commonDao;
	
	@Transactional(readOnly = true)
	public int selectIpVersionValidation(String ipAddress) {
		return commonDao.selectIpVersionValidation(ipAddress);
	}
	
	@Transactional(readOnly = true)
	public Date selectSysDate() {
		return commonDao.selectSysDate();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertEmailLog(SmtpVo smtpVo){
		return commonDao.insertEmailLog(smtpVo);
	}

}
