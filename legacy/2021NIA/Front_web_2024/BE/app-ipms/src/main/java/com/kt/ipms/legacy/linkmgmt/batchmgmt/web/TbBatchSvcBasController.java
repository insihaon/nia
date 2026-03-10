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
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.service.BatchJobService;
import com.kt.ipms.legacy.linkmgmt.common.service.TbBatchSvcBasService;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class TbBatchSvcBasController extends CommonController{
	@Autowired
	private BatchJobService batchJobService;
	
	@Autowired
	private TbBatchSvcBasService tbBatchSvcBasService;
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/setBatchOn.json")
	public @ResponseBody
	@EncryptResponse String setBatchOn(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		try{
			batchJobService.startBatchJobServices();
		}catch(ServiceException e){
			return tbCmnMstService.selectMsgDesc(e);
		}
		return CommonCodeUtil.SUCCESS_MSG;
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/setBatchOff.json")
	public @ResponseBody
	@EncryptResponse String setBatchOff(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		try{
			batchJobService.stopBatchJobServices();
		}catch(ServiceException e){
			return tbCmnMstService.selectMsgDesc(e);
		}
		return CommonCodeUtil.SUCCESS_MSG;
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/resetBatchJobs.json")
	public @ResponseBody
	@EncryptResponse String rescheduleBatchJobs(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		try{
			batchJobService.updateBatchJobs();
		}catch(ServiceException e){
			return tbCmnMstService.selectMsgDesc(e);
		}
		return CommonCodeUtil.SUCCESS_MSG;
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListTbBatchSvcBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListTbBatchSvcBasVo(@RequestBody TbBatchSvcBasVo searchVo, ModelMap model, HttpServletRequest request){
		TbBatchSvcBasListVo resultListVo = tbBatchSvcBasService.selectListTbBatchSvcBasVo(searchVo);
		return createResultList(resultListVo.getTbBatchSvcBasVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewListTbBatchSvcBas.do", method = RequestMethod.POST)
	public String selectListTbBatchSvcBasVo(@ModelAttribute("searchVo") TbBatchSvcBasVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbBatchSvcBasVo searchVoClone = new TbBatchSvcBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListTbBatchSvcBasVoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/batchmgmt/viewListTbBatchSvcBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/batchmgmt/viewListTbBatchSvcBas";
	}
	
	private ModelMap selectListTbBatchSvcBasVoModel(@ModelAttribute("searchVo") TbBatchSvcBasVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBatchSvcBasListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = tbBatchSvcBasService.selectListTbBatchSvcBasVo(searchVo);
			if(resultListVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewDetailTbBatchSvcBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectDetailTbBatchLog(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, ModelMap model,
			HttpServletRequest request) throws ServiceException {
		TbBatchSvcBasVo resultVo = tbBatchSvcBasService.selectTbBatchSvcBasVo(tbBatchSvcBasVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewDetailTbBatchSvcBas.ajax", method = RequestMethod.POST)
	public String selectDetailTbBatchLog(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		TbBatchSvcBasVo searchVoClone = new TbBatchSvcBasVo();
		try {
			CloneUtil.copyObjectInformation(tbBatchSvcBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectDetailTbBatchLogModel(tbBatchSvcBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/batchmgmt/viewDetailTbBatchSvcBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/batchmgmt/viewDetailTbBatchSvcBas";
	}
	private ModelMap selectDetailTbBatchLogModel(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBatchSvcBasVo resultVo = null;
		try {
			resultVo = tbBatchSvcBasService.selectTbBatchSvcBasVo(tbBatchSvcBasVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			model.addAttribute("resultVo",resultVo);
		} catch (ServiceException e) {
			resultVo = new TbBatchSvcBasVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		}
		return model;
	}
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewUpdateTbBatchSvcBas.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateTbBatchLog(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, ModelMap model,
			HttpServletRequest request) throws ServiceException {
		TbBatchSvcBasVo resultVo = tbBatchSvcBasService.selectTbBatchSvcBasVo(tbBatchSvcBasVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/linkmgmt/batchmgmt/viewUpdateTbBatchSvcBas.ajax", method = RequestMethod.POST)
	public String viewUpdateTbBatchLog(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		TbBatchSvcBasVo searchVoClone = new TbBatchSvcBasVo();
		try {
			CloneUtil.copyObjectInformation(tbBatchSvcBasVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateTbBatchLogModel(tbBatchSvcBasVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/batchmgmt/viewUpdateTbBatchSvcBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/batchmgmt/viewUpdateTbBatchSvcBas";
	}
	private ModelMap viewUpdateTbBatchLogModel(@RequestBody TbBatchSvcBasVo tbBatchSvcBasVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBatchSvcBasVo resultVo = null;
		try {
			resultVo = tbBatchSvcBasService.selectTbBatchSvcBasVo(tbBatchSvcBasVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			model.addAttribute("resultVo",resultVo);
		} catch (ServiceException e) {
			resultVo = new TbBatchSvcBasVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		}
		return model;
	}
	
	
	@RequestMapping(value = "/linkmgmt/batchmgmt/updateTbBatchSvcBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTbBatchSvcBas(@RequestBody TbBatchSvcBasVo updateVo, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		TbBatchSvcBasVo resultVo = null;
		try {
			resultVo = tbBatchSvcBasService.updateTbBatchSvcBasVo(updateVo);
			batchJobService.updateBatchJobs();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbBatchSvcBasVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
}
