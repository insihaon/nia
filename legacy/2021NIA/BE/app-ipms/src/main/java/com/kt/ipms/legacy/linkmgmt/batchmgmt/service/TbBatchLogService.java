package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogListVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;

@Component
@Transactional
public class TbBatchLogService {
	
	@Autowired
	private TbBatchLogTxService tbBatchLogTxService;
	
	@Transactional(readOnly = true)
	public TbBatchLogListVo selectListTbBatchLogVo(TbBatchLogVo tbBatchLogVo) throws ServiceException{
		TbBatchLogListVo resultListVo = null;
		if(tbBatchLogVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultListVo = new TbBatchLogListVo();
			List<TbBatchLogVo> tbBatchLogVos = tbBatchLogTxService.selectListPageTbBatchLogVo(tbBatchLogVo);
			int totalCount = tbBatchLogTxService.countListPageTbBatchLogVo(tbBatchLogVo);
			resultListVo.setTbBatchLogVos(tbBatchLogVos);
			resultListVo.setTotalCount(totalCount);
			if (tbBatchLogVos != null && !tbBatchLogVos.isEmpty()) {
				for(int i = 0; i < tbBatchLogVos.size(); i++) {
					if(StringUtils.hasText(tbBatchLogVos.get(i).getSstepStatus())) {
						tbBatchLogVos.get(i).setSstepStatus(tbBatchLogVos.get(i).getSstepStatus().replaceAll("\n", "<br/>"));
					}
				}		
			}
		}catch(ServiceException e) {
			throw e;
		}catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbBatchLogVo selectTbBatchLogVo(TbBatchLogVo tbBatchLogVo) throws ServiceException {
		TbBatchLogVo resultVo = null;
		if(tbBatchLogVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = tbBatchLogTxService.selectDetailTbBatchLogVo(tbBatchLogVo);
			if(resultVo == null){
				throw new ServiceException("CMN.HIGH.00000");
			}
			resultVo.setSstepStatus(resultVo.getSstepStatus().replace("\n", "<br>"));
		}catch(ServiceException e) {
			throw e;
		}catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public TbBatchSvcBasVo selectTbBatchSvcBasByTbBatchLogSeq(TbBatchLogVo tbBatchLogVo) {
		TbBatchSvcBasVo tbBatchSvcBasVo = null;
		if(tbBatchLogVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBatchSvcBasVo = tbBatchLogTxService.selectTbBatchSvcBasByTbBatchLogSeq(tbBatchLogVo);
		}catch(ServiceException e) {
			throw e;
		}catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return tbBatchSvcBasVo;
	}
	
	/****************************************************************************************
	 * 배치 연동 이력현황
	 ****************************************************************************************/
	/**
	 * 배치 연동 이력현황 > 목록 조회
	 * @param tbBatchLogVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbBatchLogListVo selectListBatchHistMst(TbBatchLogVo tbBatchLogVo) {
		TbBatchLogListVo resultListVo = null;
		try {
			if (tbBatchLogVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbBatchLogVo> resultList = tbBatchLogTxService.selectListPageTbBatchHistVo(tbBatchLogVo);
			int totalCount = tbBatchLogTxService.countListTbBatchHistVo(tbBatchLogVo);
			resultListVo = new TbBatchLogListVo();
			resultListVo.setTbBatchLogVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"배치 연동 이력현황"});
		}
		return resultListVo;
	}
}
