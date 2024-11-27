package com.kt.ipms.legacy.ipmgmt.tracemgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.service.TraceMgmtService;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPListVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpHostMstVo;


@Controller
public class TraceMgmtController extends CommonController {
	
	@Autowired
	TraceMgmtService traceMgmtService;
	@RequestMapping(value = "/ipmgmt/tracemgmt/viewTraceRouteMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewMainTraceRoute(ModelMap model, HttpServletRequest request) {
		TbTraceIPVo resultVo = new TbTraceIPVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/tracemgmt/viewTraceRouteMst.do", method = RequestMethod.POST)
	public String viewMainTraceRoute(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbTraceIPVo searchVoClone = new TbTraceIPVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewMainTraceRouteModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/tracemgmt/viewTraceRouteMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/tracemgmt/viewTraceRouteMst";
	}
	private ModelMap viewMainTraceRouteModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTraceIPVo resultVo = null;
		try {
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			resultVo = new TbTraceIPVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTraceIPVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTraceIPVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	

	/**
	 * @Method	:	selectListTraceRoute
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	메인 Tracer Route 호출
	 * @변경이력	:
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return TbTraceIPListVo
	 */
	@RequestMapping(value = "/ipmgmt/tracemgmt/selectListTraceRoute.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectListTraceRoute(@RequestBody TbTraceIPVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		TbTraceIPListVo resultListVo = null;
		try{			
			resultListVo = traceMgmtService.excuteTraceRoute(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {	
			resultListVo = new TbTraceIPListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);	
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbTraceIPListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"}));
			resultListVo.setCommonMsg(msgDesc);
		}		
		return resultListVo;
	}
	
	@RequestMapping(value = "/ipmgmt/tracemgmt/viewDetailHost.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailHost(@RequestBody TbTraceIPVo searchVo, ModelMap model, HttpServletRequest request) {
		TbTraceIpHostMstVo resultVo = traceMgmtService.selectHostDetailInfo(searchVo);
		TbTraceIpAssignMstVo resultAsgnVo = traceMgmtService.selectAssignDetailInfo(searchVo);
		return createResult(resultVo, resultAsgnVo);
	}
	@RequestMapping(value = "/ipmgmt/tracemgmt/viewDetailHost.ajax", method = RequestMethod.POST)
	public String viewDetailHost(@RequestBody TbTraceIPVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbTraceIPVo searchVoClone = new TbTraceIPVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailHostModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/tracemgmt/viewDetailHost.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/tracemgmt/viewDetailHost";
	}
	
	private ModelMap viewDetailHostModel(@RequestBody TbTraceIPVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbTraceIpHostMstVo resultVo = null;
		TbTraceIpAssignMstVo resultAsgnVo = null;
		try {
			resultVo = traceMgmtService.selectHostDetailInfo(searchVo);
			resultAsgnVo = traceMgmtService.selectAssignDetailInfo(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbTraceIpHostMstVo();
			resultAsgnVo = new TbTraceIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);	
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbTraceIpHostMstVo();
			resultAsgnVo = new TbTraceIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 호스트정보 "}));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("resultAsgnVo", resultAsgnVo);
		return model;
	}
	
//	/**
//	 * @Method	:	selectHostDetailInfo
//	 * @Date	:	2014. 12. 7.
//	 * @Author	:	neoargon
//	 * @DESC	:	팝업 Host상세 정보 조회
//	 * @변경이력	:
//	 * @param searchVo
//	 * @param request
//	 * @param response
//	 * @return TbTraceIpHostMstListVo
//	 */
//	@RequestMapping(value = "/ipmgmt/tracemgmt/selectHostDetailInfo.json", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public TbTraceIpHostMstVo selectHostDetailInfo(@RequestBody TbTraceIPVo searchVo, HttpServletRequest request, HttpServletResponse response){
//		
//		TbTraceIpHostMstVo resultVo = null;
//		try{			
//			resultVo = traceMgmtService.selectHostDetailInfo(searchVo);
//			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			
//		} catch (ServiceException e) {	
//			resultVo = new TbTraceIpHostMstVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);	
//			resultVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			resultVo = new TbTraceIpHostMstVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 호스트정보 "}));
//			resultVo.setCommonMsg(msgDesc);
//		}		
//		return resultVo;
//	}
//	
//	/**
//	 * @Method	:	selectTraceDetailAssignInfo
//	 * @Date	:	2014. 12. 7.
//	 * @Author	:	neoargon
//	 * @DESC	:	팝업 할당 상세 정보 조회
//	 * @변경이력	:
//	 * @param searchVo
//	 * @param request
//	 * @param response
//	 * @return TbTraceIpAssignMstListVo
//	 */
//	@RequestMapping(value = "/ipmgmt/tracemgmt/selectAssignDetailInfo.json", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public TbTraceIpAssignMstVo selectAssignDetailInfo(@RequestBody TbTraceIPVo searchVo, HttpServletRequest request, HttpServletResponse response){
//		
//		TbTraceIpAssignMstVo resultVo = null;
//		try{			
//			resultVo = traceMgmtService.selectAssignDetailInfo(searchVo);
//			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			
//		} catch (ServiceException e) {	
//			resultVo = new TbTraceIpAssignMstVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);	
//			resultVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			resultVo = new TbTraceIpAssignMstVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 시설정보 "}));
//			resultVo.setCommonMsg(msgDesc);
//		}		
//		return resultVo;
//	}

}
