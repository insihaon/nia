package com.kt.ipms.legacy.linkmgmt.whois.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.service.SocketFactoryService;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.SocketInfoVo;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.dao.WhoisDao;
import com.kt.ipms.legacy.linkmgmt.whois.util.WhoisToXml;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.service.WhoisMgmtTxService;

/****************************************************
 *  WHOIS 연동 관련  비즈니스 Transaction 로직을 처리하는 Interface Class
 * 
 ****************************************************/
@Component @Transactional
public class WhoisTxService {

	@Lazy @Autowired
	private WhoisDao whoisDao;

@Lazy @Autowired
	private WhoisAdapterService whoisAdapterService;

@Lazy @Autowired
	private WhoisMgmtTxService whoisMgmtTxService;

@Lazy @Autowired
	private SocketFactoryService socketFactoryService;

	/**
	 * WHOIS 연동 관련 TB_WHOIS 테이블 정보 조회
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TbWhoisComplexVo> selectListWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisVo vo = tbWhoisComplexVo.getTbWhoisVo();
		return whoisDao.selectListWhoisComplexVo(vo);
	}

	/**
	 * WHOIS 연동 관련 TB_WHOIS 테이블 정보 조회
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TbWhoisComplexVo> selectListWhoisComplexVo2(TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisVo vo = tbWhoisComplexVo.getTbWhoisVo();
		return whoisDao.selectListWhoisComplexVo2(vo);
	}

	/**
	 * Whois 전송 데이터 Xml 형식으로 변환
	 * @param tbWhoisComplexVo
	 * @return
	 * @throws IOException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String getWhoisSubmitRequestXml(TbWhoisComplexVo tbWhoisComplexVo) throws IOException {

		TbWhoisVo tbWhoisVo = tbWhoisComplexVo.getTbWhoisVo();
		com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo tbWhoisUserVo = tbWhoisComplexVo.getTbWhoisUserVo();

		String requestType = tbWhoisVo.getSrequestCd();
		String netType = tbWhoisVo.getSnettype();
		String requestXml = null;
		String clientId = tbWhoisVo.getSwhoisrequestid();

		PrintLogUtil.printLog("WHOIS REQUEST ID = CLIENT ID : " + clientId);
		PrintLogUtil.printLog("WHOIS REQUEST TYPE : " + requestType);

		PrintLogUtil.printLog("tbWhoisVo.getSfirstAddr()" + tbWhoisVo.getSfirstAddr());
		PrintLogUtil.printLog("tbWhoisVo.getSlastAddr()" + tbWhoisVo.getSlastAddr());

		WhoisToXml obj = new WhoisToXml();

		com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo searchVo = new com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo();
		com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo ktInfoVo = new com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo();

		searchVo.setSsaid("00000000000");
		ktInfoVo = whoisMgmtTxService.selectWhoisUser(searchVo);

		/***
		 * # WHOIS 신청서 유형 코드 (requestType)
		 * 01: 신규 (NEW)
		 * 02: 추가/INFRA (ADD)
		 * 03: 변경 (MOD)
		 * 04: 삭제/반환 (RTN)
		 * 06: IP 체크 (재할당정보의 등록가능 여부 확인)
		 * 07: IP 조회 (재할당정보 조회)
		 */

		obj.setReqType(requestType); // WHOIS 신청서 종류 (NEW/ADD/MOD/RTN)
		obj.setClTRID(clientId);
		obj.setStartIp(tbWhoisVo.getSfirstAddr()); // 시작IP	
		obj.setEndIp(tbWhoisVo.getSlastAddr()); // 끝IP

		// kt 정보로 통일
		obj.setOrgPhone(ktInfoVo.getSadmDpphone()); // 기관 전화번호	
		obj.setOrgEmail(ktInfoVo.getSadmEmail()); // 기관 이메일

		if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_NEW)) {

			if (StringUtils.isNotEmpty(netType) && netType.equals(CommonCodeUtil.WHOIS_REQUEST_NET_TYPE_INFRA)) {

				obj.setReqType(CommonCodeUtil.WHOIS_REQUEST_TYPE_ADD);
				obj.setNetName(tbWhoisVo.getSnetNm()); // 네트워크 이름
				obj.setNetType(tbWhoisVo.getSnettype()); // 네트워크  분류
				obj.setSvcType(tbWhoisVo.getSservicegb()); // 서비스 분류
				//obj.setSvcType("DATA_BACKBONE");							// 서비스 분류
				obj.setIpType(tbWhoisVo.getSiptype()); // IP 분류
				obj.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관 분류
				//obj.setOrgType("INFRA");							// 이용기관 분류
				obj.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스 지역
				obj.setComment(tbWhoisVo.getSsvccommnet()); // 추가 사항

			} else {

				obj.setKoOrgName(tbWhoisUserVo.getSorgname()); // 한글 기관명
				obj.setKoOrgAddr(tbWhoisUserVo.getSaddr()); // 한글 주소
				obj.setKoOrgAddrDtl(tbWhoisUserVo.getSaddrDetail()); // 한글 상세주소
				obj.setEnOrgName(tbWhoisUserVo.getSeorgname()); // 영문 기관명
				obj.setEnOrgAddr(tbWhoisUserVo.getSeaddr()); // 영문 주소
				obj.setEnOrgAddrDtl(tbWhoisUserVo.getSeaddrDetail()); // 영문 상세주소

				obj.setOrgPost(tbWhoisUserVo.getSzipcode()); // 기관 우편번호

				obj.setNetName(tbWhoisVo.getSnetNm()); // 네트워크 이름
				obj.setNetType(tbWhoisVo.getSnettype()); // 네트워크  분류
				obj.setSvcType(tbWhoisVo.getSservicegb()); // 서비스 분류
				obj.setIpType(tbWhoisVo.getSiptype()); // IP 분류
				obj.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관 분류
				obj.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스 지역
				obj.setComment(tbWhoisVo.getSsvccommnet()); // 추가 사항
			}

		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_ADD)) {

			obj.setNetName(tbWhoisVo.getSnetNm()); // 네트워크 이름
			obj.setNetType(tbWhoisVo.getSnettype()); // 네트워크  분류
			obj.setSvcType(tbWhoisVo.getSservicegb()); // 서비스 분류
			obj.setIpType(tbWhoisVo.getSiptype()); // IP 분류
			obj.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관 분류
			obj.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스 지역
			obj.setComment(tbWhoisVo.getSsvccommnet()); // 추가 사항

		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_MOD)) {

			obj.setKoOrgName(tbWhoisUserVo.getSorgname()); // 한글 기관명
			obj.setKoOrgAddr(tbWhoisUserVo.getSaddr()); // 한글 주소
			obj.setKoOrgAddrDtl(tbWhoisUserVo.getSaddrDetail()); // 한글 상세주소
			obj.setEnOrgName(tbWhoisUserVo.getSeorgname()); // 영문 기관명
			obj.setEnOrgAddr(tbWhoisUserVo.getSeaddr()); // 영문 주소
			obj.setEnOrgAddrDtl(tbWhoisUserVo.getSeaddrDetail()); // 영문 상세주소
			obj.setOrgPost(tbWhoisUserVo.getSzipcode()); // 기관 우편번호

			obj.setNetName(tbWhoisVo.getSnetNm()); // 네트워크 이름
			obj.setNetType(tbWhoisVo.getSnettype()); // 네트워크  분류
			obj.setSvcType(tbWhoisVo.getSservicegb()); // 서비스 분류
			obj.setIpType(tbWhoisVo.getSiptype()); // IP 분류
			obj.setOrgType(tbWhoisVo.getSuserorggb()); // 이용기관 분류
			obj.setSvcLoc(tbWhoisVo.getSsvcloc()); // 서비스 지역
			obj.setComment(tbWhoisVo.getSsvccommnet()); // 추가 사항
		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN)) {

			obj.setRtnReason(CommonCodeUtil.WHOIS_REQUEST_RTN_MSG);
			obj.setComment(tbWhoisVo.getSsvccommnet()); // 추가 사항
		} else if (requestType.equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_INFO)) {

			obj.setStartIp(tbWhoisVo.getSfirstAddr());
			obj.setEndIp(tbWhoisVo.getSlastAddr());
		}

		requestXml = obj.toXml();

		PrintLogUtil.printLog("REQUEST XML");
		PrintLogUtil.printLog(requestXml);

		return requestXml;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	// public epp_Result[] sendWhoisSocketNoRtn(SocketInfoVo socketInfoVo) {
	public Map<String, Object> sendWhoisSocketNoRtn(SocketInfoVo socketInfoVo) {

		Socket clientWhoisSocket = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		// epp_Result[] result =  null;
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION START");
			/** Socket Create **/
			clientWhoisSocket = socketFactoryService.createSocket(socketInfoVo);

			PrintLogUtil.printLog("WHOIS SOCKET REQUEST START");
			PrintLogUtil.printLog(socketInfoVo.getRequestUrl());
			/** Socket Request **/
			objectOutputStream = new ObjectOutputStream(clientWhoisSocket.getOutputStream());
			objectOutputStream.writeObject(socketInfoVo.getRequestUrl());
			objectOutputStream.flush();

			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE START");

			/** Socket Response **/
			objectInputStream = new ObjectInputStream(clientWhoisSocket.getInputStream());
			result = (Map<String, Object>) objectInputStream.readObject();

			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE SUCCESS");
		} catch (Exception e) {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION FAIL");
			PrintLogUtil.printLog(e.getMessage());
			PrintLogUtil.printError(e);
			//throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
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

				if (clientWhoisSocket == null) {
					result.put("m_code", "01");
				} else if (objectOutputStream == null) {
					result.put("m_code", "02");
				} else if (objectOutputStream == null) {
					result.put("m_code", "05");
				}

			} catch (Exception e) {
				PrintLogUtil.printLog(e.getMessage());
				result.put("m_code", "01");
				//throw new ServiceException("LNK.HIGH.00032", new String[]{"SOCKET 중계 서버와의 "});
			}

			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION END");
		}

		return result;
	}

	/**
	 * Send Socket
	 * @param socketInfoVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	// public epp_Result[] sendWhoisSocket(SocketInfoVo socketInfoVo) {
	public Map<String, Object> sendWhoisSocket(SocketInfoVo socketInfoVo) {

		Socket clientWhoisSocket = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		// epp_Result[] result = null;
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION START");
			/** Socket Create **/
			clientWhoisSocket = socketFactoryService.createSocket(socketInfoVo);

			PrintLogUtil.printLog("WHOIS SOCKET REQUEST START");
			PrintLogUtil.printLog(socketInfoVo.getRequestUrl());
			/** Socket Request **/
			objectOutputStream = new ObjectOutputStream(clientWhoisSocket.getOutputStream());
			objectOutputStream.writeObject(socketInfoVo.getRequestUrl());
			objectOutputStream.flush();

			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE START");

			/** Socket Response **/
			objectInputStream = new ObjectInputStream(clientWhoisSocket.getInputStream());
			// result  = (epp_Result[]) objectInputStream.readObject();
			result = (Map<String, Object>) objectInputStream.readObject();

			PrintLogUtil.printLog("WHOIS SOCKET RESPONSE SUCCESS");
		} catch (Exception e) {
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION FAIL");
			PrintLogUtil.printLog(e.getMessage());
			PrintLogUtil.printError(e);

			//throw new ServiceException("CMN.INFO.00054", new String[]{"SOCKET 중계 서버와의 연동처리 실패하였습니다. 시스템 관리자에게 문의하시기 바랍니다."});
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

				if (clientWhoisSocket == null) {
					result.put("m_code", "01");
				} else if (objectOutputStream == null) {
					result.put("m_code", "02");
				} else if (objectOutputStream == null) {
					result.put("m_code", "05");
				}

			} catch (Exception e) {
				PrintLogUtil.printLog(e.getMessage());
				result.put("m_code", "01");
				//throw new ServiceException("CMN.INFO.00054", new String[]{"SOCKET 중계 서버와의 연동처리 실패하였습니다. 시스템 관리자에게 문의하시기 바랍니다."});
			}
			PrintLogUtil.printLog("WHOIS SOCKET CONNECTION END");
		}
		return result;
	}

	/**
	 * WHOIS 연동 관련 DB Function Call
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String callUfNewWhois(TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisVo vo = tbWhoisComplexVo.getTbWhoisVo();
		return whoisDao.callUfNewWhois(vo);
	}

	/**
	 * TB_WHOIS UPDATE
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateResultCd(TbWhoisVo tbWhoisVo) {
		return whoisDao.updateResultCd(tbWhoisVo);
	}

	/**
	 * TB_WHOIS_USER UPDATE
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo) {
		return whoisDao.updateTbWhoisUserVo(tbWhoisUserVo);
	}

	/**
	 * TB_WHOIS DELETE
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteResultCd(TbWhoisVo tbWhoisVo) {
		return whoisDao.deleteResultCd(tbWhoisVo);
	}

	/**
	 * TB_WHOIS DELETE
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String deleteResultUserCd(TbWhoisVo tbWhoisVo) {
		return whoisDao.deleteResultUserCd(tbWhoisVo);
	}

	/**
	 * Whois Log
	 * @param tbWhoisVo
	 * @return
	 */
	public void insertWhoisLog(TbWhoisVo tbWhoisVo) {
		whoisDao.insertWhoisLog(tbWhoisVo);
	}

	/**
	 * 할당 Seq 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public TbWhoisVo selectAllocMstSeq(TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
		return whoisDao.selectAllocMstSeq(tbIpAllocNeossMstVo);
	}

	/**
	 * Whois Seq 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public TbWhoisVo selectWhoisSeq(TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
		return whoisDao.selectWhoisSeq(tbIpAllocNeossMstVo);
	}

	/**
	 * 할당 정보 조회
	 * @param tbIpAllocMstVo
	 * @return
	 */
	public TbIpAllocMstVo selectAllocMst(TbIpAllocMstVo tbIpAllocMstVo) {
		return whoisDao.selectAllocMst(tbIpAllocMstVo);
	}

	public int selectNipAllocMstCnt(TbIpAllocMstVo tbIpAllocMstVo) {
		return whoisDao.selectNipAllocMstCnt(tbIpAllocMstVo);
	}
}
