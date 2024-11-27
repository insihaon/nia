package com.kt.ipms.legacy.ipmgmt.createmgmt.web;

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

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.createmgmt.service.CreateMgmtService;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstListVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class CreateMgmtController extends CommonController {

	@Autowired
	private CreateMgmtService createMgmtService;

	/**
	 * IP 볼록 생성 목록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtIPMst.model", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		setPagination(searchVo);
		TbIpBlockMstListVo resultListVo = createMgmtService.selectListIpBlockMst(searchVo);
		return createResultList(resultListVo.getTbIpBlockMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtIPMst.do", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String viewListCrtIPMst(@ModelAttribute("searchVo") TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListCrtIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewListCrtIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewListCrtIPMst";
	}

	private ModelMap viewListCrtIPMstModel(@ModelAttribute("searchVo") TbIpBlockMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstListVo resultListVo = null;

		try {
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			List<CommonCodeVo> sipCreateSeqCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD,
					null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			List<CommonCodeVo> ssvcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD,
					null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipCreateSeqCds", sipCreateSeqCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			// model.addAttribute("ssvcLineTypeCds", ssvcLineTypeCds);

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

			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
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
			resultListVo = createMgmtService.selectListIpBlockMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

			String sipCreateTypeCd = searchVo.getSipCreateTypeCd();
			if (resultListVo.getTotalCount() > 0) {
				sipCreateTypeCd = resultListVo.getTbIpBlockMstVos().get(0).getSipCreateTypeCd();
			}

		} catch (ServiceException e) {
			resultListVo = new TbIpBlockMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpBlockMstListVo();
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

	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtIPMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListCrtIPMstExcel(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			TbIpBlockMstListVo resultListVo = createMgmtService.selectListIpBlockMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("생성차수|getSipCreateSeqCd");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("시작 IP|getSfirstAddr");
			mappingList.add("종료 IP|getSlastAddr");
			mappingList.add("단위블록수|getNclassCnt");
			mappingList.add("작업일자|getDmodifyDt");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpBlockMstVos(), mappingList,
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
	 * IP 블록 상세 정보
	 * 
	 * @param tbIpBlockMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewDetailCrtIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailCrtIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo, ModelMap model,
			HttpServletRequest request) {
		TbIpBlockMstVo resultVo = createMgmtService.selectIpBlockMst(tbIpBlockMstVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewDetailCrtIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailCrtIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpBlockMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailCrtIPMstModel(tbIpBlockMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewDetailCrtIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewDetailCrtIPMst";
	}

	private ModelMap viewDetailCrtIPMstModel(@RequestBody TbIpBlockMstVo tbIpBlockMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstVo resultVo = null;
		try {
			resultVo = createMgmtService.selectIpBlockMst(tbIpBlockMstVo);
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
		return model;
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewInsertCrtIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpBlockMstVo resultVo = createMgmtService.selectIpBlockMst(searchVo);
		return createResult(resultVo);
	}

	/**
	 * IP 블록 생성 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewInsertCrtIPMst.ajax", method = RequestMethod.POST)
	public String viewInsertCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
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

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewInsertCrtIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewInsertCrtIPMst";
	}

	private ModelMap viewInsertCrtIPMstModel(@RequestBody TbIpBlockMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstVo resultVo = null;
		try {

			/* 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용 */
			Map<String, Object> sipCreateTypeParamMap = new HashMap<String, Object>();
			sipCreateTypeParamMap.put("startCd", "CT0001");
			sipCreateTypeParamMap.put("endCd", "CT0004"); // 사설은 사설 IP 신청 게시판에서만 등록 가능

			List<CommonCodeVo> sipCreateTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, sipCreateTypeParamMap);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			List<CommonCodeVo> ssvcLineTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD,
					null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("ssvcLineTypeCds", ssvcLineTypeCds);
			if (searchVo.getSearchUseYn() != null && searchVo.getSearchUseYn().equals("Y")) {
				resultVo = createMgmtService.selectIpBlockMst(searchVo);
				resultVo.setSearchUseYn(searchVo.getSearchUseYn());
			} else {
				resultVo = searchVo;
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			logger.error(e);
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/**
	 * IP 블록 추가
	 * 
	 * @param tbIpBlockMstComplexVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/appendCrtIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo appendCrtIPMst(@RequestBody TbIpBlockMstComplexVo tbIpBlockMstComplexVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo resultVo = null;
		try {
			resultVo = createMgmtService.appendIpBlockMst(tbIpBlockMstComplexVo);
			resultVo.setCommonMsg("SUCCESS");
		} catch (ServiceException e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/**
	 * IP 블럭 생성 목록 등록
	 * 
	 * @param tbIpBlockMstListVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/insertListCrtIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertListCrtIPMst(@RequestBody TbIpBlockMstListVo tbIpBlockMstListVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo resultVo = null;
		try {
			for (TbIpBlockMstVo tbIpBlockMstVo : tbIpBlockMstListVo.getTbIpBlockMstVos()) {
				tbIpBlockMstVo.setScreateId(jwtUtil.getUserId(request));
				tbIpBlockMstVo.setSmodifyId(jwtUtil.getUserId(request));
			}
			TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
			tbLvlBasVo.setSsvcLineTypeCd(tbIpBlockMstListVo.getTbIpBlockMstVos().get(0).getSsvcLineTypeCd());
			tbLvlBasVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
			tbLvlBasVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
			BigInteger nlvlMstSeq = jwtUtil.getLvlMstSeq(request, tbLvlBasVo);
			createMgmtService.insertListIpBlockMst(tbIpBlockMstListVo, nlvlMstSeq);
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

		return resultVo;
	}

	/**
	 * IP 블럭 삭제
	 * 
	 * @param tbIpBlockMstVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/deleteCrtIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteCrtIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo resultVo = null;
		try {
			tbIpBlockMstVo.setScreateId(jwtUtil.getUserId(request));
			resultVo = createMgmtService.deleteIpBlockMst(tbIpBlockMstVo);
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
		return resultVo;
	}

	/**
	 * IP 블럭 수정 화면
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewUpdateCrtIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpBlockMstVo resultVo = createMgmtService.selectIpBlockMst(searchVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewUpdateCrtIPMst.ajax", method = RequestMethod.POST)
	public String viewUpdateCrtIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateCrtIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewUpdateCrtIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewUpdateCrtIPMst";
	}

	private ModelMap viewUpdateCrtIPMstModel(@RequestBody TbIpBlockMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstVo resultVo = null;
		try {
			resultVo = createMgmtService.selectIpBlockMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			logger.error(e);
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}

	/**
	 * IP 블럭 수정
	 * 
	 * @param tbIpBlockMstVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/updateCrtIPMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateCrtIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo resultVo = null;
		try {
			tbIpBlockMstVo.setSmodifyId(jwtUtil.getUserId(request));
			resultVo = createMgmtService.updateIpBlockMst(tbIpBlockMstVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			logger.error(e);
			resultVo = new TbIpBlockMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/**
	 * IP 블럭 차수 정보 AutoComplete
	 * 
	 * @param searchSipCreateSeqCd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/selectListSipCreateSeqCd.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public List<String> selectListSipCreateSeqCd(@RequestBody HashMap<String, Object> map) {
		String searchSipCreateSeqCd = (String) map.get("sipCreateSeqCd");
		List<String> retList = createMgmtService.selectListSipCreateSeqCd(searchSipCreateSeqCd);
		for (int i = 0; i < retList.size(); i++) {
			retList.set(i, retList.get(i).replaceAll("[<]", "&lt;"));
			retList.set(i, retList.get(i).replaceAll("[>]", "&gt;"));
		}
		return retList;
	}

	/**
	 * 사설 IP 볼록 생성 목록
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListCrtPrivateIPMst(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpBlockMstListVo resultListVo = createMgmtService.selectListIpBlockMst(searchVo);
		return createResultList(resultListVo.getTbIpBlockMstVos(), resultListVo.getTotalCount());
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtPrivateIPMst.do", method = RequestMethod.POST)
	public String viewListCrtPrivateIPMst(@ModelAttribute("searchVo") TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListCrtPrivateIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewListCrtPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewListCrtPrivateIPMst";
	}

	private ModelMap viewListCrtPrivateIPMstModel(@ModelAttribute("searchVo") TbIpBlockMstVo searchVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstListVo resultListVo = null;
		String sloadFlg = "T"; // 메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		try {

			List<CommonCodeVo> sipCreateSeqCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD2, null);
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService
					.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);

			model.addAttribute("sipCreateSeqCds", sipCreateSeqCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);

			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_DMODIFY_DT);
				sloadFlg = "T";
			} else {
				sloadFlg = "F";
			}

			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_DESC);
			}

			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}

			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) {
				searchVo.setSipCreateTypeCd("CT0005");
			}

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
			if (sloadFlg.equals("T")) {
				resultListVo = new TbIpBlockMstListVo();
				resultListVo.setTotalCount(0);
			} else {
				resultListVo = createMgmtService.selectListIpBlockMst(searchVo);
			}

			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);

		} catch (ServiceException e) {
			resultListVo = new TbIpBlockMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbIpBlockMstListVo();
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
	 * 사설 IP 볼록 생성 엑셀 다운로드
	 * 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewListCrtPrivateIPMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListCrtPrivateIPMstExcel(@RequestBody TbIpBlockMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_PIP_PREFIX);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) {
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			TbIpBlockMstListVo resultListVo = createMgmtService.selectListIpBlockMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("생성차수|getSipCreateSeqCd");
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("시작 IP|getSfirstAddr");
			mappingList.add("종료 IP|getSlastAddr");
			mappingList.add("단위블록수|getNclassCnt");
			mappingList.add("작업일자|getDmodifyDt");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpBlockMstVos(), mappingList,
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
	 * 사설 IP 블록 상세 정보
	 * 
	 * @param tbIpBlockMstVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ipmgmt/createmgmt/viewDetailCrtPrivateIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailCrtPrivateIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo, ModelMap model,
			HttpServletRequest request) {
		TbIpBlockMstVo resultVo = createMgmtService.selectIpBlockMst(tbIpBlockMstVo);
		return createResult(resultVo);
	}

	@RequestMapping(value = "/ipmgmt/createmgmt/viewDetailCrtPrivateIPMst.ajax", method = RequestMethod.POST)
	public String viewDetailCrtPrivateIPMst(@RequestBody TbIpBlockMstVo tbIpBlockMstVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbIpBlockMstVo searchVoClone = new TbIpBlockMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpBlockMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailCrtPrivateIPMstModel(tbIpBlockMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ipmgmt/createmgmt/viewDetailCrtPrivateIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ipmgmt/createmgmt/viewDetailCrtPrivateIPMst";
	}

	private ModelMap viewDetailCrtPrivateIPMstModel(@RequestBody TbIpBlockMstVo tbIpBlockMstVo,
			HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpBlockMstVo resultVo = null;
		try {
			resultVo = createMgmtService.selectIpBlockMst(tbIpBlockMstVo);
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
		return model;
	}

}
