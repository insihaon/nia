package com.kt.ipms.legacy.opermgmt.boardmgmt.web;

import java.lang.reflect.InvocationTargetException;
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
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.boardmgmt.service.FaqService;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class FaqController extends CommonController{

	@Autowired
	private FaqService faqService;
	
	/**
	 * FAQ 리스트 목록
	 * @param searchVo
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListFaq.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectListFaq(@RequestBody TbBoardVo searchVo, ModelMap model, HttpServletRequest request) {
		TbBoardListVo resultListVo = faqService.selectListFaq(searchVo);
		return createResultList(resultListVo.getTbBoardVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListFaq.do", method = RequestMethod.POST)
	public String selectListFaq(@ModelAttribute("searchVo") TbBoardVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbBoardVo searchVoClone = new TbBoardVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListFaqModel(searchVo, request);
		model.addAllAttributes(builtModel);
		
		searchVoClone.setUrl("/opermgmt/boardmgmt/viewListFaq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewListFaq";
	}
	private ModelMap selectListFaqModel(@ModelAttribute("searchVo") TbBoardVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardListVo resultListVo = null;
		String adminYn = null;
		try{
			searchVo.setSboardTypeCd("BH0003");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, searchVo);
			
			// 2015. 5. 18 모의해킹 관련 반영
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
			setPagination(searchVo);
			resultListVo = faqService.selectListFaq(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultListVo = new TbBoardListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new TbBoardListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		}
		
		model.addAttribute("resultListVo", resultListVo);
		PaginationInfo paginationInfo = getPaginationInfo();
		paginationInfo.setTotalRecordCount(resultListVo.getTotalCount());
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("adminYn", adminYn);	
		return model;
	}
	
	
	/**
	 * FAQ 상세정보 화면
	 * @param tbBoardVo
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailFaq.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap selectFaq(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request)  {
		TbBoardVo resultVo = faqService.selectFaq(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailFaq.ajax", method = RequestMethod.POST)
	public String selectFaq(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo searchVoClone = new TbBoardVo();
		try {
			CloneUtil.copyObjectInformation(tbBoardVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectFaqModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewDetailFaq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewDetailFaqPop";
	}
	private ModelMap selectFaqModel(@RequestBody TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		String adminYn = null;
		try {
			faqService.updateNreadCnt(tbBoardVo);
			resultVo = faqService.selectFaq(tbBoardVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
			// 2015. 5. 18 모의해킹 관련 반영
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			
		} catch (ServiceException e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("adminYn", adminYn);		
		return model;
	}
	
	/**
	 * FAQ 등록 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertFaq.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewInsertFaq(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request)  {
		tbBoardVo.setSboardTypeCd("BH0003");
		List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
		return createResultList(boardTypeSubCds, boardTypeSubCds.size());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertFaq.ajax", method = RequestMethod.POST)
	public String viewInsertFaq(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo searchVoClone = new TbBoardVo();
		try {
			CloneUtil.copyObjectInformation(tbBoardVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewInsertFaqModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewInsertFaq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewInsertFaqPop";
	}
	private ModelMap viewInsertFaqModel(TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try {
			tbBoardVo.setSboardTypeCd("BH0003");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			TbBoardVo resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	
	
	/**
	 * FAQ 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/insertFaq.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo insertFaq(@RequestBody TbBoardVo insertVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = null;
		try {
			
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
			
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){				
				String userId = jwtUtil.getUserId(request);
				insertVo.setSmodifyId(userId);
				insertVo.setScreateId(userId);
				
				resultVo = faqService.insertFaq(insertVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo = new TbBoardVo();
				resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
			}	

		} catch (ServiceException e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * FAQ 수정 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateFaq.model", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public ModelMap viewUpdateFaq(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request)  {
		TbBoardVo resultVo = faqService.selectFaq(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateFaq.ajax", method = RequestMethod.POST)
	public String viewUpdateFaq(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo searchVoClone = new TbBoardVo();
		try {
			CloneUtil.copyObjectInformation(tbBoardVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = viewUpdateFaqModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewUpdateFaq.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewUpdateFaqPop";
	}
	private ModelMap viewUpdateFaqModel(@RequestBody TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		try{
			tbBoardVo.setSboardTypeCd("BH0003");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
			resultVo = faqService.selectFaq(tbBoardVo);
			model.addAttribute("resultVo", resultVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	
	
	/**
	 * FAQ 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/updateFaq.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo updateFaq(@RequestBody TbBoardVo updateVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = new TbBoardVo();
		try{	
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
			
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){				
				faqService.updateFaq(updateVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo = new TbBoardVo();
				resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
			}	

		} catch (ServiceException e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * FAQ 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/deleteFaq.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo deleteFaq(@RequestBody TbBoardVo deleteVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = new TbBoardVo();
		try {	
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
			
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){		
				faqService.deleteFaq(deleteVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo = new TbBoardVo();
				resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
			}		

		} catch (ServiceException e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	
	
}
