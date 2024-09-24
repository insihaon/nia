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
import com.kt.ipms.legacy.opermgmt.boardmgmt.service.BoardService;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardReplyListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardReplyVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class BoardController extends CommonController{
	
	@Autowired
	private BoardService boardService;
	
	/**
	 * 게시판 리스트 목록
	 * @param searchVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListBoard.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListBoard(@RequestBody TbBoardVo searchVo, ModelMap model, 
			HttpServletRequest request) {
		TbBoardListVo resultListVo =boardService.selectListBoard(searchVo);
		return createResultList(resultListVo.getTbBoardVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewListBoard.do", method = RequestMethod.POST)
	public String selectListBoard(@ModelAttribute("searchVo") TbBoardVo searchVo, ModelMap model, 
			HttpServletRequest request,HttpServletResponse response) {
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
		ModelMap builtModel = selectListBoardModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewListBoard.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewListBoard";
	}
	private ModelMap selectListBoardModel(@ModelAttribute("searchVo") TbBoardVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardListVo resultListVo = null;
		try{
			searchVo.setSboardTypeCd("BH0002");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, searchVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
			setPagination(searchVo);
			resultListVo = boardService.selectListBoard(searchVo);
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
		return model;
	}
	
	
	/**
	 * 게시판 상세정보 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailBoard.model", method = RequestMethod.POST)
	public ModelMap selectBoard(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request) {
		TbBoardVo resultVo = boardService.selectBoard(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewDetailBoard.ajax", method = RequestMethod.POST)
	public String selectBoard(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = selectBoardModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewDetailBoard.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewDetailBoardPop";
	}
	private ModelMap selectBoardModel(@RequestBody TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		String adminYn = "N";
		try{
			resultVo = boardService.selectBoard(tbBoardVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			
			
			// 2015. 5. 18 모의해킹 관련 반영
			String usrGradeCd = jwtUtil.getUserGradeCd(request);
			if(usrGradeCd != null && usrGradeCd.equals("UR0001")){
				adminYn = "Y";
			}
			else{
				if(resultVo != null){
					String txtcreateId = resultVo.getScreateId();
					String userId = jwtUtil.getUserId(request);
					
					if(txtcreateId.equals(userId)){
						adminYn = "Y";
					}else{
						adminYn = "N";
					}
				}
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
		model.addAttribute("resultVo", resultVo);
		model.addAttribute("adminYn", adminYn);
		return model;
	}
	
	
	/**
	 * 게시판 등록 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertBoard.model", method = RequestMethod.POST)
	public ModelMap viewInsertBoard(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request) {
		tbBoardVo.setSboardTypeCd("BH0002");
		List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, tbBoardVo);
		return createResultList(boardTypeSubCds, boardTypeSubCds.size());
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewInsertBoard.ajax", method = RequestMethod.POST)
	public String viewInsertBoard(TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewInsertBoardModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewInsertBoard.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewInsertBoardPop";
	}
	private ModelMap viewInsertBoardModel(TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try{
			tbBoardVo.setSboardTypeCd("BH0002");
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
	 * 게시판 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/insertBoard.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo insertBoard(@RequestBody TbBoardVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbBoardVo resultVo = null;
		
		//모의해킹괄련 적용 2015. 05 .19
		String userId = jwtUtil.getUserId(request);
		insertVo.setSmodifyId(userId);
		insertVo.setScreateId(userId);
		
		try{
			int count = boardService.insertBoard(insertVo);
			if(count == 1) {
				resultVo = new TbBoardVo();
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
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
	 * 게시판 수정 화면
	 * @param tbBoardVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateBoard.model", method = RequestMethod.POST)
	public ModelMap viewUpdateBoard(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request) {
		TbBoardVo resultVo = boardService.selectBoard(tbBoardVo);
		return createResult(resultVo);
	}
	@RequestMapping(value = "/opermgmt/boardmgmt/viewUpdateBoard.ajax", method = RequestMethod.POST)
	public String viewUpdateBoard(@RequestBody TbBoardVo tbBoardVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
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
		ModelMap builtModel = viewUpdateBoardModel(tbBoardVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/opermgmt/boardmgmt/viewUpdateBoard.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "opermgmt/boardmgmt/viewUpdateBoardPop";
	}
	private ModelMap viewUpdateBoardModel(@RequestBody TbBoardVo tbBoardVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		TbBoardVo resultVo = null;
		try{
			resultVo =boardService.selectBoard(tbBoardVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			tbBoardVo.setSboardTypeCd("BH0002");
			List<CommonCodeVo> boardTypeSubCds = commonCodeService.selectListCommonCode(CommonCodeUtil.BOARD_TYPE_SUB_CD, resultVo);
			model.addAttribute("boardTypeSubCds", boardTypeSubCds);
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
	@RequestMapping(value = "/opermgmt/boardmgmt/updateBoard.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo updateBoard(@RequestBody TbBoardVo updateVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = new TbBoardVo();
		try{
			boardService.updateBoard(updateVo);
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
		return resultVo;
	}
	
	/**
	 * 게시판 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/deleteBoard.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardVo deleteBoard(@RequestBody TbBoardVo deleteVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardVo resultVo = new TbBoardVo();
		try {
			boardService.deleteBoard(deleteVo);
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
		return resultVo;
	}
	
	/**
	 * 게시판 댓글 리스트 목록
	 * @param tbBoardReplyVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/selectListBoardReply.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardReplyListVo selectListBoardReply(@RequestBody TbBoardReplyVo tbBoardReplyVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		TbBoardReplyListVo resultListRePlyVo = null;
		try{
			resultListRePlyVo = boardService.selectListBoardReply(tbBoardReplyVo);
			resultListRePlyVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultListRePlyVo = new TbBoardReplyListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListRePlyVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultListRePlyVo = new TbBoardReplyListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultListRePlyVo.setCommonMsg(msgDesc);
		}
		
		return resultListRePlyVo;
	}
	
	/**
	 * 게시판 댓글 등록
	 * @param insertVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/insertBoardReply.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardReplyVo insertBoardReply(@RequestBody TbBoardReplyVo insertVo, HttpServletRequest request, HttpServletResponse response) {
		TbBoardReplyVo resultVo = new TbBoardReplyVo();
		try{
			resultVo = boardService.insertBoardReply(insertVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		
		return resultVo;
	}
	
	/**
	 * 게시판 댓글 수정
	 * @param updateVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/updateBoardReply.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardReplyVo updateBoardReply(@RequestBody TbBoardReplyVo updateVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardReplyVo resultVo = new TbBoardReplyVo();
		try{
			resultVo = boardService.updateBoardReply(updateVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e){
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
	
	/**
	 * 게시판 댓글 삭제
	 * @param deleteVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @
	 */
	@RequestMapping(value = "/opermgmt/boardmgmt/deleteBoardReply.json", method = RequestMethod.POST)
	@ResponseBody
	public TbBoardReplyVo deleteBoardReply(@RequestBody TbBoardReplyVo deleteVo, HttpServletRequest request, HttpServletResponse response)  {
		TbBoardReplyVo resultVo = new TbBoardReplyVo();
		try {
			resultVo = boardService.deleteBoardReply(deleteVo);
			resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
		} catch (ServiceException e) {
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new TbBoardReplyVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
}
