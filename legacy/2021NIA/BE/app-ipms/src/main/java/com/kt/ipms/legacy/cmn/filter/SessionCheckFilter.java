package com.kt.ipms.legacy.cmn.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class SessionCheckFilter implements Filter {

	private final KTLogger logger = KTLoggerFactory.getLogger(SessionCheckFilter.class);
	
	private List<String> avoidUrlList;
	
	@Override
	public void destroy() {
		logger.info("SessionCheckFilter DESTROY");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("# SESSION CHECK FILTER START #");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestURL = httpServletRequest.getRequestURL().toString();
		String urlPattern = requestURL.substring(requestURL.lastIndexOf(".") + 1, requestURL.length());
		try {
			boolean isPass = false;
			logger.debug("## REQUEST URL : " + requestURL + ", URL PATTERN : " + urlPattern);

			for (String avoidUrl : avoidUrlList) {
				if (requestURL.contains(avoidUrl)) {
					isPass = true;
					break;
				}
			}
			if (!isPass) {
				logger.debug("## SESSION CHECK START");
				HttpSession session = httpServletRequest.getSession(false);
				if (session == null) {
					logger.info("### SESSION IS NULL");
					if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
						httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SESSION TIME OUT");
					} else {
						httpServletResponse.sendRedirect("/");
					}
					return;
				} else {
					LoginInfoVo sessionVo = (LoginInfoVo) session.getAttribute("user");
					if (sessionVo == null || !StringUtils.hasText(sessionVo.getSuserId())) {
						logger.info("### SESSION VO IS NULL OR USERID IS NULL");
						if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
							httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SESSION TIME OUT");
						} else {
							httpServletResponse.sendRedirect("/");
						}
						return;
					}
				}
				logger.debug("## SESSION CHECK END");
			}
		} catch (Exception e) {
			logger.info("### SESSION CHECK EXCEPTION");
			if (urlPattern.equals("ajax") || urlPattern.equals("json")) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SESSION TIME OUT");
			} else {
				httpServletResponse.sendRedirect("/");
			}
			return;
		} finally {
			logger.info("# SESSION CHECK FILTER END #");
		}
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("SessionCheckFilter INIT");
		String urls = filterConfig.getInitParameter("avoidUrls");
		logger.info("# AVOID URLS : " + urls);
		StringTokenizer st = new StringTokenizer(urls, ",");
		avoidUrlList = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			avoidUrlList.add(st.nextToken());
		}
	}

}
