package com.kt.ipms.legacy.ipmgmt.allocmgmt.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.dao.TbIpAllocMstDao;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpmsSvcVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.dao.TbIpAssignMstDao;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;

@Component
@Transactional
public class AllocMgmtTxService {

	@Lazy
	@Autowired
	private TbIpAllocMstDao tbIpAllocMstDao;

	@Lazy
	@Autowired
	private TbIpAssignMstDao tbIpAssignMstDao;

	@Lazy
	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectMergeListPageIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectMergeListPageIpAllocMst(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public int countMergeListPageIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countMergeListPageIpAllocMst(ipAllocOperMstVo);
	}
	@Transactional(readOnly = true)
	public int countDuplicateTbIpAllocMstVo(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countDuplicateTbIpAllocMstVo(ipAllocOperMstVo);
	}
	@Transactional(readOnly = true)
	public int countDuplicateTbIpAllocMstVo2(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countDuplicateTbIpAllocMstVo2(ipAllocOperMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<IpAllocOperMstVo> selectListTbIpAssignMstVoContinuityList(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListTbIpAssignMstVoContinuityList(ipAllocOperMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbIpAssignMstVoGroupId(IpAllocOperMstListVo ipAllocOperMstListVo) {
		return tbIpAllocMstDao.updateTbIpAssignMstVoGroupId(ipAllocOperMstListVo);
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListPageIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListPageIpAllocOperMstVo(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListPageIpAllocOperMstVo(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectIpAllocOperMstVo(ipAllocOperMstVo);
	}

	/* IP 할당정보 상세 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListIpAllocDetail(ipAllocOperMstVo);
	}

	/* IP 할당정보 상세 조회(COUNT) */
	@Transactional(readOnly = true)
	public int countListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListIpAllocDetail(ipAllocOperMstVo);
	}

	/* IP 할당정보 회선 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectIpAllocDetail(ipAllocOperMstVo);
	}

	/* IP 할당정보 링크 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectIpAllocDetailLink(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectIpAllocDetailLink(ipAllocOperMstVo);
	}

	/* IP 할당 대상 정보 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListAlcIPMstViaInMstSeq(IpAllocOperMstListVo ipAllocOperMstListVo) {
		return tbIpAllocMstDao.selectListAlcIPMstViaInMstSeq(ipAllocOperMstListVo);
	}

	/* IP 할당 대상 정보 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListAlcIPMstViaInMstSeq2(IpAllocOperMstListVo ipAllocOperMstListVo) {
		return tbIpAllocMstDao.selectListAlcIPMstViaInMstSeq2(ipAllocOperMstListVo);
	}

	/* IP 할당 결과 (회선) 정보 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListSvcMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListSvcMst(ipAllocOperMstVo);
	}

	/* 할당 회선 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertIPAllocMst(List<TbIpAllocMstVo> tbIpAllocMstVos) {

		int insertCnt = 0;
		for (TbIpAllocMstVo tbIpAllocMstVo : tbIpAllocMstVos) {
			// 채번을 통한 SAID 추출(수동-장치할당의 경우)
			if (tbIpAllocMstVo.getSexPushYn().equals("O")) {
				TbIpAllocMstVo saidVo = new TbIpAllocMstVo();
				saidVo = tbIpAllocMstDao.selectSaidSeq(tbIpAllocMstVo);
				tbIpAllocMstVo.setSsaid(saidVo.getSsaid());
			}
			insertCnt += tbIpAllocMstDao.insertTbIpAllocMstVo(tbIpAllocMstVo);

		}
		if (insertCnt != tbIpAllocMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00020", new String[] { "IP블록 할당" });
		}

	}

	/* 할당 회선 삭제 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processDeleteIPAllocMst(List<TbIpAllocMstVo> tbIpAllocMstVos) {

		int deleteCnt = 0;
		for (TbIpAllocMstVo tbIpAllocMstVo : tbIpAllocMstVos) {
			deleteCnt += tbIpAllocMstDao.deleteTbIpAllocMstVo(tbIpAllocMstVo);
		}
		if (deleteCnt != tbIpAllocMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00022", new String[] { "IP블록 할당" });
		}
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListPageNeMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListPageNeMst(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageNeMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListPageNeMst(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListPageTbIpHostMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListPageTbIpHostMst(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageTbIpHostMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListPageTbIpHostMst(ipAllocOperMstVo);
	}

	/* 수용국 정보 조회(시설정보 기반) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectHostSofficeList(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectHostSofficeList(ipAllocOperMstVo);
	}

	/* 메인 화면 Multi IP 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListMainSearchMultiIp(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListMainSearchMultiIp(ipAllocOperMstVo);
	}

	/* 메인 화면 Multi IP 조회 count */
	@Transactional(readOnly = true)
	public int countListMainSearchMultiIp(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListMainSearchMultiIp(ipAllocOperMstVo);
	}

	/* VPN IP현황 조회 */
	@Transactional(readOnly = true)
	public List<TbIpAllocMstVo> selectListPageVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectListPageVpnIPStat(tbIpAllocMstVo);
	}

	/* VPN IP현황 조회 count */
	@Transactional(readOnly = true)
	public int countListPageVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.countListPageVpnIPStat(tbIpAllocMstVo);
	}

	/* 조직별 서비스 유형 목록 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectOrgSassignTypeCdList(tbIpAllocMstVo);
	}

	/* 조직별 서비스 유형 목록 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectOrgSassignTypeCdList2(tbIpAllocMstVo);
	}

	/* 시설용 IP 서비스 유형 목록 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectFacilitesTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectFacilitesTypeCdList(tbIpAllocMstVo);
	}

	/* VPN IP현황 상세 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectVpnIPStatDetail(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectVpnIPStat(tbIpAllocMstVo);
	}

	/* IPMS SVC MST Seq 조회 */
	@Transactional(readOnly = true)
	public BigInteger selectIpmsSvcMstSeq(TbIpAllocMstVo tbIpAllocMstVo) {
		return tbIpAllocMstDao.selectIpmsSvcMstSeq(tbIpAllocMstVo);
	}

	/* IPMS SVC MST Seq 조회 */
	@Transactional(readOnly = true)
	public List<IpmsSvcVo> selectIpmsSvc(IpmsSvcVo ipmsSvcVo) {
		return tbIpAllocMstDao.selectIpmsSvc(ipmsSvcVo);
	}

	/* 수용국 정보 조회(링크정보 기반) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLinkSofficeList(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectLinkSofficeList(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListPageTbIpLinkMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.selectListPageTbIpLinkMst(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageTbIpLinkMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbIpAllocMstDao.countListPageTbIpLinkMst(ipAllocOperMstVo);
	}

	/* 비고변경용 할당 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processScommentUpdateAllocIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		int updateCnt = 0;
		for (TbIpAssignMstVo tbIpAssignMstVo : tbIpAssignMstVos) {
			updateCnt += tbIpAssignMstDao.updateScommentTbIpAssignMstVo(tbIpAssignMstVo);

			/* 이력관리 이력 등록 */
			TbIpAssignMstVo srcTbIpAssignMstVo = new TbIpAssignMstVo();
			TbIpAssignMstVo descTbIpAssignMstVo = new TbIpAssignMstVo();

			srcTbIpAssignMstVo.setNipAssignMstSeq(tbIpAssignMstVo.getNipAssignMstSeq());
			descTbIpAssignMstVo = tbIpAssignMstDao.selectTbIpAssignMstVo(srcTbIpAssignMstVo);

			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			CloneUtil.copyObjectInformation(descTbIpAssignMstVo, ipHistoryMstVo);

			if (!StringUtils.hasText(tbIpAssignMstVo.getMenuType())) {
				ipHistoryMstVo.setsMenuHistCd("Aloc");
				// ipHistoryMstVo.setSmenuNm("IP 할당");
				// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
			} else if (tbIpAssignMstVo.getMenuType().equals("Rout")) {
				ipHistoryMstVo.setsMenuHistCd("Rout");
				// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
				// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
			} else {
				ipHistoryMstVo.setsMenuHistCd("Aloc");
				// ipHistoryMstVo.setSmenuNm("IP 할당");
				// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
			}

			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MODIFY); // 수정
			ipHistoryMstVo.setScreateId(tbIpAssignMstVo.getSmodifyId());
			ipHistoryMstVo.setScomment(tbIpAssignMstVo.getScomment());

			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);

		}
		if (updateCnt != tbIpAssignMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "IP할당블록" });
		}

	}

}
