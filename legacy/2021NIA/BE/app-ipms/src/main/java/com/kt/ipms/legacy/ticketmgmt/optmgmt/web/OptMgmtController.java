package com.kt.ipms.legacy.ticketmgmt.optmgmt.web;

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
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.service.OptMgmtService;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class OptMgmtController extends CommonController {

	@Autowired
	private OptMgmtService optMgmtService;

	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPMst.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListOptIPMst(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstListVo resultListVo = optMgmtService.selectListOptimizeIpSource(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPMst.do", method = RequestMethod.POST)
	public String viewListOptIPMst(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewListOptIPMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/optmgmt/viewListOptIPMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/optmgmt/viewListOptIPMst";
	}
	private ModelMap viewListOptIPMstModel(@ModelAttribute("searchVo") TbIpAssignMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {
			
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			
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
			resultListVo = optMgmtService.selectListOptimizeIpSource(searchVo);
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
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPTarget.model", method = RequestMethod.POST)
	public ModelMap viewListOptIPTarget(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstListVo resultListVo = optMgmtService.selectListOptimizeIpTarget(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPTarget.ajax", method = RequestMethod.POST)
	public String viewListOptIPTarget(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewListOptIPTargetModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/optmgmt/viewListOptIPTarget.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/optmgmt/viewListOptIPTarget";
	}
	private ModelMap viewListOptIPTargetModel(@RequestBody TbIpAssignMstVo searchVo,
	HttpServletRequest request)	{
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {
			resultListVo = optMgmtService.selectListOptimizeIpTarget(searchVo);
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
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPRecommend.model", method = RequestMethod.POST)
	public ModelMap viewListOptIPRecommend(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbIpAssignMstListVo resultListVo = optMgmtService.selectListOptimizeIpRecommend(searchVo);
		return createResultList(resultListVo.getTbIpAssignMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPRecommend.ajax", method = RequestMethod.POST)
	public String viewListOptIPRecommend(@RequestBody TbIpAssignMstVo searchVo, ModelMap model,
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
		ModelMap builtModel = viewListOptIPRecommendModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/optmgmt/viewListOptIPRecommend.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/optmgmt/viewListOptIPRecommend";
	}
	private ModelMap viewListOptIPRecommendModel(@RequestBody TbIpAssignMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpAssignMstListVo resultListVo = null;
		try {
			resultListVo = optMgmtService.selectListOptimizeIpRecommend(searchVo);
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
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/selectListOptIPRecommend.json", method = RequestMethod.POST)
	@ResponseBody
	public TbIpAssignMstListVo selectListOptIPRecommend(@RequestBody TbIpAssignMstVo searchVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			resultListVo = optMgmtService.selectListOptimizeIpRecommend(searchVo);
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
		return resultListVo;
	}
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPResult.model", method = RequestMethod.POST)
	public ModelMap viewListOptIPResult(@RequestBody TbOptimizationIpMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbOptimizationIpMstListVo resultListVo = optMgmtService.selectListOptimizeIpResult(searchVo);
		return createResultList(resultListVo.getTbOptimizationIpMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPResult.ajax", method = RequestMethod.POST)
	public String viewListOptIPResult(@RequestBody TbOptimizationIpMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbOptimizationIpMstVo searchVoClone = new TbOptimizationIpMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListOptIPResultModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/optmgmt/viewListOptIPResult.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/optmgmt/viewListOptIPResult";
	}
	private ModelMap viewListOptIPResultModel(@RequestBody TbOptimizationIpMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbOptimizationIpMstListVo resultListVo = null;
		try {
			resultListVo = optMgmtService.selectListOptimizeIpResult(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOptimizationIpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOptimizationIpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/insertListOptIPRecommend.json", method = RequestMethod.POST)
	@ResponseBody
	public TbOptimizationIpMstVo insertListOptIPRecommend(@RequestBody TbOptimizationIpMstListVo tbOptimizationIpMstListVo) {
		TbOptimizationIpMstVo resultVo = null;
		try {
			optMgmtService.insertListOptIPMst(tbOptimizationIpMstListVo);
			resultVo = new TbOptimizationIpMstVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbOptimizationIpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbOptimizationIpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPMstResult.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListOptIPMstResult(@RequestBody TbOptimizationIpMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbOptimizationIpMstListVo resultListVo = optMgmtService.selectListPageOptimizeIpResult(searchVo);
		return createResultList(resultListVo.getTbOptimizationIpMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPMstResult.do", method = RequestMethod.POST)
	public String viewListOptIPMstResult(@ModelAttribute("searchVo") TbOptimizationIpMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbOptimizationIpMstVo searchVoClone = new TbOptimizationIpMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListOptIPMstResultModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/optmgmt/viewListOptIPMstResult.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/optmgmt/viewListOptIPMstResult";
	}
	private ModelMap viewListOptIPMstResultModel(@ModelAttribute("searchVo") TbOptimizationIpMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbOptimizationIpMstListVo resultListVo = null;
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
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			setPagination(searchVo);
			resultListVo = optMgmtService.selectListPageOptimizeIpResult(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbOptimizationIpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbOptimizationIpMstListVo();
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
	 * 조각 IP 관리 현황 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return FileVo
	 * @throws Exception
	 */
	@RequestMapping(value = "/ticketmgmt/optmgmt/viewListOptIPMstResultExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public FileVo viewListOptIPMstResultExcel(@ModelAttribute("searchVo") TbOptimizationIpMstVo searchVo,
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
			
			TbOptimizationIpMstListVo resultListVo = optMgmtService.selectListPageOptimizeIpResultExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("대상 IP|getVirtualPipPrefix");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("As-Is IP|getPipPrefix");
			mappingList.add("최적화IP수|getNcnt");
			mappingList.add("To-Be IP|getPipPrefixTar");
			mappingList.add("작업일자|getDmodifyDt");
			mappingList.add("비고|getScomment");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbOptimizationIpMstVos(), mappingList, request);
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
}
