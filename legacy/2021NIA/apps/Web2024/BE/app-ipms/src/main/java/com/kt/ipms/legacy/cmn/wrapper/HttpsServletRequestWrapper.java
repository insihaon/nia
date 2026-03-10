package com.kt.ipms.legacy.cmn.wrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kt.framework.utils.StringUtils;


public class HttpsServletRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletResponse response = null;
	
	public HttpsServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public HttpSession getSession() {
		HttpSession session = super.getSession();
		setSessionCookie(session);
		return session;
	}

	@Override
	public HttpSession getSession(boolean create) {
		HttpSession session = super.getSession(create);
		setSessionCookie(session);
		return session;
	}
	
	private void setSessionCookie(HttpSession session) {
		if (session != null && response != null) {
			Object cookieOverWritten = getAttribute("COOKIE_OVERWRITTEN_FLAG");
			if (cookieOverWritten == null 
				&& isSecure() && isRequestedSessionIdFromCookie() && session.isNew()) {
				Cookie cookie = new Cookie("JSESSIONID", session.getId());
				cookie.setMaxAge(-1);
				String contextPath = getContextPath();
				if (StringUtils.hasText(contextPath)) {
					cookie.setPath(contextPath);
				} else {
					cookie.setPath("/");
				}
				cookie.setSecure(true); //Sparrow-SENSITIVE_COOKIE_IN_HTTPS_SESSION_WITHOUT_SECURE_ATTRIBUTE
				response.addCookie(cookie);
				setAttribute("COOKIE_OVERWRITTEN_FLAG", true);
			}
		}
	}
	

}
