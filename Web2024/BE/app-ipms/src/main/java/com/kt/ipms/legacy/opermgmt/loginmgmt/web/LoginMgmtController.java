package com.kt.ipms.legacy.opermgmt.loginmgmt.web;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.JwtUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.adapter.OrderMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstListVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.adapter.BoardMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;
import com.kt.ipms.legacy.opermgmt.loginmgmt.service.LoginMgmtService;
import com.kt.ipms.legacy.opermgmt.loginmgmt.service.LoginMgmtTxService;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.adapter.ConfigMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.adapter.TbAuditDhcpMstAdapterService;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.adapter.OptMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;

@Controller
public class LoginMgmtController  extends CommonController{
	
	
	@Autowired
	private LoginMgmtService loginMgmtService;
	
	@Autowired
	private BoardMgmtAdapterService boardMgmtAdapterService;
	
	@Autowired
	private OrderMgmtAdapterService orderMgmtAdapterService;
	
	@Autowired
	private ConfigMgmtAdapterService configMgmtAdapterService;
	
	@Autowired
	private TbAuditDhcpMstAdapterService tbAuditDhcpMstAdapterService;
	
	@Autowired
	private OptMgmtAdapterService optMgmtAdapterService;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	@Autowired
	private LoginMgmtTxService loginMgmtTxService;
	
	// @RequestMapping(value = "/", method ={ RequestMethod.GET,  RequestMethod.POST })
	// public String home(Locale locale, Model model , HttpServletRequest request, HttpServletResponse response)  {
		
	// 	/*
	// 	if(!request.isSecure())
	// 	{
	// 	return "redirect:https"+request.getRequestURL().toString().replace("http","");
	// 	}
	// 	*/
	// 	return "viewLogin";
	// }
	
//	@RequestMapping(value = "/login.json", method =   { RequestMethod.GET,  RequestMethod.POST })
//	@ResponseBody
//	public LoginInfoVo login(@RequestBody LoginInfoVo loginVo 
//			, HttpServletRequest request, HttpServletResponse response)  {
//		LoginInfoVo resultLoginVo = null;
//		try {
//			resultLoginVo = loginMgmtService.login(request, loginVo);
//			
//			if(  resultLoginVo.getsConnRsltCD().equals(CommonCodeUtil.LDAP_RESULT_FAIL))
//			{
//				ServiceException e = new ServiceException("CMN.INFO.00035");
//				String msgDesc = tbCmnMstService.selectMsgDesc(e);
//				resultLoginVo.setCommonMsg(msgDesc);
//			}
//			else
//			{
//				jwtUtil.setNewSession(request, loginVo);
//				resultLoginVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
//			}
//		} catch(ServiceException e)	{
//			resultLoginVo = new LoginInfoVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(e);
//			resultLoginVo.setCommonMsg(msgDesc);
//		} catch (Exception e) {
//			resultLoginVo = new LoginInfoVo();
//			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
//			resultLoginVo.setCommonMsg(msgDesc);
//		}
//		return resultLoginVo;
//	}

	@RequestMapping(value = "/reLogin", method = RequestMethod.GET)
	public void reLogin(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		LoginInfoVo loginVo = null;
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream("login.ser");
			in = new ObjectInputStream(fileIn);
			loginVo = (LoginInfoVo) in.readObject();
			jwtUtil.setNewSession(request, loginVo);
		} catch (Exception e) {
			// 예외처리
		} finally {
			if(in != null){
				in.close();
			}
			if(fileIn != null){
				fileIn.close();
			}
		}
	}
	
	@RequestMapping(value = "/newLogin.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> newLogin(@RequestBody LoginInfoVo loginVo, HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException{
		
		Map<String,Object> retMap = new HashMap<String,Object>();		
		LoginInfoVo resultLoginVo = null;
		
		boolean isOtpCheck = configPropertieService.getBoolean("Auth.isOtpCheck");
		boolean isDev = configPropertieService.getBoolean("Common.isDev");
		boolean isTodayLogin = false;
				
		try{
			String userid = loginVo.getSuserId();
			String password = URLDecoder.decode(loginVo.getsUserPw(), "UTF-8");
			loginVo.setSuserId(userid);
			loginVo.setsUserPw(password);
			
			isTodayLogin = loginMgmtService.checkTodayLogin(request,loginVo);
//			logintxservice  setHostInfomation 으로 ip값 셋팅 하고 플래그 추가(현재 페이지 ip와 tb_user_conn_hist ip 비교)
			loginMgmtTxService.setHostInfomation(request, loginVo);
			boolean isIpLogin = loginMgmtService.checkIPLogin(loginVo);
			
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> IS TODAY LOGIN : "+isTodayLogin);
			
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> IS TODAY LOGIN : "+(isIpLogin));
			
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> IS TODAY LOGIN : "+(isOtpCheck && !isTodayLogin && isIpLogin));
			
			if(isOtpCheck && !isTodayLogin){ // OTP 로직
				PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> OTP LOGIN");
				resultLoginVo = loginMgmtService.newLogin(request, loginVo);
			}else{ // 일반 로직
				PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> BASIC LOGIN");
				resultLoginVo = loginMgmtService.login(request, loginVo);
			}
			
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> LOGIN STEP 1 END");
			
			if(resultLoginVo.getsConnRsltCD().equals(CommonCodeUtil.LDAP_RESULT_FAIL)) {
				ServiceException e = new ServiceException("CMN.INFO.00035");
				String msgDesc = tbCmnMstService.selectMsgDesc(e);
				retMap.put("commonMsg", msgDesc);
				retMap.put("result", "fail");
			} else {
				retMap.put("commonMsg", CommonCodeUtil.SUCCESS_MSG);
				if (isOtpCheck && !isTodayLogin){
					PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> OTP STEP START");
					Map<String,Object> otpRetMap = loginMgmtService.callOtpSend(request,loginVo);					
					retMap.putAll(otpRetMap);
				}else{
					Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
					if(isRun){
						if(isIpLogin){
							PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> OTP STEP START");
							Map<String,Object> otpRetMap = loginMgmtService.callOtpSend(request,loginVo);					
							retMap.putAll(otpRetMap);
						}else{
							// jwtUtil.setNewSession(request, loginVo);
							retMap.put("result", "success");
						}
					}else{
						// jwtUtil.setNewSession(request, loginVo);
						retMap.put("result", "success");
					}
				}

				if(isDev) {
					FileOutputStream fileOut = null;
					ObjectOutputStream out = null;
					try {
						fileOut = new FileOutputStream("login.ser");
						out = new ObjectOutputStream(fileOut);
						out.writeObject(loginVo);
					} catch (Exception e) {
						// 예외처리
					} finally {
						if(out != null){
							out.close();
						}
						if(fileOut != null){
							fileOut.close();
						}
					}
				}
			}
			
		} catch(ServiceException e)	{
			
			e.printStackTrace();
			String msgDesc = "알수없는 에러가 발생했습니다";
			retMap.put("result", "fail");
			retMap.put("commonMsg", msgDesc);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			String msgDesc = "알수없는 에러가 발생했습니다";
			retMap.put("result", "fail");
			retMap.put("commonMsg", msgDesc);
		}
		
		return retMap;
	}
	
	@RequestMapping(value = "otpconfirm.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> otpConfirm(@RequestBody Map<String,Object> reqMap,HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> retMap = new HashMap<String,Object>();
		LoginInfoVo loginInfoVo = new LoginInfoVo();
		
		PrintLogUtil.pringLogHashMap(reqMap);
		String userId = reqMap.get("suserId").toString();
		loginInfoVo.setSuserId(userId);
		
		/*OTP 컨펌 로직 추가*/
		retMap = loginMgmtService.callOtpConfirm(request, loginInfoVo, reqMap);
		if(retMap.get("result").equals("success")){
			jwtUtil.setNewSession(request, loginInfoVo);
		}
		return retMap;
	}
	
	@RequestMapping(value = "otprecreate.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> otpRecreate(@RequestBody LoginInfoVo loginVo, HttpServletRequest request, HttpServletResponse response){
				
		Map<String,Object> otpRetMap = loginMgmtService.callOtpSend(request,loginVo);
				
		return otpRetMap; 
		
	}
		
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String loginDo(@ModelAttribute("searchVo") LoginInfoVo loginVo
			, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		LoginInfoVo resultLoginVo = null;
		String url = "viewLogin";
		try {
			String password = URLDecoder.decode(loginVo.getsUserPw(), "UTF-8");
			loginVo.setsUserPw(password);
			resultLoginVo = loginMgmtService.login(request, loginVo);
			
			if(resultLoginVo.getsConnRsltCD().equals(CommonCodeUtil.LDAP_RESULT_FAIL)) {
				ServiceException e = new ServiceException("CMN.INFO.00035");
				String msgDesc = tbCmnMstService.selectMsgDesc(e);
				resultLoginVo.setCommonMsg(msgDesc);
			} else {
				// jwtUtil.setNewSession(request, loginVo);
				resultLoginVo.setCommonMsg(CommonCodeUtil.SUCCESS_MSG);
				url = "redirect:/main/viewMain.do";
				return url;
			}
		} catch(ServiceException e)	{
			resultLoginVo = new LoginInfoVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(e);
			resultLoginVo.setCommonMsg(msgDesc);
		} catch (Exception e) {
			resultLoginVo = new LoginInfoVo();
			String msgDesc = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultLoginVo.setCommonMsg(msgDesc);
		}
		model.addAttribute("resultLoginVo", resultLoginVo);
		return url;
	}
		
	@RequestMapping(value = "/logout.do", method = { RequestMethod.GET,  RequestMethod.POST })
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			LoginInfoVo  logoutVo  =  jwtUtil.getSessionVO(request);
			if (logoutVo != null) {
				loginMgmtService.logout(logoutVo);
			}
		} catch (Exception e) {
			PrintLogUtil.printError(e);
		} finally {
			try {
				jwtUtil.invalidSession(request);
			} catch (Exception e) {
				PrintLogUtil.printError(e);
			}
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/main/viewMain.model", method = { RequestMethod.GET,  RequestMethod.POST })
	public ModelMap viewMain(@RequestBody CommonVo searchVo, HttpServletRequest request)  {
		ModelMap resultModel = new ModelMap();

		HashMap<String, Object> map = new HashMap<String, Object>();
		ModelMap model = viewMainModel(request);
		/** 공지사항 목록 **/
		TbBoardListVo mainNotiListVo = (TbBoardListVo)model.get("mainNotiListVo");
		map.put("mainNotiListVo", mainNotiListVo.getTbBoardVos());
		map.put("mainNotiTotalCount", mainNotiListVo.getTotalCount());
		/** 긴급 팝업 공지 SEQ **/
		map.put("notiSeq", model.get("notiSeq"));

		/** NEOSS 오더 목록 **/
		IpAllocOrderMstListVo mainOrderListVo = (IpAllocOrderMstListVo)model.get("mainOrderListVo");
		map.put("mainOrderListVo", mainOrderListVo.getIpAllocOrderMstVos());
		map.put("mainOrderTotalCount", mainOrderListVo.getTotalCount());
			
		/** IFOMS 감사 현황 목록 **/
		TbConfigRouteMstListVo mainIfomsListVo = (TbConfigRouteMstListVo)model.get("mainIfomsListVo");
		map.put("mainIfomsListVo", mainIfomsListVo.getTbConfigRouteMstVos());
		map.put("mainIfomsTotalCount", mainIfomsListVo.getTotalCount());
		
		/** 신인증 IP 최적화 목록 **/
		TbAuditDhcpMstListVo mainAuditListVo = (TbAuditDhcpMstListVo)model.get("mainAuditListVo");
		map.put("mainAuditListVo", mainAuditListVo.getTbAuditDhcpMstVos());
		map.put("mainAudioTotalCount", mainAuditListVo.getTotalCount());
		
		/** 조각 IP 최적화 현황 목록 **/
		TbOptimizationIpMstListVo mainOptListVo = (TbOptimizationIpMstListVo)model.get("mainOptListVo");
		map.put("mainOptListVo", mainOptListVo.getTbOptimizationIpMstVos());
		map.put("mainOptTotalCount", mainOptListVo.getTotalCount());

		resultModel.addAttribute("result",map);

		return resultModel;
	}
	@RequestMapping(value = "/main/viewMain.do", method = { RequestMethod.GET,  RequestMethod.POST })
	public String viewMain(@ModelAttribute("searchVo") CommonVo searchVo, ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
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
		ModelMap builtModel = viewMainModel(request);
		model.addAllAttributes(builtModel);

		searchVoClone.setUrl("/main/viewMain.model");
		model.addAttribute("searchVoJson", searchVoJson(searchVoClone));
		return "main/viewMain";
	}
	private ModelMap viewMainModel(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try{
			/** 공지사항 목록 **/
			TbBoardListVo mainNotiListVo = null;
			
			/** 긴급 팝업 공지 SEQ **/
			String notiSeq = null;
			
			/** NEOSS 오더 목록 **/
			IpAllocOrderMstListVo mainOrderListVo = null;
			
			/** IFOMS 감사 현황 목록 **/
			TbConfigRouteMstListVo mainIfomsListVo = null;
			
			/** 신인증 IP 최적화 목록 **/
			TbAuditDhcpMstListVo mainAuditListVo = null;
			
			/** 조각 IP 최적화 현황 목록 **/
			TbOptimizationIpMstListVo mainOptListVo = null;
			
			String userGradeCd = jwtUtil.getUserGradeCd(request);
			
			try {
				mainNotiListVo = boardMgmtAdapterService.selectListTbBoardByMain();
				
				if (mainNotiListVo != null && mainNotiListVo.getTotalCount() > 0) {
					List<TbBoardVo> notiList =  mainNotiListVo.getTbBoardVos();
					for (int i=0; i < notiList.size(); i++) {
						TbBoardVo itemVo = notiList.get(i);
						if (itemVo.getSboardTypeSubCd().equals("BM0002")) {
							notiSeq = itemVo.getSeq().toString();
							break;
						}
					}
				}
			} catch (Exception e) {
				mainNotiListVo = new TbBoardListVo();
				mainNotiListVo.setTotalCount(0);
			}
			model.addAttribute("mainNotiListVo", mainNotiListVo);
			model.addAttribute("notiSeq", notiSeq);
			
			if (userGradeCd.equals(CommonCodeUtil.USER_GRADE_R)) {
				mainOrderListVo = new IpAllocOrderMstListVo();
				mainOrderListVo.setTotalCount(0);
				mainIfomsListVo = new TbConfigRouteMstListVo();
				mainIfomsListVo.setTotalCount(0);
				mainAuditListVo = new TbAuditDhcpMstListVo();
				mainAuditListVo.setTotalCount(0);
				mainOptListVo = new TbOptimizationIpMstListVo();
				mainOptListVo.setTotalCount(0);
			} else {
				TbLvlMstListVo tbLvlMstListVo = jwtUtil.getLvlSeqList(request, new TbLvlMstVo());
				try {
					IpAllocOrderMstVo searchVo = new IpAllocOrderMstVo();
					searchVo.setLvlMstSeqListVo(tbLvlMstListVo);
					setPagination(searchVo);
					mainOrderListVo = orderMgmtAdapterService.selectListMainOrderMst(searchVo);
				} catch (Exception e) {
					mainOrderListVo = new IpAllocOrderMstListVo();
					mainOrderListVo.setTotalCount(0);
				}
				
				try {
					TbConfigRouteMstVo searchVo = new TbConfigRouteMstVo();
					searchVo.setLvlMstSeqListVo(tbLvlMstListVo);
					setPagination(searchVo);
					mainIfomsListVo = configMgmtAdapterService.selectListMainRouteMst(searchVo);
				} catch (Exception e) {
					mainIfomsListVo = new TbConfigRouteMstListVo();
					mainIfomsListVo.setTotalCount(0);
				}
				
				try {
					TbAuditDhcpMstVo searchVo = new TbAuditDhcpMstVo();
					searchVo.setTypeFlag("MAIN");
					searchVo.setSortType(CommonCodeUtil.SORT_TYPE_DMODIFY_DT);
					searchVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_DESC);
					searchVo.setLvlMstSeqListVo(tbLvlMstListVo);
					setPagination(searchVo);
					mainAuditListVo = tbAuditDhcpMstAdapterService.selectListAuditDhcpMst(searchVo);
				} catch (Exception e) {
					mainAuditListVo = new TbAuditDhcpMstListVo();
					mainAuditListVo.setTotalCount(0);
				}
				
				try {
					TbOptimizationIpMstVo searchVo = new TbOptimizationIpMstVo();
					searchVo.setLvlMstSeqListVo(tbLvlMstListVo);
					setPagination(searchVo);
					mainOptListVo = optMgmtAdapterService.selectListPageOptimizeIpResult(searchVo);
				} catch (Exception e) {
					mainOptListVo = new TbOptimizationIpMstListVo();
					mainOptListVo.setTotalCount(0);
				}
			}
			model.addAttribute("mainOrderListVo", mainOrderListVo);
			model.addAttribute("mainIfomsListVo", mainIfomsListVo);
			model.addAttribute("mainAuditListVo", mainAuditListVo);
			model.addAttribute("mainOptListVo", mainOptListVo);
		} catch (Exception e) {
			PrintLogUtil.printError(e);
		}
		return model;
	}
	
	
	
	@RequestMapping(value = "/main/selectMainIpInfo.do", method = RequestMethod.POST)
	public String selectMainIpInfo(ModelMap model, HttpServletRequest request, HttpServletResponse response)  {
		
		try{
			
			TbBoardListVo mainNotiListVo = new TbBoardListVo();
			mainNotiListVo.setTotalCount(0);
			mainNotiListVo.setTypeFlag("MENU");
			String notiSeq = null;
			/** NEOSS 오더 목록 **/
			IpAllocOrderMstListVo mainOrderListVo = new IpAllocOrderMstListVo();
			mainOrderListVo.setTotalCount(0);
			
			/** IFOMS 감사 현황 목록 **/
			TbConfigRouteMstListVo mainIfomsListVo = new TbConfigRouteMstListVo();
			mainIfomsListVo.setTotalCount(0);
			
			/** 신인증 IP 최적화 목록 **/
			TbAuditDhcpMstListVo mainAuditListVo = new TbAuditDhcpMstListVo();
			mainAuditListVo.setTotalCount(0);
			
			/** 조각 IP 최적화 현황 목록 **/
			TbOptimizationIpMstListVo mainOptListVo = new TbOptimizationIpMstListVo();
			mainOptListVo.setTotalCount(0);
			
			IpAllocOperMstVo tbIpInfoVo = null;
			TbIpHostMstVo tbHostInfoVo = null;
			model.addAttribute("tbIpInfoVo", tbIpInfoVo);
			model.addAttribute("tbHostInfoVo", tbHostInfoVo);
			
			model.addAttribute("mainNotiListVo", mainNotiListVo);
			model.addAttribute("notiSeq", notiSeq);
			model.addAttribute("mainOrderListVo", mainOrderListVo);
			model.addAttribute("mainIfomsListVo", mainIfomsListVo);
			model.addAttribute("mainAuditListVo", mainAuditListVo);
			model.addAttribute("mainOptListVo", mainOptListVo);
			// System.out.println("Request Url ==="+request.getRequestURI().toString());  //Codeeyes-Critical-sysout
			PrintLogUtil.printLog("Request Url ==="+request.getRequestURI().toString());
		} catch (Exception e) {
			PrintLogUtil.printError(e);
		}
		
		return "main/viewMain";
	}
	
}
