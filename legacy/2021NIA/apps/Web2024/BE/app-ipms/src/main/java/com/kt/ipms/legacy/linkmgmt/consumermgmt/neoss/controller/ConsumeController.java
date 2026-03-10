package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.linkmgmt.common.util.ConsumerUtil;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.service.ConsumeService;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Controller
public class ConsumeController {
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());

	@Autowired
	private ConsumerUtil consumerUtil;

	@Autowired
	private LinkUtil linkUtil;

	@Autowired
	private ConsumeService consumeService;

	// 2024-07-23
	
	@RequestMapping(value = "/ipmgmt/consumermgmt/neoss/consumeNes0001.do", method = RequestMethod.POST)
	public void consumeNes0001(@ModelAttribute("searchVo") CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String date = searchVo.getSearchWrd() == null ||  searchVo.getSearchWrd().length() == 0 ?  "2023-06-10" : null;
		consumeService.consumeNes0001(date);
	}

	@RequestMapping(value = "/ipmgmt/consumermgmt/neoss/consumeNes0002.do", method = RequestMethod.POST)
	public void consumeNes0002(@ModelAttribute("searchVo") TbIpAllocNeossMstListVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		consumeService.consumeNes0002(searchVo);
	}

	@RequestMapping(value = "/ipmgmt/consumermgmt/neoss/consumeNes0003.do", method = RequestMethod.POST)
	public void consumeNes0003(@ModelAttribute("searchVo") TbIpAllocNeossMstListVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		consumeService.consumeNes0003(searchVo);
	}

	@RequestMapping(value = "/ipmgmt/consumermgmt/neoss/consumeNes0004.do", method = RequestMethod.POST)
	public void consumeNes0004(@ModelAttribute("searchVo") TbIpAllocNeossMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		consumeService.consumeNes0004(searchVo);
	}

	@RequestMapping(value = "/ipmgmt/consumermgmt/neoss/consumeNes0005.do", method = RequestMethod.POST)
	public void consumeNes0005(@ModelAttribute("searchVo") TbIpAllocNeossMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		consumeService.consumeNes0005(searchVo);
	}
}
