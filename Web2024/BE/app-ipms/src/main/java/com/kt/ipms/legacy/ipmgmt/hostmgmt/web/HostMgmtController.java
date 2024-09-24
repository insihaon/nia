package com.kt.ipms.legacy.ipmgmt.hostmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.service.HostMgmtService;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class HostMgmtController extends CommonController {
	
	@Autowired
	private HostMgmtService hostMgmtService;

	@RequestMapping(value="/ipmgmt/hostmgmt/viewListHostIPMst.model", method = RequestMethod.POST)
	public ModelMap viewListHostIPMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpHostMstListVo resultListVo = hostMgmtService.selectListIpHostMst(searchVo);
		return createResultList(resultListVo.getTbIpHostMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value="/ipmgmt/hostmgmt/viewListHostIPMst.ajax", method = RequestMethod.POST)
	public String viewListHostIPMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListHostIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewListHostIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewListHostIPMst";
	}
	private ModelMap viewListHostIPMstModel(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = hostMgmtService.selectListIpHostMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/viewListHostIPMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo viewListHostIPMstExcel(@ModelAttribute("searchVo")  TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		FileVo resultVo = new FileVo();
		try {
			
			TbIpHostMstListVo resultListVo = hostMgmtService.selectListIpHostMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("수용국|getSrssofficesNm");
			mappingList.add("Host IP|getPipHostInet");
			mappingList.add("용도|getScomment");
			//mappingList.add("HOST유형|getSipHostTypeNm");
			mappingList.add("I/F명|getSipIfNm");
			mappingList.add("장비명|getSipHostNm");
			//mappingList.add("모델명|getSmodelname");			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbIpHostMstVos(), mappingList, request);
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

	@RequestMapping(value="/ipmgmt/hostmgmt/viewDetailHostIPMst.model", method = RequestMethod.POST)
	public ModelMap viewDetailHostIPMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpHostMstListVo resultListVo = hostMgmtService.selectListIpHostMstViaIpInfo(searchVo);
		return createResultList(resultListVo.getTbIpHostMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/hostmgmt/viewDetailHostIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailHostIPMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailHostIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewDetailHostIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewDetailHostIPMst";
	}
	private ModelMap viewDetailHostIPMstModel(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstListVo resultListVo = null;
		try {
			resultListVo = hostMgmtService.selectListIpHostMstViaIpInfo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	@RequestMapping(value="/ipmgmt/hostmgmt/updateListHostIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpHostMstVo updateListHostIPMst(@RequestBody TbIpHostMstListVo tbIpHostMstListVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo resultVo = null;
		try {
			hostMgmtService.updateListIpHostMst(tbIpHostMstListVo);
			resultVo = new TbIpHostMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/updateHostIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpHostMstVo updateHostIPMst(@RequestBody TbIpHostMstVo tbIpHostMstVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo resultVo = null;
		try {
			hostMgmtService.updateIpHostMst(tbIpHostMstVo);
			resultVo = new TbIpHostMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/viewListIpHostMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListIpHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
		HttpServletRequest request){
		TbIpHostMstListVo resultListVo = hostMgmtService.selectListVirtualIpHostMst(searchVo);
		return createResultList(resultListVo.getTbIpHostMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/hostmgmt/viewListIpHostMst.do", method = RequestMethod.POST)
	public String viewListIpHostMst(@ModelAttribute("searchVo") TbIpHostMstVo searchVo, ModelMap model,
		HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListIpHostMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewListIpHostMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewListIpHostMst";
	}
	private ModelMap viewListIpHostMstModel(@ModelAttribute("searchVo") TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstListVo resultListVo = null;
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			//List<CommonCodeVo> sassignTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			//model.addAttribute("sassignTypeCds", sassignTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			
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
			
			if (!StringUtils.hasText(searchVo.getSipHostTypeCd())){
				searchVo.setSipHostTypeCd("HT0010");
			}	
			/** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = hostMgmtService.selectLoadOfficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}	
			
			setPagination(searchVo);
			resultListVo = hostMgmtService.selectListVirtualIpHostMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpHostMstListVo();
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
	
	
	@RequestMapping(value = "/ipmgmt/hostmgmt/selectOfficeList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpHostMstListVo selectOfficeList(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstListVo resultListVo = null;
		try{
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			if (!StringUtils.hasText(searchVo.getSipHostTypeCd())){
				searchVo.setSipHostTypeCd("HT0010");
			}	
			resultListVo = hostMgmtService.selectOfficeList(searchVo);
			//resultListVo = jwtUtil.getNodeList(request, searchVo);
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	@RequestMapping(value="/ipmgmt/hostmgmt/viewInsertIpHostMst.model", method = RequestMethod.POST)
	public ModelMap viewInsertIpHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpHostMstListVo resultListVo = new TbIpHostMstListVo();
		return createResultList(resultListVo.getTbIpHostMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/hostmgmt/viewInsertIpHostMst.ajax", method = RequestMethod.POST)
	public String viewInsertIpHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertIpHostMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewInsertIpHostMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewInsertIpHostMst";
	}
	private ModelMap viewInsertIpHostMstModel(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstListVo resultListVo = new TbIpHostMstListVo();
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpHostMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/insertHostIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpHostMstVo insertHostIPMst(@RequestBody TbIpHostMstVo tbIpHostMstVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo resultVo = null;
		try {
			hostMgmtService.insertIpHostMst(tbIpHostMstVo);
			resultVo = new TbIpHostMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/deleteHostIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpHostMstVo deleteHostIPMst(@RequestBody TbIpHostMstVo tbIpHostMstVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo resultVo = null;
		try {
			hostMgmtService.deleteIpHostMst(tbIpHostMstVo);
			resultVo = new TbIpHostMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/viewDetailIPHostMst.model", method = RequestMethod.POST)
	public ModelMap viewDetailIPHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpHostMstVo resultVo = hostMgmtService.selectTbIpHostMstVo(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/hostmgmt/viewDetailIPHostMst.ajax", method = RequestMethod.POST)
	public String viewDetailIPHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailIPHostMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewDetailIPHostMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewDetailIpHostMst";
	}
	private ModelMap viewDetailIPHostMstModel(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstVo resultVo = null;
		try {
			resultVo = hostMgmtService.selectTbIpHostMstVo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	@RequestMapping(value="/ipmgmt/hostmgmt/viewUpdateIPHostMst.model", method = RequestMethod.POST)
	public ModelMap viewUpdateIPHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpHostMstVo resultVo = hostMgmtService.selectTbIpHostMstVo(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/hostmgmt/viewUpdateIPHostMst.ajax", method = RequestMethod.POST)
	public String viewUpdateIPHostMst(@RequestBody TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		TbIpHostMstVo searchVoClone = new TbIpHostMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateIPHostMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/hostmgmt/viewUpdateIPHostMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/hostmgmt/viewUpdateIpHostMst";
	}
	private ModelMap viewUpdateIPHostMstModel(@RequestBody TbIpHostMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpHostMstVo resultVo = null;
		try {
			resultVo = hostMgmtService.selectTbIpHostMstVo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpHostMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	@RequestMapping(value="/ipmgmt/hostmgmt/viewListVirtualHostInfoExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo viewListVirtualHostInfoExcel(@ModelAttribute("searchVo") TbIpHostMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
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
			
			if (!StringUtils.hasText(searchVo.getSipHostTypeCd())){
				searchVo.setSipHostTypeCd("HT0010");
			}	
			
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}	
			
			
			TbIpHostMstListVo resultListVo = hostMgmtService.selectListVirtualIpHostExcel(searchVo);			
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("수용국|getSrssofficesNm");
			mappingList.add("IP|getPipHostInet");
			mappingList.add("장비명|getSipHostNm");
			mappingList.add("모델명|getSmodelname");
			mappingList.add("용도|getScomment");
			mappingList.add("대표여부|getSprorityYn");
			mappingList.add("작업일자|getDmodifyDt");
			String fileName = excelUtil.createExcelFile(resultListVo.getTbIpHostMstVos(), mappingList, request);
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

}
