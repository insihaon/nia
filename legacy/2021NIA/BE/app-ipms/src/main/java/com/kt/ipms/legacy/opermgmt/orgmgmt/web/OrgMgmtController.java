package com.kt.ipms.legacy.opermgmt.orgmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
import com.kt.ipms.legacy.cmn.util.JwtUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.service.OrgMgmtService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbSvcLineTypeCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbSvcLineTypeCdVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class OrgMgmtController extends CommonController {

	@Autowired
	OrgMgmtService orgMgmtService;

	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());

	/**
	 * 계위코드관리 목록 리스트
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewListTbLvlCdVo.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListTbLvlCdVo(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbLvlCdListVo resultListVo = orgMgmtService.selectListTbLvlCdVo(searchVo);
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewListTbLvlCdVo.do", method = RequestMethod.POST)
	public String selectListTbLvlCdVo(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListTbLvlCdVoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewListTbLvlCdVo.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewListTbLvlCd";
	}

	private ModelMap selectListTbLvlCdVoModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			List<CommonCodeVo> externalLinkTypes = commonCodeService
					.selectListCommonCode(CommonCodeUtil.EXTERNAL_LINK_USE_TYPE_CD, null);
			model.addAttribute("externalLinkTypes", externalLinkTypes);
			setPagination(searchVo);
			resultListVo = orgMgmtService.selectListTbLvlCdVo(searchVo);
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
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}

	/**
	 * 계위코드관리 수정 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewUpdateTbLvlCdVo.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUPdateTbLvlCdVo(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlCdVo resultVo = orgMgmtService.selectTbLvlCdVo(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewUpdateTbLvlCdVo.ajax", method = RequestMethod.POST)
	public String viewUPdateTbLvlCdVo(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUPdateTbLvlCdVoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewUpdateTbLvlCdVo.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewUpdateTbLvlCd";
	}

	private ModelMap viewUPdateTbLvlCdVoModel(@RequestBody TbLvlCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdVo tbLvlCdVo = null;
		try {
			List<CommonCodeVo> externalLinkTypes = commonCodeService
					.selectListCommonCode(CommonCodeUtil.EXTERNAL_LINK_USE_TYPE_CD, null);
			model.addAttribute("externalLinkTypes", externalLinkTypes);
			tbLvlCdVo = orgMgmtService.selectTbLvlCdVo(searchVo);
			tbLvlCdVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbLvlCdVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbLvlCdVo.setCommonMsg(msgDesc);
			// tbLvlCdVo.setTotalCount(0);
		}
		model.addAttribute("tbLvlCdVo", tbLvlCdVo);
		return model;
	}

	/**
	 * 계위코드관리 등록 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertTbLvlCdVo.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertTbLvlCdVo(@RequestBody TbLvlCdVo requestVo, ModelMap model,
			HttpServletRequest request) {
		List<CommonCodeVo> resultList = commonCodeService.selectListCommonCode(CommonCodeUtil.EXTERNAL_LINK_USE_TYPE_CD,
				null);
		return createResultList(resultList, resultList.size());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertTbLvlCdVo.ajax", method = RequestMethod.POST)
	public String viewInsertTbLvlCdVo(@RequestBody TbLvlCdVo requestVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(requestVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertTbLvlCdVoModel(requestVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewInsertTbLvlCdVo.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewInsertTbLvlCd";
	}

	private ModelMap viewInsertTbLvlCdVoModel(@RequestBody TbLvlCdVo requestVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try {
			List<CommonCodeVo> externalLinkTypes = commonCodeService
					.selectListCommonCode(CommonCodeUtil.EXTERNAL_LINK_USE_TYPE_CD, null);
			model.addAttribute("externalLinkTypes", externalLinkTypes);
			// tbLvlCdVo = orgMgmtService.selectTbLvlCdVo(searchVo);
			// tbLvlCdVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbLvlCdVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbLvlCdVo.setCommonMsg(msgDesc);
		}
		// model.addAttribute("tbLvlCdVo", tbLvlCdVo);
		// PaginationInfo paginationInfo = getPaginationInfo();
		// paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		// model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}

	/**
	 * 계위코드관리 수정
	 * 
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/updateTbLvlCdVo.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlCdVo updateTbCmnMsgCd(@RequestBody TbLvlCdVo updateVo, HttpServletRequest request,
			HttpServletResponse reponse) {
		TbLvlCdVo resultVo = new TbLvlCdVo();
		try {
			orgMgmtService.updateTbLvlCdVo(updateVo);
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

	/**
	 * 계위코드관리 등록
	 * 
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/insertTbLvlCdVo.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlCdVo insertTbCmnMsgCd(@RequestBody TbLvlCdVo insertVo, HttpServletRequest request,
			HttpServletResponse reponse) {
		TbLvlCdVo resultVo = new TbLvlCdVo();
		try {
			orgMgmtService.insertTbLvlCdVo(insertVo);
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

	@RequestMapping(value = "/opermgmt/orgmgmt/selectSearchLvlCd.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlCdListVo selectSearchLvlCd(@RequestBody TbLvlCdVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo = orgMgmtService.searchTbLvlCd(searchVo);
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
		// model.addAttribute("resultListVo", resultListVo);
		return resultListVo;
	}

	/***
	 * 운영조직 검색 팝업 화면 실행
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchTbLvlCd.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewSearchTbLvlCd(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlCdListVo resultListVo = new TbLvlCdListVo();
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchTbLvlCd.ajax", method = RequestMethod.POST)
	public String viewSearchTbLvlCd(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchTbLvlCdModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewSearchTbLvlCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewSearchLvlCd";
	}

	private ModelMap viewSearchTbLvlCdModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo = new TbLvlCdListVo();
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
	 * 서비스망관리 목록 리스트
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewListSvcLineType.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListSvcLineType(@RequestBody TbSvcLineTypeCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbSvcLineTypeCdListVo resultListVo = orgMgmtService.selectListSvcLineType(searchVo);
		return createResultList(resultListVo.getTbSvcLineTypeCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewListSvcLineType.do", method = RequestMethod.POST)
	public String selectListSvcLineType(@ModelAttribute("searchVo") TbSvcLineTypeCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbSvcLineTypeCdVo searchVoClone = new TbSvcLineTypeCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListSvcLineTypeModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewListSvcLineType.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewListSvcLineType";
	}

	private ModelMap selectListSvcLineTypeModel(@ModelAttribute("searchVo") TbSvcLineTypeCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbSvcLineTypeCdListVo resultListVo = null;
		try {
			List<CommonCodeVo> suseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.USE_TYPE_CD, null);
			model.addAttribute("suseTypeCds", suseTypeCd);
			setPagination(searchVo);
			resultListVo = orgMgmtService.selectListSvcLineType(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbSvcLineTypeCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbSvcLineTypeCdListVo();
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
	 * 서비스망관리 등록화면
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/viewInsertSvcLineType.ajax", method = RequestMethod.POST)
	public String viewInsertSvcLineType(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		return "opermgmt/orgmgmt/viewInsertSvcLineTypePop";
	}

	/**
	 * 서비스망관리 목록
	 * 
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/insertSvcLineType.json", method = RequestMethod.POST)
	public TbSvcLineTypeCdVo insertSvcLineType(@RequestBody TbSvcLineTypeCdVo insertVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbSvcLineTypeCdVo resultVo = new TbSvcLineTypeCdVo();
		try {
			orgMgmtService.insertSvcLineType(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbSvcLineTypeCdVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbSvcLineTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/**
	 * 서비스망관리 수정 화면
	 * 
	 * @param tbSvcLineTypeCdVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/viewUpdateSvcLineType.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateSvcLineType(@RequestBody TbSvcLineTypeCdVo tbSvcLineTypeCdVo, ModelMap model,
			HttpServletRequest request) {
		TbSvcLineTypeCdVo resultVo = orgMgmtService.selectLineType(tbSvcLineTypeCdVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "opermgmt/orgmgmt/viewUpdateSvcLineType.ajax", method = RequestMethod.POST)
	public String viewUpdateSvcLineType(@RequestBody TbSvcLineTypeCdVo tbSvcLineTypeCdVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbSvcLineTypeCdVo searchVoClone = new TbSvcLineTypeCdVo();
		try {
			CloneUtil.copyObjectInformation(tbSvcLineTypeCdVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateSvcLineTypeModel(tbSvcLineTypeCdVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewUpdateSvcLineType.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewUpdateSvcLineTypePop";
	}

	private ModelMap viewUpdateSvcLineTypeModel(@RequestBody TbSvcLineTypeCdVo tbSvcLineTypeCdVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbSvcLineTypeCdVo resultVo = null;
		try {
			resultVo = orgMgmtService.selectLineType(tbSvcLineTypeCdVo);
			model.addAttribute("resultVo", resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			List<CommonCodeVo> suseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.USE_TYPE_CD, null);
			model.addAttribute("suseTypeCds", suseTypeCd);
		} catch (ServiceException e) {
			resultVo = new TbSvcLineTypeCdVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbSvcLineTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}

	/**
	 * 서비스망관리 수정
	 * 
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/updateSvcLineType.json", method = RequestMethod.POST)
	@ResponseBody
	public TbSvcLineTypeCdVo updateBoard(@RequestBody TbSvcLineTypeCdVo updateVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbSvcLineTypeCdVo resultVo = new TbSvcLineTypeCdVo();
		try {
			orgMgmtService.updateSvcLineType(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbSvcLineTypeCdVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbSvcLineTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;
	}

	/**
	 * 운용조직 기본 관리 리스트 목록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewListOrgBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListOrgBas(@RequestBody TbOrgBasVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbOrgBasListVo resultListVo = orgMgmtService.selectListOrgBas(searchVo);
		return createResultList(resultListVo.getTbOrgBasVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewListOrgBas.do", method = RequestMethod.POST)
	public String selectListOrgBas(@ModelAttribute("searchVo") TbOrgBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbOrgBasVo searchVoClone = new TbOrgBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListOrgBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewListOrgBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewListOrgBas";
	}

	private ModelMap selectListOrgBasModel(@ModelAttribute("searchVo") TbOrgBasVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbOrgBasListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = orgMgmtService.selectListOrgBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOrgBasListVo();
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

	/***
	 * 운용조직 검색 화면에서 조직명으로 검색 한 결과
	 * 
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/selectSearchOrgBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbOrgBasListVo selectSearchOrgBas(@RequestBody TbOrgBasVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbOrgBasListVo resultListVo = null;
		try {
			resultListVo = orgMgmtService.searchOrgBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		// model.addAttribute("resultListVo", resultListVo);
		return resultListVo;
	}

	/***
	 * 운영조직 검색 팝업 화면 실행
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchTbOrgBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap searchViewTbOrgBas(@RequestBody TbOrgBasVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbOrgBasListVo resultListVo = new TbOrgBasListVo();
		return createResultList(resultListVo.getTbOrgBasVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchTbOrgBas.ajax", method = RequestMethod.POST)
	public String searchViewTbOrgBas(@ModelAttribute("searchVo") TbOrgBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbOrgBasVo searchVoClone = new TbOrgBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = searchViewTbOrgBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewSearchTbOrgBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewSearchOrgBas";
	}

	private ModelMap searchViewTbOrgBasModel(@ModelAttribute("searchVo") TbOrgBasVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbOrgBasListVo resultListVo = null;
		try {
			resultListVo = new TbOrgBasListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}

		model.addAttribute("resultListVo", resultListVo);
		return model;
	}

	/***
	 * 운영조직관리 updateSipmsOrgYn
	 * 
	 * @param tbOrgBasListVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/updateSipmsOrgYn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbOrgBasListVo updateSipmsOrgYn(@RequestBody TbOrgBasListVo tbOrgBasListVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbOrgBasListVo resultListVo = new TbOrgBasListVo();
		try {
			resultListVo = orgMgmtService.updateSipmsOrgYn(tbOrgBasListVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbOrgBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	/**
	 * 조직계위관리 목록 리스트
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewListLvlBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListLvlBas(@RequestBody TbLvlBasVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbLvlBasListVo resultListVo = orgMgmtService.selectListLvlBas(searchVo);
		return createResultList(resultListVo.getTbLvlBasVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewListLvlBas.do", method = RequestMethod.POST)
	public String selectListLvlBas(@ModelAttribute("searchVo") TbLvlBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlBasVo searchVoClone = new TbLvlBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListLvlBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewListLvlBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewListLvlBas";
	}

	private ModelMap selectListLvlBasModel(@ModelAttribute("searchVo") TbLvlBasVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlBasListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = orgMgmtService.selectListLvlBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

			// TbLvlBasListVo tbSvcLineList = jwtUtil.getSvcLineList(request);
			// model.addAttribute("svcLineList", tbSvcLineList);

			// searchVo.setSsvcLineTypeCd("CL0001");
			// TbLvlBasListVo tbCenterList = jwtUtil.getCenterList(request, searchVo);
			// model.addAttribute("centerList", tbCenterList);

			TbLvlBasListVo tbSvcLineList = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;

			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
			} else {
				if (StringUtils.hasText(tbSvcLineList.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {

					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(tbSvcLineList.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.setSsvcLineTypeCd(tbSvcLineList.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);

					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
					}

				} else {
					centerListVo = new TbLvlBasListVo();
					// nodeListVo = new TbLvlBasListVo();
				}
			}
			model.addAttribute("svcLineList", tbSvcLineList);
			model.addAttribute("centerList", centerListVo);

		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
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

	/*-------------------------------- 권한 조직 계위 정보 조회   Start------------------------------------*/
	@RequestMapping(value = "opermgmt/orgmgmt/selectAuthSvcLineList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasListVo selectAuthSvcLineList(@RequestBody TbLvlBasVo searchVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbLvlBasListVo resultListVo = null;
		try {
			resultListVo = jwtUtil.getSvcLineList(request);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	@RequestMapping(value = "opermgmt/orgmgmt/selectAuthCenterList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasListVo selectAuthCenterList(@RequestBody TbLvlBasVo searchVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbLvlBasListVo resultListVo = null;
		try {
			resultListVo = jwtUtil.getCenterList(request, searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	@RequestMapping(value = "opermgmt/orgmgmt/selectAuthNodeList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasListVo selectAuthNodeList(@RequestBody TbLvlBasVo searchVo, HttpServletRequest request,
			HttpServletResponse response) {
		TbLvlBasListVo resultListVo = null;
		try {
			resultListVo = jwtUtil.getNodeList(request, searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	/*-------------------------------- 권한 조직 계위 정보 조회    end------------------------------------*/

	/**
	 * fm시설 국사 팝업
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertLvlRoleSub.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertLvlRoleSub(@RequestBody TbLvlRoleSubVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlRoleSubListVo resultListVo = orgMgmtService.selectListLvlRoleSub(searchVo);
		return createResultList(resultListVo.getTbLvlRoleSubVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertLvlRoleSub.ajax", method = RequestMethod.POST)
	public String viewInsertLvlRoleSub(@RequestBody TbLvlRoleSubVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlRoleSubVo searchVoClone = new TbLvlRoleSubVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertLvlRoleSubModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewInsertLvlRoleSub.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewInsertFmMgmtPop";
	}

	private ModelMap viewInsertLvlRoleSubModel(@RequestBody TbLvlRoleSubVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlRoleSubListVo resultListVo;
		try {

			/// ** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);

			resultListVo = orgMgmtService.selectListLvlRoleSub(searchVo);

			resultListVo.setBfssvcLineTypeNm(searchVo.getSsvcLineTypeNm());
			resultListVo.setBfssvcGroupNm(searchVo.getSsvcGroupNm());
			resultListVo.setBfssvchighObjNm(searchVo.getSsvchighObjNm());
			resultListVo.setBfssvcObjNm(searchVo.getSsvcObjNm());

			resultListVo.setBfssvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			resultListVo.setBfssvcObjCd(searchVo.getSsvcObjCd());

			model.addAttribute("resultListVo", resultListVo);

		} catch (ServiceException e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbLvlCdVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbLvlCdVo.setCommonMsg(msgDesc);
		}
		return model;
	}

	/**
	 * fm시설 국사 등록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/insertLvlRoleSub.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlRoleSubListVo insertLvlRoleSub(@RequestBody TbLvlRoleSubVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlRoleSubListVo resultListVo;
		try {

			orgMgmtService.insertLvlRoleSub(searchVo);

			resultListVo = orgMgmtService.selectListLvlRoleSub(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbLvlRoleSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlRoleSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;

	}

	/**
	 * fm시설 국사 삭제
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/deleteLvlRoleSub.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlRoleSubListVo deleteLvlRoleSub(@RequestBody TbLvlRoleSubVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlRoleSubListVo resultListVo;
		try {

			orgMgmtService.deleteLvlRoleSub(searchVo);

			resultListVo = orgMgmtService.selectListLvlRoleSub(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbLvlRoleSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlRoleSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;

	}

	/**
	 * son오더 노드국 관리 팝업
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/viewInsertLvlSonMgmtPop.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertLvlSon(@RequestBody TbLvlSubCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlSubCdListVo resultListVo = orgMgmtService.selectListLvlSubCd(searchVo);
		return createResultList(resultListVo.getTbLvlSubCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "opermgmt/orgmgmt/viewInsertLvlSonMgmtPop.ajax", method = RequestMethod.POST)
	public String viewInsertLvlSon(@RequestBody TbLvlSubCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlSubCdVo searchVoClone = new TbLvlSubCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertLvlSonModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewInsertLvlSonMgmtPop.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewInsertSonMgmtPop";
	}

	private ModelMap viewInsertLvlSonModel(@RequestBody TbLvlSubCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlSubCdListVo resultListVo;
		try {

			/// ** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);

			resultListVo = orgMgmtService.selectListLvlSubCd(searchVo);

			resultListVo.setBfssvcLineTypeNm(searchVo.getSsvcLineTypeNm());
			resultListVo.setBfslvlGroupNm(searchVo.getSlvlGroupNm());
			resultListVo.setBfslvlHighNm(searchVo.getSlvlHighNm());
			resultListVo.setBfslvlNm(searchVo.getSlvlNm());

			resultListVo.setBfssvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			resultListVo.setBfslvlCd(searchVo.getSlvlCd());

			model.addAttribute("resultListVo", resultListVo);

		} catch (ServiceException e) {
			TbLvlSubCdVo tbLvlCdVo = new TbLvlSubCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbLvlCdVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbLvlSubCdVo tbLvlCdVo = new TbLvlSubCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbLvlCdVo.setCommonMsg(msgDesc);
		}
		return model;
	}

	/**
	 * SON 오더 국사 중복 체크
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */

	@RequestMapping(value = "opermgmt/orgmgmt/selectSloffice.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlSubCdVo selectSloffice(@RequestBody TbLvlSubCdVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {

		TbLvlSubCdVo resultVo;

		try {

			int value = orgMgmtService.selectSloffice(searchVo);

			resultVo = new TbLvlSubCdVo();
			if (value > 0) {
				resultVo.setCommonMsg("FAIL");
			} else {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}

		} catch (ServiceException e) {
			resultVo = new TbLvlSubCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {

			resultVo = new TbLvlSubCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;

	}

	/**
	 * son오더 노드국 등록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */

	@RequestMapping(value = "opermgmt/orgmgmt/insertTbLvlSubCd.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlSubCdListVo insertTbLvlSubCd(@RequestBody TbLvlSubCdVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {

		TbLvlSubCdListVo resultListVo;

		try {

			orgMgmtService.insertTbLvlSubCd(searchVo);

			resultListVo = orgMgmtService.selectListLvlSubCd(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbLvlSubCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {

			resultListVo = new TbLvlSubCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;
	}

	/**
	 * son오더 노드국 삭제
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */

	@RequestMapping(value = "opermgmt/orgmgmt/deleteTbLvlSubCd.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlSubCdListVo deleteTbLvlSubCd(@RequestBody TbLvlSubCdVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {

		TbLvlSubCdListVo resultListVo;

		try {

			orgMgmtService.deleteTbLvlSubCd(searchVo);

			resultListVo = orgMgmtService.selectListLvlSubCd(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbLvlSubCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlSubCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;
	}

	/**
	 * 조직계위코드 이동 팝업
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewUdateLvlBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUdateLvlBas(@RequestBody TbLvlBasVo searchVo, ModelMap model,
			HttpServletRequest request) {
		ModelMap resultModel = new ModelMap();
		HashMap<String, Object> map = new HashMap<String, Object>();

		TbLvlBasListVo svcLineListVo = orgMgmtService.selectListSvcLineAll();
		// TbLvlBasListVo centerListVo = orgMgmtService.selectlistCenterAll();
		// TbLvlBasListVo nodeListVo = orgMgmtService.selectlistNodeAll();

		map.put("svcLineListVo", svcLineListVo.getTbLvlBasVos());
		// map.put("svcLineListTotalCount", svcLineListVo.getTotalCount());

		// map.put("centerListVo", centerListVo.getTbLvlBasVos());
		// map.put("centerListTotalCount", centerListVo.getTotalCount());

		// map.put("nodeListVo", nodeListVo.getTbLvlBasVos());
		// map.put("nodeListTotalCount", nodeListVo.getTotalCount());

		resultModel.addAttribute("result", map);
		return resultModel;
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewUdateLvlBas.ajax", method = RequestMethod.POST)
	public String viewUdateLvlBas(@RequestBody TbLvlBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlBasVo searchVoClone = new TbLvlBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUdateLvlBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewUdateLvlBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewUpdateLvlBasPop";
	}

	private ModelMap viewUdateLvlBasModel(@RequestBody TbLvlBasVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try {

			TbLvlBasListVo svcLineListVo = orgMgmtService.selectListSvcLineAll();
			TbLvlBasListVo centerListVo = orgMgmtService.selectlistCenterAll();
			TbLvlBasListVo nodeListVo = orgMgmtService.selectlistNodeAll();

			/// ** 코드 설정 **/
			// List<CommonCodeVo> sLvlSubvCds =
			/// commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_CD, null);

			// model.addAttribute("isvcLineListVo", svcLineListVo);
			// model.addAttribute("icenterListVo", centerListVo);
			// model.addAttribute("inodeListVo", sLvlSubvCds);

			model.addAttribute("isvcLineListVo", svcLineListVo);
			model.addAttribute("icenterListVo", centerListVo);
			model.addAttribute("inodeListVo", nodeListVo);

		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			searchVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			searchVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", searchVo);
		return model;
	}

	/**
	 * 조직계위 이동
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/updateTbLvlMove.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlMstVo updateTbLvlMove(@RequestBody TbLvlMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlMstVo resultVo;
		try {
			orgMgmtService.updateTbLvlMove(searchVo);

			resultVo = new TbLvlMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultVo = new TbLvlMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbLvlMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;
	}

	/**
	 * 주노드 수정
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/updatetblvlrolemst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlMstVo updatetblvlrolemst(@RequestBody TbLvlMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlMstVo resultVo;
		try {
			orgMgmtService.updatetblvlrolemst(searchVo);

			resultVo = new TbLvlMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultVo = new TbLvlMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbLvlMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;
	}

	/**
	 * 조직계위 등록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/insertTbLvlBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlRoleMstVo insertTbLvlBas(@RequestBody TbLvlRoleMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlRoleMstVo resultVo;
		try {
			orgMgmtService.insertTbLvlBas(searchVo);

			resultVo = new TbLvlRoleMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultVo = new TbLvlRoleMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbLvlRoleMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;
	}

	/**
	 * 조직계위코드관리 등록 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 * 
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertLvlBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertLvlBas(HttpServletRequest request) {
		ModelMap resultModel = new ModelMap();
		HashMap<String, Object> map = new HashMap<String, Object>();

		TbLvlBasListVo svcLineListVo = orgMgmtService.selectListSvcLineAll();
		map.put("svcLineListVo", svcLineListVo.getTbLvlBasVos());
		resultModel.addAttribute("result", map);

		return resultModel;
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewInsertLvlBas.ajax", method = RequestMethod.POST)
	public String viewInsertLvlBas(@RequestBody CommonVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {

		CommonVo searchVoClone = new CommonVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertLvlBasModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewInsertLvlBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewInsertLvlBasPop";
	}

	private ModelMap viewInsertLvlBasModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try {

			TbLvlBasListVo svcLineListVo = orgMgmtService.selectListSvcLineAll();

			// as-is 2015 .07 .04 팝업에서 국사코드 센터 조회로 변경
			// TbLvlBasListVo centerListVo = orgMgmtService.selectlistCenterAll() ;
			// TbLvlBasListVo nodeListVo = orgMgmtService.selectlistNodeAll() ;

			// model.addAttribute("isvcLineListVo", svcLineListVo);
			// model.addAttribute("icenterListVo", centerListVo);
			// model.addAttribute("inodeListVo", nodeListVo);

			/// ** 수용국 코드 설정 **/
			// List<CommonCodeVo> sLvlSubvCds =
			/// commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_CD, null);

			model.addAttribute("isvcLineListVo", svcLineListVo);

		} catch (ServiceException e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbLvlCdVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			TbLvlCdVo tbLvlCdVo = new TbLvlCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbLvlCdVo.setCommonMsg(msgDesc);
		}
		return model;
	}

	/**
	 * 조직계위 정보관리 중복검사
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "opermgmt/orgmgmt/insertValidTbLvlBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasVo insertValidTbLvlBas(@RequestBody TbLvlBasVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {

		int result;
		TbLvlBasVo resultVo;

		try {

			resultVo = new TbLvlBasVo();

			result = orgMgmtService.countSsvcGroupCd(searchVo);
			if (result > 0) {
				result = orgMgmtService.countNodeTbLvlBasVo(searchVo);

				if (result > 0) {
					resultVo.setCommonMsg("FAIL");
				} else {
					resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
				}
			} else {
				if (searchVo.getSsvcObjCd().equals("000000")) {
					result = orgMgmtService.countNodeTbLvlBasVo(searchVo);

					if (result > 0) {
						resultVo.setCommonMsg("FAIL");
					} else {
						resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
					}
				} else {
					resultVo.setCommonMsg("CENTERFAIL");
				}
			}

		} catch (ServiceException e) {
			resultVo = new TbLvlBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbLvlBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;

	}

	/**
	 * 조직계위 정보관리 중복검사
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */

	@RequestMapping(value = "opermgmt/orgmgmt/validTbLvlBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasVo validTbLvlBas(@RequestBody TbLvlBasVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {

		int result;
		TbLvlBasVo resultVo;

		try {

			result = orgMgmtService.validTbLvlBas(searchVo);
			resultVo = new TbLvlBasVo();

			if (result > 0) {
				resultVo.setCommonMsg("FAIL");
			} else {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}

		} catch (ServiceException e) {
			resultVo = new TbLvlBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbLvlBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;

	}

	/**
	 * 조직계위 국사조회 팝업
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchLvlCd.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewSearchLvlCd(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlCdListVo resultListVo = new TbLvlCdListVo();
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchLvlCd.ajax", method = RequestMethod.POST)
	public String viewSearchLvlCd(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchLvlCdModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewSearchLvlCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewSearchNodePop";
	}

	private ModelMap viewSearchLvlCdModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo = new TbLvlCdListVo();
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
	 * 조직계위 센터노드 조회 팝업
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchCenterNode.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewSearchCenterNode(@RequestBody TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbLvlCdListVo resultListVo = new TbLvlCdListVo();
		return createResultList(resultListVo.getTbLvlCdVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/orgmgmt/viewSearchCenterNode.ajax", method = RequestMethod.POST)
	public String viewSearchCenterNode(@ModelAttribute("searchVo") TbLvlCdVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbLvlCdVo searchVoClone = new TbLvlCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchCenterNodeModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/orgmgmt/viewSearchCenterNode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/orgmgmt/viewSearchCenterNode";
	}

	private ModelMap viewSearchCenterNodeModel(@ModelAttribute("searchVo") TbLvlCdVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbLvlCdListVo resultListVo = null;
		try {
			resultListVo = new TbLvlCdListVo();
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

}
