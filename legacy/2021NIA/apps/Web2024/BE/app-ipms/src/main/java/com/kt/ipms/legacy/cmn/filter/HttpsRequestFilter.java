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

import com.kt.ipms.legacy.cmn.wrapper.HttpsServletRequestWrapper;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class HttpsRequestFilter implements Filter {

	private final KTLogger logger = KTLoggerFactory.getLogger(HttpsRequestFilter.class);
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.info("HttpsRequestFilter INIT");
	}
	
	@Override
	public void destroy() {
		logger.info("HttpsRequestFilter DESTROY");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("# HTTPS WRAPPER FILTER START #");
		HttpsServletRequestWrapper httpsServletRequestWrapper = new HttpsServletRequestWrapper((HttpServletRequest) request);
		httpsServletRequestWrapper.setResponse((HttpServletResponse) response);
		logger.info("# HTTPS WRAPPER FILTER END #");
		chain.doFilter(httpsServletRequestWrapper, response);
	}

	

}
