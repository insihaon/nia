package com.kt.ipms.legacy.ticketmgmt.optmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter.AssignMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.dao.TbOptimizationIpMstDao;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;

@Component
@Transactional
public class OptMgmtTxService {

	@Lazy @Autowired
	private TbOptimizationIpMstDao tbOptimizationIpMstDao;
	
@Lazy @Autowired
	private AssignMgmtAdapterService assignMgmtAdapterService;
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtAdapterService.selectListOptimizeIpSource(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtAdapterService.selectListOptimizeIpTarget(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtAdapterService.selectListOptimizeIpRecommend(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbOptimizationIpMstVo> selectListOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		return tbOptimizationIpMstDao.selectListTbOptimizationIpMstVo(tbOptimizationIpMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		return tbOptimizationIpMstDao.countListTbOptimizationIpMstVo(tbOptimizationIpMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbOptimizationIpMstVo> selectListPageOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		return tbOptimizationIpMstDao.selectListPageTbOptimizationIpMstVo(tbOptimizationIpMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		return tbOptimizationIpMstDao.countListPageTbOptimizationIpMstVo(tbOptimizationIpMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertListOptIPMst(List<TbOptimizationIpMstVo> tbOptimizationIpMstVos) {
		int insertCnt = 0;
		for (TbOptimizationIpMstVo tbOptimizationIpMstVo : tbOptimizationIpMstVos) {
			insertCnt += tbOptimizationIpMstDao.insertTbOptimizationIpMstVo(tbOptimizationIpMstVo);
		}
		if (insertCnt != tbOptimizationIpMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"최적화 대체 결과"});
		}
	}
}
