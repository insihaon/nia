package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.web;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.codej.web.controller.FileController;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.service.ExcelCheckService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.service.IpUploadMgmtService;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadListVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubListVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class IpUploadMgmtController extends CommonController {

	@Autowired
	IpUploadMgmtService ipUploadMgmtService;
	
	@Autowired
	private AllocMgmtService allocMgmtService;
	
	@Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;
	
	@Autowired
	private ExcelCheckService excelCheckService;
	@Autowired
	private FileController fileController;
	
	@RequestMapping(value = "/ipmgmt/ipuploadmgmt/viewIpUploadMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewIpUploadMst(@RequestBody TbIpUploadVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpUploadListVo resultListVo = ipUploadMgmtService.selectListPageIpUpload(searchVo);
		return createResultList(resultListVo.getTbIpUloadVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ipmgmt/ipuploadmgmt/viewIpUploadMst.do", method = RequestMethod.POST)
	public String viewIpUploadMst(@ModelAttribute("searchVo") TbIpUploadVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpUploadVo searchVoClone = new TbIpUploadVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewIpUploadMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ipuploadmgmt/viewIpUploadMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ipuploadmgmt/viewIpUploadMst";
	}
	private ModelMap viewIpUploadMstModel(@ModelAttribute("searchVo") TbIpUploadVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpUploadListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = ipUploadMgmtService.selectListPageIpUpload(searchVo);
		} catch (ServiceException e) {
			String msg= tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msg);
		} catch (Exception e) {
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msg);
		}
		
		model.addAttribute("resultListVo",resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	@RequestMapping(value = "/ipmgmt/ipuploadmgmt/upload.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public Map<String,Object> fileUpload(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response)  {
		ModelMap resultModel = new ModelMap();
		Map<String,Object> retMap = new HashMap<String,Object>();
		TbIpUploadVo resultListVo = new TbIpUploadVo(); 
		File convFile = null;
		excelCheckService.updateExcelUp("Y");
		try{
		
			MultipartFile file =  request.getFile("file");
			// SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = file.getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			/* Sparrow - PATH_TRAVERSAL Start */
			fileName = fileName.replaceAll("/","");
			fileName = fileName.replaceAll("\\\\","");
			fileName = fileName.replaceAll("&","");
			/* Sparrow - PATH_TRAVERSAL End */
			fileController.uploadFile(file);
			// convFile = new File(fileName);
 			// convFile = new File(System.getProperty("jboss.server.temp.dir")+File.separator+file.getOriginalFilename());
			// file.transferTo(convFile);
		
			TbIpUploadVo insertVo = new TbIpUploadVo();
			insertVo.setsFileNm(fileName);
			insertVo.setScreateId(jwtUtil.getUserId(request));
			insertVo.setSmodifyId(jwtUtil.getUserId(request));
			
			// Step1 . 엑셀 파싱
			List<Map<String,Object>> parseList = new ArrayList<Map<String,Object>>();
			if(extension.equals("xlsx")){
				parseList = ipUploadMgmtService.parseUploadFile(request, insertVo, convFile);
			}else{
				parseList = ipUploadMgmtService.parseUploadTxtFile(request, insertVo, convFile);
			}
			
			
			ipUploadMgmtService.insertTbIpUploadMst(insertVo);
			
			// Step2. 유효성 검증 
			String pSvcLineTypeCd = request.getParameter("ssvcLineTypeCd");
			String pSvcGroupCd = request.getParameter("ssvcGroupCd");
			String pSvcObjCd = request.getParameter("ssvcObjCd");
			insertVo.setSsvcLineTypeCd(pSvcLineTypeCd);
			insertVo.setSsvcGroupCd(pSvcGroupCd);
			insertVo.setSsvcObjCd(pSvcObjCd);
			
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
			tbLvlMstVo.setSsvcLineTypeCd(pSvcLineTypeCd);
			tbLvlMstVo.setSsvcGroupCd(pSvcGroupCd);
			tbLvlMstVo.setSsvcObjCd(pSvcObjCd);
			 
			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
			
			// 수용국 조회
			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
			
			// sAssignTypeCD 조회 (할당유형)
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			// 데이터 검증
			List<Map<String,Object>> retList = ipUploadMgmtService.validSetUploadFile(request, insertVo, parseList, sipCreateTypeCds, sipVersionTypeCds, svcLineTypeCds, sLvlSubvCds, sassignTypeCds);
			
			insertVo.setScreateId(jwtUtil.getUserId(request));
			insertVo.setSmodifyId(jwtUtil.getUserId(request));
			
			// Step3. IpUploadSub 등록
			ipUploadMgmtService.insertIpUploadSub(request,parseList,insertVo);
			
			// Step4. 서비스용 Vo 생성 및 호출
			ipUploadMgmtService.createServiceVo(request,parseList,insertVo);
			
			// Step5. IPUploadMst 상태값 변경
			ipUploadMgmtService.updateIpUploadMst(request,insertVo);
						
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			PrintLogUtil.printLogInfo(e.toString());
			e.printStackTrace();
			resultListVo = new TbIpUploadVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			excelCheckService.updateExcelUp("N");
		} catch (Exception e) {
			PrintLogUtil.printLogInfo(e.toString());
			e.printStackTrace();
			resultListVo = new TbIpUploadVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			excelCheckService.updateExcelUp("N");
		}finally{
			excelCheckService.updateExcelUp("N");
			try{
				convFile.delete();
			}catch(Exception e){
				PrintLogUtil.printLogInfo(e.toString());
				resultListVo = new TbIpUploadVo();
				String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
				resultListVo.setCommonMsg(msgDesc);
			}
		}
		excelCheckService.updateExcelUp("N");
		model.addAttribute("resultListVo", resultListVo);
		retMap.put("resultListVo", resultListVo);
		resultModel.addAttribute("result", retMap);
		return resultModel;
	}
		
	/**
	 * 양식 파라미터 선택 레이어 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/viewCreFormatMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpBlockMstVo resultVo = new TbIpBlockMstVo();
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/viewCreFormatMst.ajax", method = RequestMethod.POST)
	public String viewInsertCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		TbIpUploadVo searchVoClone = new TbIpUploadVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertCrtIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ipuploadmgmt/viewCreFormatMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ipuploadmgmt/viewCreFormatMst";
	}
	private ModelMap viewInsertCrtIPMstModel(@RequestBody TbIpBlockMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstVo resultVo = null;
		
		try {
			
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
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);

			resultVo = new TbIpBlockMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("suserID", jwtUtil.getSessionVO(request).getSuserId());
		return model;
	}
	
	
	
	/**
	 * 상세페이지
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/viewDetailIpMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailIpMst(@RequestBody TbIpUploadVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpUploadSubListVo resultVo = ipUploadMgmtService.selectUploadDetail(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/viewDetailIpMst.ajax", method = RequestMethod.POST)
	public String viewDetailIpMst(@RequestBody TbIpUploadVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpUploadVo searchVoClone = new TbIpUploadVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailIpMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/ipuploadmgmt/viewDetailIpMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/ipuploadmgmt/viewDetailIpMst";
	}
	private ModelMap viewDetailIpMstModel(@RequestBody TbIpUploadVo searchVo, HttpServletRequest reques) {
		ModelMap model = new ModelMap();
		TbIpUploadSubListVo resultVo = new TbIpUploadSubListVo();
		
		try {
			
			// Map<String, Object> paramMap = new HashMap<String, Object>();
			PrintLogUtil.printLogValueObject(searchVo);
			resultVo = ipUploadMgmtService.selectUploadDetail(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			resultVo = new TbIpUploadSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			resultVo = new TbIpUploadSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	

	/**
	 * IP Upload 관리 - 엑셀 양식 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/downloadformat.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListWhoisExcel(@RequestBody TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		
		try{
			// Step1. 공통 코드 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
			
			// Step1. 선택한 계위 정보의 장비 목록 시설정보 가져오기
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
		
			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
			tbLvlMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			tbLvlMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			tbLvlMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			 
			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
			
			// 수용국 조회
			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
			
			// resultListVo = allocMgmtService.selectListNeMst(searchVo);
			
			// sAssignTypeCD 조회 (할당유형)
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			
			// Step2. 엑셀 생성 
			List<IpAllocOperMstVo> hostList = ipUploadMgmtService.selectListTbIpHostMst(searchVo,tbLvlBasVo);
			// List<Map<String,Object>> dummyList = new ArrayList<Map<String,Object>>();
			List<TbIpUploadVo> dummyList = new ArrayList<TbIpUploadVo>();
			
//			List<String> sheetNames = new ArrayList<String>(Arrays.asList("업로드양식1","(참고)서비스망코드2","수용국코드3","IP생성유형코드5","IP할당유형코드6","장비목록7")); // 시트명
			List<String> sheetNames = new ArrayList<String>(Arrays.asList("업로드양식","(참고)서비스망코드","(참고)IP생성유형코드","(참고)IP할당유형코드","(참고)수용국코드","(참고)장비목록")); // 시트명
//			List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","SvcLineTypeCd","HostSofficeList","VersionTypeCd","createType","OrgSassignType"));
			List<List<?>> voList = new ArrayList<List<?>>(); // 데이터 값 모음
			List<List<String>> mappingList = new ArrayList<List<String>>(); // 매핑
			
			// 업로드양식
			List<String> mapping1 = new ArrayList<String>();
			mapping1.add("(필수)서비스망유형CD|getSsvcLineTypeCd|1");  // (필수)서비스망유형CD
			mapping1.add("(필수)IP생성유형코드|getSipCreateTypeCd|1"); // (필수)IP생성유형코드
			mapping1.add("(필수)IP할당유형코드|getSassignTypeCd|1"); // (필수)IP할당유형코드
			mapping1.add("(필수)IP블록|getPipprefix|1"); // (필수)IP블록 
			mapping1.add("(필수)IP블록 입력범위|getPipprefixRange|1"); // (필수)IP블록 입력범위 
			mapping1.add("(필수)게이트웨이IP|getSgatewayip|1"); // (필수)게이트웨이
			mapping1.add("수용국코드|getSicisofficescode|2"); // 수용국코드
			mapping1.add("장비대표IP|getSsubscmstip|2"); // 장비대표IP
			mapping1.add("장치모델명|getSmodelname|2"); // 장치모델명
			mapping1.add("시설표준코드|getSsubscnnescode|2"); //시설표준코드
			mapping1.add("장비별칭|getSsubscnealias|2"); //장비별칭
			mapping1.add("전용번호|getSllnum|3"); //전용번호
			mapping1.add("인터페이스명|getSsubsclgipportdescription|3"); // 인터페이스명
			mapping1.add("국축스위치시리얼IP|getSsubsclgipportip|3"); // 국축스위치시리얼IP
			mapping1.add("가입자축스위치시리얼IP|getSsubscrouterserialip|3"); // 가입자축스위치시리얼IP
			mapping1.add("수용회선명|getSconnalias|3"); // 수용회선명 
			mapping1.add("비고|getScomment|3"); // 비고
			
			/**
			*테스트시나리오용 1
			**/
//			TbIpUploadVo tempVo = new TbIpUploadVo();
//			tempVo.setSsvcLineTypeCd("CL0008");
//			tempVo.setSipCreateTypeCd("CT0001");
//			tempVo.setSassignTypeCd("SA9035");
//			tempVo.setPipprefix("11.0.3.1/32");
//			tempVo.setSicisofficescode("R00435");
//			tempVo.setSsubscmstip("115.21.48.126");
//			tempVo.setSmodelname("DAS_V4208");
//			tempVo.setSgatewayip("11.0.3.1");
//			tempVo.setSsubscnnescode("");
//			tempVo.setSsubscnealias("02000160-0022");
//			tempVo.setSsubsclgipportseq("");
//			tempVo.setSsubsclgipportdescription("");
//			tempVo.setSsubsclgipportip("");
//			tempVo.setSsubscrouterserialip("");
//			tempVo.setSconnalias("");
//			
//			TbIpUploadVo tempVo2 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo2);
//			tempVo2.setPipprefix("11.0.3.16/29");
//			tempVo2.setSgatewayip("11.0.3.22");
//			
//			TbIpUploadVo tempVo3 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo3);
//			tempVo3.setPipprefix("11.0.3.32/28");
//			tempVo3.setSgatewayip("11.0.3.46");
//			
//			TbIpUploadVo tempVo4 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo4);
//			tempVo4.setPipprefix("11.0.3.64/27");
//			tempVo4.setSgatewayip("11.0.3.94");
//			
//			TbIpUploadVo tempVo5 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo5);
//			tempVo5.setPipprefix("11.0.3.6/31");
//			tempVo5.setSgatewayip("11.0.3.7");
//			
//			dummyList.add(tempVo);
//			dummyList.add(tempVo2);
//			dummyList.add(tempVo3);
//			dummyList.add(tempVo4);
//			dummyList.add(tempVo5);
			
			/**
			*테스트시나리오용 2
			**/
//			for(int i=0;i<256;i++){
//				TbIpUploadVo tempVo = new TbIpUploadVo();
//				tempVo.setSsvcLineTypeCd("CL0008");
//				tempVo.setSipCreateTypeCd("CT0001");
//				tempVo.setSassignTypeCd("SA9035");
//				tempVo.setPipprefix("11.0.3."+Integer.toString(i)+"/32");
//				tempVo.setSicisofficescode("R00435");
//				tempVo.setSsubscmstip("115.21.48.126");
//				tempVo.setSmodelname("DAS_V4208");
//				tempVo.setSgatewayip("11.0.3."+Integer.toString(i));
//				tempVo.setSsubscnnescode("");
//				tempVo.setSsubscnealias("02000160-0022");
//				tempVo.setSsubsclgipportseq("");
//				tempVo.setSsubsclgipportdescription("");
//				tempVo.setSsubsclgipportip("");
//				tempVo.setSsubscrouterserialip("");
//				tempVo.setSconnalias("");
//				
//				dummyList.add(tempVo);
//			}
			
			/**
			*테스트시나리오용3 
			**/
//			TbIpUploadVo tempVo = new TbIpUploadVo();
//			tempVo.setSsvcLineTypeCd("CL0008");
//			tempVo.setSipCreateTypeCd("CT0001");
//			tempVo.setSassignTypeCd("SA9035");
//			tempVo.setPipprefix("11.0.3.0/24");
//			tempVo.setSicisofficescode("R00435");
//			tempVo.setSsubscmstip("115.21.48.126");
//			tempVo.setSmodelname("DAS_V4208");
//			tempVo.setSgatewayip("11.0.3.254");
//			tempVo.setSsubscnnescode("");
//			tempVo.setSsubscnealias("02000160-0022");
//			tempVo.setSsubsclgipportseq("");
//			tempVo.setSsubsclgipportdescription("");
//			tempVo.setSsubsclgipportip("");
//			tempVo.setSsubscrouterserialip("");
//			tempVo.setSconnalias("");
//			dummyList.add(tempVo);
			
			/**
			 * 테스트시나리오용4
			 */
//			TbIpUploadVo tempVo = new TbIpUploadVo();
//			tempVo.setSsvcLineTypeCd("CL0008");
//			tempVo.setSipCreateTypeCd("CT0001");
//			tempVo.setSassignTypeCd("SA9035");
//			tempVo.setPipprefix("11.0.3.125/32");
//			tempVo.setSicisofficescode("R00435");
//			tempVo.setSsubscmstip("115.21.48.126");
//			tempVo.setSmodelname("DAS_V4208");
//			tempVo.setSgatewayip("11.0.3.125");
//			tempVo.setSsubscnnescode("");
//			tempVo.setSsubscnealias("02000160-0022");
//			tempVo.setSsubsclgipportseq("");
//			tempVo.setSsubsclgipportdescription("");
//			tempVo.setSsubsclgipportip("");
//			tempVo.setSsubscrouterserialip("");
//			tempVo.setSconnalias("");
//			
//			TbIpUploadVo tempVo2 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo2);
//			tempVo2.setPipprefix("11.0.3.254/32");
//			tempVo2.setSgatewayip("11.0.3.254");
//			
//			dummyList.add(tempVo);
//			dummyList.add(tempVo2);
			
			/**
			 * 테스트 시나리오 6
			 */
//			TbIpUploadVo tempVo = new TbIpUploadVo();
//			tempVo.setSsvcLineTypeCd("CL0008");
//			tempVo.setSipCreateTypeCd("CT0001");
//			tempVo.setSassignTypeCd("SA9035");
//			tempVo.setPipprefix("20.0.128.0/18");
//			tempVo.setSicisofficescode("R00435");
//			tempVo.setSsubscmstip("115.21.48.126");
//			tempVo.setSmodelname("DAS_V4208");
//			tempVo.setSgatewayip("20.0.159.255");
//			tempVo.setSsubscnnescode("");
//			tempVo.setSsubscnealias("02000160-0022");
//			tempVo.setSsubsclgipportseq("");
//			tempVo.setSsubsclgipportdescription("");
//			tempVo.setSsubsclgipportip("");
//			tempVo.setSsubscrouterserialip("");
//			tempVo.setSconnalias("");
//			
//			TbIpUploadVo tempVo2 = new TbIpUploadVo();
//			CloneUtil.copyObjectInformation(tempVo, tempVo2);
//			tempVo2.setPipprefix("20.0.160.0/24");
//			tempVo2.setSgatewayip("20.0.160.255");
//			
//			dummyList.add(tempVo);
//			dummyList.add(tempVo2);
			
			/**
			 * 테스트시나리오 7
			 */
			/* */
			TbIpUploadVo tempVo = new TbIpUploadVo();
			tempVo.setSsvcLineTypeCd("CL0008");
			tempVo.setSipCreateTypeCd("CT0005");
			tempVo.setSassignTypeCd("SA0001");
			tempVo.setPipprefix("10.0.0.0/24");
			tempVo.setSicisofficescode("VV0077");
			tempVo.setSsubscmstip("125.145.56.5");
			tempVo.setSmodelname("ZFV_GS4000-160ES");
			tempVo.setSgatewayip("10.0.0.254");
			tempVo.setSsubscnnescode("GGHS03592");
			tempVo.setSsubscnealias("Balan-MDG005");
			tempVo.setPipprefixRange("10.0.0.0/17");
			tempVo.setSllnum("021234567890");
			tempVo.setSsubsclgipportdescription("IPMS interface 0001");
			tempVo.setSsubsclgipportip("115.21.58.1");
			tempVo.setSsubscrouterserialip("115.21.58.2");
			tempVo.setSconnalias("경기도교육청");
			tempVo.setScomment("비고테스트");
			
			TbIpUploadVo tempVo2 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo2);
			tempVo2.setPipprefix("10.0.1.0/24");
			tempVo2.setSgatewayip("10.0.1.254");
			tempVo2.setSassignTypeCd("SA1008");
//			tempVo2.setSicisofficescode("VV9999");
			
			TbIpUploadVo tempVo3 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo3);
			tempVo3.setPipprefix("10.0.2.0/25");
			tempVo3.setSgatewayip("10.0.2.126");
			tempVo3.setSassignTypeCd("SA1009");
			
			TbIpUploadVo tempVo4 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo4);
			tempVo4.setPipprefix("10.0.3.0/24");
			tempVo4.setSgatewayip("10.0.3.254");
			tempVo4.setSassignTypeCd("SA1010");
			
			TbIpUploadVo tempVo5 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo5);
			tempVo5.setPipprefix("10.0.4.0/24");
			tempVo5.setSgatewayip("10.0.4.254");
			tempVo5.setSassignTypeCd("SA1011");
			
			TbIpUploadVo tempVo6 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo6);
			tempVo6.setPipprefix("10.0.5.0/24");
			tempVo6.setSgatewayip("10.0.5.254");
			tempVo6.setSassignTypeCd("SA1012");
			
			TbIpUploadVo tempVo7 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo7);
			tempVo7.setPipprefix("10.0.6.0/24");
			tempVo7.setSgatewayip("10.0.6.254");
			tempVo7.setSassignTypeCd("SA1013");
			
			TbIpUploadVo tempVo8 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo8);
			tempVo8.setPipprefix("10.0.7.0/24");
			tempVo8.setSgatewayip("10.0.7.254");
			tempVo8.setSassignTypeCd("SA1014");
			
			TbIpUploadVo tempVo9 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo9);
			tempVo9.setPipprefix("10.0.8.0/24");
			tempVo9.setSgatewayip("10.0.8.254");
			tempVo9.setSassignTypeCd("SA9035");
			
			TbIpUploadVo tempVo10 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo10);
			tempVo10.setPipprefix("10.0.9.0/24");
			tempVo10.setSgatewayip("10.0.9.254");
			tempVo10.setSassignTypeCd("SA9041");
			
			TbIpUploadVo tempVo11 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo11);
			tempVo11.setPipprefix("10.0.10.0/24");
			tempVo11.setSgatewayip("10.0.10.254");
			tempVo11.setSassignTypeCd("SA0001");
			
			TbIpUploadVo tempVo12 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo12);
			tempVo12.setPipprefix("10.0.11.0/24");
			tempVo12.setSgatewayip("10.0.11.254");
			tempVo12.setSassignTypeCd("SA1008");
			
			TbIpUploadVo tempVo13 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo13);
			tempVo13.setPipprefix("10.0.12.0/24");
			tempVo13.setSgatewayip("10.0.12.254");
			tempVo13.setSassignTypeCd("SA1009");
			
			TbIpUploadVo tempVo14 = new TbIpUploadVo();
			CloneUtil.copyObjectInformation(tempVo, tempVo14);
			tempVo14.setPipprefix("10.0.13.0/24");
			tempVo14.setSgatewayip("10.0.13.254");
			tempVo14.setSassignTypeCd("SA1010");
			
			dummyList.add(tempVo);
			dummyList.add(tempVo2);
			dummyList.add(tempVo3);
			dummyList.add(tempVo4);
			dummyList.add(tempVo5);
			dummyList.add(tempVo6);
			dummyList.add(tempVo7);
			dummyList.add(tempVo8);
			dummyList.add(tempVo9);
			dummyList.add(tempVo10);
			dummyList.add(tempVo11);
			dummyList.add(tempVo12);
			dummyList.add(tempVo13);
			dummyList.add(tempVo14);
			
			
			/***
			 * 시나리오 8
			 */
			/* 
			TbIpUploadVo tempVo = new TbIpUploadVo();
			tempVo.setSsvcLineTypeCd("CL0001");
			tempVo.setSipCreateTypeCd("CT0005");
			tempVo.setSassignTypeCd("SA1035");
			tempVo.setPipprefix("192.168.0.0/16");
			tempVo.setSicisofficescode("Z00003");
			tempVo.setSsubscmstip("125.145.56.5");
			tempVo.setSmodelname("ZFV_GS4000-160ES");
			tempVo.setSgatewayip("192.168.255.253");
			tempVo.setSsubscnnescode("GGHS03592");
			tempVo.setSsubscnealias("Balan-MDG005");
			tempVo.setPipprefixRange("192.168.0.0/16");
			tempVo.setSllnum("021234567890");
			tempVo.setSsubsclgipportdescription("IPMS interface 0001");
			tempVo.setSsubsclgipportip("115.21.58.1");
			tempVo.setSsubscrouterserialip("115.21.58.2");
			tempVo.setSconnalias("여의도센터");
			tempVo.setScomment("비고테스트");
			dummyList.add(tempVo);
			*/
			
			
			/* MIG */
//			List<Map<String, Object>> tmpListVo = new ArrayList<Map<String,Object>>();
//			tmpListVo = ipUploadMgmtService.selectListMigIpBlock1(searchVo);
//			
//			for(Map<String,Object> vo : tmpListVo) {
//				
//				TbIpUploadVo tempVo = new TbIpUploadVo();
//
//				//PrintLogUtil.pringLogHashMap(vo);
//				
//				tempVo.setSsvcLineTypeCd(vo.get("ssvc_line_type_cd").toString());
//				tempVo.setSipCreateTypeCd(vo.get("sip_create_type_cd").toString());
//				tempVo.setSassignTypeCd(vo.get("sassign_type_cd").toString());
//				tempVo.setPipprefix(vo.get("pip_prefix").toString());
//				tempVo.setSicisofficescode(vo.get("sicisofficescode").toString());
//				tempVo.setSsubscmstip(vo.get("ssubscmstip").toString());
//				tempVo.setSmodelname(vo.get("smodelname").toString());
//				tempVo.setSgatewayip(vo.get("sgatewayip").toString());
//				tempVo.setSsubscnnescode(vo.get("ssubscnescode").toString());
//				tempVo.setSsubscnealias(vo.get("ssubscnealias").toString());
//				tempVo.setPipprefixRange(vo.get("pip_prefix_range").toString());
//				tempVo.setSllnum(vo.get("sllnum").toString());
//				tempVo.setSsubsclgipportdescription(vo.get("ssubsclgipportdescription").toString());
//				tempVo.setSsubsclgipportip(vo.get("ssubsclgipportip").toString());
//				tempVo.setSsubscrouterserialip(vo.get("ssubscrouterserialip").toString());
//				tempVo.setSconnalias(vo.get("sconnalias").toString());
//				
//				dummyList.add(tempVo);
//			}
			
			
			// ------------------- 시나리오 끝 ------------------- 
			
			List<String> mapping2 = new ArrayList<String>();  // (참고)서비스망코드
			mapping2.add("코드|getCode");
			mapping2.add("명칭|getName");
			
			List<String> mapping3 = new ArrayList<String>(); // (참고)IP생성유형코드
			mapping3.add("코드|getCode");
			mapping3.add("명칭|getName");
			
			List<String> mapping4 = new ArrayList<String>(); // (참고)IP할당유형코드
			mapping4.add("코드|getCode");
			mapping4.add("명칭|getName");
			
//			List<String> mapping5 = new ArrayList<String>(); // (참고)수용국코드
//			mapping5.add("코드|getCode");
//			mapping5.add("명칭|getName");
			
//			List<String> mapping6 = new ArrayList<String>(); 
//			mapping6.add("코드|getCode");
//			mapping6.add("명칭|getName");
			
			List<String> mapping7 = new ArrayList<String>(); // (참고)장비목록
			mapping7.add("수용국코드|getSofficecode");
			mapping7.add("장비대표IP|getSsubscmstip");
			mapping7.add("모델명|getSmodelname");
			mapping7.add("시설표준코드|getSsubscnnescode");
			mapping7.add("장비명|getSsubscnealias");
			mapping7.add("수용국|getSofficename");
			
			mappingList.add(mapping1);
			mappingList.add(mapping2);
			mappingList.add(mapping3);
			mappingList.add(mapping4);
//			mappingList.add(mapping5);
//			mappingList.add(mapping6);
			mappingList.add(mapping7);
						
			voList.add(dummyList);
			voList.add(svcLineTypeCds); // 서비스망코드
			voList.add(sipCreateTypeCds);  // IP생성유형코드
			voList.add(sassignTypeCds);  // 할당유형코드
//			voList.add(sLvlSubvCds); // 수용국코드 
//			voList.add(sipVersionTypeCds);
			voList.add(hostList); // 장비목록
			
			// String fileName = excelUtil.createExcelFile(hostList, mappingList, request);
			// String fileName = excelUtil.createIpUploadExcelFile(voList, mappingList, sheetNames, request);
			
			// if (StringUtils.hasText(fileName)) {
			// 	resultVo.setFileName(fileName);
			// 	resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			// } else {
			// 	throw new ServiceException("CMN.HIGH.00050");
			// }
			return excelDownloadService.generateAndDownloadIpUploadExcel(voList, mappingList, sheetNames, request);
			
		}catch (ServiceException e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	// 3계위 선택시 수용국 조회
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/selectSearchHostList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	 public List<CommonCodeVo> selectSearchHostList(@RequestBody TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
		List<CommonCodeVo> resultListVo = null;
		 
		// Step1. 공통 코드 
		List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
		List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
		List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
		
		// Step1. 선택한 계위 정보의 장비 목록 시설정보 가져오기
		TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
	
		TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
		tbLvlMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		tbLvlMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
		tbLvlMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
		 
		
		TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
		tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
		
		TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
		searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
		
		IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
		searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
		
		// 수용국 조회
		resultListVo = allocMgmtService.selectHostSofficeList(searchOperVo);
					
		 
		 return resultListVo;
	 } 
	
	/**
	 * MIG - IP Upload 관리 - 엑셀 양식 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value="/ipmgmt/ipuploadmgmt/downloadformatMig1.json", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public FileVo viewMigExcelDown1(@ModelAttribute("searchVo") TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
//		
//		FileVo resultVo = new FileVo();
//		
//		try{
//			// Step1. 공통 코드 
//			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
//			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
//			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
//			
//			// Step1. 선택한 계위 정보의 장비 목록 시설정보 가져오기
//			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
//		
//			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
//			tbLvlMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
//			tbLvlMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
//			tbLvlMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
//			 
//			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
//			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
//			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
//			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			// 수용국 조회
//			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
//			
//			// resultListVo = allocMgmtService.selectListNeMst(searchVo);
//			
//			// sAssignTypeCD 조회 (할당유형)
//			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
//			
//			
//			// Step2. 엑셀 생성 
//			List<IpAllocOperMstVo> hostList = ipUploadMgmtService.selectListTbIpHostMst(searchVo,tbLvlBasVo);
//			// List<Map<String,Object>> dummyList = new ArrayList<Map<String,Object>>();
//			List<TbIpUploadVo> dummyList = new ArrayList<TbIpUploadVo>();
//			
//			List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","서비스망코드","수용국코드","IP버전코드","IP생성유형코드","IP할당유형코드","장비목록")); // 시트명
//			// List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","SvcLineTypeCd","HostSofficeList","VersionTypeCd","createType","OrgSassignType"));
//			List<List<?>> voList = new ArrayList<List<?>>(); // 데이터 값 모음
//			List<List<String>> mappingList = new ArrayList<List<String>>(); // 매핑
//			
//			// 템플릿
//			List<String> mapping1 = new ArrayList<String>();
//			mapping1.add("(필수)서비스망유형CD|getSsvcLineTypeCd|true"); 
//			mapping1.add("(필수)IP생성유형코드|getSipCreateTypeCd|true");
//			mapping1.add("(필수)IP할당유형코드|getSassignTypeCd|true");
//			mapping1.add("(필수)IP블록|getPipprefix|true");
//			mapping1.add("(필수)IP블록 입력범위|getPipprefixRange|true");
//			mapping1.add("수용국코드|getSicisofficescode");
//			mapping1.add("장비대표IP|getSsubscmstip");
//			mapping1.add("장치모델명|getSmodelname");
//			mapping1.add("(필수)게이트웨이IP|getSgatewayip|true");
//			mapping1.add("시설표준코드|getSsubscnnescode"); //
//			mapping1.add("장비별칭|getSsubscnealias"); //
//			mapping1.add("전용번호|getSllnum"); //
//			mapping1.add("인터페이스명|getSsubsclgipportdescription"); //
//			mapping1.add("국축스위치시리얼IP|getSsubsclgipportip"); // 국축스위치시리얼IP
//			mapping1.add("가입자축스위치시리얼IP|getSsubscrouterserialip"); // 가입자축스위치시리얼IP
//			mapping1.add("수용회선명|getSconnalias"); // 수용회선명
//			mapping1.add("비고|getScomment");
//			
//			/* MIG */
//			List<Map<String, Object>> tmpListVo = new ArrayList<Map<String,Object>>();
//			tmpListVo = ipUploadMgmtService.selectListMigIpBlock1(searchVo);
//			
//			for(Map<String,Object> vo : tmpListVo) {
//				
//				TbIpUploadVo tempVo = new TbIpUploadVo();
//
//				//PrintLogUtil.pringLogHashMap(vo);
//				
//				tempVo.setSsvcLineTypeCd(vo.get("ssvc_line_type_cd").toString());
//				tempVo.setSipCreateTypeCd(vo.get("sip_create_type_cd").toString());
//				tempVo.setSassignTypeCd(vo.get("sassign_type_cd").toString());
//				tempVo.setPipprefix(vo.get("pip_prefix").toString());
//				tempVo.setSicisofficescode(vo.get("sicisofficescode").toString());
//				tempVo.setSsubscmstip(vo.get("ssubscmstip").toString());
//				tempVo.setSmodelname(vo.get("smodelname").toString());
//				tempVo.setSgatewayip(vo.get("sgatewayip").toString());
//				tempVo.setSsubscnnescode(vo.get("ssubscnescode").toString());
//				tempVo.setSsubscnealias(vo.get("ssubscnealias").toString());
//				tempVo.setPipprefixRange(vo.get("pip_prefix_range").toString());
//				tempVo.setSllnum(vo.get("sllnum").toString());
//				tempVo.setSsubsclgipportdescription(vo.get("ssubsclgipportdescription").toString());
//				tempVo.setSsubsclgipportip(vo.get("ssubsclgipportip").toString());
//				tempVo.setSsubscrouterserialip(vo.get("ssubscrouterserialip").toString());
//				tempVo.setSconnalias(vo.get("sconnalias").toString());
//				
//				dummyList.add(tempVo);
//			}
//			
//			
//			// ------------------- 시나리오 끝 ------------------- 
//			
//			List<String> mapping2 = new ArrayList<String>(); 
//			mapping2.add("코드|getCode");
//			mapping2.add("명칭|getName");
//			
//			List<String> mapping3 = new ArrayList<String>(); 
//			mapping3.add("코드|getCode");
//			mapping3.add("명칭|getName");
//			
//			List<String> mapping4 = new ArrayList<String>(); 
//			mapping4.add("코드|getCode");
//			mapping4.add("명칭|getName");
//			
//			List<String> mapping5 = new ArrayList<String>(); 
//			mapping5.add("코드|getCode");
//			mapping5.add("명칭|getName");
//			
//			List<String> mapping6 = new ArrayList<String>(); 
//			mapping6.add("코드|getCode");
//			mapping6.add("명칭|getName");
//			
//			List<String> mapping7 = new ArrayList<String>(); // 장비목록
//			mapping7.add("수용국코드|getSofficecode");
//			mapping7.add("수용국|getSofficename");
//			mapping7.add("장비명|getSsubscnealias");
//			mapping7.add("모델명|getSmodelname");
//			mapping7.add("장비대표IP|getSsubscmstip");
//			mapping7.add("시설표준코드|getSsubscnnescode");
//			
//			mappingList.add(mapping1);
//			mappingList.add(mapping2);
//			mappingList.add(mapping3);
//			mappingList.add(mapping4);
//			mappingList.add(mapping5);
//			mappingList.add(mapping6);
//			mappingList.add(mapping7);
//						
//			voList.add(dummyList);
//			voList.add(svcLineTypeCds);
//			voList.add(sLvlSubvCds);
//			voList.add(sipVersionTypeCds);
//			voList.add(sipCreateTypeCds);
//			voList.add(sassignTypeCds);
//			voList.add(hostList);
//			
//			// String fileName = excelUtil.createExcelFile(hostList, mappingList, request);
//			String fileName = excelUtil.createIpUploadExcelFile(voList, mappingList, sheetNames, request);
//			
//			if (StringUtils.hasText(fileName)) {
//				resultVo.setFileName(fileName);
//				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			} else {
//				throw new ServiceException("CMN.HIGH.00050");
//			}
//			
//		}catch (ServiceException e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);
//			resultVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//			resultVo.setCommonMsg(msgDesc);
//		}
//		
//		return resultVo;
//	}
//	
//	/**
//	 * MIG - IP Upload 관리 - 엑셀 양식 다운로드
//	 * @param searchVo
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value="/ipmgmt/ipuploadmgmt/downloadformatMig1_1.json", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public FileVo viewMigExcelDown1_1(@ModelAttribute("searchVo") TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
//		
//		FileVo resultVo = new FileVo();
//		
//		try{
//			// Step1. 공통 코드 
//			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
//			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
//			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
//			
//			// Step1. 선택한 계위 정보의 장비 목록 시설정보 가져오기
//			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
//		
//			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
//			tbLvlMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
//			tbLvlMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
//			tbLvlMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
//			 
//			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
//			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
//			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
//			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			// 수용국 조회
//			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
//			
//			// resultListVo = allocMgmtService.selectListNeMst(searchVo);
//			
//			// sAssignTypeCD 조회 (할당유형)
//			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
//			
//			
//			// Step2. 엑셀 생성 
//			List<IpAllocOperMstVo> hostList = ipUploadMgmtService.selectListTbIpHostMst(searchVo,tbLvlBasVo);
//			// List<Map<String,Object>> dummyList = new ArrayList<Map<String,Object>>();
//			List<TbIpUploadVo> dummyList = new ArrayList<TbIpUploadVo>();
//			
//			List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","서비스망코드","수용국코드","IP버전코드","IP생성유형코드","IP할당유형코드","장비목록")); // 시트명
//			// List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","SvcLineTypeCd","HostSofficeList","VersionTypeCd","createType","OrgSassignType"));
//			List<List<?>> voList = new ArrayList<List<?>>(); // 데이터 값 모음
//			List<List<String>> mappingList = new ArrayList<List<String>>(); // 매핑
//			
//			// 템플릿
//			List<String> mapping1 = new ArrayList<String>();
//			mapping1.add("(필수)서비스망유형CD|getSsvcLineTypeCd|true"); 
//			mapping1.add("(필수)IP생성유형코드|getSipCreateTypeCd|true");
//			mapping1.add("(필수)IP할당유형코드|getSassignTypeCd|true");
//			mapping1.add("(필수)IP블록|getPipprefix|true");
//			mapping1.add("(필수)IP블록 입력범위|getPipprefixRange|true");
//			mapping1.add("수용국코드|getSicisofficescode");
//			mapping1.add("장비대표IP|getSsubscmstip");
//			mapping1.add("장치모델명|getSmodelname");
//			mapping1.add("(필수)게이트웨이IP|getSgatewayip|true");
//			mapping1.add("시설표준코드|getSsubscnnescode"); //
//			mapping1.add("장비별칭|getSsubscnealias"); //
//			mapping1.add("전용번호|getSllnum"); //
//			mapping1.add("인터페이스명|getSsubsclgipportdescription"); //
//			mapping1.add("국축스위치시리얼IP|getSsubsclgipportip"); // 국축스위치시리얼IP
//			mapping1.add("가입자축스위치시리얼IP|getSsubscrouterserialip"); // 가입자축스위치시리얼IP
//			mapping1.add("수용회선명|getSconnalias"); // 수용회선명
//			mapping1.add("비고|getScomment");
//			
//			/* MIG */
//			List<Map<String, Object>> tmpListVo = new ArrayList<Map<String,Object>>();
//			tmpListVo = ipUploadMgmtService.selectListMigIpBlock1(searchVo);
//			
//			if("CL0001".equals(searchVo.getSsvcLineTypeCd())) {
//				
//				for(int i=0;i<256;i=i+2){
//					TbIpUploadVo tempVo = new TbIpUploadVo();
//					tempVo.setSsvcLineTypeCd("CL0001");
//					tempVo.setSipCreateTypeCd("CT0005");
//					tempVo.setSassignTypeCd("SA9035");
//					tempVo.setPipprefix("192.168."+i+".0/23");
//					tempVo.setSicisofficescode("Z00003");
//					tempVo.setSsubscmstip("125.145.56.5");
//					tempVo.setSmodelname("ZFV_GS4000-160ES");
//					tempVo.setSgatewayip("192.168."+(i+1)+".254");
//					tempVo.setSsubscnnescode("GGHS03592");
//					tempVo.setSsubscnealias("Balan-MDG005");
//					tempVo.setPipprefixRange("192.168.0.0/16");
//					tempVo.setSllnum("021234567890");
//					tempVo.setSsubsclgipportdescription("IPMS interface 0001");
//					tempVo.setSsubsclgipportip("115.21.58.1");
//					tempVo.setSsubscrouterserialip("115.21.58.2");
//					tempVo.setSconnalias("여의도센터");
//					tempVo.setScomment("비고테스트");
//					dummyList.add(tempVo);
//				}
//				
//			} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "172.16.0.0/16".equals(searchVo.getPipprefixRange())) {
//				
//				for(int i=0;i<256;i=i+2){
//					TbIpUploadVo tempVo = new TbIpUploadVo();
//					tempVo.setSsvcLineTypeCd("CL0002");
//					tempVo.setSipCreateTypeCd("CT0005");
//					tempVo.setSassignTypeCd("SA1035");
//					tempVo.setPipprefix("172.16."+i+".0/23");
//					tempVo.setSicisofficescode("Z00003");
//					tempVo.setSsubscmstip("125.145.56.5");
//					tempVo.setSmodelname("ZFV_GS4000-160ES");
//					tempVo.setSgatewayip("172.16."+(i+1)+".254");
//					tempVo.setSsubscnnescode("GGHS03592");
//					tempVo.setSsubscnealias("Balan-MDG005");
//					tempVo.setPipprefixRange("172.16.0.0/16");
//					tempVo.setSllnum("021234567890");
//					tempVo.setSsubsclgipportdescription("IPMS interface 0001");
//					tempVo.setSsubsclgipportip("115.21.58.1");
//					tempVo.setSsubscrouterserialip("115.21.58.2");
//					tempVo.setSconnalias("여의도센터");
//					tempVo.setScomment("비고테스트");
//					dummyList.add(tempVo);
//				}
//				
//			} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "10.221.0.0/16".equals(searchVo.getPipprefixRange())) {
//				
//				for(int i=0;i<256;i=i+2){
//					TbIpUploadVo tempVo = new TbIpUploadVo();
//					tempVo.setSsvcLineTypeCd("CL0002");
//					tempVo.setSipCreateTypeCd("CT0005");
//					tempVo.setSassignTypeCd("SA1035");
//					tempVo.setPipprefix("10.221."+i+".0/23");
//					tempVo.setSicisofficescode("Z00003");
//					tempVo.setSsubscmstip("125.145.56.5");
//					tempVo.setSmodelname("ZFV_GS4000-160ES");
//					tempVo.setSgatewayip("10.221."+(i+1)+".254");
//					tempVo.setSsubscnnescode("GGHS03592");
//					tempVo.setSsubscnealias("Balan-MDG005");
//					tempVo.setPipprefixRange("10.221.0.0/16");
//					tempVo.setSllnum("021234567890");
//					tempVo.setSsubsclgipportdescription("IPMS interface 0001");
//					tempVo.setSsubsclgipportip("115.21.58.1");
//					tempVo.setSsubscrouterserialip("115.21.58.2");
//					tempVo.setSconnalias("여의도센터");
//					tempVo.setScomment("비고테스트");
//					dummyList.add(tempVo);
//				}
//				
//			} else {
//				
//				for(Map<String,Object> vo : tmpListVo) {
//					
//					TbIpUploadVo tempVo = new TbIpUploadVo();
//
//					//PrintLogUtil.pringLogHashMap(vo);
//					
//					tempVo.setSsvcLineTypeCd(vo.get("ssvc_line_type_cd").toString());
//					tempVo.setSipCreateTypeCd(vo.get("sip_create_type_cd").toString());
//					tempVo.setSassignTypeCd(vo.get("sassign_type_cd").toString());
//					tempVo.setPipprefix(vo.get("pip_prefix").toString());
//					tempVo.setSicisofficescode(vo.get("sicisofficescode").toString());
//					tempVo.setSsubscmstip(vo.get("ssubscmstip").toString());
//					tempVo.setSmodelname(vo.get("smodelname").toString());
//					tempVo.setSgatewayip(vo.get("sgatewayip").toString());
//					tempVo.setSsubscnnescode(vo.get("ssubscnescode").toString());
//					tempVo.setSsubscnealias(vo.get("ssubscnealias").toString());
//					tempVo.setPipprefixRange(vo.get("pip_prefix_range").toString());
//					tempVo.setSllnum(vo.get("sllnum").toString());
//					tempVo.setSsubsclgipportdescription(vo.get("ssubsclgipportdescription").toString());
//					tempVo.setSsubsclgipportip(vo.get("ssubsclgipportip").toString());
//					tempVo.setSsubscrouterserialip(vo.get("ssubscrouterserialip").toString());
//					tempVo.setSconnalias(vo.get("sconnalias").toString());
//					
//					dummyList.add(tempVo);
//				}
//				
//				
//			}
//			
//			
//			// ------------------- 시나리오 끝 ------------------- 
//			
//			List<String> mapping2 = new ArrayList<String>(); 
//			mapping2.add("코드|getCode");
//			mapping2.add("명칭|getName");
//			
//			List<String> mapping3 = new ArrayList<String>(); 
//			mapping3.add("코드|getCode");
//			mapping3.add("명칭|getName");
//			
//			List<String> mapping4 = new ArrayList<String>(); 
//			mapping4.add("코드|getCode");
//			mapping4.add("명칭|getName");
//			
//			List<String> mapping5 = new ArrayList<String>(); 
//			mapping5.add("코드|getCode");
//			mapping5.add("명칭|getName");
//			
//			List<String> mapping6 = new ArrayList<String>(); 
//			mapping6.add("코드|getCode");
//			mapping6.add("명칭|getName");
//			
//			List<String> mapping7 = new ArrayList<String>(); // 장비목록
//			mapping7.add("수용국코드|getSofficecode");
//			mapping7.add("수용국|getSofficename");
//			mapping7.add("장비명|getSsubscnealias");
//			mapping7.add("모델명|getSmodelname");
//			mapping7.add("장비대표IP|getSsubscmstip");
//			mapping7.add("시설표준코드|getSsubscnnescode");
//			
//			mappingList.add(mapping1);
//			mappingList.add(mapping2);
//			mappingList.add(mapping3);
//			mappingList.add(mapping4);
//			mappingList.add(mapping5);
//			mappingList.add(mapping6);
//			mappingList.add(mapping7);
//						
//			voList.add(dummyList);
//			voList.add(svcLineTypeCds);
//			voList.add(sLvlSubvCds);
//			voList.add(sipVersionTypeCds);
//			voList.add(sipCreateTypeCds);
//			voList.add(sassignTypeCds);
//			voList.add(hostList);
//			
//			// String fileName = excelUtil.createExcelFile(hostList, mappingList, request);
//			String fileName = excelUtil.createIpUploadExcelFile(voList, mappingList, sheetNames, request);
//			
//			if (StringUtils.hasText(fileName)) {
//				resultVo.setFileName(fileName);
//				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			} else {
//				throw new ServiceException("CMN.HIGH.00050");
//			}
//			
//		}catch (ServiceException e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);
//			resultVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//			resultVo.setCommonMsg(msgDesc);
//		}
//		
//		return resultVo;
//	}
//	
//	/**
//	 * MIG - IP Upload 관리 - 엑셀 양식 다운로드
//	 * @param searchVo
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value="/ipmgmt/ipuploadmgmt/downloadformatMig2.json", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public FileVo viewMigExcelDown2(@ModelAttribute("searchVo") TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
//		
//		FileVo resultVo = new FileVo();
//		
//		try{
//			// Step1. 공통 코드 
//			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
//			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
//			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
//			
//			// Step1. 선택한 계위 정보의 장비 목록 시설정보 가져오기
//			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
//		
//			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
//			tbLvlMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
//			tbLvlMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
//			tbLvlMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
//			 
//			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
//			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
//			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
//			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			tbLvlBasVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
//			tbLvlBasVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
//			tbLvlBasVo.setSsvcObjCd(searchVo.getSsvcObjCd());
//			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
//			searchVo.setNlvlMstSeq(nlvlMstSeq);
//			
//			// 수용국 조회
//			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
//			
//			// resultListVo = allocMgmtService.selectListNeMst(searchVo);
//			
//			// sAssignTypeCD 조회 (할당유형)
//			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
//			
//			
//			// Step2. 엑셀 생성 
//			List<IpAllocOperMstVo> hostList = ipUploadMgmtService.selectListTbIpHostMst(searchVo,tbLvlBasVo);
//			// List<Map<String,Object>> dummyList = new ArrayList<Map<String,Object>>();
//			List<TbIpUploadVo> dummyList = new ArrayList<TbIpUploadVo>();
//			
//			List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","서비스망코드","수용국코드","IP버전코드","IP생성유형코드","IP할당유형코드","장비목록")); // 시트명
//			// List<String> sheetNames = new ArrayList<String>(Arrays.asList("템플릿","SvcLineTypeCd","HostSofficeList","VersionTypeCd","createType","OrgSassignType"));
//			List<List<?>> voList = new ArrayList<List<?>>(); // 데이터 값 모음
//			List<List<String>> mappingList = new ArrayList<List<String>>(); // 매핑
//			
//			// 템플릿
//			List<String> mapping1 = new ArrayList<String>();
//			mapping1.add("(필수)서비스망유형CD|getSsvcLineTypeCd|true"); 
//			mapping1.add("(필수)IP생성유형코드|getSipCreateTypeCd|true");
//			mapping1.add("(필수)IP할당유형코드|getSassignTypeCd|true");
//			mapping1.add("(필수)IP블록|getPipprefix|true");
//			mapping1.add("(필수)IP블록 입력범위|getPipprefixRange|true");
//			mapping1.add("수용국코드|getSicisofficescode");
//			mapping1.add("장비대표IP|getSsubscmstip");
//			mapping1.add("장치모델명|getSmodelname");
//			mapping1.add("(필수)게이트웨이IP|getSgatewayip|true");
//			mapping1.add("시설표준코드|getSsubscnnescode"); //
//			mapping1.add("장비별칭|getSsubscnealias"); //
//			mapping1.add("전용번호|getSllnum"); //
//			mapping1.add("인터페이스명|getSsubsclgipportdescription"); //
//			mapping1.add("국축스위치시리얼IP|getSsubsclgipportip"); // 국축스위치시리얼IP
//			mapping1.add("가입자축스위치시리얼IP|getSsubscrouterserialip"); // 가입자축스위치시리얼IP
//			mapping1.add("수용회선명|getSconnalias"); // 수용회선명 
//			mapping1.add("비고|getScomment");
//			
//			/* MIG */
//			List<Map<String, Object>> tmpListVo = new ArrayList<Map<String,Object>>();
//			tmpListVo = ipUploadMgmtService.selectListMigIpBlock2(searchVo);
//			
//			for(Map<String,Object> vo : tmpListVo) {
//				
//				TbIpUploadVo tempVo = new TbIpUploadVo();
//
//				tempVo.setSsvcLineTypeCd(vo.get("ssvc_line_type_cd").toString());
//				tempVo.setSipCreateTypeCd(vo.get("sip_create_type_cd").toString());
//				tempVo.setSassignTypeCd(vo.get("sassign_type_cd").toString());
//				tempVo.setPipprefix(vo.get("pip_prefix").toString());
//				tempVo.setSicisofficescode(vo.get("sicisofficescode").toString());
//				tempVo.setSsubscmstip(vo.get("ssubscmstip").toString());
//				tempVo.setSmodelname(vo.get("smodelname").toString());
//				tempVo.setSgatewayip(vo.get("sgatewayip").toString());
//				tempVo.setSsubscnnescode(vo.get("ssubscnescode").toString());
//				tempVo.setSsubscnealias(vo.get("ssubscnealias").toString());
//				tempVo.setPipprefixRange(vo.get("pip_prefix_range").toString());
//				tempVo.setSllnum(vo.get("sllnum").toString());
//				tempVo.setSsubsclgipportdescription(vo.get("ssubsclgipportdescription").toString());
//				tempVo.setSsubsclgipportip(vo.get("ssubsclgipportip").toString());
//				tempVo.setSsubscrouterserialip(vo.get("ssubscrouterserialip").toString());
//				tempVo.setSconnalias(vo.get("sconnalias").toString());
//				tempVo.setScomment(vo.get("scomment").toString());
//				
//				dummyList.add(tempVo);
//			}
//			
//			
//			// ------------------- 시나리오 끝 ------------------- 
//			
//			List<String> mapping2 = new ArrayList<String>(); 
//			mapping2.add("코드|getCode");
//			mapping2.add("명칭|getName");
//			
//			List<String> mapping3 = new ArrayList<String>(); 
//			mapping3.add("코드|getCode");
//			mapping3.add("명칭|getName");
//			
//			List<String> mapping4 = new ArrayList<String>(); 
//			mapping4.add("코드|getCode");
//			mapping4.add("명칭|getName");
//			
//			List<String> mapping5 = new ArrayList<String>(); 
//			mapping5.add("코드|getCode");
//			mapping5.add("명칭|getName");
//			
//			List<String> mapping6 = new ArrayList<String>(); 
//			mapping6.add("코드|getCode");
//			mapping6.add("명칭|getName");
//			
//			List<String> mapping7 = new ArrayList<String>(); // 장비목록
//			mapping7.add("수용국코드|getSofficecode");
//			mapping7.add("수용국|getSofficename");
//			mapping7.add("장비명|getSsubscnealias");
//			mapping7.add("모델명|getSmodelname");
//			mapping7.add("장비대표IP|getSsubscmstip");
//			mapping7.add("시설표준코드|getSsubscnnescode");
//			
//			mappingList.add(mapping1);
//			mappingList.add(mapping2);
//			mappingList.add(mapping3);
//			mappingList.add(mapping4);
//			mappingList.add(mapping5);
//			mappingList.add(mapping6);
//			mappingList.add(mapping7);
//						
//			voList.add(dummyList);
//			voList.add(svcLineTypeCds);
//			voList.add(sLvlSubvCds);
//			voList.add(sipVersionTypeCds);
//			voList.add(sipCreateTypeCds);
//			voList.add(sassignTypeCds);
//			voList.add(hostList);
//			
//			// String fileName = excelUtil.createExcelFile(hostList, mappingList, request);
//			String fileName = excelUtil.createIpUploadExcelFile(voList, mappingList, sheetNames, request);
//			
//			if (StringUtils.hasText(fileName)) {
//				resultVo.setFileName(fileName);
//				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			} else {
//				throw new ServiceException("CMN.HIGH.00050");
//			}
//			
//		}catch (ServiceException e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);
//			resultVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//			resultVo.setCommonMsg(msgDesc);
//		}
//		
//		return resultVo;
//	}
//	
//	
//	@RequestMapping(value = "/ipmgmt/ipuploadmgmt/onlymergecancel.ajax", method = RequestMethod.POST)
//	@ResponseBody
//	@EncryptResponse
//	public Map<String,Object> onlyMergeCancel(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response)  {
//		Map<String,Object> retMap = new HashMap<String,Object>();
//		TbIpUploadVo resultListVo = new TbIpUploadVo(); 
//		File convFile = null;
//		try{
//		
//			MultipartFile file =  request.getFile("file");
//			SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//			String fileName = file.getOriginalFilename();
//			/* Sparrow - PATH_TRAVERSAL Start */
//			fileName = fileName.replaceAll("/","");
//			fileName = fileName.replaceAll("\\\\","");
//			fileName = fileName.replaceAll("&","");
//			/* Sparrow - PATH_TRAVERSAL End */
//			convFile = new File(fileName);
// 			// convFile = new File(System.getProperty("jboss.server.temp.dir")+File.separator+file.getOriginalFilename());
//			file.transferTo(convFile);
//		
//			TbIpUploadVo insertVo = new TbIpUploadVo();
//			insertVo.setsFileNm(fileName);
//			insertVo.setScreateId(jwtUtil.getUserId(request));
//			insertVo.setSmodifyId(jwtUtil.getUserId(request));
//			
//			// Step1 . 엑셀 파싱
//			List<Map<String,Object>> parseList = ipUploadMgmtService.parseUploadFile(request, insertVo, convFile);
//			
//			ipUploadMgmtService.insertTbIpUploadMst(insertVo);
//			
//			// Step2. 유효성 검증 
//			String pSvcLineTypeCd = request.getParameter("ssvcLineTypeCd");
//			String pSvcGroupCd = request.getParameter("ssvcGroupCd");
//			String pSvcObjCd = request.getParameter("ssvcObjCd");
//			insertVo.setSsvcLineTypeCd(pSvcLineTypeCd);
//			insertVo.setSsvcGroupCd(pSvcGroupCd);
//			insertVo.setSsvcObjCd(pSvcObjCd);
//			
//			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
//			TbLvlMstVo tbLvlMstVo = new TbLvlMstVo();
//			tbLvlMstVo.setSsvcLineTypeCd(pSvcLineTypeCd);
//			tbLvlMstVo.setSsvcGroupCd(pSvcGroupCd);
//			tbLvlMstVo.setSsvcObjCd(pSvcObjCd);
//			 
//			TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request,tbLvlMstVo);
//			tbLvlBasVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
//			searchasTypeVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			IpAllocOperMstVo searchOperVo = new IpAllocOperMstVo();
//			searchOperVo.setLvlMstSeqListVo(tbLvlMstListVo);
//			
//			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null); // IP 생성 유형
//			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null); // IP 버전 
//			List<CommonCodeVo> svcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null); // 서비스 라인
//			
//			// 수용국 조회
//			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchOperVo); // 수용국코드
//			
//			// sAssignTypeCD 조회 (할당유형)
//			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
//			
//			// 데이터 검증
//			List<Map<String,Object>> retList = ipUploadMgmtService.validSetUploadFile(request, insertVo, parseList, sipCreateTypeCds, sipVersionTypeCds, svcLineTypeCds, sLvlSubvCds, sassignTypeCds);
//			
//			insertVo.setScreateId(jwtUtil.getUserId(request));
//			insertVo.setSmodifyId(jwtUtil.getUserId(request));
//			
//			// Step3. IpUploadSub 등록
//			ipUploadMgmtService.insertIpUploadSub(request,parseList,insertVo);
//			
//			// Step4. 서비스용 Vo 생성 및 호출
//			ipUploadMgmtService.onlyMergeCancelCreateServiceVo(request,parseList,insertVo);
//			
//			// Step5. IPUploadMst 상태값 변경
//			ipUploadMgmtService.updateIpUploadMst(request,insertVo);
//						
//			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			resultListVo = new TbIpUploadVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);
//			resultListVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultListVo = new TbIpUploadVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//			resultListVo.setCommonMsg(msgDesc);
//		}finally{
//			try{
//				convFile.delete();
//			}catch(Exception e){
//				resultListVo = new TbIpUploadVo();
//				String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//				resultListVo.setCommonMsg(msgDesc);
//			}
//		}
//		
//		model.addAttribute("resultListVo", resultListVo);
//		retMap.put("resultListVo", resultListVo);
//		
//		return retMap;
//	}
	
	@RequestMapping(value="/ipmgmt/ipuploadmgmt/downloadtextformat.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewDownloadText(@RequestBody TbIpUploadVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		try{
			// String fileName = excelUtil.createIpUploadTextFile(request);
			// if (StringUtils.hasText(fileName)) {
			// 	resultVo.setFileName(fileName);
			// 	resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			// } else {
			// 	throw new ServiceException("CMN.HIGH.00050");
			// }
			return excelDownloadService.generateAndDownloadTxt(request);
		}catch (ServiceException e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
}