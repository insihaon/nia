package com.kt.ipms.legacy.opermgmt.grantmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.grantmgmt.service.GrantMgmtService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class GrantMgmtController  extends CommonController {
	
	
	@Autowired
	GrantMgmtService grantMgmtService;
	
	
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbAdmrApvTxn.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListTbAdmrApvTxnVo(@RequestBody TbAdmrApvTxnVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbAdmrApvTxnListVo resultListVo = grantMgmtService.selectListTbAdmrApvTxnVo(searchVo);
		return createResultList(resultListVo.getTbAdmrApvTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbAdmrApvTxn.do", method = RequestMethod.POST)
	public String selectListTbAdmrApvTxnVo(@ModelAttribute("searchVo") TbAdmrApvTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserAuthTxnVo searchVoClone = new TbUserAuthTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListTbAdmrApvTxnVoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewListTbAdmrApvTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewListAdmrTxn";
	}
	private ModelMap selectListTbAdmrApvTxnVoModel(@ModelAttribute("searchVo") TbAdmrApvTxnVo searchVo, 
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbAdmrApvTxnListVo resultListVo = null;
		try {
			
			
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			model.addAttribute("userGradeCds", userGradeCds);
			
		//	List<TbLvlBasVo>  svcLineLists  = jwtUtil.getSvcLineList(request).getTbLvlBasVos();
		//	model.addAttribute("svcLineList", svcLineLists);
			setPagination(searchVo);
			resultListVo = grantMgmtService.selectListTbAdmrApvTxnVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbAdmrApvTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbAdmrApvTxnListVo();
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
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewInsertAdmrTxn.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertTbLvlCdVo(@RequestBody TbAdmrApvTxnVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbAdmrApvTxnVo resultVo = new TbAdmrApvTxnVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewInsertAdmrTxn.ajax", method = RequestMethod.POST)
	public String viewInsertTbLvlCdVo(@RequestBody TbAdmrApvTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserAuthTxnVo searchVoClone = new TbUserAuthTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertTbLvlCdVoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewInsertAdmrTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewInsertAdmrTxn";
	}
	private ModelMap viewInsertTbLvlCdVoModel(@RequestBody TbAdmrApvTxnVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbAdmrApvTxnVo tbAdmrApvTxnVo = null;
		try {
			
			
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			model.addAttribute("userGradeCds", userGradeCds);
			//tbLvlCdVo = orgMgmtService.selectTbLvlCdVo(searchVo);
			//tbLvlCdVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			tbAdmrApvTxnVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbAdmrApvTxnVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbAdmrApvTxnVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbAdmrApvTxnVo.setCommonMsg(msgDesc);
			//tbLvlCdVo.setTotalCount(0);
		}
		//model.addAttribute("tbLvlCdVo", tbLvlCdVo);
		//PaginationInfo paginationInfo = getPaginationInfo();
		//paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		//model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/grantmgmt/viewListOperTeamAuth.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListOperTeamAuth(@RequestBody TbOperTeamAuthTxnVo searchVo, 
			HttpServletRequest request, HttpServletResponse response)  {
		TbOperTeamAuthTxnListVo resultListVo = grantMgmtService.selectOperTeamAuthTxnList(searchVo);
		return createResultList(resultListVo.getTbOperTeamAuthTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/grantmgmt/viewListOperTeamAuth.do", method = RequestMethod.POST)
	public String selectListOperTeamAuth(@ModelAttribute("searchVo") TbOperTeamAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserAuthTxnVo searchVoClone = new TbUserAuthTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListOperTeamAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/grantmgmt/viewListOperTeamAuth.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantmgmt/viewListOperTeamAuth";
	}
	private ModelMap selectListOperTeamAuthModel(@ModelAttribute("searchVo") TbOperTeamAuthTxnVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbOperTeamAuthTxnListVo resultListVo = null;
		try {
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			if (searchVo.getTbLvlBasVo() !=null  && StringUtils.hasText(searchVo.getTbLvlBasVo().getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getTbLvlBasVo().getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getTbLvlBasVo().getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getTbLvlBasVo().getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.getTbLvlBasVo().setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.getTbLvlBasVo().setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					
					if( searchVo.getTbLvlBasVo() == null)
					{
						TbLvlBasVo searchBasVo = new TbLvlBasVo();
						searchVo.setTbLvlBasVo(searchBasVo);
					}
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.getTbLvlBasVo().setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.getTbLvlBasVo().setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.getTbLvlBasVo().setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
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
			
			if(searchVo.getTbLvlBasVo() == null)
			{
				TbLvlBasVo tempVo = new TbLvlBasVo();
				searchVo.setTbLvlBasVo(tempVo);
			}
			resultListVo = grantMgmtService.selectOperTeamAuthTxnList(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbOperTeamAuthTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOperTeamAuthTxnListVo();
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
	
	
	
	@RequestMapping(value="/opermgmt/grantmgmt/insertAdmrAutTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbAdmrApvTxnVo insertAdmrAutTxn(@RequestBody TbAdmrApvTxnVo tbAdmrApvTxnVo , HttpServletRequest request, HttpServletResponse response)  
	{
		TbAdmrApvTxnVo resultVo = new TbAdmrApvTxnVo();
		
		try {
			grantMgmtService.insertAdmrAutTxn(tbAdmrApvTxnVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;
	}
	
	
	@RequestMapping(value="/opermgmt/grantmgmt/deleteAdmrAutTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbAdmrApvTxnVo deleteAdmrAutTxn(@RequestBody TbAdmrApvTxnListVo tbAdmrApvTxnListVo , HttpServletRequest request, HttpServletResponse response)  
	{
		TbAdmrApvTxnVo resultVo = new TbAdmrApvTxnVo();
		
		try {
			grantMgmtService.deleteAdmrAutTxn(tbAdmrApvTxnListVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAdmrApvTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/grantmgmt/viewListUserAuth.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		setPagination(searchVo);
		TbUserAuthTxnListVo resultListVo = grantMgmtService.selectUserAuthTxnList(searchVo);
		return createResultList(resultListVo.getTbUserAuthTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/grantmgmt/viewListUserAuth.do", method = RequestMethod.POST)
	public String viewListUserAuth(@ModelAttribute("searchVo") TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserAuthTxnVo searchVoClone = new TbUserAuthTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewListUserAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/grantmgmt/viewListUserAuth.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantmgmt/viewListUserAuth";
	}
	private ModelMap viewListUserAuthModel(@ModelAttribute("searchVo") TbUserAuthTxnVo searchVo, 
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserAuthTxnListVo resultListVo = null;
		try {
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			if (searchVo.getTbLvlBasVo() !=null  && StringUtils.hasText(searchVo.getTbLvlBasVo().getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getTbLvlBasVo().getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getTbLvlBasVo().getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getTbLvlBasVo().getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.getTbLvlBasVo().setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.getTbLvlBasVo().setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					
					if( searchVo.getTbLvlBasVo() == null)
					{
						TbLvlBasVo searchBasVo = new TbLvlBasVo();
						searchVo.setTbLvlBasVo(searchBasVo);
					}
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.getTbLvlBasVo().setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.getTbLvlBasVo().setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.getTbLvlBasVo().setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
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
			
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);			
			model.addAttribute("userGradeCds", userGradeCds);
			setPagination(searchVo);
			
			if(searchVo.getTbLvlBasVo() == null)
			{
				TbLvlBasVo tempVo = new TbLvlBasVo();
				searchVo.setTbLvlBasVo(tempVo);
			}
			resultListVo = grantMgmtService.selectUserAuthTxnList(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserAuthTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbUserAuthTxnListVo();
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
	
	
	@RequestMapping(value="/opermgmt/grantmgmt/deleteUserAuthTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbUserAuthTxnVo deleteUserAuthTxn(@RequestBody TbUserAuthTxnListVo tbUserAuthTxnListVo , HttpServletRequest request, HttpServletResponse response)  
	{
		TbUserAuthTxnVo resultVo = new TbUserAuthTxnVo();
		
		try {
			grantMgmtService.deleteTbUserAuthTxnVo(tbUserAuthTxnListVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);			
		} catch (ServiceException e) {
			resultVo = new TbUserAuthTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbUserAuthTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;
	}

	@RequestMapping(value = "/opermgmt/grantmgmt/selectUserGradeCds.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectUserGradeCds()  {
				List<CommonCodeVo> userGradeCds = new ArrayList<>();
		try {
			userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createResultList(userGradeCds, userGradeCds.size());
	}
	
	@RequestMapping(value = "/opermgmt/grantmgmt/viewInsertUserAuth.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertUserAuth(@RequestBody TbUserAuthTxnVo searchVo, 
			HttpServletRequest request)  {
		TbUserAuthTxnListVo resultListVo = grantMgmtService.selectDetailUserAuthTxn(searchVo);
		return createResult(resultListVo);
	}
	@RequestMapping(value = "/opermgmt/grantmgmt/viewInsertUserAuth.ajax", method = RequestMethod.POST)
	public String viewInsertUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserAuthTxnVo searchVoClone = new TbUserAuthTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertUserAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/grantmgmt/viewInsertUserAuth.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantmgmt/viewInsertUserAuth";
	}
	private ModelMap viewInsertUserAuthModel(@RequestBody TbUserAuthTxnVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserAuthTxnListVo resultListVo = null;
		try {		
			
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			model.addAttribute("userGradeCds", userGradeCds);
			
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = new TbLvlBasListVo();
			TbLvlBasListVo nodeListVo = new TbLvlBasListVo();
			
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			
			if(StringUtils.hasText(searchVo.getSuserId())) {
				resultListVo = grantMgmtService.selectDetailUserAuthTxn(searchVo);
			}
			else{
				resultListVo = new TbUserAuthTxnListVo();
				resultListVo.setTotalCount(0);
			}
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);			
			
		}catch (ServiceException e){
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			//tbLvlCdVo.setTotalCount(0);
		}
		
		model.addAttribute("resultListVo", resultListVo);
		//model.addAttribute("tbLvlCdVo", tbLvlCdVo);
		//PaginationInfo paginationInfo = getPaginationInfo();
		//paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		//model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	
	@RequestMapping(value="/opermgmt/grantmgmt/insertUserAuthTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbUserAuthTxnVo insertUserAuthTxn(@RequestBody TbUserAuthTxnListVo tbUserAuthTxnListVo , HttpServletRequest request, HttpServletResponse response)  
	{
		TbUserAuthTxnVo resultVo = new TbUserAuthTxnVo();
		
		try {
			grantMgmtService.insertTbUserAuthTxnVo(tbUserAuthTxnListVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);			
		} catch (ServiceException e) {
			resultVo = new TbUserAuthTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbUserAuthTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return  resultVo;
	}

}
