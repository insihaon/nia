package com.kt.ipms.legacy.ticketmgmt.configmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.service.ConfigMgmtService;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class ConfigMgmtController extends CommonController {
	
	@Autowired
	private ConfigMgmtService configMgmtService;

	@RequestMapping(value = "/ticketmgmt/configmgmt/viewListConfigMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListConfigMst(@RequestBody TbConfigInterfaceMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbConfigInterfaceMstListVo resultListVo = configMgmtService.selectListFirstStepTopology(searchVo);
		return createResultList(resultListVo.getTbConfigInterfaceMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewListConfigMst.do", method = RequestMethod.POST)
	public String viewListConfigMst(@ModelAttribute("searchVo") TbConfigInterfaceMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigInterfaceMstVo searchVoClone = new TbConfigInterfaceMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListConfigMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/configmgmt/viewListConfigMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/configmgmt/viewListConfigMst";
	}
	private ModelMap viewListConfigMstModel(@ModelAttribute("searchVo") TbConfigInterfaceMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbConfigInterfaceMstListVo resultListVo = null;
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
			
			/** Topology Level 설정 **/
			searchVo.setNflagLevel(1000);
			resultListVo = configMgmtService.selectListFirstStepTopology(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
		} catch (ServiceException e) {
			resultListVo = new TbConfigInterfaceMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbConfigInterfaceMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	
	@RequestMapping(value = "/ticketmgmt/configmgmt/selectListTopologyTree.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public List<Map<String, Object>> selectListTopologyTree(@RequestParam Map<String, Object> paramMap, 
			HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> resultListMap = null;
		try {
			resultListMap = configMgmtService.selectListTopologyTree(paramMap);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListMap;
	}
	
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewListRouteMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListRouteMst(@RequestBody TbConfigRouteMstVo searchVo, ModelMap model,
			HttpServletRequest request) {
		TbConfigRouteMstListVo resultListVo = configMgmtService.selectListPageRouteMst(searchVo);
		return createResultList(resultListVo.getTbConfigRouteMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewListRouteMst.ajax", method = RequestMethod.POST)
	public String viewListRouteMst(@RequestBody TbConfigRouteMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigRouteMstVo searchVoClone = new TbConfigRouteMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListRouteMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/configmgmt/viewListRouteMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/configmgmt/viewListRouteMst";
	}
	private ModelMap viewListRouteMstModel(@RequestBody TbConfigRouteMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbConfigRouteMstListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = configMgmtService.selectListPageRouteMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbConfigRouteMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbConfigRouteMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewListRouteMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListRouteMstExcel(@ModelAttribute("searchVo") TbConfigRouteMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			
			TbConfigRouteMstListVo resultListVo = configMgmtService.selectListPageRouteMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("장비명|getShostNm");
			//mappingList.add("장비IP|getShostIp");
			mappingList.add("I/F명|getSrouteGwIfNm");
			mappingList.add("I/F IP|getSrouteGwIfIp");
			mappingList.add("Description|getScomment");
			mappingList.add("IP블록|getSrouteIpBlock");
			mappingList.add("전용번호|getSllum");
			mappingList.add("감사|getSauditFlag");
			String fileName = excelUtil.createExcelFile(resultListVo.getTbConfigRouteMstVos(), mappingList, request);
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
	
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewDetailRouteMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewDetailRouteMst(@RequestBody TbConfigRouteMstVo searchVo, ModelMap model,
			HttpServletRequest request){
		TbConfigRouteMstVo resultVo = configMgmtService.selectConfigRouteMst(searchVo);
		IpAllocOperMstListVo resultListVo = configMgmtService.selectListIpAllocDetail(resultVo);
		return createResultList(resultListVo.getIpAllocOperMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/ticketmgmt/configmgmt/viewDetailRouteMst.ajax", method = RequestMethod.POST)
	public String viewDetailRouteMst(@RequestBody TbConfigRouteMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		TbConfigRouteMstVo searchVoClone = new TbConfigRouteMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailRouteMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/configmgmt/viewDetailRouteMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/configmgmt/viewDetailRouteMst";
	}
	private ModelMap viewDetailRouteMstModel(@RequestBody TbConfigRouteMstVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbConfigRouteMstVo resultVo = null;
		IpAllocOperMstListVo resultListVo = null;
		try {
			resultVo = configMgmtService.selectConfigRouteMst(searchVo);
			resultListVo = configMgmtService.selectListIpAllocDetail(resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbConfigRouteMstVo();
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbConfigRouteMstVo();
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setTotalCount(0);
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
}
