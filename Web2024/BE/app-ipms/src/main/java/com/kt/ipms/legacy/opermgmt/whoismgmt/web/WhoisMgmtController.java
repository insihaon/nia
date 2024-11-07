package com.kt.ipms.legacy.opermgmt.whoismgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.vo.FileVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.service.ReqBoardService;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.service.WhoisMgmtService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.WhoisStatusVo;
import com.kt.log4kt.utils.StringUtil;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class WhoisMgmtController extends CommonController {
	
	@Autowired
	private WhoisMgmtService whoisService;
	
	@Autowired
	private WhoisAdapterService whoisAdpaterService;
	
	@Autowired
	private TacsMgmtService tacsMgmtService;

	@Autowired
	private UserMgmtService userMgmtService;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	@Autowired
	private ReqBoardService reqBoardService;
	
	@RequestMapping(value="/opermgmt/whoismgmt/viewListWhois.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListTbWhois(@RequestBody TbWhoisVo searchVo, ModelMap model, HttpServletRequest request) {
		TbWhoisListVo resultListVo = whoisService.selectListPageWhois(searchVo);
		return createResultList(resultListVo.getTbWhoisVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="/opermgmt/whoismgmt/viewListWhois.do", method = RequestMethod.POST)
	public String viewListTbWhois(@ModelAttribute("searchVo") TbWhoisVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo searchVoClone = new TbWhoisVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListTbWhoisModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewListWhois.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewListWhois";
	}
	private ModelMap viewListTbWhoisModel(@ModelAttribute("searchVo") TbWhoisVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisListVo resultListVo = null;
		WhoisStatusVo statusVo = null;
		List<CommonCodeVo> sassignTypeCdList = null;
		
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		
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
			
			if("N".equals(searchVo.getLoadSearchYn())) {
				sloadFlg = "F";
			}
			
			
			if (sloadFlg.equals("T")){
				resultListVo = new TbWhoisListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = whoisService.selectListPageWhois(searchVo);
			}
			
			PrintLogUtil.printLogInfoValueObject(searchVo);
			
			statusVo = whoisService.countWhoisByStatus();
			
			/** AssignTypeCode List**/
			sassignTypeCdList = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, new HashMap<String,String>());
			
			/**Status Code 목록 조회(TRANS, REQUEST)**/
			List<CommonCodeVo> listTransStatusCd = whoisService.selectListVTbWhoisTransStatusCd();
			List<CommonCodeVo> listReqTypeCd = whoisService.selectListVTbWhoisReqTypeCd();
			model.addAttribute("listTransStatusCd", listTransStatusCd);
			model.addAttribute("listReqTypeCd", listReqTypeCd);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msg);
		} catch(Exception e) {
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msg);		
		}
		model.addAttribute("resultListVo",resultListVo);
		model.addAttribute("statusVo",statusVo);
		model.addAttribute("sassignTypeCdList", sassignTypeCdList);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/viewRegWhois.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewRegWhois(@RequestBody TbWhoisVo tbWhoisVo, ModelMap model, HttpServletRequest request) {
		TbWhoisVo resultVo = whoisService.selectWhois(tbWhoisVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewRegWhois.ajax", method = RequestMethod.POST)
	public String viewRegWhois(@RequestBody TbWhoisVo tbWhoisVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo searchVoClone = new TbWhoisVo();
		try {
			CloneUtil.copyObjectInformation(tbWhoisVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewRegWhoisModel(tbWhoisVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewRegWhois.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewRegWhois";
	}
	private ModelMap viewRegWhoisModel(@RequestBody TbWhoisVo tbWhoisVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisVo resultVo = null;
		TbWhoisUserVo userVo = null;
		List<String> scity = null; 
		TbWhoisUserVo ktInfoVo = null;
		try {
			resultVo = whoisService.selectWhois(tbWhoisVo);
			TbWhoisUserVo searchVo = new TbWhoisUserVo();
			searchVo.setSsaid(resultVo.getSsaid());
			userVo = whoisService.selectWhoisUser(searchVo);
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
			scity = whoisService.selectListScity();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo = new TbWhoisVo();
			userVo = new TbWhoisUserVo();
			resultVo.setCommonMsg(msg);
			userVo.setCommonMsg(msg);
			scity = new ArrayList<String>();
			ktInfoVo = new TbWhoisUserVo();
		} catch(Exception e) {
			resultVo = new TbWhoisVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
			scity = new ArrayList<String>();
			ktInfoVo = new TbWhoisUserVo();
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("userVo", userVo);
		model.addAttribute("scity", scity);
		model.addAttribute("ktInfoVo", ktInfoVo);
		return model;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/viewSearchZipCode.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewSearchZipCode(@RequestBody TbNewZipcodeVo tbZipcodeVo, ModelMap model, HttpServletRequest request) {
		setPagination(tbZipcodeVo);
		TbNewZipcodeListVo resultListVo = whoisService.selectListPageTbNewZipcode(tbZipcodeVo);
		return createResultList(resultListVo.getTbZipcodeVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewSearchZipCode.ajax", method = RequestMethod.POST)
	public String viewSearchZipCode(@RequestBody TbNewZipcodeVo tbZipcodeVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbNewZipcodeVo searchVoClone = new TbNewZipcodeVo();
		try {
			CloneUtil.copyObjectInformation(tbZipcodeVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchZipCodeModel(tbZipcodeVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewSearchZipCode.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewSearchZipCode";
	}
	private ModelMap viewSearchZipCodeModel(@RequestBody TbNewZipcodeVo tbZipcodeVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		if(tbZipcodeVo == null || tbZipcodeVo.getDong() == null) {
			model.addAttribute("pageType", tbZipcodeVo.getPageType());
			
			setPagination(tbZipcodeVo);
			PaginationInfo paginationInfo = getPaginationInfo();
			paginationInfo.setTotalRecordCount(0);
			
			model.addAttribute("resultListVo", null);
			model.addAttribute("paginationInfo", paginationInfo);
			return model;
		}
		TbNewZipcodeListVo resultListVo = null;
		try {
			
			setPagination(tbZipcodeVo);
			resultListVo = whoisService.selectListPageTbNewZipcode(tbZipcodeVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultListVo = new TbNewZipcodeListVo();
			resultListVo.setTotalCount(0);
			resultListVo.setCommonMsg(msg);
		} catch(Exception e) {
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msg);
		}
		
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("searchWrd", tbZipcodeVo.getDong());
		model.addAttribute("pageType", tbZipcodeVo.getPageType());
		return model;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/viewUpdateKtInfo.model", method=RequestMethod.POST)
	public ModelMap viewUpdateKtInfo(ModelMap model, HttpServletRequest request) {
		TbWhoisUserVo resultVo = new TbWhoisUserVo();
		resultVo = whoisService.selectWhoisUser(resultVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewUpdateKtInfo.ajax", method=RequestMethod.POST)
	public String viewUpdateKtInfo(@RequestBody CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		CommonVo searchVoClone = new CommonVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateKtInfoModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewUpdateKtInfo.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewUpdateKtInfo";
	}
	private ModelMap viewUpdateKtInfoModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisUserVo resultVo = null;
		try {
			resultVo = new TbWhoisUserVo();
			resultVo.setSsaid("00000000000");
			resultVo = whoisService.selectWhoisUser(resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			resultVo = new TbWhoisUserVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			 resultVo = new TbWhoisUserVo();
			 String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			 resultVo.setCommonMsg(msg);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	@RequestMapping(value="opermgmt/whoismgmt/updateKtInfo.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisUserVo updateKtInfoSubmit(@RequestBody TbWhoisUserVo ktInfoVo, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisUserVo resultVo = null;
		try {
			resultVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo.setSmodifyId(jwtUtil.getUserId(request));
			
			int result = whoisService.updateWhoisUser(ktInfoVo);
			if(result == 1) {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			resultVo = new TbWhoisUserVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			resultVo = new TbWhoisUserVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
		}
		return resultVo;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/selectEaddr.json", method=RequestMethod.POST)
	@ResponseBody
	public String selectEaddrDetail(@RequestBody String addrDetail) {
		String eaddrDetail = null;
		try {
			eaddrDetail = whoisService.selectEaddrDetail(addrDetail);
		} catch(ServiceException e) {
			eaddrDetail = "";
		} catch(Exception e) {
			eaddrDetail = "";
		}
		eaddrDetail = eaddrDetail.replaceAll("[<]", "&lt;");
		eaddrDetail = eaddrDetail.replaceAll("[>]", "&gt;");
		return eaddrDetail;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/updateWhoisComplex.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisComplexVo updateWhosiComplex(@RequestBody TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisComplexVo resultVo = null;
		try {
			int result = whoisService.updateWhoisComplex(tbWhoisComplexVo);
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = new TbWhoisComplexVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
		}
		return resultVo;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisKeywordMst.model", method=RequestMethod.POST)
	public ModelMap viewListWhoisKeywordMst(@RequestBody TbWhoisKeywordVo searchVo) {
		TbWhoisKeywordListVo resultListVo = whoisService.selectListTbWhoisKeyword(searchVo);
		return createResultList(resultListVo.getTbWhoisKeywordVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisKeywordMst.ajax", method=RequestMethod.POST)
	public String viewListWhoisKeywordMst(@RequestBody TbWhoisKeywordVo searchVo, ModelMap model) {
		TbWhoisKeywordVo searchVoClone = new TbWhoisKeywordVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListWhoisKeywordMstModel(searchVo);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewListWhoisKeywordMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewListWhoisKeywordMst";
	}
	
	private ModelMap viewListWhoisKeywordMstModel(@RequestBody TbWhoisKeywordVo searchVo) {
		ModelMap model = new ModelMap();
		TbWhoisKeywordListVo resultListVo = null;
		model.addAttribute("searchVo", searchVo);
		try{
			setPagination(searchVo);
			resultListVo = whoisService.selectListTbWhoisKeyword(searchVo);
		} catch(ServiceException e){
			resultListVo = new TbWhoisKeywordListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbWhoisKeywordListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultListVo", resultListVo);
		return model;
	}
	
	@RequestMapping(value = "opermgmt/whoismgmt/viewInsertWhoisKeyword.ajax", method=RequestMethod.POST)
	public String viewInserWhoisKeyword(){
		return "opermgmt/whoismgmt/viewRegWhoisKeywordMst";
	}
	
	@RequestMapping(value = "opermgmt/whoismgmt/insertWhoisKeyword.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisKeywordVo insertWhoisKeywordVo(@RequestBody TbWhoisKeywordVo tbWhoisKeywordVo) {
		TbWhoisKeywordVo resultVo = null;
		try {
			int count = whoisService.insertWhoisKeywordVo(tbWhoisKeywordVo);
			String commonMsg;
			if(count != 1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
			}
			resultVo = new TbWhoisKeywordVo();
			resultVo.setCommonMsg(commonMsg);
		} catch(ServiceException e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/deleteWhoisKeyword.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisKeywordVo deleteWhoisKeyword(@RequestBody TbWhoisKeywordListVo tbWhoisKeywordListVo) {
		TbWhoisKeywordVo resultVo = null;
		try {
			whoisService.deleteWhoisKeyword(tbWhoisKeywordListVo);
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = CommonCodeUtil.SUCCESS_MSG;
			resultVo.setCommonMsg(commonMsg);
		} catch(ServiceException e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	@RequestMapping(value="opermgmt/whoismgmt/insertWhoisComplex.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisComplexVo insertWhois(@RequestBody TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisComplexVo resultVo = null;
		try {
			int result = whoisService.insertWhoisComplex(tbWhoisComplexVo);
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = new TbWhoisComplexVo();
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
		}
		return resultVo;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * WHOIS 대체 키워드 목록 화면 로드 & 조회
	 * @param searchVo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisKeywordMstNew.model", method=RequestMethod.POST)
	public ModelMap viewListWhoisKeywordMstNew(@RequestBody TbWhoisKeywordVo searchVo) {
		TbWhoisKeywordListVo resultListVo = whoisService.selectListTbWhoisKeywordNew(searchVo);
		return createResultList(resultListVo.getTbWhoisKeywordVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisKeywordMstNew.ajax", method=RequestMethod.POST)
	public String viewListWhoisKeywordMstNew(@RequestBody TbWhoisKeywordVo searchVo, ModelMap model) {
		TbWhoisKeywordVo searchVoClone = new TbWhoisKeywordVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListWhoisKeywordMstNewModel(searchVo);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewListWhoisKeywordMstNew.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewListWhoisKeywordMstNew";
	}
	private ModelMap viewListWhoisKeywordMstNewModel(@RequestBody TbWhoisKeywordVo searchVo) {
		ModelMap model = new ModelMap();
		TbWhoisKeywordListVo resultListVo = null;
		model.addAttribute("searchVo", searchVo);
		try{
			setPagination(searchVo);
			resultListVo = whoisService.selectListTbWhoisKeywordNew(searchVo);
		} catch(ServiceException e){
			resultListVo = new TbWhoisKeywordListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbWhoisKeywordListVo();
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
	 * WHOIS 대체 키워드 등록 
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@RequestMapping(value = "opermgmt/whoismgmt/insertWhoisKeywordNew.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisKeywordVo insertWhoisKeywordNewVo(@RequestBody TbWhoisKeywordVo tbWhoisKeywordVo, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisKeywordVo resultVo = null;
		
		try {
			tbWhoisKeywordVo.setScreateId(jwtUtil.getUserId(request));
			int count = whoisService.insertWhoisKeywordNewVo(tbWhoisKeywordVo);
			String commonMsg;
			if(count != 1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
			}
			resultVo = new TbWhoisKeywordVo();
			resultVo.setCommonMsg(commonMsg);
		} catch(ServiceException e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	/**
	 * WHOIS 대체 키워드 삭제
	 * @param tbWhoisKeywordListVo
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/deleteWhoisKeywordNew.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisKeywordVo deleteWhoisKeywordNew(@RequestBody TbWhoisKeywordListVo tbWhoisKeywordListVo) {
		TbWhoisKeywordVo resultVo = null;
		try {
			whoisService.deleteWhoisKeywordNew(tbWhoisKeywordListVo);
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = CommonCodeUtil.SUCCESS_MSG;
			resultVo.setCommonMsg(commonMsg);
		} catch(ServiceException e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisKeywordVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	/**
	 * WHOIS 정보수정 팝업 화면 로드 & 조회
	 * @param tbWhoisVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/viewRegWhoisNew.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewRegWhoisNew(@RequestBody TbWhoisVo tbWhoisVo, ModelMap model, HttpServletRequest request) {
		TbWhoisVo resultVo = whoisService.selectWhois(tbWhoisVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewRegWhoisNew.ajax", method = RequestMethod.POST)
	public String viewRegWhoisNew(@RequestBody TbWhoisVo tbWhoisVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo searchVoClone = new TbWhoisVo();
		try {
			CloneUtil.copyObjectInformation(tbWhoisVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewRegWhoisNewModel(tbWhoisVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewRegWhoisNew.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewRegWhoisNew";
	}
	private ModelMap viewRegWhoisNewModel(@RequestBody TbWhoisVo tbWhoisVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisVo resultVo = null;
		TbWhoisUserVo userVo = null;
		List<String> scity = null; 
		TbWhoisUserVo ktInfoVo = null;
		
		TbWhoisUserVo allocInfoVo = null;
		TbWhoisComplexVo tmpVo = null;
		
		try {
			resultVo = whoisService.selectWhois(tbWhoisVo);
			TbWhoisUserVo searchVo = new TbWhoisUserVo();
			searchVo.setSsaid(resultVo.getSsaid());
			userVo = whoisService.selectWhoisUser(searchVo);
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
			scity = whoisService.selectListScity();
			
			if("N".equals(resultVo.getDbMatchYn())) {
				tmpVo = whoisService.selectWhoisAlloc(tbWhoisVo);
				allocInfoVo = tmpVo.getTbWhoisUserVo();
			}
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch(ServiceException e) {
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo = new TbWhoisVo();
			userVo = new TbWhoisUserVo();
			resultVo.setCommonMsg(msg);
			userVo.setCommonMsg(msg);
			scity = new ArrayList<String>();
			ktInfoVo = new TbWhoisUserVo();
			allocInfoVo = new TbWhoisUserVo();
		} catch(Exception e) {
			resultVo = new TbWhoisVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
			scity = new ArrayList<String>();
			ktInfoVo = new TbWhoisUserVo();
			allocInfoVo = new TbWhoisUserVo();
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("userVo", userVo);
		model.addAttribute("scity", scity);
		model.addAttribute("ktInfoVo", ktInfoVo);
		model.addAttribute("allocInfoVo", allocInfoVo);
		return model;
	}
	
	
	/**
	 * WHOIS 정보수정 팝업 수정
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/updateWhoisComplexNew.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisComplexVo updateWhosiComplexNew(@RequestBody TbWhoisComplexVo tbWhoisComplexVo, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisComplexVo resultVo = new TbWhoisComplexVo();
		int result = 0;
		try {
			tbWhoisComplexVo.getTbWhoisVo().setSmodifyId(jwtUtil.getUserId(request));
			tbWhoisComplexVo.getTbWhoisUserVo().setSmodifyId(jwtUtil.getUserId(request));
			
			if(tbWhoisComplexVo.getTbWhoisVo().getType().equals("ADD_NETNAME_ERROR")) {		// 추가신청서 - 네트워크명 오류
				result = whoisService.updateWhoisComplexNew2(tbWhoisComplexVo);
			} else if(tbWhoisComplexVo.getTbWhoisVo().getType().equals("ADD_IPBLOCK_ERROR_RESUBMIT")) { // 추가신청서 - IP BLOCK 중복 후 재전송
				result = whoisService.updateWhoisComplexNew2(tbWhoisComplexVo);
			} else if(tbWhoisComplexVo.getTbWhoisVo().getType().equals("NEW_IPBLOCK_ERROR_RESUBMIT")) { // 신규신청서 - IP BLOCK 중복 후 재전송
				result = whoisService.updateWhoisComplexNew2(tbWhoisComplexVo);
			} else if (tbWhoisComplexVo.getTbWhoisVo().getType().equals("DEFAULT")) {
				result = whoisService.updateWhoisComplexNew(tbWhoisComplexVo);
			} else if(tbWhoisComplexVo.getTbWhoisVo().getType().equals("MOD_ERROR")) { // 신규신청서로 재전송
				result = whoisService.updateWhoisComplexNew2(tbWhoisComplexVo);
			} else if(tbWhoisComplexVo.getTbWhoisVo().getType().equals("NEW_NETNAME_ERROR")) { // 추가 신청서로 재전송
				result = whoisService.updateWhoisComplexNew2(tbWhoisComplexVo);
			}
			
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			//resultVo = new TbWhoisComplexVo();
			//resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
			TbWhoisComplexVo searchVo = new TbWhoisComplexVo();
			TbWhoisVo t = new TbWhoisVo();
			t.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
			searchVo.setTbWhoisVo(t);
			TbWhoisComplexListVo resultListVo = whoisService.selectListWhoisComplex(searchVo);
			List<TbWhoisComplexVo> vo = resultListVo.getTbWhoisComplexVos();
			
			if("03".equals(tbWhoisComplexVo.getTbWhoisVo().getSrequestCd())) {
				
				if(vo.size() > 0) {
					t.setSwhoisresultCd(vo.get(0).getTbWhoisVo().getSwhoisresultCd());
					//t.setSwhoisresultMsg(vo.get(0).getTbWhoisVo().getSwhoisresultMsg());
				} else {
					t.setSwhoisresultCd("04");
					//t.setSwhoisresultMsg(CommonCodeUtil.SUCCESS_MSG);
				}
				
			} else {
				if(vo.size() > 0) {
					t.setSwhoisresultCd(vo.get(0).getTbWhoisVo().getSwhoisresultCd());
					//t.setSwhoisresultMsg(vo.get(0).getTbWhoisVo().getSwhoisresultMsg());
					
				} else {
					throw new ServiceException("CMN.HIGH.00001");
				}
			}
			
			resultVo.setTbWhoisVo(t);
		} catch(ServiceException e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msg);
		} catch(Exception e) {
			resultVo = new TbWhoisComplexVo();
			String msg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msg);
		}
		return resultVo;
	}
	
	/**
	 * WHOIS 정보 변경 신청 목록 화면 로드 & 조회
	 * @param searchVo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewListWhoisModReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewListWhoisModReq(@RequestBody TbWhoisModifyVo searchVo, ModelMap model, HttpServletRequest request) {
		TbWhoisModifyListVo resultListVo = whoisService.selectListPageTbWhoisModifyVo(searchVo);
		return createResultList(resultListVo.getTbWhoisModifyVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/whoismgmt/viewListWhoisModReq.do", method = RequestMethod.POST)
	public String viewListWhoisModReq(@ModelAttribute("searchVo") TbWhoisModifyVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo searchVoClone = new TbWhoisModifyVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListWhoisModReqModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewListWhoisModReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewListWhoisModReq";
	}
	private ModelMap viewListWhoisModReqModel(@ModelAttribute("searchVo") TbWhoisModifyVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisModifyListVo resultListVo = null;
		try{
			
			String userGrade  = jwtUtil.getUserGradeCd(request);
			String userId  =  jwtUtil.getUserId(request);
			
			// WHOIS정보변경신청 목록 리스트 표기시 관리자제외 모든 사용자는 자신글만 조회
			if(!userGrade.equals(CommonCodeUtil.USER_GRADE_A)) {
				searchVo.setsApplyId(userId);
			}
			
			/** 정열조건 세팅 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("DAPPLY_DT");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr("DESC");
			}
			
			setPagination(searchVo);
			resultListVo = whoisService.selectListPageTbWhoisModifyVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbWhoisModifyListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbWhoisModifyListVo();
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
	 * WHOIS 정보 변경 신청 목록 엑셀 다운로드
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewListWhoisModReqExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListWhoisModReqExcel(@RequestBody TbWhoisModifyVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try{
			
			
			String userGrade  = jwtUtil.getUserGradeCd(request);
			String userId  =  jwtUtil.getUserId(request);
			// WHOIS정보변경신청 목록 리스트 표기시 관리자제외 모든 사용자는 자신글만 조회
			if(!userGrade.equals(CommonCodeUtil.USER_GRADE_A)) {
				searchVo.setsApplyId(userId);
			}
			
			/** 정열조건 세팅 **/
			if (!StringUtils.hasText(searchVo.getSortType())) {
				searchVo.setSortType("DCREATE_DT");
			}
			if (!StringUtils.hasText(searchVo.getSortOrdr())) {
				searchVo.setSortOrdr("DESC");
			}
			
			
			TbWhoisModifyListVo  resultListVo = whoisService.viewListWhoisModReqExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("신청번호|getNmodify_apply_seq");
			mappingList.add("시작IP|getSfirstAddr");
			mappingList.add("끝IP|getSlastAddr");
			mappingList.add("기관명(변경전)|getsBefOrgName");
			mappingList.add("기관명(변경후)|getsAftOrgName");
			mappingList.add("신청자|getsApplyNm");
			mappingList.add("신청일시|getdApplyDt");
			mappingList.add("처리일시|getdApprovalDt");
			mappingList.add("상태|getsStatNm");

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbWhoisModifyVos(), mappingList, request);
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
	 * WHOIS 정보 변경 신청 상세보기 팝업 화면 로드 & 조회
	 * @param tbWhoisModifyVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "opermgmt/whoismgmt/viewDetailWhoisModReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailWhoisModReq(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, ModelMap model, HttpServletRequest request) {
		TbWhoisModifyVo resultVo = whoisService.selectTbWhoisModifyVo(tbWhoisModifyVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "opermgmt/whoismgmt/viewDetailWhoisModReq.ajax", method = RequestMethod.POST)
	public String viewDetailWhoisModReq(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo searchVoClone = new TbWhoisModifyVo();
		try {
			CloneUtil.copyObjectInformation(tbWhoisModifyVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewDetailWhoisModReqModel(tbWhoisModifyVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewDetailWhoisModReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewDetailWhoisModReq"; 
	}
	private ModelMap viewDetailWhoisModReqModel(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisModifyVo resultVo = null;
		String userGrade = null;
		try {
			userGrade = jwtUtil.getUserGradeCd(request);
			resultVo = whoisService.selectTbWhoisModifyVo(tbWhoisModifyVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("userGrade", userGrade);
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 *  WHOIS 정보 변경 신청 팝업 화면 로드 & IP 주소조회
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewRegWhoisModReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap _viewRegWhoisModReq(@RequestBody TbWhoisModifyVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisModifyListVo resultListVo = null;
		TbWhoisUserVo ktInfoVo = null;
		try {
			setPagination(searchVo);
			resultListVo = whoisService.selectListTbWhoisModReqIp(searchVo);
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
		} catch(ServiceException e){
			resultListVo = new TbWhoisModifyListVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbWhoisModifyListVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		model.addAttribute("resultListVo", resultListVo.getTbWhoisModifyVos());
		model.addAttribute("resultListVoTotalCount", resultListVo.getTotalCount());
		model.addAttribute("ktInfoVo", ktInfoVo);
		
		return model;
	}
	@RequestMapping(value = "/opermgmt/whoismgmt/viewRegWhoisModReq.ajax", method = RequestMethod.POST)
	public String viewRegWhoisModReq(@RequestBody TbWhoisModifyVo searchVo, ModelMap model, HttpServletRequest request) {
		TbWhoisModifyVo searchVoClone = new TbWhoisModifyVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewRegWhoisModReqModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewRegWhoisModReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewRegWhoisModReq";
	}
	private ModelMap viewRegWhoisModReqModel(@RequestBody TbWhoisModifyVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisModifyListVo resultListVo = null;
		TbWhoisUserVo ktInfoVo = null;
		model.addAttribute("searchVo", searchVo);

		try{
			setPagination(searchVo);
			resultListVo = whoisService.selectListTbWhoisModReqIp(searchVo);
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
			
		} catch(ServiceException e){
			resultListVo = new TbWhoisModifyListVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultListVo = new TbWhoisModifyListVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("ktInfoVo", ktInfoVo);
		return model;
	}
	
	
	/**
	 * WHOIS정보변경신청 - WHOIS 정보 조회
	 * @param searchVo
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/selectSearchWhoisInfo.json", method = RequestMethod.POST)
	@ResponseBody
	public TbWhoisModifyVo selectSearchWhoisInfo(@RequestBody TbWhoisModifyVo searchVo,  HttpServletRequest request, HttpServletResponse response){
		
		TbWhoisModifyVo resultVo = null;
		try{			
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String firstAddr = searchVo.getSfirstAddr();
			String lastAddr = searchVo.getSlastAddr();
			
			String newClTrid = searchVo.getSwhoisrequestid() +  "-" + simpleDateFormat.format(new Date());
			
			WhoisInfoObj obj = whoisAdpaterService.whoisIpInfo(firstAddr, lastAddr, newClTrid);
			resultVo = new TbWhoisModifyVo();
			resultVo.setInfoObj(obj);
			
			if(obj.getRtnMsg() == null) {
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			} else {
				 if(obj.getRtnMsg().equals("SOCKET_ERROR")) {
					 resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
				 } else {
					 resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
				 }
			}
			
		} catch (ServiceException e) {	
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);	
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc =tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}		
		
		return resultVo;
	}
	
	
	/**
	 * WHOIS정보변경신청 - 등록(변경신청)
	 * @param tbWhoisModifyVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "opermgmt/whoismgmt/insertRegWhoisModReq.json", method=RequestMethod.POST)
	@ResponseBody
	public TbWhoisModifyVo insertRegWhoisModReqVo(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo resultVo = null;
		try {
			tbWhoisModifyVo.setsApplyId(jwtUtil.getUserId(request));
			tbWhoisModifyVo.setsCreateId(jwtUtil.getUserId(request));
			
			int count = whoisService.insertWhoisModifyVo(tbWhoisModifyVo);
			String commonMsg;
			if(count != 1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
				commonMsg = SendMail("Whois-Req-Info-User", tbWhoisModifyVo, request);		// Whois 정보 변경신청시(요청자)
				commonMsg = SendMail("Whois-Req-Info-Admin", tbWhoisModifyVo, request);		// Whois 정보 변경신청시(담당자)
			}
			resultVo = new TbWhoisModifyVo();
			resultVo.setCommonMsg(commonMsg);
		} catch(ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisModifyVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		return resultVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 수정화면 로드
	 * @param tbWhoisModifyVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewUpdateWhoisModReq.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateWhoisModReq(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, ModelMap model, HttpServletRequest request) {
		TbWhoisModifyVo resultVo = whoisService.selectTbWhoisModifyVo(tbWhoisModifyVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/whoismgmt/viewUpdateWhoisModReq.ajax", method = RequestMethod.POST)
	public String viewUpdateWhoisModReq(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo searchVoClone = new TbWhoisModifyVo();
		try {
			CloneUtil.copyObjectInformation(tbWhoisModifyVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateWhoisModReqModel(tbWhoisModifyVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewUpdateWhoisModReq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewUpdateWhoisModReq";
	}
	private ModelMap viewUpdateWhoisModReqModel(@RequestBody TbWhoisModifyVo tbWhoisModifyVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisModifyVo resultVo = null;
		TbWhoisUserVo ktInfoVo = null;
		try {
			resultVo = whoisService.selectTbWhoisModifyVo(tbWhoisModifyVo);
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			ktInfoVo = new TbWhoisUserVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			ktInfoVo = new TbWhoisUserVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("ktInfoVo", ktInfoVo);
		return model;
	}
	
	
	/**
	 * WHOIS정보변경신청 - 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewUpdateWhoisModReqVo.json", method = RequestMethod.POST)
	public TbWhoisModifyVo viewUpdateWhoisModReqVo(@RequestBody TbWhoisModifyVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo resultVo = null;
		try {
			updateVo.setsModifyId(jwtUtil.getUserId(request));
			
			resultVo = whoisService.updateWhoisModReqVo(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 수정(변경신청취소)
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/viewDeleteWhoisModReq.json", method = RequestMethod.POST)
	public TbWhoisModifyVo viewDeleteWhoisModReq(@RequestBody TbWhoisModifyVo deleteVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo resultVo = null;
		try{
			resultVo = whoisService.deleteWhoisModReqVo(deleteVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 승인/반려
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/updateWhoisModReqAppr.json", method = RequestMethod.POST)
	public TbWhoisModifyVo updateWhoisModReqAppr(@RequestBody TbWhoisModifyVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisModifyVo resultVo = new TbWhoisModifyVo();
		String commonMsg;
		TbWhoisModifyVo tbWhoisModifiyVo = new TbWhoisModifyVo();
		try {
			updateVo.setsApprovalId(jwtUtil.getUserId(request));
			updateVo.setsCreateId(jwtUtil.getUserId(request));
			updateVo.setsModifyId(jwtUtil.getUserId(request));
			int count= whoisService.updateWhoisModReqAppr(updateVo);

			if(count !=1) {
				commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			} else {
				
				commonMsg = CommonCodeUtil.SUCCESS_MSG;
				tbWhoisModifiyVo = whoisService.selectTbWhoisModifyVo(updateVo);
				
				if(tbWhoisModifiyVo.getsStatCd().equals("20")) {
					commonMsg = SendMail("Whois-Aprv-Info", tbWhoisModifiyVo, request);
				} else if(tbWhoisModifiyVo.getsStatCd().equals("30")) {
					commonMsg = SendMail("Whois-Reject-Info", tbWhoisModifiyVo, request);
				} else {
					commonMsg = CommonCodeUtil.FAIL_MSG;
				}
			}
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbWhoisModifyVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbWhoisModifyVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * WHOIS정보공개관리 - 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/deleteTbWhoisVo.json", method = RequestMethod.POST)
	public TbWhoisVo deleteTbWhoisVo(@RequestBody TbWhoisVo deleteVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo resultVo = new TbWhoisVo();
		String commonMsg;
		try {
			
			whoisService.deleteTbWhoisVo(deleteVo);
			resultVo = new TbWhoisVo();
			commonMsg = CommonCodeUtil.SUCCESS_MSG;
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * WHOIS정보공개관리 - 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/deleteTbWhoisVo2.json", method = RequestMethod.POST)
	public TbWhoisVo deleteTbWhoisVo2(@RequestBody TbWhoisVo deleteVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo resultVo = new TbWhoisVo();
		String commonMsg;
		try {
			
			whoisService.deleteKisaIp(deleteVo);
			
			whoisService.deleteTbWhoisVo(deleteVo);
			resultVo = new TbWhoisVo();
			commonMsg = CommonCodeUtil.SUCCESS_MSG;
			resultVo.setCommonMsg(commonMsg);
		} catch (ServiceException e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 *  WHOIS정보공개관리 - DB 현행화 (목록)
	 * @param matchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/dbMatchListTbWhoisVo.json", method = RequestMethod.POST)
	public TbWhoisVo dbMatchListTbWhoisVo(@RequestBody TbWhoisVo matchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo resultVo = new TbWhoisVo();
		String commonMsg;
		try {
			
			matchVo.setScreateId(jwtUtil.getUserId(request));
			matchVo.setSmodifyId(jwtUtil.getUserId(request));
			String result = whoisService.matchTbWhoisVo("LIST", matchVo);
			
			resultVo = new TbWhoisVo();
			resultVo.setCommonMsg(result);
		} catch (ServiceException e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 *  WHOIS정보공개관리 - DB 현행화 (단건)
	 * @param matchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/dbMatchTbWhoisVo.json", method = RequestMethod.POST)
	public TbWhoisVo dbMatchTbWhoisVo(@RequestBody TbWhoisVo matchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo resultVo = new TbWhoisVo();
		String commonMsg;
		try {
			
			matchVo.setScreateId(jwtUtil.getUserId(request));
			matchVo.setSmodifyId(jwtUtil.getUserId(request));
			String result = whoisService.matchTbWhoisVo("POPUP", matchVo);
			
			resultVo = new TbWhoisVo();
			resultVo.setCommonMsg(result);
		} catch (ServiceException e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * Mail Value Setting
	 * @param type
	 * @param tbWhoisModifyVo
	 * @return
	 */
	public String SendMail (String mailType, TbWhoisModifyVo tbWhoisModifyVo, HttpServletRequest request) {
		
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
			searchVo.setSuserId(tbWhoisModifyVo.getsCreateId());
			userEmail = userMgmtService.selectEmail(searchVo);					// 요청자 메일주소
			List<ReqAdminEmailVo> reqAdminEmailVoList = reqBoardService.selectAdminEmailList(); //담당자 주소
			
			userName = jwtUtil.getUserNm(request);
			userOrg = jwtUtil.getUserDeptOrgNm(request);
			
			Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
			
			map.put("MAIL_TYPE", mailType);
			
			if(mailType.equals("Whois-Req-Info-User")) {					// 변경 신청시
				// 요청자에게 발송할 메일
				subject = "[접수알림] IPMS Whois 정보 변경 신청 접수 알림";
				
				map.put("FIRST_IP", tbWhoisModifyVo.getSfirstAddr());			// 시작 IP
				map.put("LAST_IP", tbWhoisModifyVo.getSlastAddr());			// 끝 IP
				map.put("BEF_ORG_NAME", tbWhoisModifyVo.getsBefOrgName());			// 한글기관명(변경전)
				map.put("BEF_ORG_ADDR", tbWhoisModifyVo.getsBefOrgAddr());			// 한글주소(변경전)
				map.put("BEF_ZIPCODE", tbWhoisModifyVo.getsBefZipCode());			// 우편번호(변경전)
				map.put("BEF_ORG_ENAME", tbWhoisModifyVo.getsBefEOrgName());			// 영문기관명(변경전)
				map.put("BEF_ORG_EADDR", tbWhoisModifyVo.getsBefEOrgAddr());			// 영문주소(변경전)
				map.put("AFT_ORG_NAME", tbWhoisModifyVo.getsAftOrgName());			// 한글기관명(변경후)
				map.put("AFT_ORG_ADDR", tbWhoisModifyVo.getsAftOrgAddr());			// 한글주소(변경후)
				map.put("AFT_ORG_ADDR_DTL", tbWhoisModifyVo.getsAftOrgAddrDetail());			// 한글주소(변경후)
				map.put("AFT_ZIPCODE", tbWhoisModifyVo.getsAftZipCode());			// 우편번호(변경후)
				map.put("AFT_ORG_ENAME", tbWhoisModifyVo.getsAftEOrgName());			// 영문기관명(변경후)
				map.put("AFT_ORG_EADDR", tbWhoisModifyVo.getsAftEOrgAddr());			// 영문주소(변경후)
				map.put("AFT_ORG_EADDR_DTL", tbWhoisModifyVo.getsAftEOrgAddrDetail());			// 영문주소(변경후)
				
				map.put("TITLE", subject);
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
				
				
				
			} else if(mailType.equals("Whois-Req-Info-Admin")) {					// 변경 신청시
				// 담당자에게 발송할 메일
				subject = "[접수알림] IPMS Whois 정보 변경 신청 접수 알림";
				
				map.put("USER_NAME", userName);			// 요청자 이름
				map.put("USER_ORG", userOrg);				// 요청자 소속
				map.put("USER_EMAIL", userEmail);			// 요청자 이메일
				map.put("FIRST_IP", tbWhoisModifyVo.getSfirstAddr());			// 시작 IP
				map.put("LAST_IP", tbWhoisModifyVo.getSlastAddr());			// 끝 IP
				map.put("BEF_ORG_NAME", tbWhoisModifyVo.getsBefOrgName());			// 한글기관명(변경전)
				map.put("BEF_ORG_ADDR", tbWhoisModifyVo.getsBefOrgAddr());			// 한글주소(변경전)
				map.put("BEF_ZIPCODE", tbWhoisModifyVo.getsBefZipCode());			// 우편번호(변경전)
				map.put("BEF_ORG_ENAME", tbWhoisModifyVo.getsBefEOrgName());			// 영문기관명(변경전)
				map.put("BEF_ORG_EADDR", tbWhoisModifyVo.getsBefEOrgAddr());			// 영문주소(변경전)
				map.put("AFT_ORG_NAME", tbWhoisModifyVo.getsAftOrgName());			// 한글기관명(변경후)
				map.put("AFT_ORG_ADDR", tbWhoisModifyVo.getsAftOrgAddr());			// 한글주소(변경후)
				map.put("AFT_ORG_ADDR_DTL", tbWhoisModifyVo.getsAftOrgAddrDetail());			// 한글주소(변경후)
				map.put("AFT_ZIPCODE", tbWhoisModifyVo.getsAftZipCode());			// 우편번호(변경후)
				map.put("AFT_ORG_ENAME", tbWhoisModifyVo.getsAftEOrgName());			// 영문기관명(변경후)
				map.put("AFT_ORG_EADDR", tbWhoisModifyVo.getsAftEOrgAddr());			// 영문주소(변경후)
				map.put("AFT_ORG_EADDR_DTL", tbWhoisModifyVo.getsAftEOrgAddrDetail());			// 영문주소(변경후)
				
				map.put("TITLE", subject);
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
				
			} else if(mailType.equals("Whois-Aprv-Info") && tbWhoisModifyVo.getsStatCd().equals("20")) { 		// 승인 시
				// 요청자에게 발송할 메일
				subject = "[완료알림] IPMS Whois 정보 변경 처리 완료 알림";
				
				map.put("FIRST_IP", tbWhoisModifyVo.getSfirstAddr());			// 시작 IP
				map.put("LAST_IP", tbWhoisModifyVo.getSlastAddr());			// 끝 IP
				map.put("BEF_ORG_NAME", tbWhoisModifyVo.getsBefOrgName());			// 한글기관명(변경전)
				map.put("BEF_ORG_ADDR", tbWhoisModifyVo.getsBefOrgAddr());			// 한글주소(변경전)
				map.put("BEF_ZIPCODE", tbWhoisModifyVo.getsBefZipCode());			// 우편번호(변경전)
				map.put("BEF_ORG_ENAME", tbWhoisModifyVo.getsBefEOrgName());			// 영문기관명(변경전)
				map.put("BEF_ORG_EADDR", tbWhoisModifyVo.getsBefEOrgAddr());			// 영문주소(변경전)
				map.put("AFT_ORG_NAME", tbWhoisModifyVo.getsAftOrgName());			// 한글기관명(변경후)
				map.put("AFT_ORG_ADDR", tbWhoisModifyVo.getsAftOrgAddr());			// 한글주소(변경후)
				map.put("AFT_ORG_ADDR_DTL", tbWhoisModifyVo.getsAftOrgAddrDetail());			// 한글주소(변경후)
				map.put("AFT_ZIPCODE", tbWhoisModifyVo.getsAftZipCode());			// 우편번호(변경후)
				map.put("AFT_ORG_ENAME", tbWhoisModifyVo.getsAftEOrgName());			// 영문기관명(변경후)
				map.put("AFT_ORG_EADDR", tbWhoisModifyVo.getsAftEOrgAddr());			// 영문주소(변경후)
				map.put("AFT_ORG_EADDR_DTL", tbWhoisModifyVo.getsAftEOrgAddrDetail());			// 영문주소(변경후)
				
				map.put("TITLE", subject);
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
				
				
			} else if(mailType.equals("Whois-Reject-Info") && tbWhoisModifyVo.getsStatCd().equals("30")) { 		// 반려 시
				// 요청자에게 발송할 메일
				subject = "[완료알림] IPMS Whois 정보 변경 요청 반려 처리 알림";
				
				map.put("FIRST_IP", tbWhoisModifyVo.getSfirstAddr());			// 시작 IP
				map.put("LAST_IP", tbWhoisModifyVo.getSlastAddr());			// 끝 IP
				map.put("BEF_ORG_NAME", tbWhoisModifyVo.getsBefOrgName());			// 한글기관명(변경전)
				map.put("BEF_ORG_ADDR", tbWhoisModifyVo.getsBefOrgAddr());			// 한글주소(변경전)
				map.put("BEF_ZIPCODE", tbWhoisModifyVo.getsBefZipCode());			// 우편번호(변경전)
				map.put("BEF_ORG_ENAME", tbWhoisModifyVo.getsBefEOrgName());			// 영문기관명(변경전)
				map.put("BEF_ORG_EADDR", tbWhoisModifyVo.getsBefEOrgAddr());			// 영문주소(변경전)
				map.put("AFT_ORG_NAME", tbWhoisModifyVo.getsAftOrgName());			// 한글기관명(변경후)
				map.put("AFT_ORG_ADDR", tbWhoisModifyVo.getsAftOrgAddr());			// 한글주소(변경후)
				map.put("AFT_ORG_ADDR_DTL", tbWhoisModifyVo.getsAftOrgAddrDetail());			// 한글주소(변경후)
				map.put("AFT_ZIPCODE", tbWhoisModifyVo.getsAftZipCode());			// 우편번호(변경후)
				map.put("AFT_ORG_ENAME", tbWhoisModifyVo.getsAftEOrgName());			// 영문기관명(변경후)
				map.put("AFT_ORG_EADDR", tbWhoisModifyVo.getsAftEOrgAddr());			// 영문주소(변경후)
				map.put("AFT_ORG_EADDR_DTL", tbWhoisModifyVo.getsAftEOrgAddrDetail());			// 영문주소(변경후)
				map.put("REJECT_RSN", tbWhoisModifyVo.getSreject_rsn());			// 반려사유
				
				map.put("TITLE", subject);
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
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
		
		return result;
	}
	
	/**
	 * Whois 정보공개관리 엑셀 다운로드
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/opermgmt/whoismgmt/viewListWhoisExcel.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> viewListWhoisExcel(@RequestBody TbWhoisVo searchVo, HttpServletRequest request, HttpServletResponse response){
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
		
			/** 계위 Seq 목록 조회 **/
			TbLvlMstVo searchSeqVo = new TbLvlMstVo();
			searchSeqVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
			searchSeqVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
			searchSeqVo.setSsvcObjCd(searchVo.getSsvcObjCd());
			TbLvlMstListVo resultSeqList = jwtUtil.getLvlSeqList(request, searchSeqVo);
			searchVo.setLvlMstSeqListVo(resultSeqList);
			
			setPagination(searchVo);
			
			TbWhoisVo  resultListVo = whoisService.viewListWhoisExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			
			mappingList.add("시작IP|getSfirstAddr");
			mappingList.add("마지막 IP|getSlastAddr");
			mappingList.add("노드|getNodeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("기관명|getSorgname");
			mappingList.add("네트워크이름|getSnetNm");
			mappingList.add("작업종류|getSwhoisRequestTypeNm");
			mappingList.add("변경일시|getSmodifyDt");
			mappingList.add("등록현황|getSwhoisTranStatusNm");
			mappingList.add("입력구분|getStransKind");
			
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbWhoisVos(), mappingList, request);
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
	 * whois kisa 정보 조회 및 관리
	 * @param tbWhoisVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/viewSearchKisa.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewSearchKisa(@RequestBody TbWhoisVo searchVo, ModelMap model, HttpServletRequest request) {
		TbWhoisComplexVo resultVo = whoisService.selectListTbWhoisControl(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewSearchKisa.ajax", method = RequestMethod.POST)
	public String viewSearchKisa(@RequestBody TbWhoisVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbWhoisVo searchVoClone = new TbWhoisVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchKisaModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewSearchKisa.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewSearchKisa";
	}
	private ModelMap viewSearchKisaModel(@RequestBody TbWhoisVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbWhoisComplexVo resultVo = null;
		TbWhoisUserVo ktInfoVo = null;
		model.addAttribute("searchVo", searchVo);

		TbWhoisVo kisaIpVo = new TbWhoisVo();
		TbWhoisUserVo kisaUserVo = new TbWhoisUserVo();
		try{
			setPagination(searchVo);
			resultVo = whoisService.selectListTbWhoisControl(searchVo);
			searchVo.setSsearchIp(resultVo.getTbWhoisVo().getSfirstAddr());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			
			// kisa ip search
			if(!searchVo.getSsearchIp().isEmpty()) {
			
				String firstAddr = resultVo.getTbWhoisVo().getSfirstAddr();
				String lastAddr = resultVo.getTbWhoisVo().getSlastAddr();
				
				String newClTrid = resultVo.getTbWhoisVo().getSwhoisrequestid() +  "-" + simpleDateFormat.format(new Date());
				
				WhoisInfoObj obj = whoisAdpaterService.whoisIpInfo(firstAddr, lastAddr, newClTrid);
				
				String rtnMsg = obj.getRtnMsg();
				
				if(StringUtil.isNullorBlank(obj.getRtnMsg())) {
					kisaUserVo.setSorgname(obj.getKoOrgName());
					kisaUserVo.setSaddr(obj.getKoOrgAddr());
					kisaUserVo.setSzipcode(obj.getOrgPost());
					kisaUserVo.setSeorgname(obj.getEnOrgName());
					kisaUserVo.setSeaddr(obj.getEnOrgAddr());
					kisaUserVo.setSadmDpphone(obj.getOrgPhone());
					kisaUserVo.setSadmEmail(obj.getOrgEmail());
					
					kisaIpVo.setSfirstAddr(obj.getStartIP());
					kisaIpVo.setSlastAddr(obj.getEndIP());
					kisaIpVo.setSnetNm(obj.getNetName());
					kisaIpVo.setSnettype(obj.getNetType());
					kisaIpVo.setSservicegb(obj.getSvcType());
					kisaIpVo.setSiptype(obj.getIpType());
					kisaIpVo.setSuserorggb(obj.getOrgType());
					kisaIpVo.setSsvcloc(obj.getSvcLoc());
					
				} else {
						kisaIpVo.setCommonMsg(obj.getRtnMsg());
				}
			}
			
			ktInfoVo = new TbWhoisUserVo();
			ktInfoVo.setSsaid("00000000000");
			ktInfoVo = whoisService.selectWhoisUser(ktInfoVo);
			
		} catch(ServiceException e){
			resultVo = new TbWhoisComplexVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			resultVo = new TbWhoisComplexVo();
			ktInfoVo = new TbWhoisUserVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		model.addAttribute("userVo", resultVo.getTbWhoisUserVo());
		model.addAttribute("resultVo", resultVo.getTbWhoisVo());
		model.addAttribute("ktInfoVo", ktInfoVo);
		model.addAttribute("kisaUserVo", kisaUserVo);
		model.addAttribute("kisaIpVo", kisaIpVo);
		return model;
	}
	
	
	/**
	 * Whois 반송 건 조회
	 * @param searchVo
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/selectWhoisComplexNew.json", method=RequestMethod.POST)
	@ResponseBody
	public String selectWhoisComplexNew(@RequestBody TbWhoisVo searchVo) {
		String strCount = null;
		try {
			strCount = whoisService.selectWhoisComplexNew(searchVo);
		} catch(ServiceException e) {
			strCount = null;
		} catch(Exception e) {
			strCount = null;
		}
		return strCount;
	}
	
	/**
	 *  Whois Kisa ip 해지
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/whoismgmt/deleteKisaIp.json", method = RequestMethod.POST)
	public com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo deleteKisaIp(@RequestBody TbWhoisVo deleteVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo resultVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
		String commonMsg;
		try {
			
			resultVo = whoisService.deleteKisaIp(deleteVo);
			
		} catch (ServiceException e) {
			resultVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
	/**
	 * DB 현행화 전송 건 목록 화면 로드 & 조회
	 * @param searchVo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisDbMatchMst.model", method=RequestMethod.POST)
	public ModelMap viewListWhoisDbMatchMst(@RequestBody TbWhoisVo searchVo) {
		TbWhoisListVo resultListVo = whoisService.selectListDbMatch(searchVo);
		return createResultList(resultListVo.getTbWhoisVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value="opermgmt/whoismgmt/viewListWhoisDbMatchMst.ajax", method=RequestMethod.POST)
	public String viewListWhoisDbMatchMst(@RequestBody TbWhoisVo searchVo, ModelMap model) {
		TbWhoisVo searchVoClone = new TbWhoisVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListWhoisDbMatchMstModel(searchVo);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/whoismgmt/viewListWhoisDbMatchMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/whoismgmt/viewListWhoisDbMatchMst";
	}
	private ModelMap viewListWhoisDbMatchMstModel(@RequestBody TbWhoisVo searchVo) {
		ModelMap model = new ModelMap();
		TbWhoisListVo resultListVo = null;
		List<CommonCodeVo> sassignTypeCdList = null;
		List<CommonCodeVo> listReqTypeCd = null;
		
		String sloadFlg = "T"; //메뉴 호출 시 조회를 처리하지 않기 위한 예외값 로직 추가(2014.12.24 전필권 과장님 요청)
		
		try{
			
			
			/** AssignTypeCode List**/
			sassignTypeCdList = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, new HashMap<String,String>());
			listReqTypeCd = whoisService.selectListVTbWhoisReqTypeCd();
			
			setPagination(searchVo);
		
			if("N".equals(searchVo.getLoadSearchYn())) {
				sloadFlg = "F";
			}
			
			
			if (sloadFlg.equals("T")){
				resultListVo = new TbWhoisListVo();
				resultListVo.setTotalCount(0);
			}else{
				resultListVo = whoisService.selectListDbMatch(searchVo);
			}
	
			
		} catch(ServiceException e){
			e.printStackTrace();
			resultListVo = new TbWhoisListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(commonMsg);
		} catch(Exception e) {
			e.printStackTrace();
			resultListVo = new TbWhoisListVo();
			String commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(commonMsg);
		}
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("sassignTypeCdList", sassignTypeCdList);
		model.addAttribute("listReqTypeCd", listReqTypeCd);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultListVo", resultListVo);
		model.addAttribute("searchVo", searchVo);
		return model;
	}
	
	
}
