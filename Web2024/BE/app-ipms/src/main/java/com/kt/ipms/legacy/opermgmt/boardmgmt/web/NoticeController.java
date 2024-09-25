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

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.boardmgmt.service.NoticeService;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class NoticeController extends CommonController {

	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 공지사항 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListNotice.model", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ModelMap selectListTbBoard(@RequestBody TbBoardVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbBoardListVo resultListVo = noticeService.selectListNotice(searchVo);
		return createResultList(resultListVo.getTbBoardVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListNotice.do", method = RequestMethod.POST)
	public String selectListTbBoard(@ModelAttribute("searchVo") TbBoardVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = selectListTbBoardModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewListNotice.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewListNotice";
	}
	private ModelMap selectListTbBoardModel(@ModelAttribute("searchVo") TbBoardVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardListVo resultListVo = null;
		String adminYn = null;
		try {
			searchVo.setSboardTypeCd("BH0001");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, searchVo);
		
			// 2015. 5. 18 모의해킹 관련 반영
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
			setPagination(searchVo);
			resultListVo = noticeService.selectListNotice(searchVo);
			resultListVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
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
	 * 공지사항 상세정보 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailNotice.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectNotice(@RequestBody TbBoardVo tbBoardVo, ModelMap model,
			HttpServletRequest request)  {
		TbBoardVo resultVo = noticeService.selectNotice(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailNotice.ajax", method = RequestMethod.POST)
	public String selectNotice(@RequestBody TbBoardVo tbBoardVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = selectNoticeModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewDetailNotice.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewDetailNotice";
	}
	private ModelMap selectNoticeModel(@RequestBody TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		String adminYn = null;
		try {
			noticeService.updateNreadCnt(tbBoardVo);
			resultVo = noticeService.selectNotice(tbBoardVo);
			resultVo.setScrnType(tbBoardVo.getScrnType());
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			// 2015. 5. 18 모의해킹 관련 반영
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
				
				if(resultVo.getScrnType() != null && !resultVo.getScrnType().equals("") && resultVo.getScrnType().equals("MAIN")){
					adminYn = "N";
				}
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
	 * 공지사항 상세정보 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailEmergencyNotice.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewDetailEmergencyNotice(@RequestBody TbBoardVo searchVo, ModelMap model,
			HttpServletRequest request)  {
		TbBoardVo resultVo = noticeService.selectNotice(searchVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailEmergencyNotice.ajax", method = RequestMethod.POST)
	public String viewDetailEmergencyNotice(@ModelAttribute("searchVo") TbBoardVo searchVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = viewDetailEmergencyNoticeModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewDetailEmergencyNotice.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewDetailEmergencyNotice";
	}
	private ModelMap viewDetailEmergencyNoticeModel(@ModelAttribute("searchVo") TbBoardVo searchVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		try {
			noticeService.updateNreadCnt(searchVo);
			resultVo = noticeService.selectNotice(searchVo);
			resultVo.setScrnType(searchVo.getScrnType());
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
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 공지사항 등록 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertNotice.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewInsertNotice(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request)  {
		tbBoardVo.setSboardTypeCd("BH0001");
		List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
		return createResultList(boardTypeSubCds, boardTypeSubCds.size());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertNotice.ajax", method = RequestMethod.POST)
	public String viewInsertNotice(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = viewInsertNoticeModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewInsertNotice.model.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewInsertNotice";
	}
	private ModelMap viewInsertNoticeModel(TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try{
			tbBoardVo.setSboardTypeCd("BH0001");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
		} catch (ServiceException e)
		{
			throw e;
		} catch (Exception e) {
			TbBoardVo resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return model;
	}
	
	/**
	 * 게시판 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/insertNotice.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo insertNotice(@RequestBody TbBoardVo insertVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = null;
		try {
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
			
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){		
				String userId = jwtUtil.getUserId(request);
				insertVo.setSmodifyId(userId);
				insertVo.setScreateId(userId);
				
				resultVo = noticeService.insertNotice(insertVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo = new TbBoardVo();
				resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
			}		

		} catch (ServiceException e){
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
	 * 공지사항 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/deleteNotice.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo deleteNotice(@RequestBody TbBoardVo deleteVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = null;
		try {
			
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
		
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){
				resultVo = noticeService.deleteNotice(deleteVo);
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
	 * 공지사항 수정 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateNotice.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap viewUpdateNotice(@RequestBody TbBoardVo tbBoardVo, ModelMap model,
			HttpServletRequest request)  {
		TbBoardVo resultVo = noticeService.selectNotice(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateNotice.ajax", method = RequestMethod.POST)
	public String viewUpdateNotice(@RequestBody TbBoardVo tbBoardVo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = viewUpdateNoticeModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewUpdateNotice.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewUpdateNotice";
	}
	private ModelMap viewUpdateNoticeModel(@RequestBody TbBoardVo tbBoardVo,
	HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		try {
			
			//모의해킹괄련 적용 2015. 05 .19
			resultVo = noticeService.selectNotice(tbBoardVo);
			tbBoardVo.setSboardTypeCd("BH0001");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		
		} catch (ServiceException e){
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultVo", resultVo);
		return model;
	}
	
	
	/**
	 * 게시판 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/updateNotice.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo updateNotice(@RequestBody TbBoardVo updateVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = new TbBoardVo();
		try{
			//모의해킹괄련 적용 2015. 05 .19
			String userCd = jwtUtil.getUserGradeCd(request);
		
			if(userCd.equals(CommonCodeUtil.USER_GRADE_A)){
				resultVo = noticeService.updateNotice(updateVo);
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}else{
				resultVo.setCommonMsg(CommonCodeUtil.FAIL_MSG);
			}
			
		} catch (ServiceException e){
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
