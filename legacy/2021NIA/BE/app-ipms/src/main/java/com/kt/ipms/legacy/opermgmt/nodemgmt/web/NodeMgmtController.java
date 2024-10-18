package com.kt.ipms.legacy.opermgmt.nodemgmt.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
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
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.nodemgmt.service.NodeMgmtService;
import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtListVo;
import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.web.RequireMgmtController;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class NodeMgmtController extends CommonController{
	@Autowired
	private AllocMgmtService allocMgmtService;
	
	@Autowired
	private NodeMgmtService nodeMgmtService;
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	@Autowired
	private TacsMgmtService tacsMgmtService;
	
	@Autowired
	private ReqBoardService reqBoardService;
	
	@Autowired
	private AssignMgmtService assignMgmtService;
	
	@Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;
	
	@RequestMapping(value = "/opermgmt/nodemgmt/viewListNode.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListReq(@RequestBody NodeMgmtVo searchVo, ModelMap model, HttpServletRequest request){
		setPagination(searchVo);
		NodeMgmtListVo resultListVo = nodeMgmtService.selectListNodeMgmt(searchVo);
		return createResultList(resultListVo.getNodeMgmtVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/nodemgmt/viewListNode.do", method = RequestMethod.POST)
	public String selectListReq( @ModelAttribute("searchVo") NodeMgmtVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		NodeMgmtVo searchVoClone = new NodeMgmtVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListReqModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/nodemgmt/viewListNode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/nodemgmt/viewListNode";
	}
	private ModelMap selectListReqModel( @ModelAttribute("searchVo") NodeMgmtVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		NodeMgmtListVo resultListVo = null;
		try{
			if(searchVo.getSortOrdr().isEmpty()){
				searchVo.setSortOrdr("DESC");
			}
			if(searchVo.getSortType().isEmpty()){
				searchVo.setSortType("dcreate_dt");
			}
			setPagination(searchVo);
			resultListVo = nodeMgmtService.selectListNodeMgmt(searchVo);
		} catch (ServiceException e){
			resultListVo = new NodeMgmtListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new NodeMgmtListVo();
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
	
	@RequestMapping(value = "/opermgmt/nodemgmt/selectListIpAssignMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListIpAssignMst(HttpServletRequest request, @RequestBody TbIpAssignMstVo searchVo)  {
		setPagination(searchVo);
		TbIpAssignMstListVo resultListVo = assignMgmtService.selectListIpAssignMst(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/nodemgmt/viewInsertNode.ajax", method = RequestMethod.POST)
	public String viewinsertNode(ModelMap model, HttpServletRequest request, @RequestBody TbIpAssignMstVo searchVo)  {
		NodeMgmtVo searchVoClone = new NodeMgmtVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewinsertNodeModel(request, searchVo);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/nodemgmt/viewInsertNode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/nodemgmt/viewInsertNodePop";
	}
	private ModelMap viewinsertNodeModel(HttpServletRequest request, @RequestBody TbIpAssignMstVo searchVo) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipCreateSeqCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipCreateSeqCds", sipCreateSeqCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			/** 계위별 배정 레벨 목록 조회 **/
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			assignLevelCdParamMap.put("startCd", "IA0001");
			assignLevelCdParamMap.put("endCd", "IA0006");
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD, assignLevelCdParamMap);
			searchVo.setSearchBgnCd(sassignLevelCds.get(0).getCode());
			searchVo.setSearchEndCd(sassignLevelCds.get(sassignLevelCds.size()-1).getCode());
			/** 배정 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
				sloadFlg = "T";
			}else{
				sloadFlg = "F";
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			setPagination(searchVo);
			if (sloadFlg.equals("T")){
				resultListVo = new TbIpAssignMstListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = assignMgmtService.selectListIpAssignMst(searchVo);
				
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		
		TbLvlBasVo tsearchVo = new TbLvlBasVo();
		TbLvlBasListVo upSvcLineListVo = orgMgmtAdapterService.selectListSvcLine(tsearchVo);
		TbLvlBasListVo upCenterListVo = new TbLvlBasListVo();//Codeeyes-Urgent-내용 없는 문장 제한
		TbLvlBasListVo upNodeListVo = new TbLvlBasListVo();//Codeeyes-Urgent-내용 없는 문장 제한
		model.addAttribute("upSvcLineListVo", upSvcLineListVo);
		model.addAttribute("upCenterListVo", upCenterListVo);
		model.addAttribute("upNodeListVo", upNodeListVo);
		
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	@RequestMapping(value="/opermgmt/nodemgmt/insertNode.json", method=RequestMethod.POST)
	@ResponseBody
	public NodeMgmtVo insertNode(HttpServletRequest request, @RequestBody NodeMgmtVo nodeMgmtVo) {
		NodeMgmtVo resultVo = null;
		int result = 0;
		try {
			String userId = jwtUtil.getUserId(request);
			String userNm = jwtUtil.getUserNm(request);
			nodeMgmtVo.setsCreateId(userId);
			nodeMgmtVo.setsUserNm(userNm);
			NodeMgmtVo tempVo = new NodeMgmtVo();
			tempVo.setNipAssignMstSeq(nodeMgmtVo.getNipAssignMstSeq());
			tempVo.setProgressStatus("nod001");
			result = nodeMgmtService.selectNodeCount(tempVo);
			
			if(result == 0){
				resultVo = nodeMgmtService.insertNode(nodeMgmtVo);
				NodeMgmtVo tempNodeVo = nodeMgmtService.selectNode(resultVo);
				Map<String,Object> map = RequireMgmtController.domainToMap(tempNodeVo);
				map.put("MAIL_TYPE","Node-Insert");
				nodeMailSend(map ,request);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo = new NodeMgmtVo();
				resultVo.setCommonMsg("duplicate");
			}
			
			
		} catch(ServiceException e) {
			resultVo = new NodeMgmtVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			resultVo = new NodeMgmtVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
		}
		return resultVo;
	
	}

	@RequestMapping(value = "/opermgmt/nodemgmt/viewDetailNode.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectNode(@RequestBody NodeMgmtVo searchVo, ModelMap model, HttpServletRequest request)  {
		NodeMgmtVo resultVo = nodeMgmtService.selectNode(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/nodemgmt/viewDetailNode.ajax", method = RequestMethod.POST)
	public String selectNode(@RequestBody NodeMgmtVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		NodeMgmtVo searchVoClone = new NodeMgmtVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectNodeModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/nodemgmt/viewDetailNode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/nodemgmt/viewDetailNodePop";
	}
	private ModelMap selectNodeModel(@RequestBody NodeMgmtVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		NodeMgmtVo resultVo = null;
		String adminYn = null;
		String ownerYn = null;
		try {
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			resultVo = nodeMgmtService.selectNode(searchVo);
			String userId = jwtUtil.getUserId(request);
//			if(adminYn == "Y"){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(adminYn.equals("Y")){
				ownerYn = "Y";
			}else{
				if(resultVo.getsCreateId().equals(userId)){
					ownerYn = "Y";
				}else{
					ownerYn = "N";
				}
			}
			model.addAttribute("ownerYn",ownerYn);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("adminYn", adminYn);
		return model;
	}
	
	
	@RequestMapping(value="/opermgmt/nodemgmt/viewfnDeleteAlcIPMst.json", method=RequestMethod.POST)
	@ResponseBody
	public IpAllocOperMstListVo deleteAlcIPMst(HttpServletRequest request,@RequestBody IpAllocOperMstVo searchVo){
		IpAllocOperMstListVo resultListVo = null;
		try {
			resultListVo = allocMgmtService.selectListIpAllocDetail(searchVo);
			IpAllocMstComplexVo tempObject = new IpAllocMstComplexVo();
			tempObject.setSrcIpAllocMstVo(searchVo);
			String userId = jwtUtil.getUserId(request);
			tempObject.setMenuType(searchVo.getMenuType());
			tempObject.setScreateId(userId);
			tempObject.setSmodifyId(userId);
			nodeMgmtService.deleteListAllocIPMst(resultListVo, tempObject, userId);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	@RequestMapping(value="/opermgmt/nodemgmt/viewDeleteNode.json", method=RequestMethod.POST)
	@ResponseBody
	public NodeMgmtVo deleteNode(HttpServletRequest request, @RequestBody NodeMgmtVo searchVo){
		NodeMgmtVo resultVo = null;
		int result = 0;
		try {
			searchVo.setsModifyId(jwtUtil.getUserId(request));
			result = nodeMgmtService.deleteNode(searchVo);
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = new NodeMgmtVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new NodeMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new NodeMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value="/opermgmt/nodemgmt/viewCancelNode.json", method=RequestMethod.POST)
	@ResponseBody
	public NodeMgmtVo cancelNode(HttpServletRequest request, @RequestBody NodeMgmtVo searchVo){
		NodeMgmtVo resultVo = null;
		int result = 0;
		try {
			searchVo.setsModifyId(jwtUtil.getUserId(request));
			result = nodeMgmtService.cancelNode(searchVo);
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = new NodeMgmtVo();
			NodeMgmtVo tempNodeVo = nodeMgmtService.selectNode(searchVo);
			Map<String,Object> map = RequireMgmtController.domainToMap(tempNodeVo);
			map.put("MAIL_TYPE","Node-Delete");
			nodeMailSend(map ,request);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new NodeMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new NodeMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/nodemgmt/confirmNode.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpAssignMstVo confirmNode(@RequestBody TbIpAssignMstComplexVo tbIpAssignMstComplexVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignMstVo resultVo = null;
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null 
				|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null || tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			NodeMgmtVo tempNodeVo = nodeMgmtService.selectNode(tbIpAssignMstComplexVo.getNodeMgmtVo());
			resultVo = nodeMgmtService.selectAssignMst(tbIpAssignMstComplexVo.getDestIpAssignMstVos().get(0));
			
			Boolean validateFlag = validateData(request,resultVo,tempNodeVo);
			
			if(validateFlag){
				TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
				TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
				tbLvlBasVo.setSsvcLineTypeCd(srcIpAssignMstVo.getSsvcLineTypeCd());
				tbLvlBasVo.setSsvcGroupCd(srcIpAssignMstVo.getSsvcGroupCd());
				tbLvlBasVo.setSsvcObjCd(srcIpAssignMstVo.getSsvcObjCd());
				BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
				srcIpAssignMstVo.setNlvlMstSeq(nlvlMstSeq);
				srcIpAssignMstVo.setSmodifyId(jwtUtil.getUserId(request));
				tbIpAssignMstComplexVo.setScreateId(jwtUtil.getUserId(request));
				tbIpAssignMstComplexVo.setSmodifyId(jwtUtil.getUserId(request));
				allocMgmtService.updateListAsgnIPMst(tbIpAssignMstComplexVo);
				resultVo = new TbIpAssignMstVo();
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
				tbIpAssignMstComplexVo.getNodeMgmtVo().setsModifyId(jwtUtil.getUserId(request));
				nodeMgmtService.comfirmDcompleteDate(tbIpAssignMstComplexVo.getNodeMgmtVo());
				Map<String,Object> map = RequireMgmtController.domainToMap(tempNodeVo);
				map.put("MAIL_TYPE","Node-Delete");
				nodeMailSend(map ,request);
			}else{
				resultVo.setCommonMsg("이전신청 접수 후 IP블록의 데이터가 변경되었습니다.");
			}
			
			
			
		} catch (ServiceException e) {
			resultVo = new TbIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	public Boolean validateData(HttpServletRequest request, TbIpAssignMstVo resultVo, NodeMgmtVo tempNodeVo){
		Boolean result = true;
		
		
		if(!resultVo.getSassignLevelCd().equals(tempNodeVo.getSassignLevelCd())){
			result = false;
		}
		
		if(!resultVo.getSassignTypeCd().equals(tempNodeVo.getSassignTypeCd())){
			result = false;
		}
		
		TbLvlBasVo tempLvlBasVo = new TbLvlBasVo();
		tempLvlBasVo.setSsvcLineTypeCd(tempNodeVo.getBeforeSsvcLineTypeCd());
		tempLvlBasVo.setSsvcGroupCd(tempNodeVo.getBeforeSsvcGroupCd());
		tempLvlBasVo.setSsvcObjCd(tempNodeVo.getBeforeSsvcObjCd());
		BigInteger tempnlvlMstSeq = jwtUtil.getLvlMstSeq(request, tempLvlBasVo);
		
		if(!resultVo.getNlvlMstSeq().equals(tempnlvlMstSeq)){
			result = false;
		}
		return result;
	}
	
	public void nodeMailSend(Map<String,Object> map, HttpServletRequest request){
		try {
			map.put("TITLE", "IPMS 노드 이전신청");
			String mail_type = map.get("MAIL_TYPE").toString();
			String progress_status = map.get("progressStatus").toString();
			SmtpVo smtpVo = new SmtpVo();
			smtpVo.setMailType(mail_type);
			String subject = "";
			if(progress_status.equals("nod001")){
				subject = "[IPMS 노드 이전신청 등록 알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 신청사항이 정상적으로 접수되었습니다.";
				smtpVo.setSubject(subject);
			}else if(progress_status.equals("nod002")){
				subject = "[IPMS 노드 이전신청 완료 알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 신청사항이 정상적으로 완료되었습니다.";
				smtpVo.setSubject(subject);
			}else if(progress_status.equals("nod003")){
				subject = "[IPMS 노드 이전신청 반려 알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 신청사항이 정상적으로 반려되었습니다.";
				smtpVo.setSubject(subject);
			}
			//get html
			String content = smtpUtil.parseHtml(map, request);
			//get user email
			TbUserBasVo searchVo = new TbUserBasVo();
			searchVo.setSuserId(map.get("sCreateId").toString());
			String userEmail = userMgmtService.selectEmail(searchVo);
			//test
//			userEmail = "91295753@ktfriend.com"; 
			
			//set smtpvo
			smtpVo.setToEmail(userEmail);
			smtpVo.setMessage(content);
			smtpVo.setUserID(jwtUtil.getUserId(request));
			smtpUtil.sendMail(smtpVo);
			
			//get Admin email
			List<ReqAdminEmailVo> reqAdminEmailVoList = reqBoardService.selectAdminEmailList();
			for (int i = 0; i < reqAdminEmailVoList.size(); i++) {
			    userEmail = reqAdminEmailVoList.get(i).getsUserEmail();
				smtpVo.setToEmail(userEmail);
				smtpUtil.sendMail(smtpVo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/ipmgmt/nodemgmt/viewListNodeExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo selectListNodeExcel(@ModelAttribute("searchVo") NodeMgmtVo searchVo ,HttpServletRequest request, HttpServletResponse response)  {
		FileVo resultVo = new FileVo();
		try {
			setPagination(searchVo);
			NodeMgmtListVo resultListVo = nodeMgmtService.selectListNodeMgmtExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("번호|getSeq");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("변경전 서비스망|getBeforeSsvcLineTypeNm");
			mappingList.add("변경전 본부|getBeforeSsvcGroupNm");
			mappingList.add("변경전 노드|getBeforeSsvcObjNm");
			mappingList.add("변경후 서비스망|getAfterSsvcLineTypeNm");
			mappingList.add("변경후 본부|getAfterSsvcGroupNm");
			mappingList.add("변형후 노드|getAfterSsvcObjNm");
			mappingList.add("신청자|getsUserNm");
			mappingList.add("신청일시|getdCreateDt");
			mappingList.add("처리일시|getdCompleteDt");
			mappingList.add("진행상태|getProgressStatusNm");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getNodeMgmtVos(), mappingList, request);
			
			if (org.springframework.util.StringUtils.hasText(fileName)) {
				resultVo.setFileName(fileName);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00050");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultVo;
	}
	
	@RequestMapping(value = "opermgmt/nodemgmt/selectAuthCenterList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasListVo selectAuthCenterList(@RequestBody TbLvlBasVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbLvlBasListVo resultListVo = null;
		try{
			resultListVo = orgMgmtAdapterService.selectlistCenter(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	@RequestMapping(value = "opermgmt/nodemgmt/selectAuthNodeList.json", method = RequestMethod.POST)
	@ResponseBody
	public TbLvlBasListVo selectAuthNodeList(@RequestBody TbLvlBasVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbLvlBasListVo resultListVo = null;
		try{
			resultListVo = orgMgmtAdapterService.selectlistNode(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbLvlBasListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	@RequestMapping(value = "/opermgmt/nodemgmt/viewfnSelectNode.ajax", method = RequestMethod.POST)
	@ResponseBody
	public TbIpAssignMstVo confirmNode(@RequestBody TbIpAssignMstVo tbIpAssignMstVo, 
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignMstVo resultVo = null;
		try {
			TbIpAssignMstVo tempVo = nodeMgmtService.selectAssignMst(tbIpAssignMstVo);
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			List<TbLvlMstVo> tbLvlMstVo = resultSeqList.getTbLvlMstVos();
			
			resultVo = new TbIpAssignMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
			for(TbLvlMstVo data: tbLvlMstVo){
				if(data.getNlvlSeq().equals(tempVo.getNlvlMstSeq())){
					resultVo.setCommonMsg("insert");
					break;
				}
			}
			
		}catch (ServiceException e) {
			resultVo = new TbIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
		
	}
	
}
