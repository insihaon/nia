package com.kt.ipms.legacy.cmn.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kt.framework.exception.ServiceException;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class JackingProtectionFilter implements Filter {

	private final KTLogger logger = KTLoggerFactory.getLogger(JackingProtectionFilter.class);
	
	private String mode;
	
	private String excludeUrl;
	
	@Override
	public void destroy() {
		logger.info("JackingProtectionFilter DESTROY");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String path = ((HttpServletRequest) request).getRequestURI().toString();
		String urlPattern = this.excludeUrl.substring(this.excludeUrl.indexOf("/") , this.excludeUrl.lastIndexOf("/")+1);
		boolean isExclude = path.contains(urlPattern);
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		
		if (!isExclude) {
			logger.info("# JACKING PROTECTION FILTER START #");
			httpServletResponse.addHeader("X-FRAME-OPTIONS", mode);
			logger.info("# JACKING PROTECTION FILTER END #");
			chain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			logger.info("# REST API START #");
		
			String url = httpServletRequest.getRequestURI();
			RequestDispatcher disp = httpServletRequest.getRequestDispatcher(httpServletRequest.getContextPath() + url);	 
			disp.forward(request, response);
			
			logger.info("# REST API END #");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("JackingProtectionFilter INIT");
		mode = filterConfig.getInitParameter("mode");
		
		this.excludeUrl = filterConfig.getInitParameter("apiUrl");
	}
	
}
