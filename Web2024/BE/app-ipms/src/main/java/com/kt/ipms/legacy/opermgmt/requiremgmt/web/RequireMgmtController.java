package com.kt.ipms.legacy.opermgmt.requiremgmt.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardListVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardSubVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.codej.base.dto.response.UploadFileResponse;
import com.codej.base.property.FileStorageProperties;
import com.codej.web.controller.FileController;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class RequireMgmtController extends CommonController{
	@Autowired
	private ReqBoardService reqBoardService;
	@Autowired
	private UserMgmtService userMgmtService;
	@Autowired
	private TacsMgmtService tacsMgmtService;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private FileController fileController;


	
	@RequestMapping(value = "/opermgmt/requiremgmt/viewListReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListReq(@RequestBody ReqBoardVo searchVo, ModelMap model,
			HttpServletRequest request){
		ReqBoardListVo resultListVo = reqBoardService.selectListReqBoard(searchVo);
		return createResultList(resultListVo.getReqBoardVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/requiremgmt/viewListReq.do", method = RequestMethod.POST)
	public String selectListReq(@ModelAttribute("searchVo") ReqBoardVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		ReqBoardVo searchVoClone = new ReqBoardVo();
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

		searchVoClone.setUrl("/opermgmt/requiremgmt/viewListReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/requiremgmt/viewListReq";
	}
	private ModelMap selectListReqModel(@ModelAttribute("searchVo") ReqBoardVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		ReqBoardListVo resultListVo = null;
		try{
			ReqBoardSubVo reqBoardSubVo = new ReqBoardSubVo();
			reqBoardSubVo.setRboardTypeCd("REQ001");
			List<ReqBoardSubVo> requestType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("requestType",requestType);
			
			reqBoardSubVo.setRboardTypeCd("REQ002");
			List<ReqBoardSubVo> progressType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("progressType",progressType);
			
			setPagination(searchVo);
			
			resultListVo = reqBoardService.selectListReqBoard(searchVo);
			
		} catch (ServiceException e){
			resultListVo = new ReqBoardListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new ReqBoardListVo();
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
	
	
	/**
	 * Req 상세정보 화면
	 * @param tbBoardVo
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/requiremgmt/viewDetailReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectReqBoard(@RequestBody ReqBoardVo searchVo, HttpServletRequest request)  {
		ModelMap model = new ModelMap();
		ReqBoardVo resultVo = null;
		String adminYn = null;
		String ownerYn = null;
		try {
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			resultVo = reqBoardService.selectReqBoard(searchVo);
			String userId = jwtUtil.getUserId(request);

			if(adminYn != null && adminYn.equals("Y")){
				ownerYn = "Y";
			}else{
				if(resultVo.getRboardScreateId().equals(userId)){
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
	@RequestMapping(value = "/opermgmt/requiremgmt/viewDetailReq.ajax", method = RequestMethod.POST)
	public String selectReqBoard(@RequestBody ReqBoardVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		ReqBoardVo searchVoClone = new ReqBoardVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectReqBoardModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/requiremgmt/viewDetailReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/requiremgmt/viewUpdateAdminReqPop";
		//		if(adminYn == "Y"){
		//			return "opermgmt/requiremgmt/viewUpdateAdminReqPop";
		//		}else{
		//			return "opermgmt/requiremgmt/viewUpdateUserReqPop";
		//		}
	}
	private ModelMap selectReqBoardModel(@RequestBody ReqBoardVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		ReqBoardVo resultVo = null;
		String adminYn = null;
		String ownerYn = null;
		try {
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			ReqBoardSubVo reqBoardSubVo = new ReqBoardSubVo();
			reqBoardSubVo.setRboardTypeCd("REQ001");
			List<ReqBoardSubVo> requestType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("requestType",requestType);
			
			reqBoardSubVo.setRboardTypeCd("REQ002");
			List<ReqBoardSubVo> progressType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("progressType",progressType);
			
			resultVo = reqBoardService.selectReqBoard(searchVo);
			String userId = jwtUtil.getUserId(request);
			// System.out.println(adminYn);  //Codeeyes-Critical-sysout
//			if(adminYn == "Y"){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(adminYn.equals("Y")){
				ownerYn = "Y";
			}else{
				if(resultVo.getRboardScreateId().equals(userId)){
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
	
	@RequestMapping(value = "/opermgmt/requiremgmt/viewInsertReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewinsertReq( ModelMap model, HttpServletRequest request)  {
		ModelMap builtModel = viewinsertReqModel(request);
		List<ReqBoardSubVo> requestType = (List<ReqBoardSubVo>)builtModel.get("requestType");
		List<ReqBoardSubVo> progressType = (List<ReqBoardSubVo>)builtModel.get("progressType");

		ModelMap resultModel = new ModelMap();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("requestType", requestType);
		map.put("requestTypeTotalCount", requestType.size());

		map.put("progressType", progressType);
		map.put("progressTypeTotalCount", progressType.size());

		resultModel.addAttribute("result",map);
		return resultModel;
	}
	@RequestMapping(value = "/opermgmt/requiremgmt/viewInsertReq.ajax", method = RequestMethod.POST)
	public String viewinsertReq(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		ReqBoardVo searchVoClone = new ReqBoardVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewinsertReqModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/requiremgmt/viewInsertReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/requiremgmt/viewInsertReqPop";
	}
	private ModelMap viewinsertReqModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		String adminYn = null;
		try {
			ReqBoardSubVo reqBoardSubVo = new ReqBoardSubVo();
			reqBoardSubVo.setRboardTypeCd("REQ001");
			List<ReqBoardSubVo> requestType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("requestType",requestType);
			
			reqBoardSubVo.setRboardTypeCd("REQ002");
			List<ReqBoardSubVo> progressType = reqBoardService.selectListReqBoardSub(reqBoardSubVo);
			model.addAttribute("progressType",progressType);
			
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			String userNm = jwtUtil.getUserNm(request);
			model.addAttribute("userNm",userNm);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/requiremgmt/insertReq.json", method = RequestMethod.POST)
	@ResponseBody
	public ReqBoardVo insertReq(ModelMap model,MultipartHttpServletRequest request, HttpServletResponse response)  {
		ReqBoardVo resultVo = new ReqBoardVo();
		try {
			String userId = jwtUtil.getUserId(request);
			MultipartFile file = request.getFile("file");
			String uploadPath = null;
			// String uploadPath = request.getSession().getServletContext().getRealPath("/").concat("resources") + "\\upload";
			if(file != null) {
				UploadFileResponse fileResponse = fileController.uploadFile(file);
				uploadPath = fileResponse.getDownloadUrl() ;
			}

			// String filePath = fileResponse.getDownloadUrl().substring(0, fileResponse.getDownloadUrl().lastIndexOf("/"));

			//setVo
			ReqBoardVo reqBoardVo = new ReqBoardVo();
			reqBoardVo.setRboardTitle(request.getParameter("rboardTitle"));
			reqBoardVo.setRboardDivision(request.getParameter("rboardDivision"));
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			reqBoardVo.setRboardDesireDate(transFormat.parse(request.getParameter("rboardDesireDate")));
			reqBoardVo.setRboardPurposeRequest(request.getParameter("rboardPurposeRequest"));
			reqBoardVo.setRboardImportance(request.getParameter("rboardImportance"));
			reqBoardVo.setRboardContent(request.getParameter("rboardContent"));
			reqBoardVo.setRboardScreateId(userId);
			reqBoardVo.setRboardSModifyId(userId);
			reqBoardVo.setsUserNm(request.getParameter("sUserNm"));
			reqBoardVo.setRboardFilePath(uploadPath);
			resultVo = reqBoardService.insertReq(reqBoardVo, file);
			
			//send mail
			ReqBoardVo tempVo = reqBoardService.selectEmailContent(resultVo);
			Map<String,Object> map = domainToMap(tempVo);
			map.put("MAIL_TYPE",request.getParameter("mail_type"));
			map.put("TITLE", map.get("rboardTitle").toString());
			reqMailSend(map, request);
			
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/requiremgmt/deleteReq.json", method = RequestMethod.POST)
	@ResponseBody
	public ReqBoardVo deleteReq(@RequestBody ReqBoardVo deleteVo, HttpServletRequest request, HttpServletResponse response)  {
		ReqBoardVo resultVo = null;
		try {
			resultVo = reqBoardService.deleteReq(deleteVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/requiremgmt/updateReq.json", method = RequestMethod.POST)
	@ResponseBody
	public ReqBoardVo updateReq(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response)  {
		ReqBoardVo resultVo = null;
		String adminYn = null;
		try {
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			String userId = jwtUtil.getUserId(request);
			MultipartFile file = request.getFile("file");
			String uploadPath = null;
			// String uploadPath = request.getSession().getServletContext().getRealPath("/").concat("resources") + "\\upload";
			if(file != null) {
				UploadFileResponse fileResponse = fileController.uploadFile(file);
				uploadPath = fileResponse.getDownloadUrl() ;
			}
			//setVos
			ReqBoardVo reqBoardVo = new ReqBoardVo();
			reqBoardVo.setSeq(new BigInteger(request.getParameter("seq")));
			reqBoardVo.setRboardSeq(new BigInteger(request.getParameter("seq")));
			reqBoardVo.setRboardTitle(request.getParameter("rboardTitle"));
			reqBoardVo.setRboardDivision(request.getParameter("rboardDivision"));
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			reqBoardVo.setRboardDesireDate(transFormat.parse(request.getParameter("rboardDesireDate")));
			reqBoardVo.setRboardPurposeRequest(request.getParameter("rboardPurposeRequest"));
			reqBoardVo.setRboardImportance(request.getParameter("rboardImportance"));
			reqBoardVo.setRboardContent(request.getParameter("rboardContent"));
//			if(adminYn == "Y"){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(adminYn != null && adminYn.equals("Y")){
				reqBoardVo.setRboardActionDetail(request.getParameter("rboardActionDetail"));
				reqBoardVo.setRboardProgress(request.getParameter("rboardProgress"));
				if(!request.getParameter("rboardExpectedDate").equals("")){
					reqBoardVo.setRboardExpectedDate(transFormat.parse(request.getParameter("rboardExpectedDate")));
				}
				
			}
			
			reqBoardVo.setRboardSModifyId(userId);
			reqBoardVo.setsUserNm(request.getParameter("sUserNm"));
			reqBoardVo.setRboardFilePath(uploadPath);
			resultVo = reqBoardService.updateReq(reqBoardVo, file, adminYn);
			
			//send mail
			ReqBoardVo tempVo = reqBoardService.selectEmailContent(resultVo);
			if(tempVo.getRboardProgress().equals("조치 완료") || tempVo.getRboardProgress().equals("접수 반려")){
				Map<String,Object> map = domainToMap(tempVo);
				map.put("MAIL_TYPE",request.getParameter("mail_type"));
				map.put("TITLE", map.get("rboardTitle").toString());
				reqMailSend(map, request);
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/requiremgmt/ReqDownLoad.do", method = RequestMethod.GET)
	public void reqDownload(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException{
		ReqBoardVo resultVo = null;
		ReqBoardVo reqBoardVo = new ReqBoardVo();
		reqBoardVo.setSeq(new BigInteger(request.getParameter("seq")));
		resultVo = reqBoardService.selectReqBoardUpload(reqBoardVo);
		String fileName = new String(resultVo.getRboardFileOriginName().getBytes("UTF-8"), "ISO-8859-1");
		response.setHeader("Content-Disposition",
                "attachment;filename="+fileName);
		response.setContentType("text/plain");
		String path = resultVo.getRboardFilePath();
		File down_file = new File(path);
		FileInputStream fileIn = null;
	    try {
			fileIn = new FileInputStream(down_file);
			IOUtils.copy(fileIn, response.getOutputStream());
			response.flushBuffer();
			fileIn.close();
			IOUtils.closeQuietly(fileIn);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fileIn.close();
		}
	}
	
	/**
	 * vo를 map형식으로 변환해서 반환
	 * @param vo VO
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> domainToMap(Object vo) throws Exception {
	    return domainToMapWithExcept(vo, null);
	}

	/**
	 * 특정 변수를 제외해서 vo를 map형식으로 변환해서 반환.
	 * @param vo VO
	 * @param arrExceptList 제외할 property 명 리스트
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> domainToMapWithExcept(Object vo, String[] arrExceptList) throws Exception {
	    Map<String, Object> result = new HashMap<String, Object>();
	    BeanInfo info = Introspector.getBeanInfo(vo.getClass());
	    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        Method reader = pd.getReadMethod();
	        if (reader != null) {
	            if(arrExceptList != null && arrExceptList.length > 0 && isContain(arrExceptList, pd.getName())) continue;
	            result.put(pd.getName(), reader.invoke(vo));
	        }
	    }
	    return result;
	}
	public static Boolean isContain(String[] arrList, String name) {
	    for (String arr : arrList) {
	        if (StringUtils.contains(arr, name))
	            return true;
	    }
	    return false;
	}
	
	@RequestMapping(value = "/ipmgmt/requiremgmt/viewListReqExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> selectListReqExcel(@RequestBody ReqBoardVo searchVo ,HttpServletRequest request, HttpServletResponse response)  {
		FileVo resultVo = new FileVo();
		try {
			setPagination(searchVo);
			ReqBoardListVo resultListVo = reqBoardService.selectListReqBoardExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("번호|getSeq");
			mappingList.add("요청사항구분|getRboardDivision");
			mappingList.add("제목|getRboardTitle");
			mappingList.add("등록자|getsUserNm");
			mappingList.add("등록일|getRboardDcreateDt");
			mappingList.add("희망완료일|getRboardDesireDate");
			mappingList.add("완료예정일|getRboardExpectedDate");
			mappingList.add("중요도|getRboardImportance");
			mappingList.add("진행상태|getRboardProgress");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getReqBoardVos(), mappingList, request);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}	
	
	public void reqMailSend(Map<String,Object> map, MultipartHttpServletRequest request){
		try {
			String mail_type = map.get("MAIL_TYPE").toString();
			String rboardProgress = map.get("rboardProgress").toString();
			SmtpVo smtpVo = new SmtpVo();
			smtpVo.setMailType(mail_type);
			String subject = "";
			if(rboardProgress.equals("요청사항 접수")){
				subject = "[IPMS 요청사항 등록 알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 요청사항이 정상적으로 접수되었습니다.";
				smtpVo.setSubject(subject);
			}else if(rboardProgress.equals("조치 완료")){
				subject = "[IPMS 요청사항 완료 알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 요청사항이 정상적으로 완료되었습니다.";
				smtpVo.setSubject(subject);
			}else if(rboardProgress.equals("접수 반려")){
				subject = "[IPMS 요청사항 반려  알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 요청사항이 반려되었습니다.";
				smtpVo.setSubject(subject);
			}else if(rboardProgress.equals("조치 진행 중")){
				subject = "[IPMS 요청사항 진행  알림]"+map.get("sUserNm").toString()+"님으로부터 등록하신 요청사항이 진행 중이 되었습니다.";
				smtpVo.setSubject(subject);
			}
			//get html
			String content = smtpUtil.parseHtml(map, request);
			
			//get user email
			TbUserBasVo searchVo = new TbUserBasVo();
			searchVo.setSuserId(map.get("rboardScreateId").toString());
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
			
			//send mail to km
			userEmail = "91295753@ktfriend.com"; 
			smtpVo.setToEmail(userEmail);
			smtpVo.setMessage(content);
			smtpVo.setUserID(jwtUtil.getUserId(request));
			smtpUtil.sendMail(smtpVo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
