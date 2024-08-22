package com.kt.ipms.legacy.ipmgmt.hostmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.dao.TbIpHostMstDao;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;

@Component
@Transactional
public class HostMgmtTxService {

	@Lazy @Autowired
	private TbIpHostMstDao tbIpHostMstDao;
	
	@Transactional(readOnly = true)	
	public List<TbIpHostMstVo> selectTbIpHostInfoVo(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectTbIpHostInfoVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbIpHostMstVo> selectListPageIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectListPageTbIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.countListPageTbIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbIpHostMstVo> selectListPageVirtualIpHostMstVo(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectListPageVirtualIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageVirtualIpHostMstVo(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.countListPageVirtualIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstVo selectTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectTbIpHostMstVo(tbIpHostMstVo);
	}
	
	
	
	@Transactional(readOnly = true)
	public List<TbIpHostMstVo> selectListIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectListTbIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processUpdateListIpHostMst(List<TbIpHostMstVo> tbIpHostMstVos){
		int updateCnt = 0;
		for (TbIpHostMstVo tbIpHostMstVo : tbIpHostMstVos) {
			updateCnt += tbIpHostMstDao.updateTbIpHostMstVo(tbIpHostMstVo);
		}
		if (updateCnt != tbIpHostMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"HOST 정보"});
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.insertTbIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.deleteTbIpHostMstVo(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbIpHostMstVo> selectOfficeList(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectOfficeList(tbIpHostMstVo);
	}
	
	/* 수용국 정보 조회(최초) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpHostMstVo tbIpHostMstVo){
		return tbIpHostMstDao.selectLoadOfficeList(tbIpHostMstVo);
	}

}
