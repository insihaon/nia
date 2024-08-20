package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbIpAllocNeossMstDao;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAlloLinkOperMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpmsSvcMstvo;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0001IPMSSVCInfo;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0001Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0001Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0002Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0002Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003IpSuggestList;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0004Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0004ReservedIP;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0004Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0005AssingdIp;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0005Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0005Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006AssignedIP;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008VPNAssingdIp;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0008VPNHostAssignedIPList;

/**
 * @FileName 	: NeossService.java
 * @Project  	: KT_IPMS
 * @Package  	: com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.service
 * @Date 		: 2014. 9. 12.
 * @Description :
 */
@Component
@Transactional
public class NeossService{
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private NeosstxService neosstxService;
		
	@Autowired
	private TbIpAllocNeossMstDao tbIpAllocNeossMstDao;	

	@Autowired
	private LinkUtil linkUtil;
	
	/*성공메시지*/
	private final static String IFSUCESSCD =CommonCodeUtil.SUCCESS_MSG;
	private final static String IFSUCESSMSG =CommonCodeUtil.SUCCESS_MSG;
	
	/*Validation : 필수값입력 부재*/
	private final static  String IFINPUTERR_CD ="ERR0001";
	private final static  String IFINPUTERR_MSG ="필수값이 입력되지 않았습니다.";
	/*Validation : 연동 싷패*/
	private final static String IFFAILERR_CD ="ERR0002";
	private final static String IFFAILERR_MSG ="연동 실패.";
	
	/*Validation : 미처리 오더 정보 존재*/
	private final static String IFDUAPERR_CD ="ERR0003";
	private final static String IFDUAPERR_MSG ="미처리 오더 정보 존재.";
	
	/*Validation : 선행 오더 미존재*/
//	private final static  String IFODRERR_CD ="ERR0004";
//	private final static String IFODRERR_MSG ="선행 오더 미존재.";
	
	/*Validation : 지원 가능한 Bitmask가 넘었습니다.*/
	private final static  String IFBITOVER_CD ="ERR0005";
	private final static  String IFBITOVER_MSG ="지원 가능하지 않은 Bitmask가 입력되었습니다.";
	
	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFMISODR_CD ="ERR0006";
	private final static String IFMISODR_MSG ="미정의 된 종료 유형 입니다.";
	
	/*Exception*/
	private final static String IFEXCEPTION_CD ="ERR9999";
	
	/* 2021.05.11 추가 상세 결과 코드 추가 */
	private final static String IFSC00001_CD = "SC00001";
	private final static String IFSC00001_MSG = "정상 할당되었으나, 확인필요(IPMS정보와 라우팅정보 불일치 IP블록)";

	private final static String IFSC00002_CD = "SC00002";
	private final static String IFSC00002_MSG = "정상 할당되었으나, 확인필요(중계/PE에 Summary 설정 내역 없음)";

	private final static String IFSC00003_CD = "SC00003";
	private final static String IFSC00003_MSG = "정상 할당되었으나, 확인필요(타사 또는 타노드와 라우팅 중복됨)";

	private final static String IFSC00004_CD = "SC00004";
	private final static String IFSC00004_MSG = "정상 할당되었으나, 확인필요(라우팅정보 불일치, Summary 누락)";

	private final static String IFSC00005_CD = "SC00005";
	private final static String IFSC00005_MSG = "정상 할당되었으나, 확인필요(라우팅정보 불일치, 타사/타노드 라우팅 중복)";

	private final static String IFSC00006_CD = "SC00006";
	private final static String IFSC00006_MSG = "정상 할당되었으나, 확인필요(타사/타노드 라우팅 중복, Summary 누락)";

	private final static String IFSC00007_CD = "SC00007";
	private final static String IFSC00007_MSG = "정상 할당되었으나, 확인필요(라우팅정보 불일치, 타사/타노드 라우팅 중복, Summary 누락)";

	private final static String IFSC00008_CD = "SC00008";
	private final static String IFSC00008_MSG = "정상 할당되었으나, 일부 확인필요(라우팅정보 불일치, 타사/타노드 라우팅 중복, Summary 누락)";
		
	/**
	 * @MEthod 	: consumeNeoss0001Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0001 selectIpmsSvcInfo(): IPMS 상품정보 조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */	
	@Transactional(readOnly = true)
	public Nes0001Response consumeNeoss0001Service(Nes0001Request stcRequest) {

		/*LogKey*/
		String inputkey = "";
		
		TbLnkIpmsSvcMstvo requestVo = new TbLnkIpmsSvcMstvo();
		
		// Create Return Object
		Nes0001Response stcResponse = new Nes0001Response();	
		List<Nes0001IPMSSVCInfo> stcipmsSvcInfoList = new ArrayList<Nes0001IPMSSVCInfo>();
		List<TbLnkIpmsSvcMstvo> tbLnkIpmsSvcMstvo = null;
		
		stcResponse.setResultCD(IFFAILERR_CD);
		stcResponse.setResultMSG(IFFAILERR_MSG);
		
		// Check inputParam
		if (linkUtil.getString(stcRequest.getEXSVCCD()).isEmpty() 
				|| linkUtil.getString(stcRequest.getSVCUSETYPECD()).isEmpty()) {
			
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		}
		else
		{			
			try{
				requestVo.setSexSvcCd(stcRequest.getEXSVCCD());
				requestVo.setSsvcUseTypeCd(stcRequest.getSVCUSETYPECD());
				
				inputkey = "EXSVCCD : "+ stcRequest.getEXSVCCD()+" /SVCUSETYPECD : "+ stcRequest.getSVCUSETYPECD();
				
				tbLnkIpmsSvcMstvo = neosstxService.selectIpmsSvcInfo(requestVo);
				
				for(TbLnkIpmsSvcMstvo foreachVo : tbLnkIpmsSvcMstvo){
					Nes0001IPMSSVCInfo tmpVo = new Nes0001IPMSSVCInfo();
					
					tmpVo.setIPMSSVCSeq(linkUtil.getString(foreachVo.getNipmsSvcSeq().toString()));
					tmpVo.setAssignTypeCD(foreachVo.getSassignTypeCd());
					tmpVo.setAssignTypeNM(foreachVo.getSassignTypeNm());
					tmpVo.setEXSVCCD(foreachVo.getSexSvcCd());
					tmpVo.setSVCUSETYPECD(foreachVo.getSsvcUseTypeCd());
					tmpVo.setUSETYPECD(foreachVo.getSuseTypeCd());
					
					stcipmsSvcInfoList.add(tmpVo);
				}
				
				stcResponse.setStcIPMSSVCInfoList(stcipmsSvcInfoList);
				
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSMSG);
				
			} catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				stcResponse.setResultMSG(e.toString());	
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging
				linkUtil.setSystemlog("Nes_IPM_WS_0001",inputkey ,stcResponse.getResultCD(), stcResponse.getResultMSG());
			}		
		}
		return stcResponse;
	}
			
	/**
	 * @MEthod 	: consumeNeoss0002Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0002 insertNeOSSOdr(): ODR 정보 생성
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 * @throws Exception
	 */	
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0002Response consumeNeoss0002Service(Nes0002Request stcRequest){

		String inputkey = "";
		int nresult=0;
		
		TbIpAllocNeossMstVo defaultMstVo = new TbIpAllocNeossMstVo();	
		
		// Create Return Object
		Nes0002Response stcResponse = new Nes0002Response();  
		
		stcResponse.setResultCD(IFFAILERR_CD);
		stcResponse.setResultMSG(IFFAILERR_MSG);
		
		// Check inputParam
		if ( linkUtil.getString(stcRequest.getOdrNum()).isEmpty()
				|| stcRequest.getStcSVCInfo() == null
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getSaid()).isEmpty() 
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getNodeOfficescode()).isEmpty()
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getEXSVCCD()).isEmpty()
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getSVCUSETYPECD()).isEmpty()
				){
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		}
		else{
			// Business Service[ Create NeOSS ODR ]
			try{
				PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0002==================");

				inputkey =" ODRNUM : " + stcRequest.getOdrNum() + " / SAID : "+ stcRequest.getStcSVCInfo().getSaid();
				
				/*set Input Data*/
				defaultMstVo.setSordernum(stcRequest.getOdrNum());
				defaultMstVo.setSaid(stcRequest.getStcSVCInfo().getSaid());				
				
				//미처리 오더 정보 확인
				TbIpAllocNeossMstVo tmpMstVo = tbIpAllocNeossMstDao.selectDefaultOdrAllocNeossMstVo(defaultMstVo);
				
				defaultMstVo.setOdrregdt(stcRequest.getOdrRegDT());
				defaultMstVo.setOdrhopedt(stcRequest.getOdrHopeDT());
				defaultMstVo.setLlnum(stcRequest.getStcSVCInfo().getLLNum());
				defaultMstVo.setIcisofficescode(stcRequest.getStcSVCInfo().getNodeOfficescode());
				defaultMstVo.setSofficecode(stcRequest.getStcSVCInfo().getICISOfficescode());
				defaultMstVo.setSvcUseTypeCd(stcRequest.getStcSVCInfo().getSVCUSETYPECD());					
				defaultMstVo.setExSvcCd(stcRequest.getStcSVCInfo().getEXSVCCD());
				defaultMstVo.setInstalladdress(stcRequest.getStcSVCInfo().getInstallAddress());
				defaultMstVo.setIpVersionTypeCd(stcRequest.getStcSVCInfo().getIPVERSIONTYPECD());
				defaultMstVo.setInstallroadaddress(stcRequest.getStcSVCInfo().getInstallRoadAddress());
				defaultMstVo.setCustname(stcRequest.getCustname());
				defaultMstVo.setreportopinion(stcRequest.getReportopinion());
				defaultMstVo.setSspeeddesc(stcRequest.getStcSVCInfo().getSPEEDDESC());
				defaultMstVo.setDcreateDt(commonService.selectSysDate());
				defaultMstVo.setSexPushYn("N");				
				defaultMstVo.setScreateId("NeOSS WS ODR Create");
				defaultMstVo.setresultcd(IFSUCESSCD);
				defaultMstVo.setresultmsg(IFSUCESSMSG);

				PrintLogUtil.printLogInfoValueObject(defaultMstVo);
												
				if(tmpMstVo!=null &&( linkUtil.getString(tmpMstVo.getOdrCloseTypeCd()).trim().isEmpty())){
					// 미처리 오더 정보 존재 함 오류 반환
					stcResponse.setResultCD(IFDUAPERR_CD);
					stcResponse.setResultMSG(IFDUAPERR_MSG);
				}
				else{					
					
					/* WS Oder 정보 생성*/
					nresult = neosstxService.insertNeOSSOdr(defaultMstVo);
					
					if(nresult==0){
						// Make Retrun Param			
						stcResponse.setResultCD(IFSUCESSCD);
						stcResponse.setResultMSG(IFSUCESSMSG);
					}else{
						stcResponse.setResultCD(IFEXCEPTION_CD);
						stcResponse.setResultMSG("연동된 ODR 정보에 검증되지 않는 문제가 있습니다.");
					}
				}			
			}catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				stcResponse.setResultMSG(e.toString());	
				linkUtil.setSystemERR(e);
			}
			finally{
				
				// Set InterFace Logging
				linkUtil.setSystemlog("Nes_IPM_WS_0002", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG());				
			}		
		}
		return stcResponse;
	}
		
	/**
	 * @MEthod 	: consumeNeoss0003Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0003 selectSuggestIPList(): 가용 IPBlock 추천 List 연동 
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */	
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0003Response consumeNeoss0003Service(Nes0003Request stcRequest){

		/*LogKey*/
		String inputkey = "";
		int nDivCnt = 999;
		// Create Return Object
		Nes0003Response stcResponse = new Nes0003Response();
		TbIpAllocNeossMstVo tbIpAllocNeossMstVo = new TbIpAllocNeossMstVo();
		List<Nes0003IpSuggestList> stcipSuggestList = null;
		HashMap<String, Object> hparam = new HashMap<String, Object>();
		
		// Check inputParam
		if( linkUtil.getString(stcRequest.getSaid()).isEmpty()
				|| linkUtil.getString(stcRequest.getNodeOfficescode()).isEmpty()
				|| linkUtil.getString(stcRequest.getIPMSSVCSeq()).isEmpty()
				|| linkUtil.getString(stcRequest.getEXSVCCD()).isEmpty()
				|| linkUtil.getString(stcRequest.getSVCUSETYPECD()).isEmpty()
				|| linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).isEmpty()
				|| linkUtil.getString(stcRequest.getIpBitmask()).isEmpty()	
				|| linkUtil.getString(stcRequest.getSvcLineTypeCd()).isEmpty()
				){
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else if((linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).equals(CommonCodeUtil.IPV4))
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) != 0) 
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) < 16 || linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) > 30)){
			stcResponse.setResultCD(IFBITOVER_CD);
			stcResponse.setResultMSG(IFBITOVER_MSG);	
		} else if((linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).equals(CommonCodeUtil.IPV6))
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) != 0) 
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) < 48 || linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) > 64)){
			stcResponse.setResultCD(IFBITOVER_CD);
			stcResponse.setResultMSG(IFBITOVER_MSG);
		} else{
		
			try{		
				// Business Service[ Suggest IP Block List ]				
				tbIpAllocNeossMstVo.setSordernum(stcRequest.getOdrNum());
				tbIpAllocNeossMstVo.setSaid(stcRequest.getSaid());				
				tbIpAllocNeossMstVo.setIcisofficescode(stcRequest.getNodeOfficescode());				
				tbIpAllocNeossMstVo.setIpmsSvcSeq(linkUtil.getBIntFromString(stcRequest.getIPMSSVCSeq()));
				tbIpAllocNeossMstVo.setAssignTypeCd(stcRequest.getAssignTypeCD());
				tbIpAllocNeossMstVo.setExSvcCd(stcRequest.getEXSVCCD());
				tbIpAllocNeossMstVo.setSvcUseTypeCd(stcRequest.getSVCUSETYPECD());
				tbIpAllocNeossMstVo.setIpVersionTypeCd(stcRequest.getIPVERSIONTYPECD());
				tbIpAllocNeossMstVo.setIpCreateTypeCd(stcRequest.getIPCREATETYPECD());
				tbIpAllocNeossMstVo.setIpbitmask(linkUtil.getIntgerFromString(stcRequest.getIpBitmask())) ;
				tbIpAllocNeossMstVo.setssvcLineTypeCd(stcRequest.getSvcLineTypeCd());
				PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0003==================");
				PrintLogUtil.printLogInfoValueObject(tbIpAllocNeossMstVo);
								
				/*set Input Data*/
				if(linkUtil.getString(stcRequest.getIPADDRESS()).isEmpty()){					
					inputkey = "SAID : " +  stcRequest.getSaid() + " / IPMSSVCSeq :  " + stcRequest.getIPMSSVCSeq();
					
					stcipSuggestList = neosstxService.selectSuggestIPList(tbIpAllocNeossMstVo);					
					
					// Select Suggest IP Block List
					if(stcipSuggestList.size() < 1 && (tbIpAllocNeossMstVo.getIpbitmask() != null && tbIpAllocNeossMstVo.getIpbitmask() != 0  )){
						hparam.put("ssvcLineTypeCd", tbIpAllocNeossMstVo.getssvcLineTypeCd());
						hparam.put("icisofficescode", tbIpAllocNeossMstVo.getIcisofficescode());
						hparam.put("ipmsSvcSeq", tbIpAllocNeossMstVo.getIpmsSvcSeq());
						hparam.put("assignTypeCd", tbIpAllocNeossMstVo.getAssignTypeCd());
						hparam.put("ipVersionTypeCd", tbIpAllocNeossMstVo.getIpVersionTypeCd());
						hparam.put("ipCreateTypeCd", tbIpAllocNeossMstVo.getIpCreateTypeCd());
						hparam.put("ipbitmask", tbIpAllocNeossMstVo.getIpbitmask());
						
						nDivCnt = neosstxService.getdivIpBlock(hparam);
					}
					if(nDivCnt == 0){					
						stcipSuggestList = neosstxService.selectSuggestIPList(tbIpAllocNeossMstVo);
					}					
				} else if(!linkUtil.getString(stcRequest.getIPADDRESS()).isEmpty()) {

					inputkey = "SAID : " +  stcRequest.getSaid() + " / IPMSSVCSeq :  " + stcRequest.getIPMSSVCSeq() +" / ipAddressSearchKey : "+stcRequest.getIPADDRESS();
					
					tbIpAllocNeossMstVo.setSipAddressSearchKey(stcRequest.getIPADDRESS());
					stcipSuggestList = neosstxService.selectSuggestIPList(tbIpAllocNeossMstVo);						
				}
				// Make Retrun Param				
				for(Nes0003IpSuggestList stcipSuggest :stcipSuggestList){
					
					stcResponse.getStcIpSuggestList().add(stcipSuggest);
				}				
					
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSCD);
	
			} catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				stcResponse.setResultMSG(e.toString());	
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging
				linkUtil.setSystemlog("Nes_IPM_WS_0003",inputkey,  stcResponse.getResultCD(), stcResponse.getResultMSG());			
			}
		}
		return stcResponse;
	}
		
	/**
	 * @MEthod 	: consumeNeoss0004Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0004 insertReservedIPList(): IP Block 할당 예약 정보 연동
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0004Response consumeNeoss0004Service(Nes0004Request stcRequest) {
				
		/*LogKey*/
		String inputkey = "";	
		int nresult =0;
		
		TbIpAllocNeossMstVo defaultMstVo = new TbIpAllocNeossMstVo();
		List<TbIpAllocNeossMstVo> tmpListVo = new ArrayList<TbIpAllocNeossMstVo>();
		
		// Create Return Object
		Nes0004Response stcResponse = new Nes0004Response(); 
		
		stcResponse.setResultCD("");
		
		// Check inputParam
		if (linkUtil.getString(stcRequest.getRegYN()).isEmpty()
				|| stcRequest.getStcSVCInfo() == null
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getSaid()).isEmpty()
				|| linkUtil.getString(stcRequest.getStcSVCInfo().getNodeOfficescode()).isEmpty()
				|| stcRequest.getReservedIPList().size() < 1				 
				) {
			
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		}
		else
		{		
			try
			{
				/*set Input Data*/
				inputkey = "RegYN : " +stcRequest.getRegYN() + " / SAID :" + stcRequest.getStcSVCInfo().getSaid();
									
				// Business Service[ Reserved IP Block  ]				
//				if(!linkUtil.getString(stcRequest.getOdrNum()).isEmpty())
//				{
//					defaultMstVo.setSordernum(stcRequest.getOdrNum());
//					defaultMstVo.setSaid(stcRequest.getStcSVCInfo().getSaid());
//					defaultMstVo = tbIpAllocNeossMstDao.selectDefaultOdrAllocNeossMstVo(defaultMstVo);	
//					
//					if(defaultMstVo != null && !linkUtil.getString(defaultMstVo.getOdrCloseTypeCd()).trim().isEmpty()){
//						// 선행 오더 미존재
//						stcResponse.setResultCD(IFODRERR_CD);
//						stcResponse.setResultMSG(IFODRERR_MSG);
//					}
//					else{
//						defaultMstVo = new TbIpAllocNeossMstVo();
//					}
//						
//				}
				
				if(stcResponse.getResultCD().trim().isEmpty()){
					
					for(Nes0004ReservedIP nes0004ReservedIP : stcRequest.getReservedIPList() )
					{
						
						PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0004==================");
						PrintLogUtil.printLogInfoValueObject(stcRequest.getStcSVCInfo());
						PrintLogUtil.printLogInfoValueObject(nes0004ReservedIP);
						TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
												
						tmpVo.setSordernum(stcRequest.getOdrNum());						
						tmpVo.setRegyn(stcRequest.getRegYN());
						tmpVo.setSaid(stcRequest.getStcSVCInfo().getSaid());
						tmpVo.setLlnum(stcRequest.getStcSVCInfo().getLLNum());
						tmpVo.setIcisofficescode(stcRequest.getStcSVCInfo().getNodeOfficescode());
						tmpVo.setIpVersionTypeCd(nes0004ReservedIP.getIPVERSIONTYPECD());
						tmpVo.setIpCreateTypeCd(nes0004ReservedIP.getIPCREATETYPECD());
						tmpVo.setNipAssignMstSeq(linkUtil.getBIntFromString(nes0004ReservedIP.getIPASSIGNSEQ()));
						tmpVo.setIpBlock(nes0004ReservedIP.getIpBlock());
						tmpVo.setIpbitmask(linkUtil.getIntgerFromString(nes0004ReservedIP.getIpBitmask()));
						tmpVo.setSipaddr(nes0004ReservedIP.getSIpAddr());
						tmpVo.setEipaddr(nes0004ReservedIP.getEIpAddr());
						tmpVo.setIpmsSvcSeq(linkUtil.getBIntFromString(nes0004ReservedIP.getIPMSSVCSeq()));
						tmpVo.setAssignTypeCd(nes0004ReservedIP.getAssignTypeCD());
						tmpVo.setExSvcCd(nes0004ReservedIP.getEXSVCCD());
						tmpVo.setSvcUseTypeCd(nes0004ReservedIP.getSVCUSETYPECD());
						tmpVo.setGatewayip(nes0004ReservedIP.getGatewayIP());
						tmpVo.setSlacpBlockYN(nes0004ReservedIP.getLACPBLOCKYN());
						tmpVo.setSofficecode(stcRequest.getStcSVCInfo().getICISOfficescode());
						tmpVo.setSexPushYn("N");				
						tmpVo.setScreateId("NEOSS WS Reserved");	
						tmpVo.setresultcd(IFSUCESSCD);
						tmpVo.setresultmsg(IFSUCESSMSG);
						
						tmpVo.setOdrregdt(defaultMstVo.getOdrregdt());
						tmpVo.setOdrhopedt(defaultMstVo.getOdrhopedt());
						tmpVo.setInstalladdress(defaultMstVo.getInstalladdress());						
						tmpVo.setInstallroadaddress(defaultMstVo.getInstallroadaddress());
						tmpVo.setCustname(defaultMstVo.getCustname());
						tmpVo.setreportopinion(defaultMstVo.getreportopinion());
						
						tmpListVo.add(tmpVo);					
					}	
					
					nresult = neosstxService.insertReservedIPList(tmpListVo);
					
					if(nresult == 0){
						// Make Retrun Param
						stcResponse.setResultCD(IFSUCESSCD);
						stcResponse.setResultMSG(IFSUCESSMSG);
					}				
				}
			}catch (ServiceException e){
				throw e;		
			}catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);				
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging
				linkUtil.setSystemlog("Nes_IPM_WS_0004", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG());

			}	
		}
		
		return stcResponse;	
	}

	/**
	 * @MEthod 	: consumeNeoss0005Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0005 insertFixIPList(): IP Block 확정 정보 연동
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0005Response consumeNeoss0005Service(Nes0005Request stcRequest) {
		
		/*LogKey*/
		String inputkey = "";		
		
		TbIpAllocNeossMstVo defaultMstVo = new TbIpAllocNeossMstVo();
		List<TbIpAllocNeossMstVo> tmpListVo = new ArrayList<TbIpAllocNeossMstVo>();
		
		// Create Return Object
		Nes0005Response stcResponse = new Nes0005Response();  
		
		stcResponse.setResultCD("");
		
		// Check inputParam
		if (linkUtil.getString(stcRequest.getRegYN()).isEmpty()
				|| linkUtil.getString(stcRequest.getOdrCloseTypeCD()).isEmpty()				
				) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else {	
			try {
				
//				if(!linkUtil.getString(stcRequest.getOdrNum()).isEmpty())
//				{
//					defaultMstVo.setSordernum(stcRequest.getOdrNum());
//					defaultMstVo.setSaid(stcRequest.getStcSVCInfo().getSaid());
//					defaultMstVo = tbIpAllocNeossMstDao.selectDefaultOdrAllocNeossMstVo(defaultMstVo);
//					
//					defaultMstVo.setDcreateDt(commonService.selectSysDate());
//					defaultMstVo.setSexPushYn("N");				
//					defaultMstVo.setScreateId("NeOSS WS IP Fixed");
//					
//					if(defaultMstVo!=null && !linkUtil.getString(defaultMstVo.getOdrCloseTypeCd()).trim().isEmpty()){
//						// 선행 오더 미존재
//						stcResponse.setResultCD(IFODRERR_CD);
//						stcResponse.setResultMSG(IFODRERR_MSG);
//					}
//					else{
//						defaultMstVo = new TbIpAllocNeossMstVo();
//					}
//				}
				
				inputkey = "RegYN : " + stcRequest.getRegYN() + " / SAID : " + stcRequest.getStcSVCInfo().getSaid();
				
				if(stcResponse.getResultCD().isEmpty()){
					
					defaultMstVo.setSordernum(stcRequest.getOdrNum());
					defaultMstVo.setRegyn(stcRequest.getRegYN());
					defaultMstVo.setWorkerid(stcRequest.getWorkerID());
					defaultMstVo.setWorkername(stcRequest.getWorkerName());
					defaultMstVo.setOdrCloseTypeCd(stcRequest.getOdrCloseTypeCD());
					defaultMstVo.setSaid(stcRequest.getStcSVCInfo().getSaid());
					defaultMstVo.setLlnum(stcRequest.getStcSVCInfo().getLLNum());
					defaultMstVo.setIcisofficescode(stcRequest.getStcSVCInfo().getNodeOfficescode());
					defaultMstVo.setLacpsid(stcRequest.getStcSVCInfo().getLACPSaid());
					defaultMstVo.setInstalladdress(stcRequest.getStcSVCInfo().getInstallAddress());
					defaultMstVo.setInstallroadaddress(stcRequest.getStcSVCInfo().getInstallRoadAddress());
					defaultMstVo.setSspeeddesc(stcRequest.getStcSVCInfo().getSPEEDDESC());
					defaultMstVo.setSofficecode(stcRequest.getStcSVCInfo().getICISOfficescode());
					defaultMstVo.setDcreateDt(commonService.selectSysDate());
					defaultMstVo.setSexPushYn("N");				
					defaultMstVo.setScreateId("NeOSS WS IP Fixed");
					
					if(stcRequest.getOdrCloseTypeCD().equals("ODR0001")){		// 변경 존재
						
						neosstxService.insertCancelNeossOdr(defaultMstVo);
						
						// Make Retrun Param			
						stcResponse.setResultCD(IFSUCESSCD);
						stcResponse.setResultMSG(IFSUCESSMSG);
						
					}else if(stcRequest.getOdrCloseTypeCD().equals("ODR0000")){	// 변경 작업
						
						// 물리시성 정보 등록					
						defaultMstVo.setSubscnescode(stcRequest.getStcNeInfo().getSUBSCNESCODE());
						defaultMstVo.setSubscmstip(stcRequest.getStcNeInfo().getSUBSCMSTIP());
						defaultMstVo.setSubsclgipportseq(stcRequest.getStcNeInfo().getSUBSCLGIPPORTSEQ());
						defaultMstVo.setSubsclgipportdescription(stcRequest.getStcNeInfo().getSUBSCLGIPPORTDESCRIPTION());
						defaultMstVo.setSubsclgipportip(stcRequest.getStcNeInfo().getSUBSCLGIPPORTIP());
						defaultMstVo.setSubscrouterserialip(stcRequest.getStcNeInfo().getSUBSCROUTERSERIALIP());
						defaultMstVo.setSubscnealias(stcRequest.getStcNeInfo().getSUBSCNEALIAS());
						defaultMstVo.setConnalias(stcRequest.getStcNeInfo().getCONNALIAS());
						defaultMstVo.setModelname(stcRequest.getStcNeInfo().getModelName());						
						// 논리시설 정보 등록	
						List<Nes0005AssingdIp> nes0005AssingdIp =  stcRequest.getStcAssignedIPList();
						PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0005==================");
						PrintLogUtil.printLogInfoValueObject(stcRequest.getStcSVCInfo());
						PrintLogUtil.printLogInfoValueObject(stcRequest.getStcNeInfo());
						PrintLogUtil.printLogInfoValueObject(stcRequest.getStcAssignedIPList());
						for(Nes0005AssingdIp foreachvo : nes0005AssingdIp){
							TbIpAllocNeossMstVo tmpvo = new TbIpAllocNeossMstVo();
							
							tmpvo.setSordernum(defaultMstVo.getSordernum());
							tmpvo.setRegyn(defaultMstVo.getRegyn());
							tmpvo.setWorkerid(defaultMstVo.getWorkerid());
							tmpvo.setWorkername(defaultMstVo.getWorkername());
							tmpvo.setOdrCloseTypeCd(defaultMstVo.getOdrCloseTypeCd());
							tmpvo.setSaid(defaultMstVo.getSaid());
							tmpvo.setLlnum(defaultMstVo.getLlnum());
							tmpvo.setIcisofficescode(defaultMstVo.getIcisofficescode());
							tmpvo.setLacpsid(defaultMstVo.getLacpsid());
							tmpvo.setCustname(defaultMstVo.getCustname());
							tmpvo.setreportopinion(defaultMstVo.getreportopinion());
							tmpvo.setOdrregdt(defaultMstVo.getOdrregdt());
							tmpvo.setOdrhopedt(defaultMstVo.getOdrhopedt());
							tmpvo.setInstalladdress(defaultMstVo.getInstalladdress());
							tmpvo.setInstallroadaddress(defaultMstVo.getInstallroadaddress());
							tmpvo.setSspeeddesc(defaultMstVo.getSspeeddesc());
							tmpvo.setSofficecode(defaultMstVo.getSofficecode());
							
							/*물리시설*/
							tmpvo.setSubscnescode(defaultMstVo.getSubscnescode());
							tmpvo.setSubscmstip(defaultMstVo.getSubscmstip());
							tmpvo.setSubsclgipportseq(defaultMstVo.getSubsclgipportseq());
							tmpvo.setSubsclgipportdescription(defaultMstVo.getSubsclgipportdescription());
							tmpvo.setSubsclgipportip(defaultMstVo.getSubsclgipportip());
							tmpvo.setSubscrouterserialip(defaultMstVo.getSubscrouterserialip());
							tmpvo.setSubscnealias(defaultMstVo.getSubscnealias());
							tmpvo.setConnalias(defaultMstVo.getConnalias());
							tmpvo.setModelname(defaultMstVo.getModelname());
							/*논리시설*/
							tmpvo.setIpVersionTypeCd(foreachvo.getIPVERSIONTYPECD());
							tmpvo.setIpCreateTypeCd(foreachvo.getIPCREATETYPECD());
							tmpvo.setNipAssignMstSeq(linkUtil.getBIntFromString(foreachvo.getIPASSIGNSEQ()));
							tmpvo.setIpBlock(foreachvo.getIpBlock());
							tmpvo.setIpbitmask(linkUtil.getIntgerFromString(foreachvo.getIpBitmask()));
							tmpvo.setSipaddr(foreachvo.getSIpAddr());
							tmpvo.setEipaddr(foreachvo.getEIpAddr());
							tmpvo.setIpmsSvcSeq(linkUtil.getBIntFromString(foreachvo.getIPMSSVCSeq()));
							tmpvo.setAssignTypeCd(foreachvo.getAssignTypeCD());
							tmpvo.setExSvcCd(foreachvo.getEXSVCCD());
							tmpvo.setSvcUseTypeCd(foreachvo.getSVCUSETYPECD());
							tmpvo.setGatewayip(foreachvo.getGatewayIP());
							tmpvo.setSlacpBlockYN(foreachvo.getLACPBLOCKYN());
							
							tmpListVo.add(tmpvo);	
						}
						neosstxService.insertFixIPList(tmpListVo);
						
						// Make Retrun Param			
						//// 2021.05.11 ResultCd, ResultMSG 추가
						List<String> arrTemp = new ArrayList<String>();
						
						// 할당할때만 
						if(stcRequest.getRegYN().equals("Y")) {
							for(Nes0005AssingdIp foreachvo : nes0005AssingdIp) {
								
								TbIpAlloLinkOperMstVo vo = neosstxService.selectRoutingResult(foreachvo.getIpBlock(), foreachvo.getIpBitmask(), linkUtil.getBIntFromString(foreachvo.getIPASSIGNSEQ()));
								
								// 정상코드가 아닐때만 리스트에 담아서 비교
								if(!vo.getDetailRslt().equals(IFSUCESSCD)) {
									arrTemp.add(vo.getDetailRslt());	
								}
							}	
						}
						
						
						if(!arrTemp.isEmpty() && arrTemp.size() > 0) {
							
							// 다건 할당
							if(stcRequest.getStcAssignedIPList().size() > 1) {
								stcResponse.setResultCD(IFSC00008_CD);
								stcResponse.setResultMSG(IFSC00008_MSG);
							} else {
								
								String rslt = arrTemp.get(0);
								
								if(rslt.equals(IFSC00001_CD)) {
									stcResponse.setResultCD(IFSC00001_CD);
									stcResponse.setResultMSG(IFSC00001_MSG);
								} else if(rslt.equals(IFSC00002_CD)) {
									stcResponse.setResultCD(IFSC00002_CD);
									stcResponse.setResultMSG(IFSC00002_MSG);
								} else if(rslt.equals(IFSC00003_CD)) {
									stcResponse.setResultCD(IFSC00003_CD);
									stcResponse.setResultMSG(IFSC00003_MSG);
								} else if(rslt.equals(IFSC00004_CD)) {
									stcResponse.setResultCD(IFSC00004_CD);
									stcResponse.setResultMSG(IFSC00004_MSG);
								} else if(rslt.equals(IFSC00005_CD)) {
									stcResponse.setResultCD(IFSC00005_CD);
									stcResponse.setResultMSG(IFSC00005_MSG);
								} else if(rslt.equals(IFSC00006_CD)) {
									stcResponse.setResultCD(IFSC00006_CD);
									stcResponse.setResultMSG(IFSC00006_MSG);
								} else if(rslt.equals(IFSC00007_CD)) {
									stcResponse.setResultCD(IFSC00007_CD);
									stcResponse.setResultMSG(IFSC00007_MSG);
								} else if(rslt.equals(IFSUCESSCD)) {
									stcResponse.setResultCD(IFSUCESSCD);
									stcResponse.setResultMSG(IFSUCESSMSG);
								}
							}
							
						} else {
							stcResponse.setResultCD(IFSUCESSCD);
							stcResponse.setResultMSG(IFSUCESSMSG);
						}
						
					}
					else{
						stcResponse.setResultCD(IFMISODR_CD);
						stcResponse.setResultMSG(IFMISODR_MSG);						
					}
				}			
				
			}catch (ServiceException e){
				throw e;		
			}catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging
				linkUtil.setSystemlog("Nes_IPM_WS_0005", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG()); 				
			}	
		}
		
		return stcResponse;
	}

	/**
	 * @MEthod 	: consumeNeoss0006Service
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0006 selectFixedIPList(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	public Nes0006Response consumeNeoss0006Service(Nes0006Request stcRequest) {
		
		/*LogKey*/
		String inputkey = "";
		
		// Create Return Object
		Nes0006Response stcResponse = new Nes0006Response();  
		List<TbIpAlloLinkOperMstVo> stcAssignedIPList = null;
		
		// Check inputParam
		if (linkUtil.getString(stcRequest.getSaid()).isEmpty()
				|| linkUtil.getString(stcRequest.getLACPYN()).isEmpty()	
				 ){
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		}
		else{			
		
			try{
				
				inputkey = "SAID : "+ stcRequest.getSaid() +" / LACPYN :"+ stcRequest.getLACPYN();
				// Business Service[ Fixed IP Block  ]
				
				stcAssignedIPList = neosstxService.selectFixedIPList(stcRequest);
				
				
				for(TbIpAlloLinkOperMstVo foreachVo : stcAssignedIPList)
				{	
					Nes0006AssignedIP nes0006AssignedIP = new Nes0006AssignedIP();
					
					nes0006AssignedIP.setIPMSSVCSeq(linkUtil.getString(foreachVo.getNipmsSvcSeq().toString()));
					nes0006AssignedIP.setAssignTypeCD(foreachVo.getSassignTypeCd());
					nes0006AssignedIP.setEXSVCCD(foreachVo.getSexSvcCd());
					nes0006AssignedIP.setIPVERSIONTYPECD(foreachVo.getSipVersionTypeCd());
					nes0006AssignedIP.setIPCREATETYPECD(foreachVo.getSipCreateTypeCd());
					nes0006AssignedIP.setIpBlock(foreachVo.getSipBlock());
					nes0006AssignedIP.setIpBitmask(linkUtil.getString(foreachVo.getNbitmask().toString()));
					nes0006AssignedIP.setSIpAddr(foreachVo.getSfirstAddr());
					nes0006AssignedIP.setEIpAddr(foreachVo.getSlastAddr());
					nes0006AssignedIP.setGatewayIP(foreachVo.getGatewayip());
					nes0006AssignedIP.setSVCLINETYPECD(foreachVo.getSsvcLineTypeCd());
					nes0006AssignedIP.setIPASSIGNSEQ(foreachVo.getNipAssignMstSeq().toString());
					
					stcResponse.getStcAssignedIPList().add(nes0006AssignedIP);					
					
					stcResponse.setLACPSaid(foreachVo.getLacpsid());
				}
				
				// Make Retrun Param			
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSMSG);
				
			}catch (ServiceException e){
				throw e;		
			} catch (Exception e) {			
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging			
				linkUtil.setSystemlog("Nes_IPM_WS_0006", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG());
			}	
		}
		
		return stcResponse;
	}	


	/**
	 * @Method	:	consumeNeoss0007Service
	 * @Date	:	2014. 11. 9.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0007 insertNoKTIPBlock() : IPMS NO KT IP 정보 관리
	 * @변경이력	:
	 * @param stcRequest
	 * @return Nes0007Response
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0007Response consumeNeoss0007Service(Nes0007Request stcRequest) {
		
		//LogKey
		String inputkey = "";
		
		// Create Return Object
		Nes0007Response stcResponse = new Nes0007Response();  		
		
		// Check inputParam
		if ( stcRequest.getStcNoKTSvcInfo() == null  || stcRequest.getStcNoKTAssignedIPList() == null 
				|| (stcRequest.getStcNoKTAssignedIPList() != null && stcRequest.getStcNoKTAssignedIPList().size()==0 ) ){
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		}
		else{
			try{				
				inputkey = "SAID : "+ stcRequest.getStcNoKTSvcInfo().getSaid() +" / RegYN :"+ stcRequest.getREGYN();
				// Business Service
				
				if(stcRequest != null 
						&& (stcRequest.getStcNoKTSvcInfo() != null && stcRequest.getStcNoKTSvcInfo().getSaid()!= null)
						&& (stcRequest.getStcNoKTAssignedIPList() != null && stcRequest.getStcNoKTAssignedIPList().size() > 0 )
						){
					neosstxService.insertNoKTIPBlock(stcRequest);
				} else {
					stcResponse.setResultCD(IFINPUTERR_CD);
					stcResponse.setResultMSG(IFINPUTERR_MSG);				
				}							
				// Make Retrun Param			
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSMSG);
				
			}catch (ServiceException e){
				throw e;		
			}catch (Exception e) {				
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging			
				linkUtil.setSystemlog("Nes_IPM_WS_0007", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG());
			}	
		}
		return stcResponse;
	}
	
	/**
	 * @Method	:	consumeNeoss0008Service
	 * @Date	:	2014. 11. 28.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0008 insertVPNFixIPList() : VPN IP Block 정보를 예약/확정/취소
	 * @변경이력	:
	 * @param stcRequest
	 * @return Nes0008Response
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Nes0008Response consumeNeoss0008Service(Nes0008Request stcRequest) {
		//LogKey
		String inputkey = "";
		int nresult =0;
		
		// Create Return Object
		Nes0008Response stcResponse = new Nes0008Response();
		List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList = new ArrayList<TbIpAllocNeossMstVo>();
		List<TbIpAllocNeossMstVo> tbVpnIpAllocNeossMstVoList = new ArrayList<TbIpAllocNeossMstVo>();
		
		if(stcRequest == null || linkUtil.getString(stcRequest.getSaid()).isEmpty() || linkUtil.getString(stcRequest.getRegYN()).isEmpty() || (stcRequest.getStcVPNAssignedIPList() ==null && stcRequest.getStcVPNHostAssignedIPList() == null ) ){
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);			
		} else {
			try{
				
			inputkey = "SAID : "+ stcRequest.getSaid() +" / RegYN :"+ stcRequest.getRegYN();
			
			stcResponse.setResultCD(IFSUCESSCD);
			stcResponse.setResultMSG(IFSUCESSMSG);

			//Set Input Data
			
			if(stcRequest.getStcVPNAssignedIPList()!= null && stcRequest.getStcVPNAssignedIPList().size() > 0){
				for(Nes0008VPNAssingdIp nes0008VPNAssingdIp : stcRequest.getStcVPNAssignedIPList()){
					TbIpAllocNeossMstVo tmpassing = new TbIpAllocNeossMstVo();
					/*기본 정보*/
					tmpassing.setSexPushYn("N");
					tmpassing.setRegyn(stcRequest.getRegYN());
					tmpassing.setSaid(stcRequest.getSaid());					
					tmpassing.setLlnum(stcRequest.getLLNUM());
					tmpassing.setCustname(stcRequest.getCUSTNAME());
					tmpassing.setIcisofficescode(stcRequest.getNodeOfficescode());
					tmpassing.setSofficecode(stcRequest.getICISOfficescode());
					tmpassing.setInstalladdress(stcRequest.getInstallAddress());						
					tmpassing.setInstallroadaddress(stcRequest.getInstallRoadAddress());
					tmpassing.setreportopinion(stcRequest.getReportopinion());					
					tmpassing.setWorkerid(stcRequest.getWorkerID());
					tmpassing.setWorkername(stcRequest.getWorkerName());
					tmpassing.setExSvcCd(stcRequest.getEXSVCCD());
					
					tmpassing.setSvcUseTypeCd(stcRequest.getSVCUSETYPECD());
					tmpassing.setssvcLineTypeCd("CL0005");
					
					tmpassing.setScreateId(stcRequest.getWorkerID());					
					tmpassing.setSmodifyId(stcRequest.getWorkerID());
					tmpassing.setDmodifyDt(commonService.selectSysDate());
					
					tmpassing.setresultcd(IFSUCESSCD);
					tmpassing.setresultmsg(IFSUCESSMSG);
					
					/*물리시설*/
					if(stcRequest.getStcVPNNeInfo() != null)
					{
						tmpassing.setSubscnescode(stcRequest.getStcVPNNeInfo().getSUBSCNESCODE());
						tmpassing.setSubscmstip(stcRequest.getStcVPNNeInfo().getSUBSCMSTIP());
						tmpassing.setSubsclgipportseq(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTSEQ());
						tmpassing.setSubsclgipportdescription(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTDESCRIPTION());
						tmpassing.setSubsclgipportip(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTIP());
						tmpassing.setSubscrouterserialip(stcRequest.getStcVPNNeInfo().getSUBSCROUTERSERIALIP());
						tmpassing.setSubscnealias(stcRequest.getStcVPNNeInfo().getSUBSCNEALIAS());
					}
					
					/*논리 시설*/
					tmpassing.setIpVersionTypeCd(nes0008VPNAssingdIp.getIPVERSIONTYPECD());
					tmpassing.setIpCreateTypeCd(nes0008VPNAssingdIp.getIPCREATETYPECD());
					tmpassing.setNipAssignMstSeq(linkUtil.getBIntFromString(nes0008VPNAssingdIp.getIPASSIGNSEQ()));
					tmpassing.setIpBlock(nes0008VPNAssingdIp.getIpBlock());
					tmpassing.setIpbitmask(linkUtil.getIntgerFromString(nes0008VPNAssingdIp.getIpBitmask()));
					tmpassing.setSipaddr(nes0008VPNAssingdIp.getSIpAddr());
					tmpassing.setEipaddr(nes0008VPNAssingdIp.getEIpAddr());
					tmpassing.setIpmsSvcSeq(linkUtil.getBIntFromString(nes0008VPNAssingdIp.getIPMSSVCSeq()));
					tmpassing.setAssignTypeCd(nes0008VPNAssingdIp.getAssignTypeCD());
				
					
					tbIpAllocNeossMstVoList.add(tmpassing);
					
				}
				
			}
			
			if(stcRequest.getStcVPNHostAssignedIPList()!= null && stcRequest.getStcVPNHostAssignedIPList().size() > 0){
				for(Nes0008VPNHostAssignedIPList nes0008VPNHostAssignedIPList : stcRequest.getStcVPNHostAssignedIPList()){
					TbIpAllocNeossMstVo tmphostassing = new TbIpAllocNeossMstVo();
					
					/*기본 정보*/
					tmphostassing.setSexPushYn("N");
					tmphostassing.setNticketNoSeq(linkUtil.getBIntFromString("0"));
					tmphostassing.setRegyn(stcRequest.getRegYN());
					tmphostassing.setSaid(stcRequest.getSaid());					
					tmphostassing.setLlnum(stcRequest.getLLNUM());
					tmphostassing.setCustname(stcRequest.getCUSTNAME());
					tmphostassing.setIcisofficescode(stcRequest.getNodeOfficescode());
					tmphostassing.setSofficecode(stcRequest.getICISOfficescode());
					tmphostassing.setInstalladdress(stcRequest.getInstallAddress());						
					tmphostassing.setInstallroadaddress(stcRequest.getInstallRoadAddress());
					tmphostassing.setreportopinion(stcRequest.getReportopinion());					
					tmphostassing.setWorkerid(stcRequest.getWorkerID());
					tmphostassing.setWorkername(stcRequest.getWorkerName());
					tmphostassing.setExSvcCd(stcRequest.getEXSVCCD());					
					tmphostassing.setSvcUseTypeCd(stcRequest.getSVCUSETYPECD());
					tmphostassing.setssvcLineTypeCd("CL0005");
					
					tmphostassing.setScreateId(stcRequest.getWorkerID());					
					tmphostassing.setSmodifyId(stcRequest.getWorkerID());
					tmphostassing.setDmodifyDt(commonService.selectSysDate());
					
					tmphostassing.setresultcd(IFSUCESSCD);
					tmphostassing.setresultmsg(IFSUCESSMSG);
					
					/*물리시설*/
					if(stcRequest.getStcVPNNeInfo() != null)
					{
						tmphostassing.setSubscnescode(stcRequest.getStcVPNNeInfo().getSUBSCNESCODE());
						tmphostassing.setSubscmstip(stcRequest.getStcVPNNeInfo().getSUBSCMSTIP());
						tmphostassing.setSubsclgipportseq(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTSEQ());
						tmphostassing.setSubsclgipportdescription(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTDESCRIPTION());
						tmphostassing.setSubsclgipportip(stcRequest.getStcVPNNeInfo().getSUBSCLGIPPORTIP());
						tmphostassing.setSubscrouterserialip(stcRequest.getStcVPNNeInfo().getSUBSCROUTERSERIALIP());
						tmphostassing.setSubscnealias(stcRequest.getStcVPNNeInfo().getSUBSCNEALIAS());
					}
					/*논리 시설*/
					tmphostassing.setIpVersionTypeCd(nes0008VPNHostAssignedIPList.getIPVERSIONTYPECD());
					tmphostassing.setIpCreateTypeCd(nes0008VPNHostAssignedIPList.getIPCREATETYPECD());
					tmphostassing.setIpBlock(nes0008VPNHostAssignedIPList.getIpBlock());
					tmphostassing.setIpbitmask(linkUtil.getIntgerFromString(nes0008VPNHostAssignedIPList.getIpBitmask()));
					tmphostassing.setIpmsSvcSeq(linkUtil.getBIntFromString(nes0008VPNHostAssignedIPList.getIPMSSVCSeq()));
					tmphostassing.setAssignTypeCd(nes0008VPNHostAssignedIPList.getAssignTypeCD());
					
					tbVpnIpAllocNeossMstVoList.add(tmphostassing);
				}				
			}

			// 예약 등록 =R, 예약 회수 =C
			if(stcRequest.getRegYN().equalsIgnoreCase("R") || stcRequest.getRegYN().equalsIgnoreCase("C")){
				//예약
				nresult = neosstxService.insertVPNReservedIPList(tbIpAllocNeossMstVoList,tbVpnIpAllocNeossMstVoList);
				
				if(nresult != 0 )
					throw new Exception("예약이 정상 등록 되지 않읐습니다. IPBlock 정보를  확인 하십시요");
				
			}else if(stcRequest.getRegYN().equalsIgnoreCase("Y") || stcRequest.getRegYN().equalsIgnoreCase("N")){
				//할당
				nresult = neosstxService.insertVPNFixIPList(tbIpAllocNeossMstVoList,tbVpnIpAllocNeossMstVoList);
				
				if(nresult != 0 )
					throw new Exception("확정이 정상 등록 되지 않읐습니다. IPBlock 정보를  확인 하십시요");
			}
			else
			{
				stcResponse.setResultCD(IFINPUTERR_CD);
				stcResponse.setResultMSG(IFINPUTERR_MSG);				
			}
			
			}catch (ServiceException e){
				throw e;		
			} catch(Exception e) {				
				linkUtil.setSystemERR(e);
			}
			finally{
				// Set InterFace Logging			
				linkUtil.setSystemlog("Nes_IPM_WS_0008", inputkey, stcResponse.getResultCD(), stcResponse.getResultMSG());
			}	
			
		}			
		return stcResponse;
	}
	
}
