package com.kt.ipms.legacy.statmgmt.ipstatmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
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
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.service.IpStatMgmtService;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpIntgrmSvcStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class IpStatMgmtController extends CommonController {
	
	@Autowired
	private IpStatMgmtService ipStatMgmtService;
	
	/**
	 * 1.생성차수별 IP현황 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListCreateIPStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListCreateIpStatMst(@RequestBody IpCreateStatVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpCreateStatListVo resultListVo = ipStatMgmtService.selectListIpCreateStat(searchVo);
		return createResultList(resultListVo.getIpCreateStatVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListCreateIPStat.do", method = RequestMethod.POST)
	public String viewListCreateIpStatMst(@ModelAttribute("searchVo") IpCreateStatVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		IpCreateStatVo searchVoClone = new IpCreateStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListCreateIpStatMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListCreateIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListCreateIPStat";
	}
	private ModelMap viewListCreateIpStatMstModel(@ModelAttribute("searchVo") IpCreateStatVo searchVo, HttpServletRequest request){
		ModelMap model = new ModelMap();
		IpCreateStatListVo resultListVo = null;
		try {
			// 생성차수 조회 
			List<CommonCodeVo> sipCreateSeqCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_SEQ_CD, null);
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			model.addAttribute("sipCreateSeqCds", sipCreateSeqCds);
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			// 생성차수별 IP현황 목록 
			setPagination(searchVo);
			resultListVo = ipStatMgmtService.selectListIpCreateStat(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpCreateStatListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpCreateStatListVo();
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
	 * 1.생성차수별 IP현황 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListCreateIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListCreateIpStatMstExcel(@ModelAttribute("searchVo") IpCreateStatVo searchVo, 
			HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			IpCreateStatListVo resultListVo = ipStatMgmtService.selectListIpCreateStatExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("생성차수|getSipCreateSeqCd");
			mappingList.add("IP버전|getSipVersionTypeNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("총 IP수|getNcntTotal");
			mappingList.add("미배정 IP수|getNcntNotAssign");
			mappingList.add("예비배정 IP수|getNcntRersAssign");
			mappingList.add("배정 IP수|getNcntAssign");
			mappingList.add("서비스배정 IP수|getNcntServAssign");
			mappingList.add("할당예약 IP수|getNcntRersAlloc");
			mappingList.add("할당 IP수|getNcntAlloc");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpCreateStatVos(), mappingList, request);
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
	 * 2.조직별 IP현황 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgIPStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListOrgIpStatMst(@RequestBody IpOrgStatVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpOrgStatListVo resultListVo = ipStatMgmtService.selectListOrgIpStatMst(searchVo);
		return createResultList(resultListVo.getIpOrgStatVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgIPStat.do", method = RequestMethod.POST)
	public String viewListOrgIpStatMst(@ModelAttribute("searchVo") IpOrgStatVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		IpOrgStatVo searchVoClone = new IpOrgStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListOrgIpStatMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListOrgIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListOrgIPStat";
	}
	private ModelMap viewListOrgIpStatMstModel(@ModelAttribute("searchVo") IpOrgStatVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpOrgStatListVo resultListVo = null;
		try {
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
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
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			// 조직별 IP현황 목록 
			setPagination(searchVo);
			resultListVo = ipStatMgmtService.selectListOrgIpStatMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpOrgStatListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpOrgStatListVo();
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
	 * 2.조직별 IP현황 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListOrgIpStatMstExcel(@ModelAttribute("searchVo") IpOrgStatVo searchVo,
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
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			//목록 조회 
			IpOrgStatListVo resultListVo = ipStatMgmtService.selectListOrgIpStatMstExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("총 IP 수|getNcntTotal");
			mappingList.add("할당 IP 수|getNallocCnt");
			mappingList.add("미할당 IP 수(계)|getNasssignNcntSum");
			mappingList.add("미할당 IP 수(~/24,~52)|getNasssignNcnt24");
			mappingList.add("미할당 IP 수(/25,52~)|getNasssignNcnt25");
			mappingList.add("미할당 IP 수(/26,56~)|getNasssignNcnt26");
			mappingList.add("미할당 IP 수(/27,60~)|getNasssignNcnt27");
			mappingList.add("미할당 IP 수(/28,64)|getNasssignNcnt28");
			mappingList.add("미할당 IP 수(이하)|getNasssignNcnt29");
			mappingList.add("할당율(%)|getNassignPercent");
			
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpOrgStatVos(), mappingList, request);
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
	 * 3.조직서비스별 IP현황 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgSvcIPStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListOrgSvcIpStatMst(@RequestBody IpOrgServiceStatVo searchVo,
			HttpServletRequest request){
		IpOrgServiceStatListVo resultListVo = ipStatMgmtService.selectListOrgSvcIpStatMst(searchVo);
		return createResultList(resultListVo.getIpOrgServiceStatVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgSvcIPStat.do", method = RequestMethod.POST)
	public String viewListOrgSvcIpStatMst(@ModelAttribute("searchVo") IpOrgServiceStatVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		IpOrgServiceStatVo searchVoClone = new IpOrgServiceStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListOrgSvcIpStatMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListOrgSvcIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListOrgSvcIPStat";
	}
	private ModelMap viewListOrgSvcIpStatMstModel(@ModelAttribute("searchVo") IpOrgServiceStatVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpOrgServiceStatListVo resultListVo = null;
		try {
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
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
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			// 조직서비스별 IP현황 목록 
			resultListVo = ipStatMgmtService.selectListOrgSvcIpStatMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpOrgServiceStatListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpOrgServiceStatListVo();
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
	 * 3.조직서비스별 IP현황 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgSvcIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListOrgSvcIpStatMstExcel(@ModelAttribute("searchVo") IpOrgServiceStatVo searchVo,
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
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			//목록 조회 
			IpOrgServiceStatListVo resultListVo = ipStatMgmtService.selectListOrgSvcIpStatMstExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("총 IP수|getNcntTotal"); 
			mappingList.add("미배정 IP수|getNcntNotAssign");
			mappingList.add("예비배정 IP수|getNcntRersAssign");
			mappingList.add("배정 IP수|getNcntAssign");
			mappingList.add("서비스배정 IP수|getNcntServAssign");
			mappingList.add("할당예약 IP수|getNcntRersAlloc");
			mappingList.add("할당 IP수|getNcntAlloc");
			mappingList.add("할당율(%)|getNcntPercent");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpOrgServiceStatVos(), mappingList, request);
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
	 * 4.단일블록별 IP현황 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListSingleBlockIPStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListSingleBlockIpStatMst(@RequestBody IpSingleBlockStatVo searchVo, ModelMap model,
			HttpServletRequest request){
		IpSingleBlockStatListVo resultListVo = ipStatMgmtService.selectListSingleBlockIpStatMst(searchVo);
		return createResultList(resultListVo.getIpSingleBlockStatVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListSingleBlockIPStat.do", method = RequestMethod.POST)
	public String viewListSingleBlockIpStatMst(@ModelAttribute("searchVo") IpSingleBlockStatVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		IpSingleBlockStatVo searchVoClone = new IpSingleBlockStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListSingleBlockIpStatMstModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListSingleBlockIPStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListSingleBlockIPStat";
	}
	private ModelMap viewListSingleBlockIpStatMstModel(@ModelAttribute("searchVo") IpSingleBlockStatVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		IpSingleBlockStatListVo resultListVo = null;
		try {
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
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
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			// 단일블록별 IP현황 목록 
			setPagination(searchVo);
			resultListVo = ipStatMgmtService.selectListSingleBlockIpStatMst(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new IpSingleBlockStatListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new IpSingleBlockStatListVo();
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
	 * 4.단일블록별 IP현황 목록 Excel
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListSingleBlockIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo viewListSingleBlockIpStatMstExcel(@ModelAttribute("searchVo") IpSingleBlockStatVo searchVo,
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
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
	
			//목록 조회 
			IpSingleBlockStatListVo resultListVo = ipStatMgmtService.selectListSingleBlockIpStatMstExcel(searchVo);
			
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("서비스망|getSsvcLineTypeNm");
			mappingList.add("본부|getSsvcGroupNm");
			mappingList.add("노드|getSsvcObjNm");
			mappingList.add("공인/사설|getSipCreateTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("IP블록|getPipPrefix");
			mappingList.add("할당 IP 수|getNallocCnt");
			mappingList.add("가용 IP 수|getNuseFreeCnt");
			mappingList.add("할당율(%)|getNpercent");
			mappingList.add("Subnet 수|getNsubnetCnt");
			
			String fileName = excelUtil.createExcelFile(resultListVo.getIpSingleBlockStatVos(), mappingList, request);
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
	
	/********************************************************************************2021 IPMS 고도화 ******************************/
	/**
	 * IP주소 라우팅 비교/점검 통계 현황 > 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListIntgrmSvcStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListIntgrmSvcStat(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request){
		ModelMap resultModel = new ModelMap();
		ModelMap model = viewListIntgrmSvcStatModel(searchVo, request);
		
		Map<String,Object> resultMap = (Map<String,Object>)model.get("result");
		resultModel.addAttribute("result", resultMap);
		
		return resultModel;
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListIntgrmSvcStat.do", method = RequestMethod.POST)
	public String viewListIntgrmSvcStat(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpIntgrmSvcStatVo searchVoClone = new IpIntgrmSvcStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListIntgrmSvcStatModel(searchVo, request);
		model.addAllAttributes(builtModel);
		
		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListIntgrmSvcStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListIntgrmSvcStat";
	}
	private ModelMap viewListIntgrmSvcStatModel(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcList = "";
		try {
			
			/* 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용  */
			Map<String,Object> sipCreateTypeParamMap = new HashMap<String,Object>();
			sipCreateTypeParamMap.put("startCd", "CT0001");
			sipCreateTypeParamMap.put("endCd", "CT0004");  // 사설은 사설 IP 신청 게시판에서만 등록 가능
			
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, sipCreateTypeParamMap);
			
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			
			if( null == searchVo.getHidDate()) {
				SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar cal = Calendar.getInstance();
		 
		        cal.add(Calendar.DATE, -1);
		        String searchBgnDe = Format.format(cal.getTime());
		        
			    searchVo.setSearchBgnDe(searchBgnDe);	
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
					if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
						
						TbLvlBasVo searchCenterVo = new TbLvlBasVo();
						searchCenterVo.setSsvcLineTypeCd("CL0001");
						centerListVo = jwtUtil.getCenterList(request, searchCenterVo);
						nodeListVo = new TbLvlBasListVo();
					} else {
						centerListVo = new TbLvlBasListVo();
						nodeListVo = new TbLvlBasListVo();	
					}
					
				}
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			model.addAttribute("centerListVo", centerListVo);
			model.addAttribute("nodeListVo", nodeListVo);
			
			/** 계위 Seq 목록 조회 **/
			if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
				searchVo.setSsvcLineTypeCd("CL0001");	// KORNET
			}
			
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}
			
			// 멀티셀렉트
			if(null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for(int i=0;i<sAssignTypeMulti.length;i++){
					listString.add(sAssignTypeMulti[i]);
				}
				
				searchVo.setSassignTypeMulti(listString);
			}
			
			List<Map<String,String>> svcList = new ArrayList<Map<String,String>>();
			int cnt = 0;
			
			if ((null == searchVo.getChkSvcList()  || searchVo.getChkSvcList().size() == 0) &&  null == searchVo.getSassignTypeMulti()) { //서비스 셋팅
				
				for(CommonCodeVo vo : sassignTypeCds) {
					
					if (cnt < 3) {
						Map<String,String> map = new HashMap<String, String>();
						map.put("code", vo.getCode());
						map.put("name", vo.getName());
						svcList.add(map);
					} else {
						break;
					}
					
					cnt += 1;
				}
				
				searchVo.setChkSvcList(svcList);
				
			} else {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					for(String str : searchVo.getSassignTypeMulti()) {
						
						Map<String,String> map = new HashMap<String, String>();
						if(vo.getCode().equals(str) && cnt < 3) {
						
							map.put("code", vo.getCode());
							map.put("name", vo.getName());
							svcList.add(map);
							cnt += 1;	
						} 
						
					} 
				}
			}
			model.addAttribute("svcList", svcList);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			model.addAttribute("svcListAll", svcListAll);
			
			searchVo.setChkSvcList(svcList);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListIntgrmSvcStatMst(searchVo);
			
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcList);
				
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("data", strList);
			resultMap.put("svcList", strSvcList);

			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		//model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("result", resultMap);
		return model;
	}
	
	
	/**
	 * IP주소 라우팅 비교/점검 통계 현황 > 전체 서비스 엑셀 다운
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/statmgmt/ipstatmgmt/selectListIntgrmAll.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public Map<String,Object> selectListIntgrmAll(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcList = "";
		
		try {
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}

			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			searchVo.setChkSvcList(svcListAll);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListIntgrmSvcStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcListAll);
			
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("result", strList);
			resultMap.put("svcList", strSvcList);
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		result.put("result", resultMap);
		
		return result;
	}
	
	/**
	 * IP 조직서비스별 통계 현황 > 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgSvcStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListOrgSvcStat(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request){
		ModelMap resultModel = new ModelMap();
		ModelMap model = viewListOrgSvcStatModel(searchVo, request);
		
		Map<String,Object> resultMap = (Map<String,Object>)model.get("result");
		resultModel.addAttribute("result", resultMap);
		
		return resultModel;
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListOrgSvcStat.do", method = RequestMethod.POST)
	public String viewListOrgSvcStat(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpIntgrmSvcStatVo searchVoClone = new IpIntgrmSvcStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListOrgSvcStatModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListOrgSvcStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListOrgSvcStat";
	}
	private ModelMap viewListOrgSvcStatModel(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcList = "";
		String sloadFlg = "T";
		try {
			
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
						
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			
			if( null == searchVo.getHidDate()) {
				SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar cal = Calendar.getInstance();
		 
		        cal.add(Calendar.DATE, -1);
		        String searchBgnDe = Format.format(cal.getTime());
		        
			    searchVo.setSearchBgnDe(searchBgnDe);	
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
			/*if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) { 
				searchVo.setSsvcLineTypeCd("CL0001");	// KORNET
			}*/
			
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList2(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}
			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			// 멀티셀렉트
			if(null != searchVo.getSassignTypeCdMultiStr() && !"".equals(searchVo.getSassignTypeCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sAssignTypeMulti = searchVo.getSassignTypeCdMultiStr().split(";");
				for(int i=0;i<sAssignTypeMulti.length;i++){
					listString.add(sAssignTypeMulti[i]);
				}
				
				searchVo.setSassignTypeMulti(listString);
			}
			
			List<Map<String,String>> svcList = new ArrayList<Map<String,String>>();
			int cnt = 0;
			
			if ((null == searchVo.getChkSvcList()  || searchVo.getChkSvcList().size() == 0) &&  null == searchVo.getSassignTypeMulti()) { //서비스 셋팅
				
				for(CommonCodeVo vo : sassignTypeCds) {
					
					if(vo.getCode().equals("SA0001") || vo.getCode().equals("SA0002") || vo.getCode().equals("SA0003")) {
						if (cnt <= 3) {
							Map<String,String> map = new HashMap<String, String>();
							map.put("code", vo.getCode());
							map.put("name", vo.getName());
							svcList.add(map);
						} else {
							break;
						}							
					}
					
					cnt += 1;
				}
				
				searchVo.setChkSvcList(svcList);
				
			} else {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					for(String str : searchVo.getSassignTypeMulti()) {
						
						Map<String,String> map = new HashMap<String, String>();
						if(vo.getCode().equals(str) && cnt < 3) {
						
							map.put("code", vo.getCode());
							map.put("name", vo.getName());
							svcList.add(map);
							cnt += 1;	
						} 
						
					} 
				}
			}
			model.addAttribute("svcList", svcList);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			model.addAttribute("svcListAll", svcListAll);
			
			searchVo.setChkSvcList(svcList);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
						
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListOrgSvcStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcList);
				
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("data", strList);
			resultMap.put("svcList", strSvcList);

			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		//model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("result", resultMap);
		return model;
	}
	
	
	/**
	 * IP 조직별서비스 통계 현황 > 전체 서비스 엑셀 다운
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/statmgmt/ipstatmgmt/selectListOrgSvcAll.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public Map<String,Object> selectListOrgSvcAll(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcList = "";
		
		try {
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList2(searchasTypeVo);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}

			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			searchVo.setChkSvcList(svcListAll);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
			
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListOrgSvcStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcListAll);
			
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("result", strList);
			resultMap.put("svcList", strSvcList);
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		result.put("result", resultMap);
		
		return result;
	}
	
	/**
	 * IP 서비스별 통계 현황 > 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListSvcStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListSvcStat(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request){
		ModelMap resultModel = new ModelMap();
		ModelMap model = viewListSvcStatModel(searchVo, request);
		
		Map<String,Object> resultMap = (Map<String,Object>)model.get("result");
		resultModel.addAttribute("result", resultMap);
		
		return resultModel;
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListSvcStat.do", method = RequestMethod.POST)
	public String viewListSvcStat(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpIntgrmSvcStatVo searchVoClone = new IpIntgrmSvcStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListSvcStatModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListSvcStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListSvcStat";
	}
	private ModelMap viewListSvcStatModel(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcLineList = "";
		try {
			
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
						
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			
			if( null == searchVo.getHidDate()) {
				SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar cal = Calendar.getInstance();
		 
		        cal.add(Calendar.DATE, -1);
		        String searchBgnDe = Format.format(cal.getTime());
		        
			    searchVo.setSearchBgnDe(searchBgnDe);	
			} 
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			List<TbLvlBasVo> tempList = svcLineListVo.getTbLvlBasVos();
			List<TbLvlBasVo> newList = new ArrayList<TbLvlBasVo>();
			for(int i=0;i<tempList.size();i++){
				if(!tempList.get(i).getSsvcLineTypeCd().equals("")){
					newList.add(tempList.get(i));
				}
			}
			
			svcLineListVo.setTbLvlBasVos(newList);
			svcLineListVo.setTotalCount(newList.size());
			
			model.addAttribute("svcLineListVo", svcLineListVo);
			
			List<TbLvlBasVo> tbLvlBasVo = svcLineListVo.getTbLvlBasVos();
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			                                                             
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}
			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			// 멀티셀렉트
			if(null != searchVo.getSsvcLineCdMultiStr() && !"".equals(searchVo.getSsvcLineCdMultiStr())){
				List<String> listString = new ArrayList<String>();
				String[] sSsvcLineCdMulti = searchVo.getSsvcLineCdMultiStr().split(";");
				for(int i=0;i<sSsvcLineCdMulti.length;i++){
					listString.add(sSsvcLineCdMulti[i]);
				}
				
				searchVo.setSsvcLineMulti(listString);
			}
			
			List<Map<String,String>> svcLineList = new ArrayList<Map<String,String>>();
			int cnt = 0;
			
			if ((null == searchVo.getChkSvcLineList()  || searchVo.getChkSvcLineList().size() == 0) &&  null == searchVo.getSsvcLineMulti()) { //서비스망 셋팅
				
				for(TbLvlBasVo vo : tbLvlBasVo) {
					if (cnt < 5) {
						
						PrintLogUtil.printLogInfoValueObject(vo);
						Map<String,String> map = new HashMap<String, String>();
						map.put("code", vo.getSsvcLineTypeCd());
						map.put("name", vo.getSsvcLineTypeNm());
						svcLineList.add(map);
					} else {
						break;
					}
					
					cnt += 1;
				}
				
				searchVo.setChkSvcList(svcLineList);
				
			} else {
				
				for(TbLvlBasVo vo : tbLvlBasVo) {
					for(String str : searchVo.getSsvcLineMulti()) {
						
						Map<String,String> map = new HashMap<String, String>();
						if(vo.getSsvcLineTypeCd().equals(str) && cnt < 5) {
						
							map.put("code", vo.getSsvcLineTypeCd());
							map.put("name", vo.getSsvcLineTypeNm());
							svcLineList.add(map);
							cnt += 1;	
						} 
						
					} 
				}
			}
			model.addAttribute("svcLineList", svcLineList);
			
			searchVo.setChkSvcLineList(svcLineList);
			//searchVo.setChkSvcList(svcList);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
						
			PrintLogUtil.printLogInfoValueObject(searchVo);
			// IP주소 서비스별 통계 현황 목록  
			resultList = ipStatMgmtService.selectListSvcStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcLineList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcLineList);
				
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("svcLineList", strSvcLineList);
			resultMap.put("data", strList);
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		//model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("result", resultMap);
		
		return model;
	}
	
	
	/**
	 * IP 서비스별 통계 현황 > 전체 서비스 엑셀 다운
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/statmgmt/ipstatmgmt/viewListSvcIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public Map<String,Object> selectListSvcAll(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcList = "";
		String strSvcLineList = "";
		
		try {
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}

			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			PrintLogUtil.printLogInfoValueObject(searchVo);
			
			List<Map<String,String>> svcLineList = new ArrayList<Map<String,String>>();
			svcLineList = ipStatMgmtService.selectListSvcLineList();
			
			List<Map<String,String>> chkLineList = new ArrayList<Map<String,String>>();
			for(Map<String,String> temp : svcLineList){
				Map<String,String> tempMap = new HashMap<String,String>();
				tempMap.put("name", temp.get("ssvc_line_type_nm"));
				tempMap.put("code", temp.get("ssvc_line_type_cd"));
				chkLineList.add(tempMap);
			}
			
			searchVo.setChkSvcLineList(chkLineList);
						
			//searchVo.setChkSvcList(svcListAll);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
			
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListSvcStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcListAll);
			strSvcLineList =  oReq.writerWithDefaultPrettyPrinter().writeValueAsString(chkLineList);
			
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("result", strList);
			resultMap.put("svcList", strSvcList);
			resultMap.put("svcLineList", strSvcLineList);
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		result.put("result", resultMap);
		
		return result;
	}
	
	
	/**
	 * IP 블록크기별 통계 현황 > 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListBlockSizeStat.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListBlockSizeStat(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request){
		ModelMap resultModel = new ModelMap();
		ModelMap model = viewListBlockSizeStatModel(searchVo, request);
		
		Map<String,Object> resultMap = (Map<String,Object>)model.get("result");
		resultModel.addAttribute("result", resultMap);
		
		return resultModel;
	}
	@RequestMapping(value="/statmgmt/ipstatmgmt/viewListBlockSizeStat.do", method = RequestMethod.POST)
	public String viewListBlockSizeStat(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		IpIntgrmSvcStatVo searchVoClone = new IpIntgrmSvcStatVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListBlockSizeStatModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/statmgmt/ipstatmgmt/viewListBlockSizeStat.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "statmgmt/ipstatmgmt/viewListBlockSizeStat";
	}
	private ModelMap viewListBlockSizeStatModel(@ModelAttribute("searchVo") IpIntgrmSvcStatVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String strList = "";
		String strSvcLineList = "";
		try {
			
			// 공인/사설 조회 
			List<CommonCodeVo> sipCreateTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_CREATE_TYPE_CD, null);
			
			// IP 버전 조회 
			List<CommonCodeVo> sipVersionTypeCds = commonCodeService.selectListCommonCode(CommonCodeUtil.IP_VERSION_TYPE_CD, null);
			
			model.addAttribute("sipVersionTypeCds", sipVersionTypeCds);
			model.addAttribute("sipCreateTypeCds", sipCreateTypeCds);
			
			if( null == searchVo.getHidDate()) {
				SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar cal = Calendar.getInstance();
		 
		        cal.add(Calendar.DATE, -1);
		        String searchBgnDe = Format.format(cal.getTime());
		        
			    searchVo.setSearchBgnDe(searchBgnDe);	
			} 
			
			/** 계위 정보 설정 **/
			TbLvlBasListVo svcLineListVo = jwtUtil.getSvcLineList(request);
			if (StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				TbLvlBasVo searchCenterVo = new TbLvlBasVo();
				searchCenterVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
				
			} else {
				if (StringUtils.hasText(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd())) {
					searchVo.setSsvcLineTypeCd(svcLineListVo.getTbLvlBasVos().get(0).getSsvcLineTypeCd());
				} 
			}
			model.addAttribute("svcLineListVo", svcLineListVo);
			
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			                                                             
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			model.addAttribute("sassignTypeCds", sassignTypeCds);
			
			// Default Type Setting 
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}
			
			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			
			// 블록사이즈 설정
			List<Map<String,String>> blockSizeCds = new ArrayList<Map<String,String>>();
			if(searchVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4)){
				for(int i=0;i<=32;i++){
					Map<String,String> map = new HashMap<String, String>();
					map.put("code",Integer.toString(i));
					map.put("name",Integer.toString(i));
					blockSizeCds.add(map);
				}
			}else{
				for(int i=0;i<=128;i++){
					Map<String,String> map = new HashMap<String, String>();
					map.put("code",Integer.toString(i));
					map.put("name",Integer.toString(i));
					blockSizeCds.add(map);
				}
			}
			
			model.addAttribute("blockSizeCds", blockSizeCds);
			
			// 멀티셀렉트
			if(null != searchVo.getSblockSizeMultiStr() && !"".equals(searchVo.getSblockSizeMultiStr())){
				List<Map<String,String>> listString = new ArrayList<Map<String,String>>();
				String[] sBlockSizeCdMulti = searchVo.getSblockSizeMultiStr().split(";");
				for(int i=0;i<sBlockSizeCdMulti.length;i++){
					Map<String,String> tempMap = new HashMap<String,String>();
					tempMap.put("code", sBlockSizeCdMulti[i]);
					tempMap.put("name", sBlockSizeCdMulti[i]);
					listString.add(tempMap);
				}
				searchVo.setChkBlockSizeList(listString);
			}
			
			List<Map<String,String>> blockSizeCdsList = new ArrayList<Map<String,String>>();
			int blockcnt = 0;
			
			if (null == searchVo.getChkBlockSizeList()) { //서비스망 셋팅
				for(Map<String,String> vo : blockSizeCds) {
					
					if(vo.get("code").equals("24") || vo.get("code").equals("25") || vo.get("code").equals("26") || vo.get("code").equals("27") ||vo.get("code").equals("28")) {
						if (blockcnt < 5) {						
							Map<String,String> map = new HashMap<String, String>();
							map.put("code", vo.get("code"));
							map.put("name", vo.get("name"));
							blockSizeCdsList.add(map);
						} else {
							break;
						}
					
						blockcnt += 1;
					}
				}
				searchVo.setChkBlockSizeList(blockSizeCdsList);
				
			}else{
				for(Map<String,String> vo : searchVo.getChkBlockSizeList()) {
					Map<String,String> map = new HashMap<String, String>();
					if(blockcnt < 5) {
						map.put("code", vo.get("code"));
						map.put("name", vo.get("name"));
						blockSizeCdsList.add(map);
						blockcnt += 1;	
					} 
				} 
			}
			
			model.addAttribute("blockSizeCdsList",blockSizeCdsList);
			
			//searchVo.setChkSvcList(svcList);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
						
			
			// IP주소 서비스별 통계 현황 목록  
			
			resultList = ipStatMgmtService.selectListBlockSizeStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			
			String strBlockSizeCds = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(blockSizeCds);
			String strBlockSizeCdsList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(blockSizeCdsList);
			
			resultMap.put("blockSizeCdsList",strBlockSizeCdsList);
			resultMap.put("blockSizeCds", strBlockSizeCds);
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			
			resultMap.put("data", strList);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			e.printStackTrace();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		//model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("result", resultMap);
		return model;
	}
	
	
	/* IP 서비스별 통계 현황 > 전체 서비스 엑셀 다운
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/statmgmt/ipstatmgmt/viewListBlockSizeIPStatExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public Map<String,Object> viewListBlockSizeIPStatExcel(@RequestBody IpIntgrmSvcStatVo searchVo, HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,String>> resultList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		String strList = "";
		String strSvcList = "";
		String strBlockSizeList = "";
		
		try {
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			/** 조직별 서비스 유형 셋팅(2014.12.04) **/
			TbIpAllocMstVo searchasTypeVo = new TbIpAllocMstVo();
			searchasTypeVo.setLvlMstSeqListVo(resultSeqList);
			List<CommonCodeVo> sassignTypeCds = ipStatMgmtService.selectOrgSassignTypeCdList(searchasTypeVo);
			
			List<Map<String,String>> svcListAll = new ArrayList<Map<String,String>>();
			if(sassignTypeCds.size() > 0) {
				for(CommonCodeVo vo : sassignTypeCds) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("code", vo.getCode());
					map.put("name", vo.getName());
					svcListAll.add(map);
				}
			}
			
			if (!StringUtils.hasText(searchVo.getSipCreateTypeCd())) { //공인 셋팅
				searchVo.setSipCreateTypeCd("CT0001");
			}
			
			if (!StringUtils.hasText(searchVo.getSipVersionTypeCd())) { //IPv4
				searchVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
			}
			
			if (!StringUtils.hasText(searchVo.getSearchBgnDe())) { // 작업일자 셋팅
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				Date date = new Date();
				String tmp = format.format(date);
				searchVo.setSearchBgnDe(tmp);
			}

			String originDate = searchVo.getSearchBgnDe();
			if(!searchVo.getSearchBgnDe().isEmpty()) {
				
				String dt = originDate.replaceAll("-", "");
				searchVo.setSearchBgnDe(dt);
			}
			

			// 블록사이즈 설정
			List<Map<String,String>> blockSizeCds = new ArrayList<Map<String,String>>();
			if(searchVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4)){
				for(int i=0;i<=32;i++){
					Map<String,String> map = new HashMap<String, String>();
					map.put("code",Integer.toString(i));
					map.put("name",Integer.toString(i));
					blockSizeCds.add(map);
				}
			}else{
				for(int i=0;i<=128;i++){
					Map<String,String> map = new HashMap<String, String>();
					map.put("code",Integer.toString(i));
					map.put("name",Integer.toString(i));
					blockSizeCds.add(map);
				}
			}
			
			searchVo.setChkBlockSizeList(blockSizeCds);
												
			//searchVo.setChkSvcList(svcListAll);			// 콤보박스에서 선택한 서비스유형
			searchVo.setSassignTypeCds(sassignTypeCds);	// 전체 서비스유형
			
			// IP주소 라우팅 비교/점검 통계 현황 목록  
			resultList = ipStatMgmtService.selectListBlockSizeStatMst(searchVo);
			searchVo.setSearchBgnDe(originDate);
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			
			strList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
			strSvcList = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(svcListAll);
			strBlockSizeList =  oReq.writerWithDefaultPrettyPrinter().writeValueAsString(blockSizeCds);
			
			resultMap.put("resultStatus", CommonCodeUtil.SUCCESS_MSG);
			resultMap.put("result", strList);
			resultMap.put("svcList", strSvcList);
			resultMap.put("blockSizeCds", strBlockSizeList);
			
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultMap.put("resultStatus", msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultMap.put("resultStatus", msgDesc);
		}
		
		result.put("result", resultMap);
		
		return result;
	}
	
}
