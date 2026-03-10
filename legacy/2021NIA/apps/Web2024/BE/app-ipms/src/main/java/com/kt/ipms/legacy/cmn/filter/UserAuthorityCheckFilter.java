package com.kt.ipms.legacy.cmn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class UserAuthorityCheckFilter implements Filter {

	private final KTLogger logger = KTLoggerFactory.getLogger(UserAuthorityCheckFilter.class);
	
	@Override
	public void destroy() {
		logger.info("UserAuthorityCheckFilter DESTROY");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("# USER AUTHORITY CHECK FILTER START");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		try {
			String userGradeCd = getUserGradeCd(httpServletRequest);
			if (!CommonCodeUtil.USER_GRADE_A.equals(userGradeCd)) {
				logger.debug("## USER UNAUTHORIZED");
				invalidSession(httpServletRequest);
			}
		} catch (Exception e) {
			logger.info("# USER AUTHORITY CHECK EXCEPTION");
			invalidSession(httpServletRequest);
		} finally {
			logger.info("# USER AUTHORITY CHECK FILTER END");
		}
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("UserAuthorityCheckFilter INIT");
		
//		ServletContext servletContext = filterConfig.getServletContext();
//		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
//		sessionUtil = autowireCapableBeanFactory.getBean(jwtUtil.class);
	}
	
	private void invalidSession(HttpServletRequest request)  {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("user");
			session.invalidate();
		}
	}
	
	private String getUserGradeCd(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String userGradeCd = null;
		if (session != null) {
			LoginInfoVo sessionVO = (LoginInfoVo) session.getAttribute("user");
			if (sessionVO != null) {
				userGradeCd = sessionVO.getSuserGradeCd();
			}
		}
		return userGradeCd;
	}

}
