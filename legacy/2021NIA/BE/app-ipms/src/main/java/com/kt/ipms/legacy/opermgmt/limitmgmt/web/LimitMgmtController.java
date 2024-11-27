package com.kt.ipms.legacy.opermgmt.limitmgmt.web;


import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.limitmgmt.service.LimitMgmtService;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditAssignBasVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;


@Controller
public class LimitMgmtController  extends CommonController{
	

	@Autowired
	private LimitMgmtService limitMgmtService;

	@RequestMapping(value = "/opermgmt/limitmgmt/viewListLimitMgmt.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListLimitMgmt(@RequestBody TbAuditDhcpBasVo searchVo) {
		TbAuditDhcpBasVo resultVo = limitMgmtService.selectTbAuditDhcpBasVo(searchVo);
		TbAuditAssignBasVo resultVo2 = limitMgmtService.selectTbAuditAssignBasVo(searchVo);
		return createResult(resultVo, resultVo2);
	}

	@RequestMapping(value = "/opermgmt/limitmgmt/viewListLimitMgmt.do", method = RequestMethod.POST)
	public String viewListLimitMgmt(@ModelAttribute("searchVo") TbAuditDhcpBasVo searchVo,  ModelMap model) {
		TbAuditDhcpBasVo searchVoClone = new TbAuditDhcpBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListLimitMgmtModel(searchVo);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/limitmgmt/viewListLimitMgmt.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return  "opermgmt/limitmgmt/viewListLimitMgmt";	
	}
	private ModelMap viewListLimitMgmtModel(@ModelAttribute("searchVo") TbAuditDhcpBasVo searchVo) {
		ModelMap model = new ModelMap();
		TbAuditDhcpBasVo resultVo = null;
		
		TbAuditAssignBasVo resultVo2 = null;
		
		try{
			
			resultVo = limitMgmtService.selectTbAuditDhcpBasVo(searchVo);
			
			resultVo2 = limitMgmtService.selectTbAuditAssignBasVo(searchVo);
			
			model.addAttribute("resultVo", resultVo);
			model.addAttribute("resultVo2", resultVo2);
		
		}catch(ServiceException e){
			resultVo = new TbAuditDhcpBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		}catch(Exception e){
			resultVo = new TbAuditDhcpBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	
	
	@RequestMapping(value = "opermgmt/limitmgmt/updateTbAuditDhcpBasVo.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTbAuditDhcpBasVo(@RequestBody TbAuditDhcpBasVo searchVo,  ModelMap model) {
		
		TbAuditDhcpBasVo resultVo = null;
		
		try{
			
			limitMgmtService.updateTbAuditDhcpBasVo(searchVo);
		
			resultVo = new TbAuditDhcpBasVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		
		}catch(ServiceException e){
			resultVo = new TbAuditDhcpBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		}catch(Exception e){
			resultVo = new TbAuditDhcpBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;	
	}
	
	
	
	@RequestMapping(value = "opermgmt/limitmgmt/updateAuditAssignBasVo.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateAuditAssignBasVo(@RequestBody TbAuditAssignBasVo searchVo,  ModelMap model) {
		
		TbAuditAssignBasVo resultVo = null;
		
		try{
			
			limitMgmtService.updateTbAuditAssignBasVo(searchVo);
		
			resultVo = new TbAuditAssignBasVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		
		}catch(ServiceException e){
			resultVo = new TbAuditAssignBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		}catch(Exception e){
			resultVo = new TbAuditAssignBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;	
	}
	
	
	


}
