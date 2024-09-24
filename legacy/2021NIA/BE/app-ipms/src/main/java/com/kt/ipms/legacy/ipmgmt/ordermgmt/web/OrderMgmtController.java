package com.kt.ipms.legacy.ipmgmt.ordermgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.service.OrderMgmtService;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstListVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class OrderMgmtController extends CommonController {
	
	@Autowired
	private OrderMgmtService orderMgmtService;
	
	/**
	 * Neoss 오더 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ordermgmt/viewListNeossMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListIpAllocOrderMst(@RequestBody IpAllocOrderMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpAllocOrderMstListVo resultListVo = orderMgmtService.selectListIpAllocOrderMst(searchVo);
		return createResultList(resultListVo.getIpAllocOrderMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/ordermgmt/viewListNeossMst.do", method = RequestMethod.POST)
	public String selectListIpAllocOrderMst(@ModelAttribute("searchVo") IpAllocOrderMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
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
		ModelMap builtModel = selectListIpAllocOrderMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ordermgmt/viewListNeossMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ordermgmt/viewListNeossMst";
	}
	private ModelMap selectListIpAllocOrderMstModel(@ModelAttribute("searchVo") IpAllocOrderMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOrderMstListVo resultListVo = null;
		String sloadFlg = "F"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			List<CommonCodeVo> svcUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_USE_TYPE_CD, null);
			
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
			
			model.addAttribute("svcUseTypeCd", svcUseTypeCd);
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 수용국 코드 설정 **/
			//List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			List<CommonCodeVo> sLvlSubvCds = orderMgmtService.selectOrderSofficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
			/** 오더 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("SODRREGDT");
				sloadFlg = "F";//자동 조회되도록 변경 재요청_전필권과장 (T=>F)(2014.12.29)
			}else{
				sloadFlg = "F";
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_DESC);
			}
			
			setPagination(searchVo);
			if (sloadFlg.equals("T")){
				resultListVo = new IpAllocOrderMstListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = orderMgmtService.selectListIpAllocOrderMst(searchVo);
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOrderMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOrderMstListVo();
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
	 * Neoss 오더 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ordermgmt/viewListNeossMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo selectListIpAllocOrderMstExcel(@ModelAttribute("searchVo") IpAllocOrderMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response){
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
			
			/** 오더 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("SODRREGDT");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			IpAllocOrderMstListVo resultListVo = orderMgmtService.selectListIpAllocOrderMstExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("노드국|getSicisofficesname");
			mappingList.add("수용국|getSofficename");
			mappingList.add("오더번호|getSordernum");
			mappingList.add("SAID|getSsaid");
			mappingList.add("전용회선번호|getSllnum");
			mappingList.add("접수일|getSodrregdt");
			mappingList.add("희망일|getSodrhopedt");
			mappingList.add("고객명|getScustname");
			mappingList.add("상품명|getSexSvcNm"); 
			mappingList.add("이용목적|getSsvcUseTypeNm");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpAllocOrderMstVos(), mappingList, request);
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
	 * 상품정보 조회 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ordermgmt/viewSearchNeossSvcMst.model", method = RequestMethod.POST)
	public ModelMap viewSearchNeossSvcMst(@RequestBody TbIpmsSvcMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpmsSvcMstListVo resultListVo = orderMgmtService.selectListIpmsSvc(searchVo);
		return createResultList(resultListVo.getTbIpmsSvcMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/ordermgmt/viewSearchNeossSvcMst.ajax", method = RequestMethod.POST)
	public String viewSearchNeossSvcMst(@RequestBody TbIpmsSvcMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
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
		ModelMap builtModel = viewSearchNeossSvcMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ordermgmt/viewSearchNeossSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ordermgmt/viewSearchNeossSvcMst";
	}
	private ModelMap viewSearchNeossSvcMstModel(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpmsSvcMstListVo resultListVo = null;
		try {
			List<CommonCodeVo> svcHgroupCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_HGROUP_CD, null);
			List<CommonCodeVo> sexLinkUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SEX_LINK_USE_TYPE_CD, null);
			List<CommonCodeVo> sassignTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
			List<CommonCodeVo> svcUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_USE_TYPE_CD, null);
			List<CommonCodeVo> suseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.USE_TYPE_CD, null);
			TbIpmsSvcMstListVo ssvcMgroupCds = null;
			TbIpmsSvcMstListVo ssvcLgroupCds = null;
			if (StringUtils.hasText(searchVo.getSsvcHgroupCd())) {
				TbIpmsSvcMstVo searchMgroupVo = new TbIpmsSvcMstVo();
				searchMgroupVo.setSsvcHgroupCd(searchVo.getSsvcHgroupCd());
				ssvcMgroupCds = orderMgmtService.selectListSvcMgroupNm(searchMgroupVo);
				if (StringUtils.hasText(searchVo.getSsvcMainClsCode())) {
					TbIpmsSvcMstVo searchLgroupVo = new TbIpmsSvcMstVo();
					searchLgroupVo.setSsvcMainClsCode(searchVo.getSsvcMainClsCode());
					ssvcLgroupCds = orderMgmtService.selectListSvcMgroupNm1(searchLgroupVo);
				} else {
					ssvcLgroupCds = new TbIpmsSvcMstListVo();
				}
			} else {
				ssvcMgroupCds = new TbIpmsSvcMstListVo();
				ssvcLgroupCds = new TbIpmsSvcMstListVo();
			}
			model.addAttribute("ssvcMgroupCds", ssvcMgroupCds);
			model.addAttribute("ssvcLgroupCds", ssvcLgroupCds);
			model.addAttribute("svcHgroupCd", svcHgroupCd);
			model.addAttribute("sexLinkUseTypeCd", sexLinkUseTypeCd);
			model.addAttribute("sassignTypeCd", sassignTypeCd);
			model.addAttribute("svcUseTypeCd", svcUseTypeCd);
			model.addAttribute("suseTypeCd", suseTypeCd);
			setPagination(searchVo);
			resultListVo = orderMgmtService.selectListIpmsSvc(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setCommonMsg(e.getMessage());
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfoSvc", paginationInfo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	/**
	 * 오더 세부 정보 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/ordermgmt/viewDetailNeossMst.model", method = RequestMethod.POST)
	public ModelMap viewDetailSubSvcMst(@RequestBody IpAllocOrderMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpAllocOrderMstListVo resultListVo = orderMgmtService.selectListIpAllocOrderMst(searchVo);
		return createResultList(resultListVo.getIpAllocOrderMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/ordermgmt/viewDetailNeossMst.ajax", method = RequestMethod.POST)
	public String viewDetailSubSvcMst(@RequestBody IpAllocOrderMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
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
		ModelMap builtModel = viewDetailSubSvcMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ordermgmt/viewDetailNeossMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ordermgmt/viewDetailNeossMst";
	}
	private ModelMap viewDetailSubSvcMstModel(@RequestBody IpAllocOrderMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOrderMstListVo resultListVo = null;
		try {
			resultListVo = orderMgmtService.selectListIpAllocOrderMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOrderMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOrderMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	
	/**
	 * IP블록정보 조회 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ordermgmt/viewSearchAssignMst.model", method = RequestMethod.POST)
	public ModelMap viewSearchAssignMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpAllocOperMstListVo resultListVo = orderMgmtService.selectListIpAllocMst(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/ordermgmt/viewSearchAssignMst.ajax", method = RequestMethod.POST)
	public String viewSearchAssignMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
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
		ModelMap builtModel = viewSearchAssignMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ordermgmt/viewSearchAssignMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ordermgmt/viewSearchAssignMst";
	}
	private ModelMap viewSearchAssignMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {
			
			//망 정보 셋팅
			Map<String, String> ssvcLineTypeCdParamMap = new HashMap<String, String>();
			ssvcLineTypeCdParamMap.put("startCd", "CL0001");
			ssvcLineTypeCdParamMap.put("endCd", "CL0002");
			List<CommonCodeVo> ssvcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, ssvcLineTypeCdParamMap);
			
			//오더할당 대상 블록 망정보
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			
			//조회조건에 따른 서비스 정보 셋팅 및 조건 초기화
			List<CommonCodeVo> sassignTypeCds = orderMgmtService.selectListSassignLevelCds(searchVo);
			searchVo.setSexSvcCd("");
			searchVo.setSsvcUseTypeCd("");
			if (sassignTypeCds != null && sassignTypeCds.size() > 0){
				if(!StringUtils.hasText(searchVo.getSassignTypeCd())){
					searchVo.setSassignTypeCd(sassignTypeCds.get(0).getCode());
				}
			}else{
				throw new ServiceException("CMN.HIGH.00023", new String[]{" (원인 : 상품-서비스 정보 미존재) 할당 대상 블록목록 "});
			}
			
			//배정레벨 셋팅
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			assignLevelCdParamMap.put("startCd", "IA0004");
			assignLevelCdParamMap.put("endCd", "IA0006");
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD, assignLevelCdParamMap);
			
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			model.addAttribute("ssvcLineTypeCds", ssvcLineTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("sassignLevelCds", sassignLevelCds);
			
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
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 할당 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			setPagination(searchVo);
			resultListVo = orderMgmtService.selectListIpAllocMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfoAssign", paginationInfo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	
	/**
	 * 오더 할당 정보 처리
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/ordermgmt/insertOrderAlcIPMst.json", method = RequestMethod.POST)
	public IpAllocOrderMstVo insertOrderAlcIPMst(@RequestBody IpAllocOrderMstComplexVo ipAllocOrderMstComplexVo,
			HttpServletRequest request, HttpServletResponse response){
		IpAllocOrderMstVo resultVo = null;
		try {
			if (ipAllocOrderMstComplexVo == null || ipAllocOrderMstComplexVo.getSrcIpAllocOrderMstVo() == null 
				|| ipAllocOrderMstComplexVo.getDestIpAllocOrderMstVos() == null || ipAllocOrderMstComplexVo.getDestIpAllocOrderMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			IpAllocOrderMstVo srcIpAllocOrderMstVo = ipAllocOrderMstComplexVo.getSrcIpAllocOrderMstVo();
			srcIpAllocOrderMstVo.setScreateId(jwtUtil.getUserId(request));
			ipAllocOrderMstComplexVo.setScreateId(jwtUtil.getUserId(request));
			orderMgmtService.insertListOrderAlcIPMst(ipAllocOrderMstComplexVo);
			resultVo = new IpAllocOrderMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new IpAllocOrderMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new IpAllocOrderMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	
	/**
	 * 수용국 정보 조회
	 * @param tbIpAssignSubVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/ordermgmt/selectOrderOfficeList.json", method = RequestMethod.POST)
	@ResponseBody
	public IpAllocOrderMstListVo selectOfficeList(@RequestBody IpAllocOrderMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		IpAllocOrderMstListVo resultListVo = null;
		try{
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			resultListVo = orderMgmtService.selectSetOrderOfficeList(searchVo);
			
		} catch (ServiceException e) {
			resultListVo = new IpAllocOrderMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOrderMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
}
