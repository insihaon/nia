package com.kt.ipms.legacy.ipmgmt.allocmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
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

import com.codej.base.property.FileStorageProperties;
import com.codej.web.annotation.EncryptResponse;
// import com.kt.ipms.legacy.cmn.web.FileController;
import com.codej.web.controller.FileController;
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class AllocMgmtController extends CommonController {

	@Autowired
	private AllocMgmtService allocMgmtService;

	@Autowired
	protected FileController fileController;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	/**
	 * IP 할당 목록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMst.model", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListIpBlockMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		/** 계위 Seq 목록 조회 **/
		TbLvlMstVo searchSeqVo = new TbLvlMstVo();
		searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		searchSeqVo.setSsvcGroupCdMulti(searchVo.getSsvcGroupCdMulti());
		searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

		TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
		searchVo.setLvlMstSeqListVo(resultSeqList);

		setPagination(searchVo);
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListIpAllocMst(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMst.do", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String selectListIpBlockMst(@ModelAttribute("searchVo") IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = ipBlockMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewListIpAllocMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewListIpAlcMst";
	}

	private ModelMap ipBlockMstModel(@ModelAttribute("searchVo") IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		String sloadFlg = "T"; // 메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			assignLevelCdParamMap.put("startCd", "IA0004");
			assignLevelCdParamMap.put("endCd", "IA0006");
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD,
					assignLevelCdParamMap);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("sassignLevelCds", sassignLevelCds);

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

			if (null != searchVo.getSsvcGroupCdMultiStr() && !"".equals(searchVo.getSsvcGroupCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] ssvcGroupCdMulti = searchVo.getSsvcGroupCdMultiStr().split(";");
				for (int i = 0; i < ssvcGroupCdMulti.length; i++) {
					listString.add(ssvcGroupCdMulti[i]);
				}
				searchVo.setSsvcGroupCdMulti(listString);
			}

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCdMulti(searchVo.getSsvcGroupCdMulti());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			/** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectLoadOfficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);

			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);

			// 멀티셀렉트
			if (null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for (int i = 0; i < sAssignTypeMulti.length; i++) {
					listString.add(sAssignTypeMulti[i]);
				}

				searchVo.setSassignTypeMulti(listString);
			}
			/** 할당 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getLlSrchTypeCd())) {
				searchVo.setLlSrchTypeCd("llnum");
			}
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
				sloadFlg = "T";
			} else {
				sloadFlg = "F";
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소 / 회선, 할당 기반) */
			if (StringUtils.hasText(searchVo.getMoveType())) {
				if (searchVo.getMoveType().equals("llnum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSllnum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				} else if (searchVo.getMoveType().equals("ordernum")
						&& StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSordernum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}
				sloadFlg = "F";
			} else {
				if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
					sloadFlg = "F";
				}

				if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
					searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
					sloadFlg = "F";
				}
			}

			setPagination(searchVo);
			if (sloadFlg.equals("T")) {
				resultListVo = new IpAllocOperMstListVo();
				resultListVo.setTotalCount(0);
			} else {
				resultListVo = allocMgmtService.selectListIpAllocMst(searchVo);
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
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
	 * IP 할당 목록 엑셀 저장
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMstExcel.json")
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> selectListIpBlockMstExcel(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
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
			// 멀티셀렉트
			if (null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for (int i = 0; i < sAssignTypeMulti.length; i++) {
					listString.add(sAssignTypeMulti[i]);
				}

				searchVo.setSassignTypeMulti(listString);
			}

			if (null != searchVo.getSsvcGroupCdMultiStr() && !"".equals(searchVo.getSsvcGroupCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] ssvcGroupCdMulti = searchVo.getSsvcGroupCdMultiStr().split(";");
				for (int i = 0; i < ssvcGroupCdMulti.length; i++) {
					listString.add(ssvcGroupCdMulti[i]);
				}
				searchVo.setSsvcGroupCdMulti(listString);
			}

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCdMulti(searchVo.getSsvcGroupCdMulti());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			/** 할당 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소 / 회선, 할당 기반) */
			if (StringUtils.hasText(searchVo.getMoveType())) {
				if (searchVo.getMoveType().equals("llnum") && StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSllnum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				} else if (searchVo.getMoveType().equals("ordernum")
						&& StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSordernum(searchVo.getMoveSearchWrd());
					searchVo.setLlSrchTypeCd(searchVo.getMoveType());
				}
			} else {
				if (StringUtils.hasText(searchVo.getMoveSearchWrd())) {
					searchVo.setSearchWrd(searchVo.getMoveSearchWrd());
				}

				if (StringUtils.hasText(searchVo.getMoveSipVersionTypeCd())) {
					searchVo.setSipVersionTypeCd(searchVo.getMoveSipVersionTypeCd());
				}
			}

			IpAllocOperMstListVo resultListVo = allocMgmtService.selectListIpAllocMstExcel(searchVo);

			List<String> mappingList = new ArrayList<String>();
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("할당상태|getSassignLevelNm");
			mappingList.add("회선|getNipAllocMstCnt");
			mappingList.add("작업일자|getDmodifyDt");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("장비명|getSsubscnealias");
			mappingList.add("I/F명|getSsubsclgipportdescription");
			mappingList.add("비고|getScomment");
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getIpAllocOperMstVos(), mappingList,
					request);
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
	 * IP 시설정보 조회 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewSearchtNeMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchtNeMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {

		// ModelMap builtModel = searchtNeMstModel(searchVo, request);
		List<CommonCodeVo> sLvlSubvCds = new ArrayList<CommonCodeVo>();
		IpAllocOperMstListVo resultListVo = null;

		try {
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());

			if (null != searchVo.getSsvcGroupCdMultiStr() && !"".equals(searchVo.getSsvcGroupCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] ssvcGroupCdMulti = searchVo.getSsvcGroupCdMultiStr().split(";");
				for (int i = 0; i < ssvcGroupCdMulti.length; i++) {
					listString.add(ssvcGroupCdMulti[i]);
				}
				searchSeqVo.setSsvcGroupCdMulti(listString);
			}

			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			String sSrchTypeCd = searchVo.getSneSrchTypeCd();
			/** 수용국 코드 설정 **/
			// 수용국 조회 시 할당 기준인지 호스트 기준인지의 처리
			if (sSrchTypeCd.equals("1")) {// 1. ALLOC 할당기준, 2.HOST 수용국
				/** 수용국 코드 설정 **/
				sLvlSubvCds = allocMgmtService.selectLoadOfficeList(searchVo);
				model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			} else {
				sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchVo);
				model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			}
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return createResultList(sLvlSubvCds, sLvlSubvCds.size());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewSearchtNeMst.ajax", method = RequestMethod.POST)
	public String viewSearchtNeMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = searchtNeMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewSearchtNeMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewSearchtNeMst";
	}

	private ModelMap searchtNeMstModel(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());

			if (null != searchVo.getSsvcGroupCdMultiStr() && !"".equals(searchVo.getSsvcGroupCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] ssvcGroupCdMulti = searchVo.getSsvcGroupCdMultiStr().split(";");
				for (int i = 0; i < ssvcGroupCdMulti.length; i++) {
					listString.add(ssvcGroupCdMulti[i]);
				}
				searchSeqVo.setSsvcGroupCdMulti(listString);
			}

			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			String sSrchTypeCd = searchVo.getSneSrchTypeCd();
			/** 수용국 코드 설정 **/
			// 수용국 조회 시 할당 기준인지 호스트 기준인지의 처리
			if (sSrchTypeCd.equals("1")) {// 1. ALLOC 할당기준, 2.HOST 수용국
				/** 수용국 코드 설정 **/
				List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectLoadOfficeList(searchVo);
				model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			} else {
				List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectHostSofficeList(searchVo);
				model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			}

			resultListVo = allocMgmtService.selectListNeMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}

	/**
	 * IP 시설정보 조회 화면 결과
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/selectSearchtNeMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectSearchtNeMst(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstListVo resultListVo = null;
		try {

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			resultListVo = allocMgmtService.selectListNeMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			resultListVo.setSrchTypeFlag(searchVo.getSneSrchTypeCd());
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	/**
	 * IP 할당 정보 회선 조회
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailSubSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailSubSvcMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAllocMstVo resultVo = allocMgmtService.selectIpAllocDetail(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailSubSvcMst.ajax", method = RequestMethod.POST)
	public String viewDetailSubSvcMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewDetailSubSvcMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewDetailSubSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewDetailSubSvcMst";
	}

	private ModelMap viewDetailSubSvcMstModel(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAllocMstVo resultVo = null;
		try {

			/* 할당 기준의 수용국 조회일 경우 처리 */
			if (searchVo.getNipAllocMstSeq() == null) {
				/** 계위 Seq 목록 조회 **/
				TbLvlMstVo searchSeqVo = new TbLvlMstVo();
				searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
				searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

				TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
				searchVo.setLvlMstSeqListVo(resultSeqList);
			}

			resultVo = allocMgmtService.selectIpAllocDetail(searchVo);

			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/**
	 * IP 할당 정보 링크 조회
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailLinkMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailLinkMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAllocMstVo resultVo = allocMgmtService.selectIpAllocDetailLink(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailLinkMst.ajax", method = RequestMethod.POST)
	public String viewDetailLinkMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
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
		ModelMap builtModel = viewDetailLinkMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewDetailLinkMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewDetailLinkMst";
	}

	private ModelMap viewDetailLinkMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAllocMstVo resultVo = null;
		try {

			/* 할당 기준의 수용국 조회일 경우 처리 */
			if (searchVo.getNipAllocMstSeq() == null) {
				/** 계위 Seq 목록 조회 **/
				TbLvlMstVo searchSeqVo = new TbLvlMstVo();
				searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
				searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

				TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
				searchVo.setLvlMstSeqListVo(resultSeqList);
			}

			resultVo = allocMgmtService.selectIpAllocDetailLink(searchVo);

			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}

		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/**
	 * IP 할당정보 세부 조회
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailAlcIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailAlcIPMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListIpAllocDetail(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailAlcIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailAlcIPMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewDetailAlcIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewDetailAlcIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewDetailAlcIPMst";
	}

	private ModelMap viewDetailAlcIPMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {

			resultListVo = allocMgmtService.selectListIpAllocDetail(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		model.addAttribute("resultListVo", resultListVo);
		return model;
	}

	/**
	 * IP 할당 정보 팝업 조회
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewInsertAlcIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertAlcIPMst(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo, ModelMap model,
			HttpServletRequest request) {
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListAlcIPMstViaInMstSeq(ipAllocOperMstListVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewInsertAlcIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertAlcIPMst(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo searchVoClone = new IpAllocOperMstVo();
		try {
			CloneUtil.copyObjectInformation(ipAllocOperMstListVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertAlcIPMstModel(ipAllocOperMstListVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewInsertAlcIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewInsertAlcIPMst";
	}

	private ModelMap viewInsertAlcIPMstModel(@RequestBody IpAllocOperMstListVo ipAllocOperMstListVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		int totalCount = 0;
		try {

			totalCount = ipAllocOperMstListVo.getTotalCount();
			resultListVo = allocMgmtService.selectListAlcIPMstViaInMstSeq(ipAllocOperMstListVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("totalCount", totalCount);
		return model;
	}

	/**
	 * IP 시설정보 조회 화면 결과
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/selectDetailSubSvcMstList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectDetailSubSvcMstList(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstListVo resultListVo = null;
		try {
			resultListVo = allocMgmtService.selectDetailSubSvcMstList(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setCommonMsg(e.getMessage());
			resultListVo.setTotalCount(0);
		}
		return resultListVo;
	}

	/**
	 * IP 할당 정보 처리
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/insertAlcIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertAlcIPMst(@RequestBody IpAllocMstComplexVo ipAllocMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo resultVo = null;
		try {
			if (ipAllocMstComplexVo == null || ipAllocMstComplexVo.getSrcIpAllocMstVo() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			ipAllocMstComplexVo.getSrcIpAllocMstVo().setScreateId(jwtUtil.getUserId(request));
			ipAllocMstComplexVo.getSrcIpAllocMstVo().setSmodifyId(jwtUtil.getUserId(request));
			IpAllocOperMstVo srcIpAllocMstVo = ipAllocMstComplexVo.getSrcIpAllocMstVo();

			ipAllocMstComplexVo.setScreateId(jwtUtil.getUserId(request));
			ipAllocMstComplexVo.setSmodifyId(jwtUtil.getUserId(request));
			allocMgmtService.insertListAllocIPMst(ipAllocMstComplexVo);
			resultVo = new IpAllocOperMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/* 해지 처리 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/deletAlcIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteAlcIPMst(@RequestBody IpAllocMstComplexVo ipAllocMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstVo resultVo = null;
		try {
			if (ipAllocMstComplexVo == null || ipAllocMstComplexVo.getSrcIpAllocMstVo() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			ipAllocMstComplexVo.getSrcIpAllocMstVo().setSmodifyId(jwtUtil.getUserId(request));
			IpAllocOperMstVo srcIpAllocMstVo = ipAllocMstComplexVo.getSrcIpAllocMstVo();
			srcIpAllocMstVo.setSmodifyId(jwtUtil.getUserId(request));
			ipAllocMstComplexVo.setScreateId(jwtUtil.getUserId(request));
			ipAllocMstComplexVo.setSmodifyId(jwtUtil.getUserId(request));
			allocMgmtService.deleteListAllocIPMst(ipAllocMstComplexVo);
			resultVo = new IpAllocOperMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/* 블록 반납 처리 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/updateAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateAsgnIPMst(@RequestBody TbIpAssignMstComplexVo tbIpAssignMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo resultVo = null;
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			tbLvlBasVo.setSsvcLineTypeCd(srcIpAssignMstVo.getSsvcLineTypeCd());
			tbLvlBasVo.setSsvcGroupCd(srcIpAssignMstVo.getSsvcGroupCd());
			tbLvlBasVo.setSsvcObjCd(srcIpAssignMstVo.getSsvcObjCd());
			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
			srcIpAssignMstVo.setNlvlMstSeq(nlvlMstSeq);
			srcIpAssignMstVo.setSmodifyId(jwtUtil.getUserId(request));
			allocMgmtService.updateListAsgnIPMst(tbIpAssignMstComplexVo);
			resultVo = new TbIpAssignMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
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

	/* 비고 수정 처리 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/updateScommentAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateScommentAsgnIPMst(@RequestBody TbIpAssignMstComplexVo tbIpAssignMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo resultVo = null;
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			srcIpAssignMstVo.setSmodifyId(jwtUtil.getUserId(request));
			tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);

			allocMgmtService.updateListScommentAllocIPMst(tbIpAssignMstComplexVo);
			resultVo = new TbIpAssignMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
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

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMstByMain.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListIpAllocMstByMain(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		ModelMap resultModel = new ModelMap();

		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListPageMainIpAssignMst(searchVo);

		TbIpHostMstVo tbHostInfoVo = new TbIpHostMstVo();
		if (searchVo.getSearchWrd().equals("CV0001")) {
			tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
			tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
		} else if (searchVo.getSearchWrd().equals("CV0002")) {
			tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
			tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
		} else if (searchVo.getSearchWrd().equals("SAID")) {
			tbHostInfoVo.setSsaid(searchVo.getSsaid());
		} else if (searchVo.getSearchWrd().equals("SLLNUM")) {
			tbHostInfoVo.setSllnum(searchVo.getSllnum());
		} else if (searchVo.getSearchWrd().equals("SCONNALIAS")) {
			tbHostInfoVo.setSconnAlias(searchVo.getSconnAlias());
		}

		TbIpHostMstListVo tbIpHostMstListVo = allocMgmtService.selectTbIpHostInfoVo(tbHostInfoVo);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultListVo", resultListVo.getIpAllocOperMstVos());
		map.put("resultListTotalCount", resultListVo.getTotalCount());
		map.put("tbIpHostMstListVo", tbIpHostMstListVo.getTbIpHostMstVos());
		map.put("tbIpHostMstListTotalCount", tbIpHostMstListVo.getTotalCount());
		resultModel.addAttribute("result", map);
		return resultModel;
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMstByMain.ajax", method = RequestMethod.POST)
	public String selectListIpAllocMstByMain(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
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
		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewListIpAllocMstByMain.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));

		ModelMap builtModel = selectListIpAllocMstByMainModelMap(searchVo, request);
		model.addAllAttributes(builtModel);
		String rtnVal = "";

		IpAllocOperMstListVo resultListVo = (IpAllocOperMstListVo) builtModel.get("resultListVo");
		if (resultListVo.getTotalCount() == 0) {
			rtnVal = "ipmgmt/allocmgmt/viewDetailIpInfo";
		} else if (resultListVo.getTotalCount() == 1) {
			rtnVal = "ipmgmt/allocmgmt/viewDetailIpInfo";
		} else if (resultListVo.getTotalCount() > 1) {
			rtnVal = "ipmgmt/allocmgmt/viewListIpInfo";
		}
		return rtnVal;
	}

	private ModelMap selectListIpAllocMstByMainModelMap(@RequestBody IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		IpAllocOperMstVo tbIpInfoVo = null;
		TbIpHostMstVo tbHostInfoVo = null;
		// String rtnVal = "";
		try {

			setPagination(searchVo);
			resultListVo = allocMgmtService.selectListPageMainIpAssignMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

			if (resultListVo.getTotalCount() == 0) {

				tbIpInfoVo = null;

				tbHostInfoVo = new TbIpHostMstVo();

				if (searchVo.getSearchWrd().equals("CV0001")) {
					tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
					tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
				} else if (searchVo.getSearchWrd().equals("CV0002")) {
					tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
					tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
				} else if (searchVo.getSearchWrd().equals("SAID")) {
					tbHostInfoVo.setSsaid(searchVo.getSsaid());
				} else if (searchVo.getSearchWrd().equals("SLLNUM")) {
					tbHostInfoVo.setSllnum(searchVo.getSllnum());
				} else if (searchVo.getSearchWrd().equals("SCONNALIAS")) {
					tbHostInfoVo.setSconnAlias(searchVo.getSconnAlias());
				}

				TbIpHostMstListVo tbIpHostMstListVo = allocMgmtService.selectTbIpHostInfoVo(tbHostInfoVo);

				if (tbIpHostMstListVo.getTotalCount() == 1) {
					tbHostInfoVo = tbIpHostMstListVo.getTbIpHostMstVos().get(0);
				} else {
					tbHostInfoVo = null;
				}
				model.addAttribute("tbIpInfoVo", tbIpInfoVo);
				model.addAttribute("tbHostInfoVo", tbHostInfoVo);
				// rtnVal = "ipmgmt/allocmgmt/viewDetailIpInfo";

			} else if (resultListVo.getTotalCount() == 1) {

				searchVo.setNipAssignMstSeq(resultListVo.getIpAllocOperMstVos().get(0).getNipAssignMstSeq());
				searchVo.setNipAllocMstSeq(resultListVo.getIpAllocOperMstVos().get(0).getNipAllocMstSeq());
				tbIpInfoVo = allocMgmtService.selectMainIpInfoMst(searchVo);

				tbHostInfoVo = new TbIpHostMstVo();

				if (searchVo.getSearchWrd().equals("CV0001")) {
					tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
					tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
				} else if (searchVo.getSearchWrd().equals("CV0002")) {
					tbHostInfoVo.setSearchWrd(searchVo.getSfirstAddr());
					tbHostInfoVo.setSipVersionTypeCd(searchVo.getSipVersionTypeCd());
				} else if (searchVo.getSearchWrd().equals("SAID")) {
					tbHostInfoVo.setSsaid(searchVo.getSsaid());
				} else if (searchVo.getSearchWrd().equals("SLLNUM")) {
					tbHostInfoVo.setSllnum(searchVo.getSllnum());
				} else if (searchVo.getSearchWrd().equals("SCONNALIAS")) {
					tbHostInfoVo.setSconnAlias(searchVo.getSconnAlias());
				}

				TbIpHostMstListVo tbIpHostMstListVo = allocMgmtService.selectTbIpHostInfoVo(tbHostInfoVo);

				if (tbIpHostMstListVo.getTotalCount() == 1) {
					tbHostInfoVo = tbIpHostMstListVo.getTbIpHostMstVos().get(0);
				} else {
					tbHostInfoVo = null;
				}
				model.addAttribute("tbIpInfoVo", tbIpInfoVo);
				model.addAttribute("tbHostInfoVo", tbHostInfoVo);
				// rtnVal = "ipmgmt/allocmgmt/viewDetailIpInfo";

			} else if (resultListVo.getTotalCount() > 1) {
				model.addAttribute("resultListVo", resultListVo);
				PaginationInfo paginationInfo = getPaginationInfo();
				paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
				model.addAttribute("paginationInfo", paginationInfo);
				// rtnVal = "ipmgmt/allocmgmt/viewListIpInfo";
			}
			model.addAttribute("searchVo", searchVo);

		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
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

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListIpAllocMstByMainExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> selectListIpAllocMstByMainExcel(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		IpAllocOperMstListVo resultListVo = null;
		FileVo resultVo = new FileVo();

		try {

			setPagination(searchVo);
			resultListVo = allocMgmtService.selectListPageMainIpAssignMstExcel(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

			List<String> mappingList = new ArrayList<String>();
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("시작 IP|getSfirstAddr");
			mappingList.add("끝 IP|getSlastAddr");
			mappingList.add("BitMask|getNbitmask");
			mappingList.add("할당상태|getSassignLevelNm");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("SAID|getSsaid");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("수용회선명|getSconnAlias");
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getIpAllocOperMstVos(), mappingList,
					request);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}

		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailIpAllocMstByMain.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectTbUserBas(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		IpAllocOperMstVo tbIpInfoVo = allocMgmtService.selectMainIpInfoMst(searchVo);
		return createResult(tbIpInfoVo);
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailIpAllocMstByMain.ajax", method = RequestMethod.POST)
	public String selectTbUserBas(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = selectTbUserBasModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewDetailIpAllocMstByMain.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewDetailIpInfoPop";
	}

	private ModelMap selectTbUserBasModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstVo tbIpInfoVo = null;
		// TbIpHostMstVo tbHostInfoVo = null;
		try {

			tbIpInfoVo = allocMgmtService.selectMainIpInfoMst(searchVo);
			tbIpInfoVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			tbIpInfoVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			tbIpInfoVo = new IpAllocOperMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			tbIpInfoVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("tbIpInfoVo", tbIpInfoVo);
		// model.addAttribute("tbHostInfoVo", tbHostInfoVo);
		return model;
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListMultiIpInfo.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListMultiIpInfo(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListMainSearchMultiIp(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListMultiIpInfo.do", method = RequestMethod.POST)
	public String viewListMultiIpInfo(@ModelAttribute("searchVo") IpAllocOperMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewListMultiIpInfoModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewListMultiIpInfo.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewListMultiIpInfo";
	}

	private ModelMap viewListMultiIpInfoModel(@ModelAttribute("searchVo") IpAllocOperMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {

			if (searchVo.getSearchWrd() != null) {
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrd().replace("\r\n", ",").replace("/t", ",");
				searchVo.setSearchWrd(tempWrd);
			}

			resultListVo = allocMgmtService.selectListMainSearchMultiIp(searchVo);

			if (searchVo.getSearchWrd() != null) {
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrd().replace("\r\n", "\r\n").replace("/t", "\r\n").replace(",", "\r\n");
				searchVo.setSearchWrd(tempWrd);
			}

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		// PaginationInfo paginationInfo = getPaginationInfo();
		// paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		// model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListMultiIpInfoExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListMultiIpInfoExcel(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {

			if (searchVo.getSearchWrd() != null) {
				String tempWrd = "";
				tempWrd = searchVo.getSearchWrd().replace("\r\n", ",").replace("/t", ",");
				searchVo.setSearchWrd(tempWrd);
			}

			IpAllocOperMstListVo resultListVo = allocMgmtService.selectListMainSearchMultiIpExcel(searchVo);

			List<String> mappingList = new ArrayList<String>();
			mappingList.add("검색 IP|getSearchWrd");
			mappingList.add("IP 주소|getPipPrefix");
			mappingList.add("시작 IP|getSfirstAddr");
			mappingList.add("끝 IP|getSlastAddr");
			mappingList.add("BitMask|getNbitmask");
			mappingList.add("할당상태|getSassignLevelNm");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("SAID|getSsaid");
			mappingList.add("전용번호|getSllnum");
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getIpAllocOperMstVos(), mappingList,
					request);
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
	 * VPN IP현황 목록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListVpnIPStat.model", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListVPNIPStat(@RequestBody TbIpAllocMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAllocMstListVo resultListVo = allocMgmtService.selectListVpnIPStat(searchVo);
		return createResultList(resultListVo.getTbIpAllocMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListVpnIPStat.do", method = RequestMethod.POST)
	public String selectListVPNIPStat(@ModelAttribute("searchVo") TbIpAllocMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = selectListVPNIPStatModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewListVpnIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewListVpnIPStat";
	}

	private ModelMap selectListVPNIPStatModel(@ModelAttribute("searchVo") TbIpAllocMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAllocMstListVo resultListVo = null;
		String sloadFlg = "T"; // 메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);

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

			/** 서비스 목록 조회 **/
			List<CommonCodeVo> sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);

			/** 현황 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
				sloadFlg = "T";
			} else {
				sloadFlg = "F";
			}
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			setPagination(searchVo);
			if (sloadFlg.equals("T")) {
				resultListVo = new TbIpAllocMstListVo();
				resultListVo.setTotalCount(0);
			} else {
				resultListVo = allocMgmtService.selectListVpnIPStat(searchVo);
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAllocMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpAllocMstListVo();
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
	 * VPN IP현황 목록 엑셀 저장
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewListVpnIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> selectListVPNIPStatExcel(@RequestBody TbIpAllocMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
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

			/** 현황 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			setPagination(searchVo);
			TbIpAllocMstListVo resultListVo = allocMgmtService.selectListVpnIPStatExcel(searchVo);

			List<String> mappingList = new ArrayList<String>();
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("계약자명|getScustname");
			mappingList.add("장비명|getSsubscnealias");
			mappingList.add("상품명|getSipmsSvcNm");
			mappingList.add("보유|getSvpnTypeNm");
			mappingList.add("작업일자|getDmodifyDt");

			// String fileName =
			// excelUtil.createExcelFile(resultListVo.getTbIpAllocMstVos(), mappingList,
			// request);

			// if (StringUtils.hasText(fileName)) {
			// resultVo.setFileName(fileName);
			// resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			// } else {
			// throw new ServiceException("CMN.HIGH.00050");
			// }
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpAllocMstVos(), mappingList,
					request);

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
	 * 조직별 서비스 정보 조회
	 * 
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/selectOrgSassignTypeCdList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectOrgSassignTypeCdList(@RequestBody TbIpAllocMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAllocMstListVo resultListVo = new TbIpAllocMstListVo();
		try {

			List<TbIpAllocMstVo> orgSassignTypeCdVos = new ArrayList<TbIpAllocMstVo>();

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			List<CommonCodeVo> sassignTypeCds = new ArrayList<CommonCodeVo>();

			sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList(searchVo);

			if (sassignTypeCds != null) {

				for (CommonCodeVo orgSassignTypeCdVo : sassignTypeCds) {
					TbIpAllocMstVo orgSassignTypeCdMstVo = new TbIpAllocMstVo();

					orgSassignTypeCdMstVo.setSassignTypeCd(orgSassignTypeCdVo.getCode());
					orgSassignTypeCdMstVo.setSassignTypeNm(orgSassignTypeCdVo.getName());
					orgSassignTypeCdVos.add(orgSassignTypeCdMstVo);
				}

				resultListVo.setTbIpAllocMstVos(orgSassignTypeCdVos);
			}

		} catch (ServiceException e) {
			resultListVo = new TbIpAllocMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAllocMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;
	}

	/**
	 * 조직별 서비스 정보 조회
	 * 
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/selectSassignTypeCdList2.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectOrgSassignTypeCdList2(@RequestBody TbIpAllocMstVo searchVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAllocMstListVo resultListVo = new TbIpAllocMstListVo();
		try {

			List<TbIpAllocMstVo> orgSassignTypeCdVos = new ArrayList<TbIpAllocMstVo>();

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			List<CommonCodeVo> sassignTypeCds = new ArrayList<CommonCodeVo>();

			sassignTypeCds = allocMgmtService.selectOrgSassignTypeCdList2(searchVo);

			if (sassignTypeCds != null) {

				for (CommonCodeVo orgSassignTypeCdVo : sassignTypeCds) {
					TbIpAllocMstVo orgSassignTypeCdMstVo = new TbIpAllocMstVo();

					orgSassignTypeCdMstVo.setSassignTypeCd(orgSassignTypeCdVo.getCode());
					orgSassignTypeCdMstVo.setSassignTypeNm(orgSassignTypeCdVo.getName());
					orgSassignTypeCdVos.add(orgSassignTypeCdMstVo);
				}

				resultListVo.setTbIpAllocMstVos(orgSassignTypeCdVos);
			}

		} catch (ServiceException e) {
			resultListVo = new TbIpAllocMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAllocMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		return resultListVo;
	}

	/**
	 * VPN IP현황 상세 조회
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailVpnIPStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailVpnIPStat(@RequestBody TbIpAllocMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAllocMstVo resultVo = allocMgmtService.selectVpnIPStatDetail(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewDetailVpnIPStat.ajax", method = RequestMethod.POST)
	public String viewDetailVpnIPStat(@RequestBody TbIpAllocMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewDetailVpnIPStatModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewDetailVpnIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewDetailVpnIPStat";
	}

	private ModelMap viewDetailVpnIPStatModel(@RequestBody TbIpAllocMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAllocMstVo resultVo = null;
		try {
			resultVo = allocMgmtService.selectVpnIPStatDetail(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpAllocMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/**
	 * IP 링크정보 조회 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/viewSearchtLnMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchtLnMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		IpAllocOperMstListVo resultListVo = allocMgmtService.selectListIpAllocMst(searchVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/allocmgmt/viewSearchtLnMst.ajax", method = RequestMethod.POST)
	public String viewSearchtLnMst(@RequestBody IpAllocOperMstVo searchVo, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
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
		ModelMap builtModel = viewSearchtLnMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/allocmgmt/viewSearchtLnMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/allocmgmt/viewSearchtLnMst";
	}

	private ModelMap viewSearchtLnMstModel(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpAllocOperMstListVo resultListVo = null;
		try {

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			/** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = allocMgmtService.selectLinkSofficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);

			// System.out.println(">>>>>>>>>>>>" + searchVo.getPifSerialIp());
			// //Codeeyes-Critical-sysout
			logger.info(">>>>>>>>>>>>" + searchVo.getPifSerialIp());
			resultListVo = allocMgmtService.selectListLnMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);

		if (resultListVo.getTotalCount() > 0 && null != searchVo.getPifSerialIp()) {
			searchVo.setSaofficescode(resultListVo.getIpAllocOperMstVos().get(0).getSaofficescode());
		}

		model.addAttribute("searchVo", searchVo);
		return model;
	}

	/**
	 * IP 링크정보 조회 화면 결과
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/allocmgmt/selectSearchtLnMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectSearchtLnMst(@RequestBody IpAllocOperMstVo searchVo, HttpServletRequest request,
			HttpServletResponse response) {
		IpAllocOperMstListVo resultListVo = null;
		try {

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());

			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			resultListVo = allocMgmtService.selectListLnMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			resultListVo.setSrchTypeFlag(searchVo.getSneSrchTypeCd());
		} catch (ServiceException e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new IpAllocOperMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

}
