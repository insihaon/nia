package com.kt.ipms.legacy.ipmgmt.routmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.service.RoutMgmtService;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstListVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.service.ExcelParseBatchService;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class RoutMgmtController extends CommonController{
	
	@Autowired
	private RoutMgmtService routMgmtService;
	
	@Autowired
	private AllocMgmtService allocMgmtService;
	
	@Autowired
	ExcelParseBatchService excel;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	/****************************************************************************************
	 * IP주소 라우팅 비교/점검
	 ****************************************************************************************/
	
	/**
	 * IP주소 라우팅 비교/점검 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewListRoutChkMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListRoutChkMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRoutChkMstListVo resultListVo = routMgmtService.selectListRoutChkMst(searchVo);
		return createResultList(resultListVo.getTbRoutChkMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewListRoutChkMst.do", method = RequestMethod.POST)
	public String viewListRoutChkMst(@ModelAttribute("searchVo") TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListRoutChkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewListRoutChkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewListRoutChkMst";
	}
	private ModelMap viewListRoutChkMstModel(@ModelAttribute("searchVo") TbRoutChkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRoutChkMstListVo resultListVo = null;
		try {
			/* 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용  */
			Map<String,Object> sipCreateTypeParamMap = new HashMap<String,Object>();
			sipCreateTypeParamMap.put("startCd", "CT0001");
			sipCreateTypeParamMap.put("endCd", "CT0004");  // 사설은 사설 IP 신청 게시판에서만 등록 가능
			
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, sipCreateTypeParamMap);
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD, null);
			List<CommonCodeVo> sdbIntgrmRsltCds = commonCodeService.selectListCommonCode(CommonCodeUtil.INTGRM_RSLT_CD, null);
			
			model.addAttribute("sassignLevelCds", sassignLevelCds);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sdbIntgrmRsltCds", sdbIntgrmRsltCds);
			
//			if(searchVo.getSexcpt_yn() == "" || searchVo.getSexcpt_yn() == null) { //Codeeyes-Urgent-객체 비교시 == , != 사용제한 , 불안전한 Null 검사
			if(searchVo.getSexcpt_yn() == null || searchVo.getSexcpt_yn().equals("")) {
				searchVo.setSexcpt_yn("N");
			}
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			
			
			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				} else {
					
					if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
						
						TbLvlBasVo searchCenterVo = new TbLvlBasVo();
						searchCenterVo.setSsvcLineTypeCd("CL0001");
						centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
						nodeListVo = new TbLvlBasListVo();
					} else {
						centerListVo = new TbLvlBasListVo();
						nodeListVo = new TbLvlBasListVo();	
					}
				}
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);

			/** 계위 Seq 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
				searchVo.setSsvcLineTypeCd("CL0001");	// KORNET
			}
			
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			/** 조직별 서비스 유형 셋팅 **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			setPagination(searchVo);
			
			// IP조회(멀티)
//			if(searchVo.getSearchWrdMulti() != null || searchVo.getSearchWrdMulti() != ""){ //Codeeyes-Critical-불안전한 Null 검사 금지
			if(searchVo.getSearchWrdMulti() != null && !searchVo.getSearchWrdMulti().equals("")){
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrdMulti().replace("\r\n", ",").replace("/t", ",");
				

				//Codeeyes-Urgent-String 추가 시 String Buffer 사용
				//String rmDupStr = "";
				StringBuffer sb = new StringBuffer();
				
				// 중복제거
				ArrayList<String> arr = new ArrayList<String>();
				String[] strArr = tempWrd.split(",");
				
				if(strArr != null && tempWrd.contains(",")){
					for(int i=0; i<strArr.length; i++) {
						arr.add(strArr[i]);
					}
					
					TreeSet<String> arr2 = new TreeSet<String>(arr);
					ArrayList<String> arr3 = new ArrayList<String>(arr2); 
					
					for(int j=0; j<arr3.size(); j++) {
						sb.append(arr3.get(j));
						sb.append(",");
						// rmDupStr += arr3.get(j) + ",";
					}
					
					if(sb.toString().contains(",")) {
						// rmDupStr = rmDupStr.substring(0, rmDupStr.length()-1);
						sb.substring(0, sb.length()-1);
					}
					
				} else {
					// rmDupStr = tempWrd;
					sb = new StringBuffer();
					sb.append(tempWrd);
				}
				
				String rmDupStr = sb.toString(); 
				searchVo.setSearchWrdMulti(rmDupStr);
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			// 멀티셀렉트(서비스)
			if(null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for(int i=0;i<sAssignTypeMulti.length;i++){
					listString.add(sAssignTypeMulti[i]);
				}
				
				searchVo.setSassignTypeMulti(listString);
			}
			
			// 멀티셀렉트(IP블록상태)
			if(null != searchVo.getSassignLevelCdMultiStr() && !"".equals(searchVo.getSassignLevelCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignLevlMulti = searchVo.getSassignLevelCdMultiStr().split(";");
				for(int i=0;i<sAssignLevlMulti.length;i++){
					listString.add(sAssignLevlMulti[i]);
				}
				
				searchVo.setSassignLevelMulti(listString);
			}
			
			/*if(searchVo.getSsvcLineTypeCd().equals("CL0003")) {
				searchVo.setSexcpt_yn("ALL");
				searchVo.setSdbIntgrmRsltCd(null);
			}*/
			resultListVo = routMgmtService.selectListRoutChkMst(searchVo);
			
			// IP조회(멀티)
			if(searchVo.getSearchWrdMulti() != null){
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrdMulti().replace("\r\n", "\r\n").replace("/t", "\r\n").replace( ",", "\r\n");
				searchVo.setSearchWrdMulti(tempWrd);
			}
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbRoutChkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbRoutChkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 > 엑셀 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/routmgmt/viewListRoutChkMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo viewListRoutChkMstExcel(@ModelAttribute("searchVo") TbRoutChkMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		try{
			
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			
			
			/** 계위 정보 설정 **/
			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				} else {
					
					if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
						
						TbLvlBasVo searchCenterVo = new TbLvlBasVo();
						searchCenterVo.setSsvcLineTypeCd("CL0001");
						centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
						nodeListVo = new TbLvlBasListVo();
					} else {
						centerListVo = new TbLvlBasListVo();
						nodeListVo = new TbLvlBasListVo();	
					}
				}
			}

			/** 계위 Seq 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
				searchVo.setSsvcLineTypeCd("CL0001");	// KORNET
			}
			
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			/** 조직별 서비스 유형 셋팅 **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			setPagination(searchVo);
			
			// IP조회(멀티)
//			if(searchVo.getSearchWrdMulti() != null || searchVo.getSearchWrdMulti() != ""){ //Codeeyes-Critical-불안전한 Null 검사 금지
			if(searchVo.getSearchWrdMulti() != null && !searchVo.getSearchWrdMulti().equals("")){
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrdMulti().replace("\r\n", ",").replace("/t", ",");
				
				//Codeeyes-Urgent-String 추가 시 String Buffer 사용
				// String rmDupStr = "";
				StringBuffer sb = new StringBuffer();
				
				// 중복제거
				ArrayList<String> arr = new ArrayList<String>();
				String[] strArr = tempWrd.split(",");
				
				if(strArr != null && tempWrd.contains(",")){
					for(int i=0; i<strArr.length; i++) {
						arr.add(strArr[i]);
					}
					
					TreeSet<String> arr2 = new TreeSet<String>(arr);
					ArrayList<String> arr3 = new ArrayList<String>(arr2); 
					
					for(int j=0; j<arr3.size(); j++) {
						sb.append(arr3.get(j));
						sb.append(",");
						// rmDupStr += arr3.get(j) + ",";
					}
					
					//if(rmDupStr.contains(",")) {
					if(sb.toString().contains(",")) {
//						rmDupStr = rmDupStr.substring(0, rmDupStr.length()-1);
						sb.substring(0, sb.length()-1);
					}
					
				} else {
//					rmDupStr = tempWrd;
					sb = new StringBuffer();
					sb.append(tempWrd);
				}
				
				String rmDupStr = sb.toString(); 
				searchVo.setSearchWrdMulti(rmDupStr);
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			// 멀티셀렉트(서비스)
			if(null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for(int i=0;i<sAssignTypeMulti.length;i++){
					listString.add(sAssignTypeMulti[i]);
				}
				
				searchVo.setSassignTypeMulti(listString);
			}
			
			// 멀티셀렉트(IP블록상태)
			if(null != searchVo.getSassignLevelCdMultiStr() && !"".equals(searchVo.getSassignLevelCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignLevlMulti = searchVo.getSassignLevelCdMultiStr().split(";");
				for(int i=0;i<sAssignLevlMulti.length;i++){
					listString.add(sAssignLevlMulti[i]);
				}
				
				searchVo.setSassignLevelMulti(listString);
			}			
			
			TbRoutChkMstListVo  resultListVo = routMgmtService.selectListRoutChkMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			if(searchVo.getSsvcLineTypeCd().equals("CL0003")) {
				mappingList.add("서비스망|getSsvcLineTypeNm");
				mappingList.add("본부|getSsvcGroupNm");
				mappingList.add("노드|getSsvcObjNm");
				mappingList.add("공인/사설|getSipCreateTypeNm");
				mappingList.add("서비스|getSassignTypeNm");
				mappingList.add("IPMS IP블록|getPipmsIpPrefix");
				mappingList.add("IPMS IP회선|getNipAllocMstCnt");
				mappingList.add("IPMS IP블록상태|getSassignLevelNm");
				mappingList.add("실제 라우팅 장비 IP블록|getProutingIpPrefix");
				mappingList.add("실제 라우팅 장비 Community|getScommunity");
				mappingList.add("실제 라우팅 장비 사용장비|getSuseRouting");
				mappingList.add("실제 라우팅 장비 사용여부|getSroutingUseYn");
				
				//mappingList.add("Community 기준 전용번호|getSllnum");
				//mappingList.add("Community 기준 장비명|getSsubcnealias");
				
				mappingList.add("분할/병합건수|getNtargetCnt");
				mappingList.add("장비수집일자|getScollect_dt");
				mappingList.add("예외처리여부|getSexcpt_yn");
				
				mappingList.add("예외처리 유형|getSexcptNm");
				mappingList.add("예외처리 세부사유|getSexcptRsn");
				
				mappingList.add("진행상태|getSdbIntgrmRsltNm");
				//mappingList.add("DB현행화결과|getSdefaultDbRslt");
			} else {
				mappingList.add("서비스망|getSsvcLineTypeNm");
				mappingList.add("본부|getSsvcGroupNm");
				mappingList.add("노드|getSsvcObjNm");
				mappingList.add("공인/사설|getSipCreateTypeNm");
				mappingList.add("서비스|getSassignTypeNm");
				mappingList.add("IPMS IP블록|getPipmsIpPrefix");
				mappingList.add("IPMS IP회선|getNipAllocMstCnt");
				mappingList.add("IPMS IP블록상태|getSassignLevelNm");
				mappingList.add("실제 라우팅 장비 IP블록|getProutingIpPrefix");
				mappingList.add("실제 라우팅 장비 Nexthop|getSipNexthop");
				mappingList.add("실제 라우팅 장비 사용여부|getSroutingUseYn");
				
				mappingList.add("Nexthop 기준 전용번호|getSllnum");
				mappingList.add("Nexthop 기준 장비명|getSsubcnealias");
				
				mappingList.add("분할/병합건수|getNtargetCnt");
				mappingList.add("장비수집일자|getScollect_dt");
				mappingList.add("예외처리여부|getSexcpt_yn");
				
				mappingList.add("예외처리 유형|getSexcptNm");
				mappingList.add("예외처리 세부사유|getSexcptRsn");
				
				mappingList.add("진행상태|getSdbIntgrmRsltNm");
				//mappingList.add("DB현행화결과|getSdefaultDbRslt");
			}
			
			
			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbRoutChkMstVos(), mappingList, request);
			if (StringUtils.hasText(fileName)) {
				resultVo.setFileName(fileName);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00050");
			}
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/insertListIpBlockMatchMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo insertListIpBlockMatchMst(@RequestBody TbRoutChkMstVo tbRoutChkMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbRoutChkMstVo resultVo = null;

		String resultMsg = CommonCodeUtil.INTGRM_DB_RESULT01;		// IP블록 현행화 실패
		TbRoutChkMstVo vo = new TbRoutChkMstVo();
		List<String> chkListStr = new ArrayList<String>();
		String userId = jwtUtil.getUserId(request);
		int chkSize = 0;
		String sexcptYn = "";
		try {
			
			// 1. 체크된건 값 구분
			chkListStr = tbRoutChkMstVo.getChkListStr();
			
			String scollectDt = "";							// 수집일자
			BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
			BigInteger nroutingChkMstSeq = null;		// 라우팅_비교_결과_MSTSEQ
			String sipPrefixGubun = "";						// 비교_구분(PIPMS/PROUTING)
			chkSize = chkListStr.size();
					
			for(String tmp : chkListStr) {		// 체크한 건
				
				if(tmp.contains("_")) {
					scollectDt = tmp.split("_")[0];
					nlvlMstSeq = new BigInteger(tmp.split("_")[1]);
					nroutingChkMstSeq = new BigInteger(tmp.split("_")[2]);
					sipPrefixGubun = tmp.split("_")[3];
					sexcptYn = tmp.split("_")[4];
				} 
				
				if(sexcptYn.equals("Y")) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"예외건은 IP Block 해지 후 분할/병합이 불가능합니다."});
				}
				
				vo.setScollectDtOrigin(scollectDt);
				vo.setNlvlMstSeq(nlvlMstSeq);
				vo.setNroutingChkMstSeq(nroutingChkMstSeq);
				vo.setSipPrefixGubun(sipPrefixGubun);
				vo.setScreateId(userId);
				vo.setSmodifyId(userId);
				vo.setMenuType(tbRoutChkMstVo.getMenuType());
				
				// 2. IP 블록 현행화 Operation
				routMgmtService.insertListIpBlockMatchMst(vo);
			}
			
			resultVo = new TbRoutChkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc); 
			
			// DB 현행화 결과 상태 Update
			if(!sexcptYn.equals("Y")) {
				updateRoutChkMstVo(tbRoutChkMstVo, resultMsg, userId);
			} 
			
		} catch (Exception e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
			
			// DB 현행화 결과 상태 Update
			if(!sexcptYn.equals("Y")) {
				updateRoutChkMstVo(tbRoutChkMstVo,  resultMsg, userId);
			} 
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > DB 현행화 결과 상태 Update
	 * @param tbRoutChkMstVo
	 * @param status
	 * @param userId
	 */
	public void updateRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo, String status, String userId) {
		
		List<String> chkListStr = new ArrayList<String>();
		TbRoutChkMstVo vo = new TbRoutChkMstVo();
		
		String scollectDt = "";							// 수집일자
		BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
		BigInteger nroutingChkMstSeq = null;		// 라우팅_비교_결과_MSTSEQ
		String sipPrefixGubun = "";						// 비교_구분(PIPMS/PROUTING)
		
		chkListStr = tbRoutChkMstVo.getChkListStr();
		
		for(String tmp : chkListStr) {
			
			if(tmp.contains("_")) {
				scollectDt = tmp.split("_")[0];
				nlvlMstSeq = new BigInteger(tmp.split("_")[1]);
				nroutingChkMstSeq = new BigInteger(tmp.split("_")[2]);
				sipPrefixGubun = tmp.split("_")[3];
			} 
			
			vo.setScollectDtOrigin(scollectDt);
			vo.setNlvlMstSeq(nlvlMstSeq);
			vo.setNroutingChkMstSeq(nroutingChkMstSeq);
			vo.setSipPrefixGubun(sipPrefixGubun);
			vo.setScreateId(userId);
			vo.setSmodifyId(userId);
			vo.setSdbIntgrmRsltCd(status);			// DB 현행화 결과 상태 Update
			
			routMgmtService.updateRoutChkMstVo(vo);
		}
		
	} 
	

	/**
	 * IP주소 라우팅 비교/점검 > 실제 라우팅 DB 수집
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/insertListRoutChkMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo insertListRoutChkMst(@RequestBody TbRoutChkMstVo tbRoutChkMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbRoutChkMstVo resultVo = null;
		Map<String,String> result = null;
		try {
			
			tbRoutChkMstVo.setSmodifyId(jwtUtil.getUserId(request));
			result = routMgmtService.insertListRoutChkMst(tbRoutChkMstVo);
			
			String resultCd = result.get("resultCd");
			String resultMsg = result.get("resultMsg");
			
			resultVo = new TbRoutChkMstVo();
			
			if(resultCd.equals("S0000")) {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {				
				resultVo.setCommonMsg(resultMsg);
			}
			
		} catch (ServiceException e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 실제 라우팅 DB 수집 전 확인 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutChkMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewPopRoutChkMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbFcltCmdMstListVo resultListVo = routMgmtService.selectListFcltOrgCmdMst(searchVo);
		return createResultList(resultListVo.getTbFcltCmdMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutChkMst.ajax", method = RequestMethod.POST)
	public String viewPopRoutChkMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewPopRoutChkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewPopRoutChkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewPopRoutChkMst";
	}
	private ModelMap viewPopRoutChkMstModel(@RequestBody TbRoutChkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltCmdMstListVo resultListVo = null;
		
		model.addAttribute("searchVo", searchVo);
		
		try{
			resultListVo = routMgmtService.selectListFcltOrgCmdMst(searchVo);
		} catch(ServiceException e){
			resultListVo = new TbFcltCmdMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbFcltCmdMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * 배치테스트
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/routmgmt/test.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo test(@ModelAttribute("searchVo") TbRoutChkMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		excel.insertIpHostMstBatch();
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 상세 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailRoutChkMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailRoutChkMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request){
		IpAllocOperMstVo resultVo = allocMgmtService.selectMainIpInfoMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailRoutChkMst.ajax", method = RequestMethod.POST)
	public String viewDetailRoutChkMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailRoutChkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);
		
		searchVoClone.setUrl("/ipmgmt/routmgmt/viewDetailRoutChkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewDetailRoutChkMst";
	}
	private ModelMap viewDetailRoutChkMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstVo tbIpInfoVo = null;
		
		try {	
			
			tbIpInfoVo =  allocMgmtService.selectMainIpInfoMst(searchVo);
			tbIpInfoVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbIpInfoVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbIpInfoVo.setCommonMsg(msgDesc);
		} 
		model.addAttribute("tbIpInfoVo", tbIpInfoVo);
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 > Nexthop 상세 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailNextHop.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailNextHop(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request){
		IpAllocOperMstVo resultVo = null;
		if(searchVo.getSpageType().equals("link")) {
			resultVo =  routMgmtService.selectLinkMst(searchVo);		// 링크 마스터 조회	
		} else {
			resultVo =  routMgmtService.selectNextHop(searchVo);		
		}
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailNextHop.ajax", method = RequestMethod.POST)
	public String viewDetailNextHop(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailNextHopModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewDetailNextHop.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewDetailNextHop";
	}
	private ModelMap viewDetailNextHopModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstVo tbIpInfoVo = null;
		
		try {	
			
			if(searchVo.getSpageType().equals("link")) {
				tbIpInfoVo =  routMgmtService.selectLinkMst(searchVo);		// 링크 마스터 조회	
			} else {
				tbIpInfoVo =  routMgmtService.selectNextHop(searchVo);		
			}
			
			if(tbIpInfoVo.getTotalCount() > 0) {
				tbIpInfoVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);	
			} else {
				tbIpInfoVo = new IpAllocOperMstVo();
				String msgDesc = ""; 
				if(searchVo.getSpageType().equals("link")) {
					msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.INFO.00054", new String[]{"링크 정보가 존재하지 않습니다."}));
				} else {
					msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.INFO.00054", new String[]{"Nexthop 정보가 존재하지 않습니다."}));	
				}
				
				tbIpInfoVo.setCommonMsg(msgDesc);
			}			
			
		} catch (ServiceException e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbIpInfoVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbIpInfoVo.setCommonMsg(msgDesc);
		} 
		model.addAttribute("tbIpInfoVo", tbIpInfoVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	

	/**
	 * IP주소 라우팅 비교/점검 > 예외처리 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutChkExceptMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewPopRoutChkExceptMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRoutChkMstListVo resultListVo = new TbRoutChkMstListVo();
		return createResultList(resultListVo.getTbRoutChkMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutChkExceptMst.ajax", method = RequestMethod.POST)
	public String viewPopRoutChkExceptMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewPopRoutChkExceptMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewPopRoutChkExceptMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewPopRoutChkExceptMst";
	}
	private ModelMap viewPopRoutChkExceptMstModel(@RequestBody TbRoutChkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRoutChkMstListVo resultListVo = null;
		
		model.addAttribute("searchVo", searchVo);
		
		try{
			List<CommonCodeVo> exceptCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ROUTING_EXCEPT_CD, null);
			model.addAttribute("sexceptCds", exceptCds);
				
		} catch(ServiceException e){
			resultListVo = new TbRoutChkMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbRoutChkMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외처리 팝업 > 예외 처리 및 해제
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/insertListExcptMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo insertListExcptMst(@RequestBody TbRoutChkMstVo tbRoutChkMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbRoutChkMstVo resultVo = null;
		String userId = jwtUtil.getUserId(request);
		
		try {
			
			tbRoutChkMstVo.setScreateId(userId);
			tbRoutChkMstVo.setSmodifyId(userId);
			
			String sexcptYn = tbRoutChkMstVo.getSexcpt_yn();
			
			if(sexcptYn.equals("Y")) {
				routMgmtService.insertListExcptMst(tbRoutChkMstVo);	
			} else if(sexcptYn.equals("N")) {
				routMgmtService.insertListExcptCancelMst(tbRoutChkMstVo);	
			}
			
			resultVo = new TbRoutChkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
			
		} catch (Exception e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 상세 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailRoutExcptMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailRoutExcptMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request){
		TbRoutChkMstListVo resultListVo = routMgmtService.selectListPageRoutExcptMstVo(searchVo);
		TbRoutChkMstVo resultVo = resultListVo.getTbRoutChkMstVos().get(0);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewDetailRoutExcptMst.ajax", method = RequestMethod.POST)
	public String viewDetailRoutExcptMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailRoutExcptMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewDetailRoutExcptMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewDetailRoutExcptMst";
	}
	private ModelMap viewDetailRoutExcptMstModel(@RequestBody TbRoutChkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRoutChkMstVo resultVo = null;
		
		try{
			TbRoutChkMstListVo resultListVo =  routMgmtService.selectListPageRoutExcptMstVo(searchVo);
			
			resultVo = resultListVo.getTbRoutChkMstVos().get(0);
			
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
				
		} catch(ServiceException e){
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	

	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/selectCheckService.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo selectCheckService(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		TbRoutChkMstVo resultVo = new TbRoutChkMstVo();
		List<String> chkListStr = new ArrayList<String>();
		TbRoutChkMstVo vo = new TbRoutChkMstVo();
		List<String> strList = new ArrayList<String>();
		String ipBlock = null;
		try{
			
			// 1. 체크된건 값 구분
			chkListStr = searchVo.getChkListStr();
			
			String scollectDt = "";							// 수집일자
			BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
			BigInteger nroutingChkMstSeq = null;		// 라우팅_비교_결과_MSTSEQ
			String sipPrefixGubun = "";						// 비교_구분(PIPMS/PROUTING)
					
			for(String tmp : chkListStr) {		// 체크한 건
				TbRoutChkMstListVo resultListVo = null;
				
				if(tmp.contains("_")) {
					scollectDt = tmp.split("_")[0];
					nlvlMstSeq = new BigInteger(tmp.split("_")[1]);
					nroutingChkMstSeq = new BigInteger(tmp.split("_")[2]);
					sipPrefixGubun = tmp.split("_")[3];
				} 
				
				
				vo.setScollectDtOrigin(scollectDt);
				vo.setNlvlMstSeq(nlvlMstSeq);
				vo.setNroutingChkMstSeq(nroutingChkMstSeq);
				vo.setSipPrefixGubun(sipPrefixGubun);
				
				// 2. 서비스 조회
				resultListVo = routMgmtService.selectListIpBlockServiceCheck(vo);
				
				if(null != resultListVo.getTbRoutChkMstVos().get(0)) {
					ipBlock = resultListVo.getTbRoutChkMstVos().get(0).getProutingIpPrefix();
					strList.add(ipBlock);
				}
				
			}
			
			if(strList.isEmpty()) {
				resultVo.setCommonMsg("SUCCESS01");  // 분할/병합 진행
			} else {
				resultVo.setStrIpBlock(strList);
				resultVo.setCommonMsg("SUCCESS02");  // 서비스 동일하게 하는 작업 먼저 우선 -> 팝업
			}
			
		} catch(ServiceException e){
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회 팝업
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutServiceChkMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewPopRoutServiceChkMst(@RequestBody TbRoutChkMstVo searchVo, HttpServletRequest request){
		ModelMap model = viewPopRoutServiceChkMstModel(searchVo, request);
		TbRoutChkMstListVo resultListVo = (TbRoutChkMstListVo)model.get("resultListVo");
		return createResultList(resultListVo.getTbRoutChkMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopRoutServiceChkMst.ajax", method = RequestMethod.POST)
	public String viewPopRoutServiceChkMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewPopRoutServiceChkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewPopRoutServiceChkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewPopRoutServiceChkMst";
	}
	private ModelMap viewPopRoutServiceChkMstModel(@RequestBody TbRoutChkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRoutChkMstListVo resultListVo = null;
		TbRoutChkMstVo vo = new TbRoutChkMstVo();
		
		try{
			
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅 **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			vo.setStrIpBlock(searchVo.getStrIpBlock());
			vo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			vo.setSipCreateTypeCd(searchVo.getSipCreateTypeCd());
			vo.setNlvlMstSeq(searchVo.getNlvlMstSeq());
			
			if(null != searchVo.getSpageType()) {
				vo.setProutingIpPrefix(searchVo.getProutingIpPrefix());
				resultListVo = routMgmtService.selectIpBlockServiceCheckList2(vo);
			} else {
				resultListVo = routMgmtService.selectIpBlockServiceCheckList(vo);	
				
			}
			
			
		} catch(ServiceException e){
			e.printStackTrace();
			resultListVo = new TbRoutChkMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			e.printStackTrace();
			resultListVo = new TbRoutChkMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 변경
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/updateServiceMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo updateServiceMst(@RequestBody TbRoutChkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		TbRoutChkMstVo resultVo = new TbRoutChkMstVo();
		List<String> chkListStr = new ArrayList<String>();
		TbRoutChkMstVo vo = new TbRoutChkMstVo();
		String userId = jwtUtil.getUserId(request);
		
		try{
			
			// 1. 체크된건 값 구분
			chkListStr = searchVo.getChkListStr();
			
			String scollectDt = "";							// 수집일자
			BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
			String proutingIpPrefix = null;					// 라우팅_비교_결과_MSTSEQ
			String sassignTypeCd = "";						// 서비스 value
					
			for(String tmp : chkListStr) {		// 체크한 건
				
				if(tmp.contains("_")) {
					scollectDt = tmp.split("_")[1];
					nlvlMstSeq = new BigInteger(tmp.split("_")[2]);
					proutingIpPrefix = tmp.split("_")[3];
					sassignTypeCd = tmp.split("_")[4];
				} 
				
				vo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				vo.setSipCreateTypeCd(searchVo.getSipCreateTypeCd());
				vo.setScollectDtOrigin(scollectDt);
				vo.setNlvlMstSeq(nlvlMstSeq);
				vo.setProutingIpPrefix(proutingIpPrefix);
				vo.setSassignTypeCd(sassignTypeCd);
				vo.setScreateId(jwtUtil.getUserId(request));
				vo.setSmodifyId(jwtUtil.getUserId(request));
				vo.setSpageType(searchVo.getSpageType());
				vo.setMenuType(searchVo.getMenuType());
				
				// 2. 서비스 변경
				routMgmtService.updateServiceMst(vo);
				
			}
			
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch(ServiceException e){
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbRoutChkMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 > 상태 업데이트
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/upateStatusMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo upateStatusMst(@RequestBody TbRoutChkMstVo tbRoutChkMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbRoutChkMstVo resultVo = null;

		String resultMsg = CommonCodeUtil.INTGRM_DB_RESULT03;		// DB현행화 실패
		String userId = jwtUtil.getUserId(request);
		
		try {
			
			tbRoutChkMstVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT04);  // DB현행화 성공
			routMgmtService.updateRoutChkMstVo(tbRoutChkMstVo);
			
			resultVo = new TbRoutChkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc); 
			
			updateRoutChkMstVo(tbRoutChkMstVo, CommonCodeUtil.INTGRM_DB_RESULT03, userId);
			
		} catch (Exception e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
			
			updateRoutChkMstVo(tbRoutChkMstVo,  CommonCodeUtil.INTGRM_DB_RESULT03, userId);
		}
		
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 할당 팝업
	 * @param ipAllocOperMstListVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopInsertAlcIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertAlcIPMst(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo, ModelMap model, HttpServletRequest request){
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListAlcIPMstViaInMstSeq2(ipAllocOperMstListVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopInsertAlcIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertAlcIPMst(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(ipAllocOperMstListVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertAlcIPMstModel(ipAllocOperMstListVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewPopInsertAlcIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewPopInsertAlcIPMst";
	}
	private ModelMap viewInsertAlcIPMstModel(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {
			
			resultListVo = allocMgmtService.selectListAlcIPMstViaInMstSeq2(ipAllocOperMstListVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		
		model.addAttribute("tbRoutChkMstVo", ipAllocOperMstListVo.getTbRoutChkMstVo());
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 > 해지 팝업
	 * @param ipAllocOperMstListVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopDetailAlcIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailAlcIPMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request){
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListIpAllocDetail(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/routmgmt/viewPopDetailAlcIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailAlcIPMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailAlcIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/routmgmt/viewPopDetailAlcIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/routmgmt/viewPopDetailAlcIPMst";
	}
	private ModelMap viewDetailAlcIPMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {
			resultListVo = allocMgmtService.selectListIpAllocDetail(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		
		model.addAttribute("tbRoutChkMstVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
}
