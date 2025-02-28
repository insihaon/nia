package com.kt.ipms.legacy.ipmgmt.assignmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.codej.web.annotation.EncryptResponse;
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.mysql.cj.util.DnsSrv.SrvRecord;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class AssignMgmtController extends CommonController {

	@Autowired
	private AssignMgmtService assignMgmtService;
	/* 배정관리 > 배정 코드 리스트 */

	@RequestMapping(value = "/ipmgmt/assignmgmt/selectSassignLevelCds.json", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	@EncryptResponse
	public ModelMap selectSassignLevelCds(@RequestBody TbIpAssignMstVo searchVo,
			HttpServletRequest request) {
		List<CommonCodeVo> sassignLevelCds = new ArrayList<CommonCodeVo>();
		try {
			/** 계위별 배정 레벨 목록 조회 **/
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			String gradeCd = jwtUtil.getUserGradeCd(request);
			if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
				assignLevelCdParamMap.put("startCd", "IA0001");
			} else if (gradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
				assignLevelCdParamMap.put("startCd", "IA0001"); // 2015-01-08 망관리잗ㅎ 미배정 보이도록 요청
			} else {
				assignLevelCdParamMap.put("startCd", "IA0003");
			}
			assignLevelCdParamMap.put("endCd", "IA0004");
			sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD,
					assignLevelCdParamMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createResultList(sassignLevelCds, sassignLevelCds.size());
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListAsgnIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListAsgnIPMst(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstListVo resultListVo = new TbIpAssignMstListVo();
		setPagination(searchVo);
		ModelMap builtModel = viewListAsgnIPMstModel(searchVo, request);
		resultListVo = (TbIpAssignMstListVo) builtModel.get("resultListVo");
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListAsgnIPMst.do", method = RequestMethod.POST)
	public String viewListAsgnIPMst(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListAsgnIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewListAsgnIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewListAsgnIPMst";
	}

	private ModelMap viewListAsgnIPMstModel(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		String sloadFlg = "T"; // 메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {
			// List<CommonCodeVo> sipCreateTypeCds =
			// commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD,
			// null);
			// List<CommonCodeVo> sipCreateSeqCds =
			// commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD,
			// null);
			// List<CommonCodeVo> sipVersionTypeCds =
			// commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD,
			// null);
			// model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			// model.addAttribute("sipCreateSeqCds", sipCreateSeqCds);
			// model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);

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
			// model.addAttribute("svcLineListVo", svcLineListVo);
			// model.addAttribute("centerListVo", centerListVo);
			// model.addAttribute("nodeListVo", nodeListVo);

			if (null != searchVo.getSsvcGroupCdMultiStr() &&
					!"".equals(searchVo.getSsvcGroupCdMultiStr())) {
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

			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			// TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			// searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			// List<CommonCodeVo> sassignTypeCds =
			// assignMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			// model.addAttribute("sassignTypeCds", sassignTypeCds);

			/** 계위별 배정 레벨 목록 조회 **/
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			String gradeCd = jwtUtil.getUserGradeCd(request);
			if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
				assignLevelCdParamMap.put("startCd", "IA0001");
			} else if (gradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
				assignLevelCdParamMap.put("startCd", "IA0001"); // 2015-01-08 망관리잗ㅎ 미배정 보이도록 요청
			} else {
				assignLevelCdParamMap.put("startCd", "IA0003");
			}
			assignLevelCdParamMap.put("endCd", "IA0004");
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD,
					assignLevelCdParamMap);
			model.addAttribute("sassignLevelCds", sassignLevelCds);

			// 시설용IP화면 서비스그룹 하위 코드 setting
			if (searchVo.getIsFacilities()) {
				TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
				/* 멀티 list 조건 추가 */
				List<String> listString = new ArrayList<String>();
				String[] sAssignGroupTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for (int i = 0; i < sAssignGroupTypeMulti.length; i++) {
					listString.add(sAssignGroupTypeMulti[i]);
				}
				searchasTypeVo.setSassignTypeMulti(listString);

				List<CommonCodeVo> sassignGroupTypeCds = assignMgmtService.selectFacilitesTypeCdList(searchasTypeVo);
				String sAssignTypeMultiStr = new String();
				for (CommonCodeVo vo : sassignGroupTypeCds) {
					sAssignTypeMultiStr = sAssignTypeMultiStr.concat(vo.getSubCodeStr());
				}
				searchVo.setSassignTypeCdMultiStr(sAssignTypeMultiStr);
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

			/** 배정 목록 조회 **/
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
			if (!StringUtils.hasText(searchVo.getSassignLevelCd())) {
				searchVo.setSearchBgnCd(sassignLevelCds.get(0).getCode());
				searchVo.setSearchEndCd(sassignLevelCds.get(sassignLevelCds.size() -
						1).getCode());
			} else {
				searchVo.setSearchBgnCd("");
				searchVo.setSearchEndCd("");
			}
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			setPagination(searchVo);
			if (sloadFlg.equals("T")) {
				resultListVo = new TbIpAssignMstListVo();
				resultListVo.setTotalCount(0);
			} else {
				setPagination(searchVo);
				resultListVo = assignMgmtService.selectListIpAssignMst(searchVo);
			}
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpAssignMstListVo();
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListAsgnIPMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListAsgnIPMstExcel(@RequestBody TbIpAssignMstVo searchVo,
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
					}
				}
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

			/** 계위별 배정 레벨 목록 조회 **/
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			String gradeCd = jwtUtil.getUserGradeCd(request);
			if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
				assignLevelCdParamMap.put("startCd", "IA0001");
			} else if (gradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
				assignLevelCdParamMap.put("startCd", "IA0002");
			} else {
				assignLevelCdParamMap.put("startCd", "IA0003");
			}
			assignLevelCdParamMap.put("endCd", "IA0004");
			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD,
					assignLevelCdParamMap);

			/** 배정 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			if (!StringUtils.hasText(searchVo.getSassignLevelCd())) {
				searchVo.setSearchBgnCd(sassignLevelCds.get(0).getCode());
				searchVo.setSearchEndCd(sassignLevelCds.get(sassignLevelCds.size() - 1).getCode());
			} else {
				searchVo.setSearchBgnCd("");
				searchVo.setSearchEndCd("");
			}

			if (null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())) {
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for (int i = 0; i < sAssignTypeMulti.length; i++) {
					listString.add(sAssignTypeMulti[i]);
				}

				searchVo.setSassignTypeMulti(listString);
			}

			TbIpAssignMstListVo resultListVo = assignMgmtService.selectListIpAssignMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("시작 IP|getSfirstAddr");
			mappingList.add("종료 IP|getSlastAddr");
			mappingList.add("단위블록수|getNclassCnt");
			mappingList.add("작업일자|getDmodifyDt");
			mappingList.add("배정상태|getSassignLevelNm");
			mappingList.add("비고|getScomment");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpAssignMstVos(), mappingList,
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertDivAsgnIPMst(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstVo resultVo = assignMgmtService.selectIpAssignMst(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertDivAsgnIPMst(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));

		ModelMap builtModel = viewInsertDivAsgnIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);
		String retPage = "";
		if (!StringUtils.hasText(searchVo.getTypeFlag()) || searchVo.getTypeFlag().equals("Asgn")) {
			retPage = "ipmgmt/assignmgmt/viewInsertDivAsgnIPMst";
		} else {
			retPage = "ipmgmt/allocmgmt/viewInsertDivAlcIPMst";
		}
		return retPage;
	}

	private ModelMap viewInsertDivAsgnIPMstModel(@RequestBody TbIpAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstVo resultVo = null;
		try {
			resultVo = assignMgmtService.selectIpAssignMst(searchVo);
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
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/appendDivAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendDivAsgnIPMst(@RequestBody TbIpAssignMstVo tbIpAssignMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			resultListVo = assignMgmtService.appendDivIpAssignMst(tbIpAssignMstVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/appendMergeDivAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendMergeDivAsgnIPMst(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo,
			HttpServletRequest request, HttpServletResponse response) {

		TbIpAssignMstVo resultVo = null;
		try {
			resultVo = assignMgmtService.appendMergeDivIpAssignMst(tbIpAssignMstListVo);
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/insertListDivAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertListDivAsgnIPMst(@RequestBody TbIpAssignMstComplexVo tbIpAssignMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo resultVo = null;
		try {
			tbIpAssignMstComplexVo.setScreateId(jwtUtil.getUserId(request));
			tbIpAssignMstComplexVo.setSmodifyId(jwtUtil.getUserId(request));
			assignMgmtService.insertListDivAsgnIPMst(tbIpAssignMstComplexVo);
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertMrgAsgnIPMst(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstComplexVo resultComplexVo = null;
		try {
			resultComplexVo = assignMgmtService.validateMrgAsgnIPMst(tbIpAssignMstListVo);
		} catch (ServiceException e) {
			resultComplexVo = new TbIpAssignMstComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultComplexVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultComplexVo = new TbIpAssignMstComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultComplexVo.setCommonMsg(msgDesc);
		}
		return createResult(resultComplexVo);
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertMrgAsgnIPMst(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstListVo searchVoClone = new TbIpAssignMstListVo();
		try {
			CloneUtil.copyObjectInformation(tbIpAssignMstListVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));

		ModelMap builtModel = viewInsertMrgAsgnIPMstModel(tbIpAssignMstListVo, request);
		model.addAllAttributes(builtModel);
		String retPage = "";
		if (!StringUtils.hasText(tbIpAssignMstListVo.getTypeFlag())
				|| tbIpAssignMstListVo.getTypeFlag().equals("Asgn")) {
			retPage = "ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst";
		} else {
			retPage = "ipmgmt/allocmgmt/viewInsertMrgAlcIPMst";
		}
		return retPage;
	}

	private ModelMap viewInsertMrgAsgnIPMstModel(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstComplexVo resultComplexVo = null;
		try {
			if (!StringUtils.hasText(tbIpAssignMstListVo.getTypeFlag())
					|| tbIpAssignMstListVo.getTypeFlag().equals("Asgn")) {
				List<CommonCodeVo> sassignTypeCds = commonCodeService
						.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
				model.addAttribute("sassignTypeCds", sassignTypeCds);
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

				/** 계위별 배정 레벨 목록 조회 **/
				Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
				String gradeCd = jwtUtil.getUserGradeCd(request);
				if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
					assignLevelCdParamMap.put("startCd", "IA0001");
				} else if (gradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
					assignLevelCdParamMap.put("startCd", "IA0002");
				} else {
					assignLevelCdParamMap.put("startCd", "IA0003");
				}
				assignLevelCdParamMap.put("endCd", "IA0004");

				List<CommonCodeVo> sassignLevelCds = commonCodeService
						.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD, assignLevelCdParamMap);
				model.addAttribute("sassignLevelCds", sassignLevelCds);
			}
			resultComplexVo = assignMgmtService.validateMrgAsgnIPMst(tbIpAssignMstListVo);
			resultComplexVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultComplexVo = new TbIpAssignMstComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultComplexVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultComplexVo = new TbIpAssignMstComplexVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultComplexVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultComplexVo", resultComplexVo);
		return model;
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/insertMrgAsgnIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertMrgAsgnIPMst(@RequestBody TbIpAssignMstComplexVo tbIpAssignMstComplexVo,
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
			srcIpAssignMstVo.setScreateId(jwtUtil.getUserId(request));
			srcIpAssignMstVo.setSmodifyId(jwtUtil.getUserId(request));
			tbIpAssignMstComplexVo.setScreateId(jwtUtil.getUserId(request));
			tbIpAssignMstComplexVo.setSmodifyId(jwtUtil.getUserId(request));
			assignMgmtService.insertMrgAsgnIPMst(tbIpAssignMstComplexVo);
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewUpdateAsgnIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateAsgnIPMst(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo, ModelMap model,
			HttpServletRequest request) {
		ModelMap builtModel = viewUpdateAsgnIPMstModel(tbIpAssignMstListVo, request);
		TbIpAssignMstListVo resultListVo = (TbIpAssignMstListVo) builtModel.get("resultListVo");

		ModelMap resultModel = createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
		resultModel.addAttribute("sassignTypeCds", builtModel.get("sassignTypeCds"));
		resultModel.addAttribute("disabledMap", builtModel.get("disabledMap"));

		return resultModel;
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewUpdateAsgnIPMst.ajax", method = RequestMethod.POST)
	public String viewUpdateAsgnIPMst(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpAssignMstListVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateAsgnIPMstModel(tbIpAssignMstListVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewUpdateAsgnIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewUpdateAsgnIPMst";
	}

	private ModelMap viewUpdateAsgnIPMstModel(@RequestBody TbIpAssignMstListVo tbIpAssignMstListVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {
			String gradeCd = jwtUtil.getUserGradeCd(request);
			/** 목록 조회 **/
			resultListVo = assignMgmtService.selectListAsgnIPMstViaInMstSeq(tbIpAssignMstListVo);

			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = null;
			TbLvlBasListVo nodeListVo = null;
			TbIpAssignMstVo resultVo = resultListVo.getTbIpAssignMstVos().get(0);
			String ssvcLineTypeCd = resultVo.getSsvcLineTypeCd();
			String ssvcGroupCd = resultVo.getSsvcGroupCd();
			String ssvcObjCd = resultVo.getSsvcObjCd();
			Map<String, Object> disabledMap = new HashMap<String, Object>();

			for (TbLvlBasVo itemVo : svcLineListVo.getTbLvlBasVos()) {
				if (itemVo.getSsvcLineTypeCd().equals(ssvcLineTypeCd)) {
					itemVo.setTypeFlag("selected");
				} else {
					itemVo.setTypeFlag(null);
				}
			}
			TbLvlBasVo searchCenterVo = new TbLvlBasVo();
			searchCenterVo.setSsvcLineTypeCd(ssvcLineTypeCd);
			centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
			if (ssvcGroupCd.equals(CommonCodeUtil.USER_LVL_CD_NA)) {
				nodeListVo = new TbLvlBasListVo();
				if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
					disabledMap.put("ssvcLineTypeCd", false);
				} else {
					disabledMap.put("ssvcLineTypeCd", true);
				}
				disabledMap.put("ssvcGroupCd", false);
				disabledMap.put("ssvcObjCd", false);
			} else {
				disabledMap.put("ssvcLineTypeCd", true);
				disabledMap.put("ssvcGroupCd", true);
				for (TbLvlBasVo itemVo : centerListVo.getTbLvlBasVos()) {
					if (itemVo.getSsvcGroupCd().equals(ssvcGroupCd)) {
						itemVo.setTypeFlag("selected");
					} else {
						itemVo.setTypeFlag(null);
					}
				}
				searchCenterVo.setSsvcGroupCd(ssvcGroupCd);
				nodeListVo = jwtUtil.getNodeList(request, searchCenterVo);
				if (!ssvcObjCd.equals(CommonCodeUtil.USER_LVL_CD_NA)) {
					for (TbLvlBasVo itemVo : nodeListVo.getTbLvlBasVos()) {
						if (itemVo.getSsvcObjCd().equals(ssvcObjCd)) {
							itemVo.setTypeFlag("selected");
						} else {
							itemVo.setTypeFlag(null);
						}
					}
					disabledMap.put("ssvcObjCd", true);
				} else {
					disabledMap.put("ssvcObjCd", false);
				}
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			model.addAttribute("disabledMap", disabledMap);

			/** 계위별 배정 레벨 목록 조회 **/
			Map<String, String> assignLevelCdParamMap = new HashMap<String, String>();
			if (gradeCd.equals(CommonCodeUtil.USER_GRADE_A) || gradeCd.equals(CommonCodeUtil.USER_GRADE_S)) {
				assignLevelCdParamMap.put("startCd", "IA0001");
			} else if (gradeCd.equals(CommonCodeUtil.USER_GRADE_N)) {
				assignLevelCdParamMap.put("startCd", "IA0002");
			} else {
				assignLevelCdParamMap.put("startCd", "IA0003");
			}
			assignLevelCdParamMap.put("endCd", "IA0004");

			List<CommonCodeVo> sassignLevelCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_LEVEL_CD,
					assignLevelCdParamMap);
			for (CommonCodeVo itemVo : sassignLevelCds) {
				if (itemVo.getCode().equals("IA0004")) {
					itemVo.setTypeFlag("selected");
				} else {
					itemVo.setTypeFlag(null);
				}
			}
			model.addAttribute("sassignLevelCds", sassignLevelCds);

			/** 서비스 목록 조회(망코드 분류) **/
			Map<String, Object> assignTypeCdParamMap = new HashMap<String, Object>();
			assignTypeCdParamMap.put("ssvcLineTypeCd", ssvcLineTypeCd);
			List<CommonCodeVo> sassignTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD,
					assignTypeCdParamMap);
			model.addAttribute("sassignTypeCds", sassignTypeCds);

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/updateAsgnIPMst.json", method = RequestMethod.POST)
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
			assignMgmtService.updateListAsgnIPMst(tbIpAssignMstComplexVo);
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailAsgnIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailAsgnIPMst(@RequestBody TbIpAssignMstVo tbIpAssignMstVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstVo resultVo = assignMgmtService.selectIpAssignMst(tbIpAssignMstVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailAsgnIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailAsgnIPMst(@RequestBody TbIpAssignMstVo tbIpAssignMstVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpAssignMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailAsgnIPMstModel(tbIpAssignMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewDetailAsgnIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewDetailAsgnIPMst";
	}

	private ModelMap viewDetailAsgnIPMstModel(@RequestBody TbIpAssignMstVo tbIpAssignMstVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstVo resultVo = null;
		try {
			resultVo = assignMgmtService.selectIpAssignMst(tbIpAssignMstVo);
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
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/* 비고 수정 처리 */
	@RequestMapping(value = "/ipmgmt/assignmgmt/updateScommentAsgnIPMst.json", method = RequestMethod.POST)
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

			assignMgmtService.updateListScommentAsgnIPMst(tbIpAssignMstComplexVo);
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

	/******************************
	 * 미배정 현황 Start
	 ****************************************/
	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListUnAssignIP.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListUnAssignIP(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {

		/** 계위 Seq 목록 조회 **/
		TbLvlMstVo searchSeqVo = new TbLvlMstVo();
		searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
		searchSeqVo.setSsvcObjCd("000000");

		TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
		searchVo.setLvlMstSeqListVo(resultSeqList);

		setPagination(searchVo);
		TbIpAssignMstListVo resultListVo = assignMgmtService.selectListUnAssignBlock(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListUnAssignIP.do", method = RequestMethod.POST)
	public String viewListUnAssignIP(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListUnAssignIPModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewListUnAssignIP.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewListUnAssignIP";
	}

	private ModelMap viewListUnAssignIPModel(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
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

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd("000000");
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			setPagination(searchVo);
			resultListVo = assignMgmtService.selectListUnAssignBlock(searchVo);

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpAssignMstListVo();
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewListUnAssignIPExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListUnAssignIPExcel(@RequestBody TbIpAssignMstVo searchVo,
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
					}
				}
			}

			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd("000000");
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);

			TbIpAssignMstListVo resultListVo = assignMgmtService.selectListUnAssignBlockExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("미배정|getnUnAssignBlockCnt");
			mappingList.add("예비배정|getnReserveAssignBlockCnt");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpAssignMstVos(), mappingList,
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

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailUnAssignIP.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailUnAssignIP(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbIpAssignMstListVo resultListVo = assignMgmtService.selectListIpAssignMst(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailUnAssignIP.ajax", method = RequestMethod.POST)
	public String viewDetailUnAssignIP(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailUnAssignIPModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewDetailUnAssignIP.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewDetailUnAssignIP";
	}

	private ModelMap viewDetailUnAssignIPModel(@RequestBody TbIpAssignMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = assignMgmtService.selectListIpAssignMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpAssignMstListVo();
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

	/******************************
	 * 미배정 현황 End
	 ****************************************/

	/**
	 * Summary 상세조회
	 * 
	 * @param tbIpAssignMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailSummary.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailSummaryMst(@RequestBody TbIpAssignMstVo tbIpAssignMstVo, HttpServletRequest request) {
		ModelMap model = viewDetailSummaryMstModel(tbIpAssignMstVo, request);
		TbIpAssignMstListVo resultListVo = (TbIpAssignMstListVo) model.get("resultListVo");
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/assignmgmt/viewDetailSummary.ajax", method = RequestMethod.POST)
	public String viewDetailSummaryMst(@RequestBody TbIpAssignMstVo tbIpAssignMstVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpAssignMstVo searchVoClone = new TbIpAssignMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpAssignMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailSummaryMstModel(tbIpAssignMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/assignmgmt/viewDetailSummary.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/assignmgmt/viewDetailSummary";
	}

	private ModelMap viewDetailSummaryMstModel(@RequestBody TbIpAssignMstVo tbIpAssignMstVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {

			if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0001")) {
				resultListVo = assignMgmtService.selectSummaryDetailKornet(tbIpAssignMstVo);
			} else if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0002")) {
				resultListVo = assignMgmtService.selectSummaryDetailPremium(tbIpAssignMstVo);
			} else if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0003")) {
				// resultListVo = assignMgmtService.selectSummaryDetail(tbIpAssignMstVo);
				PrintLogUtil.printLog(""); // Codeeyes-Urgent-빈 If문 사용 제한
			} else if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0004")) {
				resultListVo = assignMgmtService.selectSummaryDetailKornet(tbIpAssignMstVo);
			} else if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0005")) {
				resultListVo = assignMgmtService.selectSummaryDetailKornet(tbIpAssignMstVo);
			} else if (tbIpAssignMstVo.getSsvcLineTypeCd().equals("CL0006")) {
				resultListVo = assignMgmtService.selectSummaryDetailKornet(tbIpAssignMstVo);
			} else {
				throw new ServiceException("CMN.INFO.00054", new String[] { "Summary 정보가 없습니다." });
			}

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			resultListVo = new TbIpAssignMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}

		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
}
