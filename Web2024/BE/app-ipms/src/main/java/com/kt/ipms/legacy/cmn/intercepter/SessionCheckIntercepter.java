package com.kt.ipms.legacy.cmn.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.JwtUtil;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;


public class SessionCheckIntercepter extends HandlerInterceptorAdapter {
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		boolean result = false;
		String rootPath = "/";
		String requestUrl = request.getRequestURL().toString();
//		requestUrl = requestUrl.substring(0, requestUrl.indexOf("jsessionid") - 1);
		String urlPattern = requestUrl.substring(requestUrl.lastIndexOf(".") + 1, requestUrl.length());
		boolean checkReferer = configPropertieService.getBoolean("Referer.isCheck");
		String allowUrl = configPropertieService.getString("Referer.url");
		String referedUrl = request.getHeader("referer");
		try {
			if (checkReferer && (referedUrl == null || (referedUrl.indexOf("http://" + allowUrl) == -1 && referedUrl.indexOf("https://" + allowUrl) == -1))) {
				jwtUtil.invalidSession(request);			
			}
			
			HttpSession session = request.getSession(false);
			if (session == null) {
				if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SESSION TIME OUT");
				} else {
					response.sendRedirect(rootPath);
				}
				return false;
			} else {
				LoginInfoVo sessionVo = (LoginInfoVo) session.getAttribute("user");
				if (sessionVo == null || !StringUtils.hasText(sessionVo.getSuserId())) {
					if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SESSION TIME OUT");
					} else {
						response.sendRedirect(rootPath);
					}
					return false;
				}
			}
			result = true;
		} catch (Exception e) {
			return false;
		}
		return result;
	}
	
	
}
