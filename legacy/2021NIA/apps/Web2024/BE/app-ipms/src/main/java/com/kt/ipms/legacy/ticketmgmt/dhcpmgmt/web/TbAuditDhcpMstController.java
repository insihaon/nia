package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.web;





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

import com.codej.web.annotation.EncryptResponse;
import com.codej.web.vo.BaseVo;
import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.service.TbAuditDhcpMstService;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class TbAuditDhcpMstController extends CommonController {

	@Autowired
	TbAuditDhcpMstService tbAuditDhcpMstService;
		
	/**
	 * dhcp 감사관리
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception  viewListDhcpAuditList.jsp
	 */
	@RequestMapping(value="/ticketmgmt/dhcpmgmt/viewListDhcpAuditList.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListDhcpAuditList(@RequestBody TbAuditDhcpMstVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbAuditDhcpMstListVo resultListVo = tbAuditDhcpMstService.selectListTbAuditDhcpMstVo(searchVo);
		return createResultList(resultListVo.getTbAuditDhcpMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/ticketmgmt/dhcpmgmt/viewListDhcpAuditList.do", method = RequestMethod.POST)
	public String viewListDhcpAuditList(@ModelAttribute("searchVo") TbAuditDhcpMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbAuditDhcpMstVo searchVoClone = new TbAuditDhcpMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListDhcpAuditListModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/dhcpmgmt/viewListDhcpAuditList.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/dhcpmgmt/viewListDhcpAuditList"; 
	}
	private ModelMap viewListDhcpAuditListModel(@ModelAttribute("searchVo") TbAuditDhcpMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbAuditDhcpMstListVo resultListVo = null;
		
		try {

			///** 계위 정보 설정 **/
		
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
			
			///** 수용국 코드 설정 **/
			List<CommonCodeVo> sLvlSubvCds = tbAuditDhcpMstService.selectOrderSofficeList(searchVo);
			model.addAttribute("sLvlSubvCds", sLvlSubvCds);
			
	
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_DMODIFY_DT);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			setPagination(searchVo); 
			resultListVo = tbAuditDhcpMstService.selectListTbAuditDhcpMstVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbAuditDhcpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbAuditDhcpMstListVo();
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
	 * dhcp 감사관리 Excel 저장
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception  viewListDhcpAuditList.jsp
	 */
	@RequestMapping(value="/ticketmgmt/dhcpmgmt/viewListDhcpAuditListExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListDhcpAuditListExcel(@ModelAttribute("searchVo") TbAuditDhcpMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
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
				searchVo.setSortType(CommonCodeUtil.SORT_TYPE_DMODIFY_DT);
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			
			TbAuditDhcpMstListVo resultListVo = tbAuditDhcpMstService.selectListTbAuditDhcpMstVoExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("노드|getStopNodeNm");
			mappingList.add("수용국|getSofficeNm");
			mappingList.add("시설명|getSl3EquipAlias");
			mappingList.add("시설 대표 IP|getSl3Mstip");
			mappingList.add("SAID|getSsaid");
			mappingList.add("전용번호|getSllnum");
			mappingList.add("블록수|getNipBlockCnt");
			mappingList.add("IP 수|getNipCnt");
			mappingList.add("최대할당율|getNmaxPeak");
			mappingList.add("최소할당율|getNminPeak");
			mappingList.add("상한경보|getBauditNm");
			mappingList.add("하한경보|getBoptimizeNm");
			mappingList.add("관리|getBfalgNm");
			mappingList.add("작업일자|getDmodifyDt");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getTbAuditDhcpMstVos(), mappingList, request);
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

	
	@RequestMapping(value="/ticketmgmt/dhcpmgmt/viewListDhcpAuditModpop.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListDhcpAuditModpop(@RequestBody TbAuditDhcpMstVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbAuditDhcpMstVo resultVo = tbAuditDhcpMstService.selectTbAuditDhcpMstVo(searchVo);;
		TbAuditDhcpBasVo resultVo2 = null;
		String bflagYn = searchVo.getBfalg();
		if(bflagYn.equals("Y")){
			resultVo2 = tbAuditDhcpMstService.selectTbAuditDhcpBasVo(resultVo2);
		}
		return createResult(resultVo, resultVo2);
	}
	@RequestMapping(value="/ticketmgmt/dhcpmgmt/viewListDhcpAuditModpop.ajax", method = RequestMethod.POST)
	public String viewListDhcpAuditModpop(@RequestBody TbAuditDhcpMstVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
		TbAuditDhcpMstVo searchVoClone = new TbAuditDhcpMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListDhcpAuditModpopModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/ticketmgmt/dhcpmgmt/viewListDhcpAuditModpop.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "ticketmgmt/dhcpmgmt/viewListDhcpAuditModpop"; 
	}
	private ModelMap viewListDhcpAuditModpopModel(@RequestBody TbAuditDhcpMstVo searchVo,
	HttpServletRequest request){
		ModelMap model = new ModelMap();
		TbAuditDhcpMstVo resultVo = null;
		TbAuditDhcpBasVo resultVo2 = null;
		
		try {
			
			String bflagYn = searchVo.getBfalg();
			//임계 조회 , DHCP화면조회
			resultVo = tbAuditDhcpMstService.selectTbAuditDhcpMstVo(searchVo);
			if(bflagYn.equals("Y")){
				resultVo2 = tbAuditDhcpMstService.selectTbAuditDhcpBasVo(resultVo2);
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);		
			
		} catch (ServiceException e) {
			resultVo = new TbAuditDhcpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);


		} catch (Exception e) {
			resultVo = new TbAuditDhcpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);

		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("resultVo2", resultVo2);
		return model;
	}
	
	
	/*DHCP 임계치수정 : 수동관리*/
	@RequestMapping(value = "ticketmgmt/dhcpmgmt/updateDhcpAuditMst.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateTbAuditDhcpMst(@RequestBody TbAuditDhcpMstVo searchVo, 
			HttpServletRequest request, HttpServletResponse response, ModelMap model){
		TbAuditDhcpMstVo resultVo = null;
		try {
			
		 tbAuditDhcpMstService.updateTbAuditDhcpMstVo(searchVo);
		 resultVo = new TbAuditDhcpMstVo();
		 resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		 
		 model.addAttribute("resultVo", resultVo);
		 
		} catch (ServiceException e) {
			resultVo = new TbAuditDhcpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAuditDhcpMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	
	/*DHCP 임계치조회 : 자동관리*/
	@RequestMapping(value = "ticketmgmt/dhcpmgmt/selectAuditDhcpBas.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectTbAuditDhcpBasVo(@RequestBody TbAuditDhcpBasVo searchVo, 
			HttpServletRequest request, HttpServletResponse response, ModelMap model){
		TbAuditDhcpBasVo resultVo = null;
		try{
			
			 resultVo = tbAuditDhcpMstService.selectTbAuditDhcpBasVo(searchVo);
		 
			 model.addAttribute("resultVo", resultVo);
		 
		} catch (ServiceException e) {
			 resultVo = new TbAuditDhcpBasVo();
			 String msgDesc = tbCmnMstService.selectMsgDesc(e);
			 resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			 resultVo = new TbAuditDhcpBasVo();
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
	@RequestMapping(value = "ticketmgmt/dhcpmgmt/selectOrderOfficeList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectOfficeList(@RequestBody TbAuditDhcpMstVo searchVo, HttpServletRequest request, HttpServletResponse response){
		TbAuditDhcpMstListVo resultListVo = null;
		try{
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstService.selectSetOrderOfficeList(searchVo);
			resultListVo = new TbAuditDhcpMstListVo();
			resultListVo.setTbAuditDhcpMstVos(resultList);
			
		} catch (ServiceException e) {
			resultListVo = new TbAuditDhcpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbAuditDhcpMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	
	
}
