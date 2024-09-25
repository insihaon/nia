package com.kt.ipms.legacy.opermgmt.ifomsmgmt.web;

import java.lang.reflect.InvocationTargetException;
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
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.ifomsmgmt.service.IfomsMgmtService;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class IfomsMgmtController extends CommonController {

	@Autowired
	private IfomsMgmtService ifomsMgmtService;
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewListIFomsMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListIFomsMst(@RequestBody TbConfigInterfaceMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbConfigInterfaceMstListVo resultListVo = ifomsMgmtService.selectListPageConfigInterfaceMst(searchVo);
		return createResultList(resultListVo.getTbConfigInterfaceMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewListIFomsMst.do", method = RequestMethod.POST)
	public String viewListIFomsMst(@ModelAttribute("searchVo") TbConfigInterfaceMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo searchVoClone = new TbConfigInterfaceMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListIFomsMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/ifomsmgmt/viewListIFomsMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/ifomsmgmt/viewListIFomsMst";
	}
	private ModelMap viewListIFomsMstModel(@ModelAttribute("searchVo") TbConfigInterfaceMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbConfigInterfaceMstListVo resultListVo = null;
		try {
			List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_SHOST_IP);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			searchVo.setNflagLevel(1000);
			
			setPagination(searchVo);
			resultListVo = ifomsMgmtService.selectListPageConfigInterfaceMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbConfigInterfaceMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbConfigInterfaceMstListVo();
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
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewInsertIFomsMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertIFomsMst(ModelMap model, 
			HttpServletRequest request) {
		TbConfigInterfaceMstVo resultVo = new TbConfigInterfaceMstVo();
		return createResult(resultVo);
	}
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewInsertIFomsMst.ajax", method = RequestMethod.POST)
	public String viewInsertIFomsMst(@RequestBody CommonVo searchVo, ModelMap model, 
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo searchVoClone = new TbConfigInterfaceMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertIFomsMstModel();
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/ifomsmgmt/viewInsertIFomsMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/ifomsmgmt/viewInsertIFomsMst";
	}
	private ModelMap viewInsertIFomsMstModel() {
		ModelMap model = new ModelMap();
		TbConfigInterfaceMstVo resultVo = null;
		try {
			List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			resultVo = new TbConfigInterfaceMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/insertIFomsMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbConfigInterfaceMstVo insertIFomsMst(@RequestBody TbConfigInterfaceMstVo tbConfigInterfaceMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo resultVo = null;
		try {
			ifomsMgmtService.insertIFomsMst(tbConfigInterfaceMstVo);
			resultVo = new TbConfigInterfaceMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewUpdateIFomsMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateIFomsMst(@RequestBody TbConfigInterfaceMstVo searchVo, ModelMap model, 
			HttpServletRequest request) {
		TbConfigInterfaceMstVo resultVo = ifomsMgmtService.selectConfigInterfaceMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/opermgmt/ifomsmgmt/viewUpdateIFomsMst.ajax", method = RequestMethod.POST)
	public String viewUpdateIFomsMst(@RequestBody TbConfigInterfaceMstVo searchVo, ModelMap model, 
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo searchVoClone = new TbConfigInterfaceMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewUpdateIFomsMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/ifomsmgmt/viewUpdateIFomsMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/ifomsmgmt/viewUpdateIFomsMst";
	}
	private ModelMap viewUpdateIFomsMstModel(@RequestBody TbConfigInterfaceMstVo searchVo, 
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbConfigInterfaceMstVo resultVo = null;
		try {
			List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			resultVo = ifomsMgmtService.selectConfigInterfaceMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/updateIFomsMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbConfigInterfaceMstVo updateIFomsMst(@RequestBody TbConfigInterfaceMstVo tbConfigInterfaceMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo resultVo = null;
		try {
			ifomsMgmtService.updateIFomsMst(tbConfigInterfaceMstVo);
			resultVo = new TbConfigInterfaceMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/opermgmt/ifomsmgmt/deleteIFomsMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbConfigInterfaceMstVo deleteIFomsMst(@RequestBody TbConfigInterfaceMstVo tbConfigInterfaceMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo resultVo = null;
		try {
			ifomsMgmtService.deleteIFomsMst(tbConfigInterfaceMstVo);
			resultVo = new TbConfigInterfaceMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigInterfaceMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
}
