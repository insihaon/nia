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

import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class DomainCheckFilter implements Filter {
	
	private final KTLogger logger = KTLoggerFactory.getLogger(DomainCheckFilter.class);
	
	@Override
	public void destroy() {
		logger.info("DomainCheckFilter DESTROY");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("# DOMAIN CHECK FILTER START #");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		try {
			String referer = httpServletRequest.getHeader("REFERER");
			String requestURL = httpServletRequest.getRequestURL().toString();
			String remoteADDR = httpServletRequest.getRemoteAddr();
			String remoteHOST = httpServletRequest.getRemoteHost();
			String localADDR = httpServletRequest.getLocalAddr();

			if(requestURL.contains("localhost") && referer == null) {
				referer = "http://localhost:8080/";
			}

			logger.debug("## REFERER : " + referer);
			logger.debug("## REQUEST URL : " + requestURL);
			logger.debug("## REMOTE ADDR : " + remoteADDR);
			logger.debug("## REMOTE HOST : " + remoteHOST);
			logger.debug("## LOCAL ADDR : " + localADDR);
			if (referer != null) {
				int startIdx = referer.indexOf("//") + 2;
				int endIdx = referer.indexOf("/", startIdx);
				String refererSite = referer.substring(startIdx, endIdx);
				if (refererSite.indexOf(":") > 0) {
					refererSite = refererSite.substring(0, refererSite.indexOf(":"));
				}
				
				startIdx = requestURL.indexOf("//") + 2;
				endIdx = requestURL.indexOf("/", startIdx);
				String site = requestURL.substring(startIdx, endIdx);
				if (site.indexOf(":") > 0) {
					site = site.substring(0, site.indexOf(":"));
				}
				logger.info("## REFERER DOMAIN : " + refererSite + ", REQUEST DOMAIN : " + site);
				if (!site.equals(refererSite)) {
					logger.info("### UNAUTHORIZED DOMAIN(REFERER != REQUEST) SESSION INVALID");
					invalidSession(httpServletRequest);
				}
			} else {
				logger.info("### UNAUTHORIZED DOMAIN(REFERER IS NULL) SESSION INVALID");
				invalidSession(httpServletRequest);
			}
		} catch (Exception e) {
			logger.info("### DOMAIN CHECK EXCEPTION SESSION INVALID");
			invalidSession(httpServletRequest);
		} finally {
			logger.info("# DOMAIN CHECK FILTER END #");
		}
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("DomainCheckFilter INIT");
	}
	
	private void invalidSession(HttpServletRequest request)  {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("user");
			session.invalidate();
		}
	}

}
