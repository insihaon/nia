package com.kt.ipms.legacy.linkmgmt.batchmgmt.web;

import java.lang.reflect.InvocationTargetException;

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

import com.codej.web.annotation.EncryptResponse;
import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.service.BatchJobService;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.service.TbBatchLogService;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogListVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class TbBatchLogController extends CommonController{
	@Autowired
	private TbBatchLogService tbBatchLogService;
	
	@Autowired
	private BatchJobService batchJobService;

	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListTbBatchLog.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListTbBatchLogVo(@RequestBody TbBatchLogVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbBatchLogListVo resultListVo = tbBatchLogService.selectListTbBatchLogVo(searchVo);
		return createResultList(resultListVo.getTbBatchLogVos(), resultListVo.getTotalCount());
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListTbBatchLog.ajax", method = RequestMethod.POST)
	public String viewListTbBatchLogVo(@RequestBody TbBatchLogVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbBatchLogVo searchVoClone = new TbBatchLogVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTbBatchLogVoModel(searchVo, request);
		model.addAllAttributes(builtModel);
		
		searchVoClone.setUrl("/linkmgmt/batchmgmt/viewListTbBatchLog.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/batchmgmt/viewListTbBatchLog";
	}

	private ModelMap viewListTbBatchLogVoModel(@RequestBody TbBatchLogVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBatchLogListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = tbBatchLogService.selectListTbBatchLogVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo = new TbBatchLogListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(commonMsg);
		}
		model.addAttribute("resultListVo", resultListVo);

		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfoPopup", paginationInfo);

		return model;
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewDetailTbBatchLog.ajax", method = RequestMethod.POST)
	public String selectDetailTbBatchLog(@RequestBody TbBatchLogVo tbBatchLogVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbBatchLogVo resultVo = null;
		try {
			resultVo = tbBatchLogService.selectTbBatchLogVo(tbBatchLogVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			model.addAttribute("resultVo",resultVo);
		} catch (ServiceException e) {
			resultVo = new TbBatchLogVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		}

		tbBatchLogVo.setUrl("/linkmgmt/batchmgmt/viewDetailTbBatchLog.model");
		model.addAttribute("searchVoJson", searchVoJson(tbBatchLogVo));
		return "linkmgmt/batchmgmt/viewDetailTbBatchLog";
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/redoBatchProccess.json", method = RequestMethod.POST)
	public TbBatchSvcBasVo redoBatchProccess(@RequestBody TbBatchLogVo tbBatchLogVo, HttpServletRequest request, HttpServletResponse response) {
		TbBatchSvcBasVo resultVo = null;
		TbBatchSvcBasVo tbBatchSvcBasVo = null;
		try {
			tbBatchSvcBasVo = tbBatchLogService.selectTbBatchSvcBasByTbBatchLogSeq(tbBatchLogVo);
			resultVo = batchJobService.redoBatchProccess(tbBatchSvcBasVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbBatchSvcBasVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	/****************************************************************************************
	 * 배치 연동 이력현황
	 ****************************************************************************************/
	
	/**
	 * 배치 연동 이력현황 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListBatchHistMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListBatchHistMst(@RequestBody TbBatchLogVo searchVo, ModelMap model, HttpServletRequest request) {
		TbBatchLogListVo resultListVo = tbBatchLogService.selectListBatchHistMst(searchVo);
		return createResultList(resultListVo.getTbBatchLogVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListBatchHistMst.do", method = RequestMethod.POST)
	public String viewListBatchHistMst(@ModelAttribute("searchVo") TbBatchLogVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbBatchLogVo searchVoClone = new TbBatchLogVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListBatchHistMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/batchmgmt/viewListBatchHistMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/batchmgmt/viewListBatchHistMst";
	}
	private ModelMap viewListBatchHistMstModel(@ModelAttribute("searchVo") TbBatchLogVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBatchLogListVo resultListVo = null;
		try {	
			
			setPagination(searchVo);
			resultListVo = tbBatchLogService.selectListBatchHistMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbBatchLogListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbBatchLogListVo();
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
	
}
