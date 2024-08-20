package com.kt.ipms.legacy.ipmgmt.createmgmt.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter.AssignMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.dao.TbIpBlockMstDao;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.adapter.TicketMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;
import com.kt.log4kt.utils.StringUtil;

@Component
@Transactional
public class CreateMgmtTxService {
	
	
	@Autowired
	private TbIpBlockMstDao tbIpBlockMstDao;
	
	@Autowired
	private AssignMgmtAdapterService assignMgmtAdapterService;
	
	@Autowired
	private TicketMgmtAdapterService ticketMgmtAdapterService;
	
	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;
	
	@Transactional(readOnly = true)
	public List<TbIpBlockMstVo> selectListPageIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.selectListPageTbIpBlockMstVo(tbIpBlockMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.countListPageTbIpBlockMstVo(tbIpBlockMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpBlockMstVo selectIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.selectTbIpBlockMstVo(tbIpBlockMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countDuplicateTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.countDuplicateTbIpBlockMstVo(tbIpBlockMstVo);
	}
	

	@Transactional(readOnly = true)
	public int countDuplicateTbIpBlockMstVo2(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.countDuplicateTbIpBlockMstVo2(tbIpBlockMstVo);
	}
	
	@Transactional(readOnly = true)
	public String selectNewSipCreateSeqCd(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.selectNewSipCreateSeqCd(tbIpBlockMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		tbIpBlockMstDao.insertTbIpBlockMstVo(tbIpBlockMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo, BigInteger nlvlMstSeq) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		tbIpBlockMstVo.setNlvlMstSeq(nlvlMstSeq);
		tbIpBlockMstDao.insertTbIpBlockMstVo(tbIpBlockMstVo);
		TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
		CloneUtil.copyObjectInformation(tbIpBlockMstVo, tbIpAssignMstVo);
		tbIpAssignMstVo.setScomment(null);
		tbIpAssignMstVo.setNlvlMstSeq(nlvlMstSeq);
//		tbIpAssignMstVo.setNipAllocMstCnt(BigInteger.ZERO); //default값 있음
		
		// 사설IP신청 게시판에서 승인 후 IP블록 생성 시 할당상태 배정(미할당) 상태로 되도록 전필권차장님 요청 (2023.02.27)
		if("PrivateReq".equals(tbIpBlockMstVo.getsMenuType())) {
			tbIpAssignMstVo.setSassignLevelCd("IA0003");		// 배정(미할당)
		} else {
			tbIpAssignMstVo.setSassignLevelCd("IA0001");		// 미배정
		}
		
		tbIpAssignMstVo.setNipmsSvcSeq(BigInteger.ZERO);
		tbIpAssignMstVo.setSassignTypeCd("SA0000");
		tbIpAssignMstVo.setSipAllocExTypeCd("AE0000");
		assignMgmtAdapterService.insertIpAssignMst(tbIpAssignMstVo);
		
		/*Ticket관리 (생성)*/
		TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
		CloneUtil.copyObjectInformation(tbIpAssignMstVo, tbTicketMstVo);
		tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_CREATE_SAVE);
		ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
		
		/* 이력관리 이력 등록 */
		IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
		CloneUtil.copyObjectInformation(tbIpAssignMstVo, ipHistoryMstVo);
		// IP블록관리, 사설IP신청
		//String smenuNm = tbIpBlockMstVo.getSmenuNm() != null ? tbIpBlockMstVo.getSmenuNm() : "IP 블록관리";
		//ipHistoryMstVo.setSmenuNm(smenuNm);
		
		String smenuHistCd = tbIpBlockMstVo.getsMenuHistCd() != null ? tbIpBlockMstVo.getsMenuHistCd() : "Block";
		ipHistoryMstVo.setsMenuHistCd(smenuHistCd);
		
		IpHistoryMstVo insertTbIpBlockMstVo = historyMgmtAdapterService.selectBlockIpInfo(ipHistoryMstVo);
		ipHistoryMstVo.setNpriority(insertTbIpBlockMstVo.getNpriority());
		
		//ipHistoryMstVo.setSmenuId("M00006"); // IP 블록관리
		ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_SAVE);	// 등록
		ipHistoryMstVo.setScomment(tbIpBlockMstVo.getScomment());
		
		if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcGroupCd())) {
			ipHistoryMstVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
		}
		
		if(StringUtil.isNullorBlank(ipHistoryMstVo.getSsvcObjCd())) {
			ipHistoryMstVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
		}
		
		historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countAssignBlockViaIpAssignMstVo(TbIpBlockMstVo tbIpBlockMstVo) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
		CloneUtil.copyObjectInformation(tbIpBlockMstVo, tbIpAssignMstVo);
		return assignMgmtAdapterService.countAssignBlockViaIpAssignMstVo(tbIpAssignMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processDeleteIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
		
		/* Ticket관리 (생성-삭제) */
		TbIpAssignMstListVo delTbIpAssignMstListVo = new TbIpAssignMstListVo();				//삭제 대상 블록
		List<TbIpAssignMstVo> deleteTbIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();	//삭제 대상 블록
		
		tbIpAssignMstVo.setNipBlockMstSeq(tbIpBlockMstVo.getNipBlockMstSeq());
		delTbIpAssignMstListVo = assignMgmtAdapterService.selectListIpAssignMst(tbIpAssignMstVo);
		deleteTbIpAssignMstVos = delTbIpAssignMstListVo.getTbIpAssignMstVos();
		
		for (TbIpAssignMstVo deleteTbIpAssignMstVo : deleteTbIpAssignMstVos) {
			TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
			CloneUtil.copyObjectInformation(deleteTbIpAssignMstVo, tbTicketMstVo);
			tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_CREATE_DELETE);
			tbTicketMstVo.setScreateId(tbIpBlockMstVo.getScreateId());
			ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
			
			/* 이력관리 이력 등록 */
			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			ipHistoryMstVo.setNipBlockMstSeq(deleteTbIpAssignMstVo.getNipBlockMstSeq());
			IpHistoryMstVo deleteTbIpBlockMstVo = historyMgmtAdapterService.selectBlockIpInfo(ipHistoryMstVo);
			
			CloneUtil.copyObjectInformation(deleteTbIpBlockMstVo, ipHistoryMstVo);
			//String smenuNm = tbIpBlockMstVo.getSmenuNm() != null ? tbIpBlockMstVo.getSmenuNm() : "IP 블록관리";
			//ipHistoryMstVo.setSmenuNm(smenuNm);
			
			String sMenuHistCd = tbIpBlockMstVo.getsMenuHistCd() != null ? tbIpBlockMstVo.getsMenuHistCd() : "Block";
			ipHistoryMstVo.setsMenuHistCd(sMenuHistCd);
			
			//ipHistoryMstVo.setSmenuNm("IP 블록관리");
			//ipHistoryMstVo.setSmenuId("M00006"); // IP 블록관리
			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_DELETE);  // 삭제
			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
		}
		
		tbIpBlockMstVo.setScomment(null);
		tbIpBlockMstVo.setScreateId(null);
		CloneUtil.copyObjectInformation(tbIpBlockMstVo, tbIpAssignMstVo);
		assignMgmtAdapterService.deleteListIpAssignMst(tbIpAssignMstVo);
		tbIpBlockMstDao.deleteTbIpBlockMstVo(tbIpBlockMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void processUpdateIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		tbIpBlockMstDao.updateTbIpBlockMstVo(tbIpBlockMstVo);
		TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
		CloneUtil.copyObjectInformation(tbIpBlockMstVo, tbIpAssignMstVo);
		tbIpAssignMstVo.setScomment(null);
		assignMgmtAdapterService.updateSipCreateSeqCdIpAssignMst(tbIpAssignMstVo);
		
		/* Ticket관리 (생성-수정) */
		TbIpAssignMstListVo updateTbIpAssignMstListVo = new TbIpAssignMstListVo();				//수정 대상 블록
		List<TbIpAssignMstVo> updateTbIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();	//수정 대상 블록
		
		tbIpAssignMstVo.setNipBlockMstSeq(tbIpBlockMstVo.getNipBlockMstSeq());
		updateTbIpAssignMstListVo = assignMgmtAdapterService.selectListIpAssignMst(tbIpAssignMstVo);
		updateTbIpAssignMstVos = updateTbIpAssignMstListVo.getTbIpAssignMstVos();
		
		for (TbIpAssignMstVo updateTbIpAssignMstVo : updateTbIpAssignMstVos) {
			TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
			CloneUtil.copyObjectInformation(updateTbIpAssignMstVo, tbTicketMstVo);
			tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_CREATE_MODIFY);
			tbTicketMstVo.setScreateId(tbIpBlockMstVo.getSmodifyId());
			ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
			

			/* 이력관리 이력 등록 */
			IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
			CloneUtil.copyObjectInformation(updateTbIpAssignMstVo, ipHistoryMstVo);
			
			ipHistoryMstVo.setsMenuHistCd("Block");
			//ipHistoryMstVo.setSmenuNm("IP 블록관리");
			//ipHistoryMstVo.setSmenuId("M00006"); // IP 블록관리
			ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_MODIFY);  // 수정
			ipHistoryMstVo.setScreateId(tbIpBlockMstVo.getSmodifyId());
			ipHistoryMstVo.setScomment(tbIpBlockMstVo.getScomment());
			historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
			
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<String> selectListSipCreateSeqCd(String searchSipCreateSeqCd) {
		return tbIpBlockMstDao.selectListSipCreateSeqCd(searchSipCreateSeqCd);
	}
	
	@Transactional(readOnly = true)
	public int checkCountIPBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		return tbIpBlockMstDao.checkCountIPBlockMst(tbIpBlockMstVo);
	}

}
