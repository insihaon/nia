package com.kt.ipms.legacy.cmn.intercepter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kt.ipms.legacy.cmn.service.ExcelCheckService;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;

public class ExcelCheckInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private ExcelCheckService excelCheckService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ParseException {
		Date now = new Date();
		boolean result = false;
		String requestUrl = request.getRequestURL().toString();
		String urlPattern = requestUrl.substring(requestUrl.lastIndexOf(".") + 1, requestUrl.length());
		PrintLogUtil.printLog("interceptor now = "+now.toString());
		if(requestUrl.contains("Excel.json")){
			String excel_down_flag = excelCheckService.selectIpExcelDownCheck();
			if(excel_down_flag.equals("N")){
				excelCheckService.updateExcelDown("Y");
				result = true;
			}else{
				String dmodify_dt = excelCheckService.selectModifyDt();
				String  format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
				now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format1);
				Date format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dmodify_dt);
				int diffMin = (int) ((now.getTime() - format2.getTime()) / 60000);
				if(diffMin > 10){
					result = true;
				}else{
					response.setCharacterEncoding("utf-8");
					response.getWriter().write("{\"commonMsg\":\"진행중인 엑셀작업이 있습니다. 다음에 다시 시도해 주십시오.\"}");
				}
			}
		}else if(requestUrl.contains("upload.ajax")){
			String excel_up_flag = excelCheckService.selectIpExcelUpCheck();
			if(excel_up_flag.equals("N")){
				result = true;
			}else{
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"resultListVo\":{\"commonMsg\":\"진행중인 업로드작업이 있습니다. 다음에 다시 시도해 주십시오.\"}}");
			}
		}
		return result;
	}
}
