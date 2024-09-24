package com.kt.ipms.legacy.ipmgmt.historymgmt.web;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.service.HistoryMgmtService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstListVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class HistoryMgmtController extends CommonController {

	@Autowired
	private HistoryMgmtService historyMgmtService;
	
	/**
	 * 이력관리 페이지 로드
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value="/ipmgmt/historymgmt/viewListIpHistoryMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListIpHistoryMst(@RequestBody IpHistoryMstVo searchVo, ModelMap model, HttpServletRequest request){
		IpHistoryMstListVo resultListVo = historyMgmtService.selectListIpHistMst(searchVo);
		return createResultList(resultListVo.getIpHistoryMstVos(), resultListVo.getTotalCount());
	}
	
	@RequestMapping(value="/ipmgmt/historymgmt/viewListIpHistoryMst.do", method = RequestMethod.POST)
	public String selectListIpHistoryMst(@ModelAttribute("searchVo") IpHistoryMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpHistoryMstVo searchVoClone = new IpHistoryMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListIpHistoryMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/historymgmt/viewListIpHistoryMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/historymgmt/viewListIpHistMst";
	}
	private ModelMap selectListIpHistoryMstModel(@ModelAttribute("searchVo") IpHistoryMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpHistoryMstListVo resultListVo = null;
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		
		try {
			
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			List<CommonCodeVo> nipHistTaskCds = commonCodeService.selectListCommonCode(CommonCodeUtil.HIST_TASK_CD, null);
			
			model.addAttribute("sassignLevelCds", sassignLevelCds);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("nipHistTaskCds", nipHistTaskCds);
			
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
					centerListVo = new TbLvlBasListVo();
					nodeListVo = new TbLvlBasListVo();
				}
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			TbLvlMstListVo resultSeqList = new TbLvlMstListVo();
			if(!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				searchVo.setLvlMstSeqListVo(null);
			} else {
				searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
				searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
				resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
				searchVo.setLvlMstSeqListVo(resultSeqList);
			}
			
			
			
			/** 조직별 서비스 유형 셋팅 **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = historyMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getLlSrchTypeCd())) {
				searchVo.setLlSrchTypeCd("llnum");
			}
	
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소 / 회선, 할당 기반)*/
			if (StringUtils.hasText(searchVo.getMoveType())){
				if (searchVo.getMoveType().equals("llnum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSllnum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}else if (searchVo.getMoveType().equals("ordernum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSordernum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}
				sloadFlg = "F";
			}else{
				if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
					sloadFlg = "F";
				}
				
				if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
					searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
					sloadFlg = "F";
				}
			}
			
			IpHistoryMstListVo yyyyList = new IpHistoryMstListVo();
			IpHistoryMstVo vo = new IpHistoryMstVo();
			yyyyList = historyMgmtService.selectTableYear(vo);
			model.addAttribute("yyyyList", yyyyList);

			setPagination(searchVo);
			
			if (searchVo.getYyyy() == 0) {
				sloadFlg = "T";
				int yyyy = Calendar.getInstance().get(Calendar.YEAR);
				searchVo.setYyyy(yyyy);
			}else{
				sloadFlg = "F";
			}
			
			if (sloadFlg.equals("T")){
				resultListVo = new IpHistoryMstListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = historyMgmtService.selectListIpHistMst(searchVo);
			}
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
			resultListVo = new IpHistoryMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			e.printStackTrace();
			resultListVo = new IpHistoryMstListVo();
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
	 * 이력관리 목록 엑셀 저장
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/historymgmt/viewListIpHistoryMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo selectListIpHistoryMstExcel(@ModelAttribute("searchVo") IpHistoryMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){

		FileVo resultVo = new FileVo();
		
		try {
			
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
					centerListVo = new TbLvlBasListVo();
					nodeListVo = new TbLvlBasListVo();
				}
			}
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소 / 회선, 할당 기반)*/
			if (StringUtils.hasText(searchVo.getMoveType())){
				if (searchVo.getMoveType().equals("llnum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSllnum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}else if (searchVo.getMoveType().equals("ordernum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSordernum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}
			}else{
				if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
				}
				
				if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
					searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
				}
			}
			
			IpHistoryMstListVo resultListVo = historyMgmtService.selectListIpHistMstExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("작업시스템|getSworkSystem");
			mappingList.add("메뉴명|getSmenuNm");
			mappingList.add("상세작업분류|getSipHistTaskNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("할당상태|getSassignLevelNm");
			mappingList.add("작업자|getScreateNm");
			mappingList.add("작업일자|getDcreateDt");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("장비명|getSsubscnealias");
			mappingList.add("I/F명|getSsubsclgipportdescription");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpHistoryMstVos(), mappingList, request);
			if (StringUtils.hasText(fileName)) {
				resultVo.setFileName(fileName);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00050");
			}
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * 이력관리 이력 등록
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value="ipmgmt/historymgmt/insertTbHistoryVo.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlCdVo insertTbCmnMsgCd(@RequestBody IpHistoryMstVo insertVo, HttpServletRequest request, HttpServletResponse reponse)  {
		TbLvlCdVo resultVo = new TbLvlCdVo();
		try {
			historyMgmtService.insertTbIpHistoryMstVo(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbLvlCdVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/ipmgmt/historymgmt/viewDetailIpHistMst.model", method = RequestMethod.POST)
	public ModelMap viewDetailIpHistMst(@RequestBody IpHistoryMstVo searchVo, ModelMap model, HttpServletRequest request){
		IpHistoryMstVo resultVo = historyMgmtService.selectMainIpInfoMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/historymgmt/viewDetailIpHistMst.ajax", method = RequestMethod.POST)
	public String viewDetailIpHistMst(@RequestBody IpHistoryMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpHistoryMstVo searchVoClone = new IpHistoryMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailIpHistMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/historymgmt/viewDetailIpHistMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/historymgmt/viewDetailIpHistMst";
	}
	private ModelMap viewDetailIpHistMstModel(@RequestBody IpHistoryMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpHistoryMstVo ipHistoryMstVo = null;
		
		try {	
			
			ipHistoryMstVo =  historyMgmtService.selectMainIpInfoMst(searchVo);
			ipHistoryMstVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			ipHistoryMstVo = new IpHistoryMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			ipHistoryMstVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			ipHistoryMstVo = new IpHistoryMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			ipHistoryMstVo.setCommonMsg(msgDesc);
		} 
		model.addAttribute("ipHistoryMstVo", ipHistoryMstVo);
		return model;
	}
	

	
}
