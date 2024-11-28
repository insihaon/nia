package com.kt.ipms.legacy.ipmgmt.linkmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codej.web.annotation.EncryptResponse;
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.service.LinkMgmtService;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstListVo;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class LinkMgmtController extends CommonController{

	@Autowired
	private LinkMgmtService linkMgmtService;
	
	/**
	 * 운용정보관리(링크) > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/viewListIpLinkMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListIpLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request){
		setPagination(searchVo);
		TbIpLinkMstListVo resultListVo = linkMgmtService.selectListIpLinkMst(searchVo);
		return createResultList(resultListVo.getTbIpLinkMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/linkmgmt/viewListIpLinkMst.do", method = RequestMethod.POST)
	public String viewListIpLinkMst(@ModelAttribute("searchVo") TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListIpLinkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linkmgmt/viewListIpLinkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewListIpLinkMst";
	}
	private ModelMap viewListIpLinkMstModel(@ModelAttribute("searchVo") TbIpLinkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpLinkMstListVo resultListVo = null;
		try {
			
			/** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = linkMgmtService.selectLoadOfficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}	
			if (!StringUtils.hasText(searchVo.getLlSrchTypeCd())) {
				searchVo.setLlSrchTypeCd("llnum");
			}
			
			setPagination(searchVo);
			resultListVo = linkMgmtService.selectListIpLinkMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpLinkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpLinkMstListVo();
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
	 * 운용정보관리(링크) > 엑셀 다운로드
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/viewListIpLinkMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListIpLinkMstExcel(@RequestBody TbIpLinkMstVo searchVo, ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		TbIpLinkMstListVo resultListVo = null;
		try {
			
			/** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = linkMgmtService.selectLoadOfficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}	
			if (!StringUtils.hasText(searchVo.getLlSrchTypeCd())) {
				searchVo.setLlSrchTypeCd("llnum");
			}
			
			setPagination(searchVo);
			resultListVo = linkMgmtService.selectListIpLinkMst(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("링크IP블록|getPifSerialIp");
			mappingList.add("자국 수용국명|getSaofficescodeNm");
			mappingList.add("자국 장비명|getSanealias");
			mappingList.add("자국 장비IP|getSamstip");
			mappingList.add("자국 IF명|getSaifname");
			
			mappingList.add("대국 수용국명|getSzofficescodeNm");
			mappingList.add("대국 장비명|getSznealias");
			mappingList.add("대국 장비IP|getSzmstip");
			mappingList.add("대국 IF명|getSzifname");
			
			mappingList.add("SAID|getSsaid");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("수용회선명|getSconnalias");
			
			mappingList.add("작업일자|getDmodifyDt");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpLinkMstVos(), mappingList, request);
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	/**
	 * 운용정보관리(링크) > 수용국 조회 
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/linkmgmt/selectOfficeList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectOfficeList(@RequestBody TbIpLinkMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstListVo resultListVo = null;
		try{
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			resultListVo =  linkMgmtService.selectOfficeList(searchVo);
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpLinkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpLinkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	/**
	 * 운용정보관리(링크) > 수용국 조회 
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/viewDetailIPLinkMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailIPLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request){
		TbIpLinkMstVo resultVo = linkMgmtService.selectTbIpLinkMstVo(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/linkmgmt/viewDetailIPLinkMst.ajax", method = RequestMethod.POST)
	public String viewDetailIPLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailIPLinkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/mgmt/linkmgmt/viewDetailIPLinkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewDetailIPLinkMst";
	}
	private ModelMap viewDetailIPLinkMstModel(@RequestBody TbIpLinkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpLinkMstVo resultVo = null;
		try {
			resultVo = linkMgmtService.selectTbIpLinkMstVo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	/**
	 * 운용정보관리(링크) > 등록 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/viewInsertIpLinkMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertIpLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request){
		TbIpLinkMstListVo resultListVo = new TbIpLinkMstListVo();
		return createResultList(resultListVo.getTbIpLinkMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/linkmgmt/viewInsertIpLinkMst.ajax", method = RequestMethod.POST)
	public String viewInsertIpLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertIpLinkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linkmgmt/viewInsertIpLinkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewInsertIpLinkMst";
	}
	private ModelMap viewInsertIpLinkMstModel(@RequestBody TbIpLinkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpLinkMstListVo resultListVo = new TbIpLinkMstListVo();
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpLinkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpLinkMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	/**
	 * 운용정보관리(링크) > 등록
	 * @param tbIpLinkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/insertLinkIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertLinkIPMst(@RequestBody TbIpLinkMstVo tbIpLinkMstVo, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo resultVo = null;
		try {
			tbIpLinkMstVo.setSmodifyId(jwtUtil.getUserId(request));
			linkMgmtService.insertIpLinkMst(tbIpLinkMstVo);
			resultVo = new TbIpLinkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/***
	 * 수용국 검색 팝업 화면 실행
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/ipmgmt/linkmgmt/viewSearchAOfficeCode.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchAOfficeCode(@RequestBody TbLvlCdVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbLvlCdListVo resultListVo = new TbLvlCdListVo();
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/linkmgmt/viewSearchAOfficeCode.ajax", method = RequestMethod.POST)
	public String viewSearchAOfficeCode(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchAOfficeCodeModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linkmgmt/viewSearchAOfficeCode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewSearchAOfficeCode";
	}
	private ModelMap viewSearchAOfficeCodeModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo =  new TbLvlCdListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		
		model.addAttribute("resultListVo", resultListVo);	
		return model;
	}
	
	
	/***
	 * 수용국 검색 팝업 화면 실행
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/ipmgmt/linkmgmt/viewSearchZOfficeCode.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchZOfficeCode(@RequestBody TbLvlCdVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbLvlCdListVo resultListVo =  new TbLvlCdListVo();
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/linkmgmt/viewSearchZOfficeCode.ajax", method = RequestMethod.POST)
	public String viewSearchZOfficeCode(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchZOfficeCodeModel(searchVo, request);
		model.addAllAttributes(builtModel);	
		searchVoClone.setUrl("/ipmgmt/linkmgmt/viewSearchZOfficeCode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewSearchZOfficeCode";
	}
	private ModelMap viewSearchZOfficeCodeModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo =  new TbLvlCdListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * 운용정보관리(링크) > 수정 팝업화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/viewUpdateIPLinkMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateIPLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request){
		TbIpLinkMstVo resultVo = linkMgmtService.selectTbIpLinkMstVo(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/linkmgmt/viewUpdateIPLinkMst.ajax", method = RequestMethod.POST)
	public String viewUpdateIPLinkMst(@RequestBody TbIpLinkMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo searchVoClone = new TbIpLinkMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateIPLinkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linkmgmt/viewUpdateIPLinkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linkmgmt/viewUpdateIPLinkMst";
	}
	private ModelMap viewUpdateIPLinkMstModel(@RequestBody TbIpLinkMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpLinkMstVo resultVo = null;
		try {
			resultVo = linkMgmtService.selectTbIpLinkMstVo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	/**
	 * 운용정보관리(링크) > 수정
	 * @param tbIpLinkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/updateLinkIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateHostIPMst(@RequestBody TbIpLinkMstVo tbIpLinkMstVo, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo resultVo = null;
		try {
			tbIpLinkMstVo.setSmodifyId(jwtUtil.getUserId(request));
			linkMgmtService.updateTbIpLinkMstVo(tbIpLinkMstVo);
			resultVo = new TbIpLinkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 운용정보관리(링크) > 삭제
	 * @param tbIpHostMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/linkmgmt/deleteLinkIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteLinkIPMst(@RequestBody TbIpLinkMstVo tbIpLinkMstVo, HttpServletRequest request, HttpServletResponse response){
		TbIpLinkMstVo resultVo = null;
		try {
			linkMgmtService.deleteIpLinkMst(tbIpLinkMstVo);
			resultVo = new TbIpLinkMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpLinkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
}
