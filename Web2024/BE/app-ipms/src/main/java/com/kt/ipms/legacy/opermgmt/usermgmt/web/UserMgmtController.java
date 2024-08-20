package com.kt.ipms.legacy.opermgmt.usermgmt.web;

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
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnListVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class UserMgmtController extends CommonController {
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());
	
	/**
	 * 사용자관리 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbUserBas.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListTbUserBas(@RequestBody TbUserBasVo searchVo, ModelMap model,
	HttpServletRequest request)  {
		TbUserBasListVo resultListVo = userMgmtService.selectListTbUserBas(searchVo);
		return createResultList(resultListVo.getTbUserBasVos(), resultListVo.getTotalCount());
	}
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbUserBas.do", method = RequestMethod.POST)
	public String selectListTbUserBas(@ModelAttribute("searchVo") TbUserBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserBasVo searchVoClone = new TbUserBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListTbUserBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewListTbUserBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewListUserBas";
	}
	
	private ModelMap selectListTbUserBasModel(@ModelAttribute("searchVo") TbUserBasVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserBasListVo resultListVo = null;
		try {
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			List<CommonCodeVo> userSttusCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_STTUS_CD, null);
			model.addAttribute("userGradeCds", userGradeCds);
			model.addAttribute("userSttusCds", userSttusCds);
			setPagination(searchVo);
			resultListVo = userMgmtService.selectListTbUserBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbUserBasListVo();
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
	
	@RequestMapping(value = "/opermgmt/usermgmt/selectSearchTbUserBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbUserBasListVo selectSearchTbUserBas(@RequestBody TbUserBasVo searchVo, 
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserBasListVo resultListVo = null;
		try {

			resultListVo = userMgmtService.selectSearchTbUserBas(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbUserBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		//model.addAttribute("resultListVo", resultListVo);		
		return resultListVo;
	}
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewSearchTbUserBas.model", method = RequestMethod.POST)
	public ModelMap searchViewTbUserBas(@RequestBody TbUserBasVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserBasListVo resultListVo = new TbUserBasListVo();
		return createResultList(resultListVo.getTbUserBasVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewSearchTbUserBas.ajax", method = RequestMethod.POST)
	public String searchViewTbUserBas(@RequestBody TbUserBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserBasVo searchVoClone = new TbUserBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = searchViewTbUserBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewSearchTbUserBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewSearchUserId";
	}
	private ModelMap searchViewTbUserBasModel(@RequestBody TbUserBasVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();

		TbUserBasListVo resultListVo = null;
		try {

			resultListVo =  new TbUserBasListVo();
			resultListVo.setTypeFlag(searchVo.getTypeFlag());
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbUserBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * 사용자관리 상세정보 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/usermgmt/viewDetailTbUserBas.model", method = RequestMethod.POST)
	public ModelMap selectTbUserBas(@RequestBody TbUserBasVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserBasVo resultVo = userMgmtService.selectTbuserBas(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewDetailTbUserBas.ajax", method = RequestMethod.POST)
	public String selectTbUserBas(@RequestBody TbUserBasVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserBasVo searchVoClone = new TbUserBasVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectTbUserBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewDetailTbUserBas.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewDetailUserBas";
	}
	private ModelMap selectTbUserBasModel(@RequestBody TbUserBasVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserBasVo tbUserBasVo = null;
		try {
			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD, null);
			List<CommonCodeVo> userSttusCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_STTUS_CD, null);
			model.addAttribute("userGradeCds", userGradeCds);
			model.addAttribute("userSttusCds", userSttusCds);
			//setPagination(searchVo);
			tbUserBasVo = userMgmtService.selectTbuserBas(searchVo);
			tbUserBasVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			tbUserBasVo = new TbUserBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbUserBasVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbUserBasVo = new TbUserBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbUserBasVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("tbUserBasVo", tbUserBasVo);
		//PaginationInfo paginationInfo = getPaginationInfo();
		//paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		//model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/**
	 * 사용자관리 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value="opermgmt/usermgmt/updateTbUserBas.json", method = RequestMethod.POST)
	@ResponseBody
	public TbUserBasVo updateTbUserBas(@RequestBody TbUserBasVo updateVo, HttpServletRequest request, HttpServletResponse reponse)  {
		TbUserBasVo resultVo = new TbUserBasVo();
		try {
				userMgmtService.updateTbuserBas(updateVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			resultVo = new TbUserBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbUserBasVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 사용자접속 현황 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbUserConnHist.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListTbUserConnHist(@RequestBody TbUserConnHistVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserConnHistListVo resultListVo = userMgmtService.selectListTbUserConnHist(searchVo);
		return createResultList(resultListVo.getTbUserConnHistVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewListTbUserConnHist.do", method = RequestMethod.POST)
	public String selectListTbUserConnHist(@ModelAttribute("searchVo") TbUserConnHistVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserConnHistVo searchVoClone = new TbUserConnHistVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListTbUserConnHistModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewListTbUserConnHist.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewListUserConnHist";
	}
	private ModelMap selectListTbUserConnHistModel(@ModelAttribute("searchVo") TbUserConnHistVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserConnHistListVo resultListVo = null;
		try {
			List<CommonCodeVo> userConnResultCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_CONN_RESLT_TYPE_CD, null);
			model.addAttribute("userConnResultCds", userConnResultCds);
			
			setPagination(searchVo);
			
			resultListVo = userMgmtService.selectListTbUserConnHist(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			resultListVo = new TbUserConnHistListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbUserConnHistListVo();
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
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewListUserHndSetTxn.model", method = RequestMethod.POST)
	public ModelMap viewListUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserHndsetApyTxnListVo resultListVo = userMgmtService.selectListTbUserHndsetApyTxnVo(searchVo);
		return createResultList(resultListVo.getTbUserHndsetApyTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewListUserHndSetTxn.ajax", method = RequestMethod.POST)
	public String viewListUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserHndsetApyTxnVo searchVoClone = new TbUserHndsetApyTxnVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListUserHndSetTxnModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewListUserHndSetTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewUpdateUserConIp";
	}
	private ModelMap viewListUserHndSetTxnModel(@RequestBody TbUserHndsetApyTxnVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserHndsetApyTxnListVo resultListVo = null;
		try {
			
			resultListVo = userMgmtService.selectListTbUserHndsetApyTxnVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);	
		model.addAttribute("tbUserHndsetApyTxnVo",searchVo );
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewInsertUserHndSetTxn.model", method = RequestMethod.POST)
	public ModelMap viewInsertUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserHndsetApyTxnListVo resultListVo = userMgmtService.insertTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
		return createResultList(resultListVo.getTbUserHndsetApyTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewInsertUserHndSetTxn.ajax", method = RequestMethod.POST)
	public String viewInsertUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserHndsetApyTxnVo searchVoClone = new TbUserHndsetApyTxnVo();
		try {
			CloneUtil.copyObjectInformation(tbUserHndsetApyTxnVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertUserHndSetTxnModel(tbUserHndsetApyTxnVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewInsertUserHndSetTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewUpdateUserConIp";
	}
	private ModelMap viewInsertUserHndSetTxnModel(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserHndsetApyTxnListVo resultListVo = null;
		try {
			
			resultListVo = userMgmtService.insertTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
			
			if(resultListVo.getCommonMsg().equals("DUP"))
			{
				String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("APP.INFO.00046", new String[]{tbUserHndsetApyTxnVo.getSuserHndsetId()}));
				resultListVo.setsComment(msgDesc);
			}
			else {
				resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}
			
			
		} catch (ServiceException e){
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);	
		model.addAttribute("tbUserHndsetApyTxnVo",tbUserHndsetApyTxnVo);
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/usermgmt/viewDeleteUserHndSetTxn.model", method = RequestMethod.POST)
	public ModelMap viewDeleteUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo, ModelMap model,
			HttpServletRequest request)  {
		TbUserHndsetApyTxnListVo resultListVo = userMgmtService.deleteTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
		return createResultList(resultListVo.getTbUserHndsetApyTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/usermgmt/viewDeleteUserHndSetTxn.ajax", method = RequestMethod.POST)
	public String viewDeleteUserHndSetTxn(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbUserHndsetApyTxnVo searchVoClone = new TbUserHndsetApyTxnVo();
		try {
			CloneUtil.copyObjectInformation(tbUserHndsetApyTxnVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDeleteUserHndSetTxnModel(tbUserHndsetApyTxnVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/usermgmt/viewDeleteUserHndSetTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/usermgmt/viewUpdateUserConIp";
	}
	private ModelMap viewDeleteUserHndSetTxnModel(@RequestBody TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserHndsetApyTxnListVo resultListVo = null;
		try {
			
			resultListVo = userMgmtService.deleteTbUserHndsetApyTxnVo(tbUserHndsetApyTxnVo);
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserHndsetApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);	
		model.addAttribute("tbUserHndsetApyTxnVo",tbUserHndsetApyTxnVo);
		return model;
	}
	
	

}
