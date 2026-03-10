package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.dao.LinkIpmsMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocOrderMstDao;
import com.kt.ipms.legacy.linkmgmt.common.util.ConsumerUtil;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocOrderMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpmsSvcMstvo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.ArrayOfStcIPMSSVCInfoList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.ArrayOfStcReservedIPList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.IPMS0001Request;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.IPMS0002Request;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.IPMS0003Request;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.IPMS0004Request;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.IPMS0005Request;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertFixedIPList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertFixedIPListResponse;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertIPMSSvcInfoList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertIPMSSvcInfoListResponse;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertReservedIPList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.InsertReservedIPListResponse;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.SelectSVCInfo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.SelectSVCInfoResponse;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.SelectSubscFixedIPList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.SelectSubscFixedIPListResponse;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.StcAssignedIPList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.StcIPMSSVCInfoList;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo.StcReservedIPList;

@Component
@Transactional
public class ConsumeService{
	
	/*
	 * NeOSS ERRCODE 정의
	 * SUCCESS 	: 	성공
	 * ER0001  	: 	필수값 부재
	 * ER0002	:	오더정보 없음 (예약 시 오더가 없는 경우)
	 * ER0003	:	원부정보 없음 (확정 시 원부가 없는 경우)
	 * ER999	:	EXCEPTION
	 */

	
@Lazy @Autowired
	private CommonService commonService;
	
	@Autowired
	private ConsumerUtil consumerUtil;
	
	@Lazy @Autowired
	private LinkIpmsMstDao linkIpmsMstDao;
	
	@Lazy @Autowired
	private TbLnkIpAllocMstDao tbLnkIpAllocMstDao;

	@Lazy @Autowired
	private TbLnkIpAllocOrderMstDao tbLnkIpAllocOrderMstDao;

	@Autowired
	private LinkUtil linkUtil;
	/**
	 * @MEthod 	: consumeNes0001
	 * @Date	: 2014. 9. 23.
	 * @Author	: neoargon
	 * @DESC	: IPM_NeS_WS_0001 IPMS insertIpmsSvcInfoList : 상품 정보 연동
	 * @변경이력 	:
	 * @param arrayOfStcIPMSSVCInfoList
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public TbIpAllocNeossMstVo consumeNes0001(String strDate){	
		
		String strCurrentDate = strDate;
		
		if( strCurrentDate == null || strCurrentDate.isEmpty())
		{
			Date date = new Date();
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
		    strCurrentDate = simpleDateFormat.format(date);
		}
		
		//LogKey Vlaue
		String sIfid ="IPM_NeS_WS_0001";
		String inputkey = strCurrentDate;
		String sresultCD ="EXCEPTION";
		String sresultMSG ="";
		TbBatchLogVo tbBatchLogVo = new TbBatchLogVo(); 
		
		//Create Batch Start Vo		
		Random rand = new Random();		
		int nreturn = rand.nextInt(50000)+1;	
		
		tbBatchLogVo.setNpid(BigInteger.valueOf(nreturn));
		tbBatchLogVo.setSifId("IPM_NeS_WS_0001");
		tbBatchLogVo.setSsysNm("NeOSS");
		tbBatchLogVo.setStbNm("tb_ipms_svc_mst");
		tbBatchLogVo.setDstartDt(commonService.selectSysDate());
		tbBatchLogVo.setScomment("IPMS 상품정보 연동 시작");
		tbBatchLogVo.setSstepStatus("IPM_NeS_WS_0001 Batch Start");
		tbBatchLogVo.setSbatchEndYn("N");
		tbBatchLogVo.setDcreateDt(commonService.selectSysDate());
		
//		tbBatchLogVo
		// Create Request Object
		InsertIPMSSvcInfoList stcRequest = new InsertIPMSSvcInfoList();
		IPMS0001Request iPMS0001RequesVo = new IPMS0001Request();
		TbLnkIpmsSvcMstvo requestVo = new TbLnkIpmsSvcMstvo();
		
		// Create Response Object
		InsertIPMSSvcInfoListResponse stcResponse = new InsertIPMSSvcInfoListResponse();
		TbIpAllocNeossMstVo stcReturnVo = new TbIpAllocNeossMstVo();
		List<TbLnkIpmsSvcMstvo> tbLnkIpmsSvcMstListvo = new ArrayList<TbLnkIpmsSvcMstvo>();
		ArrayOfStcIPMSSVCInfoList arrayOfStcIPMSSVCInfoList = new ArrayOfStcIPMSSVCInfoList();
		
		// Set RequsetParam
		requestVo.setCurrentDt(strCurrentDate);
				
		try{
			
			tbLnkIpmsSvcMstListvo = linkIpmsMstDao.selectBatchIPMSSVCMST(requestVo);
			
			for(TbLnkIpmsSvcMstvo foreachVo : tbLnkIpmsSvcMstListvo){
				StcIPMSSVCInfoList tmpVo = new StcIPMSSVCInfoList();
				
				tmpVo.setIPMSSVCSeq(foreachVo.getNipmsSvcSeq().toString());
				tmpVo.setAssignTypeCD(foreachVo.getSassignTypeCd());
				tmpVo.setAssignTypeNM(foreachVo.getSassignTypeNm());
				tmpVo.setEXSVCCD(foreachVo.getSexSvcCd());
				tmpVo.setSVCUSETYPECD(foreachVo.getSsvcUseTypeCd());
				tmpVo.setUSETYPECD(foreachVo.getSuseTypeCd());
				tmpVo.setSVCLINETYPECD(foreachVo.getSsvcLineTypeCd());
				
				arrayOfStcIPMSSVCInfoList.getStcIPMSSVCInfoList().add(tmpVo);
			}
			
			iPMS0001RequesVo.setSvcList(arrayOfStcIPMSSVCInfoList);	
			
			// Call WebService			
			if(iPMS0001RequesVo.getSvcList().getStcIPMSSVCInfoList().size() > 0)
			{			
				stcRequest.setReq(iPMS0001RequesVo);
				
				stcResponse = (InsertIPMSSvcInfoListResponse) this.consumerUtil.getWebServiceTemplate().marshalSendAndReceive(linkUtil.getconsumerURI(sIfid), stcRequest, new SoapActionCallback(linkUtil.getCAllBackURI(sIfid)));
				
				if(stcResponse != null)
				{
					stcReturnVo.setresultcd(linkUtil.getString(stcResponse.getInsertIPMSSvcInfoListResult().getResultCD()));
					stcReturnVo.setresultmsg(linkUtil.getString(stcResponse.getInsertIPMSSvcInfoListResult().getResultMSG()));
					
					sresultCD = stcReturnVo.getresultcd();
					sresultMSG = stcReturnVo.getresultmsg();
					
					if(!CommonCodeUtil.SUCCESS_MSG.equals(stcReturnVo.getresultcd())){
						tbBatchLogVo.setDendDt(commonService.selectSysDate());
						tbBatchLogVo.setDtotalTime(linkUtil.getDateTimeAge(tbBatchLogVo.getDstartDt(), tbBatchLogVo.getDendDt()));
						tbBatchLogVo.setScomment("IPMS 상품정보 연동 종료(Response ERROR)");
						tbBatchLogVo.setSstepStatus(tbBatchLogVo.getSstepStatus()+"\\s\\n "+sresultMSG);
						tbBatchLogVo.setSbatchEndYn("Y");
						tbBatchLogVo.setNcnt(0);
						throw new Exception("NeOSS [ ResultCD : "+stcReturnVo.getresultcd()+" ] [ MSG : "+stcReturnVo.getresultmsg()+" ]");
					}
					else{
						
						tbBatchLogVo.setDendDt(commonService.selectSysDate());
						tbBatchLogVo.setScomment("IPMS 상품정보 연동 종료(OK)");
						tbBatchLogVo.setSstepStatus(tbBatchLogVo.getSstepStatus()+"\\s\\n "+sresultMSG);
						tbBatchLogVo.setSbatchEndYn("Y");
						tbBatchLogVo.setNcnt(iPMS0001RequesVo.getSvcList().getStcIPMSSVCInfoList().size());	
						tbBatchLogVo.setDmodifyDt(commonService.selectSysDate());
						
						linkUtil.insertBatchLog(tbBatchLogVo);
					}
				} else {
					throw new Exception("IPM_NeS_WS_0001: 상품 정보 연동 오류가 발생 하였습니다. NeOSS 서버 이상 입니다");
				}
					
			}
			else{
				tbBatchLogVo.setDendDt(commonService.selectSysDate());				
				tbBatchLogVo.setScomment("IPMS 상품정보 연동 종료(No Data)");
				tbBatchLogVo.setSstepStatus(tbBatchLogVo.getSstepStatus()+"\\s\\n IPMS 상품정보 연동 종료");
				tbBatchLogVo.setSbatchEndYn("Y");
				tbBatchLogVo.setNcnt(0);
				
				linkUtil.insertBatchLog(tbBatchLogVo);
				
				sresultCD = CommonCodeUtil.SUCCESS_MSG;
				sresultMSG = "연동 대상이 없습니다.";
			}
			
		} catch (Exception e) {
			linkUtil.setSystemERR(e);	
			
			tbBatchLogVo.setDendDt(commonService.selectSysDate());				
			tbBatchLogVo.setScomment("IPMS 상품정보 연동  오류 ");
			tbBatchLogVo.setSstepStatus(tbBatchLogVo.getSstepStatus()+"\\s\\n EXCEPTION \\s\\n IPMS 상품정보 연동 종료");
			tbBatchLogVo.setSbatchEndYn("Y");
			tbBatchLogVo.setNcnt(iPMS0001RequesVo.getSvcList().getStcIPMSSVCInfoList().size());
			tbBatchLogVo.setDmodifyDt(commonService.selectSysDate());
			
			linkUtil.insertBatchLog(tbBatchLogVo);
		}
		finally{
			linkUtil.setSystemlog(sIfid, inputkey, sresultCD, sresultMSG);
		}
		return stcReturnVo;
	}
	
	/**
	 * @MEthod 	: consumeNes0002
	 * @Date	: 2014. 9. 23.
	 * @Author	: neoargon
	 * @DESC	: IPM_NeS_WS_0002 insertReservedIPList : Block 예약 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean consumeNes0002(TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo){
		
		//LogKey Vlaue
		String sIfid ="IPM_NeS_WS_0002";		
		String inputkey ="";
		String sresultCD ="EXCEPTION";
		String sresultMSG ="";
		
		// Create Request Object	
		InsertReservedIPList stcRequest = new InsertReservedIPList();
		IPMS0002Request iPMS0002Request = new IPMS0002Request();
		ArrayOfStcReservedIPList  reQuestVo = new ArrayOfStcReservedIPList();	
		
		// Create Response Object
		InsertReservedIPListResponse stcResponse = new InsertReservedIPListResponse();
		
		// Create Return Value
		boolean bSuccess = false;		
		
		//Exception returnValue
		String sExceptionCD ="LNK.HIGH.00032";
		String sExceptionMSG = "";
		
		// Call WebService
		try{	
			
			if(tbIpAllocNeossMstListVo != null && tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos() != null && tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().size() > 0 
					&& (!tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getssvcLineTypeCd().equalsIgnoreCase("CL0001") && !tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getssvcLineTypeCd().equalsIgnoreCase("CL0002"))){
				sExceptionCD="LNK.WARN.00047";
				sExceptionMSG="IP 블럭에 대한 예약은 KORNET과 PREMIUM";
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			}
			
			// Set Requset Param
			if(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().size() > 0){
				if(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getRegyn().equals("Y"))
					sExceptionMSG  = "NeOSS IP Block 예약 등록 정보 연동";
				else
					sExceptionMSG  = "NeOSS IP Block 예약 취소 정보 연동";
				
				inputkey = "[ SAID : " + tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getSaid() +" ] [ TYPE :  "+ sExceptionMSG+" ]";				
			}
			
			for(TbIpAllocNeossMstVo foreachVo :  tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
				StcReservedIPList tmpVo = new StcReservedIPList();
				
				iPMS0002Request.setOdrNum(foreachVo.getSordernum());
				iPMS0002Request.setSaid(foreachVo.getSaid());
				iPMS0002Request.setRegYN(foreachVo.getRegyn());
			
				tmpVo.setIPMSSVCSeq(foreachVo.getIpmsSvcSeq().toString());
				tmpVo.setAssignTypeCD(foreachVo.getAssignTypeCd());
				tmpVo.setEXSVCCD(foreachVo.getExSvcCd());
				tmpVo.setSVCUSETYPECD(foreachVo.getSvcUseTypeCd());
				tmpVo.setIPVERSIONTYPECD(foreachVo.getIpVersionTypeCd());
				tmpVo.setIPCREATETYPECD(foreachVo.getIpCreateTypeCd());
				tmpVo.setIpBlock(foreachVo.getIpBlock());
				tmpVo.setIPBitmask(foreachVo.getIpbitmask().toString());
				tmpVo.setSIpAddr(foreachVo.getSipaddr());
				tmpVo.setEIpAddr(foreachVo.getEipaddr());
				tmpVo.setIPASSIGNSEQ(foreachVo.getNipAssignMstSeq().toString());
				tmpVo.setGatewayIP(foreachVo.getGatewayip());	
				tmpVo.setLACPBLOCKYN(foreachVo.getSlacpBlockYN());
				tmpVo.setSVCLINETYPECD(foreachVo.getssvcLineTypeCd());
				foreachVo.setSexPushYn("Y");				
				foreachVo.setScreateId("NeOSS");
				
				reQuestVo.getStcReservedIPList().add(tmpVo);
			}
			
			iPMS0002Request.setIPList(reQuestVo);
			
			stcRequest.setReq(iPMS0002Request);
			PrintLogUtil.printLogInfo("##### NeOSS ALLOCATION CONSUMER LOG : IPM_NeS_WS_0002 ###");
			PrintLogUtil.printLogInfoValueObject(iPMS0002Request.getIPList().getStcReservedIPList());
			stcResponse = (InsertReservedIPListResponse) this.consumerUtil.getWebServiceTemplate().marshalSendAndReceive(linkUtil.getconsumerURI(sIfid), stcRequest, new SoapActionCallback(linkUtil.getCAllBackURI(sIfid)));
			
			if(stcResponse != null){
				sresultCD = stcResponse.getInsertReservedIPListResult().getResultCD();
				sresultMSG = stcResponse.getInsertReservedIPListResult().getResultMSG();
						
				for(TbIpAllocNeossMstVo tmpResultVo :  tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
					tmpResultVo.setresultcd(stcResponse.getInsertReservedIPListResult().getResultCD());
					tmpResultVo.setresultmsg(stcResponse.getInsertReservedIPListResult().getResultMSG());
				}
				
				if(CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getInsertReservedIPListResult().getResultCD().toUpperCase())){
					bSuccess = true;
					this.updateNeossUIODR(tbIpAllocNeossMstListVo);
				}else if(stcResponse.getInsertReservedIPListResult().getResultCD().equals("ER001")){
					bSuccess = false;
					sExceptionCD ="LNK.INFO.00002";
					throw new ServiceException(sExceptionCD);
				}else if(stcResponse.getInsertReservedIPListResult().getResultCD().equals("ER002")){
					bSuccess = false;
					sExceptionCD ="LNK.HIGH.00003";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});			
				}else if(stcResponse.getInsertReservedIPListResult().getResultCD().equals("ER003")){
					bSuccess = false;
					sExceptionCD ="LNK.HIGH.00004";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				else{
					bSuccess = false;			
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
			} else {
				bSuccess = false;				
				sExceptionMSG="IPM_NeS_WS_0002 : Block 예약 정보 연동에 오류가 발생하였습니다. NeOSS 서버 이상 입니다";
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {	
				linkUtil.setSystemERR(e);
				sresultMSG = e.toString();
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}
		finally{
			if(!bSuccess){				
				for(TbIpAllocNeossMstVo tmpResultVo :  tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
					tmpResultVo.setresultcd("ERROR");
					tmpResultVo.setresultmsg(sresultMSG);
				}
			}
			
			linkUtil.setSystemlog(sIfid, inputkey, sresultCD, sresultMSG);			
			this.setIpAllocDBListLog(tbIpAllocNeossMstListVo);
		}
		
		// Check ResponseParam		
		return bSuccess;		
	}
	
	/**
	 * @MEthod 	: consumeNes0003
	 * @Date	: 2014. 9. 23.
	 * @Author	: neoargon
	 * @DESC	: IPM_NeS_WS_0003 insertFixIPList : IP Block 확정 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean consumeNes0003(TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo){
			
		//LogKey Vlaue
		String sIfid ="IPM_NeS_WS_0003";		
		String inputkey ="";
		String sresultCD ="EXCEPTION";
		String sresultMSG ="";
		boolean baccess = true;
		
		TbIpAllocNeossMstVo svcMstCheckVo = new TbIpAllocNeossMstVo();
		
		// Create Request Object
		InsertFixedIPList stcRequest = new InsertFixedIPList();
		IPMS0003Request iPMS0003RequestVo = new IPMS0003Request();
		ArrayOfStcReservedIPList reQuestVo = new ArrayOfStcReservedIPList();
		
		// Create Response Object
		InsertFixedIPListResponse stcResponse = new InsertFixedIPListResponse();
		
		// Create Return Value
		boolean bSuccess = false;
		String sExceptionCD ="LNK.HIGH.00032";
		String sExceptionMSG = "";
		
		// Call WebService
		try{
			
			if(tbIpAllocNeossMstListVo != null && tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos() != null && tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().size() > 0 
					&& (!tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getssvcLineTypeCd().equalsIgnoreCase("CL0001") 
						&& !tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getssvcLineTypeCd().equalsIgnoreCase("CL0002")
						&& !tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getssvcLineTypeCd().equalsIgnoreCase("CL0005"))){
				sExceptionCD="LNK.WARN.00047";
				sExceptionMSG="IP 블럭에 대한 확정은 KORNET과 PREMIUM과 VPN";
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			}
			
			if(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().size() > 0){
				if(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getRegyn().equals("Y"))
					sExceptionMSG  = "NeOSS IP Block 확정 등록 정보 연동";
				else
					sExceptionMSG  = "NeOSS IP Block 확정 취소 정보 연동";
				
				inputkey = "[ SAID : " + tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getSaid() +" ] [ TYPE :  "+ sExceptionMSG+" ]";
			}
			
			if(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0).getRegyn().equals("N"))
			{
				svcMstCheckVo = this.consumeNes0005(tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos().get(0));
				if(svcMstCheckVo != null && StringUtils.hasText(svcMstCheckVo.getSaid()))
					baccess = true;
				else
					baccess = false;
			}
			else
				baccess= true;
						
			// Set Requset Param
			if(baccess)
			{
				for(TbIpAllocNeossMstVo foreachVo : tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
					StcReservedIPList tmpVo = new StcReservedIPList();
					
					iPMS0003RequestVo.setSaid(foreachVo.getSaid());
					iPMS0003RequestVo.setRegYN(foreachVo.getRegyn());
				
					tmpVo.setIPMSSVCSeq(foreachVo.getIpmsSvcSeq().toString());
					tmpVo.setAssignTypeCD(foreachVo.getAssignTypeCd());
					tmpVo.setEXSVCCD(foreachVo.getExSvcCd());
					tmpVo.setSVCUSETYPECD(foreachVo.getSvcUseTypeCd());
					tmpVo.setIPVERSIONTYPECD(foreachVo.getIpVersionTypeCd());
					tmpVo.setIPCREATETYPECD(foreachVo.getIpCreateTypeCd());
					tmpVo.setIpBlock(foreachVo.getIpBlock());
					tmpVo.setIPBitmask(foreachVo.getIpbitmask().toString());
					tmpVo.setSIpAddr(foreachVo.getSipaddr());
					tmpVo.setEIpAddr(foreachVo.getEipaddr());
					tmpVo.setIPASSIGNSEQ(foreachVo.getNipAssignMstSeq().toString());
					tmpVo.setGatewayIP(foreachVo.getGatewayip());
					tmpVo.setLACPBLOCKYN(foreachVo.getSlacpBlockYN());
					tmpVo.setSVCLINETYPECD(foreachVo.getssvcLineTypeCd());
					foreachVo.setSexPushYn("Y");				
					foreachVo.setScreateId("NeOSS");
					
					reQuestVo.getStcReservedIPList().add(tmpVo);
				}
				iPMS0003RequestVo.setIPList(reQuestVo);
				
				stcRequest.setReq(iPMS0003RequestVo);
				
				PrintLogUtil.printLogInfo("##### NeOSS ALLOCATION CONSUMER LOG 222: IPM_NeS_WS_0003 ###");
				PrintLogUtil.printLogInfoValueObject(iPMS0003RequestVo.getIPList().getStcReservedIPList());
				stcResponse = (InsertFixedIPListResponse) this.consumerUtil.getWebServiceTemplate().marshalSendAndReceive(linkUtil.getconsumerURI(sIfid), stcRequest, new SoapActionCallback(linkUtil.getCAllBackURI(sIfid)));
				if(stcResponse != null){
					for(TbIpAllocNeossMstVo tmpResultVo :  tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
						tmpResultVo.setresultcd(stcResponse.getInsertFixedIPListResult().getResultCD());
						tmpResultVo.setresultmsg(stcResponse.getInsertFixedIPListResult().getResultMSG());				
					}			
					
					sresultCD =stcResponse.getInsertFixedIPListResult().getResultCD();
					sresultMSG =stcResponse.getInsertFixedIPListResult().getResultMSG();
					
					if(CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getInsertFixedIPListResult().getResultCD().toUpperCase())){
						bSuccess = true;
					}else if(stcResponse.getInsertFixedIPListResult().getResultCD().equals("ER001")){
						bSuccess = false;
						sExceptionCD ="LNK.INFO.00002";
						throw new ServiceException(sExceptionCD);
					}else if(stcResponse.getInsertFixedIPListResult().getResultCD().equals("ER002")){
						bSuccess = false;
						sExceptionCD ="LNK.HIGH.00003";
						throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});	
					}else if(stcResponse.getInsertFixedIPListResult().getResultCD().equals("ER003")){
						bSuccess = false;
						sExceptionCD ="LNK.HIGH.00004";
						throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
					}
					else{
						bSuccess = false;				
						throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
					}
				}else {
					bSuccess = false;
					sExceptionMSG="IPM_NeS_WS_0003 IP Block 확정 정보 연동 오류가 발생하였습니다. NeOSS 서버 이상 입니다.";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			sresultMSG = e.toString();			
			throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}
		finally{
			if(!bSuccess){
				for(TbIpAllocNeossMstVo tmpResultVo :  tbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos()){
					tmpResultVo.setresultcd("ERROR");
					tmpResultVo.setresultmsg(sresultMSG);
				}
			}
			linkUtil.setSystemlog(sIfid, inputkey, sresultCD, sresultMSG);
			this.setIpAllocDBListLog(tbIpAllocNeossMstListVo);
		}		
		// Check ResponseParam		
		return bSuccess;		
	}

	/**
	 * @MEthod 	: consumeNes0004
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	: IPM_NeS_WS_0004  selectFixedIPList : NeOSS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAllocNeossMstListVo consumeNes0004(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		//LogKey Vlaue
		String sIfid ="IPM_NeS_WS_0004";		
		StringBuffer inputkey = new StringBuffer("");
		String sresultCD ="EXCEPTION";
		String sresultMSG ="";
	
		// Create Request Object
		SelectSubscFixedIPList stcRequest = new SelectSubscFixedIPList();
		IPMS0004Request iPMS0004RequestVo = new IPMS0004Request();
		
		// Create Response Object
		SelectSubscFixedIPListResponse stcResponse = new SelectSubscFixedIPListResponse();
		
		// Create Return Value
		TbIpAllocNeossMstListVo returnListVo = new TbIpAllocNeossMstListVo();	
		returnListVo.setTbIpAllocNeossMstVos(new ArrayList<TbIpAllocNeossMstVo>());	//연동 처리 관련
		String sExceptionCode = "LNK.HIGH.00032";
		
		// Call WebService
		try{
			// Set RequsetParam
			if(!tbIpAllocNeossMstVo.getSaid().isEmpty()){
				iPMS0004RequestVo.setSaid(tbIpAllocNeossMstVo.getSaid());
				inputkey.append("[ SAID : ");
				inputkey.append(tbIpAllocNeossMstVo.getSaid());
				inputkey.append(" ]");
			}
			else if(!tbIpAllocNeossMstVo.getLlnum().isEmpty()){
				iPMS0004RequestVo.setLLNum(tbIpAllocNeossMstVo.getLlnum());
				inputkey.append("[ LLNUM : ");
				inputkey.append(tbIpAllocNeossMstVo.getLlnum());
				inputkey.append(" ]");
			}
			else if(!tbIpAllocNeossMstVo.getSubscnescode().isEmpty()){
				iPMS0004RequestVo.setNescode(tbIpAllocNeossMstVo.getSubscnescode());
				inputkey.append("[ NESCODE : ");
				inputkey.append(tbIpAllocNeossMstVo.getSubscnescode());
				inputkey.append(" ]");
			}else if(!tbIpAllocNeossMstVo.getSipaddr().isEmpty()){
				iPMS0004RequestVo.setIPAddress(tbIpAllocNeossMstVo.getSipaddr());
				inputkey.append("[ IP : ");
				inputkey.append(tbIpAllocNeossMstVo.getSipaddr());
				inputkey.append(" ]");
			}
			
			iPMS0004RequestVo.setIPVERSIONTYPECD(tbIpAllocNeossMstVo.getIpVersionTypeCd());
			
			stcRequest.setReq(iPMS0004RequestVo);
			
			inputkey.append(" [ IPVersion : ");
			inputkey.append(tbIpAllocNeossMstVo.getIpVersionTypeCd());
			inputkey.append(" ]");
			
			stcResponse = (SelectSubscFixedIPListResponse) this.consumerUtil.getWebServiceTemplate().marshalSendAndReceive(linkUtil.getconsumerURI(sIfid), stcRequest, new SoapActionCallback(linkUtil.getCAllBackURI(sIfid)));
			
			if(stcResponse != null){
				if(CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getSelectSubscFixedIPListResult().getResultCD().toUpperCase()))
				{
					for(StcAssignedIPList foreachVo :  stcResponse.getSelectSubscFixedIPListResult().getIpList().getStcAssignedIPList()){
						TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
						
						tmpVo.setresultcd(stcResponse.getSelectSubscFixedIPListResult().getResultCD());
						tmpVo.setresultmsg(stcResponse.getSelectSubscFixedIPListResult().getResultMSG());
						tmpVo.setSaid(stcResponse.getSelectSubscFixedIPListResult().getSvcInfo().getSaid());
						tmpVo.setLlnum(stcResponse.getSelectSubscFixedIPListResult().getSvcInfo().getLlnum());
						tmpVo.setIcisofficescode(stcResponse.getSelectSubscFixedIPListResult().getSvcInfo().getIcisofficescode());
						tmpVo.setSvcUseTypeCd(stcResponse.getSelectSubscFixedIPListResult().getSvcInfo().getSVCUSETYPECD());
						tmpVo.setSubscnescode(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubscnescode());
						tmpVo.setSubscmstip(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubscmstip());
						tmpVo.setSubsclgipportseq(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubsclgipportseq());
						tmpVo.setSubsclgipportdescription(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubsclgipportdescription());
						tmpVo.setSubsclgipportip(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubsclgipportip());
						tmpVo.setSubscrouterserialip(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubscrouterserialip());
						tmpVo.setSubscnealias(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getSubscnealias());
						tmpVo.setConnalias(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getConnalias());
						tmpVo.setModelname(stcResponse.getSelectSubscFixedIPListResult().getNeInfo().getModelName());
						tmpVo.setIpmsSvcSeq(linkUtil.getBIntFromString(foreachVo.getIPMSSVCSeq()));
						tmpVo.setAssignTypeCd(foreachVo.getAssignTypeCD());
						tmpVo.setExSvcCd(foreachVo.getEXSVCCD());
						tmpVo.setIpVersionTypeCd(foreachVo.getIPVERSIONTYPECD());
						tmpVo.setIpCreateTypeCd(foreachVo.getIPCREATETYPECD());
						tmpVo.setIpBlock(foreachVo.getIpBlock());
						tmpVo.setIpbitmask(linkUtil.getIntgerFromString(foreachVo.getIPBitmask()));
						tmpVo.setSipaddr(foreachVo.getSIpAddr());
						tmpVo.setEipaddr(foreachVo.getEIpAddr());
						tmpVo.setGatewayip(foreachVo.getGatewayIP());
						tmpVo.setSlacpBlockYN(foreachVo.getSlacpBlockYN());
						
						returnListVo.getTbIpAllocNeossMstVos().add(tmpVo);
					}
				}
				else
				{
					tbIpAllocNeossMstVo.setresultcd(stcResponse.getSelectSubscFixedIPListResult().getResultCD());
					tbIpAllocNeossMstVo.setresultmsg(stcResponse.getSelectSubscFixedIPListResult().getResultMSG());
					
					returnListVo.getTbIpAllocNeossMstVos().add(tbIpAllocNeossMstVo);
				}
				
				sresultCD = stcResponse.getSelectSubscFixedIPListResult().getResultCD();
				sresultMSG = stcResponse.getSelectSubscFixedIPListResult().getResultMSG();
				
				if(!CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getSelectSubscFixedIPListResult().getResultCD().toUpperCase())){
					throw new ServiceException(sExceptionCode, new String[]{" NeOSS IP 할당 정보 조회."});
				}
			}else {
							
				throw new ServiceException(sExceptionCode, new String[]{" NeOSS IP 할당 정보 조회."});
			}
		
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			sresultMSG = e.toString();
			throw new ServiceException(sExceptionCode, new String[]{"NeOSS IP 할당 정보 조회"});
		}
		finally{			
			linkUtil.setSystemlog(sIfid, inputkey.toString(), sresultCD, sresultMSG);
		}
		
		
		// Check ResponseParam		
		return returnListVo;		
	}
	
	/**
	 * @MEthod 	: consumeNes0005
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	: IPM_NeS_WS_0005 selectSvcInfo : NeOSS 회선 정보 조회
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAllocNeossMstVo consumeNes0005(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		//LogKey Vlaue
		String sIfid ="IPM_NeS_WS_0005";		
		String inputkey ="";
		String sresultCD ="EXCEPTION";
		String sresultMSG ="";

		// Create Request Object
		SelectSVCInfo stcRequest = new SelectSVCInfo();
		IPMS0005Request iPMS0005RequestVo = new IPMS0005Request();
		
		// Create Response Object
		SelectSVCInfoResponse stcResponse = new SelectSVCInfoResponse();
		
		// Create Return Value
		TbIpAllocNeossMstVo returnVo = new TbIpAllocNeossMstVo();
		String sExceptionCode = "LNK.HIGH.00032";
				
		// Set Requset Param
		if(!tbIpAllocNeossMstVo.getSaid().isEmpty()){
			iPMS0005RequestVo.setSaid(tbIpAllocNeossMstVo.getSaid());
			inputkey = "[ Said :"+tbIpAllocNeossMstVo.getSaid()+" ]";
		}
		else if(!tbIpAllocNeossMstVo.getLlnum().isEmpty()){
			iPMS0005RequestVo.setLLNum(tbIpAllocNeossMstVo.getLlnum());
			inputkey = "[ LLNum :"+tbIpAllocNeossMstVo.getLlnum()+" ]";
		}
		
		stcRequest.setReq(iPMS0005RequestVo);
		
		// Call WebService
		try{
			PrintLogUtil.printLogInfo("##### NeOSS ALLOCATION CONSUMER LOG : IPM_NeS_WS_0005 ###");
			PrintLogUtil.printLogInfoValueObject(iPMS0005RequestVo);
			stcResponse = (SelectSVCInfoResponse) this.consumerUtil.getWebServiceTemplate().marshalSendAndReceive(linkUtil.getconsumerURI(sIfid), stcRequest, new SoapActionCallback(linkUtil.getCAllBackURI(sIfid)));
			
			if(stcResponse != null && stcResponse.getSelectSVCInfoResult() != null ){
				if(stcResponse.getSelectSVCInfoResult().getResultCD()!= null && StringUtils.hasText(stcResponse.getSelectSVCInfoResult().getResultCD()) &&  CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getSelectSVCInfoResult().getResultCD().toUpperCase())){ // 
					returnVo.setSaid(stcResponse.getSelectSVCInfoResult().getSvcInfo().getSaid());
					returnVo.setLlnum(stcResponse.getSelectSVCInfoResult().getSvcInfo().getLlnum());
					returnVo.setIcisofficescode(stcResponse.getSelectSVCInfoResult().getSvcInfo().getIcisofficescode());
					returnVo.setCustname(stcResponse.getSelectSVCInfoResult().getSvcInfo().getCustName());
					returnVo.setInstalladdress(stcResponse.getSelectSVCInfoResult().getSvcInfo().getAddr());
					returnVo.setContactNum(stcResponse.getSelectSVCInfoResult().getSvcInfo().getContactNum());
					returnVo.setEmail(stcResponse.getSelectSVCInfoResult().getSvcInfo().getEmail());
					returnVo.setExSvcCd(stcResponse.getSelectSVCInfoResult().getSvcInfo().getEXSVCCD());
					
					sresultCD =stcResponse.getSelectSVCInfoResult().getResultCD();
					sresultMSG =stcResponse.getSelectSVCInfoResult().getResultMSG();					
					
				} else {
					sresultCD ="No Data";
					sresultMSG="No Return Data";			
				}
				
				if(StringUtils.hasText(stcResponse.getSelectSVCInfoResult().getResultCD()) && !CommonCodeUtil.SUCCESS_MSG.equals(stcResponse.getSelectSVCInfoResult().getResultCD().toUpperCase())){				
					throw new ServiceException(sExceptionCode, new String[]{"NeOSS 회선 정보 조회"});
				}
			}
		
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			sresultMSG = e.toString();
			throw new ServiceException(sExceptionCode, new String[]{"NeOSS 회선 정보 조회"});
		}
		finally{			
			linkUtil.setSystemlog(sIfid, inputkey, sresultCD, sresultMSG);
		}		

		return returnVo;		
	}
	
	/**
	 * @Method	:	updateNeossUIODR
	 * @Date	:	2014. 10. 27.
	 * @Author	:	neoargon
	 * @DESC	:	NeOSS ODR UI용  Update
	 * @변경이력	:
	 * @param tbLnkIpAllocOrderMstVo void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int updateNeossUIODR(TbIpAllocNeossMstListVo tbIpAllocNeossMstVoList){
		
		int nreturn=0;
		int nallocCnt =0;
		int nreqCnt =0;
		
		TbLnkIpAllocOrderMstVo updatevo = new TbLnkIpAllocOrderMstVo();
		TbLnkIpAllocOrderMstVo selectvo = new TbLnkIpAllocOrderMstVo();
		TbLnkIpAllocMstVo tbLnkIpAllocMstVo = new TbLnkIpAllocMstVo();
		
		try{
			
			if(tbIpAllocNeossMstVoList != null && tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().size() > 0 &&  linkUtil.getString(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getSordernum()).length() > 0 ){
				
				updatevo.setSordernum(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getSordernum());
				updatevo.setSsaid(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getSaid());
				
					selectvo = tbLnkIpAllocOrderMstDao.selectTbLnkIpAllocOrderMstVo(updatevo);
				
				if(selectvo != null && selectvo.getNipAllocOrderSeq() != null)
				{
					
					if(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getRegyn().equalsIgnoreCase("Y"))
					{
						updatevo.setNipAllocOrderSeq(selectvo.getNipAllocOrderSeq());
						updatevo.setBflagStatus("C");
						updatevo.setDmodifyDt(commonService.selectSysDate());
						updatevo.setSmodifyId(linkUtil.getString(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getWorkerid()));
					}
					else if(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getRegyn().equalsIgnoreCase("N")){
						updatevo.setNipAllocOrderSeq(selectvo.getNipAllocOrderSeq());
						tbLnkIpAllocMstVo.setSsaid(tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().get(0).getSaid());
						nreqCnt = tbIpAllocNeossMstVoList.getTbIpAllocNeossMstVos().size();
						nallocCnt = tbLnkIpAllocMstDao.selectCountTbLnkIpAllocMstVo(tbLnkIpAllocMstVo);
						if((nallocCnt-nreqCnt) < 1)
							updatevo.setBflagStatus("R");						
					}
					
					tbLnkIpAllocOrderMstDao.updateTbLnkIpAllocOrderMstVo(updatevo);
				}
			}
		}catch(ServiceException e){			
			nreturn=1;
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			nreturn=1;
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return nreturn;
	}
	
	/**
	 * @MEthod 	: setIpAllocDBListLog
	 * @Date	: 2014. 9. 27.
	 * @Author	: neoargon
	 * @DESC	: DB Log Insert
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstListVo
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void setIpAllocDBListLog(TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo){
		linkUtil.setIpAllocDBListLog(tbIpAllocNeossMstListVo);	
	}
}
