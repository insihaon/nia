package com.kt.ipms.legacy.cmn.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

public class ImagePaginationRenderer extends AbstractPaginationRenderer
		implements ServletContextAware {

	private ServletContext servletContext;
	
	public ImagePaginationRenderer() {
		
	}

	public void initVariables() {
		firstPageLabel		= "<li><a href=\"?pageIndex={1}\" onclick=\"{0}({1});return false; \"><img src=\"" + servletContext.getContextPath() +  "/resources/images/content/btn_page_first_off.gif\" alt=\"처음페이지\" onmouseover=\"menuOver(this);\" onmouseout=\"menuOut(this);\" /></a></li>";
		previousPageLabel	= "<li><a href=\"?pageIndex={1}\" onclick=\"{0}({1});return false; \"><img src=\"" + servletContext.getContextPath() +  "/resources/images/content/btn_page_prev_off.gif\" alt=\"이전페이지\" onmouseover=\"menuOver(this);\" onmouseout=\"menuOut(this);\" /></a></li>";
		currentPageLabel	= "<li class=\"on\"><strong>{0}</strong></li>";
		otherPageLabel		= "<li><a href=\"?pageIndex={1}\" onclick=\"{0}({1});return false; \">{2}</a></li>";
		nextPageLabel		= "<li><a href=\"?pageIndex={1}\" onclick=\"{0}({1});return false; \"><img src=\"" + servletContext.getContextPath() +  "/resources/images/content/btn_page_next_off.gif\" alt=\"다음페이지\"  onmouseover=\"menuOver(this);\" onmouseout=\"menuOut(this);\" /></a></li>";
		lastPageLabel		= "<li><a href=\"?pageIndex={1}\" onclick=\"{0}({1});return false; \"><img src=\"" + servletContext.getContextPath() +  "/resources/images/content/btn_page_last_off.gif\" alt=\"마지막페이지\" onmouseover=\"menuOver(this);\" onmouseout=\"menuOut(this);\"  /></a></li>";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}

}
