package com.kt.ipms.legacy.ipmgmt.linemgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubComplexVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubListVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;

@Component
@Transactional
public class LineMgmtService {
	
	@Autowired
	private LineMgmtTxService lineMgmtTxService;
	
	@Autowired
	private IpCommonService ipCommonService;

	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;
	
	/*할당 회선 등록*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertIPAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		
		if (tbIpAssignSubVos == null || tbIpAssignSubVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP선번장블록"});
		}else{
			try{
				lineMgmtTxService.processInsertIPAssignSub(tbIpAssignSubVos);
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new ServiceException("CMN.HIGH.00020", new String[]{"IP선번장블록"});
			}
		}
	}
	
	/*할당 회선 삭제*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processDeleteIPAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		
		if (tbIpAssignSubVos == null || tbIpAssignSubVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"IP선번장블록"});
		}else{
			try{
					lineMgmtTxService.processDeleteIPAssignSub(tbIpAssignSubVos);
				} catch (ServiceException e) {
					throw e;
				} catch (Exception e) {
					throw new ServiceException("CMN.HIGH.00022", new String[]{"IP선번장블록"});
				}
		}
	}
	
	/**
	 * 지역선번장 목록 조회
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAssignSubListVo selectListIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		TbIpAssignSubListVo resultListVo = null;
		
		try {
			if (tbIpAssignSubVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignSubVo> resultList = lineMgmtTxService.selectListPageIpAssignSub(tbIpAssignSubVo);
			int totalCount = lineMgmtTxService.countListPageIpAssignSub(tbIpAssignSubVo);
			resultListVo = new TbIpAssignSubListVo();
			resultListVo.setTbIpAssignSubVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"지역 선번장목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignSubListVo selectListIpAssignSubExcel(TbIpAssignSubVo tbIpAssignSubVo){
		TbIpAssignSubListVo resultListVo = null;
		
		try {
			if (tbIpAssignSubVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			int totalCount = lineMgmtTxService.countListPageIpAssignSub(tbIpAssignSubVo);			
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpAssignSubVo> resultList = null;
			
			if(totalCount > 0) {
				tbIpAssignSubVo.setFirstIndex(1);
				tbIpAssignSubVo.setLastIndex(totalCount);
				resultList = lineMgmtTxService.selectListPageIpAssignSub(tbIpAssignSubVo);
			}
			
			resultListVo = new TbIpAssignSubListVo();
			resultListVo.setTbIpAssignSubVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"지역 선번장목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 지역선번자 상세 정보 조회
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAssignSubVo selectIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		TbIpAssignSubVo resultVo = null;
		
		try {
			if (tbIpAssignSubVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = lineMgmtTxService.selectIpAssignSub(tbIpAssignSubVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"지역 선번장상세정보"});
		}
		return resultVo;
	}
	
	/**
	 * 지역선번장 IP블록 분할 추가
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public TbIpAssignSubListVo appendDivIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		TbIpAssignSubListVo resultListVo = null;
		try {
			List<TbIpAssignSubVo> resultList = (List<TbIpAssignSubVo>) ipCommonService.getSubnetIpBlockMstInfo(tbIpAssignSubVo);
			resultListVo = new TbIpAssignSubListVo();
			resultListVo.setTbIpAssignSubVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00026");
		}
		return resultListVo;
	}
	
	/**
	 * 지역선번장 IP블록 병합 추가
	 * @param tbIpAssignSubListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAssignSubVo appendMergeDivIpAssignSub(TbIpAssignSubListVo tbIpAssignSubListVo){
		TbIpAssignSubVo resultVo = null;
		try {
			if (tbIpAssignSubListVo == null || tbIpAssignSubListVo.getTbIpAssignSubVos() == null || tbIpAssignSubListVo.getTbIpAssignSubVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignSubVo> paramList = tbIpAssignSubListVo.getTbIpAssignSubVos();
			for (TbIpAssignSubVo tbIpAssignSubVo : paramList) {
				ipCommonService.setBaseIpBlockMstInfo(tbIpAssignSubVo);
			}
			
			boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(paramList);
			if (isMergeSuccess) {
				resultVo = paramList.get(0);
			} else {
				throw new ServiceException("APP.INFO.00028");
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00027");
		}
		return resultVo;
	}
	
	/**
	 * 지역선번장 IP블록 분할 확정
	 * @param tbIpAssignSubComplexVo
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListDivAsgnIPSub(TbIpAssignSubComplexVo tbIpAssignSubComplexVo){
		try {
			if (tbIpAssignSubComplexVo == null || tbIpAssignSubComplexVo.getSrcIpAssignSubVo() == null 
				|| tbIpAssignSubComplexVo.getDestIpAssignSubVos() == null || tbIpAssignSubComplexVo.getDestIpAssignSubVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			/** 기준 정보 DB 재조회 **/
			TbIpAssignSubVo paramVo = tbIpAssignSubComplexVo.getSrcIpAssignSubVo();
			TbIpAssignSubVo searchVo = new TbIpAssignSubVo();
			searchVo.setNipAssignSubSeq(paramVo.getNipAssignSubSeq());
			
			TbIpAssignSubVo orgVo = lineMgmtTxService.selectIpAssignSub(searchVo);
			if (orgVo == null) {
				throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
			}
			//tbIpAssignSubComplexVo.setSrcIpAssignSubVo(orgVo);
			List<TbIpAssignSubVo> destIpAssignSubVos = tbIpAssignSubComplexVo.getDestIpAssignSubVos();
			for (TbIpAssignSubVo tbIpAssignSubVo : destIpAssignSubVos) {
				/** IP블럭 기준 정보 설정 **/
				ipCommonService.setBaseIpBlockMstInfo(tbIpAssignSubVo);
				
				/** IP 할당 정보 설정 **/
				tbIpAssignSubVo.setSsvcLineTypeCd(orgVo.getSsvcLineTypeCd());
				tbIpAssignSubVo.setNlvlMstSeq(orgVo.getNlvlMstSeq());
				tbIpAssignSubVo.setNipAssignMstSeq(orgVo.getNipAssignMstSeq());
				tbIpAssignSubVo.setNipAllocMstSeq(orgVo.getNipAllocMstSeq());
				tbIpAssignSubVo.setNipAssignSubNm(orgVo.getNipAssignSubNm());
				tbIpAssignSubVo.setSassignTypeCd(orgVo.getSassignTypeCd());
				tbIpAssignSubVo.setNipmsSvcSeq(orgVo.getNipmsSvcSeq());
			}
			lineMgmtTxService.processInsertDivAsgnIPSub(tbIpAssignSubComplexVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 분할 확정"});
		}
	}
	
	/**
	 * 지역선번장 IP블록 병합 유효성 체크
	 * @param tbIpAssignSubListVo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public TbIpAssignSubComplexVo validateMrgAsgnIPSub(TbIpAssignSubListVo tbIpAssignSubListVo){
		TbIpAssignSubComplexVo resultComplexVo = null;
		try {
			List<TbIpAssignSubVo> destIpAssignSubVos = lineMgmtTxService.selectListAsgnIPSubViaInSubSeq(tbIpAssignSubListVo);
			if (destIpAssignSubVos != null && destIpAssignSubVos.size() > 0) {
				List<TbIpAssignSubVo> dummyIpAssignSubVos = (ArrayList<TbIpAssignSubVo>)((ArrayList<TbIpAssignSubVo>) destIpAssignSubVos).clone();
				boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(dummyIpAssignSubVos);
				if (isMergeSuccess) {
					TbIpAssignSubVo srcIpAssignSubVo = dummyIpAssignSubVos.get(0);
					TbIpAssignSubVo destIpAssignSubVo = new TbIpAssignSubVo();
					CloneUtil.copyObjectInformation(destIpAssignSubVos.get(0), destIpAssignSubVo);
					String sipVersionTypeNm = destIpAssignSubVo.getSipVersionTypeNm();
					CloneUtil.copyIpVoInfomation(srcIpAssignSubVo, destIpAssignSubVo);
					srcIpAssignSubVo = destIpAssignSubVo;
					srcIpAssignSubVo.setSipVersionTypeNm(sipVersionTypeNm);
					resultComplexVo = new TbIpAssignSubComplexVo();
					resultComplexVo.setSrcIpAssignSubVo(srcIpAssignSubVo);
					resultComplexVo.setDestIpAssignSubVos(destIpAssignSubVos);
				} else {
					throw new ServiceException("APP.INFO.00028");
				}
			} else {
				throw new ServiceException("CMN.HIGH.00001");	
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00027");
		}
		return resultComplexVo;
	}
	
	/**
	 * 지역선번장 병합 확정
	 * @param tbIpAssignSubComplexVo
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertMrgAsgnIPSub(TbIpAssignSubComplexVo tbIpAssignSubComplexVo){
		try {
			if (tbIpAssignSubComplexVo == null || tbIpAssignSubComplexVo.getSrcIpAssignSubVo() == null
				|| tbIpAssignSubComplexVo.getDestIpAssignSubVos() == null || tbIpAssignSubComplexVo.getDestIpAssignSubVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			/** 기준 정보 DB 재조회 **/
			List<TbIpAssignSubVo> destIpAssignSubVos = tbIpAssignSubComplexVo.getDestIpAssignSubVos();
			TbIpAssignSubListVo searchListVo = new TbIpAssignSubListVo();
			searchListVo.setTbIpAssignSubVos(destIpAssignSubVos);
			List<TbIpAssignSubVo> orgVos = lineMgmtTxService.selectListAsgnIPSubViaInSubSeq(searchListVo);
			if (orgVos == null || destIpAssignSubVos.size() != orgVos.size()) {
				throw new ServiceException("APP.HIGH.00029", new String[]{"병합할 IP블록의"});
			}
			
			TbIpAssignSubVo srcIpAssignSubVo = tbIpAssignSubComplexVo.getSrcIpAssignSubVo();
			ipCommonService.setBaseIpBlockMstInfo(srcIpAssignSubVo);
			
			lineMgmtTxService.processInsertMrgAsgnIPSub(tbIpAssignSubComplexVo);
				
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 병합 확정"});
		}
	}
	
	/**
	 * 할당 회선 정보 목록 조회
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(TbIpAssignSubVo tbIpAssignSubVo){
		IpAllocOperMstListVo resultListVo = null;
		try {
			if (tbIpAssignSubVo == null || tbIpAssignSubVo.getNipAllocMstSeq() == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			IpAllocOperMstVo searchVo = new IpAllocOperMstVo();
			//searchVo.setNipAllocMstSeq(tbIpAssignSubVo.getNipAllocMstSeq());
			searchVo.setNipAssignMstSeq(tbIpAssignSubVo.getNipAssignMstSeq());
			resultListVo = lineMgmtTxService.selectListIpAllocDetail(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"할당 기준 회선 정보"});
		}
		return resultListVo;
	}
	
	/**
	 * 지역선번장 정보 수정
	 * @param tbIpAssignSubVo
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAsgnIPSub(TbIpAssignSubVo tbIpAssignSubVo){
		try {
			lineMgmtTxService.updateIpAssignSub(tbIpAssignSubVo);
			
			/* 이력관리 이력 등록 */
			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			
			IpHistoryMstVo searchVo = new IpHistoryMstVo();
			searchVo.setNipAssignMstSeq(tbIpAssignSubVo.getNipAssignMstSeq());
			searchVo.setNipAllocMstSeq(tbIpAssignSubVo.getNipAllocMstSeq());
			ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);
			
			ipHistoryMstVo.setsMenuHistCd("IpSub");
			//ipHistoryMstVo.setSmenuNm("IP 선번장");  
			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MODIFY);  // 수정
			ipHistoryMstVo.setScreateId(tbIpAssignSubVo.getScreateId());
			ipHistoryMstVo.setScomment(tbIpAssignSubVo.getScomment());
			
			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"지역선번장 정보"});
		}
	}
	
	/**
	 * 수용국 정보 조회
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAssignSubListVo selectOfficeList(TbIpAssignSubVo tbIpAssignSubVo){
		TbIpAssignSubListVo resultListVo = null;
		
		try {
			if (tbIpAssignSubVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignSubVo> resultList = lineMgmtTxService.selectOfficeList(tbIpAssignSubVo);
			resultListVo = new TbIpAssignSubListVo();
			resultListVo.setTbIpAssignSubVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 수용국 정보 조회(초기)
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpAssignSubVo tbIpAssignSubVo){
		List<CommonCodeVo> resultListVo = null;
		
		try {
			if (tbIpAssignSubVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = lineMgmtTxService.selectLoadOfficeList(tbIpAssignSubVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/** 조직별 서비스 유형 셋팅(2014.12.19) **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = lineMgmtTxService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultVo;
	}

}
