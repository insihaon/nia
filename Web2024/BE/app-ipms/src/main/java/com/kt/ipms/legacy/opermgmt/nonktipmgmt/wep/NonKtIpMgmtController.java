package com.kt.ipms.legacy.opermgmt.nonktipmgmt.wep;

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
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.service.NonKtIpMgmtService;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockListVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class NonKtIpMgmtController extends CommonController {
	@Autowired NonKtIpMgmtService nonKtIpMgmtService;
	
	/**
	 * NON-KT IP 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/nonktipmgmt/viewListNonKtIpSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListNonKtIpSvcMst(@RequestBody TbNonKtSvcMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbNonKtSvcMstListVo resultListVo = nonKtIpMgmtService.viewListNonKtIpSvcMst(searchVo);
		return createResultList(resultListVo.getTbNonKtSvcMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/nonktipmgmt/viewListNonKtIpSvcMst.do", method = RequestMethod.POST)
	public String viewListNonKtIpSvcMst(@ModelAttribute("searchVo") TbNonKtSvcMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbNonKtSvcMstVo searchVoClone = new TbNonKtSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListNonKtIpSvcMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/nonktipmgmt/viewListNonKtIpSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/nonktipmgmt/viewListNonKtIpSvcMst";
	}
	private ModelMap viewListNonKtIpSvcMstModel(@ModelAttribute("searchVo") TbNonKtSvcMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbNonKtSvcMstListVo resultListVo = null;
		try{
			List<CommonCodeVo> sicisofficescode = nonKtIpMgmtService.selectIcisOfficesCodeList(searchVo);
			model.addAttribute("sicisofficescode", sicisofficescode);
			List<CommonCodeVo> sofficecode = nonKtIpMgmtService.selectsOfficeCodeList(searchVo);
			model.addAttribute("sofficecode", sofficecode);
			setPagination(searchVo);
			resultListVo = nonKtIpMgmtService.viewListNonKtIpSvcMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbNonKtSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbNonKtSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/**
	 * NON-KT IP 상세
	 * @param tbNonKtSvcMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/nonktipmgmt/viewDetailNonKtIpSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailNonKtIpSvcMst(@RequestBody TbNonKtSvcMstVo tbNonKtSvcMstVo, ModelMap model, HttpServletRequest request) {
		TbNonKtSvcMstVo resultVo = nonKtIpMgmtService.viewDetailNonKtIpSvcMst(tbNonKtSvcMstVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/nonktipmgmt/viewDetailNonKtIpSvcMst.ajax", method = RequestMethod.POST)
	public String viewDetailNonKtIpSvcMst(@RequestBody TbNonKtSvcMstVo tbNonKtSvcMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbNonKtSvcMstVo searchVoClone = new TbNonKtSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(tbNonKtSvcMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailNonKtIpSvcMstModel(tbNonKtSvcMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/nonktipmgmt/viewDetailNonKtIpSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/nonktipmgmt/viewDetailNonKtIpSvcMst";
	}
	private ModelMap viewDetailNonKtIpSvcMstModel(@RequestBody TbNonKtSvcMstVo tbNonKtSvcMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbNonKtSvcMstVo resultVo = null;
		try {
			resultVo = nonKtIpMgmtService.viewDetailNonKtIpSvcMst(tbNonKtSvcMstVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbNonKtSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbNonKtSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * IP블록 리스트 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/nonktipmgmt/selectListNonKtIpBlock.json", method = RequestMethod.POST)
	public TbNonKtIpblockListVo selectListNonKtIpBlock(@RequestBody TbNonKtIpblockVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbNonKtIpblockListVo resultListVo = null;
		try{
			resultListVo = nonKtIpMgmtService.selectListNonKtIpBlock(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbNonKtIpblockListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbNonKtIpblockListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	/**
	 * Non KT Ip블록 작업완료 호출
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/nonktipmgmt/workCompletionIpBlock.json", method = RequestMethod.POST)
	@ResponseBody
	public TbNonKtIpblockVo workCompletionIpBlock(@RequestBody TbNonKtIpblockListVo tbNonKtIpblockListVo, HttpServletRequest request, HttpServletResponse response) {
		TbNonKtIpblockVo resultVo = null;
		try{
			nonKtIpMgmtService.workCompletionIpBlock(tbNonKtIpblockListVo);
			resultVo = new TbNonKtIpblockVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbNonKtIpblockVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbNonKtIpblockVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * Non KT 관리 리스트 엑셀 다운
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/nonktipmgmt/selectListNonKtIpSvcMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo selectListNonKtIpSvcMstExcel(@ModelAttribute("searchVo") TbNonKtSvcMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			TbNonKtSvcMstListVo resultListVo = nonKtIpMgmtService.selectListNonKtIpSvcMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("노드국|getSicisofficesname");
			mappingList.add("수용국|getSofficename");
			mappingList.add("IP|getSipBlock");
			mappingList.add("전용회선번호|getSllnum");
			mappingList.add("고객명|getScustname");
			mappingList.add("BGP영문고객명|getSenCustname");
			mappingList.add("상품명|getSexSvcNm");
			mappingList.add("AS번호|getSasNum");
			mappingList.add("상태|getSflagTypeNm");
			mappingList.add("처리자명|getSworkername");
			mappingList.add("처리일|getDmodifyDt");
			String fileName = excelUtil.createExcelFile(resultListVo.getTbNonKtSvcMstVos(), mappingList, request);
			if(StringUtils.hasText(fileName)) {
				resultVo.setFileName(fileName);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00050");
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
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
