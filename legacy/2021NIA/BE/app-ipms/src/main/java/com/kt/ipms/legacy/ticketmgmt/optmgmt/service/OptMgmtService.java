package com.kt.ipms.legacy.ticketmgmt.optmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;

@Component
@Transactional
public class OptMgmtService {

	@Lazy @Autowired
	private OptMgmtTxService optMgmtTxService;
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = optMgmtTxService.selectListOptimizeIpSource(tbIpAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"최적화대상 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultListVo = optMgmtTxService.selectListOptimizeIpTarget(tbIpAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP대체 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultListVo = optMgmtTxService.selectListOptimizeIpRecommend(tbIpAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP대체 추천 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbOptimizationIpMstListVo selectListOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		TbOptimizationIpMstListVo resultListVo = null;
		try {
			if (tbOptimizationIpMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbOptimizationIpMstVo> resultList = optMgmtTxService.selectListOptimizeIpResult(tbOptimizationIpMstVo);
			int totalCount = optMgmtTxService.countListOptimizeIpResult(tbOptimizationIpMstVo);
			resultListVo = new TbOptimizationIpMstListVo();
			resultListVo.setTbOptimizationIpMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"최적화대상 선택 결과목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbOptimizationIpMstListVo selectListPageOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		TbOptimizationIpMstListVo resultListVo = null;
		try {
			if (tbOptimizationIpMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbOptimizationIpMstVo> resultList = optMgmtTxService.selectListPageOptimizeIpResult(tbOptimizationIpMstVo);
			int totalCount = optMgmtTxService.countListPageOptimizeIpResult(tbOptimizationIpMstVo);
			resultListVo = new TbOptimizationIpMstListVo();
			resultListVo.setTbOptimizationIpMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조각 IP 관리 현황 목록"});
		}
		return resultListVo;
	}
	
	/* 조각 IP 관리 현황 목록 Excel */
	@Transactional(readOnly = true)
	public TbOptimizationIpMstListVo selectListPageOptimizeIpResultExcel(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		TbOptimizationIpMstListVo resultListVo = null;
		try {
			if (tbOptimizationIpMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = optMgmtTxService.countListPageOptimizeIpResult(tbOptimizationIpMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbOptimizationIpMstVo> resultList = null;
			if (totalCount > 0) {
				tbOptimizationIpMstVo.setFirstIndex(1);
				tbOptimizationIpMstVo.setLastIndex(totalCount);
				resultList = optMgmtTxService.selectListPageOptimizeIpResult(tbOptimizationIpMstVo);	
			}
			
			resultListVo = new TbOptimizationIpMstListVo();
			resultListVo.setTbOptimizationIpMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조각 IP 관리 현황 목록 Excel"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListOptIPMst(TbOptimizationIpMstListVo tbOptimizationIpMstListVo) {
		try {
			if (tbOptimizationIpMstListVo == null || tbOptimizationIpMstListVo.getTbOptimizationIpMstVos() == null
				|| tbOptimizationIpMstListVo.getTbOptimizationIpMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			optMgmtTxService.processInsertListOptIPMst(tbOptimizationIpMstListVo.getTbOptimizationIpMstVos());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"최적화 대체 결과"});
		}
	}
}
