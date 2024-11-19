package com.kt.ipms.legacy.opermgmt.intgrmgmt.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.service.IntgrMgmtService;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.log4kt.utils.StringUtil;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


/**
 * 운용정보관리 > IP주소 라우팅 비교/점검 > 조직별 장비 정보관리
 *
 */

@Controller
public class IntgrMgmtController  extends CommonController{
	
	@Autowired
	private IntgrMgmtService intgrMgmtService;
	
	/****************************************************************************************
	 * 조직별 장비 정보관리
	 ****************************************************************************************/
	
	/**
	 * 조직별 장비 정보관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListFcltMst.model", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ModelMap viewListFcltMst(@RequestBody TbFcltMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbFcltMstListVo resultListVo = intgrMgmtService.selectListFcltMst(searchVo);
		return createResultList(resultListVo.getTbFcltMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListFcltMst.do", method = RequestMethod.POST)
	public String viewListFcltMst(@ModelAttribute("searchVo") TbFcltMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListFcltMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListFcltMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListFcltMst";
	}
	private ModelMap viewListFcltMstModel(@ModelAttribute("searchVo") TbFcltMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltMstListVo resultListVo = null;
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
			
			setPagination(searchVo);
			resultListVo = intgrMgmtService.selectListFcltMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbFcltMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbFcltMstListVo();
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
	 * 조직별 장비 정보관리 > 신규생성 팝업
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertFcltMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertFcltMst(ModelMap model, HttpServletRequest request) {
		ModelMap builtModel = new ModelMap();
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("suseYn", "Y");
		List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
		builtModel.addAttribute("sfcltTypes", sfcltTypes);

		return builtModel;
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertFcltMst.ajax", method = RequestMethod.POST)
	public String viewInsertFcltMst(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertFcltMstModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/ifomsmgmt/viewInsertIFomsMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewInsertFcltMst";
	}
	private ModelMap viewInsertFcltMstModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltMstVo resultVo = null;
		try {
			/** 계위 정보 설정 **/
			
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = new TbLvlBasListVo();
			TbLvlBasListVo nodeListVo = new TbLvlBasListVo();
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);
			
			resultVo = new TbFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 조직별 장비 정보관리 > 등록 
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltMstVo insertFcltMst(@RequestBody TbFcltMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setScreateId(userId);
			insertVo.setSmodifyId(userId);
			intgrMgmtService.insertFcltMst(insertVo);
			resultVo = new TbFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 조직별 장비 정보관리 > 상세조회 팝업 
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateFcltMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateFcltMst(@RequestBody TbFcltMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbFcltMstVo resultVo = null;
		if (searchVo.getNfcltMstSeq() != null) {
			resultVo = intgrMgmtService.selectFcltMst(searchVo);
		} else {
			resultVo = new TbFcltMstVo();
		}
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateFcltMst.ajax", method = RequestMethod.POST)
	public String viewUpdateFcltMst(@RequestBody TbFcltMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewUpdateFcltMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewUpdateFcltMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewUpdateFcltMst";
	}
	private ModelMap viewUpdateFcltMstModel(@RequestBody TbFcltMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltMstVo resultVo = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);

			if (searchVo.getNfcltMstSeq() != null) {
				resultVo = intgrMgmtService.selectFcltMst(searchVo);
			} else {
				resultVo = new TbFcltMstVo();
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 조직별 장비 정보관리 > 수정
	 * @param updateVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/updateFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltMstVo updateFcltMst(@RequestBody TbFcltMstVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			updateVo.setSmodifyId(userId);
			intgrMgmtService.updateFcltMst(updateVo);
			resultVo = new TbFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 조직별 장비 정보관리 > 삭제
	 * @param deleteVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/deleteFcltMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltMstVo deleteFcltMst(@RequestBody TbFcltMstVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltMstVo resultVo = null;
		try {
			intgrMgmtService.deleteFcltMst(deleteVo);
			resultVo = new TbFcltMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 조직별 장비 정보관리 > 엑셀 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/opermgmt/intgrmgmt/viewListFcltMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListFcltMstExcel(@RequestBody TbFcltMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
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

			setPagination(searchVo);
			
			TbFcltMstListVo  resultListVo = intgrMgmtService.selectListFcltMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("IP주소|getPtelnetIp");
			mappingList.add("PORT|getNportFclt");
			mappingList.add("상위장비IP|getPipUpperFclt");
			mappingList.add("PEER장비IP|getPipPeerFclt");
			mappingList.add("프롬프트명|getShostNm");
			mappingList.add("모델명|getSfcltModelNm");
			mappingList.add("타입|getSfcltType");
			mappingList.add("사용여부|getSuseYn");
			
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbFcltMstVos(), mappingList, request);
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	
	/****************************************************************************************
	 * 장비별 명령어 정보관리
	 ****************************************************************************************/
	/**
	 * 장비별 명령어 정보관리 > 장비타입 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/selectListCommonCode.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListCommonCode(@RequestBody TbFcltCmdMstVo searchVo, ModelMap model, HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("suseYn", "Y");
		List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
		return createResultList(sfcltTypes, sfcltTypes.size());
	}
	/**
	 * 장비별 명령어 정보관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListFcltCmdMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListFcltCmdMst(@RequestBody TbFcltCmdMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbFcltCmdMstListVo resultListVo = intgrMgmtService.selectListFcltCmdMst(searchVo);
		return createResultList(resultListVo.getTbFcltCmdMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListFcltCmdMst.do", method = RequestMethod.POST)
	public String viewListFcltCmdMst(@ModelAttribute("searchVo") TbFcltCmdMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListFcltCmdMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListFcltCmdMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListFcltCmdMst";
	}
	private ModelMap viewListFcltCmdMstModel(@ModelAttribute("searchVo") TbFcltCmdMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltCmdMstListVo resultListVo = null;
		try {
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("suseYn", "Y");
			List<CommonCodeVo> sfcltTypes = commonCodeService.selectListCommonCode(CommonCodeUtil.FLCT_TYPE_CD, paramMap);
			model.addAttribute("sfcltTypes", sfcltTypes);
			setPagination(searchVo);
			resultListVo = intgrMgmtService.selectListFcltCmdMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbFcltCmdMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbFcltCmdMstListVo();
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
	 * 장비별 명령어 정보관리 > 신규생성 팝업
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertFcltCmdMst.ajax", method = RequestMethod.POST)
	public String viewInsertFcltCmdMst(HttpServletRequest request, HttpServletResponse response) {
		return "opermgmt/intgrmgmt/viewInsertFcltCmdMst";
	}
	
	/**
	 * 장비별 명령어 정보관리 > 등록
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertFcltCmdMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltCmdMstVo insertFcltCmdMst(@RequestBody TbFcltCmdMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltCmdMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setSmodifyId(userId);
			insertVo.setScreateId(userId);
			intgrMgmtService.insertFcltCmdMst(insertVo);
			resultVo = new TbFcltCmdMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 장비별 명령어 정보관리 > 수정 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateFcltCmdMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateFcltCmdMst(@RequestBody TbFcltCmdMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbFcltCmdMstVo resultVo = intgrMgmtService.selectFcltCmdMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateFcltCmdMst.ajax", method = RequestMethod.POST)
	public String viewUpdateFcltCmdMst(@RequestBody TbFcltCmdMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateFcltCmdMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewUpdateFcltCmdMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewUpdateFcltCmdMst";
	}
	private ModelMap viewUpdateFcltCmdMstModel(@RequestBody TbFcltCmdMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbFcltCmdMstVo resultVo = null;
		try {
			resultVo = intgrMgmtService.selectFcltCmdMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 장비별 명령어 정보관리 > 수정 
	 * @param updateVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/updateFcltCmdMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltCmdMstVo updateFcltCmdMst(@RequestBody TbFcltCmdMstVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltCmdMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			updateVo.setSmodifyId(userId);
			intgrMgmtService.updateFcltCmdMst(updateVo);
			resultVo = new TbFcltCmdMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 장비별 명령어 정보관리 > 삭제
	 * @param deleteVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/deleteFcltCmdMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbFcltCmdMstVo deleteFcltCmdMst(@RequestBody TbFcltCmdMstVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbFcltCmdMstVo resultVo = null;
		try {
			intgrMgmtService.deleteFcltCmdMst(deleteVo);
			resultVo = new TbFcltCmdMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbFcltCmdMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 장비별 명령어 정보관리 > 엑셀 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/opermgmt/intgrmgmt/viewListFcltCmdMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListFcltCmdMstExcel(@RequestBody TbFcltCmdMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		try{
			setPagination(searchVo);
			
			TbFcltCmdMstListVo  resultListVo = intgrMgmtService.selectListFcltCmdMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("장비타입|getSfcltType");
			mappingList.add("장비명령어|getSfcltCmd");
			mappingList.add("명령어순서|getNpriority");
			mappingList.add("사용여부|getSuseYn");
			
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbFcltCmdMstVos(), mappingList, request);
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리
	 ****************************************************************************************/
	
	/**
	 * 무선IP 사전 정보관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListMobileMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListMobileMst(@RequestBody TbMobileMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbMobileMstListVo resultListVo = intgrMgmtService.selectListMobileMst(searchVo);
		return createResultList(resultListVo.getTbMobileMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListMobileMst.do", method = RequestMethod.POST)
	public String viewListMobileMst(@ModelAttribute("searchVo") TbMobileMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListMobileMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListMobileMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListMobileMst";
	}
	private ModelMap viewListMobileMstModel(@ModelAttribute("searchVo") TbMobileMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMobileMstListVo resultListVo = null;
		try {
			
			if(!StringUtil.isNullorBlank(searchVo.getScommu())) {
				if(!searchVo.getScommu().contains(":")) {
					searchVo.setPipPrefix(searchVo.getScommu());
					searchVo.setScommu(null);
				}
			}
			
			setPagination(searchVo);
			resultListVo = intgrMgmtService.selectListMobileMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbMobileMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbMobileMstListVo();
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
	 * 무선IP 사전 정보관리 > 신규생성 팝업
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertMobileMst.ajax", method = RequestMethod.POST)
	public String viewInsertMobileMst(HttpServletRequest request, HttpServletResponse response) {
		return "opermgmt/intgrmgmt/viewInsertMobileMst";
	}
	
	/**
	 * 무선IP 사전 정보관리 > 등록
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertMobileMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileMstVo insertMobileMst(@RequestBody TbMobileMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbMobileMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setSmodifyId(userId);
			insertVo.setScreateId(userId);
			intgrMgmtService.insertMobileMst(insertVo);
			resultVo = new TbMobileMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 무선IP 사전 정보관리 > 수정 팝업
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateMobileMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateMobileMst(@RequestBody TbMobileMstVo searchVo, ModelMap model, HttpServletRequest request) {
		TbMobileMstVo resultVo = intgrMgmtService.selectMobileMst(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateMobileMst.ajax", method = RequestMethod.POST)
	public String viewUpdateMobileMst(@RequestBody TbMobileMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateMobileMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewUpdateMobileMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewUpdateMobileMst";
	}
	private ModelMap viewUpdateMobileMstModel(@RequestBody TbMobileMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMobileMstVo resultVo = null;
		try {
			resultVo = intgrMgmtService.selectMobileMst(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 무선IP 사전 정보관리 > 수정 
	 * @param updateVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/updateMobileMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileMstVo updateMobileMst(@RequestBody TbMobileMstVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbMobileMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			updateVo.setSmodifyId(userId);
			intgrMgmtService.updateMobileMst(updateVo);
			resultVo = new TbMobileMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	 
	 
	/**
	 * 무선IP 사전 정보관리 > 삭제
	 * @param deleteVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/deleteMobileMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileMstVo deleteMobileMst(@RequestBody TbMobileMstVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbMobileMstVo resultVo = null;
		try {
			intgrMgmtService.deleteMobileMst(deleteVo);
			resultVo = new TbMobileMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	 
	/**
	 * 무선IP 사전 정보관리 > 엑셀 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/opermgmt/intgrmgmt/viewListMobileMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListMobileMstExcel(@RequestBody TbMobileMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		try{
			setPagination(searchVo);
			
			TbMobileMstListVo  resultListVo = intgrMgmtService.selectListMobileMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("구분|getSkindNm");
			mappingList.add("서비스|getSserviceNm");
			mappingList.add("Community|getScommu");
			mappingList.add("IP 블록|getPipPrefix");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbMobileMstVos(), mappingList, request);
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
	 * 무선 전체 라우팅 수집
	 * @param tbRoutChkMstVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertListRoutChkMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbRoutChkMstVo insertListRoutChkMst(@RequestBody TbRoutChkMstVo tbRoutChkMstVo, HttpServletRequest request, HttpServletResponse response) {
		TbRoutChkMstVo resultVo = null;
		Map<String,String> result = null;
		try {
			
			tbRoutChkMstVo.setSmodifyId(jwtUtil.getUserId(request));
			result = intgrMgmtService.insertListRoutChkMst(tbRoutChkMstVo);
			
			String resultCd = result.get("resultCd");
			String resultMsg = result.get("resultMsg");
			
			resultVo = new TbRoutChkMstVo();
			
			if(resultCd.equals("S0000")) {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {				
				resultVo.setCommonMsg(resultMsg);
			}
			
		} catch (ServiceException e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbRoutChkMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	

	/****************************************************************************************
	 * 무선IP 사전 정보관리 > Summary 등록
	 ****************************************************************************************/
	
	/**
	 * 무선IP 사전 정보관리 > Summary 등록 팝업
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewPopSummaryMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewPopSummaryMst(@RequestBody TbMobileSummMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbMobileSummMstListVo resultListVo = intgrMgmtService.selectListMobileSummMst(searchVo);
		return createResultList(resultListVo.getTbMobileSummMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewPopSummaryMst.ajax", method = RequestMethod.POST)
	public String viewPopSummaryMst(@RequestBody TbMobileSummMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewPopSummaryMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewPopSummaryMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewPopSummaryMst";
	}
	private ModelMap viewPopSummaryMstModel(@RequestBody TbMobileSummMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbMobileSummMstListVo resultListVo = null;
		
		model.addAttribute("searchVo", searchVo);
		try{
			setPagination(searchVo);
			resultListVo = intgrMgmtService.selectListMobileSummMst(searchVo);
		} catch(ServiceException e){
			resultListVo = new TbMobileSummMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbMobileSummMstListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	/**
	 * 무선IP 사전 정보관리 > Summary 등록
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertMobileSummMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileSummMstVo insertMobileSummMst(@RequestBody TbMobileSummMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbMobileSummMstVo resultVo = null;
		try {
			String userId = jwtUtil.getUserId(request);
			insertVo.setSmodifyId(userId);
			insertVo.setScreateId(userId);
			intgrMgmtService.insertMobileSummMst(insertVo);
			resultVo = new TbMobileSummMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileSummMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileSummMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 무선IP 사전 정보관리 > Summary 삭제
	 * @param deleteVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/deleteMobileSummMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileSummMstVo deleteMobileSummMst(@RequestBody TbMobileSummMstVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbMobileSummMstVo resultVo = null;
		try {
			intgrMgmtService.deleteMobileSummMst(deleteVo);
			resultVo = new TbMobileSummMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbMobileSummMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbMobileSummMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리 > 텍스트 파일 업로드
	 ****************************************************************************************/

	/**
	 * 무선IP 사전 정보관리 > 텍스트 파일 업로드 팝업
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewPopUploadMst.ajax", method = RequestMethod.POST)
	public String viewPopUploadMst(HttpServletRequest request, HttpServletResponse response) {
		return "opermgmt/intgrmgmt/viewPopUploadMst";
	}
	
	/**
	 * 무선IP 사전 정보관리 > 업로드
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/uploadMobileMst.ajax", method = RequestMethod.POST)
	@ResponseBody
	public TbMobileMstVo uploadMobileMst(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		TbMobileMstVo resultVo = null;
		try {
			

			MultipartFile file =  request.getFile("file");
			String sKindCd = request.getParameter("kind");
		
			String userId = jwtUtil.getUserId(request);
			
			intgrMgmtService.uploadMobileMst(file, sKindCd, userId);
			
            resultVo = new TbMobileMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
            
		} catch (ServiceException e) {
			
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			
			resultVo = new TbMobileMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/****************************************************************************************
	 * 서비스망 별 서비스 정보관리
	 ****************************************************************************************/
	
	/**
	 * 서비스망 별 서비스 정보관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListDefaultSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListDefaultSvcMst(@RequestBody TbDefaultSvcMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbDefaultSvcMstListVo resultListVo = intgrMgmtService.selectListDefaultSvcMst(searchVo);
		return createResultList(resultListVo.getTbDefaultSvcMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListDefaultSvcMst.do", method = RequestMethod.POST)
	public String viewListDefaultSvcMst(@ModelAttribute("searchVo") TbDefaultSvcMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListDefaultSvcMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListDefaultSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListDefaultSvcMst";
	}
	private ModelMap viewListDefaultSvcMstModel(@ModelAttribute("searchVo") TbDefaultSvcMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbDefaultSvcMstListVo resultListVo = null;
		try {
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			
			model.addAttribute("svcLineListVo", svcLineListVo);
			
			//setPagination(searchVo);
			
			resultListVo = intgrMgmtService.selectListDefaultSvcMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbDefaultSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbDefaultSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		//PaginationInfo paginationInfo = getPaginationInfo();
		//paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		//model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/**
	 * 서비스망 별 서비스 정보관리 > 등록 
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertDefaultSvcMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbDefaultSvcMstVo insertDefaultSvcMst(@RequestBody List<Object> insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbDefaultSvcMstVo resultVo = null;
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			List<TbDefaultSvcMstVo> tbDefaultSvcMstVos = mapper.convertValue(insertVo, new TypeReference<List<TbDefaultSvcMstVo>>(){});
			
			for(int i=0; i<tbDefaultSvcMstVos.size(); i++) {
				tbDefaultSvcMstVos.get(i).getSsvcLineTypeCd();
				
				String userId = jwtUtil.getUserId(request);
				tbDefaultSvcMstVos.get(i).setScreateId(userId);
				tbDefaultSvcMstVos.get(i).setSmodifyId(userId);
				
				TbDefaultSvcMstVo  tbDefaultSvcMstVo = tbDefaultSvcMstVos.get(i);
				intgrMgmtService.insertDefaultSvcMst(tbDefaultSvcMstVo);
			}
			
			resultVo = new TbDefaultSvcMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbDefaultSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbDefaultSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}

	/****************************************************************************************
	 * 라우팅 연동 이력관리
	 ****************************************************************************************/
	
	/**
	 * 라우팅 연동 이력관리 > 검색조건 결과메시지 조회
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/selectSresultMsg.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectSresultMsg() {
		List<?> sresultMsg = intgrMgmtService.selectSresultMsg();
		return createResultList(sresultMsg, sresultMsg.size());
	}
	
	/**
	 * 라우팅 연동 이력관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListRoutHistMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListRoutHistMst(@RequestBody TbRoutHistMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbRoutHistMstListVo resultListVo = intgrMgmtService.selectListRoutHistMst(searchVo);
		return createResultList(resultListVo.getTbRoutHistMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListRoutHistMst.do", method = RequestMethod.POST)
	public String viewListRoutHistMst(@ModelAttribute("searchVo") TbRoutHistMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewListRoutHistMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListRoutHistMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListRoutHistMst";
	}
	private ModelMap viewListRoutHistMstModel(@ModelAttribute("searchVo") TbRoutHistMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbRoutHistMstListVo resultListVo = null;
		List<?> sresultMsg = null;
		try {	
			
			String sPromptNm = searchVo.getShostNm();
			if(sPromptNm != null && !sPromptNm.equals("")){
				searchVo.setShostNm(URLDecoder.decode(sPromptNm, "UTF-8"));
			}
			setPagination(searchVo);
			sresultMsg = intgrMgmtService.selectSresultMsg();
			resultListVo = intgrMgmtService.selectListRoutHistMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbRoutHistMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbRoutHistMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("sresultMsg", sresultMsg);
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/****************************************************************************************
	 * 유선IP 사전 정보관리
	 ****************************************************************************************/
	
	/**
	 * 유선IP 사전 정보관리 > 목록 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListWireMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListWireMst(@RequestBody TbWireMstVo searchVo, ModelMap model, HttpServletRequest request) {
		setPagination(searchVo);
		TbWireMstListVo resultListVo = intgrMgmtService.selectListWireMst(searchVo);
		return createResultList(resultListVo.getTbWireMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListWireMst.do", method = RequestMethod.POST)
	public String viewListWireMst(@ModelAttribute("searchVo") TbWireMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListWireMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewListWireMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewListWireMst";
	}
	private ModelMap viewListWireMstModel(@ModelAttribute("searchVo") TbWireMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWireMstListVo resultListVo = null;
		
		try {
			
			setPagination(searchVo);
			resultListVo = intgrMgmtService.selectListWireMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultListVo = new TbWireMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbWireMstListVo();
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
	 * 유선IP 사전 정보관리 > 등록 페이지 보기 
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertWireMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertWireMst(ModelMap model, HttpServletRequest request) {
		TbWireMstVo resultVo = new TbWireMstVo();
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewInsertWireMst.ajax", method = RequestMethod.POST)
	public String viewInsertWireMst(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel =viewInsertWireMstModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewInsertWireMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewInsertWireMst";
	}
	private ModelMap viewInsertWireMstModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWireMstVo resultVo = null;
		try {
			/** 계위 정보 설정 **/
			
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			TbLvlBasListVo centerListVo = new TbLvlBasListVo();
			TbLvlBasListVo nodeListVo = new TbLvlBasListVo();
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			
			resultVo = new TbWireMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 유선IP 사전 정보관리 > 등록 
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/insertWireMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbWireMstVo insertWireMst(@RequestBody TbWireMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo resultVo = null;
		
		try {
			String userId = jwtUtil.getUserId(request);
			
			insertVo.setScreateId(userId);
			insertVo.setSmodifyId(userId);
			
			PrintLogUtil.printLogInfoValueObject(insertVo);
			
			intgrMgmtService.insertWireMst(insertVo);
			
			resultVo = new TbWireMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	
	/**
	 * 유선IP 사전 정보관리 > 수정 페이지 보기 
	 * @param insertVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateWireMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateWireMst(@RequestBody TbWireMstVo tbWireMstVo, ModelMap model, HttpServletRequest request) {
		TbWireMstVo resultVo = intgrMgmtService.selectWireMst(tbWireMstVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewUpdateWireMst.ajax", method = RequestMethod.POST)
	public String viewUpdateWireMst(@RequestBody TbWireMstVo tbWireMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo  searchVoClone = new TbWireMstVo();
		try {
			CloneUtil.copyObjectInformation(tbWireMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateWireMstModel(tbWireMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/intgrmgmt/viewUpdateWireMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/intgrmgmt/viewUpdateWireMst";
	}
	private ModelMap viewUpdateWireMstModel(@RequestBody TbWireMstVo tbWireMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWireMstVo resultVo = null;
		try {
			resultVo = intgrMgmtService.selectWireMst(tbWireMstVo);
			PrintLogUtil.printLogInfoValueObject(resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value = "/opermgmt/intgrmgmt/updateWireMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbWireMstVo updateWireMst(@RequestBody TbWireMstVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo resultVo = null;
		
		try {
			String userId = jwtUtil.getUserId(request);
			
			updateVo.setScreateId(userId);
			updateVo.setSmodifyId(userId);
			
			PrintLogUtil.printLogInfoValueObject(updateVo);
			
			intgrMgmtService.updateWireMst(updateVo);
			
			resultVo = new TbWireMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/intgrmgmt/deleteWireMst.json", method = RequestMethod.POST)
	@ResponseBody
	public TbWireMstVo deleteWireMst(@RequestBody TbWireMstVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbWireMstVo resultVo = null;
		
		try {

			PrintLogUtil.printLogInfoValueObject(deleteVo);
			intgrMgmtService.deleteWireMst(deleteVo);
			
			resultVo = new TbWireMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWireMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	@RequestMapping(value = "/opermgmt/intgrmgmt/viewListWireMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListWireMstExcel(@RequestBody TbWireMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		FileVo resultVo = new FileVo();
		
		try{
			/** 계위 정보 설정 **/
			TbWireMstListVo  resultListVo = intgrMgmtService.selectListWireMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("구분|getSkindCd");
			mappingList.add("Community|getScommunity");
			mappingList.add("Nexthop|getSnexthop");
			
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbWireMstVos(), mappingList, request);
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	
	
}
