package com.kt.ipms.legacy.linkmgmt.socketmgmt.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.util.TACSCodeUtil.TACSErrorCodes;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.SocketInfoVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.WhoisMstVo;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisService;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.adapter.TacsMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequestVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

@Component
@Transactional
public class SocketMgmtService {
	
@Lazy @Autowired
	private ConfigPropertieService configPropertieService;
	
@Lazy @Autowired
	private SocketMgmtTxService socketMgmtTxService;
	
@Lazy @Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;
	
@Lazy @Autowired
	private CommonService commonService;
	
@Lazy @Autowired
	private TacsMgmtAdapterService tacsMgmtAdapterService;
	
@Lazy @Autowired
	private WhoisService whoisService;
	
	@Transactional(readOnly = true)
	public WhoisMstVo getWhoisMstVo(String query) {
		WhoisMstVo resultVo = null;
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = configPropertieService.getInt("Proxy.whois.client.searchPort");
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			String clientUrl = configPropertieService.getString("Proxy.whois.client.searchUrl");
			String requestUrl = clientUrl.replace("[QUERY]", query);
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			socketInfoVo.setRequestUrl(requestUrl);
			Object resultObject = socketMgmtTxService.sendWhoisSocket(socketInfoVo);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(Feature.UNWRAP_ROOT_VALUE, true);
			resultVo = (WhoisMstVo) objectMapper.readValue(resultObject.toString(), WhoisMstVo.class);
		} catch (ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Whois 서버와의 "});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public String getTraceRoute(String query) {
		String resultStr = "";
		try {
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
			int clientPort = configPropertieService.getInt("Proxy.whois.client.resultPort");
			int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			socketInfoVo.setRequestUrl(query);
			Object resultObject = socketMgmtTxService.sendWhoisSocket(socketInfoVo);
			if (resultObject != null) {
				resultStr = resultObject.toString();
			} else {
				throw new ServiceException("LNK.HIGH.00032", new String[]{"TracerRoute 서버와의 "});
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"TracerRoute 서버와의 "});
		}
		return resultStr;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void startWhoisBatchScheduler() {
		int maxCnt = configPropertieService.getInt("Batch.whoisMaxCount");
		TbWhoisComplexVo searchVo = new TbWhoisComplexVo();
		searchVo.setTbWhoisVo(new TbWhoisVo());
		searchVo.getTbWhoisVo().setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_STANDBY);

		searchVo.setLastIndex(maxCnt);
		try {
			TbWhoisComplexListVo resultListVo = socketMgmtTxService.selectListStandByWhois(searchVo);
			PrintLogUtil.printLogInfo("START WHOIS SEND BATCH SCHEDULER===========================================================");
			PrintLogUtil.printLog(String.valueOf(resultListVo.getTotalCount()));
			
			if(resultListVo.getTotalCount() > 0) {
				int cnt = 0;
				List<TbWhoisComplexVo> resultList = resultListVo.getTbWhoisComplexVos();
				for (TbWhoisComplexVo tbWhoisComplexVo : resultList) {
				//TbWhoisComplexVo tbWhoisComplexVo = resultList.get(0);
					Thread.sleep(1000);
					
					cnt++;
					try {
						
						if (cnt > maxCnt) {
							break;
						}
						
						com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo vo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo();
						
						com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo tbWhoisVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
						com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo tbWhoisUserVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo();
						
						tbWhoisVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
						tbWhoisVo.setSrequestCd(tbWhoisComplexVo.getTbWhoisVo().getSrequestCd());		 			// 신청서종류
						tbWhoisVo.setSwhoisrequestid(tbWhoisComplexVo.getTbWhoisVo().getSwhoisrequestid());		// Whois Request Id
						tbWhoisVo.setSfirstAddr(tbWhoisComplexVo.getTbWhoisVo().getSfirstAddr());							// 시작IP
						tbWhoisVo.setSlastAddr(tbWhoisComplexVo.getTbWhoisVo().getSlastAddr());							// 끝IP
						tbWhoisVo.setSnetNm(tbWhoisComplexVo.getTbWhoisVo().getSnetNm());								// 네트워크 이름
						tbWhoisVo.setSnettype(tbWhoisComplexVo.getTbWhoisVo().getSnettype());							// 네트워크  분류
						tbWhoisVo.setSservicegb(tbWhoisComplexVo.getTbWhoisVo().getSservicegb());						// 서비스 분류
						tbWhoisVo.setSiptype(tbWhoisComplexVo.getTbWhoisVo().getSiptype());								// IP 분류
						tbWhoisVo.setSuserorggb(tbWhoisComplexVo.getTbWhoisVo().getSuserorggb());						// 이용기관 분류
						tbWhoisVo.setSsvcloc(tbWhoisComplexVo.getTbWhoisVo().getSsvcloc());								// 서비스 지역
						tbWhoisVo.setSsvccommnet(tbWhoisComplexVo.getTbWhoisVo().getSsvccommnet());				// 추가사항
						tbWhoisUserVo.setSorgname(tbWhoisComplexVo.getTbWhoisUserVo().getSorgname());				// 한글기관명
						tbWhoisUserVo.setSaddr(tbWhoisComplexVo.getTbWhoisUserVo().getSaddr());						// 한글 주소
						tbWhoisUserVo.setSaddrDetail(tbWhoisComplexVo.getTbWhoisUserVo().getSaddrDetail());			// 한글 상세주소
						tbWhoisUserVo.setSeorgname(tbWhoisComplexVo.getTbWhoisUserVo().getSeorgname());			// 영문기관명
						tbWhoisUserVo.setSeaddr(tbWhoisComplexVo.getTbWhoisUserVo().getSeaddr());						// 영문 주소
						tbWhoisUserVo.setSeaddrDetail(tbWhoisComplexVo.getTbWhoisUserVo().getSeaddrDetail());		// 영문 상세주소
						tbWhoisUserVo.setSzipcode(tbWhoisComplexVo.getTbWhoisUserVo().getSzipcode());				// 우편번호
						tbWhoisUserVo.setSadmDpphone(tbWhoisComplexVo.getTbWhoisUserVo().getSadmDpphone());	// 전화번호
						tbWhoisUserVo.setSadmEmail(tbWhoisComplexVo.getTbWhoisUserVo().getSadmEmail());			// 이메일
						
						
						vo.setTbWhoisVo(tbWhoisVo);
						vo.setTbWhoisUserVo(tbWhoisUserVo);
						
						whoisService.whoisSocket(vo);	
					} catch (Exception e) {
						PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
						PrintLogUtil.printError(e);
					}
				}
			}
			/*if (resultListVo.getTotalCount() > 0) {
				SocketInfoVo socketInfoVo = new SocketInfoVo();
				InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.whois.client.ip"));
				int clientPort = configPropertieService.getInt("Proxy.whois.client.submitPort");
				int clientTimeout = configPropertieService.getInt("Proxy.whois.client.timeout");
				socketInfoVo.setClientIp(clientIp);
				socketInfoVo.setClinetPort(clientPort);
				socketInfoVo.setClientTimeout(clientTimeout);
				List<TbWhoisComplexVo> resultList = resultListVo.getTbWhoisComplexVos();
				int cnt = 0;
				for (TbWhoisComplexVo tbWhoisComplexVo : resultList) {
					Thread.sleep(1000);
					String requestXml = null;
					cnt++;
					try {
						if (cnt > maxCnt) {
							break;
						}
						requestXml = socketMgmtTxService.getWhoisSubmitRequestXml(tbWhoisComplexVo);
						PrintLogUtil.printLog(new String(requestXml.getBytes("UTF-8"), "EUC-KR"));
						if (StringUtils.hasText(requestXml)) {
							socketInfoVo.setRequestUrl(requestXml);
							Object resultObject = socketMgmtTxService.sendWhoisSocket(socketInfoVo);
							if (resultObject != null && resultObject.toString().equals(CommonCodeUtil.SUCCESS_MSG)) {
								PrintLogUtil.printLogInfo("WHOIS SEND SUCCESS");
								Date dbDate = commonService.selectSysDate();
								TbWhoisVo updateVo = new TbWhoisVo();
								updateVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
								updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_COMPLETE);
								updateVo.setDmodifyDt(dbDate);
								socketMgmtTxService.updateResultCd(updateVo);
							} else {
								PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
								if (resultObject != null) {
									PrintLogUtil.printLogInfo(resultObject.toString());
									PrintLogUtil.printLog(new String(resultObject.toString().getBytes("UTF-8"), "EUC-KR"));
								}
							}
						}
					} catch (ServiceException e) {
						PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
						PrintLogUtil.printError(e);
					} catch (Exception e) {
						PrintLogUtil.printLogInfo("WHOIS SEND FAIL");
						PrintLogUtil.printError(e);
					}
				}
			}*/
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMsgMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		PrintLogUtil.printLogInfo("END WHOIS SEND BATCH SCHEDULER===========================================================");
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectMainIpInfoMst(WhoisMstVo searchVo){
		IpAllocOperMstVo resultVo =null;
		try {
			IpAllocOperMstVo ipAllocOperMstVo = new IpAllocOperMstVo();
			ipAllocOperMstVo.setNipAssignMstSeq(searchVo.getNipAssignMstSeq());
			resultVo = socketMgmtTxService.selectMainIpInfoMst(ipAllocOperMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TacsResponseListVo getCheckTacsRouteList(TacsRequstListVo tacsRequstListVo, String userId) {
		TacsResponseListVo resultListVo = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SocketInfoVo socketInfoVo = new SocketInfoVo();
			InetAddress clientIp = InetAddress.getByName(configPropertieService.getString("Proxy.tacs.client.ip"));
			int clientPort = configPropertieService.getInt("Proxy.tacs.client.port");
			int clientTimeout = configPropertieService.getInt("Proxy.tacs.client.timeout");
			socketInfoVo.setClientIp(clientIp);
			socketInfoVo.setClinetPort(clientPort);
			socketInfoVo.setClientTimeout(clientTimeout);
			resultListVo = new TacsResponseListVo();
			List<TacsResponseVo> resultList = new ArrayList<TacsResponseVo>();
			resultListVo.setTacsResponseVos(resultList);
			List<TacsRequestVo> requestList = tacsRequstListVo.getTacsRequestVos();
			for (TacsRequestVo requestVo : requestList) {
				TbTacsConnHistVo tbTacsConnHistVo = new TbTacsConnHistVo();
				tbTacsConnHistVo.setPipFcltInet(requestVo.getTargetIp());
				tbTacsConnHistVo.setPipPrefix(requestVo.getPipPrefix());
				tbTacsConnHistVo.setSfcltPromptNm(requestVo.getPromptNm());
				tbTacsConnHistVo.setScreateId(userId);
				tbTacsConnHistVo.setSmodifyId(userId);
				TacsResponseVo tacsResponseVo = null;
				try {
					String requestJson = objectMapper.writeValueAsString(requestVo);
					socketInfoVo.setRequestUrl(requestJson);
					Object responseJson = socketMgmtTxService.sendTacsSocket(socketInfoVo);
					tacsResponseVo = (TacsResponseVo) objectMapper.readValue(responseJson.toString(), TacsResponseVo.class);
					if (tacsResponseVo == null) {
						tacsResponseVo = new TacsResponseVo();
						tacsResponseVo.setResponseCd(TACSErrorCodes.OG_ERR_UNKNOWN.getCode());
						tacsResponseVo.setResponseMsg(TACSErrorCodes.OG_ERR_UNKNOWN.getMsg());
						tacsResponseVo.setFlag("N");
						tacsResponseVo.setTargetIp(requestVo.getTargetIp());
					}
				} catch (Exception e) {
					tacsResponseVo = new TacsResponseVo();
					tacsResponseVo.setResponseCd(TACSErrorCodes.OG_ERR_UNKNOWN.getCode());
					tacsResponseVo.setResponseMsg(TACSErrorCodes.OG_ERR_UNKNOWN.getMsg());
					tacsResponseVo.setFlag("N");
					tacsResponseVo.setTargetIp(requestVo.getTargetIp());
				}
				tbTacsConnHistVo.setNresultCd(tacsResponseVo.getResponseCd());
				tbTacsConnHistVo.setSresultMsg(tacsResponseVo.getResponseMsg());
				tbTacsConnHistVo.setSavailYn(tacsResponseVo.getFlag());
				resultList.add(tacsResponseVo);
				tacsMgmtAdapterService.insertTacsConnHistNew(tbTacsConnHistVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Socket 서버와의 "});
		}
		return resultListVo;
	}

}
