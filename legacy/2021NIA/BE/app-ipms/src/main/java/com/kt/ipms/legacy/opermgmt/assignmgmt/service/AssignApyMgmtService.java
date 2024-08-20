package com.kt.ipms.legacy.opermgmt.assignmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstVo;
import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstListVo;

@Component
@Transactional
public class AssignApyMgmtService {

	@Autowired
	AssignApyMgmtTxService assignApyMgmtTxService;
	
	@Autowired
	private CommonService commonService;
	
	@Transactional(readOnly = true)
	public TbRequestAssignMstListVo selectListTbRequestAssignMst (TbRequestAssignMstVo searchVo) {
		TbRequestAssignMstListVo resultListVo = null;
		try {
			
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbRequestAssignMstVo> resultVos = assignApyMgmtTxService.selectListPageTbRequestAssignMstVo(searchVo);
			int totalCnt = assignApyMgmtTxService.countListPageTbRequestAssignMstVo(searchVo);
			
			resultListVo = new TbRequestAssignMstListVo();
			resultListVo.setTbRequestAssignMstVos(resultVos);
			resultListVo.setTotalCount(totalCnt);
			
		}catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 배정신청"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbRequestAssignMstListVo selectListPreApyAssign (TbRequestAssignMstVo searchVo) {
		TbRequestAssignMstListVo resultListVo = null;
		try {
			
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCnt = 0;
			List<TbRequestAssignMstVo> resultVos = assignApyMgmtTxService.selectListPreApyAssign(searchVo);
			if(resultVos != null) {
				totalCnt = resultVos.size();
			}
			
			resultListVo = new TbRequestAssignMstListVo();
			resultListVo.setTbRequestAssignMstVos(resultVos);
			resultListVo.setTotalCount(totalCnt);
			
		}catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 배정신청감사"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbRequestAssignMstListVo selectListTbRequestAssignMstExcel (TbRequestAssignMstVo searchVo) {
		TbRequestAssignMstListVo resultListVo = null;
		try {
			
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCnt = assignApyMgmtTxService.countListPageTbRequestAssignMstVo(searchVo);			
			if (totalCnt > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbRequestAssignMstVo> resultVos = null;
			if(totalCnt > 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCnt);
				resultVos = assignApyMgmtTxService.selectListPageTbRequestAssignMstVo(searchVo);
			}
			
			resultListVo = new TbRequestAssignMstListVo();
			resultListVo.setTbRequestAssignMstVos(resultVos);
			resultListVo.setTotalCount(totalCnt);
			
		}catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 배정신청 엑셀"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbRequestAssignMstVo selectTbRequestAssignMst (TbRequestAssignMstVo searchVo ) {
		TbRequestAssignMstVo resultVo= null;
		try{
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultVo = assignApyMgmtTxService.selectTbRequestAssignMstVo(searchVo);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 배정신청 상세정보"});
		}		
		return resultVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbRequestAssignMstVo insertTbRequestAssignMst(TbRequestAssignMstVo tbRequestAssignMstVo){
		TbRequestAssignMstVo resultVo= null;
		try{
			
			assignApyMgmtTxService.insertTbRequestAssignMstVo(tbRequestAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP 배정신청 등록"});
		}
		
		return resultVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbRequestAssignMstVo updateTbRequestAssignMst(TbRequestAssignMstVo tbRequestAssignMstVo){
		TbRequestAssignMstVo resultVo= null;
		try{
			
			if(StringUtils.hasText(tbRequestAssignMstVo.getSrequestAssignTypeCd()) 
					&& (tbRequestAssignMstVo.getSrequestAssignTypeCd().equals("RS0302") 
					|| tbRequestAssignMstVo.getSrequestAssignTypeCd().equals("RS0303")
					|| tbRequestAssignMstVo.getSrequestAssignTypeCd().equals("RS0304") )){					 
					 Date sysDate = commonService.selectSysDate();
					 tbRequestAssignMstVo.setDtrtDt(sysDate);				 
			}
			assignApyMgmtTxService.updateTbRequestAssignMstVo(tbRequestAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP 배정신청 수정"});
		}
		
		return resultVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbRequestAssignMstVo deleteTbRequestAssignMst(TbRequestAssignMstVo tbRequestAssignMstVo){
		TbRequestAssignMstVo resultVo= null;
		try{
			assignApyMgmtTxService.deleteTbRequestAssignMstVo(tbRequestAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP 배정신청 수정"});
		}
		
		return resultVo;
	}
}
