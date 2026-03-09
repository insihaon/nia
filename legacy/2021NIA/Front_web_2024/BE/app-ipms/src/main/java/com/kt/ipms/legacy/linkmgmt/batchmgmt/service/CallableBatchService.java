package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;

@Component
@Transactional
public class CallableBatchService {
	
@Lazy @Autowired
	private CallableBatchTxService callableBatchTxService;
	
@Lazy @Autowired
	private TbBatchLogTxService tbBatchLogTxService;
	
@Lazy @Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;
	
@Lazy @Autowired
	private CommonService commonService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void callUfBatchWhois() {
		Random random = new Random();
		random.setSeed(Calendar.getInstance().getTimeInMillis());
		TbBatchLogVo insertLogVo = new TbBatchLogVo();
		insertLogVo.setSifId(CommonCodeUtil.BIF_UF_BATCH_WHOIS);
		insertLogVo.setSsysNm("IPMS");
		insertLogVo.setStbNm("TB_WHOIS");
		insertLogVo.setSbatchEndYn("N");
		insertLogVo.setSstepStatus("START");
		insertLogVo.setScomment("WHOIS DATA COLLECTION PROCESSING");
		BigInteger npid = new BigInteger(String.valueOf(random.nextInt(65536)), 10);
		insertLogVo.setNpid(npid);
		try {
			tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			int resultCd = callableBatchTxService.callUfBatchWhois();
			insertLogVo.setSbatchEndYn("Y");
			if (resultCd == 0) {
				insertLogVo.setScomment("WHOIS DATA COLLECTION SUCCESS");
				insertLogVo.setSstepStatus("PROCESS SUCCESS");
			} else {
				insertLogVo.setScomment("WHOIS DATA COLLECTION FAIL");
				insertLogVo.setSstepStatus("PROCESS FAIL");
			}
			tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
		} catch (ServiceException e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("WHOIS DATA COLLECTION ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}

		} catch (Exception e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("WHOIS DATA COLLECTION ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void callUfFileMakeNTbAlloc() {
		Random random = new Random();
		random.setSeed(Calendar.getInstance().getTimeInMillis());
		TbBatchLogVo insertLogVo = new TbBatchLogVo();
		insertLogVo.setSifId(CommonCodeUtil.BIF_UF_FILE_MAKE_N_TB_ALLOC);
		insertLogVo.setSsysNm("IPMS");
		insertLogVo.setStbNm("TB_IP_ALLOC_MST");
		insertLogVo.setSbatchEndYn("N");
		insertLogVo.setSstepStatus("START");
		insertLogVo.setScomment("NMS TRANSFER ALLOC DATA PROCESSING");
		BigInteger npid = new BigInteger(String.valueOf(random.nextInt(65536)), 10);
		insertLogVo.setNpid(npid);
		try {
			tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			int resultCd = callableBatchTxService.callUfFileMakeNTbAlloc();
			insertLogVo.setSbatchEndYn("Y");
			if (resultCd == 0) {
				insertLogVo.setScomment("NMS TRANSFER ALLOC DATA SUCCESS");
				insertLogVo.setSstepStatus("PROCESS SUCCESS");
			} else {
				insertLogVo.setScomment("NMS TRANSFER ALLOC DATA FAIL");
				insertLogVo.setSstepStatus("PROCESS FAIL");
			}
			tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
		} catch (ServiceException e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("NMS TRANSFER ALLOC DATA ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}

		} catch (Exception e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("NMS TRANSFER ALLOC DATA ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void callUfFileMakeNTbOthersAllocData() {
		Random random = new Random();
		random.setSeed(Calendar.getInstance().getTimeInMillis());
		TbBatchLogVo insertLogVo = new TbBatchLogVo();
		insertLogVo.setSifId(CommonCodeUtil.BIF_UF_FILE_MAKE_N_ALLOCDATA);
		insertLogVo.setSsysNm("IPMS");
		insertLogVo.setStbNm("TB_IP_ALLOC_MST");
		insertLogVo.setSbatchEndYn("N");
		insertLogVo.setSstepStatus("START");
		insertLogVo.setScomment("NMS TRANSFER ALLOC DATA PROCESSING");
		BigInteger npid = new BigInteger(String.valueOf(random.nextInt(65536)), 10);
		insertLogVo.setNpid(npid);
		try {
			tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			int resultCd = callableBatchTxService.callUfFileMakeNTbOthersAllocData();
			insertLogVo.setSbatchEndYn("Y");
			if (resultCd == 0) {
				insertLogVo.setScomment("NMS TRANSFER ALLOC DATA SUCCESS");
				insertLogVo.setSstepStatus("PROCESS SUCCESS");
			} else {
				insertLogVo.setScomment("NMS TRANSFER ALLOC DATA FAIL");
				insertLogVo.setSstepStatus("PROCESS FAIL");
			}
			tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
		} catch (ServiceException e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("NMS TRANSFER ALLOC DATA ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}

		} catch (Exception e) {
			PrintLogUtil.printError(e);
			insertLogVo.setScomment("NMS TRANSFER ALLOC DATA ERROR");
			insertLogVo.setSstepStatus("PROCESS ERROR");
			insertLogVo.setSbatchEndYn("Y");
			if (insertLogVo.getNinputSeq() != null) {
				tbBatchLogTxService.updateTbBatchLogNew(insertLogVo);
			} else {
				tbBatchLogTxService.insertTbBatchLogNew(insertLogVo);
			}
		}
	}
	

}
