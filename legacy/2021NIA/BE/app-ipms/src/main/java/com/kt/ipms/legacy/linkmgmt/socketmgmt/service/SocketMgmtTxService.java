package com.kt.ipms.legacy.linkmgmt.socketmgmt.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.irms.epp.exception.EPPInvalidException;
import org.irms.epp.xml.EPPAddAction;
import org.irms.epp.xml.EPPIPv4AddObject;
import org.irms.epp.xml.EPPIPv4InfObject;
import org.irms.epp.xml.EPPIPv4ModObject;
import org.irms.epp.xml.EPPIPv4NewObject;
import org.irms.epp.xml.EPPIPv4RtnObject;
import org.irms.epp.xml.EPPInfAction;
import org.irms.epp.xml.EPPModAction;
import org.irms.epp.xml.EPPNewAction;
import org.irms.epp.xml.EPPRtnAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.DateUtils;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.SocketInfoVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.adapter.WhoisMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

@Component
@Transactional
public class SocketMgmtTxService {
	
	@Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;
	
	@Autowired
	private WhoisMgmtAdapterService whoisMgmtAdapterService;
	
	@Autowired
	private SocketFactoryService socketFactoryService;
		
	@Transactional(propagation = Propagation.REQUIRED)
	public Object sendWhoisSocket(SocketInfoVo socketInfoVo) {
		Object resultObject = null;
		Socket clientWhoisSocket = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION START");
			/** Socket Create **/
			clientWhoisSocket = socketFactoryService.createSocket(socketInfoVo);
			
			PrintLogUtil.printLog("WHOIS SOCKET REQUEST START");
			/** Socket Request **/
			objectOutputStream = new ObjectOutputStream(clientWhoisSocket.getOutputStream());
			objectOutputStream.writeObject(socketInfoVo.getRequestUrl());
			objectOutputStream.flush();
			
			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE START");
			/** Socket Response **/
			objectInputStream = new ObjectInputStream(clientWhoisSocket.getInputStream());
			resultObject = objectInputStream.readObject();
			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE SUCCESS");
		} catch (Exception e) {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION FAIL");
			PrintLogUtil.printLog(e.getMessage());
			PrintLogUtil.printError(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
		} finally {
			try {
				if (clientWhoisSocket != null) {
					clientWhoisSocket.close();
				}
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (Exception e) {
				PrintLogUtil.printLog(e.getMessage());
				throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
			}
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION END");
		}
		return resultObject;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisComplexListVo selectListStandByWhois(TbWhoisComplexVo tbWhoisComplexVo) {
		return whoisMgmtAdapterService.selectListStandByWhois(tbWhoisComplexVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectMainIpInfoMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtAdapterService.selectMainIpInfoMst(ipAllocOperMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateResultCd(TbWhoisVo tbWhoisVo) {
		whoisMgmtAdapterService.updateResultCd(tbWhoisVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String getWhoisSubmitRequestXml(TbWhoisComplexVo tbWhoisComplexVo) throws EPPInvalidException, IOException {
		TbWhoisVo tbWhoisVo = tbWhoisComplexVo.getTbWhoisVo();
		TbWhoisUserVo tbWhoisUserVo = tbWhoisComplexVo.getTbWhoisUserVo();
		String requestType = tbWhoisVo.getSrequestCd();
		String netType = tbWhoisVo.getSnettype();
		String requestXml = null;
		if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_NEW)) {
			if (StringUtils.hasText(netType) && netType.equals(CommonCodeUtil.WHOIS_REQUEST_NET_TYPE_INFRA)) {
				PrintLogUtil.printLog("REQUEST TYPE : INFRA");
				EPPIPv4InfObject eppiPv4InfObject = new EPPIPv4InfObject();
				/** IP 영역 **/
				eppiPv4InfObject.setStartIP(tbWhoisVo.getSfirstAddr()); // 시작 주소
				eppiPv4InfObject.setEndIP(tbWhoisVo.getSlastAddr()); // 종료 주소
				
				/** 네트워크 영역 **/
				if (StringUtils.hasText(tbWhoisVo.getSuserorggb()) 
					&& tbWhoisVo.getSuserorggb().equals(CommonCodeUtil.WHOIS_REQUEST_NET_TYPE_INFRA)) {
					eppiPv4InfObject.setNetType(CommonCodeUtil.WHOIS_REQUEST_NET_TYPE_ETC); // 네트워크분류
				} else {
					eppiPv4InfObject.setNetType(tbWhoisVo.getSuserorggb()); // 네트워크분류
				}
				eppiPv4InfObject.setOrgType(CommonCodeUtil.WHOIS_REQUEST_NET_TYPE_INFRA); // 이용기관분류
				eppiPv4InfObject.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스지역
				
				/** 상세 영역 **/
				eppiPv4InfObject.setDtlNowHost(tbWhoisVo.getShostnow()); // 즉시 IP주소 예상소요 규모(/24)
				eppiPv4InfObject.setDtlUseDtl(tbWhoisVo.getSusedtlnow()); // 즉시 IP 사용 내역
				eppiPv4InfObject.setDtl6MonthHost(tbWhoisVo.getShost6month()); // 6개월간 IP주소 예상소요 규모(/24)
				eppiPv4InfObject.setDtl6UseDtl(tbWhoisVo.getSusedtl6month()); // 6개월간 IP 사용 내역
				eppiPv4InfObject.setDtl12MonthHost(tbWhoisVo.getShost12month()); // 12개월간 IP주소 예상소요 규모(/24)
				eppiPv4InfObject.setDtl12UseDtl(tbWhoisVo.getSusedtl12month()); // 12개월간 IP 사용 내역
				
				/** 추가 영역 **/
				eppiPv4InfObject.setComment(tbWhoisVo.getSsvccommnet()); // 추가사항
				
				/** 공통 영역 **/
				eppiPv4InfObject.setPublish(CommonCodeUtil.WHOIS_REQUEST_PUBLISH_PART);
				
				EPPInfAction eppInfAction = new EPPInfAction();
				eppInfAction.setServiceObject(eppiPv4InfObject);
				eppInfAction.setClientTRID(tbWhoisVo.getSwhoisrequestid());
				requestXml = eppInfAction.createXML();
			} else {
				PrintLogUtil.printLog("REQUEST TYPE : NEW");
				EPPIPv4NewObject eppiPv4NewObject = new EPPIPv4NewObject();
				/** IP 영역 **/
				eppiPv4NewObject.setStartIP(tbWhoisVo.getSfirstAddr()); // 시작 주소
				eppiPv4NewObject.setEndIP(tbWhoisVo.getSlastAddr()); // 종료 주소
				
				/** 기관 영역 **/
				eppiPv4NewObject.setOrgKorName(tbWhoisUserVo.getSorgname()); // 한글 기관명
				eppiPv4NewObject.setOrgKorAddr(tbWhoisUserVo.getSaddr()); // 한글 주소
				eppiPv4NewObject.setOrgKorAddrDtl(tbWhoisUserVo.getSaddrDetail()); // 한글 주소 상세
				eppiPv4NewObject.setOrgEngName(tbWhoisUserVo.getSeorgname()); // 영문 기관명
				eppiPv4NewObject.setOrgEngAddr(tbWhoisUserVo.getSeaddr()); // 영문 주소
				eppiPv4NewObject.setOrgEngAddrDtl(tbWhoisUserVo.getSeaddrDetail()); // 염문 주소 상세
				eppiPv4NewObject.setOrgPost(tbWhoisUserVo.getSzipcode()); // 우편번호
				
				/** 네트워크 영역 **/
				eppiPv4NewObject.setNetName(tbWhoisVo.getSnetNm()); // 네트워크명
				eppiPv4NewObject.setNetType(tbWhoisVo.getSnettype()); // 네트워크분류
				eppiPv4NewObject.setSvcType(tbWhoisVo.getSservicegb()); // 인터넷서비스분류
				eppiPv4NewObject.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관분류
				eppiPv4NewObject.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스지역
				
				/** 관리자(담당자) 영역 **/
				eppiPv4NewObject.setAdminCKorName(tbWhoisUserVo.getSadmName()); // 담당자 한글이름
				eppiPv4NewObject.setAdminCKorOrgName(tbWhoisUserVo.getSadmOrgname()); // 담당자 한글기관명
				eppiPv4NewObject.setAdminCKorAddr(tbWhoisUserVo.getSadmAddr()); // 담당자 한글 주소
				eppiPv4NewObject.setAdminCKorAddrDtl(tbWhoisUserVo.getSadmAddrDetail()); // 담당자 한글 주소 상세
				eppiPv4NewObject.setAdminCEngName(tbWhoisUserVo.getSadmEname()); // 담당자 영문이름
				eppiPv4NewObject.setAdminCEngOrgName(tbWhoisUserVo.getSadmEorgname()); // 담당자 영문기관명
				eppiPv4NewObject.setAdminCEngAddr(tbWhoisUserVo.getSadmEaddr()); // 담당자 영문 주소
				eppiPv4NewObject.setAdminCEngAddrDtl(tbWhoisUserVo.getSadmEaddrDetail()); // 담당자 영문 주소 상세
				eppiPv4NewObject.setAdminCPost(tbWhoisUserVo.getSadmZipcode()); // 담당자 우편번호
				eppiPv4NewObject.setAdminCPhone(tbWhoisUserVo.getSadmPhone()); // 담당자 전화번호
				eppiPv4NewObject.setAdminCDPPhone(tbWhoisUserVo.getSadmDpphone()); // 담당자 대표번호
				eppiPv4NewObject.setAdminCEmail(tbWhoisUserVo.getSadmEmail()); // 담당자 이메일
				
				/** 상세 영역 **/
				eppiPv4NewObject.setDtlNowHost(tbWhoisVo.getShostnow()); // 즉시 IP주소 예상소요 규모(/24)
				eppiPv4NewObject.setDtlUseDtl(tbWhoisVo.getSusedtlnow()); // 즉시 IP 사용 내역
				eppiPv4NewObject.setDtl6MonthHost(tbWhoisVo.getShost6month()); // 6개월간 IP주소 예상소요 규모(/24)
				eppiPv4NewObject.setDtl6UseDtl(tbWhoisVo.getSusedtl6month()); // 6개월간 IP 사용 내역
				eppiPv4NewObject.setDtl12MonthHost(tbWhoisVo.getShost12month()); // 12개월간 IP주소 예상소요 규모(/24)
				eppiPv4NewObject.setDtl12UseDtl(tbWhoisVo.getSusedtl12month()); // 12개월간 IP 사용 내역
				
				/** 추가 영역 **/
				eppiPv4NewObject.setComment(tbWhoisVo.getSsvccommnet()); // 추가사항
				
				/** 공통 영역 **/
				eppiPv4NewObject.setPublish(CommonCodeUtil.WHOIS_REQUEST_PUBLISH_PART);
				
				EPPNewAction eppNewAction = new EPPNewAction();
				eppNewAction.setServiceObject(eppiPv4NewObject);
				eppNewAction.setClientTRID(tbWhoisVo.getSwhoisrequestid());
				requestXml = eppNewAction.createXML();	
			}
			
		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_ADD)) {
			PrintLogUtil.printLog("REQUEST TYPE : ADD");
			EPPIPv4AddObject eppiPv4AddObject = new EPPIPv4AddObject();
			/** IP 영역 **/
			eppiPv4AddObject.setStartIP(tbWhoisVo.getSfirstAddr()); // 시작 주소
			eppiPv4AddObject.setEndIP(tbWhoisVo.getSlastAddr()); // 종료 주소
			
			/** 네트워크 영역 **/
			eppiPv4AddObject.setNetName(tbWhoisVo.getSnetNm()); // 네트워크명
			eppiPv4AddObject.setNetType(tbWhoisVo.getSnettype()); // 네트워크분류
			eppiPv4AddObject.setSvcType(tbWhoisVo.getSservicegb()); // 인터넷서비스분류
			eppiPv4AddObject.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관분류
			eppiPv4AddObject.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스지역
			
			/** 상세 영역 **/
			eppiPv4AddObject.setDtlNowHost(tbWhoisVo.getShostnow()); // 즉시 IP주소 예상소요 규모(/24)
			eppiPv4AddObject.setDtlUseDtl(tbWhoisVo.getSusedtlnow()); // 즉시 IP 사용 내역
			eppiPv4AddObject.setDtl6MonthHost(tbWhoisVo.getShost6month()); // 6개월간 IP주소 예상소요 규모(/24)
			eppiPv4AddObject.setDtl6UseDtl(tbWhoisVo.getSusedtl6month()); // 6개월간 IP 사용 내역
			eppiPv4AddObject.setDtl12MonthHost(tbWhoisVo.getShost12month()); // 12개월간 IP주소 예상소요 규모(/24)
			eppiPv4AddObject.setDtl12UseDtl(tbWhoisVo.getSusedtl12month()); // 12개월간 IP 사용 내역
			
			/** 추가 영역 **/
			eppiPv4AddObject.setComment(tbWhoisVo.getSsvccommnet()); // 추가사항
			
			/** 공통 영역 **/
			eppiPv4AddObject.setPublish(CommonCodeUtil.WHOIS_REQUEST_PUBLISH_PART);
			
			EPPAddAction eppAddAction = new EPPAddAction();
			eppAddAction.setServiceObject(eppiPv4AddObject);
			eppAddAction.setClientTRID(tbWhoisVo.getSwhoisrequestid());
			requestXml = eppAddAction.createXML();
		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_MOD)) {
			PrintLogUtil.printLog("REQUEST TYPE : MOD");
			EPPIPv4ModObject eppiPv4ModObject = new EPPIPv4ModObject();
			/** IP 영역 **/
			eppiPv4ModObject.setStartIP(tbWhoisVo.getSfirstAddr()); // 시작 주소
			eppiPv4ModObject.setEndIP(tbWhoisVo.getSlastAddr()); // 종료 주소
			
			/** 기관 영역 **/
			eppiPv4ModObject.setOrgKorName(tbWhoisUserVo.getSorgname()); // 한글 기관명
			eppiPv4ModObject.setOrgKorAddr(tbWhoisUserVo.getSaddr()); // 한글 주소
			eppiPv4ModObject.setOrgKorAddrDtl(tbWhoisUserVo.getSaddrDetail()); // 한글 주소 상세
			eppiPv4ModObject.setOrgEngName(tbWhoisUserVo.getSeorgname()); // 영문 기관명
			eppiPv4ModObject.setOrgEngAddr(tbWhoisUserVo.getSeaddr()); // 영문 주소
			eppiPv4ModObject.setOrgEngAddrDtl(tbWhoisUserVo.getSeaddrDetail()); // 염문 주소 상세
			eppiPv4ModObject.setOrgPost(tbWhoisUserVo.getSzipcode()); // 우편번호
			
			/** 네트워크 영역 **/
			eppiPv4ModObject.setNetName(tbWhoisVo.getSnetNm()); // 네트워크명
			eppiPv4ModObject.setNetType(tbWhoisVo.getSnettype()); // 네트워크분류
			eppiPv4ModObject.setSvcType(tbWhoisVo.getSservicegb()); // 인터넷서비스분류
			eppiPv4ModObject.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관분류
			eppiPv4ModObject.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스지역
			
			/** 관리자(담당자) 영역 **/
			eppiPv4ModObject.setAdminCKorName(tbWhoisUserVo.getSadmName()); // 담당자 한글이름
			eppiPv4ModObject.setAdminCKorOrgName(tbWhoisUserVo.getSadmOrgname()); // 담당자 한글기관명
			eppiPv4ModObject.setAdminCKorAddr(tbWhoisUserVo.getSadmAddr()); // 담당자 한글 주소
			eppiPv4ModObject.setAdminCKorAddrDtl(tbWhoisUserVo.getSadmAddrDetail()); // 담당자 한글 주소 상세
			eppiPv4ModObject.setAdminCEngName(tbWhoisUserVo.getSadmEname()); // 담당자 영문이름
			eppiPv4ModObject.setAdminCEngOrgName(tbWhoisUserVo.getSadmEorgname()); // 담당자 영문기관명
			eppiPv4ModObject.setAdminCEngAddr(tbWhoisUserVo.getSadmEaddr()); // 담당자 영문 주소
			eppiPv4ModObject.setAdminCEngAddrDtl(tbWhoisUserVo.getSadmEaddrDetail()); // 담당자 영문 주소 상세
			eppiPv4ModObject.setAdminCPost(tbWhoisUserVo.getSadmZipcode()); // 담당자 우편번호
			eppiPv4ModObject.setAdminCPhone(tbWhoisUserVo.getSadmPhone()); // 담당자 전화번호
			eppiPv4ModObject.setAdminCDPPhone(tbWhoisUserVo.getSadmDpphone()); // 담당자 대표번호
			eppiPv4ModObject.setAdminCEmail(tbWhoisUserVo.getSadmEmail()); // 담당자 이메일
			
			/** 추가 영역 **/
			eppiPv4ModObject.setComment(tbWhoisVo.getSsvccommnet()); // 추가사항
			
			/** 공통 영역 **/
			eppiPv4ModObject.setPublish(CommonCodeUtil.WHOIS_REQUEST_PUBLISH_PART);
			
			EPPModAction eppModAction = new EPPModAction();
			eppModAction.setServiceObject(eppiPv4ModObject);
			eppModAction.setClientTRID(tbWhoisVo.getSwhoisrequestid());
			requestXml = eppModAction.createXML();
		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN)) {
			PrintLogUtil.printLog("REQUEST TYPE : RTN");
			EPPIPv4RtnObject eppiPv4RtnObject = new EPPIPv4RtnObject();
			/** IP 영역 **/
			eppiPv4RtnObject.setStartIP(tbWhoisVo.getSfirstAddr()); // 시작 주소
			eppiPv4RtnObject.setEndIP(tbWhoisVo.getSlastAddr()); // 종료 주소
			
			/** 반납 영역 **/
			eppiPv4RtnObject.setRtnDate(DateUtils.parseDate(tbWhoisVo.getDmodifyDt(), "yyyyMMdd"));
			eppiPv4RtnObject.setRtnReason(CommonCodeUtil.WHOIS_REQUEST_RTN_MSG);
			
			/** 추가 영역 **/
			eppiPv4RtnObject.setComment(tbWhoisVo.getSsvccommnet()); // 추가사항
			
			EPPRtnAction eppRtnAction = new EPPRtnAction();
			eppRtnAction.setServiceObject(eppiPv4RtnObject);
			eppRtnAction.setClientTRID(tbWhoisVo.getSwhoisrequestid());
			requestXml = eppRtnAction.createXML();
		}
		PrintLogUtil.printLog("REQUEST XML");
		PrintLogUtil.printLog(requestXml);

		return requestXml;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Object sendTacsSocket(SocketInfoVo socketInfoVo) {
		Object resultObject = null;
		Socket clientWhoisSocket = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			PrintLogUtil.printLog("TACS SOCKET CONNECTION START");
			
			/** Socket Create **/
			clientWhoisSocket = socketFactoryService.createSocket(socketInfoVo);
			PrintLogUtil.printLog("TACS SOCKET REQUEST START");
			/** Socket Request **/
			objectOutputStream = new ObjectOutputStream(clientWhoisSocket.getOutputStream());
			objectOutputStream.writeObject(socketInfoVo.getRequestUrl());
			objectOutputStream.flush();
			
			PrintLogUtil.printLog("TACS SOCKET RESPONSE START");
			/** Socket Response **/
			objectInputStream = new ObjectInputStream(clientWhoisSocket.getInputStream());
			resultObject = objectInputStream.readObject();
			PrintLogUtil.printLog("TACS SOCKET RESPONSE SUCCESS");
		} catch (Exception e) {
			PrintLogUtil.printLog("TACS SOCKET CONNECTION FAIL");
			PrintLogUtil.printLog(e.getMessage());
			PrintLogUtil.printError(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
		} finally {
			try {
				if (clientWhoisSocket != null) {
					clientWhoisSocket.close();
				}
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (Exception e) {
				PrintLogUtil.printLog(e.getMessage());
				throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
			}
			PrintLogUtil.printLog("TACS SOCKET CONNECTION END");
		}
		return resultObject;
	}

}
