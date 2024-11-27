package com.kt.ipms.legacy.opermgmt.srvmgmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.kt.ipms.legacy.opermgmt.srvmgmt.service.SvcMgmtService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcLgroupCdVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class SvcMgmtController extends CommonController{
	
	@Autowired
	SvcMgmtService svcMgmtService;
	
	/* 상품 리스트 조회 메인화면*/
	@RequestMapping(value = "/opermgmt/srvmgmt/viewListIpmsSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListIpmsSvc(@RequestBody TbIpmsSvcMstVo searchVo, ModelMap model, HttpServletRequest request)  {
		TbIpmsSvcMstListVo resultListVo = svcMgmtService.selectListIpmsSvc(searchVo);
		return createResultList(resultListVo.getTbIpmsSvcMstVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/srvmgmt/viewListIpmsSvcMst.do", method = RequestMethod.POST)
	public String selectListIpmsSvc(@ModelAttribute("searchVo") TbIpmsSvcMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbIpmsSvcMstVo searchVoClone = new TbIpmsSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListIpmsSvcModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewListIpmsSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewListIpmsSvcMst";
	}
	private ModelMap selectListIpmsSvcModel(@ModelAttribute("searchVo") TbIpmsSvcMstVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpmsSvcMstListVo resultListVo = null;
		try{
			
			List<CommonCodeVo> svcHgroupCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_HGROUP_CD, null);
			List<CommonCodeVo> sexLinkUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SEX_LINK_USE_TYPE_CD, null);
			List<CommonCodeVo> sassignTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
			List<CommonCodeVo> svcUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_USE_TYPE_CD, null);
			List<CommonCodeVo> suseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.USE_TYPE_CD, null);
			TbIpmsSvcMstListVo ssvcMgroupCds = null;
			TbIpmsSvcMstListVo ssvcLgroupCds = null;
			if (StringUtils.hasText(searchVo.getSsvcHgroupCd())) {
				TbIpmsSvcMstVo searchMgroupVo = new TbIpmsSvcMstVo();
				searchMgroupVo.setSsvcHgroupCd(searchVo.getSsvcHgroupCd());
				ssvcMgroupCds = svcMgmtService.selectListSvcMgroupNm(searchMgroupVo);
				if (StringUtils.hasText(searchVo.getSsvcMainClsCode())) {
					TbIpmsSvcMstVo searchLgroupVo = new TbIpmsSvcMstVo();
					searchLgroupVo.setSsvcMainClsCode(searchVo.getSsvcMainClsCode());
					ssvcLgroupCds = svcMgmtService.selectListSvcMgroupNm1(searchLgroupVo);
				} else {
					ssvcLgroupCds = new TbIpmsSvcMstListVo();
				}
			} else {
				ssvcMgroupCds = new TbIpmsSvcMstListVo();
				ssvcLgroupCds = new TbIpmsSvcMstListVo();
			}
			model.addAttribute("ssvcMgroupCds", ssvcMgroupCds);
			model.addAttribute("ssvcLgroupCds", ssvcLgroupCds);
			model.addAttribute("svcHgroupCd", svcHgroupCd);
			model.addAttribute("sexLinkUseTypeCd", sexLinkUseTypeCd);
			model.addAttribute("sassignTypeCd", sassignTypeCd);
			model.addAttribute("svcUseTypeCd", svcUseTypeCd);
			model.addAttribute("suseTypeCd", suseTypeCd);
			setPagination(searchVo);
			resultListVo = svcMgmtService.selectListIpmsSvc(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbIpmsSvcMstListVo();
			resultListVo.setCommonMsg(e.getMessage());
			resultListVo.setTotalCount(0);
		}
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		return model;
	}
	
	
	/* 상품관리 리스트 엑셀 다운 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectListIpmsSvcMstExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> selectListIpmsSvcMstExcel(@RequestBody TbIpmsSvcMstVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		try {
			TbIpmsSvcMstListVo resultListVo = svcMgmtService.selectListIpmsSvcMstExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("SEQ|getNipmsSvcSeq");
			mappingList.add("구분|getSsvcHgroupNm");
			mappingList.add("대분류|getSsvcMgroupNm");
			mappingList.add("소분류|getSsvcMgroupNm1");
			mappingList.add("상품명|getSipmsSvcNm");
			mappingList.add("이용목적|getSsvcUseTypeNm");
			mappingList.add("서비스|getSassignTypeNm");
			mappingList.add("외부연계|getSexLinkUseTypeNm");
			mappingList.add("NeOSS상품코드|getSexSvcCd");
			mappingList.add("비고|getScomment");
			mappingList.add("수정일|getDmodifyDt");
			mappingList.add("사용여부|getSuseTypeNm");
			
			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbIpmsSvcMstVos(), mappingList, request);
		} catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	
	/* 대분류 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectSsvcMgroupNmList.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectSsvcMgroupNmList(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbIpmsSvcMstListVo resultListVo = null;
		try{
			resultListVo = svcMgmtService.selectListSvcMgroupNm(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		catch (Exception e) {
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
			
		}
		return resultListVo;
	}
	/* 소분류 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectSsvcMgroupNm1List.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectSsvcMgroupNm1List(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbIpmsSvcMstListVo resultListVo = null;
		try{
			resultListVo = svcMgmtService.selectListSvcMgroupNm1(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		catch (Exception e) {
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
			
		}
		return resultListVo;
	}
	/* 상품 등록 화면 */
	@RequestMapping(value = "/opermgmt/srvmgmt/viewInsertIpmsSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertIpmsSvcMst(TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request) {
		ModelMap resultModel = new ModelMap();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data", viewInsertIpmsSvcMstModel(tbIpmsSvcMstVo, request));
		resultModel.addAttribute("result",map);

		return resultModel;
	}
	@RequestMapping(value = "/opermgmt/srvmgmt/viewInsertIpmsSvcMst.ajax", method = RequestMethod.POST)
	public String viewInsertIpmsSvcMst(TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo searchVoClone = new TbIpmsSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpmsSvcMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertIpmsSvcMstModel(tbIpmsSvcMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewInsertIpmsSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewInsertIpmsSvcMst";
	}
	private ModelMap viewInsertIpmsSvcMstModel(TbIpmsSvcMstVo tbIpmsSvcMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		List<CommonCodeVo> svcHgroupCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_HGROUP_CD, null);
		List<CommonCodeVo> sexLinkUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SEX_LINK_USE_TYPE_CD, null);
		List<CommonCodeVo> sassignTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
		List<CommonCodeVo> svcUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_USE_TYPE_CD, null);
		model.addAttribute("svcHgroupCd", svcHgroupCd);
		model.addAttribute("sexLinkUseTypeCd", sexLinkUseTypeCd);
		model.addAttribute("sassignTypeCd", sassignTypeCd);
		model.addAttribute("svcUseTypeCd", svcUseTypeCd);
		return model;
	}
	
	/* 상품 중복 check */
	@RequestMapping(value = "/opermgmt/srvmgmt/checkIpmsSvcMst.json", method = RequestMethod.POST)
	public TbIpmsSvcMstVo checkIpmsSvcMst(@RequestBody TbIpmsSvcMstVo checkVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo resultVo = null;
		try{
			int checkCount = svcMgmtService.countListPageTbIpmsSvcMstVo(checkVo);
			resultVo = new TbIpmsSvcMstVo();
			resultVo.setCheckCount(checkCount);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}catch (ServiceException e){
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		}
		catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 상품 등록 */
	@RequestMapping(value = "/opermgmt/srvmgmt/insertIpmsSvcMst.json", method = RequestMethod.POST)
	public TbIpmsSvcMstVo insertIpmsSvcMst(@RequestBody TbIpmsSvcMstVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo resultVo = null;
		try {
			resultVo = svcMgmtService.insertTbIpmsSvcMstVo(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}  catch (ServiceException e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 상품 수정 */
	@RequestMapping(value = "/opermgmt/srvmgmt/updateIpmsSvcMst.json", method = RequestMethod.POST)
	public TbIpmsSvcMstVo updateIpmsSvcMst(@RequestBody TbIpmsSvcMstVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo resultVo = null;
		try {
			resultVo = svcMgmtService.updateTbIpmsSvcMstVo(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}  catch (ServiceException e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* new LgroupCd 조회 */
	@RequestMapping(value = "opermgmt/srvmgmt/selectNewLgroupCd.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectNewLgroupCd(HttpServletRequest request, HttpServletResponse response) {
		TbSvcLgroupCdVo resultVo = null;
		String newLgroupCd = null;
		try {
			newLgroupCd = svcMgmtService.selectNewLgroupCd();
			resultVo = new TbSvcLgroupCdVo();
			resultVo.setSsvcLgroupCd(newLgroupCd);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbSvcLgroupCdVo();
			newLgroupCd = null;
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbSvcLgroupCdVo();
			newLgroupCd = null;
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* Lgroup 등록 */
	@RequestMapping(value = "/opermgmt/srvmgmt/insertTbSvcLgroupCd.json", method = RequestMethod.POST)
	public TbSvcLgroupCdVo insertTbSvcLgroupCdVo(@RequestBody TbSvcLgroupCdVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbSvcLgroupCdVo resultVo = null;
		try {
			resultVo = svcMgmtService.insertTbSvcLgroupCdVo(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbSvcLgroupCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbSvcLgroupCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* Lgroup 수정 */
	@RequestMapping(value = "/opermgmt/srvmgmt/updateTbSvcLgroupCd.json", method = RequestMethod.POST)
	public TbSvcLgroupCdVo updateTbSvcLgroupCdVo(@RequestBody TbSvcLgroupCdVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbSvcLgroupCdVo resultVo = null;
		try {
			resultVo = svcMgmtService.updateTbSvcLgroupCdVo(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbSvcLgroupCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbSvcLgroupCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 서비스분류 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectListSvcMgroup.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectListSvcMgroup(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbIpmsSvcMstListVo resultListVo = null;
		try{
			resultListVo = svcMgmtService.selectListSvcMgroup(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		catch (Exception e) {
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		return resultListVo;
	}
	
	/* 상품명 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectListSvcLgroup.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo selectListSvcLgroup(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request, HttpServletResponse response)  {
		TbIpmsSvcMstListVo resultListVo = null;
		try{
			resultListVo = svcMgmtService.selectListSvcLgroup(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		catch (Exception e) {
			resultListVo = new TbIpmsSvcMstListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		return resultListVo;
	}
	/* 외부연계 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectSexLinkUseTypeCd.json", method = RequestMethod.POST)
	public TbIpmsSvcMstVo selectSexLinkUseTypeCd(@RequestBody TbIpmsSvcMstVo searchVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo resultVo = null;
		try{
			resultVo = svcMgmtService.selectSexLinkUseTypeCd(searchVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		}
		catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 상품 수정 화면 */
	@RequestMapping(value = "/opermgmt/srvmgmt/viewUpdateIpmsSvcMst.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateIpmsSvcMst(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request) {
		TbIpmsSvcMstVo resultVo = svcMgmtService.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/srvmgmt/viewUpdateIpmsSvcMst.ajax", method = RequestMethod.POST)
	public String viewUpdateIpmsSvcMst(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo searchVoClone = new TbIpmsSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpmsSvcMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateIpmsSvcMstModel(tbIpmsSvcMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewUpdateIpmsSvcMst.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewUpdateIpmsSvcMst";
	}
	private ModelMap viewUpdateIpmsSvcMstModel(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();

		TbIpmsSvcMstVo resultVo = null;
		try {
			List<CommonCodeVo> sassignTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.ASSIGN_TYPE_CD, null);
			List<CommonCodeVo> svcUseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_USE_TYPE_CD, null);
			List<CommonCodeVo> suseTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.USE_TYPE_CD, null);
			model.addAttribute("sassignTypeCd", sassignTypeCd);
			model.addAttribute("svcUseTypeCd", svcUseTypeCd);
			model.addAttribute("suseTypeCd", suseTypeCd);
			resultVo = svcMgmtService.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	
	/* 서비스망 관리 화면(popup) */
	@RequestMapping(value = "/opermgmt/srvmgmt/viewSearchSvcLine.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewSearchSvcLine(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request) {
		TbIpmsSvcMstVo resultVo = svcMgmtService.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/srvmgmt/viewSearchSvcLine.ajax", method = RequestMethod.POST)
	public String viewSearchSvcLine(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcMstVo searchVoClone = new TbIpmsSvcMstVo();
		try {
			CloneUtil.copyObjectInformation(tbIpmsSvcMstVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewSearchSvcLineModel(tbIpmsSvcMstVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewSearchSvcLine.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewSearchSvcLine";
	}
	private ModelMap viewSearchSvcLineModel(@RequestBody TbIpmsSvcMstVo tbIpmsSvcMstVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbIpmsSvcMstVo resultVo = null;
		try{
			resultVo = svcMgmtService.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
			List<CommonCodeVo> svcLineTypeCd = commonCodeService.selectListCommonCode(CommonCodeUtil.SVC_LINE_TYPE_CD, null);
			model.addAttribute("svcLineTypeCd", svcLineTypeCd);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcMstVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
			
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/* 상품 서비스망 리스트 조회 */
	@RequestMapping(value = "/opermgmt/srvmgmt/selectListTbIpmsSvcSub.json", method = RequestMethod.POST)
	public TbIpmsSvcSubListVo selectListTbIpmsSvcSub(@RequestBody TbIpmsSvcSubVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcSubListVo resultListVo = null;
		try {
			resultListVo = svcMgmtService.selectListTbIpmsSvcSubVo(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListVo = new TbIpmsSvcSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListVo = new TbIpmsSvcSubListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
		}
		return resultListVo;
	}
	/* 상품 서비스망 등록 */
	@RequestMapping(value = "/opermgmt/srvmgmt/insertTbIpmsSvcSub.json", method = RequestMethod.POST)
	public TbIpmsSvcSubVo insertTbIpmsSvcSub(@RequestBody TbIpmsSvcSubVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcSubVo resultVo = null;
		try {
			resultVo = svcMgmtService.insertTbIpmsSvcSubVo(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}  catch (ServiceException e) {
			resultVo = new TbIpmsSvcSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	/* 상품 서비스망 삭제 */
	@RequestMapping(value = "/opermgmt/srvmgmt/deleteTbIpmsSvcSub.json", method = RequestMethod.POST)
	public TbIpmsSvcSubVo deleteTbIpmsSvcSub(@RequestBody TbIpmsSvcSubVo deleteVo, HttpServletRequest request, HttpServletResponse response) {
		TbIpmsSvcSubVo resultVo = null;
		try {
			resultVo = svcMgmtService.deleteTbIpmsSvcSubVo(deleteVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		}  catch (ServiceException e) {
			resultVo = new TbIpmsSvcSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbIpmsSvcSubVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 서비스 코드 리스트 메인 화면 */
	@RequestMapping(value = "/opermgmt/srvmgmt/viewListAssignTypeCd.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewListAssignTypeCd(@RequestBody TbAssignTypeCdVo searchVo, ModelMap model, HttpServletRequest request) {
		TbAssignTypeCdListVo resultListVo = svcMgmtService.selectListTbAssignTypeCd(searchVo);
		return createResultList(resultListVo.getTbAssignTypeCdVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/srvmgmt/viewListAssignTypeCd.do", method = RequestMethod.POST)
	public String viewListAssignTypeCd(@ModelAttribute("searchVo") TbAssignTypeCdVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbAssignTypeCdVo searchVoClone = new TbAssignTypeCdVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewListAssignTypeCdModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewListAssignTypeCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewListAssignTypeCd";
	}
	private ModelMap viewListAssignTypeCdModel(@ModelAttribute("searchVo") TbAssignTypeCdVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbAssignTypeCdListVo resultListVo = null;
		try {
			setPagination(searchVo);
			resultListVo = svcMgmtService.selectListTbAssignTypeCd(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbAssignTypeCdListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbAssignTypeCdListVo();
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
	
	
	@RequestMapping(value = "/opermgmt/srvmgmt/viewListAssignTypeCdExcel.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ResponseEntity<?> viewListAssignTypeCdExcel(@RequestBody TbAssignTypeCdVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		FileVo resultVo = new FileVo();
		
		try {
			
			TbAssignTypeCdListVo resultListVo = svcMgmtService.selectListTbAssignTypeCdExcel(searchVo);
			List<String> mappingList = new ArrayList<String>();
			mappingList.add("코드|getSassignTypeCd");
			mappingList.add("서비스명|getSassignTypeNm");
			mappingList.add("NeOSS연동 여부|getSneossDdYn");
			mappingList.add("비고|getScomment");			

			return excelDownloadService.generateAndDownloadExcel(resultListVo.getTbAssignTypeCdVos(), mappingList, request);
		}catch (ServiceException e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}
	
	/* 서비스 코드 등록 화면  */
	@RequestMapping(value = "opermgmt/srvmgmt/viewInsertAssignTypeCd.ajax", method = RequestMethod.POST)
	public String viewInsertAssignTypeCd(TbAssignTypeCdVo tbAssignTypeCdVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "opermgmt/srvmgmt/viewInsertAssignTypeCd";
	}
	
	/* 서비스 코드 등록 */
	@RequestMapping(value = "opermgmt/srvmgmt/insertAssignTypeCd.json", method = RequestMethod.POST)
	public TbAssignTypeCdVo insertAssignTypeCd(@RequestBody TbAssignTypeCdVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbAssignTypeCdVo resultVo = null;
		try {
			resultVo = svcMgmtService.insertTbAssignTypeCdVo(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/* 서비스코드 수정 화면 */
	@RequestMapping(value = "opermgmt/srvmgmt/viewUpdateAssignTypeCd.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateAssignTypeCd(@RequestBody TbAssignTypeCdVo tbAssignTypeCdVo, ModelMap model, HttpServletRequest request) {
		TbAssignTypeCdVo resultVo = svcMgmtService.selectTbAssignTypeCdVo(tbAssignTypeCdVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "opermgmt/srvmgmt/viewUpdateAssignTypeCd.ajax", method = RequestMethod.POST)
	public String viewUpdateAssignTypeCd(@RequestBody TbAssignTypeCdVo tbAssignTypeCdVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbAssignTypeCdVo searchVoClone = new TbAssignTypeCdVo();
		try {
			CloneUtil.copyObjectInformation(tbAssignTypeCdVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateAssignTypeCdModel(tbAssignTypeCdVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/srvmgmt/viewUpdateAssignTypeCd.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/srvmgmt/viewUpdateAssignTypeCd";
	}
	private ModelMap viewUpdateAssignTypeCdModel(@RequestBody TbAssignTypeCdVo tbAssignTypeCdVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbAssignTypeCdVo resultVo = null;
		try {
			resultVo = svcMgmtService.selectTbAssignTypeCdVo(tbAssignTypeCdVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/* 서비스 코드 수정 */
	@RequestMapping(value = "opermgmt/srvmgmt/updateAssignTypeCd.json", method = RequestMethod.POST)
	public TbAssignTypeCdVo updateAssignTypeCd(@RequestBody TbAssignTypeCdVo updateVo, HttpServletRequest request, HttpServletResponse response) {
		TbAssignTypeCdVo resultVo = null;
		try {
			resultVo = svcMgmtService.updateTbAssignTypeCdVo(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbAssignTypeCdVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	

}
