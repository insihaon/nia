package com.kt.ipms.legacy.ipmgmt.linemgmt.web;

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
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.service.LineMgmtService;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubComplexVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubListVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class LineMgmtController extends CommonController {

	@Autowired
	private LineMgmtService lineMgmtService;
	
	/**
	 * 지역 선번장 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ipmgmt/linemgmt/viewListAsgnIPSub.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListAsgnIPSub(@RequestBody TbIpAssignSubVo searchVo, ModelMap model,
		HttpServletRequest request){
		TbIpAssignSubListVo resultListVo = lineMgmtService.selectListIpAssignSub(searchVo);
		return createResultList(resultListVo.getTbIpAssignSubVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ipmgmt/linemgmt/viewListAsgnIPSub.do", method = RequestMethod.POST)
	public String viewListAsgnIPSub(@ModelAttribute("searchVo") TbIpAssignSubVo searchVo, ModelMap model,
		HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo searchVoClone = new TbIpAssignSubVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListAsgnIPSubModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linemgmt/viewListAsgnIPSub.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linemgmt/viewListAsgnIPSub";
	}
	private ModelMap viewListAsgnIPSubModel(@ModelAttribute("searchVo") TbIpAssignSubVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignSubListVo resultListVo = null;
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			
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
			
			/** 수용국 코드 설정 **/
			//List<CommonCodeVo> sLvlSubvCds = commonCodeService.selectListCommonCode(CommonCodeUtil.LVL_SUB_CD, null);
			List<CommonCodeVo> sLvlSubvCds = lineMgmtService.selectLoadOfficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
			/** 조직별 서비스 유형 셋팅(2014.12.19) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = lineMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
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
			
			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소)*/
			if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
				searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
				sloadFlg = "F";
			}
			
			if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
				sloadFlg = "F";
			}
			
			setPagination(searchVo);
			if (sloadFlg.equals("T")){
				resultListVo = new TbIpAssignSubListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = lineMgmtService.selectListIpAssignSub(searchVo);
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpAssignSubListVo();
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
	
	
	/* IP선번장 엑셀 다운 */
	@RequestMapping(value="/ipmgmt/linemgmt/viewListAsgnIPSubExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListAsgnIPSubExcel(@ModelAttribute("searchVo") TbIpAssignSubVo searchVo, ModelMap model,
		HttpServletRequest request, HttpServletResponse response){
		FileVo resultVo = new FileVo();
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
			
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
						
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소)*/
			if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
				searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
			}
			
			if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
			}
			
			
			TbIpAssignSubListVo resultListVo = lineMgmtService.selectListIpAssignSubExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("구분|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			//mappingList.add("상품명|getSipmsSvcNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("용도|getNipAssignSubNm");			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbIpAssignSubVos(), mappingList, request);
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
	
	/**
	 * 지역선번장 분할 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/viewInsertDivAsgnIPSub.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertDivAsgnIPSub(@RequestBody TbIpAssignSubVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpAssignSubVo resultVo = lineMgmtService.selectIpAssignSub(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/linemgmt/viewInsertDivAsgnIPSub.ajax", method = RequestMethod.POST)
	public String viewInsertDivAsgnIPSub(@RequestBody TbIpAssignSubVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo searchVoClone = new TbIpAssignSubVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertDivAsgnIPSubModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linemgmt/viewInsertDivAsgnIPSub.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linemgmt/viewInsertDivAsgnIPSub";
	}
	private ModelMap viewInsertDivAsgnIPSubModel(@RequestBody TbIpAssignSubVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignSubVo resultVo = null;
		try {
			resultVo = lineMgmtService.selectIpAssignSub(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 지역선번장 IP블록 분할 추가
	 * @param tbIpAssignSubVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/appendDivAsgnIPSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendDivAsgnIPSub(@RequestBody TbIpAssignSubVo tbIpAssignSubVo,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubListVo resultListVo = null;
		try {
			resultListVo = lineMgmtService.appendDivIpAssignSub(tbIpAssignSubVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAssignSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	/**
	 * 지역선번장 IP블록 병합 추가
	 * @param tbIpAssignSubListVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/appendMergeDivAsgnIPSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendMergeDivAsgnIPSub(@RequestBody TbIpAssignSubListVo tbIpAssignSubListVo,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo resultVo = null;
		try {
			
			resultVo = lineMgmtService.appendMergeDivIpAssignSub(tbIpAssignSubListVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 지역선번장 IP블록 분할 확정
	 * @param tbIpAssignSubComplexVo
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/insertListDivAsgnIPSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertListDivAsgnIPSub(@RequestBody TbIpAssignSubComplexVo tbIpAssignSubComplexVo, HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo resultVo = null;
		try {
			tbIpAssignSubComplexVo.setScreateId(jwtUtil.getUserId(request));
			lineMgmtService.insertListDivAsgnIPSub(tbIpAssignSubComplexVo);
			resultVo = new TbIpAssignSubVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 지역선번장 병합 화면 
	 * @param tbIpAssignSubListVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/viewInsertMrgAsgnIPSub.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertMrgAsgnIPSub(@RequestBody TbIpAssignSubListVo tbIpAssignSubListVo, ModelMap model,
			HttpServletRequest request){
		TbIpAssignSubComplexVo resultComplexVo = lineMgmtService.validateMrgAsgnIPSub(tbIpAssignSubListVo);
		return createResult(resultComplexVo);
	}
	@RequestMapping(value = "/ipmgmt/linemgmt/viewInsertMrgAsgnIPSub.ajax", method = RequestMethod.POST)
	public String viewInsertMrgAsgnIPSub(@RequestBody TbIpAssignSubListVo tbIpAssignSubListVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		
		TbIpAssignSubVo searchVoClone = new TbIpAssignSubVo();
		try {
			CloneUtil.copyObjectInformation(tbIpAssignSubListVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertMrgAsgnIPSubModel(tbIpAssignSubListVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linemgmt/viewInsertMrgAsgnIPSub.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linemgmt/viewInsertMrgAsgnIPSub";
	}
	private ModelMap viewInsertMrgAsgnIPSubModel(@RequestBody TbIpAssignSubListVo tbIpAssignSubListVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignSubComplexVo resultComplexVo = null;
		try {
			resultComplexVo = lineMgmtService.validateMrgAsgnIPSub(tbIpAssignSubListVo);
			resultComplexVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultComplexVo = new TbIpAssignSubComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultComplexVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultComplexVo = new TbIpAssignSubComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultComplexVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultComplexVo", resultComplexVo);
		return model;
	}
	
	
	/**
	 * 지역선번장 병합 확정
	 * @param tbIpAssignSubComplexVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/insertMrgAsgnIPSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertMrgAsgnIPSub(@RequestBody TbIpAssignSubComplexVo tbIpAssignSubComplexVo,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo resultVo = null;
		try {
			tbIpAssignSubComplexVo.setScreateId(jwtUtil.getUserId(request));
			lineMgmtService.insertMrgAsgnIPSub(tbIpAssignSubComplexVo);
			resultVo = new TbIpAssignSubVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 지역선번장 상세 정보
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/viewDetailAsgnIPSub.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailAsgnIPSub(@RequestBody TbIpAssignSubVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbIpAssignSubVo resultVo = lineMgmtService.selectIpAssignSub(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/ipmgmt/linemgmt/viewDetailAsgnIPSub.ajax", method = RequestMethod.POST)
	public String viewDetailAsgnIPSub(@RequestBody TbIpAssignSubVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo searchVoClone = new TbIpAssignSubVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailAsgnIPSubModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/linemgmt/viewDetailAsgnIPSub.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/linemgmt/viewDetailAsgnIPSub";
	}
	private ModelMap viewDetailAsgnIPSubModel(@RequestBody TbIpAssignSubVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignSubVo resultVo = null;
		IpAllocOperMstListVo resultListVo = null;
		try {
			resultVo = lineMgmtService.selectIpAssignSub(searchVo);
			resultListVo = lineMgmtService.selectListIpAllocDetail(resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * 지역선번장 정보 수정
	 * @param tbIpAssignSubVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/updateAsgnIPSub.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateAsgnIPSub(@RequestBody TbIpAssignSubVo tbIpAssignSubVo,
			HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubVo resultVo = null;
		try {
			tbIpAssignSubVo.setScreateId(jwtUtil.getUserId(request));
			lineMgmtService.updateAsgnIPSub(tbIpAssignSubVo);
			resultVo = new TbIpAssignSubVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAssignSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 수용국 정보 조회
	 * @param tbIpAssignSubVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/linemgmt/selectOfficeList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectOfficeList(@RequestBody TbIpAssignSubVo searchVo, HttpServletRequest request, HttpServletResponse response){
		TbIpAssignSubListVo resultListVo = null;
		try{
			if(null != searchVo.getSsvcGroupCdMultiStr() && !"".equals(searchVo.getSsvcGroupCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] ssvcGroupCdMulti = searchVo.getSsvcGroupCdMultiStr().split(";");
				for(int i=0;i<ssvcGroupCdMulti.length;i++){
					listString.add(ssvcGroupCdMulti[i]);
				}
				searchVo.setSsvcGroupCdMulti(listString);
			}
			
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			if(null != searchVo.getSsvcGroupCd()){
				searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			}else{
				searchSeqVo.setSsvcGroupCdMulti(searchVo.getSsvcGroupCdMulti());
			}
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			resultListVo = lineMgmtService.selectOfficeList(searchVo);
			//resultListVo = jwtUtil.getNodeList(request, searchVo);
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAssignSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
}
