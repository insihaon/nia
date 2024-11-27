package com.kt.ipms.legacy.opermgmt.uploadmgmt.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.annotation.EncryptResponse;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.BaseVo;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;
import com.kt.ipms.legacy.cmn.web.CommonController;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.service.UserMgmtService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

@Controller
public class EmailMgmtController extends CommonController {
	
	@Autowired
	private TacsMgmtService tacsMgmtService;
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	
	@RequestMapping(value = "/opermgmt/uploadmgmt/emailView.do", method = RequestMethod.POST)
	public String emailView(HttpServletRequest request, HttpServletResponse response){
		// System.out.println("--------------------------------------------------------------into controller");  //Codeeyes-Critical-sysout
		return "opermgmt/uploadmgmt/emailView";
	}
	
	/**
	 * 메일 보내기
	 * @param smtpVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opermgmt/uploadmgmt/sendMail.json", method = RequestMethod.POST)
	@ResponseBody
	@EncryptResponse
	public BaseVo sendMail(@RequestBody SmtpVo smtpVo, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		SmtpVo resultVo = new SmtpVo();
		String commonMsg;
		
		String mailType = null;
		String toEmail = null;
		String subject = null;		// 제목
		String content = null;		// 본문
		
		// 요청자 정보
		String userName = "";
		String userOrg = "";
		String userEmail = "";
		
		String searchIp = "";			// 검색IP
		String searchFirstIp = "";	// 시작IP
		String searchLastIp = "";	// 끝IP
		
		String sessionId = jwtUtil.getUserId(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
		
			mailType = smtpVo.getMailType();
			
			userName = jwtUtil.getUserNm(request);
			userOrg = jwtUtil.getUserDeptOrgNm(request);
			TbUserBasVo searchVo = new TbUserBasVo();
			searchVo.setSuserId(jwtUtil.getUserId(request));
			userEmail = userMgmtService.selectEmail(searchVo);
			map.put("MAIL_TYPE", mailType);
			
			if(mailType.equals("Ipms-Table-Error")) {		// IPMS Whois 테이블 조회불가시
				
				TbTacsConnBasVo vo = tacsMgmtService.selectTbTacsConnBas();
				searchIp = smtpVo.getSearchIp();
				
				toEmail = vo.getSmailAddress();
				subject = "[알림] Whois 정보 변경 신청시 IPMS DB 미등록 IP 조회 발생 알림";
				
				map.put("USER_NAME", userName);			// 요청자 이름
				map.put("USER_ORG", userOrg);				// 요청자 소속
				map.put("USER_EMAIL", userEmail);			// 요청자 이메일
				map.put("USER_SEARCH", searchIp);		// IP 주소 검색 내용
				
			} else if(mailType.equals("Kisa-Table-Error")) {		// KISA Whois 테이블 조회불가시
				
				TbTacsConnBasVo vo = tacsMgmtService.selectTbTacsConnBas();
				searchFirstIp = smtpVo.getSearchFirstIp();
				searchLastIp = smtpVo.getSearchLastIp();
				
				toEmail = vo.getSmailAddress();
				subject = "[알림] Whois 정보 변경 신청시 Kisa Whois 미등록 IP 조회 발생 알림";
				
				map.put("USER_NAME", userName);			// 요청자 이름
				map.put("USER_ORG", userOrg);				// 요청자 소속
				map.put("USER_EMAIL", userEmail);			// 요청자 이메일
				map.put("USER_SEARCH_FIRST_IP", searchFirstIp);		// 시작 IP
				map.put("USER_SEARCH_LAST_IP", searchLastIp);		// 끝 IP
				
				String strMsg = smtpVo.getWhoisRtnMsg();
				if(strMsg == null) strMsg = "";
				
				map.put("WHOIS_RTN_MSG", strMsg);			// Whois 정보조회 결과 메세지
				
			}
			
			map.put("TITLE", subject);
			content = smtpUtil.parseHtml(map, request);
			
			smtpVo.setSubject(subject);
			smtpVo.setToEmail(toEmail);
			smtpVo.setMessage(content);
			smtpVo.setUserID(sessionId);
			
			resultVo = smtpUtil.sendMail(smtpVo);
			
			commonMsg = CommonCodeUtil.SUCCESS_MSG;
			resultVo.setCommonMsg(commonMsg);
			
		} catch (ServiceException e) {
			resultVo = new SmtpVo();
			commonMsg = tbCmnMstService.selectMsgDesc(e);
			resultVo.setCommonMsg(commonMsg);
		} catch (Exception e) {
			resultVo = new SmtpVo();
			commonMsg = tbCmnMstService.selectMsgDesc(new ServiceException("CMN.HIGH.00000"));
			resultVo.setCommonMsg(commonMsg);
		}
		
		return resultVo;
	}
	
}
