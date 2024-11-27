package com.kt.ipms.legacy.opermgmt.tacsmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
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
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class TacsMgmtController extends CommonController {
	
	@Autowired
	private TacsMgmtService tacsMgmtService;
	

	@RequestMapping(value="/opermgmt/tacsmgmt/viewTacsConnBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewTacsConnBas(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request) {
		TbTacsConnBasVo resultVo = tacsMgmtService.selectTbTacsConnBas();
		return createResult(resultVo);
	}
	@RequestMapping(value="/opermgmt/tacsmgmt/viewTacsConnBas.do", method = RequestMethod.POST)
	public String viewTacsConnBas(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewTacsConnBasModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewTacsConnBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewTacsConnBas";
	}
	private ModelMap viewTacsConnBasModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsConnBasVo resultVo = null;
		try {
			resultVo = tacsMgmtService.selectTbTacsConnBas();
		} catch (ServiceException e) {
			resultVo = new TbTacsConnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsConnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	

	/*TACS 연동정보 수정*/
	@RequestMapping(value = "/opermgmt/tacsmgmt/updateTacsConnBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTacsConnBas(@RequestBody TbTacsConnBasVo tbTacsConnBasVo, 
			HttpServletRequest request, HttpServletResponse response){

		TbTacsConnBasVo resultVo = null;
		try {
			tacsMgmtService.updateTbTacsConnBas(tbTacsConnBasVo);
			
			resultVo = new TbTacsConnBasVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsConnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsConnBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsFcltMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListTacsFcltMst(@RequestBody TbTacsFcltMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbTacsFcltMstListVo resultListVo = tacsMgmtService.selectListTacsFcltMst(searchVo);
		return createResultList(resultListVo.getTbTacsFcltMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsFcltMst.do", method = RequestMethod.POST)
	public String viewListTacsFcltMst(@ModelAttribute("searchVo") TbTacsFcltMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
				TbTacsFcltMstVo searchVoClone = new TbTacsFcltMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTacsFcltMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewListTacsFcltMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewListTacsFcltMst";
	}
	private ModelMap viewListTacsFcltMstModel(@ModelAttribute("searchVo") TbTacsFcltMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsFcltMstListVo resultListVo = null;
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
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			
			setPagination(searchVo);
			resultListVo = tacsMgmtService.selectListTacsFcltMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbTacsFcltMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbTacsFcltMstListVo();
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
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewInsertTacsFcltMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertTacsFcltMst(ModelMap model, HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("suseYn", "Y");
		List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.TACS_FLCT_TYPE_CD, paramMap);
		ModelMap builtModel = new ModelMap();
		builtModel.addAttribute("sfcltTypes", sfcltTypes);
		return builtModel;
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewInsertTacsFcltMst.ajax", method = RequestMethod.POST)
	public String viewInsertTacsFcltMst(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewInsertTacsFcltMstModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewInsertTacsFcltMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewInsertTacsFcltMst";
	}
	private ModelMap viewInsertTacsFcltMstModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsFcltMstVo resultVo = null;
		try {
			/** 계위 정보 설정 **/
			
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = new TbLvlBasListVo();
			TbLvlBasListVo nodeListVo = new TbLvlBasListVo();
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.TACS_FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);
			
			resultVo = new TbTacsFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/insertTacsFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertTacsFcltMst(@RequestBody TbTacsFcltMstVo insertVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setScreateId(userId);
			insertVo.setSmodifyId(userId);
			tacsMgmtService.insertTacsFcltMst(insertVo);
			resultVo = new TbTacsFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/deleteTacsFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteTacsFcltMst(@RequestBody TbTacsFcltMstVo deleteVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltMstVo resultVo = null;
		try {
			
			tacsMgmtService.deleteTacsFcltMst(deleteVo);
			resultVo = new TbTacsFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewUpdateTacsFcltMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateTacsFcltMst(@RequestBody TbTacsFcltMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbTacsFcltMstVo resultVo = tacsMgmtService.selectTacsFcltMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewUpdateTacsFcltMst.ajax", method = RequestMethod.POST)
	public String viewUpdateTacsFcltMst(@RequestBody TbTacsFcltMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltMstVo searchVoClone = new TbTacsFcltMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateTacsFcltMstModel(searchVo, request);
		model.addAllAttributes(builtModel);
		
		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewUpdateTacsFcltMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewUpdateTacsFcltMst";
	}
	private ModelMap viewUpdateTacsFcltMstModel(@RequestBody TbTacsFcltMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsFcltMstVo resultVo = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.TACS_FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);
			if (searchVo.getNtacsFcltMstSeq() != null) {
				resultVo = tacsMgmtService.selectTacsFcltMst(searchVo);
			} else {
				resultVo = new TbTacsFcltMstVo();
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/updateTacsFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTacsFcltMst(@RequestBody TbTacsFcltMstVo updateVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			updateVo.setSmodifyId(userId);
			tacsMgmtService.updateTacsFcltMst(updateVo);
			resultVo = new TbTacsFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	@RequestMapping(value = "/opermgmt/tacsmgmt/selectListCommonCode.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListCommonCode() {
		List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.TACS_FLCT_TYPE_CD, null);
		return createResultList(sfcltTypes, sfcltTypes.size());
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsFcltCmdMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListTacsFcltCmdMst(@RequestBody TbTacsFcltCmdMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbTacsFcltCmdMstListVo resultListVo = tacsMgmtService.selectListTacsFcltCmdMst(searchVo);
		return createResultList(resultListVo.getTbTacsFcltCmdMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsFcltCmdMst.do", method = RequestMethod.POST)
	public String viewListTacsFcltCmdMst(@ModelAttribute("searchVo") TbTacsFcltCmdMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltCmdMstVo searchVoClone = new TbTacsFcltCmdMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTacsFcltCmdMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewListTacsFcltCmdMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewListTacsFcltCmdMst";
	}
	private ModelMap viewListTacsFcltCmdMstModel(@ModelAttribute("searchVo") TbTacsFcltCmdMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsFcltCmdMstListVo resultListVo = null;
		try {
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.TACS_FLCT_TYPE_CD, null);
			model.addAttribute("sfcltTypes", sfcltTypes);
			setPagination(searchVo);
			resultListVo = tacsMgmtService.selectListTacsFcltCmdMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbTacsFcltCmdMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbTacsFcltCmdMstListVo();
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
	
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewInsertTacsFcltCmdMst.ajax", method = RequestMethod.POST)
	public String viewInsertTacsFcltMstCmdMst(HttpServletRequest request, HttpServletResponse response) {
		return "opermgmt/tacsmgmt/viewInsertTacsFcltCmdMst";
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/insertTacsFcltCmdMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertTacsFcltMstCmdMst(@RequestBody TbTacsFcltCmdMstVo insertVo, 
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltCmdMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setSmodifyId(userId);
			insertVo.setScreateId(userId);
			tacsMgmtService.insertTacsFcltCmdMst(insertVo);
			resultVo = new TbTacsFcltCmdMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewUpdateTacsFcltCmdMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateTacsFcltCmdMst(@RequestBody TbTacsFcltCmdMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbTacsFcltCmdMstVo resultVo = tacsMgmtService.selectTacsFcltCmdMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewUpdateTacsFcltCmdMst.ajax", method = RequestMethod.POST)
	public String viewUpdateTacsFcltCmdMst(@RequestBody TbTacsFcltCmdMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltCmdMstVo searchVoClone = new TbTacsFcltCmdMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateTacsFcltCmdMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewUpdateTacsFcltCmdMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewUpdateTacsFcltCmdMst";
	}
	private ModelMap viewUpdateTacsFcltCmdMstModel(@RequestBody TbTacsFcltCmdMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsFcltCmdMstVo resultVo = null;
		try {
			resultVo = tacsMgmtService.selectTacsFcltCmdMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/updateTacsFcltCmdMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTacsFcltCmdMst(@RequestBody TbTacsFcltCmdMstVo updateVo, 
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsFcltCmdMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			updateVo.setSmodifyId(userId);
			tacsMgmtService.updateTacsFcltCmdMst(updateVo);
			resultVo = new TbTacsFcltCmdMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTacsFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewCheckTacsIpBlock.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewCheckTacsIpBlock(@RequestBody TbIpAssignMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = viewCheckTacsIpBlockModel(searchVo, request);
		TacsResponseListVo resultListVo = (TacsResponseListVo)model.get("resultListVo");
		return createResultList(resultListVo.getTacsResponseVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewCheckTacsIpBlock.ajax", method = RequestMethod.POST)
	public String viewCheckTacsIpBlock(@RequestBody TbIpAssignMstVo searchVo, ModelMap model, 
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewCheckTacsIpBlockModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewCheckTacsIpBlock.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewCheckTacsIpBlock";
	}
	private ModelMap viewCheckTacsIpBlockModel(@RequestBody TbIpAssignMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TacsResponseListVo resultListVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			TbIpAssignMstVo resultIpVo = tacsMgmtService.selectIpAssignMst(searchVo);
			resultIpVo.setTypeFlag(searchVo.getTypeFlag());
			model.addAttribute("resultIpVo", resultIpVo);
			resultListVo = tacsMgmtService.getCheckTacsRouteList(resultIpVo, userId);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			PrintLogUtil.printLogInfo("e =="+e.toString());
			resultListVo = new TacsResponseListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			PrintLogUtil.printLogInfo("e2 =="+e.toString());
			resultListVo = new TacsResponseListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/selectSresultMsg.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectSresultMsg() {
		List<?> sresultMsg = tacsMgmtService.selectSresultMsg();
		return createResultList(sresultMsg, sresultMsg.size());
	}
	
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsConnHist.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListTacsConnHist(@RequestBody TbTacsConnHistVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbTacsConnHistListVo resultListVo = tacsMgmtService.selectListTacsConnHist(searchVo);
		return createResultList(resultListVo.getTbTacsConnHistVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/tacsmgmt/viewListTacsConnHist.do", method = RequestMethod.POST)
	public String viewListTacsConnHist(@ModelAttribute("searchVo") TbTacsConnHistVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbTacsConnHistVo searchVoClone = new TbTacsConnHistVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTacsConnHistModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/tacsmgmt/viewListTacsConnHist.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/tacsmgmt/viewListTacsConnHist";
	}
	private ModelMap viewListTacsConnHistModel(@ModelAttribute("searchVo") TbTacsConnHistVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTacsConnHistListVo resultListVo = null;
		List<?> sresultMsg = null;
		try {	
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			
			String sPromptNm = searchVo.getSfcltPromptNm();
			if(sPromptNm != null && !sPromptNm.equals("")){
				searchVo.setSfcltPromptNm(URLDecoder.decode(sPromptNm, "UTF-8"));
			}
			setPagination(searchVo);
			sresultMsg = tacsMgmtService.selectSresultMsg();
			resultListVo = tacsMgmtService.selectListTacsConnHist(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbTacsConnHistListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbTacsConnHistListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("sresultMsg", sresultMsg);
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	

}
