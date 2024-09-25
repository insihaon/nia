package com.kt.ipms.legacy.linkmgmt.errormgmt.web;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.linkmgmt.errormgmt.service.ErrorMgmtService;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtListVo;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class ErrorMgmtController extends CommonController{
	
	@Autowired
	private ErrorMgmtService errorMgmtService;
	
	@RequestMapping(value = "/linkmgmt/erromgmt/viewListError.model", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectListReq( @ModelAttribute("searchVo") ErrorMgmtVo searchVo, ModelMap model, HttpServletRequest request){
		ErrorMgmtListVo resultListVo = errorMgmtService.selectListErrorMgmt(searchVo);
		return createResultList(resultListVo.getErrorMgmtVos(), resultListVo.getTotalCount());
	}
	@RequestMapping(value = "/linkmgmt/erromgmt/viewListError.do", method = RequestMethod.POST)
	public String selectListReq( @ModelAttribute("searchVo") ErrorMgmtVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		ErrorMgmtVo searchVoClone = new ErrorMgmtVo();
		try {
			CloneUtil.copyObjectInformation(searchVo, searchVoClone);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ModelMap builtModel = selectListReqModel(searchVo, request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/linkmgmt/erromgmt/viewListError.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "linkmgmt/errormgmt/viewListError";
	}
	private ModelMap selectListReqModel( @ModelAttribute("searchVo") ErrorMgmtVo searchVo, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		ErrorMgmtListVo resultListVo = null;
		try{
			if(searchVo.getSortOrdr().isEmpty()){
				searchVo.setSortOrdr("DESC");
			}
			if(searchVo.getSortType().isEmpty()){
				searchVo.setSortType("dcreate_dt");
			}
			setPagination(searchVo);
			resultListVo = errorMgmtService.selectListErrorMgmt(searchVo);
		} catch (ServiceException e){
			resultListVo = new ErrorMgmtListVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultListVo.setCommonMsg(msgDesc);
			resultListVo.setTotalCount(0);
		} catch (Exception e) {
			resultListVo = new ErrorMgmtListVo();
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
	
	
	@RequestMapping(value = "/linkmgmt/errormgmt/updateErrorMgmt.json", method = RequestMethod.POST)
	public ErrorMgmtVo updateErrorMgmt(HttpServletRequest request,@RequestBody ErrorMgmtListVo errorMgmtListVo){
		ErrorMgmtVo resultVo = new ErrorMgmtVo();
		try {
			errorMgmtListVo.setsModifyId(jwtUtil.getUserId(request));
			int resultcode = errorMgmtService.updateTbNeossErrorManage(errorMgmtListVo);
			System.out.println("resultcode == "+resultcode);
			if(resultcode != 0){
				System.out.println("hello");
				resultVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
			}
		} catch (ServiceException e) {
			resultVo = new ErrorMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultVo = new ErrorMgmtVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(msgDesc);
		}
		return resultVo;
	}
}
