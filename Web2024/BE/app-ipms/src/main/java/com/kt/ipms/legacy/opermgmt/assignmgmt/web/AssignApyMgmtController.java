package com.kt.ipms.legacy.opermgmt.assignmgmt.web;


import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.opermgmt.assignmgmt.service.AssignApyMgmtService;
import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstListVo;
import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class AssignApyMgmtController extends CommonController{
	
	@Autowired
	AssignApyMgmtService  assignApyMgmtService;
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	@Autowired
	private TacsMgmtService tacsMgmtService;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ReqBoardService reqBoardService;
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewListAssignApyTxn.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListAssignApyTxn(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, 
			HttpServletRequest request) {
		TbRequestAssignMstListVo resultListVo = assignApyMgmtService.selectListTbRequestAssignMst(searchVo);
		return createResultList(resultListVo.getTbRequestAssignMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/assignmgmt/viewListAssignApyTxn.do", method = RequestMethod.POST)
	public String viewListAssignApyTxn(@ModelAttribute("searchVo") TbRequestAssignMstVo searchVo, ModelMap model, 
			HttpServletRequest request, HttpServletResponse response) {
		
		TbRequestAssignMstVo searchVoClone = new TbRequestAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListAssignApyTxnModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/assignmgmt/viewListAssignApyTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/assignmgmt/viewListAssignApyTxn";
	}
	private ModelMap viewListAssignApyTxnModel(@ModelAttribute("searchVo") TbRequestAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAssignMstListVo resultListVo = null;
		try{
			
			List<CommonCodeVo> requestAssignTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SREQUEST_ASSIGN_TYPE_CD, searchVo);
			model.addAttribute("requestAssignTypeCds", requestAssignTypeCds);
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
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
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			setPagination(searchVo);
			resultListVo = assignApyMgmtService.selectListTbRequestAssignMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbRequestAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbRequestAssignMstListVo();
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
	
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewListAssignApyTxnExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo viewListAssignApyTxnExcel(@ModelAttribute("searchVo") TbRequestAssignMstVo searchVo, ModelMap model, 
			HttpServletRequest request,HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try{
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(searchVo.getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
						}
					} else {
						nodeListVo = new TbLvlBasListVo();
					}
				}
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					TbLvlBasVo searchCenterVo = new TbLvlBasVo();
					searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					searchVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
					centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
					if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
						searchVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
						nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
						if (StringUtils.hasText(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd())) {
							searchVo.setSsvcObjCd(nodeListVo.getTbLvlBasVos().get(0).getSsvcObjCd());
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
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			
			TbRequestAssignMstListVo	resultListVo = assignApyMgmtService.selectListTbRequestAssignMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("번호|getNrequestAssignSeq");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("제목|getStitle");
			mappingList.add("신청자|getSapyUserNm");
			mappingList.add("신청일시|getDapyDt");
			mappingList.add("상태|getSrequestAssignTypeNm");
			mappingList.add("처리일시|getDtrtDt");
			mappingList.add("배정IP|getSassigncontents");
			mappingList.add("배정IP개수|getNassignIpCnt");
			String fileName = excelUtil.createExcelFile(resultListVo.getTbRequestAssignMstVos(), mappingList, request);
			if (StringUtils.hasText(fileName)) {
				resultVo.setFileName(fileName);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00050");
			}
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewDetailAssignApyTxn.model", method = RequestMethod.POST)
	public ModelMap viewDetailAssignApyTxn(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRequestAssignMstVo resultVo = assignApyMgmtService.selectTbRequestAssignMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/assignmgmt/viewDetailAssignApyTxn.ajax", method = RequestMethod.POST)
	public String viewDetailAssignApyTxn(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAssignMstVo searchVoClone = new TbRequestAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailAssignApyTxnModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/assignmgmt/viewDetailAssignApyTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/assignmgmt/viewDetailAssignApyTxn";
	}
	private ModelMap viewDetailAssignApyTxnModel(@RequestBody TbRequestAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAssignMstVo resultVo = null;
		try{
			resultVo = assignApyMgmtService.selectTbRequestAssignMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewAssignIpApyPreCheck.model", method = RequestMethod.POST)
	public ModelMap viewAssignIpApyPreCheck(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRequestAssignMstListVo resultListVo = assignApyMgmtService.selectListPreApyAssign(searchVo);	
		return createResultList(resultListVo.getTbRequestAssignMstVos(), resultListVo.getTotalCount());
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewAssignIpApyPreCheck.ajax", method = RequestMethod.POST)
	public String viewAssignIpApyPreCheck(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAssignMstVo searchVoClone = new TbRequestAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewAssignIpApyPreCheckModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/assignmgmt/viewAssignIpApyPreCheck.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/assignmgmt/viewAssignIpApyPreCheck";
	}
	
	private ModelMap viewAssignIpApyPreCheckModel(@RequestBody TbRequestAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAssignMstListVo resultListVo = null;
		try{
			
			String userGrade = jwtUtil.getUserGradeCd(request);
			
			if(userGrade.equals(CommonCodeUtil.USER_GRADE_N) || userGrade.equals(CommonCodeUtil.USER_GRADE_C) || userGrade.equals(CommonCodeUtil.USER_GRADE_O)){
				TbLvlMstVo searchSeqVo = new TbLvlMstVo();		
				TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
				searchVo.setLvlMstSeqListVo(resultSeqList);
			}
			
			resultListVo = assignApyMgmtService.selectListPreApyAssign(searchVo);			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e){
			resultListVo = new TbRequestAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbRequestAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewInsertAssignApyTxn.model", method = RequestMethod.POST)
	public ModelMap viewInsertAssignApyTxn(@RequestBody TbRequestAssignMstVo tbRequestAssignMstVo, ModelMap model, HttpServletRequest request) {
		TbRequestAssignMstVo resultVo = new TbRequestAssignMstVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/assignmgmt/viewInsertAssignApyTxn.ajax", method = RequestMethod.POST)
	public String viewInsertAssignApyTxn(@RequestBody TbRequestAssignMstVo tbRequestAssignMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAssignMstVo searchVoClone = new TbRequestAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(tbRequestAssignMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertAssignApyTxnModel(tbRequestAssignMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/assignmgmt/viewInsertAssignApyTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/assignmgmt/viewInsertAssignApyTxn";
	}
	private ModelMap viewInsertAssignApyTxnModel(@RequestBody TbRequestAssignMstVo tbRequestAssignMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAssignMstVo resultVo = null;
		try{
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
				centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
				if (StringUtils.hasText(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd())) {
					searchCenterVo.setSsvcGroupCd(centerListVo.getTbLvlBasVos().get(0).getSsvcGroupCd());
					nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				} else {
					nodeListVo = new TbLvlBasListVo();
				}
			} else {
				centerListVo = new TbLvlBasListVo();
				nodeListVo = new TbLvlBasListVo();
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			
			resultVo = new TbRequestAssignMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/assignmgmt/insertAssignApyTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRequestAssignMstVo insertAssignApyTxn(@RequestBody  TbRequestAssignMstVo tbRequestAssignMstVo ,  HttpServletRequest request, HttpServletResponse response){
		TbRequestAssignMstVo resultVo = new TbRequestAssignMstVo();
		String msgDesc = null;
		try {
			
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			tbLvlBasVo.setSsvcLineTypeCd(tbRequestAssignMstVo.getSsvcLineTypeCd());
			tbLvlBasVo.setSsvcGroupCd(tbRequestAssignMstVo.getSsvcGroupCd());
			tbLvlBasVo.setSsvcObjCd(tbRequestAssignMstVo.getSsvcObjCd());
			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
			tbRequestAssignMstVo.setNlvlMstSeq(nlvlMstSeq);
			
			assignApyMgmtService.insertTbRequestAssignMst(tbRequestAssignMstVo);
			msgDesc = CommonCodeUtil.SUCCESS_MSG;
			msgDesc = SendMail("IpAssign-Req-User", tbRequestAssignMstVo, request);		// IP배정신청 등록시(요청자)
			msgDesc = SendMail("IpAssign-Req-Admin", tbRequestAssignMstVo, request);		// IP배정신청 등록시(담당자)
			
			resultVo.setCommonMsg(msgDesc);
			
		}catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/viewUpdateAssignApyTxn.model", method = RequestMethod.POST)
	public ModelMap viewUpdateAssignApyTxn(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRequestAssignMstVo resultVo = assignApyMgmtService.selectTbRequestAssignMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/assignmgmt/viewUpdateAssignApyTxn.ajax", method = RequestMethod.POST)
	public String viewUpdateAssignApyTxn(@RequestBody TbRequestAssignMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAssignMstVo searchVoClone = new TbRequestAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateAssignApyTxnModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/assignmgmt/viewUpdateAssignApyTxn.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/assignmgmt/viewUpdateAssignApyTxn";
	}
	private ModelMap viewUpdateAssignApyTxnModel(@RequestBody TbRequestAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAssignMstVo resultVo = null;
		try{
			resultVo = assignApyMgmtService.selectTbRequestAssignMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/assignmgmt/updateAssignApyTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRequestAssignMstVo updateAssignApyTxn(@RequestBody  TbRequestAssignMstVo tbRequestAssignMstVo ,  HttpServletRequest request, HttpServletResponse response){
		TbRequestAssignMstVo resultVo = new TbRequestAssignMstVo();
		String msgDesc = null;
		try {			
			
			assignApyMgmtService.updateTbRequestAssignMst(tbRequestAssignMstVo);
			msgDesc = CommonCodeUtil.SUCCESS_MSG;
			
			if("RS0302".equals(tbRequestAssignMstVo.getSrequestAssignTypeCd()))  { // 승인
				msgDesc = SendMail("IpAssign-Aprv", tbRequestAssignMstVo, request);
			} else if("RS0303".equals(tbRequestAssignMstVo.getSrequestAssignTypeCd()))  { // 반려
				msgDesc = SendMail("IpAssign-Reject", tbRequestAssignMstVo, request);
			}
			
			resultVo.setCommonMsg(msgDesc);
		}catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/assignmgmt/deleteAssignApyTxn.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRequestAssignMstVo deleteAssignApyTxn(@RequestBody  TbRequestAssignMstVo tbRequestAssignMstVo ,  HttpServletRequest request, HttpServletResponse response){
		TbRequestAssignMstVo resultVo =new TbRequestAssignMstVo();
		
		try {	
			
			assignApyMgmtService.deleteTbRequestAssignMst(tbRequestAssignMstVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * Mail Value Setting
	 * @param type
	 * @param tbWhoisModifyVo
	 * @return
	 */
	public String SendMail (String mailType, TbRequestAssignMstVo tbRequestAssignMstVo, HttpServletRequest request) {
		
		String result = null;
		
		String toEmail = "";
		String subject = "";
		String content = "";
		
		// 요청자 정보
		String userName = "";
		String userOrg = "";
		String userEmail = "";

		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {

			SmtpVo smtpVo = new SmtpVo();
			
			TbUserBasVo searchVo = new TbUserBasVo();
			searchVo.setSuserId(tbRequestAssignMstVo.getScreateId());
			
			userEmail = userMgmtService.selectEmail(searchVo);					// 요청자 메일주소
			List<ReqAdminEmailVo> reqAdminEmailVoList = reqBoardService.selectAdminEmailList(); //담당자 주소
			
			userName = jwtUtil.getUserNm(request);
			userOrg = jwtUtil.getUserDeptOrgNm(request);
			
			Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
			
			map.put("MAIL_TYPE", mailType);
			
			if(mailType.equals("IpAssign-Req-User")) {					// IP 배정신청 등록 (요청자)
				// 요청자에게 발송할 메일
				subject = "[IPMS IP배정신청 완료] IP배정신청이 정상적으로 접수되었습니다.";
				map.put("TITLE", subject);
				
				content = smtpUtil.parseHtml(map, request);
				smtpVo.setSubject(subject);
				smtpVo.setMessage(content);
				smtpVo.setUserID(jwtUtil.getUserId(request));
				if(isRun) {
					toEmail = userEmail;	
					smtpVo.setToEmail(toEmail);
					
					SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
					result = resultVo.getResult();
				}  else {
					for (int i = 0; i < reqAdminEmailVoList.size(); i++) {
						toEmail = reqAdminEmailVoList.get(i).getsUserEmail();
						smtpVo.setToEmail(toEmail);
						SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
						result = resultVo.getResult();
					}
					
				}
			} else if(mailType.equals("IpAssign-Req-Admin")) {					// IP 배정신청 등록 (관리자)
				// 담당자에게 발송할 메일
				subject = "[IPMS IP배정신청 접수 알림] " + userName + "로부터 IP배정신청이 등록되었습니다.";
				map.put("TITLE", subject);
				content = smtpUtil.parseHtml(map, request);
				
				smtpVo.setSubject(subject);
				smtpVo.setMessage(content);
				smtpVo.setUserID(jwtUtil.getUserId(request));
				
				for (int i = 0; i < reqAdminEmailVoList.size(); i++) {
					toEmail = reqAdminEmailVoList.get(i).getsUserEmail();
					smtpVo.setToEmail(toEmail);
					SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
					result = resultVo.getResult();
				}
			
			} else if(mailType.equals("IpAssign-Aprv") && tbRequestAssignMstVo.getSrequestAssignTypeCd().equals("RS0302")) { 		// 승인 시
				// 요청자에게 발송할 메일
				subject = "[IPMS IP배정 완료] 관리자로부터 IP배정이 완료되었습니다.";
				
				TbRequestAssignMstVo selVo =  assignApyMgmtService.selectTbRequestAssignMst(tbRequestAssignMstVo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				map.put("SCONTENTS", selVo.getScontents());	// 신청내용
				map.put("DTRT_DT", format.format(selVo.getDtrtDt()));	// 처리일시
				map.put("STRT_CONTENTS", selVo.getStrtContents()); // 처리내용
				map.put("TITLE", subject);
				map.put("SASSIGNCONTENTS", selVo.getSassigncontents());
				content = smtpUtil.parseHtml(map, request);
				
				smtpVo.setSubject(subject);
				smtpVo.setMessage(content);
				smtpVo.setUserID(jwtUtil.getUserId(request));
				if(isRun) {
					toEmail = userEmail;	
					smtpVo.setToEmail(toEmail);
					SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
					result = resultVo.getResult();
				}  else {
					//admin
					for (int i = 0; i < reqAdminEmailVoList.size(); i++) {
						toEmail = reqAdminEmailVoList.get(i).getsUserEmail();
						smtpVo.setToEmail(toEmail);
						SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
						result = resultVo.getResult();
					}
				}
				
				
				
			} else if(mailType.equals("IpAssign-Reject") && tbRequestAssignMstVo.getSrequestAssignTypeCd().equals("RS0303")) { 		// 반려 시
				// 요청자에게 발송할 메일
				subject = "[IPMS IP배정 반려] 관리자로부터 IP배정이 반려되었습니다.";
				
				TbRequestAssignMstVo selVo =  assignApyMgmtService.selectTbRequestAssignMst(tbRequestAssignMstVo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				map.put("SCONTENTS", selVo.getScontents());	// 신청내용
				map.put("DTRT_DT", format.format(selVo.getDtrtDt()));	// 처리일시
				map.put("STRT_CONTENTS", selVo.getStrtContents()); // 처리내용
				map.put("TITLE", subject);
				map.put("SASSIGNCONTENTS", selVo.getSassigncontents());
				content = smtpUtil.parseHtml(map, request);
				
				smtpVo.setSubject(subject);
				smtpVo.setMessage(content);
				smtpVo.setUserID(jwtUtil.getUserId(request));
				if(isRun) {
					toEmail = userEmail;	
					smtpVo.setToEmail(toEmail);
					SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
					result = resultVo.getResult();
				}  else {
					for (int i = 0; i < reqAdminEmailVoList.size(); i++) {
						toEmail = reqAdminEmailVoList.get(i).getsUserEmail();
						smtpVo.setToEmail(toEmail);
						SmtpVo resultVo = smtpUtil.sendMail(smtpVo);
						result = resultVo.getResult();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
	
		
		return result;
	}

}
