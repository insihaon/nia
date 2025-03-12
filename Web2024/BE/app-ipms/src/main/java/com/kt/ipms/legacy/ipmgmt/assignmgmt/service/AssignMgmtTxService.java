package com.kt.ipms.legacy.ipmgmt.assignmgmt.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
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
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.dao.TbIpAssignMstDao;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.service.RoutMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.adapter.TicketMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;
import com.kt.log4kt.utils.StringUtil;

@Component
@Transactional
public class AssignMgmtTxService {

	@Lazy
	@Autowired
	private TbIpAssignMstDao tbIpAssignMstDao;

	@Lazy
	@Autowired
	private TicketMgmtAdapterService ticketMgmtAdapterService;

	@Lazy
	@Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;

	@Lazy
	@Autowired
	private RoutMgmtTxService routMgmtTxService;

	@Lazy
	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListPageIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectListPageTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListPageTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public TbIpAssignMstVo selectIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countAssignBlockViaIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countAssignBlockViaTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		tbIpAssignMstDao.insertTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteListIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		tbIpAssignMstDao.deleteListTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertDivAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
		List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();

		try {
			/** 티켓 <Step 01.>기존블록 정보 조회(분할전 블록 정보 셋팅) **/
			TbIpAssignMstVo srchIpAssignMstVo = new TbIpAssignMstVo();
			List<TbIpAssignMstVo> deleteTbIpAssignMstVos = new ArrayList<TbIpAssignMstVo>(); // 삭제 대상 블록

			StringBuilder resPipPrefix = new StringBuilder();
			int i = 0;

			srchIpAssignMstVo.setNipAssignMstSeq(srcIpAssignMstVo.getNipAssignMstSeq());
			deleteTbIpAssignMstVos = tbIpAssignMstDao.selectListPageTbIpAssignMstVo(srchIpAssignMstVo);

			for (TbIpAssignMstVo deleteTbIpAssignMstVo : deleteTbIpAssignMstVos) {
				if (i < 1) {
					resPipPrefix.append(deleteTbIpAssignMstVo.getPipPrefix());
				} else {
					resPipPrefix.append(deleteTbIpAssignMstVo.getPipPrefix());
					resPipPrefix.append(",");
				}
				i = i + 1;
			}

			/** 기존 블럭 정보 삭제 **/
			tbIpAssignMstDao.deleteTbIpAssignMstVo(srcIpAssignMstVo);

			/** 분할 블럭 정보 등록 **/
			for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
				tbIpAssignMstDao.insertTbIpAssignMstVo(tbIpAssignMstVo);
			}

			/** 티켓 <Step 02.>티켓처리(배정-분할) **/
			for (TbIpAssignMstVo insertTbIpAssignMstVo : destIpAssignMstVos) {
				TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
				CloneUtil.copyObjectInformation(insertTbIpAssignMstVo, tbTicketMstVo);
				tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_DIVIDE);
				tbTicketMstVo.setSactionResultTxt(resPipPrefix.toString());
				ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
			}

			/* 이력관리 이력 등록 */
			for (TbIpAssignMstVo insertTbIpAssignMstVo : destIpAssignMstVos) {
				IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
				// CloneUtil.copyObjectInformation(insertTbIpAssignMstVo, ipHistoryMstVo);

				IpHistoryMstVo searchVo = new IpHistoryMstVo();
				searchVo.setNipAssignMstSeq(insertTbIpAssignMstVo.getNipAssignMstSeq());
				searchVo.setNipAllocMstSeq(insertTbIpAssignMstVo.getNipAllocMstSeq());

				ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);

				if (!StringUtils.hasText(tbIpAssignMstComplexVo.getMenuType())) {
					ipHistoryMstVo.setsMenuHistCd("Aloc");
					// ipHistoryMstVo.setSmenuNm("IP 할당");
					// ipHistoryMstVo.setSmenuId("M00009"); //
				} else if (tbIpAssignMstComplexVo.getMenuType().equals("Asgn")) {
					ipHistoryMstVo.setsMenuHistCd("Asgn");
					// ipHistoryMstVo.setSmenuNm("IP 배정");
					// ipHistoryMstVo.setSmenuId("M00007"); // IP 배정
				} else if (tbIpAssignMstComplexVo.getMenuType().equals("Aloc")) {
					ipHistoryMstVo.setsMenuHistCd("Aloc");
					// ipHistoryMstVo.setSmenuNm("IP 할당");
					// ipHistoryMstVo.setSmenuId("M00007"); // IP 할당
				} else if (tbIpAssignMstComplexVo.getMenuType().equals("Rout")) {
					ipHistoryMstVo.setsMenuHistCd("Rout");
					// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
					// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
				} else if (tbIpAssignMstComplexVo.getMenuType().equals("Upload")) {
					ipHistoryMstVo.setsMenuHistCd("Upload"); // 업로드관리
				} else {
					ipHistoryMstVo.setsMenuHistCd("Aloc");
					// ipHistoryMstVo.setSmenuNm("IP 할당");
					// ipHistoryMstVo.setSmenuId("M00009"); //
				}

				ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_DIVIDE); // 분할
				ipHistoryMstVo.setScomment(insertTbIpAssignMstVo.getScomment());

				if (StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcGroupCd())) {
					ipHistoryMstVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
				}

				if (StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcObjCd())) {
					ipHistoryMstVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
				}

				ipHistoryMstVo.setScreateId(tbIpAssignMstComplexVo.getScreateId());

				historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
			}

		} catch (Exception e) {

			throw new ServiceException("CMN.HIGH.00020", new String[] { "IP블록 분할 확정" });
		}

	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListAsgnIPMstViaInMstSeq(TbIpAssignMstListVo tbIpAssignMstListVo) {
		return tbIpAssignMstDao.selectListTbIpAssignMstVoViaInMstSeq(tbIpAssignMstListVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertMrgAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		int deleteCnt = 0;
		TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
		List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();

		try {
			/** 티켓 <Step 01.>기존블록 정보 조회(병합전 블록 정보 셋팅) **/
			TbIpAssignMstVo srchIpAssignMstVo = new TbIpAssignMstVo();
			TbIpAssignMstVo delTbIpAssignMstVo = new TbIpAssignMstVo(); // 삭제 대상 블록

			StringBuilder resPipPrefix = new StringBuilder();
			int i = 0;

			for (TbIpAssignMstVo deleteTbIpAssignMstVo : destIpAssignMstVos) {

				srchIpAssignMstVo.setNipAssignMstSeq(deleteTbIpAssignMstVo.getNipAssignMstSeq());
				delTbIpAssignMstVo = tbIpAssignMstDao.selectTbIpAssignMstVo(srchIpAssignMstVo);

				if (i < 1) {
					resPipPrefix.append(delTbIpAssignMstVo.getPipPrefix());
				} else {
					resPipPrefix.append(",");
					resPipPrefix.append(delTbIpAssignMstVo.getPipPrefix());
				}
				i = i + 1;
			}

			/** 기준 블럭 정보 삭제 **/
			for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
				deleteCnt += tbIpAssignMstDao.deleteTbIpAssignMstVo(tbIpAssignMstVo);
			}
			if (deleteCnt != destIpAssignMstVos.size()) {
				throw new ServiceException("CMN.HIGH.00020", new String[] { "IP블록 병합 확정" });
			}

			/** 병합 블럭 정보 등록 **/
			tbIpAssignMstDao.insertTbIpAssignMstVo(srcIpAssignMstVo);

			/** 티켓 <Step 01.>티켓처리(배정-병합) **/
			TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
			CloneUtil.copyObjectInformation(srcIpAssignMstVo, tbTicketMstVo);
			tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_MERGE);
			tbTicketMstVo.setSactionResultTxt(resPipPrefix.toString());
			ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);

			/* 이력관리 이력 등록 */
			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			// CloneUtil.copyObjectInformation(srcIpAssignMstVo, ipHistoryMstVo);

			IpHistoryMstVo searchVo = new IpHistoryMstVo();
			searchVo.setNipAssignMstSeq(srcIpAssignMstVo.getNipAssignMstSeq());
			searchVo.setNipAllocMstSeq(srcIpAssignMstVo.getNipAllocMstSeq());
			ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);

			if (!StringUtils.hasText(tbIpAssignMstComplexVo.getMenuType())) {
				ipHistoryMstVo.setsMenuHistCd("Aloc");
				// ipHistoryMstVo.setSmenuNm("IP 할당");
				// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
			} else if (tbIpAssignMstComplexVo.getMenuType().equals("Asgn")) {
				ipHistoryMstVo.setsMenuHistCd("Asgn");
				// ipHistoryMstVo.setSmenuNm("IP 배정");
				// ipHistoryMstVo.setSmenuId("M00007"); // IP 배정
			} else if (tbIpAssignMstComplexVo.getMenuType().equals("Aloc")) {
				ipHistoryMstVo.setsMenuHistCd("Aloc");
				// ipHistoryMstVo.setSmenuNm("IP 할당");
				// ipHistoryMstVo.setSmenuId("M00007"); // IP 할당
			} else if (tbIpAssignMstComplexVo.getMenuType().equals("Rout")) {
				ipHistoryMstVo.setsMenuHistCd("Rout");
				// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
				// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
			} else if (tbIpAssignMstComplexVo.getMenuType().equals("Upload")) {
				ipHistoryMstVo.setsMenuHistCd("Upload"); // 업로드관리
			} else {
				ipHistoryMstVo.setsMenuHistCd("Aloc");
				// ipHistoryMstVo.setSmenuNm("IP 할당");
				// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
			}

			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MERGE); // 병합
			ipHistoryMstVo.setScomment(srcIpAssignMstVo.getScomment());

			if (StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcGroupCd())) {
				ipHistoryMstVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
			}

			if (StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcObjCd())) {
				ipHistoryMstVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
			}

			ipHistoryMstVo.setScreateId(tbIpAssignMstComplexVo.getScreateId());

			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[] { "IP블록 병합확정" });
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void processUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {

		try {
			int updateCnt = 0;
			for (TbIpAssignMstVo tbIpAssignMstVo : tbIpAssignMstVos) {
				updateCnt += tbIpAssignMstDao.updateTbIpAssignMstVo(tbIpAssignMstVo);

				String sFlgUpType = tbIpAssignMstVo.getTypeFlag(); // 배정, 반납 분류
				BigInteger biTicketSeq = CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_ASSIGN;

				if (sFlgUpType.equals("return")) { // 반납
					biTicketSeq = CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_RETURN;
				} else if (sFlgUpType.equals("assign")) { // 배정
					biTicketSeq = CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_ASSIGN;
				} else if (sFlgUpType.equals("svcassign")) { // 서비스 배정
					biTicketSeq = CommonCodeUtil.TICKET_TYPE_IP_ASSIGN_SERVICE;
				}

				/* Ticket관리 (배정- 배정, 반납, 서비스배정) */
				TbIpAssignMstVo srcTbIpAssignMstVo = new TbIpAssignMstVo();
				TbIpAssignMstVo descTbIpAssignMstVo = new TbIpAssignMstVo();

				srcTbIpAssignMstVo.setNipAssignMstSeq(tbIpAssignMstVo.getNipAssignMstSeq());
				descTbIpAssignMstVo = tbIpAssignMstDao.selectTbIpAssignMstVo(srcTbIpAssignMstVo);

				TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
				CloneUtil.copyObjectInformation(descTbIpAssignMstVo, tbTicketMstVo);
				tbTicketMstVo.setNticketTypeSeq(biTicketSeq);
				tbTicketMstVo.setScreateId(tbIpAssignMstVo.getSmodifyId());
				tbTicketMstVo.setScomment(null);
				ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);

				/* 이력관리 이력 등록 (배정- 배정, 반납, 서비스배정) */
				IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
				// CloneUtil.copyObjectInformation(descTbIpAssignMstVo, ipHistoryMstVo);

				IpHistoryMstVo searchVo = new IpHistoryMstVo();
				searchVo.setNipAssignMstSeq(tbIpAssignMstVo.getNipAssignMstSeq());
				searchVo.setNipAllocMstSeq(tbIpAssignMstVo.getNipAllocMstSeq());
				ipHistoryMstVo = historyMgmtAdapterService.selectAllocIpInfo(searchVo);

				BigInteger biHistSeq = CommonCodeUtil.HIST_TASK_ASSIGN; // 배정

				if (sFlgUpType.equals("return")) { // 반납
					biHistSeq = CommonCodeUtil.HIST_TASK_RETURN; // 반납
				} else if (sFlgUpType.equals("assign")) { // 배정
					biHistSeq = CommonCodeUtil.HIST_TASK_ASSIGN; // 배정-배정
				} else if (sFlgUpType.equals("svcassign")) { // 서비스 배정
					biHistSeq = CommonCodeUtil.HIST_TASK_SERVICE; // 서비스배정
				}

				if (!StringUtils.hasText(tbIpAssignMstVo.getMenuType())) {
					ipHistoryMstVo.setsMenuHistCd("Asgn");
					// ipHistoryMstVo.setSmenuNm("IP 배정");
					// ipHistoryMstVo.setSmenuId("M00007"); // IP 배정
				} else if (tbIpAssignMstVo.getMenuType().equals("Rout")) {
					ipHistoryMstVo.setsMenuHistCd("Rout");
					// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
					// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
				} else if (tbIpAssignMstVo.getMenuType().equals("NodeReq")) {
					ipHistoryMstVo.setsMenuHistCd("NodeReq");
					// ipHistoryMstVo.setSmenuNm("IP주소 노드 이전 신청");
				} else if (tbIpAssignMstVo.getMenuType().equals("Aloc")) {
					ipHistoryMstVo.setsMenuHistCd("Aloc");
					// ipHistoryMstVo.setSmenuNm("IP 할당");
					// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
				} else if (tbIpAssignMstVo.getMenuType().equals("Upload")) {
					ipHistoryMstVo.setsMenuHistCd("Upload"); // 업로드관리
				} else {
					ipHistoryMstVo.setsMenuHistCd("Asgn");
					// ipHistoryMstVo.setSmenuNm("IP 배정");
					// ipHistoryMstVo.setSmenuId("M00007"); // IP 배정
				}

				ipHistoryMstVo.setNipHistTaskCd(biHistSeq);
				ipHistoryMstVo.setScreateId(tbIpAssignMstVo.getSmodifyId());

				historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);

			}
			if (updateCnt != tbIpAssignMstVos.size()) {
				throw new ServiceException("CMN.HIGH.00021", new String[] { "IP배정블록" });
			}

			/***************************
			 * 라우팅 점검 업데이트
			 ***************************/
			for (TbIpAssignMstVo tmpVo : tbIpAssignMstVos) {

				// if(tmpVo.getsGubun() == null || tmpVo.getsGubun() == "") {
				// //Codeeyes-Urgent-객체 비교시 == , != 사용제한
				if (tmpVo.getsGubun() == null || tmpVo.getsGubun().equals("")) {
					PrintLogUtil.printLog("#####ASSIGN MST SEQ: " + tmpVo.getNipAssignMstSeq());
					TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
					tbRoutChkMstVo.setNipAssignMstSeq(tmpVo.getNipAssignMstSeq());
					routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);

				}
			}

		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "IP배정블록" });
		}
	}

	/* 할당 처리용 배정 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processAlocUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {

		int updateCnt = 0;
		for (TbIpAssignMstVo tbIpAssignMstVo : tbIpAssignMstVos) {
			updateCnt += tbIpAssignMstDao.updateAlocTbIpAssignMstVo(tbIpAssignMstVo);
		}
		if (updateCnt != tbIpAssignMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "할당 IP배정블록" });
		}
	}

	/* 비고변경용 배정 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processScommentUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos)
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

			ipHistoryMstVo.setsMenuHistCd("Asgn");
			// ipHistoryMstVo.setSmenuNm("IP 배정");
			// ipHistoryMstVo.setSmenuId("M00007"); // IP 배정
			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MODIFY); // 수정
			ipHistoryMstVo.setScreateId(tbIpAssignMstVo.getSmodifyId());
			ipHistoryMstVo.setScomment(tbIpAssignMstVo.getScomment());

			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);

		}
		if (updateCnt != tbIpAssignMstVos.size()) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "IP배정블록" });
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSipCreateSeqCdTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo) {
		tbIpAssignMstDao.updateSipCreateSeqCdTbIpAssignMstVo(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListUnAssignBlock(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectListUnAssignBlock(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countSelectListUnAssignBlock(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countSelectListUnAssignBlock(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListPageOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectListPageOptimizeIpSource(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListPageOptimizeIpSource(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectListOptimizeIpTarget(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListOptimizeIpTarget(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectListOptimizeIpRecommend(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListOptimizeIpRecommend(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int checkCountAsgnIPMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.checkCountAsgnIPMst(tbIpAssignMstVo);
	}

	/** 조직별 서비스 유형 셋팅(2014.12.04) **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtAdapterService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
	}

	/* 시설용 IP 서비스 유형 목록 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectFacilitesTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtAdapterService.selectFacilitesTypeCdList(tbIpAllocMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectSummaryDetailKornet(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectSummaryDetailKornet(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageSummaryDetailKornet(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListPageSummaryDetailKornet(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpAssignMstVo> selectSummaryDetailPremium(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.selectSummaryDetailPremium(tbIpAssignMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageSummaryDetailPremium(TbIpAssignMstVo tbIpAssignMstVo) {
		return tbIpAssignMstDao.countListPageSummaryDetailPremium(tbIpAssignMstVo);
	}
}
