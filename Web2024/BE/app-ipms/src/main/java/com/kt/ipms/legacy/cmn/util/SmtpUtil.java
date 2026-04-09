package com.kt.ipms.legacy.cmn.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;


import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;










import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.vo.SmtpVo;

@Component
public class SmtpUtil {
   @Autowired
	private ConfigPropertieService configPropertieService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	/**
	 * Send Email 
	 * @param smtpVo
	 * @param request
	 * @return
	 */
	public SmtpVo sendMail (SmtpVo smtpVo) throws IOException {
		
		SmtpVo resultVo = new SmtpVo();
		
		String fromEmail = configPropertieService.getString("Mail.fromAddress");
		Properties props = new Properties();
        props.put("mail.smtp.host", configPropertieService.getString("Mail.smtp.host"));
        props.put("mail.smtp.port", configPropertieService.getString("Mail.smtp.port"));      
        props.put("mail.smtp.auth", "false"); 
        Session session = Session.getInstance(props);
        
        Boolean isSend = configPropertieService.getBoolean("Mail.isSend");
        Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
        
        String strSubject = "";
        
        InternetAddress toAddress = null;
        try{
        	
        	if(isRun) {
        		strSubject = smtpVo.getSubject();
        	} else {
        		strSubject = "[개발테스트] " +  smtpVo.getSubject();
        	}
        	
        	//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + smtpVo.getToEmail());
	        InternetAddress fromAddress = new InternetAddress(fromEmail);
	        toAddress   = new InternetAddress(smtpVo.getToEmail().toString());
	       
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(fromAddress);
	        message.addRecipient(Message.RecipientType.TO, toAddress);
	        message.setSubject(strSubject);
	        message.setContent(smtpVo.getMessage(), "text/html; charset=UTF-8");
	        
	        if(isSend) {
	        	  Transport.send(message);
	        }
	        
	        resultVo.setResult("SUCCESS");
	        
	     } catch (MessagingException e) {
	         e.printStackTrace();
	         resultVo.setResult(e.toString());
	     } 
        
        resultVo.setFromEmail(fromEmail);
        resultVo.setToEmail(smtpVo.getToEmail());
        resultVo.setSubject(strSubject);
        resultVo.setMessage(smtpVo.getMessage());
        resultVo.setUserID(smtpVo.getUserID());
        
        commonService.insertEmailLog(resultVo);
         
		return resultVo;
	}
	
	
	public String parseHtml (Map<String,Object> map , HttpServletRequest request) throws IOException {
		
		String result = "";
		String mailType = "";
		String path = "";
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader bufferedReader  = null;
		
		Boolean isLocal = null;
		String rootPath = configPropertieService.getString("Mail.rootPath");
		String htmlPath = "";
		try {
			
			mailType = map.get("MAIL_TYPE").toString();
			
			if(mailType.equals("Ipms-Table-Error")) {										// IPMS Whois 테이블 조회불가시
				path = configPropertieService.getString("Mail.Ipms");			
			} else if(mailType.equals("Kisa-Table-Error")) {								// KISA Whois 서버 조회 불가시
				path = configPropertieService.getString("Mail.Kisa");
			} else if(mailType.equals("Whois-Req-Info-User")) {						// Whois 정보 변경신청시(요청자)
				path = configPropertieService.getString("Mail.Whois.Req.User");
			} else if(mailType.equals("Whois-Req-Info-Admin")) {						// Whois 정보 변경신청시(담당자)
				path = configPropertieService.getString("Mail.Whois.Req.Admin");
			} else if(mailType.equals("Whois-Aprv-Info")) {								// Whois 정보 승인시
				path = configPropertieService.getString("Mail.Whois.Aprv");
			} else if(mailType.equals("Whois-Reject-Info")) {								// Whois 정보 반려시
				path = configPropertieService.getString("Mail.Whois.Reject");
			} else if(mailType.equals("IpAssign-Req-User")) {						// IP배정신청시(요청자)
				path = configPropertieService.getString("Mail.IpAssign.Req.User");
			} else if(mailType.equals("IpAssign-Req-Admin")) {						// IP배정신청시(담당자)
				path = configPropertieService.getString("Mail.IpAssign.Req.Admin");
			} else if(mailType.equals("IpAssign-Aprv")) {								// IP배정신청 승인시
				path = configPropertieService.getString("Mail.IpAssign.Aprv");
			} else if(mailType.equals("IpAssign-Reject")) {								// IP배정신청 반려시
				path = configPropertieService.getString("Mail.IpAssign.Reject");
			} else if(mailType.equals("PrivateAs-Req-User")) {						// 사설AS신청시(요청자)
				path = configPropertieService.getString("Mail.PrivateAs.Req.User");
			} else if(mailType.equals("PrivateAs-Req-Admin")) {						// 사설AS신청시(담당자)
				path = configPropertieService.getString("Mail.PrivateAs.Req.Admin");
			} else if(mailType.equals("PrivateAs-Aprv")) {								// 사설AS신청 승인시
				path = configPropertieService.getString("Mail.PrivateAs.Aprv");
			} else if(mailType.equals("PrivateAs-Reject")) {								// 사설AS신청 반려시
				path = configPropertieService.getString("Mail.PrivateAs.Reject");
			} else if(mailType.equals("Req-Insert")) {								// 요청사항 추가시
				path = configPropertieService.getString("Req-Insert");
				path = "/resources/html/mailTemplate15.html";
			} else if(mailType.equals("Req-Update")) {								// 요청사항 수정시
				path = configPropertieService.getString("Req-Update");
				path = "/resources/html/mailTemplate16.html";
			} else if(mailType.equals("Node-Insert")) {								// 노드 이전 신청 추가시
				path = configPropertieService.getString("Node-Insert");
				path = "/resources/html/mailTemplate17.html";
			} else if(mailType.equals("Node-Delete")) {								// 노드 이전 신청 삭제시
				path = configPropertieService.getString("Node-Delee");
				path = "/resources/html/mailTemplate18.html";
			} else if(mailType.equals("PrivateIP-Req-User")) {							// 사설IP신청시(요청자)
				path = configPropertieService.getString("Mail.PrivateIP.Req.User");
			} else if(mailType.equals("PrivateIP-Req-Admin")) {						// 사설IP신청시(담당자)
				path = configPropertieService.getString("Mail.PrivateIP.Req.Admin");
			} else if(mailType.equals("PrivateIP-Aprv")) {								// 사설IP신청 승인시
				path = configPropertieService.getString("Mail.PrivateIP.Aprv");
			} else if(mailType.equals("PrivateIP-Reject")) {								// 사설IP신청 반려시
				path = configPropertieService.getString("Mail.PrivateIP.Reject");
			} else if(mailType.equals("Grant-Insert")) {								// 권한 신청 추가시
				path = configPropertieService.getString("Node-Insert");
				path = "/resources/html/mailTemplate23.html";
			} else if(mailType.equals("Grant-Delete")) {								// 권한 신청 반려시
				path = configPropertieService.getString("Node-Delete");
				path = "/resources/html/mailTemplate24.html";
			} 
			
			isLocal =  configPropertieService.getBoolean("Mail.isLocal");
			
			if(isLocal) {
				htmlPath = request.getSession().getServletContext().getRealPath(path);
			} else {
				htmlPath = rootPath + path;
			}
			
			//Path ppath = Paths.get(htmlPath);
			//List<String> content = Files.readAllLines(ppath, StandardCharsets.UTF_8);
			
			//for(String strContent : content) {
			//	result += strContent; 
			//}
			
			fis = new FileInputStream(htmlPath);
			isr = new InputStreamReader(fis, "UTF-8");
			bufferedReader = new BufferedReader(isr);
			
			String tempStr = "";
			StringBuffer sb = new StringBuffer();
			
//			while ( (tempStr = bufferedReader.readLine()) != null ) { //Codeeyes-Urgent-String 추가 시 String Buffer 사용
			while ( (tempStr = bufferedReader.readLine()) != null ) {
//				result += tempStr;
				sb.append(tempStr);
			} 
			
   
			result = sb.toString();
					
//			if(result != "") { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(!result.equals("")) {
					 
				
				result = result.replace("[TITLE]", map.get("TITLE").toString());
				
				if(mailType.equals("Ipms-Table-Error")) {
					
					result = result.replace("[USER_NAME]", map.get("USER_NAME").toString());
					result = result.replace("[USER_ORG]", map.get("USER_ORG").toString());
					result = result.replace("[USER_EMAIL]", map.get("USER_EMAIL").toString());
					result = result.replace("[USER_SEARCH]", map.get("USER_SEARCH").toString());
					
				} else if(mailType.equals("Kisa-Table-Error")) {
					
					result = result.replace("[USER_NAME]", map.get("USER_NAME").toString());
					result = result.replace("[USER_ORG]", map.get("USER_ORG").toString());
					result = result.replace("[USER_EMAIL]", map.get("USER_EMAIL").toString());
					result = result.replace("[USER_SEARCH_FIRST_IP]", map.get("USER_SEARCH_FIRST_IP").toString());
					result = result.replace("[USER_SEARCH_LAST_IP]", map.get("USER_SEARCH_LAST_IP").toString());
					result = result.replace("[WHOIS_RTN_MSG]", map.get("WHOIS_RTN_MSG").toString());
					
				} else if(mailType.equals("Whois-Req-Info-User")) {				
					
					result = result.replace("[FIRST_IP]", map.get("FIRST_IP").toString());
					result = result.replace("[LAST_IP]", map.get("LAST_IP").toString());
					result = result.replace("[BEF_ORG_NAME]", map.get("BEF_ORG_NAME").toString());
					result = result.replace("[BEF_ORG_ADDR]", map.get("BEF_ORG_ADDR").toString());
					result = result.replace("[BEF_ZIPCODE]", map.get("BEF_ZIPCODE").toString());
					result = result.replace("[BEF_ORG_ENAME]", map.get("BEF_ORG_ENAME").toString());
					result = result.replace("[BEF_ORG_EADDR]", map.get("BEF_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_NAME]", map.get("AFT_ORG_NAME").toString());
					result = result.replace("[AFT_ORG_ADDR]", map.get("AFT_ORG_ADDR").toString());
					result = result.replace("[AFT_ORG_ADDR_DTL]", map.get("AFT_ORG_ADDR_DTL").toString());
					result = result.replace("[AFT_ZIPCODE]", map.get("AFT_ZIPCODE").toString());
					result = result.replace("[AFT_ORG_ENAME]", map.get("AFT_ORG_ENAME").toString());
					result = result.replace("[AFT_ORG_EADDR]", map.get("AFT_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_EADDR_DTL]", map.get("AFT_ORG_EADDR_DTL").toString());
				
				} else if(mailType.equals("Whois-Req-Info-Admin")) {		
					
					result = result.replace("[USER_NAME]", map.get("USER_NAME").toString());
					result = result.replace("[USER_ORG]", map.get("USER_ORG").toString());
					result = result.replace("[USER_EMAIL]", map.get("USER_EMAIL").toString());
					result = result.replace("[FIRST_IP]", map.get("FIRST_IP").toString());
					result = result.replace("[LAST_IP]", map.get("LAST_IP").toString());
					result = result.replace("[BEF_ORG_NAME]", map.get("BEF_ORG_NAME").toString());
					result = result.replace("[BEF_ORG_ADDR]", map.get("BEF_ORG_ADDR").toString());
					result = result.replace("[BEF_ZIPCODE]", map.get("BEF_ZIPCODE").toString());
					result = result.replace("[BEF_ORG_ENAME]", map.get("BEF_ORG_ENAME").toString());
					result = result.replace("[BEF_ORG_EADDR]", map.get("BEF_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_NAME]", map.get("AFT_ORG_NAME").toString());
					result = result.replace("[AFT_ORG_ADDR]", map.get("AFT_ORG_ADDR").toString());
					result = result.replace("[AFT_ORG_ADDR_DTL]", map.get("AFT_ORG_ADDR_DTL").toString());
					result = result.replace("[AFT_ZIPCODE]", map.get("AFT_ZIPCODE").toString());
					result = result.replace("[AFT_ORG_ENAME]", map.get("AFT_ORG_ENAME").toString());
					result = result.replace("[AFT_ORG_EADDR]", map.get("AFT_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_EADDR_DTL]", map.get("AFT_ORG_EADDR_DTL").toString());
					
				} else if(mailType.equals("Whois-Aprv-Info")) {		
					
					result = result.replace("[FIRST_IP]", map.get("FIRST_IP").toString());
					result = result.replace("[LAST_IP]", map.get("LAST_IP").toString());
					result = result.replace("[BEF_ORG_NAME]", map.get("BEF_ORG_NAME").toString());
					result = result.replace("[BEF_ORG_ADDR]", map.get("BEF_ORG_ADDR").toString());
					result = result.replace("[BEF_ZIPCODE]", map.get("BEF_ZIPCODE").toString());
					result = result.replace("[BEF_ORG_ENAME]", map.get("BEF_ORG_ENAME").toString());
					result = result.replace("[BEF_ORG_EADDR]", map.get("BEF_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_NAME]", map.get("AFT_ORG_NAME").toString());
					result = result.replace("[AFT_ORG_ADDR]", map.get("AFT_ORG_ADDR").toString());
					result = result.replace("[AFT_ORG_ADDR_DTL]", map.get("AFT_ORG_ADDR_DTL").toString());
					result = result.replace("[AFT_ZIPCODE]", map.get("AFT_ZIPCODE").toString());
					result = result.replace("[AFT_ORG_ENAME]", map.get("AFT_ORG_ENAME").toString());
					result = result.replace("[AFT_ORG_EADDR]", map.get("AFT_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_EADDR_DTL]", map.get("AFT_ORG_EADDR_DTL").toString());
					
				}  else if(mailType.equals("Whois-Reject-Info")) {		
					
					result = result.replace("[FIRST_IP]", map.get("FIRST_IP").toString());
					result = result.replace("[LAST_IP]", map.get("LAST_IP").toString());
					result = result.replace("[BEF_ORG_NAME]", map.get("BEF_ORG_NAME").toString());
					result = result.replace("[BEF_ORG_ADDR]", map.get("BEF_ORG_ADDR").toString());
					result = result.replace("[BEF_ZIPCODE]", map.get("BEF_ZIPCODE").toString());
					result = result.replace("[BEF_ORG_ENAME]", map.get("BEF_ORG_ENAME").toString());
					result = result.replace("[BEF_ORG_EADDR]", map.get("BEF_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_NAME]", map.get("AFT_ORG_NAME").toString());
					result = result.replace("[AFT_ORG_ADDR]", map.get("AFT_ORG_ADDR").toString());
					result = result.replace("[AFT_ORG_ADDR_DTL]", map.get("AFT_ORG_ADDR_DTL").toString());
					result = result.replace("[AFT_ZIPCODE]", map.get("AFT_ZIPCODE").toString());
					result = result.replace("[AFT_ORG_ENAME]", map.get("AFT_ORG_ENAME").toString());
					result = result.replace("[AFT_ORG_EADDR]", map.get("AFT_ORG_EADDR").toString());
					result = result.replace("[AFT_ORG_EADDR_DTL]", map.get("AFT_ORG_EADDR_DTL").toString());
					result = result.replace("[REJEC_RSN]", map.get("REJECT_RSN").toString());
					
				} else if(mailType.equals("IpAssign-Aprv") || mailType.equals("IpAssign-Reject")) {		
					
					result = result.replace("[SCONTENTS]", map.get("SCONTENTS").toString());
					result = result.replace("[DTRT_DT]", map.get("DTRT_DT").toString());
					result = result.replace("[STRT_CONTENTS]", map.get("STRT_CONTENTS").toString());
					if(map.get("SASSIGNCONTENTS") == null){
						result = result.replace("[SASSIGNCONTENTS]", "");
					}else{
						result = result.replace("[SASSIGNCONTENTS]", map.get("SASSIGNCONTENTS").toString());
					}
					
					
				}  else if(mailType.equals("PrivateAs-Aprv") || mailType.equals("PrivateAs-Reject")) {		
					
					if(map.get("AS_SEQ") != null) {
						result = result.replace("[AS_SEQ]", map.get("AS_SEQ").toString());
					}
					result = result.replace("[AS_CTM]", map.get("AS_CTM").toString());
					result = result.replace("[AS_DT]", map.get("AS_DT").toString());
				} else if(mailType.equals("Req-Insert")) {	
					result = result.replace("[SEQ]", map.get("seq").toString());
					result = result.replace("[RBOARD_DIVISION]", map.get("rboardDivision").toString());
					result = result.replace("[RBOARD_TITLE]", map.get("rboardTitle").toString());
					result = result.replace("[SUSERNM]", map.get("sUserNm").toString());
					if(map.get("rboardDcreateDt") == null){
						result = result.replace("[DCREATE_DT]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardDcreateDt").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[DCREATE_DT]", dateToStr);
					}
					if(map.get("rboardDesireDate") == null){
						result = result.replace("[RBOARD_DESIRE_DATE]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardDesireDate").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[RBOARD_DESIRE_DATE]", dateToStr);
					}
					if(map.get("rboardExpectedDate") == null){
						result = result.replace("[RBOARD_EXPECTED_DATE]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardExpectedDate").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[RBOARD_EXPECTED_DATE]", dateToStr);
					}
					result = result.replace("[RBOARD_IMPORTANCE]", map.get("rboardImportance").toString());
					result = result.replace("[RBOARD_PROGRESS]", map.get("rboardProgress").toString());
					
				}else if(mailType.equals("Req-Update")) {	
					result = result.replace("[SEQ]", map.get("seq").toString());
					result = result.replace("[RBOARD_DIVISION]", map.get("rboardDivision").toString());
					result = result.replace("[RBOARD_TITLE]", map.get("rboardTitle").toString());
					result = result.replace("[SUSERNM]", map.get("sUserNm").toString());
					if(map.get("rboardDcreateDt") == null){
						result = result.replace("[DCREATE_DT]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardDcreateDt").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[DCREATE_DT]", dateToStr);
					}
					if(map.get("rboardDesireDate") == null){
						result = result.replace("[RBOARD_DESIRE_DATE]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardDesireDate").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[RBOARD_DESIRE_DATE]", dateToStr);
					}
					if(map.get("rboardExpectedDate") == null){
						result = result.replace("[RBOARD_EXPECTED_DATE]", "");
					}else{
						SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
						Date date = transFormat.parse(map.get("rboardExpectedDate").toString());
						String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[RBOARD_EXPECTED_DATE]", dateToStr);
					}
					result = result.replace("[RBOARD_IMPORTANCE]", map.get("rboardImportance").toString());
					result = result.replace("[RBOARD_PROGRESS]", map.get("rboardProgress").toString());
					if(map.get("rboardActionDetail") == null){
						result = result.replace("[RBOARD_ACTION_DETAIL]", "");
					}else{
						result = result.replace("[RBOARD_ACTION_DETAIL]", map.get("rboardActionDetail").toString());
					}
					
				}else if(mailType.equals("Node-Insert") || mailType.equals("Node-Delete")) {		
					result = result.replace("[SEQ]", map.get("seq").toString());
					result = result.replace("[PIP_PREFIX]", map.get("pipPrefix").toString());
					result = result.replace("[BEFORE_SSVC_LINE_TYPE_NM]", map.get("beforeSsvcLineTypeNm").toString());
					result = result.replace("[BEFORE_SSVC_GROUP_NM]", map.get("beforeSsvcGroupNm").toString());
					result = result.replace("[BEFORE_SSVC_OBJ_NM]", map.get("beforeSsvcObjNm").toString());
					result = result.replace("[AFTER_SSVC_LINE_TYPE_NM]", map.get("afterSsvcLineTypeNm").toString());
					result = result.replace("[AFTER_SSVC_GROUP_NM]", map.get("afterSsvcGroupNm").toString());
					result = result.replace("[AFTER_SSVC_OBJ_NM]", map.get("afterSsvcObjNm").toString());
					result = result.replace("[SUSERNM]", map.get("sUserNm").toString());
					SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
					Date date = transFormat.parse(map.get("dCreateDt").toString());
					String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
					result = result.replace("[DCREATE_DT]", dateToStr);
					if(map.get("dCompleteDt") == null){
						result = result.replace("[DCOMPLETE_DT]", "");
					}else{
						date = transFormat.parse(map.get("dCompleteDt").toString());
						dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
						result = result.replace("[DCOMPLETE_DT]", dateToStr);
					}
					
					result = result.replace("[PROGRESS_STATUS_NM]", map.get("progressStatusNm").toString());
				}
				
				/* 사설IP신청 */
				else if(mailType.equals("PrivateIP-Req-User")) {	  // 사설IP신청 mailTemplate19.html			
					
					result = result.replace("[TITLE]", map.get("TITLE").toString());
					result = result.replace("[REQUEST_TYPE_NM]", map.get("REQUEST_TYPE_NM").toString());
					
				} else if(mailType.equals("PrivateIP-Req-Admin")) {	// 사설IP신청 mailTemplate20.html		
					
					result = result.replace("[TITLE]", map.get("TITLE").toString());
					result = result.replace("[REQUEST_TYPE_NM]", map.get("REQUEST_TYPE_NM").toString());
					
				} else if(mailType.equals("PrivateIP-Aprv")) {		// 사설IP신청 > 반려 mailTemplate21.html	
					
					result = result.replace("[TITLE]", map.get("TITLE").toString());
					result = result.replace("[REQUEST_TYPE_NM]", map.get("REQUEST_TYPE_NM").toString());
					
					result = result.replace("[SSVC_LINE_TYPE_NM]", map.get("SSVC_LINE_TYPE_NM").toString());
					result = result.replace("[SSVC_GROUP_NM]", map.get("SSVC_GROUP_NM").toString());
					result = result.replace("[SSVC_OBJ_NM]", map.get("SSVC_OBJ_NM").toString());
					result = result.replace("[SIP_VERSION_TYPE_NM]", map.get("SIP_VERSION_TYPE_NM").toString());
					result = result.replace("[PIP_PREFIXS]", map.get("PIP_PREFIXS").toString());
					result = result.replace("[APPROVE_DT]", map.get("APPROVE_DT").toString());
					
				} else if(mailType.equals("PrivateIP-Reject")) {		// 사설IP신청 > 반려 mailTemplate22.html	

					result = result.replace("[TITLE]", map.get("TITLE").toString());
					result = result.replace("[REQUEST_TYPE_NM]", map.get("REQUEST_TYPE_NM").toString());
					
					result = result.replace("[SSVC_LINE_TYPE_NM]", map.get("SSVC_LINE_TYPE_NM").toString());
					result = result.replace("[SSVC_GROUP_NM]", map.get("SSVC_GROUP_NM").toString());
					result = result.replace("[SSVC_OBJ_NM]", map.get("SSVC_OBJ_NM").toString());
					result = result.replace("[SIP_VERSION_TYPE_NM]", map.get("SIP_VERSION_TYPE_NM").toString());
					result = result.replace("[PIP_PREFIXS]", map.get("PIP_PREFIXS").toString());
					result = result.replace("[REJECT_RSN]", map.get("REJECT_RSN").toString());
					result = result.replace("[APPROVE_DT]", map.get("APPROVE_DT").toString());
					
				} else if(mailType.equals("Grant-Insert")) {		// 권한 신청  mailTemplate23.html	

					result = result.replace("[GRANT_SEQ]", map.get("grantSeq").toString());
					result = result.replace("[SUSER_NM]", map.get("suserNm").toString());
					result = result.replace("[SPOS_DEPT_FULL_NM]", map.get("sposDeptFullNm").toString());
					SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
					Date date = transFormat.parse(map.get("dcreateDt").toString());
					String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
					result = result.replace("[DCREATE_DT]", dateToStr);
					result = result.replace("[NREQUEST_TYPE_NM]", map.get("nrequestTypeNm").toString());
					
				} else if(mailType.equals("Grant-Delete")) {		// 권한 신청  mailTemplate24.html	

					result = result.replace("[GRANT_SEQ]", map.get("grantSeq").toString());
					result = result.replace("[SUSER_NM]", map.get("suserNm").toString());
					result = result.replace("[SPOS_DEPT_FULL_NM]", map.get("sposDeptFullNm").toString());
					SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
					Date date = transFormat.parse(map.get("dcreateDt").toString());
					String dateToStr = DateFormatUtils.format(date, "yyyy-MM-dd");	
					result = result.replace("[DCREATE_DT]", dateToStr);
					result = result.replace("[NREQUEST_TYPE_NM]", map.get("nrequestTypeNm").toString());
					
				}
				
				 
			}
			PrintLogUtil.printLog("SMTP EMAIL RESULT ----------------");
//			PrintLogUtil.printLog(result);
//			PrintLogUtil.printLog("----------------");
			System.out.println(result);
			
		} catch (Exception e) {
			PrintLogUtil.printLog(e.toString());
			e.printStackTrace();
		} finally{
			fis.close();
		}
		
		return result;
	}

}








