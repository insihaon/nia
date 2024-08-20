package com.kt.ipms.legacy.cmn.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.SessionUtil;

public class UserAutorityCheckInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SessionUtil sessionUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean isAuthority = true;
		String rootPath = "/";
		String requestUrl = request.getRequestURL().toString();
		String urlPattern = requestUrl.substring(requestUrl.lastIndexOf(".") + 1, requestUrl.length());
		try {
			String userGradeCd = sessionUtil.getUserGradeCd(request);
			if (!CommonCodeUtil.USER_GRADE_A.equals(userGradeCd)) {
				sessionUtil.invalidSession(request);
			}
			HttpSession session = request.getSession(false);
			if (session == null) {
				if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
				} else {
					response.sendRedirect(rootPath);
				}
				isAuthority = false;
			} 
		} catch (Exception e) {
			isAuthority = false;
		}
		return isAuthority;
	}

}
