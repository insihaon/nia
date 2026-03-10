package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.service.NeossService;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0001Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0001Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0002Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0002Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0004Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0004Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0005Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0005Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008Response;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;



@Endpoint
public class NeossEndpoint {
	@Autowired
	private NeossService neossService;

	@Autowired
	private LinkUtil linkutil;
	
	@Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;
	
	private static final String NES_0001_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0001";
	private static final String NES_0002_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0002";
	private static final String NES_0003_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0003";
	private static final String NES_0004_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0004";
	private static final String NES_0005_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0005";
	private static final String NES_0006_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0006";
	private static final String NES_0007_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0007";
	private static final String NES_0008_NAMESPACE = "http://ip.kt.com/interlocking/Nes_IPM_WS_0008";

	@PayloadRoot(localPart = "Nes0001Request", namespace = NES_0001_NAMESPACE)
	public @ResponsePayload Nes0001Response consumeNeoss0001Service(@RequestPayload Nes0001Request nes0001Request){
		Nes0001Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0001Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0001 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0001Service(nes0001Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0001 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0001Response(); 
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
	@PayloadRoot(localPart = "Nes0002Request", namespace = NES_0002_NAMESPACE)
	public @ResponsePayload() Nes0002Response consumeNeoss0002Service(@RequestPayload Nes0002Request nes0002Request){
		Nes0002Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0002Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0002 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0002Service(nes0002Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0002 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0002Response(); 
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
	@PayloadRoot(localPart = "Nes0003Request", namespace = NES_0003_NAMESPACE)
	public @ResponsePayload() Nes0003Response consumeNeoss0003Service(@RequestPayload Nes0003Request nes0003Request){
		Nes0003Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0003Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0003 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0003Service(nes0003Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0003 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0003Response(); 
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
	@PayloadRoot(localPart = "Nes0004Request", namespace = NES_0004_NAMESPACE)
	public @ResponsePayload() Nes0004Response consumeNeoss0004Service(@RequestPayload Nes0004Request nes0004Request){
		Nes0004Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0004Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0004 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0004Service(nes0004Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0004 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0004Response(); 
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
	@PayloadRoot(localPart = "Nes0005Request", namespace = NES_0005_NAMESPACE)
	public @ResponsePayload() Nes0005Response consumeNeoss0005Service(@RequestPayload Nes0005Request nes0005Request){
		Nes0005Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0005Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0005 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0005Service(nes0005Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0005 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0005Response(); 
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
	@PayloadRoot(localPart = "Nes0006Request", namespace = NES_0006_NAMESPACE)
	public @ResponsePayload() Nes0006Response consumeNeoss0006Service(@RequestPayload Nes0006Request nes0006Request){
		Nes0006Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0006Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0006 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0006Service(nes0006Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0006 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0006Response(); 
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
	@PayloadRoot(localPart = "Nes0007Request", namespace = NES_0007_NAMESPACE)
	public @ResponsePayload() Nes0007Response consumeNeoss0007Service(@RequestPayload Nes0007Request nes0007Request){
		Nes0007Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0007Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0007 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0007Service(nes0007Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0007 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0007Response(); 
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
	
	@PayloadRoot(localPart = "Nes0008Request", namespace = NES_0008_NAMESPACE)
	public @ResponsePayload() Nes0008Response consumeNeoss0008Service(@RequestPayload Nes0008Request nes0008Request){
		Nes0008Response stcResponse = null;
		String sRetrunCD="";
		String sReturnMSG ="";
		try{
			if(nes0008Request == null){
				sRetrunCD ="ERR0001";
				sReturnMSG ="필수값이 입력되지 않았습니다.";
				throw new Exception("Nes_IPM_WS_0008 stcRequest Null Pointer Exception");
			}
			stcResponse = neossService.consumeNeoss0008Service(nes0008Request);
			if(stcResponse == null){
				sRetrunCD ="ERR0002";
				sReturnMSG = "연동 실패.";
				throw new Exception("Nes_IPM_WS_0008 stcResponse Null Pointer Exception");
			}
		}catch (ServiceException e){
			stcResponse = new Nes0008Response(); 
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
