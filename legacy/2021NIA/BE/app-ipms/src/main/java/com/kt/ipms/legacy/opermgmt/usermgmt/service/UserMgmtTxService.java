package com.kt.ipms.legacy.opermgmt.usermgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.dao.TbUserBasDao;
import com.kt.ipms.legacy.opermgmt.usermgmt.dao.TbUserConnHistDao;
import com.kt.ipms.legacy.opermgmt.usermgmt.dao.TbUserHndsetApyTxnDao;
import com.kt.ipms.legacy.opermgmt.usermgmt.dao.TbUserOtpDao;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Component
public class UserMgmtTxService {

	@Lazy @Autowired
	private TbUserBasDao tbUserBasDao;

	@Lazy @Autowired
	private TbUserConnHistDao tbUserConnHistDao;

	@Lazy @Autowired
	private TbUserHndsetApyTxnDao tbUserHndsetApyTxnDao;

	@Lazy @Autowired
	private TbUserOtpDao tbUserOtpDao;
	
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());
	
	
	/********************************** 사용자 관리 START ***********************************/
	@Transactional (propagation = Propagation.REQUIRED)
	public int insertTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.insertTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int updateTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.updateTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int deleteTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.deleteTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public TbUserBasVo selectTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.selectTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserBasVo> selectListTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.selectListTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserBasVo> selectListPageTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.selectListPageTbUserBasVo(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbUserBasVo(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.countListPageTbUserBasVo(tbUserBasVo);
	}
	
	/********************************** 사용자 관리 END *************************************/
	
	/********************************** 사용자 접속 이력 START ***********************************/
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int insertTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.insertTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int updateTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.updateTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int deleteTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.deleteTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public TbUserConnHistVo selectTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.selectTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserConnHistVo> selectListTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.selectListTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserConnHistVo> selectListPageTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.selectListPageTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo)  {
		return tbUserConnHistDao.countListPageTbUserConnHistVo(tbUserConnHistVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int insertTbUserConnHist(LoginInfoVo loginInfoVo)  {
		return tbUserConnHistDao.insertTbUserConnHist( loginInfoVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int updateTbUserConnHist(LoginInfoVo loginInfoVo)  {
		return tbUserConnHistDao.updateTbUserConnHist(loginInfoVo);
	}
	/********************************** 사용자 접속 이력 END *************************************/
	
	/********************************** 사용자 단말 관리 START ***********************************/
	@Transactional (propagation = Propagation.REQUIRED)
	public int insertTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.insertTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int updateTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.updateTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public int deleteTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.deleteTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public TbUserHndsetApyTxnVo selectTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.selectTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserHndsetApyTxnVo> selectListTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.selectListTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserHndsetApyTxnVo> selectListPageTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.selectListPageTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo)  {
		return  tbUserHndsetApyTxnDao.countListPageTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
	}
	/********************************** 사용자 단말 관리 END *************************************/
	
	@Transactional(readOnly = true)
	public String selectEmail(TbUserBasVo tbUserBasVo) {
		return tbUserBasDao.selectEmail(tbUserBasVo);
	}

	@Transactional(readOnly = true)
	public Map<String,Object> checkTodayLogin(LoginInfoVo loginInfoVo) {
		return tbUserOtpDao.checkTodayLogin(loginInfoVo);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> selectUserPhoneNo(LoginInfoVo loginInfoVo) {
		return tbUserOtpDao.selectUserPhoneNo(loginInfoVo);
	}

	@Transactional (propagation = Propagation.REQUIRED)
	public int insertApiHistory(Map<String, Object> paraMap) {
		return tbUserOtpDao.insertApiHistory(paraMap);
	}

	@Transactional (propagation = Propagation.REQUIRED)
	public void updateApiHistory(Map<String, Object> paraMap) {
		tbUserOtpDao.updateApiHistory(paraMap);
	}

	public void insertOtpLogin(Map<String, Object> paraMap) {
		tbUserOtpDao.insertOtpLogin(paraMap);
	}

	public Map<String, Object> selectOtpLogin(Map<String, Object> paraMap) {
		return tbUserOtpDao.selectOtpLogin(paraMap);
	}

	public void updateOtpLogin(Map<String, Object> paraMap) {
		tbUserOtpDao.updateOtpLogin(paraMap);
	}
	
	@Transactional(readOnly = true)
	public Integer checkIPLogin(LoginInfoVo loginInfoVo) {
		return tbUserConnHistDao.checkIPLogin(loginInfoVo);
	}
}
