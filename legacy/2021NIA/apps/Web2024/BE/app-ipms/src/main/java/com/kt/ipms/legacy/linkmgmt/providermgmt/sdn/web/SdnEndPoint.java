package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.service.SdnService;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0002Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0002Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0004Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0004Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0005Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0005Response;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;

@Endpoint
public class SdnEndPoint {
	
	@Autowired
	private SdnService sdnService;

	@Autowired
	private LinkUtil linkutil;
	
	@Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;
	
	private static final String SDN_0001_NAMESPACE = "http://ip.kt.com/interlocking/SDN_IPM_WS_0001";
	private static final String SDN_0002_NAMESPACE = "http://ip.kt.com/interlocking/SDN_IPM_WS_0002";
	private static final String SDN_0003_NAMESPACE = "http://ip.kt.com/interlocking/SDN_IPM_WS_0003";
	private static final String SDN_0004_NAMESPACE = "http://ip.kt.com/interlocking/SDN_IPM_WS_0004";
	private static final String SDN_0005_NAMESPACE = "http://ip.kt.com/interlocking/SDN_IPM_WS_0005";
	

	/**
	 * 가용 IPBlock 추천 List 연동
	 * @param sdn0001Request
	 * @return
	 */
	@PayloadRoot(localPart = "Sdn0001Request", namespace = SDN_0001_NAMESPACE)
	public @ResponsePayload Sdn0001Response consumeSdn0001Service(@RequestPayload Sdn0001Request sdn0001Request){
		Sdn0001Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(sdn0001Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0001 stcRequest Null Pointer Exception");
			}
			stcResponse = sdnService.consumeSdn0001Service(sdn0001Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0001 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Sdn0001Response(); 
			stcResponse.setResultCD("ERR9999");
			String resultmsg =tbCmnMsgMstService.selectMsgDesc(e);
			stcResponse.setResultMSG(resultmsg);					
		}
		catch(Exception e) {
			stcResponse.setResultCD(sRetrunCD);
			stcResponse.setResultMSG(sReturnMSG);
			linkutil.setSystemERR(e);					
		}
		return stcResponse;
	}
	
	/**
	 * IP Block 확정 정보 연동
	 * @param sdn0002Request
	 * @return
	 */
	@PayloadRoot(localPart = "Sdn0002Request", namespace = SDN_0002_NAMESPACE)
	public @ResponsePayload() Sdn0002Response consumeSdn0002Service(@RequestPayload Sdn0002Request sdn0002Request){
		Sdn0002Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(sdn0002Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("SDN_IPM_WS_0002 stcRequest Null Pointer Exception");
			}
			stcResponse = sdnService.consumeSdn0002Service(sdn0002Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("SDN_IPM_WS_0002 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Sdn0002Response(); 
			stcResponse.setResultCD("ERR9999");
			String resultmsg =tbCmnMsgMstService.selectMsgDesc(e);
			stcResponse.setResultMSG(resultmsg);					
		}
		catch(Exception e) {
			stcResponse.setResultCD(sRetrunCD);
			stcResponse.setResultMSG(sReturnMSG);
			linkutil.setSystemERR(e);
		}
		return stcResponse;
	}
	
	/**
	 * IPMS IP 할당 정보 조회
	 * @param sdn0003Request
	 * @return
	 */
	@PayloadRoot(localPart = "Sdn0003Request", namespace = SDN_0003_NAMESPACE)
	public @ResponsePayload() Sdn0003Response consumeSdn0003Service(@RequestPayload Sdn0003Request sdn0003Request){
		Sdn0003Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(sdn0003Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("SDN_IPM_WS_0003 stcRequest Null Pointer Exception");
			}
			stcResponse = sdnService.consumeSdn0003Service(sdn0003Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("SDN_IPM_WS_0003 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Sdn0003Response(); 
			stcResponse.setResultCD("ERR9999");
			String resultmsg =tbCmnMsgMstService.selectMsgDesc(e);
			stcResponse.setResultMSG(resultmsg);					
		}
		catch(Exception e) {
			stcResponse.setResultCD(sRetrunCD);
			stcResponse.setResultMSG(sReturnMSG);
			linkutil.setSystemERR(e);
		}
		return stcResponse;
	}
	
	/**
	 * 시설정보조회 (서비스계약ID, 국사코드 등 정보 조회)
	 * @param sdn0003Request
	 * @return
	 */
	@PayloadRoot(localPart = "Sdn0004Request", namespace = SDN_0004_NAMESPACE)
	public @ResponsePayload() Sdn0004Response consumeSdn0004Service(@RequestPayload Sdn0004Request sdn0004Request){
		Sdn0004Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(sdn0004Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("SDN_IPM_WS_0004 stcRequest Null Pointer Exception");
			}
			stcResponse = sdnService.consumeSdn0004Service(sdn0004Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("SDN_IPM_WS_0004 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Sdn0004Response(); 
			stcResponse.setResultCD("ERR9999");
			String resultmsg =tbCmnMsgMstService.selectMsgDesc(e);
			stcResponse.setResultMSG(resultmsg);					
		}
		catch(Exception e) {
			stcResponse.setResultCD(sRetrunCD);
			stcResponse.setResultMSG(sReturnMSG);
			linkutil.setSystemERR(e);
		}
		return stcResponse;
	}
	
	/**
	 * IP Block 할당 예약 정보 연동
	 * @param sdn0002Request
	 * @return
	 */
	@PayloadRoot(localPart = "Sdn0005Request", namespace = SDN_0005_NAMESPACE)
	public @ResponsePayload() Sdn0005Response consumeSdn0005Service(@RequestPayload Sdn0005Request sdn0005Request){
		Sdn0005Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(sdn0005Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("SDN_IPM_WS_0005 stcRequest Null Pointer Exception");
			}
			stcResponse = sdnService.consumeSdn0005Service(sdn0005Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("SDN_IPM_WS_0005 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Sdn0005Response(); 
			stcResponse.setResultCD("ERR9999");
			String resultmsg =tbCmnMsgMstService.selectMsgDesc(e);
			stcResponse.setResultMSG(resultmsg);					
		}
		catch(Exception e) {
			stcResponse.setResultCD(sRetrunCD);
			stcResponse.setResultMSG(sReturnMSG);
			linkutil.setSystemERR(e);
		}
		return stcResponse;
	}
}
