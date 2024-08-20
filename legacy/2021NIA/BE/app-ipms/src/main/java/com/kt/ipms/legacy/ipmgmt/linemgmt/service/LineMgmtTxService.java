package com.kt.ipms.legacy.ipmgmt.linemgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.dao.TbIpAssignSubDao;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubComplexVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubListVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;
import com.kt.log4kt.utils.StringUtil;

@Component
@Transactional
public class LineMgmtTxService {
	
	@Autowired
	private TbIpAssignSubDao tbIpAssignSubDao;
	
	@Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;

	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;
	
	/*할당 회선 등록*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertIPAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		
		int insertCnt = 0;
		for (TbIpAssignSubVo tbIpAssignSubVo : tbIpAssignSubVos) {
			insertCnt += tbIpAssignSubDao.insertTbIpAssignSubVo(tbIpAssignSubVo);
		}
		if (insertCnt != tbIpAssignSubVos.size()) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP선번장블록"});
		}
	}
	
	/*할당 회선 삭제*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processDeleteIPAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		
		//지역선번장의 경우 분할이 발생할수있음으로 DELETE COUNT 체크하지 않음
		/*
		int deleteCnt = 0;
		for (TbIpAssignSubVo tbIpAssignSubVo : tbIpAssignSubVos) {
			deleteCnt += tbIpAssignSubDao.deleteTbIpAssignSubVo(tbIpAssignSubVo);
		}
		if (deleteCnt != tbIpAssignSubVos.size()) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"IP선번장블록"});
		}
		*/
		
		for (TbIpAssignSubVo tbIpAssignSubVo : tbIpAssignSubVos) {
			tbIpAssignSubDao.deleteTbIpAssignSubVo(tbIpAssignSubVo);
		}
	}
	
	@Transactional(readOnly = true)
	public List<TbIpAssignSubVo> selectListPageIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		return tbIpAssignSubDao.selectListPageTbIpAssignSubVo(tbIpAssignSubVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		return tbIpAssignSubDao.countListPageTbIpAssignSubVo(tbIpAssignSubVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignSubVo selectIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		return tbIpAssignSubDao.selectTbIpAssignSubVo(tbIpAssignSubVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertDivAsgnIPSub(TbIpAssignSubComplexVo tbIpAssignSubComplexVo){
		TbIpAssignSubVo srcIpAssignSubVo = tbIpAssignSubComplexVo.getSrcIpAssignSubVo();
		List<TbIpAssignSubVo> destIpAssignSubVos = tbIpAssignSubComplexVo.getDestIpAssignSubVos();
		
		/** 기존 블럭 정보 삭제 **/
		tbIpAssignSubDao.deleteTbIpAssignSubVo(srcIpAssignSubVo);
		
		/** 분할 블럭 정보 등록 **/
		for (TbIpAssignSubVo tbIpAssignSubVo : destIpAssignSubVos) {
			tbIpAssignSubDao.insertTbIpAssignSubVo(tbIpAssignSubVo);
		}
		
		/* 이력관리 이력 등록 */
		for (TbIpAssignSubVo tbIpAssignSubVo : destIpAssignSubVos) {
			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			//CloneUtil.copyObjectInformation(insertTbIpAssignMstVo, ipHistoryMstVo);
			
			IpHistoryMstVo searchVo = new IpHistoryMstVo();
			searchVo.setNipAssignMstSeq(tbIpAssignSubVo.getNipAssignMstSeq());
			searchVo.setNipAllocMstSeq(tbIpAssignSubVo.getNipAllocMstSeq());
			ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);
			
			ipHistoryMstVo.setsMenuHistCd("IpSub");
			//ipHistoryMstVo.setSmenuNm("IP 선번장");
			
			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_DIVIDE);	// 분할
			ipHistoryMstVo.setScomment(tbIpAssignSubVo.getScomment());
			
			if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcGroupCd())) {
				ipHistoryMstVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
			}
			
			if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcObjCd())) {
				ipHistoryMstVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
			}
			
			ipHistoryMstVo.setScreateId(tbIpAssignSubComplexVo.getScreateId());
			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertMrgAsgnIPSub(TbIpAssignSubComplexVo tbIpAssignSubComplexVo){
		int deleteCnt = 0;
		TbIpAssignSubVo srcIpAssignSubVo = tbIpAssignSubComplexVo.getSrcIpAssignSubVo();
		List<TbIpAssignSubVo> destIpAssignSubVos = tbIpAssignSubComplexVo.getDestIpAssignSubVos();
		
		/** 기준 블럭 정보 삭제 **/
		for (TbIpAssignSubVo tbIpAssignSubVo : destIpAssignSubVos) {
			deleteCnt += tbIpAssignSubDao.deleteTbIpAssignSubVo(tbIpAssignSubVo);
		}
		if (deleteCnt != destIpAssignSubVos.size()) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 병합 확정"});
		}
		/** 병합 블럭 정보 등록 **/
		tbIpAssignSubDao.insertTbIpAssignSubVo(srcIpAssignSubVo);
		
		/* 이력관리 이력 등록 */
		IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
		//CloneUtil.copyObjectInformation(insertTbIpAssignMstVo, ipHistoryMstVo);
		
		IpHistoryMstVo searchVo = new IpHistoryMstVo();
		searchVo.setNipAssignMstSeq(srcIpAssignSubVo.getNipAssignMstSeq());
		searchVo.setNipAllocMstSeq(srcIpAssignSubVo.getNipAllocMstSeq());
		ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);
		
		ipHistoryMstVo.setsMenuHistCd("IpSub");
		//ipHistoryMstVo.setSmenuNm("IP 선번장");
		
		ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MERGE);	// 병합
		ipHistoryMstVo.setScomment(srcIpAssignSubVo.getScomment());
		
		if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcGroupCd())) {
			ipHistoryMstVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
		}
		
		if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcObjCd())) {
			ipHistoryMstVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
		}
		
		ipHistoryMstVo.setScreateId(tbIpAssignSubComplexVo.getScreateId());
		historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbIpAssignSubVo> selectListAsgnIPSubViaInSubSeq(TbIpAssignSubListVo tbIpAssignSubListVo){
		return tbIpAssignSubDao.selectListTbIpAssignSubVoViaInSubSeq(tbIpAssignSubListVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo){
		return allocMgmtAdapterService.selectListIpAllocDetail(ipAllocOperMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateIpAssignSub(TbIpAssignSubVo tbIpAssignSubVo){
		tbIpAssignSubDao.updateTbIpAssignSubVo(tbIpAssignSubVo);
	}
	
	/* 수용국 정보 조회 */
	@Transactional(readOnly = true)
	public List<TbIpAssignSubVo> selectOfficeList(TbIpAssignSubVo tbIpAssignSubVo){
		return tbIpAssignSubDao.selectOfficeList(tbIpAssignSubVo);
	}
	
	/* 수용국 정보 조회(최초) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpAssignSubVo tbIpAssignSubVo){
		return tbIpAssignSubDao.selectLoadOfficeList(tbIpAssignSubVo);
	}
	
	/** 조직별 서비스 유형 셋팅(2014.12.19) **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtAdapterService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
	}

}
