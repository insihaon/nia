package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.grantmgmt.service.GrantMgmtService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.service.GrantSubsMgmtService;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubListVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantListVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.web.RequireMgmtController;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class GrantSubsMgmtController extends CommonController {
	@Autowired
	GrantMgmtService grantMgmtService;

	@Autowired
	GrantSubsMgmtService grantSubsMgmtService;

	@Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;

	@Autowired
	private UserMgmtService userMgmtService;

	@Autowired
	private ReqBoardService reqBoardService;

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewListUserAuthSubs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListUserAuth(
			@ModelAttribute("searchVo") TbUserGrantVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbUserGrantListVo resultListVo = grantSubsMgmtService.selectTbUserGrantList(searchVo);
		return createResultList(resultListVo.getTbUserGrantVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewListUserAuthSubs.do", method = RequestMethod.POST)
	public String viewListUserAuth(
			@ModelAttribute("searchVo") TbUserGrantVo searchVo,
			ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewListUserAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/grantsubsmgmt/viewListUserAuthSubs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantsubsmgmt/viewListUserAuthSubs";
	}

	private ModelMap viewListUserAuthModel(@ModelAttribute("searchVo") TbUserGrantVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserGrantListVo resultListVo = null;
		try {

			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD,
					null);
			model.addAttribute("userGradeCds", userGradeCds);
			String userId = jwtUtil.getUserId(request);
			model.addAttribute("userId", userId);
			setPagination(searchVo);
			resultListVo = grantSubsMgmtService.selectTbUserGrantList(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			System.out.println("E1 == " + e);
			resultListVo = new TbUserGrantListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			System.out.println("E2 == " + e);
			resultListVo = new TbUserGrantListVo();
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

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewInsertUserAuthSubs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request) {
		// TbUserAuthTxnListVo resultListVo =
		// grantMgmtService.selectDetailUserAuthTxn(searchVo);
		ModelMap builtModel = viewInsertUserAuthModel(searchVo, request);

		ModelMap finalModel = new ModelMap();
		finalModel.addAllAttributes(builtModel);
		TbUserAuthTxnListVo resultListVo = (TbUserAuthTxnListVo) builtModel.get("resultListVo");
		finalModel.addAttribute("resultList", resultListVo.getTbUserAuthTxnVos());
		finalModel.addAttribute("totalCount", resultListVo.getTotalCount());
		return createResultList(resultListVo.getTbUserAuthTxnVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewInsertUserAuthSubs.ajax", method = RequestMethod.POST)
	public String viewInsertUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
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

		searchVoClone.setUrl("/opermgmt/grantsubsmgmt/viewInsertUserAuthSubs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantsubsmgmt/viewInsertUserAuthSubs";
	}

	private ModelMap viewInsertUserAuthModel(@RequestBody TbUserAuthTxnVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserAuthTxnListVo resultListVo = null;
		try {

			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD,
					null);
			model.addAttribute("userGradeCds", userGradeCds);
			TbLvlBasVo tsearchVo = new TbLvlBasVo();
			TbLvlBasListVo svcLineListVo = orgMgmtAdapterService.selectListSvcLine(tsearchVo);

			model.addAttribute("svcLineListVo", svcLineListVo);

			if (StringUtils.hasText(searchVo.getSuserId())) {
				resultListVo = grantMgmtService.selectDetailUserAuthTxn(searchVo);
			} else {
				resultListVo = new TbUserAuthTxnListVo();
				resultListVo.setTotalCount(0);
			}

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			// tbLvlCdVo.setTotalCount(0);
		}

		model.addAttribute("resultListVo", resultListVo);
		// model.addAttribute("tbLvlCdVo", tbLvlCdVo);
		// PaginationInfo paginationInfo = getPaginationInfo();
		// paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		// model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/insertUserAuthTxnSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertUserAuthTxnSub(@RequestBody TbUserAuthTxnSubListVo tbUserAuthTxnSubListVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbUserAuthTxnSubVo resultVo = new TbUserAuthTxnSubVo();

		try {
			BigInteger grantSeq = grantSubsMgmtService.insertTbUserAuthTxnVo(tbUserAuthTxnSubListVo);

			TbUserGrantVo tbUserGrantVo = new TbUserGrantVo();
			tbUserGrantVo.setGrantSeq(grantSeq);
			tbUserGrantVo = grantSubsMgmtService.selectGrantVo(tbUserGrantVo);
			Map<String, Object> mailVo = RequireMgmtController.domainToMap(tbUserGrantVo);
			mailVo.put("MAIL_TYPE", "Grant-Insert");
			grantMailSend(mailVo, request);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbUserAuthTxnSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			System.out.println("e === " + e);
			resultVo = new TbUserAuthTxnSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		return resultVo;
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewDetailUserAuthSubs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request) {
		ModelMap builtModel = viewDetailUserAuthModel(searchVo, request);

		ModelMap finalModel = new ModelMap();
		finalModel.addAllAttributes(builtModel);

		TbUserAuthTxnListVo resultListVo = (TbUserAuthTxnListVo) builtModel.get("resultListVo");

		finalModel.addAttribute("resultList", resultListVo.getTbUserAuthTxnVos());
		finalModel.addAttribute("totalCount", resultListVo.getTotalCount());

		return finalModel;
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewDetailUserAuthSubs.ajax", method = RequestMethod.POST)
	public String viewDetailUserAuth(@RequestBody TbUserAuthTxnVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewDetailUserAuthModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/grantsubsmgmt/viewDetailUserAuthSubs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/grantsubsmgmt/viewDetailUserAuthSubs";
	}

	private ModelMap viewDetailUserAuthModel(@RequestBody TbUserAuthTxnVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbUserAuthTxnListVo resultListVo = null;
		TbUserAuthTxnSubListVo resultSubListVo = null;
		String adminYn = null;
		String ownerYn = null;
		BigInteger grant_seq = null;
		try {
			resultListVo = new TbUserAuthTxnListVo();
			grant_seq = searchVo.getGrantSeq();
			String userId = jwtUtil.getUserId(request);
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if (usrGradeCd != null && usrGradeCd.equals("UR0001")) {
				adminYn = "Y";
			}

			List<CommonCodeVo> userGradeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.USER_GRADE_CD,
					null);
			model.addAttribute("userGradeCds", userGradeCds);

			if (StringUtils.hasText(searchVo.getSuserId())) {
				resultListVo = grantMgmtService.selectDetailUserAuthTxn(searchVo);
			} else {
				resultListVo = new TbUserAuthTxnListVo();
				resultListVo.setTotalCount(0);
			}

			if (searchVo.getGrantSeq() != null) {
				resultSubListVo = grantSubsMgmtService.selectDetailUserAuthTxn(searchVo);
				if (resultSubListVo.getTotalCount() != 0) {
					if (resultSubListVo.getTbUserAuthTxnSubVos().get(0).getSuserId().equals(userId)) {
						ownerYn = "Y";
					} else {
						ownerYn = "N";
					}
				}
			} else {
				resultSubListVo = new TbUserAuthTxnSubListVo();
				resultSubListVo.setTotalCount(0);
			}

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			resultSubListVo = new TbUserAuthTxnSubListVo();
			resultSubListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbUserAuthTxnListVo();
			resultListVo.setTotalCount(0);
			resultSubListVo = new TbUserAuthTxnSubListVo();
			resultSubListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("resultSubListVo", resultSubListVo);
		model.addAttribute("adminYn", adminYn);
		model.addAttribute("ownerYn", ownerYn);
		model.addAttribute("grant_seq", grant_seq);
		return model;
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/viewDeleteGrant.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteGrant(HttpServletRequest request, @RequestBody TbUserGrantVo searchVo) {
		TbUserGrantVo resultVo = null;
		int result = 0;
		try {
			searchVo.setsModifyId(jwtUtil.getUserId(request));
			result = grantSubsMgmtService.deleteGrant(searchVo);
			if (result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = new TbUserGrantVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbUserGrantVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbUserGrantVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	@RequestMapping(value = "/opermgmt/grantsubsmgmt/confirmGrantSub.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo confirmGrantSub(@RequestBody TbUserAuthTxnListVo tbUserAuthTxnListVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbUserGrantVo resultVo = null;
		try {
			resultVo = new TbUserGrantVo();
			BigInteger grantSeq = tbUserAuthTxnListVo.getGrantSeq();
			if (tbUserAuthTxnListVo.getNrequestTypeCd().equals("nod002")) {
				grantSubsMgmtService.updateGrant(tbUserAuthTxnListVo);
				grantMgmtService.insertTbUserAuthTxnVo(tbUserAuthTxnListVo);

				TbUserGrantVo tbUserGrantVo = new TbUserGrantVo();
				tbUserGrantVo.setGrantSeq(grantSeq);
				tbUserGrantVo = grantSubsMgmtService.selectGrantVo(tbUserGrantVo);
				Map<String, Object> mailVo = RequireMgmtController.domainToMap(tbUserGrantVo);
				mailVo.put("MAIL_TYPE", "Grant-Delete");
				grantMailSend(mailVo, request);
			} else if (tbUserAuthTxnListVo.getNrequestTypeCd().equals("nod003")) {
				grantSubsMgmtService.updateGrant(tbUserAuthTxnListVo);

				TbUserGrantVo tbUserGrantVo = new TbUserGrantVo();
				tbUserGrantVo.setGrantSeq(grantSeq);
				tbUserGrantVo = grantSubsMgmtService.selectGrantVo(tbUserGrantVo);
				Map<String, Object> mailVo = RequireMgmtController.domainToMap(tbUserGrantVo);
				mailVo.put("MAIL_TYPE", "Grant-Delete");
				grantMailSend(mailVo, request);
			}

			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbUserGrantVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			System.out.println(e);
			resultVo = new TbUserGrantVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	public void grantMailSend(Map<String, Object> map, HttpServletRequest request) {
		try {
			map.put("TITLE", "IPMS 권한 신청");
			String mail_type = map.get("MAIL_TYPE").toString();
			String progress_status = map.get("nrequestTypeCd").toString();
			SmtpVo smtpVo = new SmtpVo();
			smtpVo.setMailType(mail_type);
			String subject = "";
			if (progress_status.equals("nod001")) {
				subject = "[IPMS 권한 신청 등록 알림]" + map.get("suserNm").toString() + "님으로부터 등록하신 신청사항이 정상적으로 접수되었습니다.";
				smtpVo.setSubject(subject);
			} else if (progress_status.equals("nod002")) {
				subject = "[IPMS 권한 신청 완료 알림]" + map.get("suserNm").toString() + "님으로부터 등록하신 신청사항이 정상적으로 완료되었습니다.";
				smtpVo.setSubject(subject);
			} else if (progress_status.equals("nod003")) {
				subject = "[IPMS 권한 신청 반려 알림]" + map.get("suserNm").toString() + "님으로부터 등록하신 신청사항이 정상적으로 반려되었습니다.";
				smtpVo.setSubject(subject);
			}
			// get html
			String content = smtpUtil.parseHtml(map, request);
			// get user email
			TbUserBasVo searchVo = new TbUserBasVo();
			searchVo.setSuserId(map.get("screateId").toString());
			String userEmail = userMgmtService.selectEmail(searchVo);
			// test
			// userEmail = "91295753@ktfriend.com";
			// set smtpvo
			smtpVo.setToEmail(userEmail);
			smtpVo.setMessage(content);
			smtpVo.setUserID(jwtUtil.getUserId(request));
			smtpUtil.sendMail(smtpVo);

			// get Admin email
			List<ReqAdminEmailVo> AdminEmailVoList = reqBoardService.selectAdminEmailList();
			for (int i = 0; i < AdminEmailVoList.size(); i++) {
				userEmail = AdminEmailVoList.get(i).getsUserEmail();
				smtpVo.setToEmail(userEmail);
				smtpUtil.sendMail(smtpVo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			PrintLogUtil.printLog(e.toString());
		}
	}
}
