package com.kt.ipms.legacy.linkmgmt.whois.adapter;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisService;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;


@Component
@Transactional
public class WhoisAdapterService {

	@Lazy @Autowired
	private WhoisService whoisService;

	/**
	 * 할당/반납 후 WHOIS 보내기 위한 정보 가공 -> DB Function Call + Send To KRNIC 
	 * @param nipAllocMstSeq
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendToWhoisWithDb(String sessionId, BigInteger seq, String requestType) {
		
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		tbWhoisVo.setSmodifyId(sessionId);
		tbWhoisVo.setType(requestType);
		
		/**
		 *  requestType = "NEW"  -> alloc_mst_seq (할당 seq)
		 *  requestType = "DEL"  -> whois_seq  (whois seq)
		 */
		tbWhoisVo.setNipAllocMstSeq(seq); 
		
		whoisService.sendToWhoisWithDb(tbWhoisVo);
	}
	
	/**
	 * 할당/반납 후 WHOIS 보내기 위한 정보 가공 -> DB Function Call + Send To KRNIC 
	 * @param nipAllocMstSeq
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendToWhoisWithDbNoRtn(String sessionId, BigInteger seq, String requestType) {
		
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		tbWhoisVo.setSmodifyId(sessionId);
		tbWhoisVo.setType(requestType);
		
		/**
		 *  requestType = "NEW"  -> alloc_mst_seq (할당 seq)
		 *  requestType = "DEL"  -> whois_seq  (whois seq)
		 */
		tbWhoisVo.setNipAllocMstSeq(seq); 
		 
		
		whoisService.sendToWhoisWithDbNoRtn(tbWhoisVo);
	}
	
	/**
	 * Send To KRNIC
	 * @param nipAllocMstSeq
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendToWhois(TbWhoisVo tbWhoisVo) {
		
		whoisService.sendToWhois(tbWhoisVo);
	}
	

	/**
	 * Send To KRNIC (WHOIS 정보 변경)
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbWhoisVo sendToWhoisModify(TbWhoisModifyVo tbWhoisModifyVo) {
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		try {
			tbWhoisVo = whoisService.sendToWhoisModify(tbWhoisModifyVo);
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return tbWhoisVo;
	}
	
	
	/**
	 * WHOIS IP 정보조회
	 * @param fistAddr
	 * @param lastAddr
	 * @param whoisRequestId
	 * @return
	 */
	@Transactional(readOnly = true)
	public WhoisInfoObj whoisIpInfo (String fistAddr, String lastAddr, String whoisRequestId) {
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		WhoisInfoObj obj = new WhoisInfoObj();
		try {

			tbWhoisVo.setSfirstAddr(fistAddr);
			tbWhoisVo.setSlastAddr(lastAddr);
			tbWhoisVo.setSwhoisrequestid(whoisRequestId);

			obj = whoisService.whoisInfo(tbWhoisVo);
			
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return obj;
	}
	
	/**
	 * 할당 Seq 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public TbWhoisVo selectAllocMstSeq (TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
		TbWhoisVo vo = new TbWhoisVo();
		try {
			vo = whoisService.selectAllocMstSeq(tbIpAllocNeossMstVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	/**
	 * Whois Seq 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public TbWhoisVo selectWhoisSeq (TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
		TbWhoisVo vo = new TbWhoisVo();
		try {
			vo = whoisService.selectWhoisSeq(tbIpAllocNeossMstVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	/**
	 * Whois 전송 예외
	 * @param gubun
	 * @param allocMstSeq
	 * @param whoisSeq
	 * @return
	 */
	public boolean exceptWhois(String gubun, BigInteger allocMstSeq, BigInteger whoisSeq) {
	
		boolean rtnValue = true;
		try {
			rtnValue = whoisService.exceptWhois(gubun, allocMstSeq, whoisSeq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnValue;
	}
	
	public int selectNipAllocMstCnt(BigInteger whoisSeq){
		int result = 0;
		try{
			result = whoisService.selectNipAllocMstCnt(whoisSeq);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result; 
	}
}
