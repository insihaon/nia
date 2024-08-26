package com.kt.ipms.legacy.ipmgmt.linkmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.dao.TbIpLinkMstDao;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstVo;

@Component @Transactional
public class LinkMgmtTxService {

	@Lazy @Autowired
	private TbIpLinkMstDao tbIpLinkMstDao;

	@Transactional(readOnly = true)
	public List<TbIpLinkMstVo> selectListPageIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.selectListPageIpLinkMst(tbIpLinkMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.countListPageIpLinkMst(tbIpLinkMstVo);
	}

	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.selectLoadOfficeList(tbIpLinkMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpLinkMstVo> selectOfficeList(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.selectOfficeList(tbIpLinkMstVo);
	}

	@Transactional(readOnly = true)
	public TbIpLinkMstVo selectTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.selectTbIpLinkMstVo(tbIpLinkMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.insertTbIpLinkMstVo(tbIpLinkMstVo);
	}

	@Transactional(readOnly = true)
	public int countTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.countTbIpLinkMstVo(tbIpLinkMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.updateTbIpLinkMstVo(tbIpLinkMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int upateTbIpAllocMstVo(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.upateTbIpAllocMstVo(tbIpLinkMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo) {
		return tbIpLinkMstDao.deleteTbIpLinkMstVo(tbIpLinkMstVo);
	}

}
