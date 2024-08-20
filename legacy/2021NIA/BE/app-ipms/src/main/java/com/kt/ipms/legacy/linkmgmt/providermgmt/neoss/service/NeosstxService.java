package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.service;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.service.RoutMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAlloLinkOperMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpmsSvcMstvo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkNonKtIpblockVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkNonKtSvcMstVo;
import com.kt.ipms.legacy.linkmgmt.common.dao.LinkIpmsMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbIpAllocNeossMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkNonKtSvcMstDao;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003IpSuggestList;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007AssignedIP;
import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0007Request;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocOrderMstVo;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocOrderMstDao;

/**
 * @FileName 	: NeosstxService.java
 * @Project  	: KT_IPMS
 * @Package  	: com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.service
 * @Date 		: 2014. 9. 12.
 * @Description :
 */
@Component
@Transactional
public class NeosstxService {	
	
@Autowired
private CommonService commonService;
	
	@Autowired
	private LinkIpmsMstDao lnkIpmsDao;
	
	@Autowired
	private TbIpAllocNeossMstDao tbIpAllocNeossMstDao;
	
	@Autowired
	private TbLnkIpAllocOrderMstDao tbLnkIpAllocOrderMstDao;
	
	@Autowired
	private TbLnkNonKtSvcMstDao tbLnkNonKtSvcMstDao;
	
	@Autowired
	private TbLnkIpAllocMstDao tbLnkIpAllocMstDao;
	
	@Autowired
	private LinkUtil linkUtil;

	@Autowired
	private WhoisAdapterService whoisAdapterService;
	
	@Autowired
	private RoutMgmtTxService routMgmtTxService;
	
	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;

	
	/**
	 * @MEthod 	: selectIpmsSvcInfo
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0001 selectIpmsSvcInfo(): IPMS 상품정보 조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<TbLnkIpmsSvcMstvo> selectIpmsSvcInfo(TbLnkIpmsSvcMstvo tbLnkIpmsSvcMstvo){		
		
		List<TbLnkIpmsSvcMstvo> returnVo = null;
		
		try{
			// Business Service [ Select TB_IPMS_SVC_MST ]
			returnVo = lnkIpmsDao.selectIpmsSvcInfo(tbLnkIpmsSvcMstvo);			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return returnVo;	
	}
	
	/**
	 * @MEthod 	: insertNeOSSOdr
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0002 insertNeOSSOdr(): ODR 정보 생성
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertNeOSSOdr(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		int nresult = 0;
		try{
			tbIpAllocNeossMstVo.setresultcd(CommonCodeUtil.SUCCESS_MSG);
			tbIpAllocNeossMstVo.setresultmsg(CommonCodeUtil.SUCCESS_MSG);			
			tbIpAllocNeossMstDao.insertTbIpAllocNeossMstVo(tbIpAllocNeossMstVo);
			nresult = this.insertNeossUIODR(tbIpAllocNeossMstVo);
			if(nresult !=0)
				throw new ServiceException("LNK.HIGH.00032", new String[]{"ODR 정보 생성"});
		}catch(ServiceException e){
			nresult =1;
			throw e;			
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);			
			nresult=1;
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return nresult;
	}

	
	/**
	 * @MEthod 	: selectSuggestIPList
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0003 selectSuggestIPList(): 가용 IPBlock 추천 List 연동
	 * @변경이력 	:
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Nes0003IpSuggestList> selectSuggestIPList(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		List<Nes0003IpSuggestList> stcipSuggestList = new ArrayList<Nes0003IpSuggestList>();
				
		Nes0003IpSuggestList stcipSuggest = new Nes0003IpSuggestList();
				 
		List<TbIpAlloLinkOperMstVo> ipSuggestListVo = null;
		
		try{
			ipSuggestListVo = lnkIpmsDao.selectSuggestIPList(tbIpAllocNeossMstVo);
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
			
		for(TbIpAlloLinkOperMstVo foreachvo : ipSuggestListVo){
			
			stcipSuggest = new Nes0003IpSuggestList();
			
			stcipSuggest.setAssignTypeCD(foreachvo.getSassignTypeCd());
			stcipSuggest.setIPVERSIONTYPECD(foreachvo.getSipVersionTypeCd());
			stcipSuggest.setIPCREATETYPECD(foreachvo.getSipCreateTypeCd());
			stcipSuggest.setIPASSIGNSEQ(foreachvo.getNipAssignMstSeq().toString());
			stcipSuggest.setIPBLOCK(foreachvo.getSipBlock());
			stcipSuggest.setIpBitmask(foreachvo.getNbitmask().toString());
			stcipSuggest.setSIpAddr(foreachvo.getSfirstAddr());
			stcipSuggest.setEIpAddr(foreachvo.getSlastAddr());
			stcipSuggest.setAssignDT(linkUtil.getStringFromDate(foreachvo.getDmodifyDt(), ""));
			stcipSuggest.setPriority(foreachvo.getpriority().toString());
			
			stcipSuggestList.add(stcipSuggest);
		}
		
		return stcipSuggestList;
	}	
	
	/**
	 * @MEthod 	: insertReservedIPList
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0004 insertReservedIPList(): IP Block 할당 예약 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVoList
	 * @param bRegYN
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertReservedIPList(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList){
		
		int nResult = 999;
		String sExceptionMSG="";
		String sExceptionCD="LNK.INFO.00006";
		
		HashMap<String,Object> paramMap = new HashMap<String, Object>();		
		
		// 할당 예약 등록
		try{
			PrintLogUtil.printLogInfo("##### insertReservedIPList");
			
			PrintLogUtil.printLogInfo("tbIpAllocNeossMstVoList.size(): " + tbIpAllocNeossMstVoList.size());
			
			for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){
				
				PrintLogUtil.printLogInfo("=============================paramVo==================");
				PrintLogUtil.printLogInfoValueObject(paramVo);
				PrintLogUtil.printLogInfo("=====================================================");
				
				paramMap.put("sordernum", paramVo.getSordernum());
				paramMap.put("regyn", paramVo.getRegyn());
				paramMap.put("said", paramVo.getSaid());
				paramMap.put("llnum", paramVo.getLlnum());
				paramMap.put("icisofficescode", paramVo.getIcisofficescode());
				paramMap.put("ip_version_type_cd", paramVo.getIpVersionTypeCd());
				paramMap.put("ip_create_type_cd", paramVo.getIpCreateTypeCd());
				paramMap.put("nip_assign_mst_seq", paramVo.getNipAssignMstSeq());
				paramMap.put("ip_block", paramVo.getIpBlock());
				paramMap.put("ipbitmask", paramVo.getIpbitmask());
				paramMap.put("sipaddr", paramVo.getSipaddr());
				paramMap.put("eipaddr", paramVo.getEipaddr());
				paramMap.put("ipms_svc_seq", paramVo.getIpmsSvcSeq());
				paramMap.put("assign_type_cd", paramVo.getAssignTypeCd());
				paramMap.put("ex_svc_cd", paramVo.getExSvcCd());
				paramMap.put("svc_use_type_cd", paramVo.getSvcUseTypeCd());
				paramMap.put("gatewayip", paramVo.getGatewayip());
				paramMap.put("sresult_cd", paramVo.getresultcd());
				paramMap.put("sresult_msg", paramVo.getresultmsg());
				paramMap.put("sofficecode", paramVo.getSofficecode());
				paramMap.put("slacpblockyn", paramVo.getSlacpBlockYN());
				paramMap.put("ssvc_line_type_cd", paramVo.getssvcLineTypeCd());
				paramMap.put("workerid", paramVo.getWorkerid());

				List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
				IpHistoryMstVo selVo = null;
				List<IpHistoryMstVo> delVoList = new ArrayList<IpHistoryMstVo>();
				IpHistoryMstVo delVo = null;
				
				IpHistoryMstVo searchVo = new IpHistoryMstVo();
				
				if(paramVo.getRegyn().equals("N")) {
					searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
					searchVo.setSsaid(paramVo.getSaid());
					delVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);	
				}
				
				PrintLogUtil.printLogInfo("=============================delVoList.size() : " + delVoList.size() + "==================");
				if(delVoList.size() > 0) {
					
					delVo = delVoList.get(0);
					PrintLogUtil.printLogInfo("=============================delVoList==================");
					PrintLogUtil.printLogInfo("delVo.getNipAssignMstSeq() = " + delVo.getNipAssignMstSeq());
					PrintLogUtil.printLogInfo("delVo.getNipAllocMstSeq() = " + delVo.getNipAllocMstSeq());
					PrintLogUtil.printLogInfo("delVo.getSsaid() = " + delVo.getSsaid());
					PrintLogUtil.printLogInfo("======================================================");
					
				}
				
				nResult = lnkIpmsDao.insertReservedIpList(paramMap);
				
				PrintLogUtil.printLogInfo("===nResult : " + nResult);
				if(nResult == 0) {

					if(paramVo.getRegyn().equals("Y")) {
						searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
						searchVo.setSsaid(paramVo.getSaid());
						selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);	
					}
					
					PrintLogUtil.printLogInfo("=============================selVoList.size() : " + selVoList.size() + "==================");
					if(selVoList.size() > 0) {
						
						selVo = selVoList.get(0);
						PrintLogUtil.printLogInfo("=============================selVoList==================");
						PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
						PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
						PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
						PrintLogUtil.printLogInfo("======================================================");
						
					}
					
						
					/* 이력관리 이력 등록 */
					IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
					//ipHistoryMstVo.setSmenuNm("NeOSS");
					
					BigInteger nipHistTaskCd = null;
					if(paramVo.getRegyn().equals("Y")) {
						
						CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
						nipHistTaskCd = CommonCodeUtil.HIST_TASK_ALLOC_RESV; // 할당예약
						
					} else if(paramVo.getRegyn().equals("N")) {
						
						CloneUtil.copyObjectInformation(delVo, ipHistoryMstVo);
						nipHistTaskCd = CommonCodeUtil.HIST_TASK_CANCEL_RESV; // 할당예약해지 
						ipHistoryMstVo.setSassignLevelCd("IA0004");  // 할당상태에서 해지를 하면 서비스배정(미할당)으로 돌아감
					}


					String sCreateId = paramVo.getWorkerid() == null ? "NeOSS" : paramVo.getWorkerid();
//					sCreateId = sCreateId == "" ? "NeOSS" : sCreateId; //Codeeyes-Urgent-객체 비교시 == , != 사용제한
					sCreateId = sCreateId.equals("") ? "NeOSS" : sCreateId;
					
					ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
					ipHistoryMstVo.setsMenuHistCd("NeOSS");

					ipHistoryMstVo.setSordernum(paramVo.getSordernum());
					ipHistoryMstVo.setSregyn(paramVo.getRegyn());
					ipHistoryMstVo.setSodrCloseTypeCd(paramVo.getOdrCloseTypeCd());
					ipHistoryMstVo.setSsaid(paramVo.getSaid());
					ipHistoryMstVo.setSllnum(paramVo.getLlnum());
					ipHistoryMstVo.setSicisofficescode(paramVo.getIcisofficescode());
					ipHistoryMstVo.setSlacpsid(paramVo.getLacpsid());
					ipHistoryMstVo.setSsubscnescode(paramVo.getSubscnescode());
					ipHistoryMstVo.setSsubscmstip(paramVo.getSubscmstip());
					ipHistoryMstVo.setSsubsclgipportseq(paramVo.getSubsclgipportseq());
					ipHistoryMstVo.setSsubsclgipportdescription(paramVo.getSubsclgipportdescription());
					ipHistoryMstVo.setSsubsclgipportip(paramVo.getSubsclgipportip());
					ipHistoryMstVo.setSsubscrouterserialip(paramVo.getSubscrouterserialip());
					ipHistoryMstVo.setSsubscnealias(paramVo.getSubscnealias());
					ipHistoryMstVo.setSconnalias(paramVo.getConnalias());
					ipHistoryMstVo.setSmodelname(paramVo.getModelname());
					ipHistoryMstVo.setScustname(paramVo.getCustname());
					ipHistoryMstVo.setScreateId(sCreateId);
					
					historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
					
				}
			
				
				if(nResult == -1){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00043";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nResult == -2){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00044";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nResult != 0){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00045";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				
				paramMap.clear();
			
			}
			
			if(nResult == 0 && tbIpAllocNeossMstVoList.size() > 0)
				nResult = this.updateNeossUIODR(tbIpAllocNeossMstVoList);		
			
			if(nResult!=0){
				sExceptionMSG="IP Block 할당 예약 정보 연동";
				sExceptionCD="LNK.HIGH.00032";
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			} 
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}

		return nResult;
	}
	
	/* */
	/**
	 * @MEthod 	: insertFixIPList
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0005 insertFixIPList(): IP Block 확정 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @return
	 * @throws Exception 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertFixIPList(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList){
		
		int nResult = 999;
		String sExceptionMSG="";
		String sExceptionCD="LNK.INFO.00006";
		
		HashMap<String,Object> paramMap = new HashMap<String, Object>();		
		
		// 할당 등록
		try{		
			
			PrintLogUtil.printLogInfo("##### insertFixIPList");

			PrintLogUtil.printLogInfo("tbIpAllocNeossMstVoList.size(): " + tbIpAllocNeossMstVoList.size());
			
			
			for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){
			
				TbIpAllocNeossMstVo delVo = new TbIpAllocNeossMstVo();
				delVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
				delVo.setSaid(paramVo.getSaid());
				
				TbWhoisVo whoisVo = new TbWhoisVo();

				whoisVo = whoisAdapterService.selectWhoisSeq(delVo);
				
				PrintLogUtil.printLogInfo("=============================paramVo==================");
				PrintLogUtil.printLogInfoValueObject(paramVo);
				PrintLogUtil.printLogInfo("======================================================");
				
				//ParamSet
				paramMap.put("sordernum", paramVo.getSordernum());
				paramMap.put("regyn", paramVo.getRegyn());
				paramMap.put("said", paramVo.getSaid());
				paramMap.put("llnum", paramVo.getLlnum());
				paramMap.put("icisofficescode", paramVo.getIcisofficescode());
				paramMap.put("ip_version_type_cd", paramVo.getIpVersionTypeCd());
				paramMap.put("ip_create_type_cd", paramVo.getIpCreateTypeCd());
				paramMap.put("nip_assign_mst_seq", paramVo.getNipAssignMstSeq());
				paramMap.put("ip_block", paramVo.getIpBlock());
				paramMap.put("ipbitmask", paramVo.getIpbitmask());
				paramMap.put("sipaddr", paramVo.getSipaddr());
				paramMap.put("eipaddr", paramVo.getEipaddr());
				paramMap.put("ipms_svc_seq", paramVo.getIpmsSvcSeq());
				paramMap.put("assign_type_cd", paramVo.getAssignTypeCd());
				paramMap.put("ex_svc_cd", paramVo.getExSvcCd());
				paramMap.put("svc_use_type_cd", paramVo.getSvcUseTypeCd());
				paramMap.put("gatewayip", paramVo.getGatewayip());
				paramMap.put("sresult_cd", "SUCCESS");
				paramMap.put("sresult_msg", "SUCCESS");
				paramMap.put("odr_close_type_cd", paramVo.getOdrCloseTypeCd());
				paramMap.put("lacpsaid", paramVo.getLacpsid());
				paramMap.put("subscnescode", paramVo.getSubscnescode());
				paramMap.put("subscmstip", paramVo.getSubscmstip());
				paramMap.put("subsclgipportseq", linkUtil.getIntgerFromString(paramVo.getSubsclgipportseq()));
				paramMap.put("subsclgipportdescription", paramVo.getSubsclgipportdescription());
				paramMap.put("subsclgipportip", paramVo.getSubsclgipportip());
				paramMap.put("subscrouterserialip", paramVo.getSubscrouterserialip());
				paramMap.put("subscnealias", paramVo.getSubscnealias());
				paramMap.put("connalias", paramVo.getConnalias());
				paramMap.put("modelname", paramVo.getModelname());
				paramMap.put("custname", paramVo.getCustname());
				paramMap.put("report_opinion", paramVo.getreportopinion());
				paramMap.put("installaddress", paramVo.getInstalladdress());
				paramMap.put("installroadaddress", paramVo.getInstallroadaddress());
				paramMap.put("workerid", paramVo.getWorkerid());
				paramMap.put("workername", paramVo.getWorkername());
				paramMap.put("odrregdt", paramVo.getOdrregdt());
				paramMap.put("odrhopedt", paramVo.getOdrhopedt());
				paramMap.put("sspeeddesc", paramVo.getSspeeddesc());
				paramMap.put("sofficecode", paramVo.getSofficecode());
				paramMap.put("slacpblockyn", paramVo.getSlacpBlockYN());
				paramMap.put("ssvc_line_type_cd", paramVo.getssvcLineTypeCd());

				List<IpHistoryMstVo> delVo2List = new ArrayList<IpHistoryMstVo>();
				IpHistoryMstVo delVo2 = new IpHistoryMstVo();
				IpHistoryMstVo searchVo = new IpHistoryMstVo();
				
				if(paramVo.getRegyn().equals("N")) {
					searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
					searchVo.setSsaid(paramVo.getSaid());
					delVo2List = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);
				}
				
				PrintLogUtil.printLogInfo("=============================delVo2List.size() : " + delVo2List.size() + "==================");
				if(delVo2List.size() > 0) {
					
					delVo2 = delVo2List.get(0);
					PrintLogUtil.printLogInfo("=============================delVo2List==================");
					PrintLogUtil.printLogInfo("delVo2.getNipAssignMstSeq() = " + delVo2.getNipAssignMstSeq());
					PrintLogUtil.printLogInfo("delVo2.getNipAllocMstSeq() = " + delVo2.getNipAllocMstSeq());
					PrintLogUtil.printLogInfo("delVo2.getSsaid() = " + delVo2.getSsaid());
					PrintLogUtil.printLogInfo("======================================================");
					
				}
				
				// 해지 시 LACP 일 경우 해당 SAID 건만 해지 해야 함
				if(paramVo.getRegyn().equals("N") && delVo2List.size() > 0) {
					nResult = lnkIpmsDao.insertFixIpList(paramMap);
				}  else {
					nResult = lnkIpmsDao.insertFixIpList(paramMap);
				}
				
				
				// 할당/해지 성공시에만 whois 전송
				if(nResult == 0) {

					TbIpAllocNeossMstVo newVo = new TbIpAllocNeossMstVo();
					newVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
					newVo.setSaid(paramVo.getSaid());
					
					TbWhoisVo allocVo = new TbWhoisVo();

					allocVo = whoisAdapterService.selectAllocMstSeq(newVo);
					
					
					/***************************
					 *  Whois 전송
					 ***************************/
					// 확정 Y , 회수 N                        
					
					try {
						
						
						if(paramVo.getRegyn().equals("Y") && allocVo.getSscreteYn().equals("N")) {
							PrintLogUtil.printLog("#####ALLOC NEOSS MST SEQ: " + allocVo.getNipAllocMstSeq());
							
							// Whois 전송 예외
							boolean rtnExceptVal = true;
							rtnExceptVal = whoisAdapterService.exceptWhois("NEW", allocVo.getNipAllocMstSeq(), null);
							if(rtnExceptVal) {
								whoisAdapterService.sendToWhoisWithDbNoRtn(null, allocVo.getNipAllocMstSeq(), "NEW");	
							}
							
																						
						} else if(paramVo.getRegyn().equals("N") && whoisVo.getSscreteYn().equals("Y")) {
							PrintLogUtil.printLog("#####WHOIS SEQ: " + whoisVo.getNwhoisSeq());
							boolean rtnExceptVal = true;
							if(whoisVo.getNwhoisSeq() != null){
								int nipAllocMstCnt = whoisAdapterService.selectNipAllocMstCnt(whoisVo.getNwhoisSeq());
																																 
								if( nipAllocMstCnt <= 1){
									// Whois 전송 예외
									rtnExceptVal = whoisAdapterService.exceptWhois("DEL", null, whoisVo.getNwhoisSeq());
									if(rtnExceptVal) {
										whoisAdapterService.sendToWhoisWithDbNoRtn(null, whoisVo.getNwhoisSeq(), "DEL");
									}
								}
							}else{
								rtnExceptVal = whoisAdapterService.exceptWhois("DEL", null, whoisVo.getNwhoisSeq());
								if(rtnExceptVal) {
									whoisAdapterService.sendToWhoisWithDbNoRtn(null, whoisVo.getNwhoisSeq(), "DEL");
								}
							}
							
						}
						
					} catch (Exception e) {
						lnkIpmsDao.insertTbNeossErrorManage(paramVo);
						PrintLogUtil.printLogInfo("===============Nes_IPM_WS_0005 Error==================");
						PrintLogUtil.printLog(e.toString());
						PrintLogUtil.printLogInfo("===============Nes_IPM_WS_0005 IP VALUE==================");
						PrintLogUtil.printLogInfo("IpBlock = " + paramVo.getIpBlock());
						PrintLogUtil.printLogInfo("SAID = " + paramVo.getSaid());
						PrintLogUtil.printLogInfo("NipAssignSeq = " + paramVo.getNipAssignMstSeq());
						PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0005 allocVO==================");
						if(allocVo == null){
							allocVo = new TbWhoisVo();
																		  
						}
						PrintLogUtil.printLogInfo("NipAllocMstSeq = " +allocVo.getNipAllocMstSeq());
						PrintLogUtil.printLogInfo("SscreteYn = " +allocVo.getSscreteYn());
						PrintLogUtil.printLogInfo("=============Nes_IPM_WS_0005 whoisVO==================");
						if(whoisVo == null){
							whoisVo = new TbWhoisVo();
																		  
						}
						PrintLogUtil.printLogInfo("NwhoisSeq = " +whoisVo.getNwhoisSeq());
						PrintLogUtil.printLogInfo("SscreteYn = " +whoisVo.getSscreteYn());
						PrintLogUtil.printLogInfo("======================================================");
					}
					
				
					
					/***************************
					 *  라우팅 점검 업데이트
					 ***************************/
					PrintLogUtil.printLogInfo("#####ASSIGN MST SEQ: " + paramVo.getNipAssignMstSeq());
					TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
					tbRoutChkMstVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());	
					routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);



					/* 이력관리 이력 등록 */
					IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
					//ipHistoryMstVo.setSmenuNm("NeOSS");
					
					List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
					IpHistoryMstVo selVo = new IpHistoryMstVo();
					
					if(paramVo.getRegyn().equals("Y")) {
						searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
						searchVo.setSsaid(paramVo.getSaid());
						selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);
					}
					
					PrintLogUtil.printLogInfo("=============================selVoList.size() : " + selVoList.size() + "==================");
					if(selVoList.size() > 0) {
						
						selVo = selVoList.get(0);
						PrintLogUtil.printLogInfo("=============================selVoList==================");
						PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
						PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
						PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
						PrintLogUtil.printLogInfo("======================================================");
						
					}
					
						String sCreateId = paramVo.getWorkerid() == null ? "NeOSS" : paramVo.getWorkerid();
//						sCreateId = sCreateId == "" ? "NeOSS" : sCreateId; //Codeeyes-Urgent-객체 비교시 == , != 사용제한
						sCreateId = sCreateId.equals("") ? "NeOSS" : sCreateId;
						
						BigInteger nipHistTaskCd = null;
						if(paramVo.getRegyn().equals("Y")) {

							CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
							nipHistTaskCd = CommonCodeUtil.HIST_TASK_ALLOC; // 할당
						} else if(paramVo.getRegyn().equals("N")) {

							CloneUtil.copyObjectInformation(delVo2, ipHistoryMstVo);
							nipHistTaskCd = CommonCodeUtil.HIST_TASK_CANCEL; // 해지
							ipHistoryMstVo.setSassignLevelCd("IA0004");  // 할당상태에서 해지를 하면 서비스배정(미할당)으로 돌아감
						}
						
						ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
						ipHistoryMstVo.setsMenuHistCd("NeOSS");
						
						ipHistoryMstVo.setSordernum(paramVo.getSordernum());
						ipHistoryMstVo.setSregyn(paramVo.getRegyn());
						ipHistoryMstVo.setSodrCloseTypeCd(paramVo.getOdrCloseTypeCd());
						ipHistoryMstVo.setSsaid(paramVo.getSaid());
						ipHistoryMstVo.setSllnum(paramVo.getLlnum());
						ipHistoryMstVo.setSicisofficescode(paramVo.getIcisofficescode());
						ipHistoryMstVo.setSlacpsid(paramVo.getLacpsid());
						ipHistoryMstVo.setSsubscnescode(paramVo.getSubscnescode());
						ipHistoryMstVo.setSsubscmstip(paramVo.getSubscmstip());
						ipHistoryMstVo.setSsubsclgipportseq(paramVo.getSubsclgipportseq());
						ipHistoryMstVo.setSsubsclgipportdescription(paramVo.getSubsclgipportdescription());
						ipHistoryMstVo.setSsubsclgipportip(paramVo.getSubsclgipportip());
						ipHistoryMstVo.setSsubscrouterserialip(paramVo.getSubscrouterserialip());
						ipHistoryMstVo.setSsubscnealias(paramVo.getSubscnealias());
						ipHistoryMstVo.setSconnalias(paramVo.getConnalias());
						ipHistoryMstVo.setSmodelname(paramVo.getModelname());
						ipHistoryMstVo.setScustname(paramVo.getCustname());
						ipHistoryMstVo.setScreateId(sCreateId);
						
						PrintLogUtil.printLogInfo("=============================ipHistoryMstVo==================");
						PrintLogUtil.printLogInfoValueObject(ipHistoryMstVo);
						PrintLogUtil.printLogInfo("==========================================================");
						try{
							historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
						}catch (Exception e) {
							PrintLogUtil.printLogInfo("=============================ipHistory insert Error==================");
							lnkIpmsDao.insertTbNeossErrorManage(paramVo);
						}
						
						
					}
				
				// 해지 시 LACP 일 경우 해당 SAID 건만 해지 해야 함 -> 해지할 건이 없으면 할당 된게 아니기 때문에
				if(paramVo.getRegyn().equals("N") && delVo2List.size() == 0) {
					nResult = 0;
				}  
				
				if(nResult == -1){
					lnkIpmsDao.insertTbNeossErrorManage(paramVo);							  
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00043";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nResult == -2){
					lnkIpmsDao.insertTbNeossErrorManage(paramVo);							  
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00044";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nResult != 0){
					lnkIpmsDao.insertTbNeossErrorManage(paramVo);							  
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00045";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				
				
				paramMap.clear();
				
				
			}
			
			if(tbIpAllocNeossMstVoList.size() > 0)
				this.deleteNeossUIODR(tbIpAllocNeossMstVoList.get(0));			
			if(nResult!=0){
				sExceptionMSG="IP Block 할당 정보 연동";
				sExceptionCD="LNK.HIGH.00032";	
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			}
					
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}
		return nResult;
	}
	
	/**
	 * @MEthod 	: insertCancelNeossOdr
	 * @Date	: 2014. 9. 21.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0005 insertFixIPList(): NeOSS ODR 취소 처리 기능
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor =Exception.class)
	public int insertCancelNeossOdr(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){		
		
		int nResult =999;
		try{
			
			nResult = tbIpAllocNeossMstDao.insertTbIpAllocNeossMstVo(tbIpAllocNeossMstVo);
			
			this.deleteNeossUIODR(tbIpAllocNeossMstVo);
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
		return nResult;
	}
	
	/* Nes_IPM_WS_0006 selectFixedIPList(): IPMS IP 할당 정보 조회*/
	/**
	 * @MEthod 	: selectFixedIPList
	 * @Date	: 2014. 9. 17.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0006 selectFixedIPList(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TbIpAlloLinkOperMstVo> selectFixedIPList(Nes0006Request stcRequest){
		
		List<TbIpAlloLinkOperMstVo> returnListvo = new ArrayList<TbIpAlloLinkOperMstVo>();
		
		TbIpAlloLinkOperMstVo paramVo = new TbIpAlloLinkOperMstVo();

		paramVo.setSsaid(stcRequest.getSaid());
		
		try{
			if(stcRequest.getLACPYN().equals("N")){
				// 순수 IP Block  조회
				returnListvo = lnkIpmsDao.selectFixedIPList(paramVo);
			}else{
				// LACP 연관 정보 포함.
				returnListvo = lnkIpmsDao.selectFixedLacpIPList(paramVo);
				
				if(returnListvo != null && returnListvo.size() == 0)
				{
					returnListvo = lnkIpmsDao.selectFixedIPList(paramVo);
				}
			}
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return returnListvo;
	}
	
	
	/**
	 * @Method	:	insertNoKTIPBlock
	 * @Date	:	2014. 11. 9.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0007 insertNoKTIPBlock() : IPMS NO KT IP 정보 관리
	 * @변경이력	:
	 * @param stcRequest void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertNoKTIPBlock(Nes0007Request stcRequest){
		
		// input Vo 작성
		TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo = new TbLnkNonKtSvcMstVo();
		int nonKtSvcMstSeq = 0;
				
		try{
			
			PrintLogUtil.printLogInfo("##### insertNoKTIPBlock");
			
			tbLnkNonKtSvcMstVo.setSregyn("Y");
			tbLnkNonKtSvcMstVo.setSworkerid(stcRequest.getWORKERID());
			tbLnkNonKtSvcMstVo.setSworkername(stcRequest.getWORKERNAME());
			tbLnkNonKtSvcMstVo.setSsaid(stcRequest.getStcNoKTSvcInfo().getSaid());
			tbLnkNonKtSvcMstVo.setSllnum(stcRequest.getStcNoKTSvcInfo().getLLNum());
			tbLnkNonKtSvcMstVo.setScustname(stcRequest.getStcNoKTSvcInfo().getCUSTNAME());			
			tbLnkNonKtSvcMstVo.setSsvcUseTypeCd(stcRequest.getStcNoKTSvcInfo().getSVCUSETYPECD());
			tbLnkNonKtSvcMstVo.setSexSvcCd(stcRequest.getStcNoKTSvcInfo().getEXSVCCD());
			tbLnkNonKtSvcMstVo.setSinstalladdress(stcRequest.getStcNoKTSvcInfo().getINSTALLADDRESS());
			tbLnkNonKtSvcMstVo.setSinstallroadaddress(stcRequest.getStcNoKTSvcInfo().getINSTALLROADADDRESS());
			tbLnkNonKtSvcMstVo.setSicisofficescode(stcRequest.getStcNoKTSvcInfo().getNODEOFFICESCODE());
			tbLnkNonKtSvcMstVo.setSofficecode(stcRequest.getStcNoKTSvcInfo().getICISOFFICESCODE());
			tbLnkNonKtSvcMstVo.setSenCustname(stcRequest.getStcNoKTSvcInfo().getBGPENCUSTNAME());
			tbLnkNonKtSvcMstVo.setSasNum(stcRequest.getStcNoKTSvcInfo().getASNUM());
			tbLnkNonKtSvcMstVo.setScreateId(stcRequest.getWORKERID());
			tbLnkNonKtSvcMstVo.setSmodifyId(stcRequest.getWORKERID());			

			nonKtSvcMstSeq = this.selectnonKtSvcMstSeq(tbLnkNonKtSvcMstVo);
			
			if(nonKtSvcMstSeq == 0){
				// tb_non_KT_svc_mst insert
				this.insertNonKtSvcMst(tbLnkNonKtSvcMstVo);
			}else{
				// tb_non_KT_svc_mst 존재시 UPDATE
				tbLnkNonKtSvcMstVo.setNnonKtSvcMstSeq(BigInteger.valueOf(nonKtSvcMstSeq));
				
				this.updateNonKtSvcMst(tbLnkNonKtSvcMstVo);
			}	
			
			tbLnkNonKtSvcMstVo.setSregyn(stcRequest.getREGYN());
			
			if(nonKtSvcMstSeq == 0)
				nonKtSvcMstSeq = this.selectnonKtSvcMstSeq(tbLnkNonKtSvcMstVo);
						
			for (Nes0007AssignedIP foreachvo : stcRequest.getStcNoKTAssignedIPList()) {
				
				TbLnkNonKtIpblockVo tmpvo = new TbLnkNonKtIpblockVo();
				tmpvo.setNnonKtSvcMstSeq(BigInteger.valueOf(nonKtSvcMstSeq));
				tmpvo.setSipBlock(foreachvo.getIPBLOCK());
				if(foreachvo.getIPBITMASK().contains(".")||foreachvo.getIPBITMASK().contains(":"))
					throw new ServiceException("CMN.HIGH.00001");
				else
					tmpvo.setNbitmask(linkUtil.getIntgerFromString(foreachvo.getIPBITMASK()));
					
				tmpvo.setScreateId(stcRequest.getWORKERID());
				tmpvo.setSmodifyId(stcRequest.getWORKERID());
				tmpvo.setsFlag(tbLnkNonKtSvcMstVo.getSregyn());
				
				this.insertNonKtIpblock(tmpvo);			
			}
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	/**
	 * @Method	:	insertVPNFixIPList
	 * @Date	:	2014. 12. 2.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0008 insertVPNFixIPList() : VPN IP Block 정보를 확정/취소
	 * @변경이력	:
	 * @param tbIpAllocNeossMstVoList
	 * @param tbVpnIpAllocNeossMstVoList
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertVPNFixIPList(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList, List<TbIpAllocNeossMstVo> tbLnkVpnIpAllocMstVo)
	{
		int nreturn= 0;
		
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		String sExceptionMSG="";
		String sExceptionCD="LNK.HIGH.00032";
	
		try{
			
			PrintLogUtil.printLogInfo("##### insertVPNFixIPList");
			
			if(tbIpAllocNeossMstVoList != null && tbIpAllocNeossMstVoList.size() > 0){
				
				nreturn = this.insertFixIPList(tbIpAllocNeossMstVoList);				
			}
			
			if(tbLnkVpnIpAllocMstVo != null && tbLnkVpnIpAllocMstVo.size() > 0){
				for(TbIpAllocNeossMstVo paramVo : tbLnkVpnIpAllocMstVo){
					paramMap.put("regyn",paramVo.getRegyn());
					paramMap.put("said",paramVo.getSaid());
					paramMap.put("llnum",paramVo.getLlnum());
					paramMap.put("icisofficescode",paramVo.getIcisofficescode());
					paramMap.put("ip_version_type_cd",paramVo.getIpVersionTypeCd());
					paramMap.put("ip_create_type_cd",paramVo.getIpCreateTypeCd());
					paramMap.put("ip_block",paramVo.getIpBlock());
					paramMap.put("ipbitmask",paramVo.getIpbitmask());
					paramMap.put("sipaddr","");
					paramMap.put("eipaddr","");
					paramMap.put("ipms_svc_seq", paramVo.getIpmsSvcSeq());
					paramMap.put("assign_type_cd",paramVo.getAssignTypeCd());	
					paramMap.put("ex_svc_cd",paramVo.getExSvcCd());
					paramMap.put("svc_use_type_cd",paramVo.getSvcUseTypeCd());
					paramMap.put("sresult_cd",paramVo.getresultcd());
					paramMap.put("sresult_msg",paramVo.getresultmsg());
					paramMap.put("subscnescode",paramVo.getSubscnescode());
					paramMap.put("subscmstip",paramVo.getSubscmstip());
					paramMap.put("subsclgipportseq",linkUtil.getIntgerFromString(paramVo.getSubsclgipportseq()));
					paramMap.put("subsclgipportdescription",paramVo.getSubsclgipportdescription());
					paramMap.put("subsclgipportip",paramVo.getSubsclgipportip());
					paramMap.put("subscrouterserialip",paramVo.getSubscrouterserialip());
					paramMap.put("subscnealias",paramVo.getSubscnealias());
					paramMap.put("custname",paramVo.getCustname());
					paramMap.put("report_opinion",paramVo.getreportopinion());
					paramMap.put("installaddress",paramVo.getInstalladdress());
					paramMap.put("installroadaddress",paramVo.getInstallroadaddress());
					paramMap.put("workerid",paramVo.getWorkerid());
					paramMap.put("workername",paramVo.getWorkername());
					paramMap.put("sofficecode",paramVo.getSofficecode());
					paramMap.put("ssvclinetypecd",paramVo.getssvcLineTypeCd());				
					
				nreturn = lnkIpmsDao.insertFixVpnIpList(paramMap);
				
				if(nreturn == -1){
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00043";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nreturn == -2){
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00044";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nreturn == 1){
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="APP.INFO.00030";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nreturn != 0){
					sExceptionMSG= paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00045";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				
				paramMap.clear();
				
				}
				
				if(nreturn!=0){
					sExceptionMSG="IP Block 할당 정보 연동";
					sExceptionCD="LNK.HIGH.00032";	
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				
			}
			
			if(nreturn != 0)
				throw new ServiceException("LNK.HIGH.00032",new String[]{"IP Block 할당  정보 연동"});
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}		
		
		return nreturn;
	}
	
	/**
	 * @Method	:	insertVPNReservedIPList
	 * @Date	:	2014. 12. 2.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0008 insertVPNFixIPList() : VPN IP Block 정보를 예약/취소
	 * @변경이력	:
	 * @param tbIpAllocNeossMstVoList
	 * @param tbVpnIpAllocNeossMstVoList
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertVPNReservedIPList(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList, List<TbIpAllocNeossMstVo> tbLnkVpnIpAllocMstVo)
	{
		int nreturn= 0;
		
		HashMap<String,Object> paramMap = new HashMap<String, Object>();	
		String sExceptionMSG="";
		String sExceptionCD="LNK.INFO.00006";

		try{
			
			PrintLogUtil.printLogInfo("##### insertVPNReservedIPList");
			
			if(tbIpAllocNeossMstVoList != null && tbIpAllocNeossMstVoList.size() > 0){
				nreturn = this.insertReservedIPList(tbIpAllocNeossMstVoList);				
			}
			
			if(tbLnkVpnIpAllocMstVo != null && tbLnkVpnIpAllocMstVo.size() > 0){				
			
				for(TbIpAllocNeossMstVo paramVo : tbLnkVpnIpAllocMstVo){
					
					paramMap.put("regyn",paramVo.getRegyn());
					paramMap.put("said",paramVo.getSaid());
					paramMap.put("llnum",paramVo.getLlnum());
					paramMap.put("icisofficescode",paramVo.getIcisofficescode());
					paramMap.put("ip_version_type_cd",paramVo.getIpVersionTypeCd());
					paramMap.put("ip_create_type_cd",paramVo.getIpCreateTypeCd());
					paramMap.put("ip_block",paramVo.getIpBlock());
					paramMap.put("ipbitmask",paramVo.getIpbitmask());
					paramMap.put("sipaddr","");
					paramMap.put("eipaddr","");
					paramMap.put("ipms_svc_seq",paramVo.getIpmsSvcSeq());
					paramMap.put("assign_type_cd",paramVo.getAssignTypeCd());					
					paramMap.put("ex_svc_cd",paramVo.getExSvcCd());
					paramMap.put("svc_use_type_cd",paramVo.getSvcUseTypeCd());
					paramMap.put("sresult_cd",paramVo.getresultcd());
					paramMap.put("sresult_msg",paramVo.getresultmsg());
					paramMap.put("sofficecode",paramVo.getSofficecode());
					paramMap.put("ssvclinetypecd",paramVo.getssvcLineTypeCd());
					paramMap.put("workerid",paramVo.getWorkerid());
					paramMap.put("custname",paramVo.getCustname());
					paramMap.put("report_opinion",paramVo.getreportopinion());				
					
				nreturn = lnkIpmsDao.insertReservedVpnIpList(paramMap);
				
				if(nreturn == -1){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00043";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nreturn == -2){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00044";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				} else if(nreturn != 0){
					sExceptionMSG=paramVo.getIpBlock();
					sExceptionCD="LNK.HIGH.00045";
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
				
				paramMap.clear();
				
				}
				
				if(nreturn!=0){
					sExceptionMSG="IP Block 할당 예약 정보 연동";
					sExceptionCD="LNK.HIGH.00032";	
					throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
				}
			}
			
			if(nreturn != 0)
				throw new ServiceException("LNK.HIGH.00032",new String[]{"IP Block 할당 예약 정보 연동"});
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
		}
		
		return nreturn;
	}
	
	/**
	 * @Method	:	selectnonKtSvcMstSeq
	 * @Date	:	2014. 11. 13.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0007 insertNoKTIPBlock() : 기존재 SCN 검색 
	 * @변경이력	:
	 * @param tbLnkNonKtSvcMstVo
	 * @return int
	 */
	@Transactional(readOnly = true)
	private int selectnonKtSvcMstSeq(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo)
	{
		int nreturn= 0;
		int ncheck= 0;
		
		List<TbLnkNonKtSvcMstVo> returnListvo =null;
		
		TbLnkNonKtSvcMstVo searchvo = new TbLnkNonKtSvcMstVo();
		
		try{
			searchvo.setSsaid(tbLnkNonKtSvcMstVo.getSsaid());
			searchvo.setSexSvcCd(tbLnkNonKtSvcMstVo.getSexSvcCd());
			
			returnListvo = tbLnkNonKtSvcMstDao.selectListTbLnkNonKtSvcMstVo(searchvo);
			
			if(returnListvo != null && returnListvo.size() > 0)
				ncheck = returnListvo.get(0).getNnonKtSvcMstSeq().intValue();
			else
				ncheck =0;
		}catch(ServiceException e){			
			ncheck = 0;
			throw e;
		}catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NO KT IP MST 조회"});
		}
		finally{
			nreturn = ncheck;
			returnListvo = null;
			searchvo = null;
		}				
		return nreturn;		
	}

	/**
	 * @Method	:	insertNonKtSvcMst
	 * @Date	:	2014. 11. 28.
	 * @Author	:	neoargon
	 * @DESC	:   Nes_IPM_WS_0007 insertNoKTIPBlock() : Non KT 회원정보 입력
	 * @변경이력	:
	 * @param tbLnkNonKtSvcMstVo
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int insertNonKtSvcMst(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo)
	{
		int nreturn = 0;
		
		try{			
			nreturn = tbLnkNonKtSvcMstDao.insertTbLnkNonKtSvcMstVo(tbLnkNonKtSvcMstVo);
			
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NO KT IP MST 등록"});
		}
		
		return  nreturn;
	}
	
	/**
	 * @Method	:	updateNonKtSvcMst
	 * @Date	:	2014. 11. 28.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0007 insertNoKTIPBlock() : Non KT 회원정보 변경
	 * @변경이력	:
	 * @param tbLnkNonKtSvcMstVo
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int updateNonKtSvcMst(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo)
	{
		int nreturn = 0;
		
		try{
			tbLnkNonKtSvcMstVo.setDmodifyDt(commonService.selectSysDate());
			nreturn = tbLnkNonKtSvcMstDao.updateTbLnkNonKtSvcMstVo(tbLnkNonKtSvcMstVo);
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NO KT IP 블럭 등록 정보 변경"});
		}
		
		return  nreturn;
	}
	
	/**
	 * @Method	:	insertNonKtIpblock
	 * @Date	:	2014. 11. 28.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0007 insertNoKTIPBlock() : Non KT IP Block 정보 입력
	 * @변경이력	:
	 * @param tbLnkNonKtIpblockVo
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int insertNonKtIpblock(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo)
	{
		int nreturn = 0;
		try{
			nreturn = tbLnkNonKtSvcMstDao.insertTbLnkNonKtIpblockVo(tbLnkNonKtIpblockVo);
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NO KT IP 블럭 등록 "});
		}
		
		return  nreturn;
	}	
	/**
	 * @MEthod 	: getdivIpBlock
	 * @Date	: 2014. 10. 4.
	 * @Author	: neoargon
	 * @DESC	: 추천리스트 조회 시 요청 블럭에 따른 IPBlock 정보 분할 기능
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int getdivIpBlock(HashMap<String, Object> hparam){
		int nResult =999;		
		StringBuffer sExceptionKey = new StringBuffer("");
		try{
			nResult = lnkIpmsDao.getdivIpBlock(hparam);
			
			if(nResult > 0){
				sExceptionKey.append("[ ssvcLineTypeCd : ");
				sExceptionKey.append(hparam.get("ssvcLineTypeCd"));
				sExceptionKey.append(" ] [ icisofficescode : ");
				sExceptionKey.append(hparam.get("icisofficescode"));
				sExceptionKey.append(" ] [ ipmsSvcSeq : ");
				sExceptionKey.append(hparam.get("ipmsSvcSeq"));
				sExceptionKey.append(" ] [ assignTypeCd : ");
				sExceptionKey.append(hparam.get("assignTypeCd"));
				sExceptionKey.append(" ] [ ipVersionTypeCd : ");
				sExceptionKey.append(hparam.get("ipVersionTypeCd"));
				sExceptionKey.append(" ] [ ipCreateTypeCd : ");
				sExceptionKey.append(hparam.get("ipCreateTypeCd"));
				sExceptionKey.append(" ] [ ipbitmask : ");
				sExceptionKey.append(hparam.get("ipbitmask"));
				sExceptionKey.append(" ]");
				sExceptionKey.append(" 처리에  오류가 발생 하였습니다.");
				
				throw new Exception(sExceptionKey.toString());
			}			
				
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("LNK.HIGH.00032", new String[]{"IP 블럭 분할"});
		}
		
		return nResult;
	}
	

	/**
	 * @Method	:	insertNeossUIODR
	 * @Date	:	2014. 10. 27.
	 * @Author	:	neoargon
	 * @DESC	:	NeOSS ODR UI용  Insert
	 * @변경이력	:
	 * @param tbLnkIpAllocOrderMstVo void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int insertNeossUIODR(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		int nreturn=0;
		
		TbLnkIpAllocOrderMstVo insertvo = new TbLnkIpAllocOrderMstVo();
		
			try{
			if(tbIpAllocNeossMstVo != null && linkUtil.getString(tbIpAllocNeossMstVo.getSordernum()).length() > 0 ){
			// Set tbLnkIpAllocOrderMstVo
			insertvo.setSordernum(tbIpAllocNeossMstVo.getSordernum());
			insertvo.setSodrregdt(tbIpAllocNeossMstVo.getOdrregdt());
			insertvo.setSodrhopedt(tbIpAllocNeossMstVo.getOdrhopedt());
			insertvo.setScustname(tbIpAllocNeossMstVo.getCustname());
			insertvo.setSreportOpinion(tbIpAllocNeossMstVo.getreportopinion());
			insertvo.setSsaid(tbIpAllocNeossMstVo.getSaid());
			insertvo.setSllnum(tbIpAllocNeossMstVo.getLlnum());
			insertvo.setSspeeddesc(tbIpAllocNeossMstVo.getSspeeddesc());			
			insertvo.setSofficecode(tbIpAllocNeossMstVo.getSofficecode());
			insertvo.setSicisofficescode(tbIpAllocNeossMstVo.getIcisofficescode());
			insertvo.setSsvcUseTypeCd(tbIpAllocNeossMstVo.getSvcUseTypeCd());
			insertvo.setSexSvcCd(tbIpAllocNeossMstVo.getExSvcCd());
			insertvo.setSinstalladdress(tbIpAllocNeossMstVo.getInstalladdress());
			insertvo.setSinstallroadaddress(tbIpAllocNeossMstVo.getInstallroadaddress());
			insertvo.setSipVersionTypeCd(tbIpAllocNeossMstVo.getIpVersionTypeCd());
			insertvo.setSofficecode(tbIpAllocNeossMstVo.getSofficecode());
			insertvo.setBflagStatus("R");
			insertvo.setScreateId(tbIpAllocNeossMstVo.getScreateId());
			insertvo.setSmodifyId(tbIpAllocNeossMstVo.getScreateId());
			
			tbLnkIpAllocOrderMstDao.insertTbLnkIpAllocOrderMstVo(insertvo);
			
			}
			
		}catch(ServiceException e){			
			nreturn =1;
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			nreturn =1;
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NEOSS ODR"});
		}		
			
		return nreturn;
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
	private int updateNeossUIODR(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList){
		
		int nreturn=0;
		int nallocCnt =0;
		int nreqCnt =0;
		
		TbLnkIpAllocOrderMstVo updatevo = new TbLnkIpAllocOrderMstVo();
		TbLnkIpAllocOrderMstVo selectvo = new TbLnkIpAllocOrderMstVo();
		TbLnkIpAllocMstVo tbLnkIpAllocMstVo = new TbLnkIpAllocMstVo();
		
		try{
			
			if(tbIpAllocNeossMstVoList != null && tbIpAllocNeossMstVoList.size() > 0 &&  linkUtil.getString(tbIpAllocNeossMstVoList.get(0).getSordernum()).length() > 0 ){
				
				updatevo.setSordernum(tbIpAllocNeossMstVoList.get(0).getSordernum());
				updatevo.setSsaid(tbIpAllocNeossMstVoList.get(0).getSaid());
				
					selectvo = tbLnkIpAllocOrderMstDao.selectTbLnkIpAllocOrderMstVo(updatevo);
				
				if(selectvo != null && selectvo.getNipAllocOrderSeq() != null)
				{
					
					if(tbIpAllocNeossMstVoList.get(0).getRegyn().equalsIgnoreCase("Y"))
					{
						updatevo.setNipAllocOrderSeq(selectvo.getNipAllocOrderSeq());
						updatevo.setBflagStatus("C");
						updatevo.setDmodifyDt(commonService.selectSysDate());
						updatevo.setSmodifyId("NeOSS_WS_Odr_Update");
					}
					else if(tbIpAllocNeossMstVoList.get(0).getRegyn().equalsIgnoreCase("N")){
						updatevo.setNipAllocOrderSeq(selectvo.getNipAllocOrderSeq());
						tbLnkIpAllocMstVo.setSsaid(tbIpAllocNeossMstVoList.get(0).getSaid());
						nreqCnt = tbIpAllocNeossMstVoList.size();
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
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NEOSS ODR"});
		}
		
		return nreturn;
	}
	
	/**
	 * @Method	:	deleteNeossUIODR
	 * @Date	:	2014. 10. 27.
	 * @Author	:	neoargon
	 * @DESC	:	NeOSS ODR UI용  Delete
	 * @변경이력	:
	 * @param tbIpAllocNeossMstVo void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int deleteNeossUIODR(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		
		int nreturn=0;
		
		TbLnkIpAllocOrderMstVo deletevo = new TbLnkIpAllocOrderMstVo();
		TbLnkIpAllocOrderMstVo selectvo = new TbLnkIpAllocOrderMstVo();
		try{
			if(tbIpAllocNeossMstVo != null && linkUtil.getString(tbIpAllocNeossMstVo.getSordernum()).length() > 0 ){
				
				deletevo.setSordernum(tbIpAllocNeossMstVo.getSordernum());
				deletevo.setSsaid(tbIpAllocNeossMstVo.getSaid());
				
				selectvo = tbLnkIpAllocOrderMstDao.selectTbLnkIpAllocOrderMstVo(deletevo);
				
				if(selectvo != null && selectvo.getNipAllocOrderSeq() != null)
				{
					// Set tbLnkIpAllocOrderMstVo
					deletevo.setNipAllocOrderSeq(selectvo.getNipAllocOrderSeq());
			
					tbLnkIpAllocOrderMstDao.deleteTbLnkIpAllocOrderMstVo(deletevo);
				}
			}
		}catch(ServiceException e){			
			nreturn=1;
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			nreturn =1;
			throw new ServiceException("LNK.HIGH.00032", new String[]{"NEOSS ODR"});
		}
		
		return nreturn;
	}
	
	/**
	 * @MEthod 	: selectRoutingResult
	 * @Date	: 2021. 05. 11.
	 * @DESC	: 라우팅 정보 조회
	 * @변경이력 	:
	 * @param ipBlock
	 * @param bitMask
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpAlloLinkOperMstVo selectRoutingResult(String ipBlock, String bitMask, BigInteger nipAssignMstSeq){
		
		TbIpAlloLinkOperMstVo returnVo = new TbIpAlloLinkOperMstVo();
		
		try{
			if(!ipBlock.isEmpty() && !bitMask.isEmpty()) {
				String pipPrefix = ipBlock + "/" + bitMask;
				TbIpAllocNeossMstVo tbIpAllocNeossMstVo = new TbIpAllocNeossMstVo();
				tbIpAllocNeossMstVo.setPipPrefix(pipPrefix);
				tbIpAllocNeossMstVo.setNipAssignMstSeq(nipAssignMstSeq);
				returnVo = lnkIpmsDao.selectRoutingResult(tbIpAllocNeossMstVo);	
			}
			
		}catch(ServiceException e){
			throw e;
		}
		catch(Exception e){
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return returnVo;
	}
}
