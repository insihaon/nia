package com.kt.ipms.legacy.opermgmt.operstdmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgLvlCdListVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgMstListVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgMstVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgTypeCdListVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class TbCmnMsgMstController extends CommonController {

	@RequestMapping(value = "/opermgmt/operstdmgmt/viewListTbCmnMsgCd.model")
	public ModelMap viewListTbCmnMsgMst(@ModelAttribute("searchVo") TbCmnMsgMstVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbCmnMsgMstListVo resultListVo = tbCmnMstService.selectListTbCmnMsgCdVo(searchVo);
		return createResultList(resultListVo.getTbCmnMsgMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/operstdmgmt/viewListTbCmnMsgCd.do")
	public String viewListTbCmnMsgMst(@ModelAttribute("searchVo") TbCmnMsgMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbCmnMsgMstVo  searchVoClone = new TbCmnMsgMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTbCmnMsgMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/operstdmgmt/viewListTbCmnMsgCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/operstdmgmt/viewListCmnMsgCd";
	}
	private ModelMap viewListTbCmnMsgMstModel(@ModelAttribute("searchVo") TbCmnMsgMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbCmnMsgMstListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = tbCmnMstService.selectListTbCmnMsgCdVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo = new TbCmnMsgMstListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultListVo = new TbCmnMsgMstListVo();
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
	
	@RequestMapping(value = "/opermgmt/operstdmgmt/viewInsertTbCmnMsgCd.model", method = RequestMethod.POST)
	public ModelMap viewInsertTbCmnMsgCd(HttpServletRequest request)  {
		ModelMap resultModel = new ModelMap();

		ModelMap model = viewInsertTbCmnMsgCdModel(request);
		TbCmnMsgLvlCdListVo tbCmnMsgLvlCdListVo = (TbCmnMsgLvlCdListVo)model.get("msgLvlCdList");
		TbCmnMsgTypeCdListVo tbCmnMsgTypeCdListVo = (TbCmnMsgTypeCdListVo)model.get("msgTypeCdList");
		 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("msgLvlCdList", tbCmnMsgLvlCdListVo.getTbCmnMsgLvlCdVos());
		map.put("msgLvlCdListTotalCount", tbCmnMsgLvlCdListVo.getTotalCount());

		map.put("msgTypeCdList", tbCmnMsgTypeCdListVo.getTbCmnMsgTypeCdVos());
		map.put("msgTypeCdListTotalCount", tbCmnMsgTypeCdListVo.getTotalCount());

		resultModel.addAttribute("result",map);
		return resultModel;
	}
	@RequestMapping(value = "/opermgmt/operstdmgmt/viewInsertTbCmnMsgCd.ajax", method = RequestMethod.POST)
	public String viewInsertTbCmnMsgCd(@RequestBody CommonVo searchVo, ModelMap model,HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = viewInsertTbCmnMsgCdModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/operstdmgmt/viewInsertTbCmnMsgCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/operstdmgmt/viewInsertCmnMsgCd";
	}
	private ModelMap viewInsertTbCmnMsgCdModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbCmnMsgLvlCdListVo tbCmnMsgLvlCdListVo = null;
		TbCmnMsgTypeCdListVo tbCmnMsgTypeCdListVo = null;
		try {
			tbCmnMsgLvlCdListVo = tbCmnMstService.selectListTbCmnMsgLvlCdVo();
			model.addAttribute("msgLvlCdList", tbCmnMsgLvlCdListVo);
			
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			tbCmnMsgLvlCdListVo = new TbCmnMsgLvlCdListVo();
			tbCmnMsgLvlCdListVo.setTotalCount(0);
			tbCmnMsgLvlCdListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			tbCmnMsgLvlCdListVo = new TbCmnMsgLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbCmnMsgLvlCdListVo.setCommonMsg(msgDesc);
			tbCmnMsgLvlCdListVo.setTotalCount(0);
		}
		try {
			tbCmnMsgTypeCdListVo = tbCmnMstService.selectListTbCmnMsgTypeCdVo();
			model.addAttribute("msgTypeCdList", tbCmnMsgTypeCdListVo);
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			tbCmnMsgTypeCdListVo = new TbCmnMsgTypeCdListVo();
			tbCmnMsgTypeCdListVo.setTotalCount(0);
			tbCmnMsgTypeCdListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			tbCmnMsgTypeCdListVo = new TbCmnMsgTypeCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbCmnMsgTypeCdListVo.setCommonMsg(msgDesc);
			tbCmnMsgTypeCdListVo.setTotalCount(0);
		}
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/operstdmgmt/insertTbCmnMsgCd.json", method = RequestMethod.POST)
	@ResponseBody
	public TbCmnMsgMstVo insertTbCmnMsgCd(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, 
			HttpServletRequest request, HttpServletResponse reponse)  {
		TbCmnMsgMstVo resultVo = null;
		try {
			resultVo = tbCmnMstService.insertTbCmnMsgCdVo(tbCmnMsgCdVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbCmnMsgMstVo();
			resultVo.setMsgCd(e.getMessageKey());
			String commonMsg = tbCmnMstService.selectTbCmnMsgMstVo(resultVo).getMsgDesc();
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbCmnMsgMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	@RequestMapping(value = "/opermgmt/operstdmgmt/viewDetailTbCmnMsgCd.model", method = RequestMethod.POST)
	public ModelMap viewDetailTbCmnMsg(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, ModelMap model, HttpServletRequest request)  {
		TbCmnMsgMstVo resultVo = tbCmnMstService.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/operstdmgmt/viewDetailTbCmnMsgCd.ajax", method = RequestMethod.POST)
	public String viewDetailTbCmnMsg(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, ModelMap model, HttpServletRequest request, HttpServletResponse reponse)  {
		TbCmnMsgMstVo  searchVoClone = new TbCmnMsgMstVo();
		try {
			CloneUtil.copyObjectInformation(tbCmnMsgCdVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailTbCmnMsgModel(tbCmnMsgCdVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/operstdmgmt/viewDetailTbCmnMsgCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/operstdmgmt/viewDetailCmnMsgCd";
	}
	private ModelMap viewDetailTbCmnMsgModel(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbCmnMsgMstVo resultVo = null;
		try {
			resultVo = tbCmnMstService.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			model.addAttribute("resultVo",resultVo);
		} catch (ServiceException e) {
			resultVo = new TbCmnMsgMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbCmnMsgMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	
	@RequestMapping(value = "opermgmt/operstdmgmt/viewUpdateTbCmnMsgCd.model", method = RequestMethod.POST)
	public ModelMap viewUpdateTbCmnMsgCd(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, ModelMap model, HttpServletRequest request)  {
		TbCmnMsgMstVo resultVo = tbCmnMstService.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "opermgmt/operstdmgmt/viewUpdateTbCmnMsgCd.ajax", method = RequestMethod.POST)
	public String viewUpdateTbCmnMsgCd(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, ModelMap model, HttpServletRequest request, HttpServletResponse reponse)  {
		TbCmnMsgMstVo searchVoClone = new TbCmnMsgMstVo();
		try {
			CloneUtil.copyObjectInformation(tbCmnMsgCdVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateTbCmnMsgCdModel(tbCmnMsgCdVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/operstdmgmt/viewUpdateTbCmnMsgCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/operstdmgmt/viewUpdateCmnMsgCd";
	}
	private ModelMap viewUpdateTbCmnMsgCdModel(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbCmnMsgMstVo resultVo = null;
		try {
			resultVo = tbCmnMstService.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			model.addAttribute("resultVo",resultVo);
		} catch (ServiceException e) {
			resultVo = new TbCmnMsgMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbCmnMsgMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		TbCmnMsgLvlCdListVo tbCmnMsgLvlCdListVo = null;
		TbCmnMsgTypeCdListVo tbCmnMsgTypeCdListVo = null;
		try {
			tbCmnMsgLvlCdListVo = tbCmnMstService.selectListTbCmnMsgLvlCdVo();
			model.addAttribute("msgLvlCdList", tbCmnMsgLvlCdListVo);
		}catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			tbCmnMsgLvlCdListVo = new TbCmnMsgLvlCdListVo();
			tbCmnMsgLvlCdListVo.setTotalCount(0);
			tbCmnMsgLvlCdListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			tbCmnMsgLvlCdListVo = new TbCmnMsgLvlCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbCmnMsgLvlCdListVo.setCommonMsg(msgDesc);
			tbCmnMsgLvlCdListVo.setTotalCount(0);
		}
		
		try {
			tbCmnMsgTypeCdListVo = tbCmnMstService.selectListTbCmnMsgTypeCdVo();
			model.addAttribute("msgTypeCdList", tbCmnMsgTypeCdListVo);
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			tbCmnMsgTypeCdListVo = new TbCmnMsgTypeCdListVo();
			tbCmnMsgTypeCdListVo.setTotalCount(0);
			tbCmnMsgTypeCdListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			tbCmnMsgTypeCdListVo = new TbCmnMsgTypeCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbCmnMsgTypeCdListVo.setCommonMsg(msgDesc);
			tbCmnMsgTypeCdListVo.setTotalCount(0);
		}
		return model;
	}
	
	@RequestMapping(value="opermgmt/operstdmgmt/updateTbCmnMsgCd.json", method = RequestMethod.POST)
	@ResponseBody
	public TbCmnMsgMstVo updateTbCmnMsgCd(@RequestBody TbCmnMsgMstVo tbCmnMsgCdVo, HttpServletRequest request, HttpServletResponse reponse)  {
		TbCmnMsgMstVo resultVo = new TbCmnMsgMstVo();
		try {
			tbCmnMstService.updateTbCmnMsgCdVo(tbCmnMsgCdVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbCmnMsgMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbCmnMsgMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

}
