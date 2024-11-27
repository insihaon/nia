package com.kt.ipms.legacy.opermgmt.menumgmt.web;

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
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.menumgmt.service.MenuMgmtService;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class MenuMgmtController extends CommonController{
	
	@Autowired
	private MenuMgmtService menuMgmtService;
	
		
	/**
	 * 화면관리 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewListScrnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListScrnBas(@RequestBody TbScrnBasVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbScrnBasListVo resultListVo = menuMgmtService.selectListPageScrnBas(searchVo);
		return createResultList(resultListVo.getTbScrnBasVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewListScrnBas.do", method = RequestMethod.POST)
	public String selectListScrnBas(@ModelAttribute("searchVo") TbScrnBasVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListScrnBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewListScrnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewListScrnBas";
	}
	private ModelMap selectListScrnBasModel(@ModelAttribute("searchVo") TbScrnBasVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbScrnBasListVo resultListVo = null;
		try {
			List<CommonCodeVo> sscrnTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SCRN_TYPE_CD, null);
			model.addAttribute("sscrnTypeCds", sscrnTypeCds);
			
			setPagination(searchVo);
			resultListVo = menuMgmtService.selectListPageScrnBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbScrnBasListVo();
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
	 * 화면관리 상세정보 화면
	 * @param tbScrnBasVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewDetailScrnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectScrnBas(@RequestBody TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request)  {
		TbScrnBasVo resultVo = menuMgmtService.selectScrnBas(tbScrnBasVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewDetailScrnBas.ajax", method = RequestMethod.POST)
	public String selectScrnBas(@RequestBody TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(tbScrnBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectScrnBasModel(tbScrnBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewDetailScrnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewDetailScrnBas";
	}
	private ModelMap selectScrnBasModel(@RequestBody TbScrnBasVo tbScrnBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbScrnBasVo resultVo = null;
		try{
			resultVo = menuMgmtService.selectScrnBas(tbScrnBasVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 화면관리 등록 화면
	 * @param tbScrnBasVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertScrnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertScrnBas(@ModelAttribute("tbScrnBasVo") TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request)  {
		TbScrnBasVo resultVo = new TbScrnBasVo();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newSscrnId", menuMgmtService.selectNewsscrnId());
		resultVo.setParamMap(map);
		return createResult(resultVo);
	}
	
	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertScrnBas.ajax", method = RequestMethod.POST)
	public String viewInsertScrnBas(@ModelAttribute("tbScrnBasVo") TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(tbScrnBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertScrnBasModel(tbScrnBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewInsertScrnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewInsertScrnBas";
	}
	
	private ModelMap viewInsertScrnBasModel(@ModelAttribute("tbScrnBasVo") TbScrnBasVo tbScrnBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbScrnBasVo resultVo = new TbScrnBasVo();
		String newSscrnId = null;
		try{
			newSscrnId = menuMgmtService.selectNewsscrnId();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("newSscrnId", newSscrnId);
		return model;
	}
	
	/**
	 * 화면관리 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/insertScrnBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertScrnBas(@RequestBody TbScrnBasVo insertVo, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo resultVo = new TbScrnBasVo();
		try{
			resultVo = menuMgmtService.insertScrnBas(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * 화면관리 수정 화면
	 * @param tbScrnBasVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewUpdateScrnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateScrnBas(@RequestBody TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request)  {
		TbScrnBasVo resultVo = menuMgmtService.selectScrnBas(tbScrnBasVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewUpdateScrnBas.ajax", method = RequestMethod.POST)
	public String viewUpdateScrnBas(@RequestBody TbScrnBasVo tbScrnBasVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(tbScrnBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewUpdateScrnBasModel(tbScrnBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewUpdateScrnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewUpdateScrnBas";
	}
	private ModelMap viewUpdateScrnBasModel(@RequestBody TbScrnBasVo tbScrnBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbScrnBasVo resultVo = null;
		try{
			resultVo = menuMgmtService.selectScrnBas(tbScrnBasVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 화면관리 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/updateScrnBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateScrnBas(@RequestBody TbScrnBasVo updateVo, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo resultVo = new TbScrnBasVo();
		try{
			resultVo = menuMgmtService.updateScrnBas(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbScrnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * 화면관리 updateScrnUseYn
	 * @param tbScrnBasListVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/updateScrnUseYn.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateSsrnUseYn(@RequestBody TbScrnBasListVo tbScrnBasListVo, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasListVo resultListVo = new TbScrnBasListVo();
		try{
			resultListVo = menuMgmtService.updateSsrnUseYn(tbScrnBasListVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	
	/**
	 * 메뉴관리 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewListMenuBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListMenuBas(@RequestBody TbMenuBasVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbMenuBasListVo resultListVo = menuMgmtService.selectMenuBas(searchVo);
		return createResultList(resultListVo.getTbMenuBasVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewListMenuBas.do", method = RequestMethod.POST)
	public String viewListMenuBas(@ModelAttribute("searchVo") TbMenuBasVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListMenuBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewListMenuBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewListMenuBas";
	}
	private ModelMap viewListMenuBasModel(@ModelAttribute("searchVo") TbMenuBasVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMenuBasListVo resultListVo = null;
		try {
		//	List<CommonCodeVo> sscrnTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SCRN_TYPE_CD, null);
			//model.addAttribute("sscrnTypeCds", sscrnTypeCds);
			
			//setPagination(searchVo);
			resultListVo = menuMgmtService.selectMenuBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbMenuBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbMenuBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		//PaginationInfo paginationInfo = getPaginationInfo();
		//paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		//model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertGroupName.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap insertViewGroupName(@RequestBody TbMenuBasVo tbMenuBasVo, ModelMap model, HttpServletRequest request)  {
		TbMenuBasVo resultVo =new TbMenuBasVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertGroupName.ajax", method = RequestMethod.POST)
	public String insertViewGroupName(@RequestBody TbMenuBasVo tbMenuBasVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(tbMenuBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = insertViewGroupNameModel(tbMenuBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewInsertGroupName.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewInsertGroupName";
	}
	private ModelMap insertViewGroupNameModel(@RequestBody TbMenuBasVo tbMenuBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMenuBasVo resultVo = null;
		try{
			 resultVo =new TbMenuBasVo();
			 resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertMenuName.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap insertViewMenuName(@RequestBody TbMenuBasVo tbMenuBasVo, ModelMap model, HttpServletRequest request)  {
		TbMenuBasVo resultVo = new TbMenuBasVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewInsertMenuName.ajax", method = RequestMethod.POST)
	public String insertViewMenuName(@RequestBody TbMenuBasVo tbMenuBasVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(tbMenuBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = insertViewMenuNameModel(tbMenuBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewInsertMenuName.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewInsertMenuName";
	}
	
	private ModelMap insertViewMenuNameModel(@RequestBody TbMenuBasVo tbMenuBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMenuBasVo resultVo = null;
		try{
			 resultVo =new TbMenuBasVo();
			 resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 메뉴권한관리 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/viewListMenuAuth.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListMenuAuth(@RequestBody TbMenuAuthVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbMenuAuthListVo resultListVo = menuMgmtService.selectListTbMenuAuth(searchVo);
		return createResultList(resultListVo.getTbMenuAuthVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewListMenuAuth.do", method = RequestMethod.POST)
	public String selectListMenuAuth(@ModelAttribute("searchVo") TbMenuAuthVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListMenuAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewListMenuAuth.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewListMenuAuth";
	}
	private ModelMap selectListMenuAuthModel(@ModelAttribute("searchVo") TbMenuAuthVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMenuAuthListVo resultListVo = null;
		try {
		
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			
			model.addAttribute("userGradeCds", userGradeCds);
			
			if(!StringUtils.hasText(searchVo.getSuserGradeCd())){
				searchVo.setSuserGradeCd(CommonCodeUtil.USER_GRADE_A);
			}
			setPagination(searchVo);
			resultListVo = menuMgmtService.selectListTbMenuAuth(searchVo);
		 	resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		
		} catch (ServiceException e) {
			resultListVo = new TbMenuAuthListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbMenuAuthListVo();
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
	 * 메뉴권한관리 UpdateMenuAuthUseYn
	 * @param menuAuthList
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/menumgmt/updateMenuAuthUseYn.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateMenuAuthUseYn(@RequestBody TbMenuAuthListVo menuAuthList, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		
		TbMenuAuthListVo resultListVo = new TbMenuAuthListVo();
		try {
			menuMgmtService.updateMenuAuthUseYn(menuAuthList);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbMenuAuthListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbMenuAuthListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
	
		return resultListVo;
	}
	
	@RequestMapping(value = "/opermgmt/menumgmt/selectListMenuBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public List<Map<String, Object>> selectListMenuBas(@ModelAttribute("searchVo") TbMenuBasVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String,Object>>();
		try {
			TbMenuBasListVo resultListVo = menuMgmtService.selectMenuBas(searchVo);
			if (!StringUtils.hasText(searchVo.getSmenuId())) {
				List<TbMenuBasVo> firstMenuList = resultListVo.getFirMenuBasVos(); 
				for (int i=0; i < firstMenuList.size(); i++) {
					TbMenuBasVo itemVo = firstMenuList.get(i);
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("key", itemVo.getSmenuId());
					itemMap.put("title", itemVo.getSmenuNm());
					itemMap.put("isFolder", true);
					itemMap.put("upperFolder", "/");
					itemMap.put("isLazy", true);
					itemMap.put("nmenuLvlSeq", itemVo.getNmenuLvlSeq());
					resultListMap.add(itemMap);
				}	
			} else {
				if (searchVo.getNmenuLvlSeq() == 1) {
					List<TbMenuBasVo> secondMenuList = resultListVo.getSecMenuBasVos();
					for (int i = 0; i < secondMenuList.size(); i++) {
						TbMenuBasVo itemVo = secondMenuList.get(i);
						if (searchVo.getSmenuId().equals(itemVo.getsUpMenuId())) {
							Map<String, Object> itemMap = new HashMap<String, Object>();
							itemMap.put("key", itemVo.getSmenuId());
							itemMap.put("title", itemVo.getSmenuNm());
							if (itemVo.getSmenuHierTypeCd().equals("UH0001")) {
								itemMap.put("isFolder", true);
								itemMap.put("isLazy", true);
							} else {
								itemMap.put("isFolder", false);
								itemMap.put("isLazy", false);
							}
							itemMap.put("upperFolder", searchVo.getSmenuId());
							itemMap.put("nmenuLvlSeq", itemVo.getNmenuLvlSeq());
							resultListMap.add(itemMap);
						}
					}
				} else {
					List<TbMenuBasVo> thrMenuList = resultListVo.getThrmenuBasVos();
					for (int i = 0; i < thrMenuList.size(); i++) {
						TbMenuBasVo itemVo = thrMenuList.get(i);
						if (searchVo.getSmenuId().equals(itemVo.getsUpMenuId())) {
							Map<String, Object> itemMap = new HashMap<String, Object>();
							itemMap.put("key", itemVo.getSmenuId());
							itemMap.put("title", itemVo.getSmenuNm());
							if (itemVo.getSmenuHierTypeCd().equals("UH0001")) {
								itemMap.put("isFolder", true);
								itemMap.put("isLazy", true);
							} else {
								itemMap.put("isFolder", false);
								itemMap.put("isLazy", false);
							}
							itemMap.put("upperFolder", searchVo.getSmenuId());
							itemMap.put("nmenuLvlSeq", itemVo.getNmenuLvlSeq());
							resultListMap.add(itemMap);
						}
					}
				}
			}
			
		} catch (ServiceException e) {
			TbMenuAuthListVo resultListVo = new TbMenuAuthListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbMenuAuthListVo resultListVo = new TbMenuAuthListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListMap;
	}
	
	
	@RequestMapping(value = "/opermgmt/menumgmt/selectDetailMenuBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectDetailMenuBas(@RequestBody TbMenuBasVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbMenuBasVo  detailVo = null;
		try {
			
			detailVo =  menuMgmtService.selectTbMenuBasVo(searchVo);
			
		} catch (ServiceException e) {
			TbMenuBasVo resultListVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbMenuBasVo resultListVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return detailVo;
	}
	
	@RequestMapping(value = "/opermgmt/menumgmt/updateTbMenuBasVo.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTbMenuBasVo(@RequestBody TbMenuBasVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbMenuBasVo resultVo = null;
		try {
			
			resultVo =  menuMgmtService.updateTbMenuBasVo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			TbMenuBasVo resultListVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbMenuBasVo resultListVo = new TbMenuBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	

	@RequestMapping(value = "/opermgmt/menumgmt/viewSearchTbScrnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchTbScrnBas(@RequestBody TbScrnBasVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbScrnBasListVo resultListVo = new TbScrnBasListVo();
		return createResultList(resultListVo.getTbScrnBasVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/menumgmt/viewSearchTbScrnBas.ajax", method = RequestMethod.POST)
	public String viewSearchTbScrnBas(@ModelAttribute("searchVo") TbScrnBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasVo searchVoClone = new TbScrnBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchTbScrnBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/menumgmt/viewSearchTbScrnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/menumgmt/viewSearchScrnBas";
	}
	private ModelMap viewSearchTbScrnBasModel(@ModelAttribute("searchVo") TbScrnBasVo searchVo, 
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbScrnBasListVo resultListVo = null;
		try {
			resultListVo =  new TbScrnBasListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/menumgmt/selectSearchScrnBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectSearchOrgBas(@RequestBody TbScrnBasVo searchVo, 
			HttpServletRequest request, HttpServletResponse response)  {
		TbScrnBasListVo resultListVo = null;
		try {
			resultListVo = menuMgmtService.selectListScrnBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbScrnBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		//model.addAttribute("resultListVo", resultListVo);		
		return resultListVo;
	}

}
