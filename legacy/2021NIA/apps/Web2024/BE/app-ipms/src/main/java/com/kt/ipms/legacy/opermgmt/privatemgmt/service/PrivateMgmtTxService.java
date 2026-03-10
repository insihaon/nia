package com.kt.ipms.legacy.opermgmt.privatemgmt.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.dao.TbIpPrivateMstDao;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstVo;

@Component @Transactional
public class PrivateMgmtTxService {

	@Lazy @Autowired
	private TbIpPrivateMstDao tbIpPrivateDao;

	@Transactional(readOnly = true)
	public List<TbIpPrivateReqMstVo> selectListTbIpPrivateReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectListTbIpPrivateReqMst(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageIpPrivateReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.countListPageIpPrivateReqMst(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public TbIpPrivateReqMstVo selectPrivateIPMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectPrivateIPMst(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public String checkPrivateIpReqValid(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.checkPrivateIpReqValid(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public String selectNipPrivateReqMstSeqCd(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectNipPrivateReqMstSeqCd(tbIpPrivateReqMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListIpPrivateReqMstVo(TbIpPrivateReqMstVo tbIpPrivateReqMstVo)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		tbIpPrivateDao.insertListIpPrivateReqMstVo(tbIpPrivateReqMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deletePrivateIPMstVo(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.deletePrivateIPMstVo(tbIpPrivateReqMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePrivateIPMstAppr(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		tbIpPrivateDao.updatePrivateIPMstAppr(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public TbIpPrivateReqMstVo selectIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpPrivateDao.selectIpBlockMst(tbIpBlockMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpPrivateReqMstVo> selectListTbIpPrivateDelReq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectListTbIpPrivateDelReq(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public int countListTbIpPrivateDelReq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.countListTbIpPrivateDelReq(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public TbIpPrivateReqMstVo selectPrivateDelIPMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectPrivateDelIPMst(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpPrivateReqMstVo> selectPrivateIPMstAll(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectPrivateIPMstAll(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpPrivateReqMstVo> selectPrivateIPMstSeqCd(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectPrivateIPMstSeqCd(tbIpPrivateReqMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpPrivateReqMstVo> selectNlvlMstSeq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		return tbIpPrivateDao.selectNlvlMstSeq(tbIpPrivateReqMstVo);
	}

}
