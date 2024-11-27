package com.kt.ipms.legacy.opermgmt.asmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.service.AsMgmtService;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstListVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class AsMgmtController extends CommonController{
	
	@Autowired
	private AsMgmtService asMgmtService;
	
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
	
	/**
	 * 사설AS 관리 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/viewListPrivateAs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListPrivateAs(@RequestBody TbRequestAsApyTxnVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbRequestAsApyTxnListVo resultListVo = asMgmtService.viewListPrivateAs(searchVo);
		return createResultList(resultListVo.getTbRequestAsApyTxnVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/asmgmt/viewListPrivateAs.do", method = RequestMethod.POST)
	public String viewListPrivateAs(@ModelAttribute("searchVo") TbRequestAsApyTxnVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListPrivateAsModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/asmgmt/viewListPrivateAs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/asmgmt/viewListPrivateAs";
	}
	private ModelMap viewListPrivateAsModel(@ModelAttribute("searchVo") TbRequestAsApyTxnVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAsApyTxnListVo resultListVo = null;
		try{
			List<CommonCodeVo> srequestAsTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SREQUEST_AS_TYPE_CD, searchVo);
			model.addAttribute("srequestAsTypeCds", srequestAsTypeCds);
			
			String userGrade  = jwtUtil.getUserGradeCd(request);
			String userId  =  jwtUtil.getUserId(request);
			//AS관리 목록 리스트 표기시 관리자제외 모든 사용자는 자신글만 조회
			if(!userGrade.equals(CommonCodeUtil.USER_GRADE_A)) {
				searchVo.setScreateId(userId);
			}
			
			/** 정열조건 세팅 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("DCREATE_DT");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr("DESC");
			}
			
			setPagination(searchVo);
			resultListVo = asMgmtService.viewListPrivateAs(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbRequestAsApyTxnListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbRequestAsApyTxnListVo();
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
	
	
	@RequestMapping(value = "/opermgmt/asmgmt/viewListPrivateAsExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListPrivateAsExcel(@RequestBody TbRequestAsApyTxnVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try{
			
			
			String userGrade  = jwtUtil.getUserGradeCd(request);
			String userId  =  jwtUtil.getUserId(request);
			//AS관리 목록 리스트 표기시 관리자제외 모든 사용자는 자신글만 조회
			if(!userGrade.equals(CommonCodeUtil.USER_GRADE_A)) {
				searchVo.setScreateId(userId);
			}
			
			/** 정열조건 세팅 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("DCREATE_DT");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr("DESC");
			}
			
			
			TbRequestAsApyTxnListVo  resultListVo = asMgmtService.viewListPrivateAsExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("AS번호|getNrequestAsSeq");
			mappingList.add("고객명|getSrequestAsCtm");
			mappingList.add("신청일|getDcreateDt");
			mappingList.add("요청자|getScreateNm");
			mappingList.add("노드1 명|getSrequestAsObjNm1");
			mappingList.add("노드1 전용회선번호|getSrequestAsObjLlnum1");
			mappingList.add("노드2 명|getSrequestAsObjNm2");
			mappingList.add("노드2 전용회선번호|getSrequestAsObjLlnum2");
			mappingList.add("처리일시|getDapvDt");
			mappingList.add("상태|getSrequestAsTypeNm");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbRequestAsApyTxnVos(), mappingList, request);
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	/**
	 * 사설AS 상세화면
	 * @param tbRequestAsApyTxnVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "opermgmt/asmgmt/viewDetailPrivateAs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailPrivateAs(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, ModelMap model, HttpServletRequest request) {
		TbRequestAsApyTxnVo resultVo = asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "opermgmt/asmgmt/viewDetailPrivateAs.ajax", method = RequestMethod.POST)
	public String viewDetailPrivateAs(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(tbRequestAsApyTxnVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailPrivateAsModel(tbRequestAsApyTxnVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/asmgmt/viewDetailPrivateAs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/asmgmt/viewDetailPrivateAs";
	}
	private ModelMap viewDetailPrivateAsModel(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAsApyTxnVo resultVo = null;
		try {
			resultVo = asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설AS 등록 화면
	 * @param
	 * @param model
	 * @param request
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/viewInsertPrivateAs.ajax", method = RequestMethod.POST)
	public String viewInsertPrivateAs(ModelMap model, HttpServletRequest request) {
		return "opermgmt/asmgmt/viewInsertPrivateAs";
	}
	
	/**
	 * 사설AS 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/insertPrivateAs.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertPrivateAs(@RequestBody TbRequestAsApyTxnVo insertVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsApyTxnVo resultVo = null;
		String msgDesc = null;
		
		try {
			resultVo = asMgmtService.insertPrivateAs(insertVo);
			msgDesc = CommonCodeUtil.SUCCESS_MSG;
			msgDesc = SendMail("PrivateAs-Req-User", insertVo, request);		// 사설AS신청 등록시(요청자)
			msgDesc = SendMail("PrivateAs-Req-Admin", insertVo, request);		// 사설AS신청 등록시(담당자)
			
			resultVo.setCommonMsg(msgDesc);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 사설AS 수정 화면
	 * @param tbRequestAsApyTxnVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "opermgmt/asmgmt/viewUpdatePrivateAs.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdatePrivateAs(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, ModelMap model, HttpServletRequest request) {
		TbRequestAsApyTxnVo resultVo = asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "opermgmt/asmgmt/viewUpdatePrivateAs.ajax", method = RequestMethod.POST)
	public String viewUpdatePrivateAs(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(tbRequestAsApyTxnVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdatePrivateAsModel(tbRequestAsApyTxnVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/asmgmt/viewUpdatePrivateAs.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/asmgmt/viewUpdatePrivateAs";
	}
	private ModelMap viewUpdatePrivateAsModel(@RequestBody TbRequestAsApyTxnVo tbRequestAsApyTxnVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAsApyTxnVo resultVo = null;
		try {
			resultVo = asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설AS 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/updatePrivateAs.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updatePrivateAs(@RequestBody TbRequestAsApyTxnVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsApyTxnVo resultVo = null;
		try {
			resultVo = asMgmtService.updatePrivateAs(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * 사설AS 삭제(신청취소)
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/deletePrivateAs.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deletePrivateAs(@RequestBody TbRequestAsApyTxnVo deleteVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsApyTxnVo resultVo = null;
		try{
			resultVo = asMgmtService.deletePrivateAs(deleteVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	/**
	 * 사설AS 할당,반납
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/updateNrequestAsSeqYn.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateNrequestAsSeqYn(@RequestBody TbRequestAsApyTxnVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsApyTxnVo resultVo = null;
		String msgDesc = null;
		try {
			resultVo = asMgmtService.updateNrequestAsSeqYn(updateVo);
			msgDesc = CommonCodeUtil.SUCCESS_MSG;
			
			if("RS0202".equals(updateVo.getSrequestAsTypeCd()))  { // 승인
				msgDesc = SendMail("PrivateAs-Aprv", updateVo, request);
			} else if("RS0203".equals(updateVo.getSrequestAsTypeCd())) {		// 반려
				msgDesc = SendMail("PrivateAs-Reject", updateVo, request);
			} 
			
			resultVo.setCommonMsg(msgDesc);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	/**
	 * 사설AS 이력 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/insertAsHist.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertAsHist(@RequestBody TbRequestAsApyTxnVo insertVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsApyTxnVo resultVo = null;
		try {
			resultVo = asMgmtService.insertAsHist(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsApyTxnVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	
	/**
	 * 사설AS 이력 목록 리스트
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/viewListAsHist.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListAsHist(@RequestBody TbRequestAsMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbRequestAsMstListVo resultListVo = asMgmtService.viewListAsHist(searchVo);
		return createResultList(resultListVo.getTbRequestAsMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/asmgmt/viewListAsHist.do", method = RequestMethod.POST)
	public String viewListAsHist(@ModelAttribute("searchVo") TbRequestAsMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListAsHistModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/asmgmt/viewListAsHist.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/asmgmt/viewListAsHist";
	}
	private ModelMap viewListAsHistModel(@ModelAttribute("searchVo") TbRequestAsMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAsMstListVo resultListVo = null;
		try{
			List<CommonCodeVo> srequestAsTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SREQUEST_AS_TYPE_CD, searchVo);
			model.addAttribute("srequestAsTypeCds", srequestAsTypeCds);
			
			setPagination(searchVo);
			resultListVo = asMgmtService.viewListAsHist(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbRequestAsMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbRequestAsMstListVo();
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
	
	
	@RequestMapping(value = "/opermgmt/asmgmt/viewListAsHistExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListAsHistExcel(@RequestBody TbRequestAsMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try{
			
			TbRequestAsMstListVo resultListVo = asMgmtService.viewListAsHistExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("AS번호|getNrequestAsSeq");
			mappingList.add("사용여부|getSrequestAsTypeNm");
			mappingList.add("고객명|getSrequestAsCtm");
			mappingList.add("신청일|getApyDt");
			mappingList.add("요청자|getApyUserNm");
			mappingList.add("노드1 명|getSrequestAsObjNm1");
			mappingList.add("노드1 전용회선번호|getSrequestAsObjLlnum1");
			mappingList.add("노드2 명|getSrequestAsObjNm2");
			mappingList.add("노드2 전용회선번호|getSrequestAsObjLlnum2");
			
			mappingList.add("최종수정자|getSmodifyNm");
			mappingList.add("최종수정일|getDmodifyDt");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbRequestAsMstVos(), mappingList, request);
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	/**
	 * 사설AS 상세화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/viewDetailAsHist.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailAsHist(@RequestBody TbRequestAsHistVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbRequestAsHistListVo resultListVo = asMgmtService.viewDetailASHist(searchVo);
		return createResultList(resultListVo.getTbRequestAsHistVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/asmgmt/viewDetailAsHist.ajax", method = RequestMethod.POST)
	public String viewDetailAsHist(@RequestBody TbRequestAsHistVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailAsHistModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/asmgmt/viewDetailAsHist.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/asmgmt/viewDetailAsHist";
	}
	private ModelMap viewDetailAsHistModel(@RequestBody TbRequestAsHistVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRequestAsHistListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = asMgmtService.viewDetailASHist(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbRequestAsHistListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbRequestAsHistListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfoSvc", paginationInfo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	

	
	/**
	 * TbRequestAsMst 미사용 AS번호 조회
	 * @param tbRequestAsMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "opermgmt/asmgmt/selectMinNrequestAsSeq.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectMinNrequestAsSeq(@RequestBody TbRequestAsMstVo tbRequestAsMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsMstVo resultVo =null;
		try {
			resultVo = asMgmtService.selectMinNrequestAsSeq(tbRequestAsMstVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * TbRequestAsMst 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/opermgmt/asmgmt/updateTbRequestAsMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTbRequestAsMst(@RequestBody TbRequestAsMstVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbRequestAsMstVo resultVo = null;
		try {
			resultVo = asMgmtService.updateTbRequestAsMst(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbRequestAsMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRequestAsMstVo();
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
	public String SendMail (String mailType, TbRequestAsApyTxnVo tbRequestAsApyTxnVo, HttpServletRequest request) {
		
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
			searchVo.setSuserId(tbRequestAsApyTxnVo.getScreateId());
			userEmail = userMgmtService.selectEmail(searchVo);					// 요청자 메일주소
			List<ReqAdminEmailVo> reqAdminEmailVoList = reqBoardService.selectAdminEmailList(); //담당자 주소
			
			userName = jwtUtil.getUserNm(request);
			userOrg = jwtUtil.getUserDeptOrgNm(request);
			
			Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
			
			map.put("MAIL_TYPE", mailType);
			
			if(mailType.equals("PrivateAs-Req-User")) {					// 사설AS신청 등록 (요청자)
				// 요청자에게 발송할 메일
				subject = "[IPMS 사설AS신청 완료] 사설AS신청이 정상적으로 접수되었습니다.";
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
				
				
				
			} else if(mailType.equals("PrivateAs-Req-Admin")) {					// 사설AS신청 등록 (관리자)
				// 담당자에게 발송할 메일
				subject = "[IPMS 사설AS신청 접수 알림] " + userName + "로부터 사설AS신청이 등록되었습니다.";
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
				
			
			} else if(mailType.equals("PrivateAs-Aprv") && tbRequestAsApyTxnVo.getSrequestAsTypeCd().equals("RS0202")) { 		// 승인 시
				// 요청자에게 발송할 메일
				subject = "[IPMS 사설AS할당 완료] 관리자로부터 사설AS 할당이 완료되었습니다.";
				
				TbRequestAsApyTxnVo selVo =  asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				map.put("AS_SEQ", selVo.getNrequestAsSeq()); // AS번호
				map.put("AS_CTM", selVo.getSrequestAsCtm());	// 고객명
				map.put("AS_DT", format.format(selVo.getDapvDt()));	// 처리일시
				
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
			} else if(mailType.equals("PrivateAs-Reject") && tbRequestAsApyTxnVo.getSrequestAsTypeCd().equals("RS0203")) { 		// 반려 시
				// 요청자에게 발송할 메일
				subject = "[IPMS 사설AS할당 반려] 관리자로부터 사설AS 할당신청이 반려되었습니다.";
				
				TbRequestAsApyTxnVo selVo =  asMgmtService.viewDetailPrivateAS(tbRequestAsApyTxnVo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				map.put("AS_CTM", selVo.getSrequestAsCtm());	// 고객명
				map.put("AS_DT", format.format(selVo.getDapvDt()));	// 처리일시
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
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
	
		// System.out.println(">>>>>>>>>result: " + result);  //Codeeyes-Critical-sysout
		PrintLogUtil.printLogInfo(">>>>>>>>>result: " + result);
		return result;
	}
}
