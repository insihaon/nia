package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.service.RoutMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.linkmgmt.common.dao.LinkIpmsMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbIpAllocNeossMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocOrderMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkNonKtSvcMstDao;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAlloLinkOperMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.adapter.NeOSSConsumeAdapterService;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001IpSuggestList;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0004Request;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;


@Component
@Transactional
public class SdnTxService {
	
		@Autowired
		private CommonService commonService;
		
		@Lazy @Autowired
		private LinkIpmsMstDao lnkIpmsDao;
		
		@Lazy @Autowired
		private TbIpAllocNeossMstDao tbIpAllocNeossMstDao;
		
		@Lazy @Autowired
		private TbLnkIpAllocOrderMstDao tbLnkIpAllocOrderMstDao;
		
		@Lazy @Autowired
		private TbLnkNonKtSvcMstDao tbLnkNonKtSvcMstDao;
		
		@Lazy @Autowired
		private TbLnkIpAllocMstDao tbLnkIpAllocMstDao;
		
		@Autowired
		private LinkUtil linkUtil;

		@Autowired
		private WhoisAdapterService whoisAdapterService;
		
		@Autowired
		private NeOSSConsumeAdapterService neOSSConsumeAdapterService;

		@Autowired
		private RoutMgmtTxService routMgmtTxService;

		@Autowired
		private HistoryMgmtAdapterService historyMgmtAdapterService;
		
		/* 2021.05.11 추가 상세 결과 코드 추가 */
		private final static String IFSC00001_CD = "SC00001";
		private final static String IFSC00001_MSG = "IPMS정보와 라우팅정보 불일치 IP블록";

		private final static String IFSC00002_CD = "SC00002";
		private final static String IFSC00002_MSG = "중계/PE에 Summary 설정 내역 없음";

		private final static String IFSC00003_CD = "SC00003";
		private final static String IFSC00003_MSG = "타사 또는 타노드와 라우팅 중복됨";

		private final static String IFSC00004_CD = "SC00004";
		private final static String IFSC00004_MSG = "IPMS정보와 라우팅정보 불일치, Summary 설정 내역 없음";

		private final static String IFSC00005_CD = "SC00005";
		private final static String IFSC00005_MSG = "IPMS정보와 라우팅정보 불일치, 타사/타노드 라우팅 중복";

		private final static String IFSC00006_CD = "SC00006";
		private final static String IFSC00006_MSG = "타사/타노드 라우팅 중복, Summary 설정 내역 없음";

		private final static String IFSC00007_CD = "SC00007";
		private final static String IFSC00007_MSG = "IPMS정보와 라우팅정보 불일치, 타사/타노드 라우팅 중복, Summary 설정 내역 없음";
		
		/**
		 * @MEthod 	: selectSuggestIPListSdn
		 * @DESC	: SDN_IPM_WS_0001 selectSuggestIPListSdn(): 가용 IPBlock 추천 List 연동
		 * @변경이력 	:
		 * @return
		 */
		@Transactional(readOnly = true)
		public List<Sdn0001IpSuggestList> selectSuggestIPListSdn (TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
			
			List<Sdn0001IpSuggestList> stcipSuggestList = new ArrayList<Sdn0001IpSuggestList>();
					
			Sdn0001IpSuggestList stcipSuggest = null;
					 
			List<TbIpAlloLinkOperMstVo> ipSuggestListVo = null;
			
			try{
				ipSuggestListVo = lnkIpmsDao.selectSuggestIPListSdn(tbIpAllocNeossMstVo);
				
			}catch(ServiceException e){
				throw e;
			}
			catch(Exception e){
				linkUtil.setSystemERR(e);
				throw new ServiceException("CMN.HIGH.00000");
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			String strModifyDate = "";
			if(ipSuggestListVo.size() > 0) {

				for(TbIpAlloLinkOperMstVo foreachvo : ipSuggestListVo){
					
					stcipSuggest = new Sdn0001IpSuggestList();
					
					stcipSuggest.setIPBLOCK(foreachvo.getSipBlock());
					stcipSuggest.setIpBitmask(foreachvo.getNbitmask().toString());
					
					if(null != foreachvo.getDmodifyDt()) {
						strModifyDate = format.format(foreachvo.getDmodifyDt());
					}
					
					stcipSuggest.setModifiedDate(strModifyDate);
					
					// IP 상세 상태 파라미터 추가
					if(null != foreachvo.getDetailRslt()) {
						if(foreachvo.getDetailRslt().equals(IFSC00001_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00001_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00001_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00002_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00002_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00002_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00003_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00003_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00003_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00004_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00004_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00004_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00005_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00005_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00005_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00006_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00006_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00006_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00007_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00007_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00007_MSG);
						} 
					}
					
					stcipSuggestList.add(stcipSuggest);
				}
				
			}
			
			
			return stcipSuggestList;
		}	
		
		@Transactional(readOnly = true)
		public String countSelectSuggestIPListSdn(TbIpAllocNeossMstVo tbIpAllocNeossMstVo) {
			return lnkIpmsDao.countSelectSuggestIPListSdn(tbIpAllocNeossMstVo);
		}
		
		
		@Transactional(readOnly = true)
		public List<Sdn0001IpSuggestList> selectDivSuggestIPListSdn(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
			
			List<Sdn0001IpSuggestList> stcipSuggestList = null;
			stcipSuggestList = new ArrayList<Sdn0001IpSuggestList>();
			Sdn0001IpSuggestList stcipSuggest = null;	 
			List<TbIpAlloLinkOperMstVo> ipSuggestListVo = null;
			
			try{
				ipSuggestListVo = lnkIpmsDao.selectDivSuggestIPListSdn(tbIpAllocNeossMstVo);
				
			}catch(ServiceException e){
				throw e;
			}
			catch(Exception e){
				linkUtil.setSystemERR(e);
				throw new ServiceException("CMN.HIGH.00000");
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			String strModifyDate = "";
			if(ipSuggestListVo.size() > 0) {

				for(TbIpAlloLinkOperMstVo foreachvo : ipSuggestListVo){
					
					stcipSuggest = new Sdn0001IpSuggestList();
					
					stcipSuggest.setIPBLOCK(foreachvo.getSipBlock());
					stcipSuggest.setIpBitmask(foreachvo.getNbitmask().toString());
					
					if(null != foreachvo.getDmodifyDt()) {
						strModifyDate = format.format(foreachvo.getDmodifyDt());
					}
					
					stcipSuggest.setModifiedDate(strModifyDate);
					
					// IP 상세 상태 파라미터 추가
					if(null != foreachvo.getDetailRslt()) {
						if(foreachvo.getDetailRslt().equals(IFSC00001_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00001_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00001_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00002_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00002_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00002_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00003_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00003_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00003_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00004_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00004_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00004_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00005_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00005_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00005_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00006_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00006_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00006_MSG);
						} else if(foreachvo.getDetailRslt().equals(IFSC00007_CD)) {
							stcipSuggest.setIpDetailStatus(IFSC00007_CD);
							stcipSuggest.setIpDetailStatusMsg(IFSC00007_MSG);
						} 
					}
					
					stcipSuggestList.add(stcipSuggest);
				}
				
			}
			return stcipSuggestList;
		}
		/**
		 * @MEthod 	: getdivIpBlock
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
		 * @MEthod 	: insertFixIPListSdn
		 * @DESC	: SDN_IPM_WS_0002 insertFixIPListSdn(): IP Block 확정 정보 연동
		 * @변경이력 	:
		 * @param tbIpAllocNeossMstVo
		 * @return
		 * @throws Exception 
		 */
		@Transactional(propagation = Propagation.REQUIRED)
		public int insertFixIPListSdn(String regYn, List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList){
			
			int nResult = 999;
			String sExceptionMSG="";
			String sExceptionCD="LNK.INFO.00006";
			Boolean neossWs = false;
			HashMap<String,Object> paramMap = new HashMap<String, Object>();		
			
			int countAlloc = 0;

			try{		

				PrintLogUtil.printLogInfo("##### insertFixIPListSdn");
				
				if("Y".equals(regYn)) { 				// 할당 등록
					
					for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){
						
						TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();
						List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>();	//연동 처리 관련
						TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
						TbIpAlloLinkOperMstVo tmpSelVo = new TbIpAlloLinkOperMstVo();
						
						//ParamSet
						paramMap.put("regyn", paramVo.getRegyn());
						paramMap.put("said", paramVo.getSaid());
						paramMap.put("ssvc_line_type_cd", paramVo.getssvcLineTypeCd());
						paramMap.put("nip_assign_mst_seq", paramVo.getNipAssignMstSeq());
						paramMap.put("ip_block", paramVo.getIpBlock());
						paramMap.put("ipbitmask", paramVo.getIpbitmask());

						paramMap.put("gatewayIp", paramVo.getGatewayip());   // 2021.01.04 추가
						
						countAlloc = lnkIpmsDao.countAlloc(paramVo);	// 할당 건수 체크
						
						PrintLogUtil.printLogInfoValueObject(paramVo);
						
						if(countAlloc > 0) {
							nResult = 997;
						} else {
							
							nResult = lnkIpmsDao.insertFixIpListSdn(paramMap);
							
							/**************************
							 * SDN 확정  정보 NeOSS 전송
							 **************************/
							// 할당 성공시에만 NeOSS 전송
							if(nResult == 0) {
								// 할당 성공 시 할당테이블 조회
								
								tmpSelVo = lnkIpmsDao.selectMstAllocVo(paramVo);
								
								tmpVo.setSaid(tmpSelVo.getSsaid());
								tmpVo.setRegyn(tmpSelVo.getSregYn());
								tmpVo.setIpmsSvcSeq(tmpSelVo.getNipmsSvcSeq());
								tmpVo.setAssignTypeCd(tmpSelVo.getSassignTypeCd());
								tmpVo.setExSvcCd(tmpSelVo.getSexSvcCd());
								tmpVo.setSvcUseTypeCd(tmpSelVo.getSsvcUseTypeCd());
								tmpVo.setIpVersionTypeCd(tmpSelVo.getSipVersionTypeCd());
								tmpVo.setIpCreateTypeCd(tmpSelVo.getSipCreateTypeCd());
								tmpVo.setIpBlock(tmpSelVo.getSipBlock());
								tmpVo.setIpbitmask(tmpSelVo.getNbitmask());
								tmpVo.setSipaddr(tmpSelVo.getSfirstAddr());
								tmpVo.setEipaddr(tmpSelVo.getSlastAddr());
								tmpVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq());
								tmpVo.setGatewayip(tmpSelVo.getGatewayip());
								tmpVo.setSlacpBlockYN(tmpSelVo.getSlacpblockyn());
								tmpVo.setssvcLineTypeCd(tmpSelVo.getSsvcLineTypeCd());
								
								linkIpAllocNeossVos.add(tmpVo);
								tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
								neossWs = neOSSConsumeAdapterService.insertFixedIPList(tbIpAllocNeossMstListVo); 
							}
							
							// 할당 성공시에만 whois 전송
							if(nResult == 0 && neossWs) {
								
								TbIpAllocNeossMstVo newVo = new TbIpAllocNeossMstVo();
								newVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
								newVo.setSaid(paramVo.getSaid());
								
								TbWhoisVo allocVo = new TbWhoisVo();

								allocVo = whoisAdapterService.selectAllocMstSeq(newVo);
								
								/***************************
								 *  Whois 전송
								 ***************************/
								// 확정 Y
								if("Y".equals(paramVo.getRegyn()) && "N".equals(allocVo.getSscreteYn())) {
								//if(paramVo.getRegyn().equals("Y") && allocVo.getSscreteYn().equals("N")) {
									PrintLogUtil.printLog("#####ALLOC NEOSS MST SEQ: " + allocVo.getNipAllocMstSeq());
									
									// Whois 전송 예외
									boolean rtnExceptVal = true;
									rtnExceptVal = whoisAdapterService.exceptWhois("NEW", allocVo.getNipAllocMstSeq(), null);
									if(rtnExceptVal) {
										whoisAdapterService.sendToWhoisWithDbNoRtn(null, allocVo.getNipAllocMstSeq(), "NEW");
									}
								} 
								
								/***************************
								 *  라우팅 점검 업데이트
								 ***************************/
								PrintLogUtil.printLog("#####ASSIGN MST SEQ: " + paramVo.getNipAssignMstSeq());
								TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
								tbRoutChkMstVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());	
								routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);
								
								
								/* 이력관리 이력 등록 */
								IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
								ipHistoryMstVo.setsMenuHistCd("SDN");
								//ipHistoryMstVo.setSmenuNm("SDN");
								
								List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
								IpHistoryMstVo selVo = new IpHistoryMstVo();
								IpHistoryMstVo searchVo = new IpHistoryMstVo();
								
								searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
								searchVo.setSsaid(paramVo.getSaid());
								selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);
								
								PrintLogUtil.printLogInfo("=============================selVoList.size() : " + selVoList.size() + "==================");
								if(selVoList.size() > 0) {
									
									selVo = selVoList.get(0);
									PrintLogUtil.printLogInfo("=============================selVoList==================");
									PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
									PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
									PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
									PrintLogUtil.printLogInfo("======================================================");
									
								}

								String sCreateId = paramVo.getWorkerid() == null ? "SDN" : paramVo.getWorkerid();
//								sCreateId = sCreateId == "" ? "SDN" : sCreateId; //Codeeyes-Urgent-객체 비교시 == , != 사용제한
								sCreateId = sCreateId.equals("") ? "SDN" : sCreateId;
								
								CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
								
								BigInteger nipHistTaskCd = null;
								nipHistTaskCd = CommonCodeUtil.HIST_TASK_ALLOC; // 할당
								
								ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
								ipHistoryMstVo.setsMenuHistCd("SDN");
								ipHistoryMstVo.setScreateId(sCreateId);
								
								historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
								
							} 
							
						}
						
						
						if(nResult == -1){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00043";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult == -2){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00044";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult != 0){
							if(nResult != 998 || nResult != 997 || nResult != 996) {
								return nResult;
							} else {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
						}
						
						paramMap.clear();
						
					}
					
					
				} else if("N".equals(regYn)) {	    // 할당 해지 
					
					for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){
						
						countAlloc = lnkIpmsDao.countAlloc(paramVo);	// 해지 건수 체크
						
						if(countAlloc == 0) {
							nResult = 996;
						} else {
							List<TbIpAlloLinkOperMstVo> ssaidList = lnkIpmsDao.selectSsaidVo(paramVo);
							
							if(ssaidList.size() == 0) {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
							
							for(TbIpAlloLinkOperMstVo vo : ssaidList) { // for-1 start
								
								TbIpAllocNeossMstVo delVo = new TbIpAllocNeossMstVo();
								delVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
								delVo.setSaid(vo.getSsaid());
								
								TbWhoisVo whoisVo = new TbWhoisVo();

								whoisVo = whoisAdapterService.selectWhoisSeq(delVo);
								
								TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();
								List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>();	//연동 처리 관련
								// TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
								// TbIpAlloLinkOperMstVo tmpSelVo = new TbIpAlloLinkOperMstVo();
								TbIpAllocNeossMstVo pVo = new TbIpAllocNeossMstVo();
								
								pVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
								pVo.setSaid(vo.getSsaid());
								// ########################################################
								// tmpSelVo = lnkIpmsDao.selectMstAllocVo(pVo); //20200512 단일에서 List로 수정
								List<TbIpAlloLinkOperMstVo> tempTmpSelVoList = lnkIpmsDao.selectMstAllocVoList(pVo);
								
								for(TbIpAlloLinkOperMstVo tmpSelVo:tempTmpSelVoList){ // for-2 start
									TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
									tmpVo.setSaid(tmpSelVo.getSsaid());
									tmpVo.setRegyn("N");
									tmpVo.setIpmsSvcSeq(tmpSelVo.getNipmsSvcSeq());
									tmpVo.setAssignTypeCd(tmpSelVo.getSassignTypeCd());
									tmpVo.setExSvcCd(tmpSelVo.getSexSvcCd());
									tmpVo.setSvcUseTypeCd(tmpSelVo.getSsvcUseTypeCd());
									tmpVo.setIpVersionTypeCd(tmpSelVo.getSipVersionTypeCd());
									tmpVo.setIpCreateTypeCd(tmpSelVo.getSipCreateTypeCd());
									tmpVo.setIpBlock(tmpSelVo.getSipBlock());
									tmpVo.setIpbitmask(tmpSelVo.getNbitmask());
									tmpVo.setSipaddr(tmpSelVo.getSfirstAddr());
									tmpVo.setEipaddr(tmpSelVo.getSlastAddr());
									tmpVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq());
									tmpVo.setGatewayip(tmpSelVo.getGatewayip());
									tmpVo.setSlacpBlockYN(tmpSelVo.getSlacpblockyn());
									tmpVo.setssvcLineTypeCd(tmpSelVo.getSsvcLineTypeCd());
									
									linkIpAllocNeossVos.add(tmpVo);
									tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
									
									
									countAlloc = lnkIpmsDao.countAlloc(tmpVo);	// 해지 건수 체크
									
									if(countAlloc == 0) {
										nResult = 996;
									} else {

										List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
										IpHistoryMstVo selVo = new IpHistoryMstVo();
										IpHistoryMstVo searchVo = new IpHistoryMstVo();
										
										searchVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq());
										searchVo.setSsaid(paramVo.getSaid());
										selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);
										
										PrintLogUtil.printLogInfo("=============================selVoList.size() : " + selVoList.size() + "==================");
										if(selVoList.size() > 0) {
											
											selVo = selVoList.get(0);
											PrintLogUtil.printLogInfo("=============================selVoList==================");
											PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
											PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
											PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
											PrintLogUtil.printLogInfo("======================================================");
											
										}
										
										neossWs = neOSSConsumeAdapterService.insertFixedIPList(tbIpAllocNeossMstListVo); 
										
										// NeOSS 연동 성공 시에 IPMS 해지
										if(neossWs) {
											//ParamSet
											paramMap.put("regyn", "N");
											paramMap.put("said", vo.getSsaid());
											paramMap.put("ssvc_line_type_cd", vo.getSsvcLineTypeCd());
											paramMap.put("nip_assign_mst_seq", vo.getNipAssignMstSeq());
											paramMap.put("ip_block", vo.getSipBlock());
											paramMap.put("ipbitmask", vo.getNbitmask());

											nResult = lnkIpmsDao.insertFixIpListSdn(paramMap);
										}

										// 해지 성공시에만 whois 전송
										if(nResult == 0 && neossWs) {
											
											TbIpAllocNeossMstVo newVo = new TbIpAllocNeossMstVo();
											newVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
											newVo.setSaid(vo.getSsaid());
											
											TbWhoisVo allocVo = new TbWhoisVo();
											allocVo = whoisAdapterService.selectAllocMstSeq(newVo);
											
											/***************************
											 *  Whois 전송
											 ***************************/
											// 회수 N
											if("N".equals(paramVo.getRegyn()) && "Y".equals(whoisVo.getSscreteYn())) {
											//if(paramVo.getRegyn().equals("N") && whoisVo.getSscreteYn().equals("Y")) {
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
											
											/***************************
											 *  라우팅 점검 업데이트
											 ***************************/
											PrintLogUtil.printLog("#####ASSIGN MST SEQ: " + tmpSelVo.getNipAssignMstSeq());
											TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
											tbRoutChkMstVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq());	
											routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);
											

											/* 이력관리 이력 등록 */
											IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
											//ipHistoryMstVo.setSmenuNm("SDN");

											CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
											
											String sCreateId = paramVo.getWorkerid() == null ? "SDN" : paramVo.getWorkerid();
//											sCreateId = sCreateId == "" ? "SDN" : paramVo.getWorkerid(); //Codeeyes-Urgent-객체 비교시 == , != 사용제한
											sCreateId = sCreateId.equals("") ? "SDN" : paramVo.getWorkerid();
											
											BigInteger nipHistTaskCd = null;
											nipHistTaskCd = CommonCodeUtil.HIST_TASK_CANCEL; // 해지

											ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
											ipHistoryMstVo.setsMenuHistCd("SDN");
											ipHistoryMstVo.setSassignLevelCd("IA0004");  // 할당상태에서 해지를 하면 서비스배정(미할당)으로 돌아감
											ipHistoryMstVo.setScreateId(sCreateId);
											
											historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
										}
										
									}
									
									
									if(nResult == -1){
										sExceptionMSG= paramVo.getIpBlock();
										sExceptionCD="LNK.HIGH.00043";
										throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
									} else if(nResult == -2){
										sExceptionMSG= paramVo.getIpBlock();
										sExceptionCD="LNK.HIGH.00044";
										throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
									} else if(nResult != 0){
										if(nResult != 998 || nResult != 997 || nResult != 996) {
											return nResult;
										} else {
											sExceptionMSG= paramVo.getIpBlock();
											sExceptionCD="LNK.HIGH.00045";
											throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
										}
										
									} 
								} //for-2 end
								
								
								paramMap.clear();
								
							} // for-1 end
							
						}
						
						
						if(nResult == -1){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00043";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult == -2){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00044";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult != 0){
							if(nResult != 998 || nResult != 997 || nResult != 996) {
								return nResult;
							} else {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
							
						} 
						
						paramMap.clear();
						
					}
				} 
				
				
				if(!neossWs) {
					if("Y".equals(regYn) && countAlloc > 0) {
						nResult = 997;
					} else if("N".equals(regYn) && countAlloc == 0) {
						nResult = 996;
					} else {
						nResult = 998;
					}
				}
				
				if(nResult!=0){
					if(nResult != 998 || nResult != 997 || nResult != 996) {
						PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
					} else {
						sExceptionMSG="IP Block 할당 정보 연동";
						sExceptionCD="LNK.HIGH.00032";	
						throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
					}
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
		 * Assign Mst 조회
		 * @param tbIpAlloLinkOperMstVo
		 * @return
		 */
		public TbIpAlloLinkOperMstVo selectMstVo (TbIpAlloLinkOperMstVo tbIpAlloLinkOperMstVo) {
			
			String sExceptionMSG="";
			String sExceptionCD="LNK.INFO.00006";
			
			TbIpAlloLinkOperMstVo resultVo = new TbIpAlloLinkOperMstVo();
			
			try {
				resultVo = lnkIpmsDao.selectMstVo(tbIpAlloLinkOperMstVo);
			} catch(ServiceException e){
				throw e;
			} catch (Exception e) {
				linkUtil.setSystemERR(e);
				throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
			}
			
			
			return resultVo;
		}
		
		/**
		 * @MEthod 	: selectFixedIPListSdn
		 * @DESC	: SDN_IPM_WS_0003 selectFixedIPListSdn(): IPMS IP 할당 정보 조회
		 * @변경이력 	:
		 * @param stcRequest
		 * @return
		 */
		@Transactional(readOnly = true)
		public List<TbIpAlloLinkOperMstVo> selectFixedIPListSdn (Sdn0003Request stcRequest){
			
			List<TbIpAlloLinkOperMstVo> returnListvo = new ArrayList<TbIpAlloLinkOperMstVo>();
			
			TbIpAlloLinkOperMstVo paramVo = new TbIpAlloLinkOperMstVo();

			paramVo.setSsaid(stcRequest.getSaid());		// 서비스계약ID
				
			try{
				returnListvo = lnkIpmsDao.selectFixedIPListSdn(paramVo);
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
		 * @MEthod 	: selectFixedIPSdn
		 * @DESC	: SDN_IPM_WS_0004 selectFixedIPSdn(): 시설정보조회
		 * @변경이력 	:
		 * @param stcRequest
		 * @return
		 */
		@Transactional(readOnly = true)
		public TbIpAlloLinkOperMstVo selectFixedIPSdn (Sdn0004Request stcRequest){
			
			TbIpAlloLinkOperMstVo returnvo = new TbIpAlloLinkOperMstVo();
			
			TbIpAlloLinkOperMstVo paramVo = new TbIpAlloLinkOperMstVo();

			paramVo.setSipBlock(stcRequest.getIpBlock());		// ip block
			paramVo.setNbitmask(linkUtil.getIntgerFromString(stcRequest.getIpBistmask()));  // bitmask
				
			try{
				returnvo = lnkIpmsDao.selectFixedIPSdn(paramVo);
			}catch(ServiceException e){
				throw e;
			}
			catch(Exception e){
				linkUtil.setSystemERR(e);
				throw new ServiceException("CMN.HIGH.00000");
			}
			
			return returnvo;
		}
		
		/**
		 * @MEthod 	: insertReservedIPListSdn
		 * @DESC	: SDN_IPM_WS_0005 insertReservedIPListSdn(): IP Block 할당 예약 정보 연동
		 * @변경이력 	:
		 * @param tbIpAllocNeossMstVo
		 * @return
		 * @throws Exception 
		 */
		@Transactional(propagation = Propagation.REQUIRED)
		public int insertReservedIPListSdn(String regYn, List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVoList){
			
			int nResult = 999;
			String sExceptionMSG="";
			String sExceptionCD="LNK.INFO.00006";
			Boolean neossWs = false;
			HashMap<String,Object> paramMap = new HashMap<String, Object>();		
			
			int countAlloc = 0;

			// 할당 예약 등록
			try{		
								
				if("Y".equals(regYn)) { 				// 할당 예약 등록
					
					for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){

						//ParamSet
						paramMap.put("sordernum", paramVo.getSordernum());
						paramMap.put("regyn", paramVo.getRegyn());
						paramMap.put("said", paramVo.getSaid());
						paramMap.put("nip_assign_mst_seq", paramVo.getNipAssignMstSeq());
						paramMap.put("ssvc_line_type_cd", paramVo.getssvcLineTypeCd());
						paramMap.put("ip_block", paramVo.getIpBlock());
						paramMap.put("ipbitmask", paramVo.getIpbitmask());
						paramMap.put("gatewayIp", paramVo.getGatewayip());   // 2021.01.04 추가
						
						
						PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>paramMap: " + paramMap.get("nip_assign_mst_seq"));
						PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>>>paramVo: " + paramVo.getNipAssignMstSeq());
						PrintLogUtil.printLogValueObject(paramVo);
						
						countAlloc = lnkIpmsDao.countAlloc(paramVo);	// 할당예약 건수 체크
						
						if(countAlloc > 0) {
							nResult = 997;
						} else {
							
							nResult = lnkIpmsDao.insertReservedIPListSdn(paramMap);
							
							/**************************
							 * SDN 예약 정보 NeOSS 전송
							 **************************/
							// 할당예약 성공시에만 NeOSS 전송
							if(nResult == 0) {
								// 할당예약 성공 시 할당테이블 조회
								TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();
								List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>();	//연동 처리 관련
								
								TbIpAlloLinkOperMstVo tmpSelVo = new TbIpAlloLinkOperMstVo();
								
								tmpSelVo = lnkIpmsDao.selectMstAllocVo(paramVo);
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>tmpSelVo.getNipmsSvcSeq(): " + tmpSelVo.getNipmsSvcSeq());
								PrintLogUtil.printLogInfo(">>>>>>>>>>>>>tmpSelVo.getNipAssignMstSeq(): " + tmpSelVo.getNipAssignMstSeq());
								//연동 대상 데이터 복제
								TbIpAllocNeossMstVo linkAllocNeossMstVo = new TbIpAllocNeossMstVo();
								
								linkAllocNeossMstVo.setSordernum(tmpSelVo.getSordernum());  //sordernum NeOSS 오더번호
								linkAllocNeossMstVo.setSaid(tmpSelVo.getSsaid()); 				//ssaid 서비스계약ID
								linkAllocNeossMstVo.setRegyn("Y"); 											//할당요청/취소 구분 [ 예약 = Y, 취소 = N ]
								linkAllocNeossMstVo.setIpmsSvcSeq(tmpSelVo.getNipmsSvcSeq()); //nipms_svc_seq  IPMS 상품MST Seq
								linkAllocNeossMstVo.setAssignTypeCd(tmpSelVo.getSassignTypeCd()); // sassign_type_cd  IP할당유형
								linkAllocNeossMstVo.setExSvcCd(tmpSelVo.getSexSvcCd()); 	//sex_svc_cd
								linkAllocNeossMstVo.setSvcUseTypeCd(tmpSelVo.getSsvcUseTypeCd()); //ssvc_use_type_cd 서비스 이용목정 [사업용 = SU0001 / 일반용 = SU0002 ]
								linkAllocNeossMstVo.setIpVersionTypeCd(tmpSelVo.getSipVersionTypeCd()); //sip_version_type_cd IPv6 = CV0002/ IPv4 CV0001
								linkAllocNeossMstVo.setIpCreateTypeCd(tmpSelVo.getSipCreateTypeCd()); //sip_create_type_cd   CT0001 = 공인 
								linkAllocNeossMstVo.setIpBlock(tmpSelVo.getSipBlock()); 		// sip_block     fe01:1:1::
								linkAllocNeossMstVo.setIpbitmask(tmpSelVo.getNbitmask()); 	//nbitmask      64
								linkAllocNeossMstVo.setSipaddr(tmpSelVo.getSfirstAddr()); 	//sfirst_addr  fe01:1:1::
								linkAllocNeossMstVo.setEipaddr(tmpSelVo.getSlastAddr()); 	//slast_addr fe01:1:1:0:ffff:ffff:ffff:ffff
								linkAllocNeossMstVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq()); // nip_assign_mst_seq 48
								linkAllocNeossMstVo.setGatewayip(tmpSelVo.getGatewayip()); //sgatewayip fe01:1:1:0:ffff:ffff:ffff:ffff
								//2014.11.13 연동 변경에 따른 추가(이중회선, 망코드 추가)
								if (tmpSelVo.getNipAllocMstCnt().equals(BigInteger.ONE)){
									linkAllocNeossMstVo.setSlacpBlockYN("N");
								}else{
									linkAllocNeossMstVo.setSlacpBlockYN("Y");
								}
								linkAllocNeossMstVo.setssvcLineTypeCd(tmpSelVo.getSsvcLineTypeCd());
								
								linkIpAllocNeossVos.add(linkAllocNeossMstVo);
								
								tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
								neossWs = neOSSConsumeAdapterService.insertReservedIPList(tbIpAllocNeossMstListVo); 
								
								if(neossWs) {

									/* 이력관리 이력 등록 */
									IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
									//ipHistoryMstVo.setSmenuNm("SDN");
									
									List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
									IpHistoryMstVo selVo = new IpHistoryMstVo();
									IpHistoryMstVo searchVo = new IpHistoryMstVo();
									
									searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
									//searchVo.setSsaid(paramVo.getSaid());
									selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);

									PrintLogUtil.printLogInfo("=============================selVoList.size() : " + selVoList.size() + "==================");
									if(selVoList.size() > 0) {
										
										selVo = selVoList.get(0);
										PrintLogUtil.printLogInfo("=============================selVoList==================");
										PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
										PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
										PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
										PrintLogUtil.printLogInfo("======================================================");
										
									}
									
									String sCreateId = paramVo.getWorkerid() == null ? "SDN" : paramVo.getWorkerid();
									

									CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
									
									
									BigInteger nipHistTaskCd = null;
									nipHistTaskCd = CommonCodeUtil.HIST_TASK_ALLOC_RESV; // 할당
									
									ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
									ipHistoryMstVo.setsMenuHistCd("SDN");
									ipHistoryMstVo.setScreateId(sCreateId);
									
									historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
									
								}
							}
							
						}
						
						if(nResult == -1){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00043";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult == -2){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00044";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult != 0){
							if(nResult != 998 || nResult != 997 || nResult != 996) {
								return nResult;
							} else {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
						}
						
						paramMap.clear();
						
					}
					
					
				} else if("N".equals(regYn)) {	    // 할당예약 해지 
					
					for(TbIpAllocNeossMstVo paramVo : tbIpAllocNeossMstVoList){
						
						countAlloc = lnkIpmsDao.countAlloc(paramVo);	// 해지 건수 체크
						
						if(countAlloc == 0) {
							nResult = 996;
						} else {
							List<TbIpAlloLinkOperMstVo> ssaidList = lnkIpmsDao.selectSsaidVo(paramVo);
							
							if(ssaidList.size() == 0) {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
							
							for(TbIpAlloLinkOperMstVo vo : ssaidList) { // for-1 start
								
								PrintLogUtil.printLogInfo(">>>>>>>>>>vo.getNipAssignMstSeq(): " + vo.getNipAssignMstSeq());
								
								TbIpAllocNeossMstVo delVo = new TbIpAllocNeossMstVo();
								delVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
								delVo.setSaid(vo.getSsaid());
								
								TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();
								List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>();	//연동 처리 관련
								// TbIpAllocNeossMstVo tmpVo = new TbIpAllocNeossMstVo();
								// TbIpAlloLinkOperMstVo tmpSelVo = new TbIpAlloLinkOperMstVo();
								TbIpAllocNeossMstVo pVo = new TbIpAllocNeossMstVo();
								
								pVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
								PrintLogUtil.printLogInfo(">>>>>>>>>>pVo: " + pVo.getNipAssignMstSeq());
								
								// ########################################################
								// tmpSelVo = lnkIpmsDao.selectMstAllocVo(pVo); //20200512 단일에서 List로 수정
								List<TbIpAlloLinkOperMstVo> tempTmpSelVoList = lnkIpmsDao.selectMstAllocVoList(pVo);
								
								for(TbIpAlloLinkOperMstVo tmpSelVo:tempTmpSelVoList){ // for-2 start
									
									PrintLogUtil.printLogInfo(">>>>>>>>>>tmpSelVo.getSordernum(): " + tmpSelVo.getSordernum());
									PrintLogUtil.printLogInfo(">>>>>>>>>>tmpSelVo.getSsaid(): " + tmpSelVo.getSsaid());
									PrintLogUtil.printLogInfo(">>>>>>>>>>tmpSelVo.getNipmsSvcSeq(): " + tmpSelVo.getNipmsSvcSeq());
									
									TbIpAllocNeossMstVo linkAllocNeossMstVo = new TbIpAllocNeossMstVo();
									linkAllocNeossMstVo.setSordernum(tmpSelVo.getSordernum());  //sordernum NeOSS 오더번호
									linkAllocNeossMstVo.setSaid(tmpSelVo.getSsaid()); 				//ssaid 서비스계약ID
									linkAllocNeossMstVo.setRegyn("N"); 											//할당요청/취소 구분 [ 예약 = Y, 취소 = N ]
									linkAllocNeossMstVo.setIpmsSvcSeq(tmpSelVo.getNipmsSvcSeq()); //nipms_svc_seq  IPMS 상품MST Seq
									linkAllocNeossMstVo.setAssignTypeCd(tmpSelVo.getSassignTypeCd()); // sassign_type_cd  IP할당유형
									linkAllocNeossMstVo.setExSvcCd(tmpSelVo.getSexSvcCd()); 	//sex_svc_cd
									linkAllocNeossMstVo.setSvcUseTypeCd(tmpSelVo.getSsvcUseTypeCd()); //ssvc_use_type_cd 서비스 이용목정 [사업용 = SU0001 / 일반용 = SU0002 ]
									linkAllocNeossMstVo.setIpVersionTypeCd(tmpSelVo.getSipVersionTypeCd()); //sip_version_type_cd IPv6 = CV0002/ IPv4 CV0001
									linkAllocNeossMstVo.setIpCreateTypeCd(tmpSelVo.getSipCreateTypeCd()); //sip_create_type_cd   CT0001 = 공인 
									linkAllocNeossMstVo.setIpBlock(tmpSelVo.getSipBlock()); 		// sip_block     fe01:1:1::
									linkAllocNeossMstVo.setIpbitmask(tmpSelVo.getNbitmask()); 	//nbitmask      64
									linkAllocNeossMstVo.setSipaddr(tmpSelVo.getSfirstAddr()); 	//sfirst_addr  fe01:1:1::
									linkAllocNeossMstVo.setEipaddr(tmpSelVo.getSlastAddr()); 	//slast_addr fe01:1:1:0:ffff:ffff:ffff:ffff
									linkAllocNeossMstVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq()); // nip_assign_mst_seq 48
									linkAllocNeossMstVo.setGatewayip(tmpSelVo.getGatewayip()); //sgatewayip fe01:1:1:0:ffff:ffff:ffff:ffff
									
									//2014.11.13 연동 변경에 따른 추가(이중회선, 망코드 추가)
									if (tmpSelVo.getNipAllocMstCnt().equals(BigInteger.ZERO)){ //단일회선 해지시 0임으로 이중화 회선아님.
										linkAllocNeossMstVo.setSlacpBlockYN("N");
									}else{
										linkAllocNeossMstVo.setSlacpBlockYN("Y");
									}
									linkAllocNeossMstVo.setssvcLineTypeCd(tmpSelVo.getSsvcLineTypeCd());
								
									linkIpAllocNeossVos.add(linkAllocNeossMstVo);
									
									countAlloc = lnkIpmsDao.countAlloc(linkAllocNeossMstVo);	// 해지 건수 체크
									
									if(countAlloc == 0) {
										nResult = 996;
									} else {
										
										tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
										neossWs = neOSSConsumeAdapterService.insertReservedIPList(tbIpAllocNeossMstListVo); 
										
										List<IpHistoryMstVo> selVoList = new ArrayList<IpHistoryMstVo>();
										IpHistoryMstVo selVo = new IpHistoryMstVo();
										IpHistoryMstVo searchVo = new IpHistoryMstVo();
										
										searchVo.setNipAssignMstSeq(tmpSelVo.getNipAssignMstSeq());
										//searchVo.setSsaid(paramVo.getSaid());
										selVoList = historyMgmtAdapterService.selectAllocIpInfoList(searchVo);
										
										PrintLogUtil.printLogInfo("=============================delVoList.size() : " + selVoList.size() + "==================");
										if(selVoList.size() > 0) {
											
											selVo = selVoList.get(0);
											PrintLogUtil.printLogInfo("=============================selVoList==================");
											PrintLogUtil.printLogInfo("selVo.getNipAssignMstSeq() = " + selVo.getNipAssignMstSeq());
											PrintLogUtil.printLogInfo("selVo.getNipAllocMstSeq() = " + selVo.getNipAllocMstSeq());
											PrintLogUtil.printLogInfo("selVo.getSsaid() = " + selVo.getSsaid());
											PrintLogUtil.printLogInfo("======================================================");
											
										}
										
										// NeOSS 연동 성공 시에 IPMS 해지
										if(neossWs) {
											//ParamSet
											paramMap.put("sordernum", paramVo.getSordernum());
											paramMap.put("regyn", paramVo.getRegyn());
											paramMap.put("said", paramVo.getSaid());
											paramMap.put("nip_assign_mst_seq", paramVo.getNipAssignMstSeq());
											paramMap.put("ssvc_line_type_cd", paramVo.getssvcLineTypeCd());
											paramMap.put("ip_block", paramVo.getIpBlock());
											paramMap.put("ipbitmask", paramVo.getIpbitmask());
											paramMap.put("gatewayIp", paramVo.getGatewayip());   // 2021.01.04 추가

											nResult = lnkIpmsDao.insertReservedIPListSdn(paramMap);
											
											

											/* 이력관리 이력 등록 */
											IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
											//ipHistoryMstVo.setSmenuNm("SDN");

											CloneUtil.copyObjectInformation(selVo, ipHistoryMstVo);
											
											String sCreateId = paramVo.getWorkerid() == null ? "SDN" : paramVo.getWorkerid();
											
											BigInteger nipHistTaskCd = null;
											nipHistTaskCd = CommonCodeUtil.HIST_TASK_CANCEL_RESV; // 해지

											ipHistoryMstVo.setNipHistTaskCd(nipHistTaskCd);  // 할당, 해지
											ipHistoryMstVo.setsMenuHistCd("SDN");
											ipHistoryMstVo.setSassignLevelCd("IA0004");  // 할당상태에서 해지를 하면 서비스배정(미할당)으로 돌아감
											ipHistoryMstVo.setScreateId(sCreateId);
											
											historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
											
										}
									}
									
									
									if(nResult == -1){
										sExceptionMSG= paramVo.getIpBlock();
										sExceptionCD="LNK.HIGH.00043";
										throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
									} else if(nResult == -2){
										sExceptionMSG= paramVo.getIpBlock();
										sExceptionCD="LNK.HIGH.00044";
										throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
									} else if(nResult != 0){
										if(nResult != 998 || nResult != 997 || nResult != 996) {
											return nResult;
										} else { 
											sExceptionMSG= paramVo.getIpBlock();
											sExceptionCD="LNK.HIGH.00045";
											throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
										}
										
									} 
								} //for-2 end
								
								
								paramMap.clear();
								
							} // for-1 end
							
						}
						
						
						if(nResult == -1){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00043";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult == -2){
							sExceptionMSG= paramVo.getIpBlock();
							sExceptionCD="LNK.HIGH.00044";
							throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
						} else if(nResult != 0){
							if(nResult != 998 || nResult != 997 || nResult != 996) {
								return nResult;
							} else {
								sExceptionMSG= paramVo.getIpBlock();
								sExceptionCD="LNK.HIGH.00045";
								throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
							}
							
						} 
						
						paramMap.clear();
						
					}
				} 
				
				
				if(!neossWs) {
					if("Y".equals(regYn) && countAlloc > 0) {
						nResult = 997;
					} else if("N".equals(regYn) && countAlloc == 0) {
						nResult = 996;
					} else {
						nResult = 998;
					}
				}
				
				if(nResult!=0){
					if(nResult != 998 || nResult != 997 || nResult != 996) {
						PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
					} else {
						sExceptionMSG="IP Block 할당예약 취소 정보 연동";
						sExceptionCD="LNK.HIGH.00032";	
						throw new ServiceException(sExceptionCD, new String[]{sExceptionMSG});
					}
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
}
