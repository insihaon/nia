package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.linkmgmt.batchmgmt.dao.TbBatchLogDao;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;

@Component
@Transactional
public class TbBatchLogTxService {
	
	@Autowired
	private TbBatchLogDao tbBatchLogDao;
	
	@Transactional(readOnly = true)
	public List<TbBatchLogVo> selectListTbBatchLog(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.selectListTbBatchLog(tbBatchLogVo);
	}
	
	@Transactional(readOnly = true)
	public TbBatchLogVo selectDetailTbBatchLogVo(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.selectDetailTbBatchLogVo(tbBatchLogVo);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbBatchLog(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.insertTbBatchLog(tbBatchLogVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbBatchLog(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.updateTbBatchLog(tbBatchLogVo);
	}
	
	@Transactional(readOnly = true)
	public TbBatchSvcBasVo selectTbBatchSvcBasByTbBatchLogSeq(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.selectTbBatchSvcBasByTbBatchLogSeq(tbBatchLogVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbBatchLogVo(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.countListPageTbBatchLogVo(tbBatchLogVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbBatchLogVo> selectListPageTbBatchLogVo(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.selectListPageTbBatchLogVo(tbBatchLogVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertTbBatchLogNew(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.insertTbBatchLog(tbBatchLogVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateTbBatchLogNew(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.updateTbBatchLog(tbBatchLogVo);
	}

	/****************************************************************************************
	 * 배치 연동 이력현황
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbBatchLogVo> selectListPageTbBatchHistVo(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.selectListPageTbBatchHistVo(tbBatchLogVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbBatchHistVo(TbBatchLogVo tbBatchLogVo) {
		return tbBatchLogDao.countListTbBatchHistVo(tbBatchLogVo);
	}
	
}
