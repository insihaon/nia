package com.kt.ipms.legacy.opermgmt.privatemgmt.web;

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

import com.codej.web.annotation.EncryptResponse;
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.service.PrivateMgmtService;
import com.kt.ipms.legacy.opermgmt.privatemgmt.service.PrivateMgmtTxService;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstComplexVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstListVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class PrivateMgmtController extends CommonController {


	@Autowired
	private PrivateMgmtService privateMgmtService;

	@Autowired
	private UserMgmtService userMgmtService;

	@Autowired
	private ReqBoardService reqBoardService;

	@Autowired
	private ConfigPropertieService configPropertieService;

	@Autowired
	private PrivateMgmtTxService privateMgmtTxService;
	
	/**
	 * 사설IP신청 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/viewListPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListPrivateIPMst(@RequestBody TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpPrivateReqMstListVo resultListVo = privateMgmtService.selectListTbIpPrivateReqMst(searchVo);
		return createResultList(resultListVo.getTbIpPrivateReqMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/opermgmt/privatemgmt/viewListPrivateIPMst.do", method = RequestMethod.POST)
	public String viewListPrivateIPMst(@ModelAttribute("searchVo") TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo searchVoClone = new TbIpPrivateReqMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListPrivateIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/privatemgmt/viewListPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/privatemgmt/viewListPrivateIPMst";
	}
	private ModelMap viewListPrivateIPMstModel(@ModelAttribute("searchVo") TbIpPrivateReqMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpPrivateReqMstListVo resultListVo = null;
		
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		String userGrade = null;
		
		try {
			
			List<CommonCodeVo> sipPrivateReqMstSeqCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_PRIVATE_REQ_MST_SEQ_CD, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			List<CommonCodeVo> ssvcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null);
			
			model.addAttribute("sipPrivateReqMstSeqCds", sipPrivateReqMstSeqCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("ssvcLineTypeCds", ssvcLineTypeCds);
			
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
			
			/** 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("SIP_PRIVATE_REQ_MST_SEQ_CD");
				sloadFlg = "T";
			}else{
				sloadFlg = "F";
			}
			
			
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_DESC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			/* 페이지 이동(호출) 시 처리 로직 (IP버전, IP주소 / 회선, 할당 기반)*/
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
				resultListVo = new TbIpPrivateReqMstListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = privateMgmtService.selectListTbIpPrivateReqMst(searchVo);
			}
			
			userGrade = jwtUtil.getUserGradeCd(request);
			
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpPrivateReqMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpPrivateReqMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		
		model.addAttribute("userGrade", userGrade);
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/**
	 * 사설IP신청 목록 엑셀 다운로드
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/viewListPrivateIPMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListPrivateIPMstExcel(@ModelAttribute("searchVo") TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
				searchVo.setSortType("SIP_PRIVATE_REQ_MST_SEQ_CD");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_DESC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			TbIpPrivateReqMstListVo resultListVo = privateMgmtService.selectListTbIpPrivateReqMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
		
			mappingList.add("신청차수|getSipPrivateReqMstSeqCd");
			mappingList.add("신청서종류|getSrequestTypeNm");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("IP버전|getSipVersionTypeCd");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("시작IP|getSfirstAddr");
			mappingList.add("끝IP|getSlastAddr");
			mappingList.add("단위블록수|getNclassCnt");
			mappingList.add("신청자|getSrequestId");
			mappingList.add("신청일시|getDrequestDt");
			mappingList.add("처리일시|getDapproveDt");
			mappingList.add("상태|getSrequestStatNm");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbIpPrivateReqMstVos(), mappingList, request);
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
	 * 사설IP신청 상세 정보
	 * @param tbIpPrivateReqMst
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/viewDetailPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailCrtIPMst(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMst, ModelMap model, HttpServletRequest request) {
		TbIpPrivateReqMstVo resultVo = privateMgmtService.selectPrivateIPMst(tbIpPrivateReqMst);
		return createResult(resultVo);
	}
	@RequestMapping(value="/opermgmt/privatemgmt/viewDetailPrivateIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailCrtIPMst(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMst, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo searchVoClone = new TbIpPrivateReqMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpPrivateReqMst, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailCrtIPMstModel(tbIpPrivateReqMst, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/privatemgmt/viewDetailPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/privatemgmt/viewDetailPrivateIPMst";
	}
	private ModelMap viewDetailCrtIPMstModel(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMst, HttpServletRequest request) {
		ModelMap model = new ModelMap();

		TbIpPrivateReqMstVo resultVo = null;
		String userGrade = null;
		String userId = null;
		
		try {
			userGrade = jwtUtil.getUserGradeCd(request);
			userId = jwtUtil.getUserId(request);
			
			resultVo = privateMgmtService.selectPrivateIPMst(tbIpPrivateReqMst);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		
		model.addAttribute("userGrade", userGrade);
		model.addAttribute("userId", userId);
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설IP신청  신규신청 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/viewInsertPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertCrtIPMst(@RequestBody TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpPrivateReqMstVo resultVo = searchVo;
		return createResult(resultVo);
	}
	@RequestMapping(value="/opermgmt/privatemgmt/viewInsertPrivateIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertCrtIPMst(@RequestBody TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo searchVoClone = new TbIpPrivateReqMstVo();
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

		searchVoClone.setUrl("/opermgmt/privatemgmt/viewInsertPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/privatemgmt/viewInsertPrivateIPMst";
	}
	private ModelMap viewInsertCrtIPMstModel(@RequestBody TbIpPrivateReqMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpPrivateReqMstVo resultVo = null;
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
			
			resultVo = searchVo;
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설IP신청 추가
	 * @param tbIpBlockMstComplexVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/appendPrivateIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendCrtIPMst(@RequestBody TbIpPrivateReqMstComplexVo tbIpPrivateReqMstComplexVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = null;
		try {
			
			TbIpPrivateReqMstVo srcIpPrivateReqMstVo = new TbIpPrivateReqMstVo();
			srcIpPrivateReqMstVo = tbIpPrivateReqMstComplexVo.getSrcIpPrivateReqMstVo();
			
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			tbLvlBasVo.setSsvcLineTypeCd(srcIpPrivateReqMstVo.getSsvcLineTypeCd());
			tbLvlBasVo.setSsvcGroupCd(srcIpPrivateReqMstVo.getSsvcGroupCd());
			tbLvlBasVo.setSsvcObjCd(srcIpPrivateReqMstVo.getSsvcObjCd());
			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
			
			srcIpPrivateReqMstVo.setNlvlMstSeq(nlvlMstSeq);
			
			resultVo = privateMgmtService.appendIpBlockMst(tbIpPrivateReqMstComplexVo);
			resultVo.setCommonMsg("SUCCESS");
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 사설IP신청 신규신청 등록
	 * @param tbIpBlockMstListVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/opermgmt/privatemgmt/insertListPrivateIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertListCrtIPMst(@RequestBody TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = null;
		String commonMsg = null;
		
		try {
			for (TbIpPrivateReqMstVo tbIpPrivateReqMstVo : tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos()) {
				tbIpPrivateReqMstVo.setScreateId(jwtUtil.getUserId(request));
				tbIpPrivateReqMstVo.setSmodifyId(jwtUtil.getUserId(request));
			}
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			tbLvlBasVo.setSsvcLineTypeCd(tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos().get(0).getSsvcLineTypeCd());
			tbLvlBasVo.setSsvcGroupCd(tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos().get(0).getSsvcGroupCd());
			tbLvlBasVo.setSsvcObjCd(tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos().get(0).getSsvcObjCd());
			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
			
			privateMgmtService.insertListIpPrivateReqMst(tbIpPrivateReqMstListVo, nlvlMstSeq);
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = CommonCodeUtil.SUCCESS_MSG;
			
			tbIpPrivateReqMstListVo.setsRequestTypeCd(CommonCodeUtil.REQUEST_TYPE01);  // 신규신청
			tbIpPrivateReqMstListVo.setsRequestTypeNm(CommonCodeUtil.REQUEST_TYPE01_NM);  // 신규신청
			tbIpPrivateReqMstListVo.setScreateId(jwtUtil.getUserId(request));
			
			/* */
			commonMsg = SendMail("PrivateIP-Req-User", tbIpPrivateReqMstListVo, request);			// 사설IP신청 등록시(요청자)
			commonMsg = SendMail("PrivateIP-Req-Admin", tbIpPrivateReqMstListVo, request);		// 사설IP신청 등록시(담당자)
			
			
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * 사설IP신청 목록 삭제
	 * @param delVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/privatemgmt/deletePrivateIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteCrtIPMst(@RequestBody TbIpPrivateReqMstVo delVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = null;
		try {
			delVo.setScreateId(jwtUtil.getUserId(request));
			resultVo = privateMgmtService.deletePrivateIPMst(delVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 사설IP신청 목록 승인/반려
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/privatemgmt/updatePrivateIPMstAppr.json", method = RequestMethod.POST)
	public TbIpPrivateReqMstVo updatePrivateIPMstAppr(@RequestBody TbIpPrivateReqMstVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = new TbIpPrivateReqMstVo();
		String commonMsg;
		TbIpPrivateReqMstVo tbIpPrivateReqMstVo = new TbIpPrivateReqMstVo();
		String userId = null;
		try {
			
			userId = jwtUtil.getUserId(request);
			updateVo.setSapproveId(userId);
			updateVo.setSmodifyId(userId);
			
			int count= privateMgmtService.updatePrivateIPMstAppr(updateVo);

			if(count !=1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
				TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo = new TbIpPrivateReqMstListVo();
				List<TbIpPrivateReqMstVo> tbIpPrivateReqMstVos = new ArrayList<TbIpPrivateReqMstVo>();
				tbIpPrivateReqMstVos.add(updateVo);
				tbIpPrivateReqMstListVo.setTbIpPrivateReqMstVos(tbIpPrivateReqMstVos);
				
				tbIpPrivateReqMstListVo.setsRequestStatCd(updateVo.getSrequestStatCd());
				tbIpPrivateReqMstListVo.setSipPrivateReqMstSeqCd(updateVo.getSipPrivateReqMstSeqCd());
				tbIpPrivateReqMstListVo.setNipPrivateReqMstSeq(updateVo.getNipPrivateReqMstSeq());
				tbIpPrivateReqMstListVo.setScreateId(jwtUtil.getUserId(request));
				
				/**/
				if(CommonCodeUtil.REQUEST_STAT02.equals(updateVo.getSrequestStatCd())) { 			// 승인
					commonMsg = SendMail("PrivateIP-Aprv", tbIpPrivateReqMstListVo, request);
				} else if(CommonCodeUtil.REQUEST_STAT03.equals(updateVo.getSrequestStatCd())) {   // 반려
					commonMsg = SendMail("PrivateIP-Reject", tbIpPrivateReqMstListVo, request);
				}
				
			}
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 *사설IP신청 목록 일괄 승인/반려 
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/privatemgmt/updatePrivateIPMstApprAll.json", method = RequestMethod.POST)
	public TbIpPrivateReqMstVo updatePrivateIPMstApprAll(@RequestBody TbIpPrivateReqMstVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = new TbIpPrivateReqMstVo();
		String commonMsg;
		
		String userId = null;
		try {
			
			userId = jwtUtil.getUserId(request);
			updateVo.setSapproveId(userId);
			updateVo.setSmodifyId(userId);
			
			int count= privateMgmtService.updatePrivateIPMstApprAll(updateVo);

			if(count !=1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
				List<String> schkList = null;
				List<BigInteger> nchkList = null;
				TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo = new TbIpPrivateReqMstListVo();

				schkList = updateVo.getsChkList(); 
				nchkList = updateVo.getnChkList();
				
				String mailType = null;
				
				if(CommonCodeUtil.REQUEST_STAT02.equals(updateVo.getSrequestStatCd())) { 			// 승인
					mailType = "PrivateIP-Aprv";
				} else if(CommonCodeUtil.REQUEST_STAT03.equals(updateVo.getSrequestStatCd())) {   // 반려
					mailType = "PrivateIP-Reject";
				}
				
				/**/
				for(String strRequestSeqCd : schkList) {
					
					tbIpPrivateReqMstListVo.setsRequestStatCd(updateVo.getSrequestStatCd());
					tbIpPrivateReqMstListVo.setnChkList(nchkList);
					tbIpPrivateReqMstListVo.setSipPrivateReqMstSeqCd(strRequestSeqCd);
					tbIpPrivateReqMstListVo.setScreateId(jwtUtil.getUserId(request));
					tbIpPrivateReqMstListVo.setSreject_rsn(updateVo.getSreject_rsn());
					 
					commonMsg = SendMail(mailType, tbIpPrivateReqMstListVo, request);
				}
				
			}
			
			
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * 사설IP신청 목록 일괄 반려 > 반려 팝업
	 * @param tbIpPrivateReqMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/privatemgmt/viewPopPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewPopPrivateIPMst(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMstVo, ModelMap model, HttpServletRequest request){
		TbIpPrivateReqMstVo resultVo = tbIpPrivateReqMstVo;
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/privatemgmt/viewPopPrivateIPMst.ajax", method = RequestMethod.POST)
	public String viewPopPrivateIPMst(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		TbIpPrivateReqMstVo searchVoClone = new TbIpPrivateReqMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpPrivateReqMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewPopPrivateIPMstModel(tbIpPrivateReqMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/privatemgmt/viewPopPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/privatemgmt/viewPopPrivateIPMst";
	}
	private ModelMap viewPopPrivateIPMstModel(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpPrivateReqMstVo resultVo = new TbIpPrivateReqMstVo();
		
		try{
			
			resultVo = tbIpPrivateReqMstVo;
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch(ServiceException e){
			resultVo = new TbIpPrivateReqMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbIpPrivateReqMstVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설IP신청 삭제신청 화면
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/opermgmt/privatemgmt/viewInsertPrivateDelIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertPrivateDelIPMst(@RequestBody TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbIpPrivateReqMstListVo resultListVo = privateMgmtService.selectListTbIpPrivateDelReq(searchVo);
		return createResultList(resultListVo.getTbIpPrivateReqMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/opermgmt/privatemgmt/viewInsertPrivateDelIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertPrivateDelIPMst(@RequestBody TbIpPrivateReqMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo searchVoClone = new TbIpPrivateReqMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertPrivateDelIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/privatemgmt/viewInsertPrivateDelIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/privatemgmt/viewInsertPrivateDelIPMst";
	}
	private ModelMap viewInsertPrivateDelIPMstModel(@RequestBody TbIpPrivateReqMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpPrivateReqMstVo resultVo = null;
		TbIpPrivateReqMstListVo resultListVo = null;
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
			
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			setPagination(searchVo);
			
			if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())){
				resultListVo = new TbIpPrivateReqMstListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = privateMgmtService.selectListTbIpPrivateDelReq(searchVo);
			}
			
			
			resultVo = searchVo;
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			e.printStackTrace();
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
			
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 사설IP신청 삭제신청 등록
	 * @param tbIpPrivateReqMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/privatemgmt/insertListPrivateDelIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertListCrtDelIPMst(@RequestBody TbIpPrivateReqMstVo tbIpPrivateReqMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpPrivateReqMstVo resultVo = null;
		String commonMsg = "";
		try {
			tbIpPrivateReqMstVo.setScreateId(jwtUtil.getUserId(request));
			tbIpPrivateReqMstVo.setSmodifyId(jwtUtil.getUserId(request));
			
			privateMgmtService.insertListIpPrivateDelReqMst(tbIpPrivateReqMstVo);
			resultVo = new TbIpPrivateReqMstVo();
			commonMsg = CommonCodeUtil.SUCCESS_MSG;
			
			/* */
			TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo = new TbIpPrivateReqMstListVo();
			tbIpPrivateReqMstListVo.setsRequestTypeNm(tbIpPrivateReqMstVo.getSrequestTypeNm());
			
			commonMsg = SendMail("PrivateIP-Req-User", tbIpPrivateReqMstListVo, request);			// 사설IP신청 등록시(요청자)
			commonMsg = SendMail("PrivateIP-Req-Admin", tbIpPrivateReqMstListVo, request);		// 사설IP신청 등록시(담당자)
			
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			e.printStackTrace();
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			resultVo = new TbIpPrivateReqMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * Mail Value Setting
	 * @param type
	 * @param tbIpPrivateReqMstListVo
	 * @return
	 */
	public String SendMail (String mailType, TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo, HttpServletRequest request) {
		
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
			searchVo.setSuserId(tbIpPrivateReqMstListVo.getScreateId());
			userEmail = userMgmtService.selectEmail(searchVo);					// 요청자 메일주소
			List<ReqAdminEmailVo> reqAdminEmailVoList = reqBoardService.selectAdminEmailList(); //담당자 주소
			
			userName = jwtUtil.getUserNm(request);
			userOrg = jwtUtil.getUserDeptOrgNm(request);
			
			Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
			
			map.put("MAIL_TYPE", mailType);
			
			if(mailType.equals("PrivateIP-Req-User")) {					// 사설IP신청 등록 (요청자)
				// 요청자에게 발송할 메일
				String requestTypeNm = tbIpPrivateReqMstListVo.getsRequestTypeNm();
				subject = "[IPMS 사설IP " + requestTypeNm + " 완료] 사설IP " + requestTypeNm + "이 정상적으로 접수되었습니다.";
				map.put("TITLE", subject);
				map.put("REQUEST_TYPE_NM", requestTypeNm);
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
				
				
				
			} else if(mailType.equals("PrivateIP-Req-Admin")) {					// 사설IP신청 등록 (관리자)
				// 담당자에게 발송할 메일
				String requestTypeNm = tbIpPrivateReqMstListVo.getsRequestTypeNm();
				subject = "[IPMS 사설IP " + requestTypeNm +" 접수 알림] " + userName + "로부터 사설IP " + requestTypeNm +"이 등록되었습니다.";
				map.put("TITLE", subject);
				map.put("REQUEST_TYPE_NM", requestTypeNm);
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
				
			
			} else if(mailType.equals("PrivateIP-Aprv") && tbIpPrivateReqMstListVo.getsRequestStatCd().equals(CommonCodeUtil.REQUEST_STAT02)) { 		// 승인 시
				
				TbIpPrivateReqMstVo selVo = new TbIpPrivateReqMstVo();
				selVo.setSipPrivateReqMstSeqCd(tbIpPrivateReqMstListVo.getSipPrivateReqMstSeqCd());
				
				if(null == tbIpPrivateReqMstListVo.getnChkList()) {
					selVo.setNipPrivateReqMstSeq(tbIpPrivateReqMstListVo.getNipPrivateReqMstSeq());
				} else {
					selVo.setnChkList(tbIpPrivateReqMstListVo.getnChkList());
				}
				
				List<TbIpPrivateReqMstVo> nodeListVo = privateMgmtTxService.selectNlvlMstSeq(selVo);
				
				for (TbIpPrivateReqMstVo nodeVo : nodeListVo) {
					
					// 신규신청일때
					if(nodeVo.getSrequestTypeCd().equals("10")) {
						
						List<TbIpPrivateReqMstVo> listVo = privateMgmtTxService.selectPrivateIPMstAll(selVo);
						
						TbIpPrivateReqMstVo mapVo = listVo.get(0);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						// 요청자에게 발송할 메일
						String requestTypeNm = mapVo.getSrequestTypeNm();
						subject = "[IPMS 사설IP " + requestTypeNm + " 승인] 관리자로부터 사설IP " + requestTypeNm +"이 승인되었습니다.";
						
						map.put("REQUEST_TYPE_NM", requestTypeNm);
						map.put("SSVC_LINE_TYPE_NM", mapVo.getSsvcLineTypeNm());			// 서비스망
						map.put("SSVC_GROUP_NM", mapVo.getSsvcGroupNm());					// 본부
						map.put("SSVC_OBJ_NM", mapVo.getSsvcObjNm());							// 국사
						map.put("SIP_VERSION_TYPE_NM", mapVo.getSipVersionTypeNm());		// IP버전
						
						//Codeeyes-Urgent-String 추가 시 String Buffer 사용
//						String pipPrefixs = "";
						StringBuffer sb = new StringBuffer();
						
						for(TbIpPrivateReqMstVo vo : listVo) {
							
//							pipPrefixs += vo.getPipPrefix() + " <br />";
							sb.append(vo.getPipPrefix());
							sb.append(" <br />");
						}
						
						String pipPrefixs = sb.toString();
						
						map.put("PIP_PREFIXS", pipPrefixs);								// IP
						map.put("APPROVE_DT", format.format(mapVo.getDapproveDt()));	// 처리일시
						map.put("TITLE", subject);
						content = smtpUtil.parseHtml(map, request);
						
						
					// 삭제신청일때 여러 노드가 한꺼번에 신청차수에 묶일 수 있음 -> 노드별 메일 전송	
					} else if(nodeVo.getSrequestTypeCd().equals("20")) {
						
						selVo.setNlvlMstSeq(nodeVo.getNlvlMstSeq());
						List<TbIpPrivateReqMstVo> ipListVo = privateMgmtTxService.selectPrivateIPMstAll(selVo);
						
						TbIpPrivateReqMstVo mapVo = ipListVo.get(0);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						// 요청자에게 발송할 메일
						String requestTypeNm = mapVo.getSrequestTypeNm();
						subject = "[IPMS 사설IP " + requestTypeNm + " 승인] 관리자로부터 사설IP " + requestTypeNm +"이 승인되었습니다.";
						
						map.put("REQUEST_TYPE_NM", requestTypeNm);
						map.put("SSVC_LINE_TYPE_NM", mapVo.getSsvcLineTypeNm());			// 서비스망
						map.put("SSVC_GROUP_NM", mapVo.getSsvcGroupNm());					// 본부
						map.put("SSVC_OBJ_NM", mapVo.getSsvcObjNm());							// 국사
						map.put("SIP_VERSION_TYPE_NM", mapVo.getSipVersionTypeNm());		// IP버전
						
						//Codeeyes-Urgent-String 추가 시 String Buffer 사용
//						String pipPrefixs = "";
						StringBuffer sb = new StringBuffer();
						
						for(TbIpPrivateReqMstVo vo : ipListVo) {
							
//							pipPrefixs += vo.getPipPrefix() + " <br />";
							sb.append(vo.getPipPrefix());
							sb.append(" <br />");
						}
						
						String pipPrefixs = sb.toString();
						
						map.put("PIP_PREFIXS", pipPrefixs);								// IP
						map.put("APPROVE_DT", format.format(mapVo.getDapproveDt()));	// 처리일시
						map.put("TITLE", subject);
						content = smtpUtil.parseHtml(map, request);
						
					}
					
					
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
				
				
			} else if(mailType.equals("PrivateIP-Reject") && tbIpPrivateReqMstListVo.getsRequestStatCd().equals(CommonCodeUtil.REQUEST_STAT03)) { 		// 반려 시
				
				TbIpPrivateReqMstVo selVo = new TbIpPrivateReqMstVo();
				selVo.setSipPrivateReqMstSeqCd(tbIpPrivateReqMstListVo.getSipPrivateReqMstSeqCd());
				
				if(null == tbIpPrivateReqMstListVo.getnChkList()) {
					selVo.setNipPrivateReqMstSeq(tbIpPrivateReqMstListVo.getNipPrivateReqMstSeq());
				} else {
					selVo.setnChkList(tbIpPrivateReqMstListVo.getnChkList());
				}

				List<TbIpPrivateReqMstVo> nodeListVo = privateMgmtTxService.selectNlvlMstSeq(selVo);
				
				for (TbIpPrivateReqMstVo nodeVo : nodeListVo) {
					
					// 신규신청일때
					if(nodeVo.getSrequestTypeCd().equals("10")) {
						
						List<TbIpPrivateReqMstVo> listVo = privateMgmtTxService.selectPrivateIPMstAll(selVo);
						
						TbIpPrivateReqMstVo mapVo = listVo.get(0);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						// 요청자에게 발송할 메일
						String requestTypeNm = mapVo.getSrequestTypeNm();
						subject = "[IPMS 사설IP " + requestTypeNm + " 반려] 관리자로부터 사설IP " + requestTypeNm +"이 반려되었습니다.";
						
						map.put("REQUEST_TYPE_NM", requestTypeNm);
						map.put("SSVC_LINE_TYPE_NM", mapVo.getSsvcLineTypeNm());			// 서비스망
						map.put("SSVC_GROUP_NM", mapVo.getSsvcGroupNm());					// 본부
						map.put("SSVC_OBJ_NM", mapVo.getSsvcObjNm());							// 국사
						map.put("SIP_VERSION_TYPE_NM", mapVo.getSipVersionTypeNm());		// IP버전
						map.put("REJECT_RSN", mapVo.getSreject_rsn());		// 반려사유
						
						//Codeeyes-Urgent-String 추가 시 String Buffer 사용
//						String pipPrefixs = "";
						StringBuffer sb = new StringBuffer();
						
						for(TbIpPrivateReqMstVo vo : listVo) {
							
//							pipPrefixs += vo.getPipPrefix() + " <br />";
							sb.append(vo.getPipPrefix());
							sb.append(" <br />");
						}
						
						String pipPrefixs = sb.toString();
						
						map.put("PIP_PREFIXS", pipPrefixs);								// IP
						map.put("APPROVE_DT", format.format(mapVo.getDapproveDt()));	// 처리일시
						map.put("TITLE", subject);
						content = smtpUtil.parseHtml(map, request);
						
						
					// 삭제신청일때 여러 노드가 한꺼번에 신청차수에 묶일 수 있음 -> 노드별 메일 전송	
					} else if(nodeVo.getSrequestTypeCd().equals("20")) {
						
						selVo.setNlvlMstSeq(nodeVo.getNlvlMstSeq());
						List<TbIpPrivateReqMstVo> ipListVo = privateMgmtTxService.selectPrivateIPMstAll(selVo);
						
						TbIpPrivateReqMstVo mapVo = ipListVo.get(0);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						// 요청자에게 발송할 메일
						String requestTypeNm = mapVo.getSrequestTypeNm();
						subject = "[IPMS 사설IP " + requestTypeNm + " 반려] 관리자로부터 사설IP " + requestTypeNm +"이 반려되었습니다.";
						
						map.put("REQUEST_TYPE_NM", requestTypeNm);
						map.put("SSVC_LINE_TYPE_NM", mapVo.getSsvcLineTypeNm());			// 서비스망
						map.put("SSVC_GROUP_NM", mapVo.getSsvcGroupNm());					// 본부
						map.put("SSVC_OBJ_NM", mapVo.getSsvcObjNm());							// 국사
						map.put("SIP_VERSION_TYPE_NM", mapVo.getSipVersionTypeNm());		// IP버전
						map.put("REJECT_RSN", mapVo.getSreject_rsn());		// 반려사유
						
						//Codeeyes-Urgent-String 추가 시 String Buffer 사용
//						String pipPrefixs = "";
						StringBuffer sb = new StringBuffer();
						
						for(TbIpPrivateReqMstVo vo : ipListVo) {
							
//							pipPrefixs += vo.getPipPrefix() + " <br />";
							sb.append(vo.getPipPrefix());
							sb.append(" <br />");
						}
						
						String pipPrefixs = sb.toString();
						
						map.put("PIP_PREFIXS", pipPrefixs);								// IP
						map.put("APPROVE_DT", format.format(mapVo.getDapproveDt()));	// 처리일시
						map.put("TITLE", subject);
						content = smtpUtil.parseHtml(map, request);
						
					}
					
					
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
				
			} 

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
	
		return result;
	}
}
