package com.kt.ipms.legacy.linkmgmt.whois.service;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.SocketInfoVo;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;
import com.kt.ipms.legacy.linkmgmt.whois.util.WhoisFromXml;
import com.kt.ipms.legacy.linkmgmt.whois.util.WhoisToXml;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.service.WhoisMgmtTxService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.log4kt.utils.StringUtil;



/****************************************************
 *  WHOIS 연동 관련  서비스 로직을 처리하는 Interface Class
 * 
 ****************************************************/
@Component
public class WhoisService {

	@Lazy @Autowired
	private ConfigPropertieService configPropertieService;

	@Lazy @Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;

	@Lazy @Autowired
	private CommonService commonService;

	@Lazy @Autowired
	private WhoisTxService whoisTxService;

	@Lazy @Autowired
	private WhoisMgmtTxService whoisMgmtTxService;

	@Autowired
	private LinkUtil linkUtil;

	/**
	 * WHOIS 연동 관련 WHOIS 작업
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)	
	public void sendToWhois(TbWhoisVo tbWhoisVo) {
		try {
			whoisSubmit(tbWhoisVo);					// 2. WHOIS 작업 (CRUD)
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}
	
	/**
	 * WHOIS 연동 관련 WHOIS 작업 (WHOIS 정보 변경)
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)	
	public TbWhoisVo sendToWhoisModify(TbWhoisModifyVo tbWhoisModifyVo) {
		// int result = 0;
		TbWhoisVo resultVo = new TbWhoisVo();
		try {
			resultVo = whoisModifySubmit(tbWhoisModifyVo);					// 2. WHOIS 정보변경
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		
		return resultVo;
	}
	
	/**
	 * WHOIS 연동 관련 WHOIS 작업 (IP CHECK)
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)	
	public void sendToWhoisIpCheck(TbWhoisVo tbWhoisVo) {
		try {
			whoisIpCheck(tbWhoisVo);					// 2. WHOIS 작업 (IP CHECK)
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}
	
	/**
	 * WHOIS 연동 관련 DB Call + WHOIS 작업
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)	
	public void sendToWhoisWithDb(TbWhoisVo tbWhoisVo) {
		try {
			
			String rtnValue = dbFunctionCall(tbWhoisVo);		// 1. DB Function Call
			if(rtnValue == null || rtnValue.equals("")) {
				PrintLogUtil.printLog("########## TB_WHOIS NO DATA");
			} else {
				tbWhoisVo.setSwhoisrequestid(rtnValue);
			    whoisSubmit(tbWhoisVo);			// 2. WHOIS 작업
			}
			
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}
	
	/**
	 * WHOIS 연동 관련 DB Call + WHOIS 작업
	 * @param tbWhoisVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)	
	public void sendToWhoisWithDbNoRtn(TbWhoisVo tbWhoisVo) {
		try {
			
			String rtnValue = dbFunctionCall(tbWhoisVo);		// 1. DB Function Call
//			if(rtnValue == null || rtnValue == "") { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(rtnValue == null || rtnValue.equals("")) {
				PrintLogUtil.printLog("########## TB_WHOIS NO DATA");
			} else {
				tbWhoisVo.setSwhoisrequestid(rtnValue);
				
			    whoisSubmitNoRtn(tbWhoisVo);									// 2. WHOIS 작업
			}
			
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}
	
	/**
	 *  WHOIS 연동 관련 DB Function Call
	 * @param tbWhoisVo
	 */
	public String dbFunctionCall(TbWhoisVo tbWhoisVo) {
		
		String rtnValue = null;
		tbWhoisVo.setSessionId(tbWhoisVo.getSmodifyId());
		TbWhoisComplexVo searchVo = new TbWhoisComplexVo();
		searchVo.setTbWhoisVo(tbWhoisVo);
		
		rtnValue = whoisTxService.callUfNewWhois(searchVo);
		
		return rtnValue;
	}
	
	/**
	 * Whois 전송을 위한 데이터 조회 
	 * 
	 */
	public void whoisSubmit(TbWhoisVo tbWhoisVo) {
		
		TbWhoisComplexVo searchVo = new TbWhoisComplexVo();
		searchVo.setTbWhoisVo(tbWhoisVo);
		String result = null; 
		
		List<TbWhoisComplexVo> resultList = null;
		
		try {
			
			if("ADD_NETNAME_ERROR".equals(tbWhoisVo.getType())) {
				
				com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo tbWhoisVo2 = new com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo();
				tbWhoisVo2.setNwhoisSeq(tbWhoisVo.getNwhoisSeq());
				tbWhoisVo2.setSmodifyId(tbWhoisVo.getSmodifyId());
				tbWhoisVo2.setType(tbWhoisVo.getType());
				result =  whoisMgmtTxService.selectWhoisComplexNew(tbWhoisVo2);
				
				searchVo.getTbWhoisVo().setSnetNm(result);
				
				if(result.equals("ERROR") || result.equals("DUPLICATION")) {
					throw new ServiceException("CMN.HIGH.00001");
				}
				// 전송할 Whois 정보 목록 조회
				resultList = whoisTxService.selectListWhoisComplexVo2(searchVo);
				
			} else if("DEL_KISA_IP".equals(tbWhoisVo.getType())) {	// kisa ip 해지
				TbWhoisComplexVo tbWhoisComplexVo = new TbWhoisComplexVo();
				
				tbWhoisVo.setSrequestCd(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN);
				tbWhoisComplexVo.setTbWhoisVo(tbWhoisVo);
				
				this.whoisSocket(tbWhoisComplexVo);
				
			} else if("DEFAULT".equals(tbWhoisVo.getType())) {
				// 전송할 Whois 정보 목록 조회
				resultList = whoisTxService.selectListWhoisComplexVo(searchVo);
			} else if("NEW_IPBLOCK_ERROR_RESUBMIT".equals(tbWhoisVo.getType())) {
				
				com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo tbWhoisVo2 = new com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo();
				tbWhoisVo2.setNwhoisSeq(tbWhoisVo.getNwhoisSeq());
				tbWhoisVo2.setSmodifyId(tbWhoisVo.getSmodifyId());
				tbWhoisVo2.setType(tbWhoisVo.getType());

				result =  whoisMgmtTxService.selectWhoisComplexNew(tbWhoisVo2);
				
				searchVo.getTbWhoisVo().setSnetNm(result);
				resultList = whoisTxService.selectListWhoisComplexVo(searchVo);
			} else if("NEW_NETNAME_ERROR".equals(tbWhoisVo.getType())) {		/// 추가 신청서 재전송
				
				com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo tbWhoisVo2 = new com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo();
				tbWhoisVo2.setNwhoisSeq(tbWhoisVo.getNwhoisSeq());
				tbWhoisVo2.setSmodifyId(tbWhoisVo.getSmodifyId());
				tbWhoisVo2.setType(tbWhoisVo.getType());

				result =  whoisMgmtTxService.selectWhoisComplexNew(tbWhoisVo2);
				
				searchVo.getTbWhoisVo().setSnetNm(result);
				resultList = whoisTxService.selectListWhoisComplexVo(searchVo);
			} else {
				resultList = whoisTxService.selectListWhoisComplexVo(searchVo);
			}
			
			if(resultList.size() > 0) {
				for(TbWhoisComplexVo tbWhoisComplexVo : resultList) {
					
					if(StringUtil.isNullorBlank(searchVo.getSmodifyId())) {
						searchVo.setSmodifyId(tbWhoisVo.getSmodifyId());
					}
					
					tbWhoisComplexVo.setSmodifyId(searchVo.getSmodifyId());
					
					this.whoisSocket(tbWhoisComplexVo);
				}
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Whois 전송을 위한 데이터 조회 
	 * 
	 */
	public void whoisSubmitNoRtn(TbWhoisVo tbWhoisVo) {
		
		TbWhoisComplexVo searchVo = new TbWhoisComplexVo();
		searchVo.setTbWhoisVo(tbWhoisVo);
		
		try {
			
			// 신청서 종류 조회
			// Map<String,Object> requestCd = 
			
			// 전송할 Whois 정보 목록 조회
			List<TbWhoisComplexVo> resultList = whoisTxService.selectListWhoisComplexVo(searchVo);
			
			if(resultList.size() > 0) {
				for(TbWhoisComplexVo tbWhoisComplexVo : resultList) {
					
					tbWhoisComplexVo.setSmodifyId(searchVo.getTbWhoisVo().getSmodifyId());
					this.whoisSocketNoRtn(tbWhoisComplexVo);
				}
				
			}
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
		}
		
	}
	
	
	/**
	 * Whois Socket 전송
	 * @param tbWhoisComplexVo
	 * 	 * # WHOIS 신청서 유형 코드 (requestType)
	 *  01: 신규 (NEW)
	 *  02: 추가/INFRA (ADD)
	 *  04: 변경 (MOD)
	 *  03: 삭제/반환 (RTN)
	 */
	public void whoisSocketNoRtn (TbWhoisComplexVo tbWhoisComplexVo) {

		String requestXml = null;
		// epp_Result[] result = null;
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			requestXml = whoisTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
			TbWhoisVo updateVo = new TbWhoisVo();
			
			updateVo.setSmodifyId(tbWhoisComplexVo.getSmodifyId());
			updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
			
			if (StringUtils.hasText(requestXml)) {
				socketInfoVo.setRequestUrl(requestXml);
				result = whoisTxService.sendWhoisSocketNoRtn(socketInfoVo);
				
				String message = "";
				
				if (result != null) {
					// short code = result[0].getCode();
					String code = result.get("m_code").toString();
					
					if(code.equals("01") ) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);			// 전송대기 (APP ~ Krnic 간 통신오류)
						message = "KRNIC Connect Error";
					} else if(code.equals("02")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_SUBMIT);				// Whois 전송대기 (Krnic ~ Whois 간 통신오류)
						if(result.get("m_msg") != null) {
							message = result.get("m_msg").toString();
						} else {
							message = "WHOIS Login Error";
						}
					} else if(code.equals("05")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);			// Whois 전송/응답대기 (Whois 응답 수신 오류)
						message = "WHOIS Error";
					} else if(code.equals("1000")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);		// 등록완료
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					} else if(!code.equals("01") && !code.equals("02") && !code.equals("05") && !code.equals("1000")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);		// 반송
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					}
					
					
					updateVo.setSwhoisresultMsg(message);
					
				} else {
					PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
					updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
					updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);				// 전송대기 (APP ~ Krnic 간 통신오류)
					updateVo.setSwhoisresultMsg("KRNIC Connect Error");
				}
				
				
				/* else {
					PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
					updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
					updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);				// 전송실패
					updateVo.setSwhoisresultMsg("WHOIS SEND FAIL");
				} */
				
				PrintLogUtil.printLogInfo("## WHOIS RESULT CD: " + updateVo.getSwhoisresultCd());
				PrintLogUtil.printLogInfo("## WHOIS RESULT MESSAGE: " + message);
				
				whoisTxService.updateResultCd(updateVo);
				
				
				// 로그 입력
				TbWhoisVo logVo = new TbWhoisVo();
				String logMessage = updateVo.getSwhoisresultMsg();
				
				logVo.setSwhoisrequestid(tbWhoisComplexVo.getTbWhoisVo().getSwhoisrequestid());
				logVo.setSresultmsg(logMessage);
				
				whoisTxService.insertWhoisLog(logVo);
				
				
				// 해지 완료 일 경우 TB_WHOIS 데이터 삭제
				String delResult = null;
				
				 if(tbWhoisComplexVo.getTbWhoisVo().getSrequestCd().equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN) 
						 && (updateVo.getSwhoisresultCd().equals(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST) || updateVo.getSwhoisresultCd().equals(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT))) {
				
					 PrintLogUtil.printLogInfo("ssaid : " + tbWhoisComplexVo.getTbWhoisVo().getSsaid() + " // nwhoisSeq: " + tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
					 
					delResult = whoisTxService.deleteResultUserCd(tbWhoisComplexVo.getTbWhoisVo());
					whoisTxService.deleteResultCd(tbWhoisComplexVo.getTbWhoisVo());
				
				}			
			
				
			} 
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			//throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	/**
	 * Whois Socket 전송
	 * @param tbWhoisComplexVo
	 * 	 * # WHOIS 신청서 유형 코드 (requestType)
	 *  01: 신규 (NEW)
	 *  02: 추가/INFRA (ADD)
	 *  04: 변경 (MOD)
	 *  03: 삭제/반환 (RTN)
	 */
	public void whoisSocket (TbWhoisComplexVo tbWhoisComplexVo) {

		String requestXml = null;
		// epp_Result[] result = null;
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			requestXml = whoisTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
			
			TbWhoisVo updateVo = new TbWhoisVo();
			
			updateVo.setSmodifyId(tbWhoisComplexVo.getSmodifyId());
			updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
			
			if (StringUtils.hasText(requestXml)) {
				socketInfoVo.setRequestUrl(requestXml);
				result = whoisTxService.sendWhoisSocket(socketInfoVo);
				
				String message = "";
				if (result != null) {
					PrintLogUtil.printLogInfo("WHOIS SEND SUCCESS");
					
				    //short resultCode = result[0].getCode();
				    //message  =  "[" + resultCode+"] " + result[0].getMsg();
					
					String code = result.get("m_code").toString();
					
					if(code.equals("01") ) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);			// 전송대기 (APP ~ Krnic 간 통신오류)
						message = "KRNIC Connect Error";
					} else if(code.equals("02")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_SUBMIT);				// Whois 전송대기 (Krnic ~ Whois 간 통신오류)
						if(result.get("m_msg") != null) {
							message = result.get("m_msg").toString();
						} else {
							message = "WHOIS Login Error";
						}
					} else if(code.equals("05")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);			// Whois 전송/응답대기 (Whois 응답 수신 오류)
						message = "WHOIS Error";
					} else if(code.equals("1000")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);		// 등록완료
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					} else if(!code.equals("01") && !code.equals("02") && !code.equals("05") && !code.equals("1000")) {
						updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);		// 반송
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					}
					
					updateVo.setSwhoisresultMsg(message);
					
				}  else {
					PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
					updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
					updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);				// 전송대기 (APP ~ Krnic 간 통신오류)
					updateVo.setSwhoisresultMsg("KRNIC Connect Error");
				}
				
				PrintLogUtil.printLogInfo("## WHOIS RESULT CD: " + updateVo.getSwhoisresultCd());
				PrintLogUtil.printLogInfo("## WHOIS RESULT MESSAGE: " + message);
				
				whoisTxService.updateResultCd(updateVo);
				
				// 로그 입력
				TbWhoisVo logVo = new TbWhoisVo();
				String logMessage = updateVo.getSwhoisresultMsg();
				
				logVo.setSwhoisrequestid(tbWhoisComplexVo.getTbWhoisVo().getSwhoisrequestid());
				logVo.setSresultmsg(logMessage);
				
				whoisTxService.insertWhoisLog(logVo);
				
				
				// 해지 완료 일 경우 TB_WHOIS 데이터 삭제
				String delResult = null;
				 if(tbWhoisComplexVo.getTbWhoisVo().getSrequestCd().equals(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN) 
						 && updateVo.getSwhoisresultCd().equals(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST)) {
					 
					 delResult = whoisTxService.deleteResultUserCd(tbWhoisComplexVo.getTbWhoisVo());
					 whoisTxService.deleteResultCd(tbWhoisComplexVo.getTbWhoisVo());
					
				}				
			} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	/**
	 * WHOIS 작업
	 * 
	 * # WHOIS 신청서 유형 코드 (requestType)
	 * 06: IP Check
	 * @param tbWhoisVo
	 */
	public void whoisIpCheck(TbWhoisVo tbWhoisVo) {
		
		String requestXml = null;
		
		try {
			
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			// epp_Result[] result = null;
			Map<String,Object> result = new HashMap<String,Object>();
			
			TbWhoisComplexVo tbWhoisComplexVo = new TbWhoisComplexVo();
			tbWhoisComplexVo.setTbWhoisVo(tbWhoisVo);
			
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			requestXml = whoisTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
				
				if (StringUtils.hasText(requestXml)) {
					socketInfoVo.setRequestUrl(requestXml);
					result = whoisTxService.sendWhoisSocket(socketInfoVo);

					if (result != null) {
							// Code..
						PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
					}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}
		
	}
	
	/**
	 * WHOIS 작업
	 * 
	 * # WHOIS 신청서 유형 코드 (requestType)
	 * 07: IP Info
	 * @param tbWhoisVo
	 */
	public WhoisInfoObj whoisInfo (TbWhoisVo tbWhoisVo) {
		
		String requestXml = null;
		WhoisInfoObj infoObj = new WhoisInfoObj();
		try {
			
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			// epp_Result[] result = null;
			Map<String,Object> result = new HashMap<String, Object>();
		
			tbWhoisVo.setSrequestCd("07");
			TbWhoisComplexVo tbWhoisComplexVo = new TbWhoisComplexVo();
			tbWhoisComplexVo.setTbWhoisVo(tbWhoisVo);
			
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			requestXml = whoisTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
				
				if (StringUtils.hasText(requestXml)) {
					socketInfoVo.setRequestUrl(requestXml);
					result = whoisTxService.sendWhoisSocket(socketInfoVo);
					
					if(result == null) {
						//infoObj.setRtnMsg("SOCKET 중계 서버와의 연동처리 실패하였습니다. 시스템 관리자에게 문의하시기 바랍니다.");
						infoObj.setRtnMsg("SOCKET_ERROR");
					} else {
						if(result.get("m_lang") != null && !result.get("m_lang").equals("")) {
							String resultXml = result.get("m_lang").toString();
							WhoisFromXml parseXml = new WhoisFromXml();
							infoObj = parseXml.fromXml(resultXml);
						} else {
							String message = result.get("m_msg").toString();
							String code = result.get("m_code").toString();
							infoObj.setRtnMsg("[" + code +"] " + message);
						}
					}
					
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			infoObj.setRtnMsg("SOCKET_ERROR");
			
			//throw new ServiceException("CMN.HIGH.00000");
		} 
		
		return infoObj;
	}
	
	/**
	 * WHOIS 정보변경
	 * 03: 변경 (MOD)
	 * @param tbWhoisVo
	 */
	public TbWhoisVo whoisModifySubmit(TbWhoisModifyVo tbWhoisModifyVo) {
		
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
				
		int resultCount = 0;
		String requestXml = null;
		// epp_Result[] result = null;
		Map<String,Object> result = new HashMap<String,Object>();
		String resultMessage = null;
		
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			WhoisToXml obj = new WhoisToXml();
			String newClTrid = tbWhoisModifyVo.getSwhoisrequestid();
			
			if(newClTrid != null) {
				String[] tmp = newClTrid.split("-");
				newClTrid = tmp[0] + "-" + tmp[1] + "-" + tmp[2];
			}
			
			obj.setReqType(CommonCodeUtil.WHOIS_REQUEST_TYPE_MOD);					// WHOIS 변경 신청서
			obj.setClTRID(tbWhoisModifyVo.getSwhoisrequestid());
			obj.setStartIp(tbWhoisModifyVo.getSfirstAddr());	// 시작IP	
			obj.setEndIp(tbWhoisModifyVo.getSlastAddr());		// 끝IP
			
			obj.setKoOrgName(tbWhoisModifyVo.getsAftOrgName());							// 한글 기관명
			obj.setKoOrgAddr(tbWhoisModifyVo.getsAftOrgAddr());					// 한글 주소
			obj.setKoOrgAddrDtl(tbWhoisModifyVo.getsAftOrgAddrDetail());				// 한글 상세주소
			obj.setEnOrgName(tbWhoisModifyVo.getsAftEOrgName());							// 영문 기관명
			obj.setEnOrgAddr(tbWhoisModifyVo.getsAftEOrgAddr());					// 영문 주소
			obj.setEnOrgAddrDtl(tbWhoisModifyVo.getsAftEOrgAddrDetail());				// 영문 상세주소
			obj.setOrgPost(tbWhoisModifyVo.getsAftZipCode());
			
			TbWhoisUserVo searchVo = new TbWhoisUserVo();
			TbWhoisUserVo ktInfoVo = new TbWhoisUserVo();
			searchVo.setSsaid("00000000000");
			ktInfoVo = whoisMgmtTxService.selectWhoisUser(searchVo);
			
			obj.setOrgEmail(ktInfoVo.getSadmEmail());
			obj.setOrgPhone(ktInfoVo.getSadmDpphone());
			
			TbWhoisVo netVo = null;
			tbWhoisModifyVo.setSwhoisrequestid(newClTrid);
			netVo = whoisMgmtTxService.selectNetTbWhoisVo(tbWhoisModifyVo);
			if(netVo == null) {
				throw new ServiceException("CMN.HIGH.00000");
			}
			
			obj.setNetName(netVo.getSnetNm());							// 네트워크 이름
			obj.setNetType(netVo.getSnettype());							// 네트워크  분류
			obj.setSvcType(netVo.getSservicegb());							// 서비스 분류
			obj.setIpType(netVo.getSiptype());							// IP 분류
			obj.setOrgType(netVo.getSuserorggb());							// 이용기관 분류
			obj.setSvcLoc(netVo.getSsvcloc());							// 서비스 지역
			obj.setComment(netVo.getSsvccommnet());			// 추가 사항
		
			requestXml = obj.toXml();
			String message = "";
			if (StringUtils.hasText(requestXml)) {
				socketInfoVo.setRequestUrl(requestXml);
				result = whoisTxService.sendWhoisSocket(socketInfoVo);
				
				com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo tbWhoisUserVo =  new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo();
				
				if (result != null) {
					// short code = result[0].getCode();
					String code = result.get("m_code").toString();
					
					
					if(code.equals("01") ) {
						tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);			// 전송대기 (APP ~ Krnic 간 통신오류)
						message = "KRNIC Connect Error";
					} else if(code.equals("02")) {
						tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_SUBMIT);				// Whois 전송대기 (Krnic ~ Whois 간 통신오류)
						if(result.get("m_msg") != null) {
							message = result.get("m_msg").toString();
						} else {
							message = "WHOIS Login Error";
						}
					} else if(code.equals("05")) {
						tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);			// Whois 전송/응답대기 (Whois 응답 수신 오류)
						message = "WHOIS Error";
					} else if(code.equals("1000")) {
						tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);		// 등록완료
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					} else if(!code.equals("01") && !code.equals("02") && !code.equals("05") && !code.equals("1000")) {
						tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);		// 반송
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					}
					
					tbWhoisVo.setSwhoisresultMsg(message);
					
				} else {
					PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
					tbWhoisVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);				// 전송대기 (APP ~ Krnic 간 통신오류)
					tbWhoisVo.setSwhoisresultMsg("KRNIC Connect Error");
				}
				
				
				tbWhoisVo.setSrequestCd(CommonCodeUtil.WHOIS_REQUEST_TYPE_MOD);		// 신청서 구분
				tbWhoisVo.setSmodifyId(tbWhoisModifyVo.getsModifyId());
				tbWhoisVo.setNwhoisSeq(tbWhoisModifyVo.getNwhoisseq());
				resultCount = whoisTxService.updateResultCd(tbWhoisVo);
				
				tbWhoisUserVo.setSsaid(tbWhoisModifyVo.getSsaid());
				tbWhoisUserVo.setNwhoisSeq(tbWhoisModifyVo.getNwhoisseq());
				tbWhoisUserVo.setSmodifyId(tbWhoisModifyVo.getsModifyId());
				tbWhoisUserVo.setSorgname(tbWhoisModifyVo.getsAftOrgName());
				tbWhoisUserVo.setSaddr(tbWhoisModifyVo.getsAftOrgAddr());
				tbWhoisUserVo.setSaddrDetail(tbWhoisModifyVo.getsAftOrgAddrDetail());
				tbWhoisUserVo.setSzipcode(tbWhoisModifyVo.getsAftZipCode());
				tbWhoisUserVo.setSeorgname(tbWhoisModifyVo.getsAftEOrgName());
				tbWhoisUserVo.setSeaddr(tbWhoisModifyVo.getsAftEOrgAddr());
				tbWhoisUserVo.setSeaddrDetail(tbWhoisModifyVo.getsAftEOrgAddrDetail());
				
				resultCount =  whoisTxService.updateTbWhoisUserVo(tbWhoisUserVo);
			}
			
			// 로그 입력
			TbWhoisVo logVo = new TbWhoisVo();
			String logMessage = resultMessage;
			
			logVo.setSwhoisrequestid(newClTrid);
			logVo.setSresultmsg(logMessage);
			logVo.setStransKind("USER");
			whoisTxService.insertWhoisLog(logVo);
		
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return tbWhoisVo;
	}
	
	/**
	 * Whois Socket 전송
	 * @param tbWhoisComplexVo
	 *  03: 삭제/반환 (RTN)
	 */
	public TbWhoisVo whoisRtnSubmit (TbWhoisComplexVo tbWhoisComplexVo) {

		TbWhoisVo resultVo = new TbWhoisVo();
		String requestXml = null;

		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = 0;
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
			
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			
			requestXml = whoisTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
			
			
			if (StringUtils.hasText(requestXml)) {
				socketInfoVo.setRequestUrl(requestXml);
				result = whoisTxService.sendWhoisSocket(socketInfoVo);
				
				String message = "";
				if (result != null) {
					PrintLogUtil.printLogInfo("WHOIS SEND SUCCESS");
					
					String code = result.get("m_code").toString();
					
					if(code.equals("01") ) {
						resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);			// 전송대기 (APP ~ Krnic 간 통신오류)
						message = "KRNIC Connect Error";
					} else if(code.equals("02")) {
						resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_SUBMIT);				// Whois 전송대기 (Krnic ~ Whois 간 통신오류)
						if(result.get("m_msg") != null) {
							message = result.get("m_msg").toString();
						} else {
							message = "WHOIS Login Error";
						}
					} else if(code.equals("05")) {
						resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);			// Whois 전송/응답대기 (Whois 응답 수신 오류)
						message = "WHOIS Error";
					} else if(code.equals("1000")) {
						resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);		// 등록완료
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					} else if(!code.equals("01") && !code.equals("02") && !code.equals("05") && !code.equals("1000")) {
						resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);		// 반송
						message  =  "[" + code+"] " + result.get("m_msg").toString();
					}
					
					resultVo.setSwhoisresultMsg(message);
					
				}  else {
					PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
					
					resultVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);				// 전송대기 (APP ~ Krnic 간 통신오류)
					resultVo.setSwhoisresultMsg("KRNIC Connect Error");
				}
				
				PrintLogUtil.printLogInfo("## WHOIS RESULT CD: " + resultVo.getSwhoisresultCd());
				PrintLogUtil.printLogInfo("## WHOIS RESULT MESSAGE: " + message);
								
			} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}

		return resultVo;
	}
	
	/**
	 * 할당 Seq 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public TbWhoisVo selectAllocMstSeq (TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
		TbWhoisVo vo = new TbWhoisVo();
		try {
			vo = whoisTxService.selectAllocMstSeq(tbIpAllocNeossMstVo);
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
			vo = whoisTxService.selectWhoisSeq(tbIpAllocNeossMstVo);
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
	public boolean exceptWhois (String gubun, BigInteger allocMstSeq, BigInteger whoisSeq) {
		boolean rtnValue = true;
		String[] arrBitMask = null;
		String[] arrSsvcGroupCd = null;
		TbIpAllocMstVo allocMstVo = new TbIpAllocMstVo();
		TbIpAllocMstVo rtnAllocMstVo = null;
		try {
			
			// 할당 IP seq
			if ("NEW".equals(gubun) && allocMstSeq == null) {
				rtnValue = false;
			} else if ("DEL".equals(gubun) && whoisSeq == null) {
				rtnValue = false;
			} else {
				
				TbIpAllocMstVo tbIpAllocMstVo = new TbIpAllocMstVo();
				tbIpAllocMstVo.setNipAllocMstSeq(allocMstSeq);
				tbIpAllocMstVo.setNwhoisSeq(whoisSeq);
				rtnAllocMstVo = whoisTxService.selectAllocMst(tbIpAllocMstVo);
				
				if (rtnAllocMstVo == null) {
					rtnValue = false;
				} else {
					
					String allocBitMask = rtnAllocMstVo.getNbitmask().toString();
					String allocSvcLineCd = rtnAllocMstVo.getSsvcLineTypeCd().toString(); 
					String allocSvcGroupCd = rtnAllocMstVo.getSsvcGroupCd().toString();
					
					
					String strBitMask = configPropertieService.getString("Whois.Except.BitMask");					// Whois 전송 예외되는 비트마스크
					String strSsvcGroupCd = configPropertieService.getString("Whois.Except.SsvcGroupCd");	// Whois 전송 예외되는 조직
					
					arrBitMask = strBitMask.split(",");
					// arrSsvcGroupCd = strSsvcGroupCd.split(",");
						
					// Whois 전송 예외되는 비트마스크
//					if(!arrBitMask.equals(null)) { //Codeeyes-Critical-equals()에서 Null 비교 금지
					if(arrBitMask != null){ 
						for(int i=0; i<arrBitMask.length; i++) {
							//PrintLogUtil.printLogInfo(">>>>>allocBitMask: " + allocBitMask + " // arrBitMask[i]: " + arrBitMask[i]);
							if(allocBitMask.equals(arrBitMask[i])) {
								rtnValue = false;
							}
						}
					}
					
					// Whois 전송 예외되는 조직
					//PrintLogUtil.printLogInfo(">>>>>allocSvcGroupCd: " + allocSvcGroupCd + " // strSsvcGroupCd: " + strSsvcGroupCd);
					if("CL0001".equals(allocSvcLineCd) || "CL0002".equals(allocSvcLineCd)) {
						if(allocSvcGroupCd.equals(strSsvcGroupCd)) {
							rtnValue = false;
						}
					}
					
					/*if(!arrSsvcGroupCd.equals(null)) {
						for(int j=0; j<arrSsvcGroupCd.length; j++) {
							//KORNET, PREMIUM
							if("CL0001".equals(allocSvcLineCd) || "CL0002".equals(allocSvcLineCd)) {
								if(allocSvcGroupCd.equals(arrSsvcGroupCd[j])) {
									rtnValue = false;
								}
							}
						}
						
					}*/
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnValue;
	}
	
	public int selectNipAllocMstCnt(BigInteger whoisSeq){
		int result = 0;
		try{
			TbIpAllocMstVo tbIpAllocMstVo = new TbIpAllocMstVo();
			tbIpAllocMstVo.setNwhoisSeq(whoisSeq);
			result = whoisTxService.selectNipAllocMstCnt(tbIpAllocMstVo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
