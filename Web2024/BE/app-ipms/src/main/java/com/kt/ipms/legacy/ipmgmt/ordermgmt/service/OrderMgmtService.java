package com.kt.ipms.legacy.ipmgmt.ordermgmt.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter.AssignMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.adapter.LineMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstListVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.adapter.NeOSSConsumeAdapterService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.adapter.SvcMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.adapter.TicketMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;

@Component
@Transactional
public class OrderMgmtService {
	
	@Autowired
	private OrderMgmtTxService orderMgmtTxService;
	
	@Autowired
	private SvcMgmtAdapterService svcMgmtAdapterService;
	
	@Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;
	
	@Autowired
	private AssignMgmtAdapterService assignMgmtAdapterService;
	
	@Autowired
	private LineMgmtAdapterService lineMgmtAdapterService;
	
	@Autowired
	private NeOSSConsumeAdapterService neOSSConsumeAdapterService;
	
	@Autowired
	private TicketMgmtAdapterService ticketMgmtAdapterService;

	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;
	
	/*오더 메인 조회*/
	@Transactional(readOnly = true)
	public IpAllocOrderMstListVo selectListIpAllocOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo){
		IpAllocOrderMstListVo resultListVo = null;
		if (ipAllocOrderMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOrderMstVo> resultList = orderMgmtTxService.selectListIpAllocOrderMst(ipAllocOrderMstVo);
			int totalCount = orderMgmtTxService.countListPageIpAllocOrderMst(ipAllocOrderMstVo);
			resultListVo = new IpAllocOrderMstListVo();
			resultListVo.setIpAllocOrderMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"오더할당대상목록"});
		}
		return resultListVo;
	}
	
	/*오더 메인 조회 Excel*/
	@Transactional(readOnly = true)
	public IpAllocOrderMstListVo selectListIpAllocOrderMstExcel(IpAllocOrderMstVo ipAllocOrderMstVo){
		IpAllocOrderMstListVo resultListVo = null;
		if (ipAllocOrderMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = orderMgmtTxService.countListPageIpAllocOrderMst(ipAllocOrderMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpAllocOrderMstVo> resultList = null;
			if (totalCount > 0) {
				ipAllocOrderMstVo.setFirstIndex(1);
				ipAllocOrderMstVo.setLastIndex(totalCount);
				resultList = orderMgmtTxService.selectListIpAllocOrderMst(ipAllocOrderMstVo);	
			}
			
			resultListVo = new IpAllocOrderMstListVo();
			resultListVo.setIpAllocOrderMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"오더할당대상목록"});
		}
		return resultListVo;
	}
	
	/* 상품정보 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListIpmsSvc(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtAdapterService.selectListIpmsSvc(tbIpmsSvcMstVo);
	}
	
	/* 대분류 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtAdapterService.selectListSvcMgroupNm(tbIpmsSvcMstVo);
	}
	
	/* 소분류 조회  */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm1(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtAdapterService.selectListSvcMgroupNm1(tbIpmsSvcMstVo);
	}
	
	/* 오더 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo){
		return allocMgmtAdapterService.selectListIpAllocMst(ipAllocOperMstVo);
	}
	
	/*서비스 정보 셋팅*/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListSassignLevelCds(IpAllocOperMstVo ipAllocOperMstVo){
		return orderMgmtTxService.selectListSassignLevelCds(ipAllocOperMstVo);
	}
	
	/*블록 할당 처리*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListOrderAlcIPMst(IpAllocOrderMstComplexVo ipAllocOrderMstComplexVo){
		try {
			if (ipAllocOrderMstComplexVo == null || ipAllocOrderMstComplexVo.getSrcIpAllocOrderMstVo() == null 
					|| ipAllocOrderMstComplexVo.getDestIpAllocOrderMstVos() == null || ipAllocOrderMstComplexVo.getDestIpAllocOrderMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			IpAllocOrderMstVo srcIpAllocOrderMstVo = ipAllocOrderMstComplexVo.getSrcIpAllocOrderMstVo();
			List<IpAllocOperMstVo> destIpAllocMstVos = ipAllocOrderMstComplexVo.getDestIpAllocOrderMstVos();
			
			IpAllocOrderMstVo nipmsSvcSeqMstVo = new IpAllocOrderMstVo(); //상품정보 셋팅용
			IpAllocOperMstVo srchNipmsSvcSeqMstVo = new IpAllocOperMstVo(); //상품정보 셋팅용
			
			IpAllocOperMstVo updateIpAllocMstVo = new IpAllocOperMstVo(); //배정테이블
			
			List<TbIpAllocMstVo> insertIpAllocMstVos = new ArrayList<TbIpAllocMstVo>();			//할당 처리 관련
			List<TbIpAssignSubVo> insertIpAssignSubVos = new ArrayList<TbIpAssignSubVo>();	//선번장 처리 관련
			List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();	//배정 처리 관련
			List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>();	//연동 처리 관련
			
			//연동용 VO 생성
			TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();
			//String sNote = srcIpAllocMstVo.getScomment();
			
			//오더 할당 기본 정보 조회
			if(srcIpAllocOrderMstVo.getNipAllocOrderSeq() != null){
				
				IpAllocOrderMstVo preIpAllocOrderMstVo = new IpAllocOrderMstVo();
				preIpAllocOrderMstVo.setNipAllocOrderSeq(srcIpAllocOrderMstVo.getNipAllocOrderSeq());
				
				//등록,수정자 ID
				String sUserId = srcIpAllocOrderMstVo.getScreateId();
				
				//<STEP 01. 오더정보 읽기>
				List<IpAllocOrderMstVo> resultList = orderMgmtTxService.selectListIpAllocOrderMst(preIpAllocOrderMstVo);
				
				if (resultList.size() == 1){
					for (IpAllocOrderMstVo baseAllocOrderMstVo : resultList) {
						//<STEP 01-01. 오더정보 복사>
						CloneUtil.copyObjectInformation(baseAllocOrderMstVo, srcIpAllocOrderMstVo);
					}
					
					srcIpAllocOrderMstVo.setScreateId(sUserId);
					srcIpAllocOrderMstVo.setSmodifyId(sUserId);
					
					for (IpAllocOperMstVo ipAllocOperMstVo : destIpAllocMstVos) {
						
						IpAllocOperMstVo searchIpAllocMstVo = new IpAllocOperMstVo(); //배정테이블
						
						//<Step 02-01. 조회조건 (할당대상 블록) 복사  => 블록마스터>
						//배정정보 확인을 위한 기준값 복사
						searchIpAllocMstVo.setSsvcLineTypeCd(ipAllocOperMstVo.getSsvcLineTypeCd());
						searchIpAllocMstVo.setSipCreateTypeCd(ipAllocOperMstVo.getSipCreateTypeCd());
						searchIpAllocMstVo.setNipAssignMstSeq(ipAllocOperMstVo.getNipAssignMstSeq());
						searchIpAllocMstVo.setSassignTypeCd(ipAllocOperMstVo.getSassignTypeCd());
						searchIpAllocMstVo.setSgatewayip(ipAllocOperMstVo.getSgatewayip());
						searchIpAllocMstVo.setNlvlMstSeq(ipAllocOperMstVo.getNlvlMstSeq());
						
						//<Step 02-02. 이중회선 계산 관련>
						//중복 회선 처리
						BigInteger iAllcnt = BigInteger.ZERO;
						iAllcnt = ipAllocOperMstVo.getNipAllocMstCnt();
						iAllcnt = iAllcnt.add(BigInteger.ONE);
						searchIpAllocMstVo.setNipAllocMstCnt(iAllcnt);
						
						//<Step 02-03. 파라미터 셋팅(조회/오더) => 블록마스터>
						//입력 대상 정보에 오더정보 매핑 
						CloneUtil.copyObjectInformation(srcIpAllocOrderMstVo, ipAllocOperMstVo);
						
						//<STEP 02-04. 배정정보 조회>
						//입력 대상 정보에 배정정보 매핑 (IP블록 기본정보)
						updateIpAllocMstVo = allocMgmtAdapterService.selectIpAllocMst(searchIpAllocMstVo);
						
						//<STEP 02-05. 배정블록 IP기본정보 복사 (배정 => 할당)>
						CloneUtil.copyIpVoInfomation(updateIpAllocMstVo, ipAllocOperMstVo);
						
						//<STEP 02-06. 오더정보 복사 (오더 => 할당)>
						ipAllocOperMstVo.setNlvlMstSeq(searchIpAllocMstVo.getNlvlMstSeq());
						ipAllocOperMstVo.setSsvcLineTypeCd(searchIpAllocMstVo.getSsvcLineTypeCd());
						ipAllocOperMstVo.setSipCreateTypeCd(searchIpAllocMstVo.getSipCreateTypeCd());
						ipAllocOperMstVo.setNipAssignMstSeq(searchIpAllocMstVo.getNipAssignMstSeq());
						ipAllocOperMstVo.setSassignTypeCd(searchIpAllocMstVo.getSassignTypeCd());
						ipAllocOperMstVo.setNipAllocMstCnt(searchIpAllocMstVo.getNipAllocMstCnt());
						ipAllocOperMstVo.setSgatewayip(searchIpAllocMstVo.getSgatewayip());
						ipAllocOperMstVo.setSexPushYn("Y"); //insert 시점에 고정(회선 - 연동정보 IPMS=> NEOSS)
						ipAllocOperMstVo.setNticketActSeq(BigInteger.ZERO); //insert 시점에 고정
						ipAllocOperMstVo.setScreateId(sUserId);
						ipAllocOperMstVo.setSmodifyId(sUserId);
						ipAllocOperMstVo.setScomment("");
						
						String sAllocFlg = updateIpAllocMstVo.getSassignLevelCd();
						
						if (sAllocFlg.equals("IA0004")){ //IA0004 : 서비스배정
							ipAllocOperMstVo.setSassignLevelCd("IA0005"); //updateTbIpAllocMstVo IA0005:할당예약
						}else{
							ipAllocOperMstVo.setSassignLevelCd(sAllocFlg); //updateTbIpAllocMstVo 할당예약, 할당은 그대로 유지
						}
						
						//<STEP 04-01-01. 할당 등록 용 블록 정보 복사 (기본:배정 정보 => 할당)>
						//등록 할당 데이터 복제 
						TbIpAllocMstVo insertIpAllocMstVo = new TbIpAllocMstVo();
						CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAllocMstVo);
						
						
						//<STEP 04-01-02. 할당 등록 용 상품코드 셋팅 및 복사 (상품 => 할당)>
						//NIPMS_SVC_SEQ 구하기(파라미터 : SEX_SVC_CD, SSVC_USE_TYPE_CD, SASSIGN_TYPE_CD)
						srchNipmsSvcSeqMstVo.setSexSvcCd(ipAllocOperMstVo.getSexSvcCd());
						srchNipmsSvcSeqMstVo.setSsvcUseTypeCd(ipAllocOperMstVo.getSsvcUseTypeCd());
						srchNipmsSvcSeqMstVo.setSassignTypeCd(ipAllocOperMstVo.getSassignTypeCd());
						
						nipmsSvcSeqMstVo = orderMgmtTxService.selectNipmsSvcSeqMst(srchNipmsSvcSeqMstVo);
						if(nipmsSvcSeqMstVo == null){
							throw new ServiceException("APP.HIGH.00033", new String[]{" (원인 : 상품정보 미존재) IP블록 할당"});
						}
							
						insertIpAllocMstVo.setNipmsSvcSeq(nipmsSvcSeqMstVo.getNipmsSvcSeq());
						ipAllocOperMstVo.setNipmsSvcSeq(nipmsSvcSeqMstVo.getNipmsSvcSeq());
						
						//<STEP 04-01-03. 할당 등록 용 오더정보 복사 (오더 => 할당)>
						//insertIpAllocMstVo.setNipAllocNeossSeq(); //null 처리 없음
						insertIpAllocMstVo.setSordernum(srcIpAllocOrderMstVo.getSordernum());
						insertIpAllocMstVo.setSregyn("O");
						insertIpAllocMstVo.setSodrCloseTypeCd(srcIpAllocOrderMstVo.getSodrCloseTypeCd());
						insertIpAllocMstVo.setSsaid(srcIpAllocOrderMstVo.getSsaid());
						insertIpAllocMstVo.setSllnum(srcIpAllocOrderMstVo.getSllnum());
						insertIpAllocMstVo.setSicisofficescode(srcIpAllocOrderMstVo.getSicisofficescode());
						insertIpAllocMstVo.setSlacpsid(srcIpAllocOrderMstVo.getSlacpsid());
						insertIpAllocMstVo.setSsubscnescode(srcIpAllocOrderMstVo.getSsubscnescode());
						insertIpAllocMstVo.setSsubscmstip(srcIpAllocOrderMstVo.getSsubscmstip());
						insertIpAllocMstVo.setSsubsclgipportseq(srcIpAllocOrderMstVo.getSsubsclgipportseq());
						insertIpAllocMstVo.setSsubsclgipportdescription(srcIpAllocOrderMstVo.getSsubsclgipportdescription());
						insertIpAllocMstVo.setSsubsclgipportip(srcIpAllocOrderMstVo.getSsubsclgipportip());
						insertIpAllocMstVo.setSsubscrouterserialip(srcIpAllocOrderMstVo.getSsubscrouterserialip());
						insertIpAllocMstVo.setSsubscnealias(srcIpAllocOrderMstVo.getSsubscnealias());
						insertIpAllocMstVo.setSconnalias(srcIpAllocOrderMstVo.getSconnalias());
						insertIpAllocMstVo.setSmodelname(srcIpAllocOrderMstVo.getSmodelname());
						insertIpAllocMstVo.setScustname(srcIpAllocOrderMstVo.getScustname());
						insertIpAllocMstVo.setSexSvcCd(srcIpAllocOrderMstVo.getSexSvcCd());
						insertIpAllocMstVo.setSsvcUseTypeCd(srcIpAllocOrderMstVo.getSsvcUseTypeCd());
						insertIpAllocMstVo.setSresultCd(srcIpAllocOrderMstVo.getSresultCd());
						insertIpAllocMstVo.setSresultMsg(srcIpAllocOrderMstVo.getSresultMsg());
						insertIpAllocMstVo.setSinstalladdress(srcIpAllocOrderMstVo.getSinstalladdress());
						insertIpAllocMstVo.setSinstallroadaddress(srcIpAllocOrderMstVo.getSinstallroadaddress());
						insertIpAllocMstVo.setSreportOpinion(srcIpAllocOrderMstVo.getSreportOpinion());
						insertIpAllocMstVo.setSodrhopedt(srcIpAllocOrderMstVo.getSodrhopedt());
						insertIpAllocMstVo.setSodrregdt(srcIpAllocOrderMstVo.getSodrregdt());
						insertIpAllocMstVo.setSofficecode(srcIpAllocOrderMstVo.getSofficecode());
						
						insertIpAllocMstVos.add(insertIpAllocMstVo);
						
						//<STEP 04-02. 선번장 등록용 복사>
						//등록 선번장 데이터 복제
						//<예외 조건> 서비스배정일 경우만 선번장에 데이터 등록 함. 
						if (sAllocFlg.equals("IA0004")){ //IA0004 : 서비스배정
							TbIpAssignSubVo insertIpAssignSubVo = new TbIpAssignSubVo();
							CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAssignSubVo);
							insertIpAssignSubVos.add(insertIpAssignSubVo);
						}
						
						//<STEP 04-03. 배정 수정용 복사>
						//수정 배정 데이터 복제
						TbIpAssignMstVo updateIpAssignMstVo = new TbIpAssignMstVo();
						CloneUtil.copyObjectInformation(ipAllocOperMstVo, updateIpAssignMstVo);
						updateIpAssignMstVo.setSipAllocExTypeCd("AE0003"); //AE0000 (강제할당처리), AE0003(웹서비스)
						destIpAssignMstVos.add(updateIpAssignMstVo);
						
						//<STEP 04-04. 연동용 복사>
						//연동 대상 데이터 복제
						TbIpAllocNeossMstVo linkAllocNeossMstVo = new TbIpAllocNeossMstVo();
						
						linkAllocNeossMstVo.setSordernum(insertIpAllocMstVo.getSordernum());	//sordernum NeOSS 오더번호
						linkAllocNeossMstVo.setSaid(insertIpAllocMstVo.getSsaid());				//ssaid 서비스계약ID
						linkAllocNeossMstVo.setRegyn("Y"); 										//할당요청/취소 구분 [ 예약 = Y, 취소 = N ]
						linkAllocNeossMstVo.setIpmsSvcSeq(insertIpAllocMstVo.getNipmsSvcSeq()); //nipms_svc_seq  IPMS 상품MST Seq
						linkAllocNeossMstVo.setAssignTypeCd(insertIpAllocMstVo.getSassignTypeCd()); // sassign_type_cd  IP할당유형
						linkAllocNeossMstVo.setExSvcCd(insertIpAllocMstVo.getSexSvcCd());		//sex_svc_cd
						linkAllocNeossMstVo.setSvcUseTypeCd(insertIpAllocMstVo.getSsvcUseTypeCd()); //ssvc_use_type_cd 서비스 이용목정 [사업용 = SU0001 / 일반용 = SU0002 ]
						linkAllocNeossMstVo.setIpVersionTypeCd(insertIpAllocMstVo.getSipVersionTypeCd()); //sip_version_type_cd IPv6 = CV0002/ IPv4 CV0001
						linkAllocNeossMstVo.setIpCreateTypeCd(insertIpAllocMstVo.getSipCreateTypeCd()); //sip_create_type_cd   CT0001 = 공인 
						linkAllocNeossMstVo.setIpBlock(insertIpAllocMstVo.getSipBlock());		// sip_block     fe01:1:1::
						linkAllocNeossMstVo.setIpbitmask(insertIpAllocMstVo.getNbitmask()); 	//nbitmask      64
						linkAllocNeossMstVo.setSipaddr(insertIpAllocMstVo.getSfirstAddr()); 	//sfirst_addr  fe01:1:1::
						linkAllocNeossMstVo.setEipaddr(insertIpAllocMstVo.getSlastAddr()); 		//slast_addr fe01:1:1:0:ffff:ffff:ffff:ffff
						linkAllocNeossMstVo.setNipAssignMstSeq(insertIpAllocMstVo.getNipAssignMstSeq()); // nip_assign_mst_seq 48
						linkAllocNeossMstVo.setGatewayip(insertIpAllocMstVo.getSgatewayip()); 	//sgatewayip fe01:1:1:0:ffff:ffff:ffff:ffff
						
						//2014.11.13 연동 변경에 따른 추가(이중회선, 망코드 추가)
						if (ipAllocOperMstVo.getNipAllocMstCnt().equals(BigInteger.ONE)){
							linkAllocNeossMstVo.setSlacpBlockYN("N");
						}else{
							linkAllocNeossMstVo.setSlacpBlockYN("Y");
						}
						linkAllocNeossMstVo.setssvcLineTypeCd(insertIpAllocMstVo.getSsvcLineTypeCd());
						
						linkIpAllocNeossVos.add(linkAllocNeossMstVo);
						
					}
					
					//<STEP 05-01. Neoss 연동호출> 연동 처리 정보 호출부(연동 결과에 따른 처리는 연동 추가 후 진행예정)
					if (linkIpAllocNeossVos != null && linkIpAllocNeossVos.size() > 0) {
						tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
						neOSSConsumeAdapterService.insertReservedIPList(tbIpAllocNeossMstListVo);
					}
					
					//<STEP 05-02-01. IPMS 할당호출> 할당 처리
					if (insertIpAllocMstVos != null && insertIpAllocMstVos.size() > 0) {
						allocMgmtAdapterService.insertIPAllocMst(insertIpAllocMstVos);
						
						//<STEP 05-02-02. IPMS 할당호출 처리후 매핑> 할당 처리 key Mapping( 
						for (TbIpAllocMstVo keyIpAllocOperMstVo : insertIpAllocMstVos) {
							for (TbIpAssignSubVo desIpAllocOperMstVo : insertIpAssignSubVos) {
								if(keyIpAllocOperMstVo.getNipAssignMstSeq().equals(desIpAllocOperMstVo.getNipAssignMstSeq())){
									desIpAllocOperMstVo.setNipAllocMstSeq(keyIpAllocOperMstVo.getNipAllocMstSeq());
								}
							}
						}
					}
					
				    //<STEP 05-03. IPMS 선번장 호출> 할당 서브(선번장) 처리
					if (insertIpAssignSubVos != null && insertIpAssignSubVos.size() > 0) {
						lineMgmtAdapterService.insertIpAssignSub(insertIpAssignSubVos);
					}
				    
				    //<STEP 05-04. IPMS 배정 변경 호출> 배정 변경(할당) 처리
					if (destIpAssignMstVos != null && destIpAssignMstVos.size() > 0) {
						assignMgmtAdapterService.updateAlocIpAssignMst(destIpAssignMstVos);
					}
					
					//<STEP 05-05. IPMS(BIF) 오더 변경 호출> 배정 변경(할당) 처리 (할당처리-종료) : 연동에서 처리함으로 해당 처리 하지않는다(2014.11.18)
					/*
					if (srcIpAllocOrderMstVo.getNipAllocOrderSeq() != null) {
						orderMgmtTxService.updateFinIpAllocOrderMst(srcIpAllocOrderMstVo);
					}
					*/
					
					/* 이력관리 이력 등록 */
					//<STEP 05-06.  Ticket정보 처리(할당)>  Ticket정보 처리(할당)
					for (TbIpAssignMstVo destTicketTbIpAssignMstVo : destIpAssignMstVos) {
						/* Step 1. 배정블록 조회
						 * - Method 호출 및 리턴 셋팅
						 */
						TbIpAssignMstVo srchTicketIpAssignMstVo = new TbIpAssignMstVo();
						TbIpAssignMstVo insertTicketTbIpAssignMstVo = new TbIpAssignMstVo();
						
						TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();
						IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
						
						
						TbIpmsSvcMstVo srchTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();
						TbIpmsSvcMstVo ssvcTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();
						
						srchTicketIpAssignMstVo.setNipAssignMstSeq(destTicketTbIpAssignMstVo.getNipAssignMstSeq());
						insertTicketTbIpAssignMstVo = assignMgmtAdapterService.selectIpAssignMst(srchTicketIpAssignMstVo);
						
						/* Step 2. 변경 Param Setting (회선, 시설) 
						 * - Step 1의 정보에 상단 셋팅 값 input
						 * - tbTicketMstVo에 블록 정보 (배정) 셋팅 및 기본값 셋팅
						 */
						insertTicketTbIpAssignMstVo.setSassignLevelCd(destTicketTbIpAssignMstVo.getSassignLevelCd());
						insertTicketTbIpAssignMstVo.setNipmsSvcSeq(destTicketTbIpAssignMstVo.getNipmsSvcSeq());
						insertTicketTbIpAssignMstVo.setSexSvcCd(destTicketTbIpAssignMstVo.getSexSvcCd());
						
						
						CloneUtil.copyObjectInformation(insertTicketTbIpAssignMstVo, tbTicketMstVo);
						tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_ALLOC_ALLOC);
						tbTicketMstVo.setScreateId(destTicketTbIpAssignMstVo.getSmodifyId());
						
						IpHistoryMstVo insertHistoryTbIpAllocMstVo = new IpHistoryMstVo();
						IpHistoryMstVo srchHistoryTbIpAllocMstVo = new IpHistoryMstVo();
						
						if (insertIpAllocMstVos != null && insertIpAllocMstVos.size() > 0) {
							for (TbIpAllocMstVo keyIpAllocOperMstVo : insertIpAllocMstVos) {
								
								if(keyIpAllocOperMstVo.getNipAssignMstSeq().equals(insertTicketTbIpAssignMstVo.getNipAssignMstSeq())){
									
									srchHistoryTbIpAllocMstVo.setNipAssignMstSeq(destTicketTbIpAssignMstVo.getNipAssignMstSeq());
									srchHistoryTbIpAllocMstVo.setNipAllocMstSeq(keyIpAllocOperMstVo.getNipAllocMstSeq());
									insertHistoryTbIpAllocMstVo = historyMgmtAdapterService.selectAllocIpInfo(srchHistoryTbIpAllocMstVo);
									
									CloneUtil.copyObjectInformation(insertHistoryTbIpAllocMstVo, ipHistoryMstVo);
									ipHistoryMstVo.setsMenuHistCd("NeossOdr");
									//ipHistoryMstVo.setSmenuNm("NeOSS 오더");
									
									if(insertHistoryTbIpAllocMstVo.getSassignLevelCd().equals("IA0005")){ //할당예약
										ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_ALLOC_RESV);  // 할당예약
									} else { //할당 
										ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_ALLOC);  // 할당
									}
									
									ipHistoryMstVo.setScreateId(ipAllocOrderMstComplexVo.getScreateId());
									

									historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
								}
							}
						}
						
						
						/* Step 3. 상품정보 셋팅 
						 * - 상단 정보에 대하여 상품코드 존재시 해당 정보 셋팅
						 */
						if (destTicketTbIpAssignMstVo.getNipmsSvcSeq() != null){
							srchTicketTbIpmsSvcMstVo.setNipmsSvcSeq(destTicketTbIpAssignMstVo.getNipmsSvcSeq());
							ssvcTicketTbIpmsSvcMstVo = svcMgmtAdapterService.selectTbIpmsSvcMstVo(srchTicketTbIpmsSvcMstVo);
							tbTicketMstVo.setSipmsSvcNm(ssvcTicketTbIpmsSvcMstVo.getSipmsSvcNm());
							tbTicketMstVo.setSsvcUseTypeCd(ssvcTicketTbIpmsSvcMstVo.getSsvcUseTypeCd());
							
							//ipHistoryMstVo.setSipmsSvcNm(ssvcTicketTbIpmsSvcMstVo.getSipmsSvcNm());
							//ipHistoryMstVo.setSsvcUseTypeCd(ssvcTicketTbIpmsSvcMstVo.getSsvcUseTypeCd());
						}
						/** Step 4. 티켓 처리 **/
						ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
						
					}
						
				}else{
					throw new ServiceException("APP.HIGH.00033", new String[]{"(원인 : 오더 기본정보 조회 오류) IP블록 할당"});
				}
				
			}else{
				throw new ServiceException("APP.HIGH.00033", new String[]{"(원인 : 오더 기본정보 없음) IP블록 할당"});
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00033", new String[]{"IP블록 할당"});
		}
	}
	
	/**
	 * 수용국 정보 조회(초기)
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrderSofficeList(IpAllocOrderMstVo ipAllocOrderMstVo){
		List<CommonCodeVo> resultListVo = null;
		try {
			if (ipAllocOrderMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = orderMgmtTxService.selectOrderSofficeList(ipAllocOrderMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 수용국 정보 조회
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public IpAllocOrderMstListVo selectSetOrderOfficeList(IpAllocOrderMstVo ipAllocOrderMstVo){
		IpAllocOrderMstListVo resultListVo = null;
		
		try {
			if (ipAllocOrderMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<IpAllocOrderMstVo> resultList = orderMgmtTxService.selectSetOrderOfficeList(ipAllocOrderMstVo);
			resultListVo = new IpAllocOrderMstListVo();
			resultListVo.setIpAllocOrderMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
}
