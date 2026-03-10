package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.service;

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
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.IpCalculateUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbIpAllocNeossMstDao;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAlloLinkOperMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001IpSuggestList;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0001Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0002AssingdIp;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0002Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0002Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003AssignedIP;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0003Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0004Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0004Response;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0005Request;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0005ReservedIP;
import com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo.Sdn0005Response;

@Component @Transactional
public class SdnService {

@Lazy @Autowired
	private CommonService commonService;

@Lazy @Autowired
	private SdnTxService sdntxService;

	@Lazy @Autowired
	private TbIpAllocNeossMstDao tbIpAllocNeossMstDao;

	@Autowired
	private LinkUtil linkUtil;

	@Autowired
	private IpCalculateUtil ipCalculateUtil;

	/*성공메시지*/
	private final static String IFSUCESSCD = CommonCodeUtil.SUCCESS_MSG;
	private final static String IFSUCESSMSG = CommonCodeUtil.SUCCESS_MSG;

	/*Validation : 필수값입력 부재*/
	private final static String IFINPUTERR_CD = "ERR0001";
	private final static String IFINPUTERR_MSG = "필수값이 입력되지 않았습니다.";
	/*Validation : 연동 싷패*/
	private final static String IFFAILERR_CD = "ERR0002";
	private final static String IFFAILERR_MSG = "연동 실패.";

	/*Validation : 미처리 오더 정보 존재*/
	private final static String IFDUAPERR_CD = "ERR0003";
	private final static String IFDUAPERR_MSG = "미처리 오더 정보 존재.";

	/*Validation : 선행 오더 미존재*/
	//	private final static  String IFODRERR_CD ="ERR0004";
	//	private final static String IFODRERR_MSG ="선행 오더 미존재.";

	/*Validation : 지원 가능한 Bitmask가 넘었습니다.*/
	private final static String IFBITOVER_CD = "ERR0005";
	private final static String IFBITOVER_MSG = "지원 가능하지 않은 Bitmask가 입력되었습니다.";

	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFMISODR_CD = "ERR0006";
	private final static String IFMISODR_MSG = "미정의 된 종료 유형 입니다.";

	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFNEOSSERR_CD = "ERR0007";
	private final static String IFNEOSSERR_MSG = "NeOSS 연동에 실패하였습니다.";

	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFREGYERR_CD = "ERR0008";
	private final static String IFREGYERR_MSG = "이미 확정된 상태입니다. 다시 확인해주시기 바랍니다.";

	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFREGNERR_CD = "ERR0009";
	private final static String IFREGNERR_MSG = "이미 해지된 상태입니다. 다시 확인해주시기 바랍니다.";

	/*Validation : 미정의 된 종료 유형 입니다.*/
	private final static String IFGATEWAY_CD = "ERR0010";
	private final static String IFGATEWAY_MSG = "GateWayIp 오류입니다.";

	/*Exception*/
	private final static String IFEXCEPTION_CD = "ERR9999";

	/**
	 * @MEthod 	: consumeSdn0001Service
	 * @DESC	: SDN_IPM_WS_0001 selectSuggestIPListSdn(): 가용 IPBlock 추천 List 연동 
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Sdn0001Response consumeSdn0001Service(Sdn0001Request stcRequest) {

		/*LogKey*/
		String inputkey = "";
		int nDivCnt = 999;
		// Create Return Object
		Sdn0001Response stcResponse = new Sdn0001Response();
		TbIpAllocNeossMstVo tbIpAllocNeossMstVo = new TbIpAllocNeossMstVo();
		List<Sdn0001IpSuggestList> stcipSuggestList = null;
		HashMap<String, Object> hparam = new HashMap<String, Object>();

		// Check inputParam
		if (linkUtil.getString(stcRequest.getOfficescode()).isEmpty()
				|| linkUtil.getString(stcRequest.getAssignTypeCD()).isEmpty()
				|| linkUtil.getString(stcRequest.getSVCUSETYPECD()).isEmpty()
				|| linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).isEmpty()
				|| linkUtil.getString(stcRequest.getIPCREATETYPECD()).isEmpty()
				|| linkUtil.getString(stcRequest.getSvcLineTypeCd()).isEmpty()
				|| linkUtil.getString(stcRequest.getIpBitmask()).isEmpty()) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else if ((linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).equals(CommonCodeUtil.IPV4))
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) != 0)
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) < 16
						|| linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) > 30)) {
			stcResponse.setResultCD(IFBITOVER_CD);
			stcResponse.setResultMSG(IFBITOVER_MSG);
		} else if ((linkUtil.getString(stcRequest.getIPVERSIONTYPECD()).equals(CommonCodeUtil.IPV6))
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) != 0)
				&& (linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) < 48
						|| linkUtil.getIntgerFromString(stcRequest.getIpBitmask()) > 64)) {
			stcResponse.setResultCD(IFBITOVER_CD);
			stcResponse.setResultMSG(IFBITOVER_MSG);
		} else {

			try {

				// Business Service[ Suggest IP Block List ]								
				tbIpAllocNeossMstVo.setIcisofficescode(stcRequest.getOfficescode()); // 국사코드
				tbIpAllocNeossMstVo.setAssignTypeCd(stcRequest.getAssignTypeCD()); // IP할당유형
				tbIpAllocNeossMstVo.setSvcUseTypeCd(stcRequest.getSVCUSETYPECD()); // 서비스이용목적
				tbIpAllocNeossMstVo.setIpVersionTypeCd(stcRequest.getIPVERSIONTYPECD()); // Ip Version
				tbIpAllocNeossMstVo.setIpCreateTypeCd(stcRequest.getIPCREATETYPECD()); // 	Ip Type
				tbIpAllocNeossMstVo.setssvcLineTypeCd(stcRequest.getSvcLineTypeCd()); // 서비스망 구분
				tbIpAllocNeossMstVo.setIpbitmask(linkUtil.getIntgerFromString(stcRequest.getIpBitmask())); // subnetmask

				PrintLogUtil.printLogInfo("=============SDN_IPM_WS_0001==================");
				PrintLogUtil.printLogInfoValueObject(tbIpAllocNeossMstVo);

				if (null != stcRequest.getLimit()) {
					tbIpAllocNeossMstVo.setNlimit(linkUtil.getIntgerFromString(stcRequest.getLimit())); // limit
				}

				/*set Input Data*/
				if (linkUtil.getString(stcRequest.getIPADDRESS()).isEmpty()) {
					inputkey = "OFFICESCODE : " + stcRequest.getOfficescode() + " / ASSIGN_TYPE_CD :  "
							+ stcRequest.getAssignTypeCD();

					stcipSuggestList = sdntxService.selectSuggestIPListSdn(tbIpAllocNeossMstVo);
					// Select Suggest IP Block List
					if (stcipSuggestList.size() < 1 && (tbIpAllocNeossMstVo.getIpbitmask() != null
							&& tbIpAllocNeossMstVo.getIpbitmask() != 0)) {
						hparam.put("ssvcLineTypeCd", tbIpAllocNeossMstVo.getssvcLineTypeCd());
						hparam.put("icisofficescode", tbIpAllocNeossMstVo.getIcisofficescode());
						hparam.put("ipmsSvcSeq", null);
						hparam.put("assignTypeCd", tbIpAllocNeossMstVo.getAssignTypeCd());
						hparam.put("ipVersionTypeCd", tbIpAllocNeossMstVo.getIpVersionTypeCd());
						hparam.put("ipCreateTypeCd", tbIpAllocNeossMstVo.getIpCreateTypeCd());
						hparam.put("ipbitmask", tbIpAllocNeossMstVo.getIpbitmask());

						nDivCnt = sdntxService.getdivIpBlock(hparam);

						PrintLogUtil.printLogInfo("nDivCnt === " + nDivCnt);
					}
				} else if (!linkUtil.getString(stcRequest.getIPADDRESS()).isEmpty()) {
					PrintLogUtil.printLogInfo("ELSE IF");
					inputkey = "OFFICESCODE : " + stcRequest.getOfficescode() + " / ASSIGN_TYPE_CD :  "
							+ stcRequest.getAssignTypeCD() + " / ipAddressSearchKey : " + stcRequest.getIPADDRESS();

					tbIpAllocNeossMstVo.setSipAddressSearchKey(stcRequest.getIPADDRESS());
					stcipSuggestList = sdntxService.selectSuggestIPListSdn(tbIpAllocNeossMstVo);
				}

				String count = sdntxService.countSelectSuggestIPListSdn(tbIpAllocNeossMstVo);

				PrintLogUtil.printLogInfo("IpBlockCount = " + count);

				if (nDivCnt == 0) {
					PrintLogUtil.printLogInfo("DIV");
					List<Sdn0001IpSuggestList> t_stcipSuggestList = null;
					t_stcipSuggestList = sdntxService.selectDivSuggestIPListSdn(tbIpAllocNeossMstVo);
					PrintLogUtil.printLogInfo("stcipSuggestList2.size() === " + t_stcipSuggestList.size());
					for (Sdn0001IpSuggestList stcipSuggest : t_stcipSuggestList) {
						stcResponse.getStcIpSuggestList().add(stcipSuggest);
					}
				} else {
					// Make Retrun Param				
					for (Sdn0001IpSuggestList stcipSuggest : stcipSuggestList) {
						stcResponse.getStcIpSuggestList().add(stcipSuggest);
					}
				}

				try {
					PrintLogUtil.printLogInfo(
							"stcResponse.getStcIpSuggestList().size() = " + stcResponse.getStcIpSuggestList().size());
					PrintLogUtil.printLogInfo("stcipSuggestList.size() === " + stcipSuggestList.size());
				} catch (Exception e) {
					PrintLogUtil.printLogInfo("stcipSuggestList Size Error");
					PrintLogUtil.printLogInfo("stcipSuggestList Size = 0");
				}

				stcResponse.setIpBlockCount(count);
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSCD);

			} catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				stcResponse.setResultMSG(e.toString());
				linkUtil.setSystemERR(e);
			} finally {
				// Set InterFace Logging
				linkUtil.setSystemlog("SDN_IPM_WS_0001", inputkey, stcResponse.getResultCD(),
						stcResponse.getResultMSG());
			}
		}
		return stcResponse;
	}

	/**
	 * @MEthod 	: consumeSdn0002Service
	 * @DESC	: SDN_IPM_WS_0002 insertFixIPListSdn(): IP Block 확정 정보 연동
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Sdn0002Response consumeSdn0002Service(Sdn0002Request stcRequest) {

		/*LogKey*/
		String inputkey = "";
		int nResult = 999;

		List<TbIpAllocNeossMstVo> tmpListVo = new ArrayList<TbIpAllocNeossMstVo>();

		// Create Return Object
		Sdn0002Response stcResponse = new Sdn0002Response();

		stcResponse.setResultCD("");

		// Check inputParam
		if (linkUtil.getString(stcRequest.getRegYN()).isEmpty()) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else {
			try {

				inputkey = "RegYN : " + stcRequest.getRegYN();

				if (stcResponse.getResultCD().isEmpty()) {
					List<Sdn0002AssingdIp> sdn0002AssingdIp = stcRequest.getStcAssignedIPList();
					// 논리시설 정보 등록	
					PrintLogUtil.printLogInfo("=============SDN_IPM_WS_0002==================");
					PrintLogUtil.printLogInfoValueObject(stcRequest.getStcAssignedIPList());

					// GateWayIp 확인
					boolean isGateWayIp = true;
					/*for(Sdn0002AssingdIp tmpVo : sdn0002AssingdIp){
						
						if(tmpVo.getGatewayIP().isEmpty()) {
							isGateWayIp = false;
						} else if(!ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tmpVo.getGatewayIP())) {
							isGateWayIp = false;
						}
					}*/

					if (isGateWayIp) {
						TbIpAlloLinkOperMstVo assignVo = new TbIpAlloLinkOperMstVo();

						for (Sdn0002AssingdIp foreachvo : sdn0002AssingdIp) {

							TbIpAllocNeossMstVo tmpvo = new TbIpAllocNeossMstVo();
							TbIpAlloLinkOperMstVo selvo = new TbIpAlloLinkOperMstVo();

							selvo.setSsaid(foreachvo.getSaid());
							selvo.setSsvcLineTypeCd(foreachvo.getSvcLineTypeCd());
							selvo.setSipBlock(foreachvo.getIpBlock());
							selvo.setNbitmask(linkUtil.getIntgerFromString(foreachvo.getIpBitmask()));
							assignVo = sdntxService.selectMstVo(selvo);

							tmpvo.setRegyn(stcRequest.getRegYN());
							tmpvo.setNipAssignMstSeq(assignVo.getNipAssignMstSeq());
							tmpvo.setSaid(foreachvo.getSaid());
							tmpvo.setssvcLineTypeCd(foreachvo.getSvcLineTypeCd());
							tmpvo.setIpBlock(foreachvo.getIpBlock());
							tmpvo.setIpbitmask(linkUtil.getIntgerFromString(foreachvo.getIpBitmask()));

							tmpvo.setGatewayip(foreachvo.getGatewayIP()); //  2021.01.04 추가

							tmpListVo.add(tmpvo);
						}

						nResult = sdntxService.insertFixIPListSdn(stcRequest.getRegYN(), tmpListVo);

						if (nResult == 999) {
							stcResponse.setResultCD(IFMISODR_CD);
							stcResponse.setResultMSG(IFMISODR_MSG);
						} else if (nResult == 998) { // NeOSS Error
							stcResponse.setResultCD(IFNEOSSERR_CD);
							stcResponse.setResultMSG(IFNEOSSERR_MSG);
						} else if (nResult == 997) { // NeOSS Error
							stcResponse.setResultCD(IFREGYERR_CD);
							stcResponse.setResultMSG(IFREGYERR_MSG);
						} else if (nResult == 996) { // NeOSS Error
							stcResponse.setResultCD(IFREGNERR_CD);
							stcResponse.setResultMSG(IFREGNERR_MSG);
						} else {
							// Make Retrun Param			
							stcResponse.setResultCD(IFSUCESSCD);
							stcResponse.setResultMSG(IFSUCESSMSG);
						}

					} else {
						stcResponse.setResultCD(IFGATEWAY_CD);
						stcResponse.setResultMSG(IFGATEWAY_MSG);
					}

				} else {
					stcResponse.setResultCD(IFMISODR_CD);
					stcResponse.setResultMSG(IFMISODR_MSG);
				}
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				linkUtil.setSystemERR(e);
			} finally {
				// Set InterFace Logging
				linkUtil.setSystemlog("SDN_IPM_WS_0002", inputkey, stcResponse.getResultCD(),
						stcResponse.getResultMSG());
			}
		}

		return stcResponse;
	}

	/**
	 * @MEthod 	: consumeSdn0003Service
	 * @DESC	: SDN_IPM_WS_0003 selectFixedIPList(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	public Sdn0003Response consumeSdn0003Service(Sdn0003Request stcRequest) {

		/*LogKey*/
		String inputkey = "";

		// Create Return Object
		Sdn0003Response stcResponse = new Sdn0003Response();
		List<TbIpAlloLinkOperMstVo> stcAssignedIPList = null;

		// Check inputParam
		if (linkUtil.getString(stcRequest.getSaid()).isEmpty()) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else {

			try {

				inputkey = "SAID : " + stcRequest.getSaid();

				// Business Service[ Fixed IP Block  ]

				stcAssignedIPList = sdntxService.selectFixedIPListSdn(stcRequest);

				for (TbIpAlloLinkOperMstVo foreachVo : stcAssignedIPList) {
					Sdn0003AssignedIP sdn0003AssignedIP = new Sdn0003AssignedIP();

					sdn0003AssignedIP.setAssignTypeCD(foreachVo.getSassignTypeCd());
					sdn0003AssignedIP.setIPVERSIONTYPECD(foreachVo.getSipVersionTypeCd());
					sdn0003AssignedIP.setIPCREATETYPECD(foreachVo.getSipCreateTypeCd());
					sdn0003AssignedIP.setIpBlock(foreachVo.getSipBlock());
					sdn0003AssignedIP.setIpBitmask(linkUtil.getString(foreachVo.getNbitmask().toString()));
					sdn0003AssignedIP.setSIpAddr(foreachVo.getSfirstAddr());
					sdn0003AssignedIP.setEIpAddr(foreachVo.getSlastAddr());
					sdn0003AssignedIP.setGatewayIP(foreachVo.getGatewayip());
					sdn0003AssignedIP.setSVCLINETYPECD(foreachVo.getSsvcLineTypeCd());

					stcResponse.getStcAssignedIPList().add(sdn0003AssignedIP);

				}

				// Make Retrun Param			
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSMSG);

			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				linkUtil.setSystemERR(e);
			} finally {
				// Set InterFace Logging			
				linkUtil.setSystemlog("SDN_IPM_WS_0003", inputkey, stcResponse.getResultCD(),
						stcResponse.getResultMSG());
			}
		}

		return stcResponse;
	}

	/**
	 * @MEthod 	: consumeSdn0004Service
	 * @DESC	: SDN_IPM_WS_0004 selectFixedIPSdn(): 시설정보조회
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	public Sdn0004Response consumeSdn0004Service(Sdn0004Request stcRequest) {

		/*LogKey*/
		String inputkey = "";

		// Create Return Object
		Sdn0004Response stcResponse = new Sdn0004Response();
		TbIpAlloLinkOperMstVo stcAssignedIP = null;

		// Check inputParam
		if (linkUtil.getString(stcRequest.getIpBlock()).isEmpty()
				|| linkUtil.getString(stcRequest.getIpBistmask()).isEmpty()) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else {

			try {

				inputkey = "IP_BLOCK : " + stcRequest.getIpBlock() + " / IP_BITMASK : " + stcRequest.getIpBistmask();

				// Business Service[ Fixed IP Block  ]

				stcAssignedIP = sdntxService.selectFixedIPSdn(stcRequest);

				stcResponse.setSaid(stcAssignedIP.getSsaid());
				stcResponse.setOfficescode(stcAssignedIP.getSicisofficescode());

				// Make Retrun Param			
				stcResponse.setResultCD(IFSUCESSCD);
				stcResponse.setResultMSG(IFSUCESSMSG);

			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				linkUtil.setSystemERR(e);
			} finally {
				// Set InterFace Logging			
				linkUtil.setSystemlog("SDN_IPM_WS_0004", inputkey, stcResponse.getResultCD(),
						stcResponse.getResultMSG());
			}
		}

		return stcResponse;
	}

	/**
	 * @MEthod 	: consumeSdn0005Service
	 * @DESC	: SDN_IPM_WS_0005 insertReservedIPListSdn(): IP Block 할당 예약 정보 연동
	 * @변경이력 	:
	 * @param stcRequest
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Sdn0005Response consumeSdn0005Service(Sdn0005Request stcRequest) {

		/*LogKey*/
		String inputkey = "";
		int nResult = 999;

		List<TbIpAllocNeossMstVo> tmpListVo = new ArrayList<TbIpAllocNeossMstVo>();

		// Create Return Object
		Sdn0005Response stcResponse = new Sdn0005Response();

		stcResponse.setResultCD("");

		// Check inputParam
		if (linkUtil.getString(stcRequest.getRegYN()).isEmpty() || stcRequest.getStcReservedIPList().size() < 1) {
			stcResponse.setResultCD(IFINPUTERR_CD);
			stcResponse.setResultMSG(IFINPUTERR_MSG);
		} else {
			try {

				/*set Input Data*/
				inputkey = "RegYN : " + stcRequest.getRegYN();

				if (stcResponse.getResultCD().trim().isEmpty()) {

					// 논리시설 정보 등록	
					List<Sdn0005ReservedIP> sdn0005ReservedIP = stcRequest.getStcReservedIPList();
					PrintLogUtil.printLogInfo("=============SDN_IPM_WS_0005==================");
					PrintLogUtil.printLogInfoValueObject(stcRequest.getStcReservedIPList());

					TbIpAlloLinkOperMstVo assignVo = new TbIpAlloLinkOperMstVo();

					// GateWayIp 확인
					boolean isGateWayIp = true;
					/*for(Sdn0005ReservedIP tmpVo : sdn0005ReservedIP){
						
						if(tmpVo.getGatewayIP().isEmpty()) {
							isGateWayIp = false;
						} else if(!ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tmpVo.getGatewayIP())) {
							isGateWayIp = false;
						}
					}*/

					if (isGateWayIp) {
						for (Sdn0005ReservedIP foreachvo : sdn0005ReservedIP) {

							TbIpAllocNeossMstVo tmpvo = new TbIpAllocNeossMstVo();
							TbIpAlloLinkOperMstVo selvo = new TbIpAlloLinkOperMstVo();

							selvo.setSsaid(foreachvo.getSaid());
							selvo.setSsvcLineTypeCd(foreachvo.getSvcLineTypeCd());
							selvo.setSipBlock(foreachvo.getIpBlock());
							selvo.setNbitmask(linkUtil.getIntgerFromString(foreachvo.getIpBitmask()));
							assignVo = sdntxService.selectMstVo(selvo);

							tmpvo.setRegyn(stcRequest.getRegYN());
							tmpvo.setNipAssignMstSeq(assignVo.getNipAssignMstSeq());
							tmpvo.setSaid(foreachvo.getSaid());
							tmpvo.setssvcLineTypeCd(foreachvo.getSvcLineTypeCd());
							tmpvo.setIpBlock(foreachvo.getIpBlock());
							tmpvo.setIpbitmask(linkUtil.getIntgerFromString(foreachvo.getIpBitmask()));

							tmpvo.setGatewayip(foreachvo.getGatewayIP());

							tmpListVo.add(tmpvo);
						}

						nResult = sdntxService.insertReservedIPListSdn(stcRequest.getRegYN(), tmpListVo);

						if (nResult == 999) {
							stcResponse.setResultCD(IFMISODR_CD);
							stcResponse.setResultMSG(IFMISODR_MSG);
						} else if (nResult == 998) { // NeOSS Error
							stcResponse.setResultCD(IFNEOSSERR_CD);
							stcResponse.setResultMSG(IFNEOSSERR_MSG);
						} else if (nResult == 997) { // NeOSS Error
							stcResponse.setResultCD(IFREGYERR_CD);
							stcResponse.setResultMSG(IFREGYERR_MSG);
						} else if (nResult == 996) { // NeOSS Error
							stcResponse.setResultCD(IFREGNERR_CD);
							stcResponse.setResultMSG(IFREGNERR_MSG);
						} else {
							// Make Retrun Param			
							stcResponse.setResultCD(IFSUCESSCD);
							stcResponse.setResultMSG(IFSUCESSMSG);
						}
					} else {
						stcResponse.setResultCD(IFGATEWAY_CD);
						stcResponse.setResultMSG(IFGATEWAY_MSG);

					}

				} else {
					stcResponse.setResultCD(IFMISODR_CD);
					stcResponse.setResultMSG(IFMISODR_MSG);
				}
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				stcResponse.setResultCD(IFEXCEPTION_CD);
				linkUtil.setSystemERR(e);
			} finally {
				// Set InterFace Logging
				linkUtil.setSystemlog("SDN_IPM_WS_0005", inputkey, stcResponse.getResultCD(),
						stcResponse.getResultMSG());
			}
		}

		return stcResponse;
	}

}
