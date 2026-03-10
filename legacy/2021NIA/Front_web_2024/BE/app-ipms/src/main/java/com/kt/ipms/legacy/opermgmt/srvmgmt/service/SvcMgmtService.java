package com.kt.ipms.legacy.opermgmt.srvmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcLgroupCdVo;

@Component
public class SvcMgmtService {
	
	@Autowired
	SvcMgmtTxService svcMgmtTxService;
	/* 상품 리스트 조회 */
	public TbIpmsSvcMstListVo selectListIpmsSvc(TbIpmsSvcMstVo searchVo)  {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcMstVo> resultList = svcMgmtTxService.selectListPageTbIpmsSvcMstVo(searchVo);
			int totalCount = svcMgmtTxService.countListPageTbIpmsSvcMstVo(searchVo);
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"상품리스트정보"});
		}
		return resultListVo;
	}
	/* 상품 리스트 엑셀 다운 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListIpmsSvcMstExcel(TbIpmsSvcMstVo searchVo) {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			int totalCount = svcMgmtTxService.countListPageTbIpmsSvcMstVo(searchVo);
			List<TbIpmsSvcMstVo> resultList = null;
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			if(totalCount > 0) {
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = svcMgmtTxService.selectListPageTbIpmsSvcMstVo(searchVo);
			}
			
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"상품 관리 리스트 정보 엑셀저장"});
		}
		return resultListVo;
	}
	
	/* 대분류 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm(TbIpmsSvcMstVo searchVo)  {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcMstVo> resultList = svcMgmtTxService.selectListSvcMgroupNmCd(searchVo);
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"대분류"});
		}
		return resultListVo;
	}
	
	/* 소분류 조회  */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm1(TbIpmsSvcMstVo searchVo)  {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcMstVo> resultList = svcMgmtTxService.selectListSvcMgroupNm1Cd(searchVo);
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
 		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"소분류"});
		}
		return resultListVo;
	}
	
	/* 상품정보 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstVo selectTbIpmsSvcMstVo(TbIpmsSvcMstVo searchVo) {
		TbIpmsSvcMstVo resultVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = svcMgmtTxService.selectTbIpmsSvcMstVo(searchVo);
 		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"상품세부정보"});
		}
		return resultVo;
	}
	
	/* 서비스분류 조회 */
	public TbIpmsSvcMstListVo selectListSvcMgroup(TbIpmsSvcMstVo searchVo) {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcMstVo> resultList = svcMgmtTxService.selectListSvcMgroup(searchVo);
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스분류"});
		}
		return resultListVo;
	}
	
	/* 상품명 조회 */
	public TbIpmsSvcMstListVo selectListSvcLgroup(TbIpmsSvcMstVo searchVo) {
		TbIpmsSvcMstListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcMstVo> resultList = svcMgmtTxService.selectListSvcLgroup(searchVo);
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setTbIpmsSvcMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"상품명"});
		}
		return resultListVo;
	}
	
	/* 외부연계 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstVo selectSexLinkUseTypeCd(TbIpmsSvcMstVo searchVo) {
		TbIpmsSvcMstVo resultVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = svcMgmtTxService.selectSexLinkUseTypeCd(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"외부연계"});
		}
		return resultVo;
	}
	/* 상품 유뮤 check */
	@Transactional(readOnly = true) 
	public int countListPageTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		int checkCount;
		if(tbIpmsSvcMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			checkCount = svcMgmtTxService.countListPageTbIpmsSvcMstVo(tbIpmsSvcMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사용 상품 중복 개수"});
		}
		return checkCount;
	}
	
	/* 상품 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpmsSvcMstVo insertTbIpmsSvcMstVo(TbIpmsSvcMstVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			svcMgmtTxService.insertTbIpmsSvcMstVo(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"상품"});
		}
		return insertVo;
	}
	/* 상품 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpmsSvcMstVo updateTbIpmsSvcMstVo(TbIpmsSvcMstVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			svcMgmtTxService.updateTbIpmsSvcMstVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"상품"});
		}
		return updateVo;
	}
	
	/* new LgroupCd 조회 */
	@Transactional(readOnly = true)
	public String selectNewLgroupCd() {
		String newLgoupCd = null;
		try {
			newLgoupCd = svcMgmtTxService.selectNewLgroupCd();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"New LgroupCd"});
		}
		return newLgoupCd;		
	}
	
	/* Lgroup 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbSvcLgroupCdVo insertTbSvcLgroupCdVo(TbSvcLgroupCdVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			//해당상품이 있는지 없는지 check
			svcMgmtTxService.insertTbSvcLgroupCdVo(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"Lgroup"});
		}
		return insertVo;
	}
	
	/* Lgroup 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbSvcLgroupCdVo updateTbSvcLgroupCdVo(TbSvcLgroupCdVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			//해당상품이 있는지 없는지 check
			svcMgmtTxService.updateTbSvcLgroupCdVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"Lgroup"});
		}
		return updateVo;
	}
	
	
	/* 상품 서비스망 리스트 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcSubListVo selectListTbIpmsSvcSubVo(TbIpmsSvcSubVo searchVo) {
		TbIpmsSvcSubListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbIpmsSvcSubVo> resultList = svcMgmtTxService.selectListTbIpmsSvcSubVo(searchVo);
			resultListVo = new TbIpmsSvcSubListVo();
			resultListVo.setTbIpmsSvcSubVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스망 리스트"});
		}
		return resultListVo;
	}
	
	/* 상품 서비스망 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpmsSvcSubVo insertTbIpmsSvcSubVo(TbIpmsSvcSubVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			svcMgmtTxService.insertTbIpmsSvcSubVo(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"서비스망"});
		}
		return insertVo;
	}
	
	/* 상품 서비스망 삭제 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpmsSvcSubVo deleteTbIpmsSvcSubVo(TbIpmsSvcSubVo deleteVo) {
		if(deleteVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}try {
			svcMgmtTxService.deleteTbIpmsSvcSubVo(deleteVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"서비스망"});
		}
		return deleteVo;
	}
	
	
	/* 서비스코드 리스트 조회 */
	@Transactional(readOnly = true)
	public TbAssignTypeCdListVo selectListTbAssignTypeCd(TbAssignTypeCdVo searchVo) {
		TbAssignTypeCdListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbAssignTypeCdVo> resultList = svcMgmtTxService.selectListPageTbAssignTypeCdVo(searchVo);
			int totalCount = svcMgmtTxService.countListTbAssignTypeCdVo(searchVo);
			resultListVo = new TbAssignTypeCdListVo();
			resultListVo.setTbAssignTypeCdVos(resultList);
			resultListVo.setTotalCount(totalCount);
 		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스코드 리스트 정보"});
		}
		return resultListVo;
		
	}
	
	@Transactional(readOnly = true)
	public TbAssignTypeCdListVo selectListTbAssignTypeCdExcel(TbAssignTypeCdVo searchVo) {
		TbAssignTypeCdListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			int totalCount = svcMgmtTxService.countListTbAssignTypeCdVo(searchVo);
			List<TbAssignTypeCdVo> resultList = null;
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			if(totalCount > 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);				
				resultList = svcMgmtTxService.selectListPageTbAssignTypeCdVo(searchVo);
			}
			
			resultListVo = new TbAssignTypeCdListVo();
			resultListVo.setTbAssignTypeCdVos(resultList);
			resultListVo.setTotalCount(totalCount);
 		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스코드 리스트 정보 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/* 서비스코드 조회 */
	@Transactional(readOnly = true)
	public TbAssignTypeCdVo selectTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		TbAssignTypeCdVo resultVo = null;
		if(tbAssignTypeCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = svcMgmtTxService.selectTbAssignTypeCdVo(tbAssignTypeCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스코드"});
		}
		return resultVo;
	}
	
	/* 서비스코드 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbAssignTypeCdVo insertTbAssignTypeCdVo(TbAssignTypeCdVo insertVo) {
		if(insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			svcMgmtTxService.insertTbAssignTypeCdVo(insertVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"서비스코드"});
		}
		return insertVo;
	}
	/* 서비스코드 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbAssignTypeCdVo updateTbAssignTypeCdVo(TbAssignTypeCdVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			svcMgmtTxService.updateTbAssignTypeCdVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"서비스코드"});
		}
		
		return updateVo;
	}
	
}
