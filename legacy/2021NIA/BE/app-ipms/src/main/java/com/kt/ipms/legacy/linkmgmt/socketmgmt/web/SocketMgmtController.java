package com.kt.ipms.legacy.linkmgmt.socketmgmt.web;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.service.CallableBatchService;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.service.SocketMgmtService;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.WhoisMstVo;

@Controller
public class SocketMgmtController extends CommonController {
	
	@Autowired
	private SocketMgmtService socketMgmtService;
	
	@Autowired
	private CallableBatchService callableBatchService;
	
	@RequestMapping(value = "/linkmgmt/socketmgmt/viewDetailWhois.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailWhois(@RequestBody WhoisMstVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		WhoisMstVo resultVo = socketMgmtService.getWhoisMstVo(searchVo.getQuery());
		IpAllocOperMstVo ipInfoVo = socketMgmtService.selectMainIpInfoMst(searchVo);
		return createResult(resultVo, ipInfoVo);
	}
	@RequestMapping(value = "/linkmgmt/socketmgmt/viewDetailWhois.ajax", method = RequestMethod.POST)
	public String viewDetailWhois(@RequestBody WhoisMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		WhoisMstVo searchVoClone = new WhoisMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailWhoisModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/socketmgmt/viewDetailWhois.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/socketmgmt/viewDetailWhoIsInfo";
	}
	private ModelMap viewDetailWhoisModel(@RequestBody WhoisMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		WhoisMstVo resultVo = null;
		IpAllocOperMstVo ipInfoVo = null;
		try {
			ipInfoVo = socketMgmtService.selectMainIpInfoMst(searchVo);
			
			resultVo = socketMgmtService.getWhoisMstVo(searchVo.getQuery());
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new WhoisMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			PrintLogUtil.printError(e);
			resultVo = new WhoisMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("whoisVo", resultVo);	
		model.addAttribute("ipInfo", ipInfoVo);	
		return model;
	}
	
	
	@RequestMapping(value = "/linkmgmt/socketmgmt/testWhoisBatch1.ajax", method = RequestMethod.POST)
	public void testWhoisBatch() {
		try {
			socketMgmtService.startWhoisBatchScheduler();
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/linkmgmt/socketmgmt/testWhoisBatch2.ajax", method = RequestMethod.POST)
	public void testWhoisBatch2() {
		try {
			callableBatchService.callUfBatchWhois();
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
	}

}
