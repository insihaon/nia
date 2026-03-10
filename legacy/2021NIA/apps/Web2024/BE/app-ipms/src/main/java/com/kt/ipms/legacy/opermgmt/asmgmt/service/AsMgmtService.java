package com.kt.ipms.legacy.opermgmt.asmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstVo;

@Component
public class AsMgmtService {
	
	@Autowired
	AsMgmtTxService asMgmtTxService;
	
	/* 사설AS 관리 목로 리스트 조회 */
	public TbRequestAsApyTxnListVo viewListPrivateAs(TbRequestAsApyTxnVo searchVo) {
		TbRequestAsApyTxnListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbRequestAsApyTxnVo> resultList = asMgmtTxService.selectListPageTbRequestAsApyTxnVo(searchVo);
			int totalCount = asMgmtTxService.countListTbRequestAsApyTxnVo(searchVo);
			resultListVo = new TbRequestAsApyTxnListVo();
			resultListVo.setTbRequestAsApyTxnVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS정보 내역"});
		}
		
		return resultListVo;
	}
	
	public TbRequestAsApyTxnListVo viewListPrivateAsExcel(TbRequestAsApyTxnVo searchVo) {
		TbRequestAsApyTxnListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = asMgmtTxService.countListTbRequestAsApyTxnVo(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbRequestAsApyTxnVo> resultList = null;
			
			if(totalCount > 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = asMgmtTxService.selectListPageTbRequestAsApyTxnVo(searchVo);
			}
			
			resultListVo = new TbRequestAsApyTxnListVo();
			resultListVo.setTbRequestAsApyTxnVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS정보 내역  엑셀저장"});
		}
		
		return resultListVo;
	}
	
	/* 사설AS 상세조회 */
	public TbRequestAsApyTxnVo viewDetailPrivateAS(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		TbRequestAsApyTxnVo resultVo = null;
		if(tbRequestAsApyTxnVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = asMgmtTxService.selectTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS상세정보"});
		}
		
		return resultVo;
	}
	
	/* 사설AS 등록 */
	public TbRequestAsApyTxnVo insertPrivateAs(TbRequestAsApyTxnVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.insertTbRequestAsApyTxnVo(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설AS"});
		}
		
		return insertVo;
	}
	
	/* 사설 AS 수정 */
	public TbRequestAsApyTxnVo updatePrivateAs(TbRequestAsApyTxnVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.updateTbRequestAsApyTxnVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사설AS"});
		}
		
		return updateVo;
	}
	
	/* 사설 AS 신청취소 */
	public TbRequestAsApyTxnVo deletePrivateAs(TbRequestAsApyTxnVo deleteVo) {
		if(deleteVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.deleteTbRequestAsApyTxnVo(deleteVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"사설AS"});
		}
		
		return deleteVo;
	}
	
	/* 사설AS updateNrequestAsSeqYn */
	public TbRequestAsApyTxnVo updateNrequestAsSeqYn(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		if(tbRequestAsApyTxnVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.updateNrequestAsSeqYn(tbRequestAsApyTxnVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설AS 할당정보"});
		}
		
		return tbRequestAsApyTxnVo;
	}
	/* 사설AS 이력 등록 */
	public TbRequestAsApyTxnVo insertAsHist(TbRequestAsApyTxnVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.insertAsHist(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설AS 이력"});
		}
		
		return insertVo;
	}
	
	/* 사설AS번호 목록 리스트 조회  (사설AS이력 목록)*/
	public TbRequestAsMstListVo viewListAsHist(TbRequestAsMstVo searchVo) {
		TbRequestAsMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbRequestAsMstVo> resultList = asMgmtTxService.selectListPageTbRequestAsMstVo(searchVo);
			int totalCount = asMgmtTxService.countListTbRequestAsMstVo(searchVo);
			int useCount = asMgmtTxService.useCountListTbRequestAsMstVo(searchVo);
			resultListVo = new TbRequestAsMstListVo();
			resultListVo.setTbRequestAsMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			resultListVo.setUseCount(useCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS번호 내역"});
		}
		
		return resultListVo;
	}
	
	public TbRequestAsMstListVo viewListAsHistExcel(TbRequestAsMstVo searchVo) {
		TbRequestAsMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = asMgmtTxService.countListTbRequestAsMstVo(searchVo);
			int useCount = 0;
			List<TbRequestAsMstVo> resultList = null;
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			if(totalCount> 0) {
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = asMgmtTxService.selectListPageTbRequestAsMstVo(searchVo);
				
				useCount = asMgmtTxService.useCountListTbRequestAsMstVo(searchVo);
			}
			
			resultListVo = new TbRequestAsMstListVo();
			resultListVo.setTbRequestAsMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			resultListVo.setUseCount(useCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS번호 내역"});
		}
		
		return resultListVo;
	}
	
	/* 사설AS 이력 상세조회 */
	public TbRequestAsHistListVo viewDetailASHist(TbRequestAsHistVo searchVo) {
		TbRequestAsHistListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbRequestAsHistVo> resultList = asMgmtTxService.selectListPageTbRequestAsHistVo(searchVo);
			int totalCount = asMgmtTxService.countListTbRequestAsHistVo(searchVo);
			resultListVo = new TbRequestAsHistListVo();
			resultListVo.setTbRequestAsHistVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설AS 이력 내역"});
		}
		
		return resultListVo;
	}
	
	
	/* 미사용 AS번호 조회 */
	public TbRequestAsMstVo selectMinNrequestAsSeq(TbRequestAsMstVo tbRequestAsMstVo) {
		TbRequestAsMstVo resultVo = null;
		if(tbRequestAsMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = asMgmtTxService.selectMinNrequestAsSeq(tbRequestAsMstVo);
			//사용가능한 AS번호가 없을때
			if(resultVo == null) {
				throw new ServiceException("APP.INFO.00042");
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"미사용 AS번호"});
		}
		return resultVo;
	}
	
	/* TbRequestAsMst 수정 */
	public TbRequestAsMstVo updateTbRequestAsMst(TbRequestAsMstVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			asMgmtTxService.updateTbRequestAsMstVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사설AS"});
		}
		
		return updateVo;
	}
}
