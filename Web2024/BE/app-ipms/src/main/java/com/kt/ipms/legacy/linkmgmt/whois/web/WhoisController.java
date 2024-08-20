package com.kt.ipms.legacy.linkmgmt.whois.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisService;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;

/**
 * WHOIS 실시간 연동 Controller
 */
@Controller
public class WhoisController extends CommonController {
	
	@Autowired
	private WhoisService whoisService;

	@RequestMapping(value="/whois/testWhoisSubmit1.json", method=RequestMethod.POST)
	@ResponseBody
	public void testWhoisSubmit1(@RequestBody TbWhoisVo tbWhoisVo) {
		try {
			if(StringUtils.isNotEmpty(tbWhoisVo.getNwhoisSeq().toString())) {
				PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
			}
			whoisService.sendToWhois(tbWhoisVo);
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		
	}
	
	@RequestMapping(value="/whois/testWhoisSubmit2.json", method=RequestMethod.POST)
	@ResponseBody
	public void testWhoisSubmit2(@RequestBody TbWhoisVo tbWhoisVo) {
		try {
			if(StringUtils.isNotEmpty(tbWhoisVo.getNwhoisSeq().toString())) {
				PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
			}
			whoisService.sendToWhoisWithDb(tbWhoisVo);
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		
	}
	
	@RequestMapping(value="/whois/testWhoisSubmit3.json", method=RequestMethod.POST)
	@ResponseBody
	public void testWhoisSubmit3(@RequestBody TbWhoisVo tbWhoisVo) {
		try {
			
			whoisService.sendToWhoisIpCheck(tbWhoisVo);
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		
	}
	
	@RequestMapping(value="/whois/testWhoisSubmit4.json", method=RequestMethod.POST)
	@ResponseBody
	public WhoisInfoObj testWhoisSubmit4(@RequestBody TbWhoisVo tbWhoisVo) {
		
		WhoisInfoObj obj = new WhoisInfoObj();
		try {
			obj = whoisService.whoisInfo(tbWhoisVo);
		} catch (ServiceException e) {
			PrintLogUtil.printLog(tbCmnMstService.selectMsgDesc(e));
		} catch (Exception e) {
			PrintLogUtil.printLog(e.getMessage());
		}
		
		return obj;
	}
}