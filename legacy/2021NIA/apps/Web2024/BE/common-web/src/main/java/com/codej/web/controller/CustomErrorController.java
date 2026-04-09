package com.codej.web.controller;

import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.codej.base.dto.AppDto;
import com.codej.base.exception.CServiceException;

@Controller
@ConditionalOnExpression("'${spring.thymeleaf.use:true}' == 'true'")
public class CustomErrorController implements ErrorController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AppDto appDto;

	@RequestMapping("/error")
	public ModelAndView error(@RequestParam HashMap<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws CServiceException, Exception {
		String url = appDto.getErrorRedirect();
		if (url.length() > 0) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(url);
			return new ModelAndView(redirectView);
		}

		if ("aam".equals(appDto.getProject()) == false) {
			return new ModelAndView("error/index");
		}

		int status = Integer.parseInt(String.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)));

		if (status == 404) {
			log.info("[deploy] thymeleaf: param={},request={},response={}", param, request, response);	
			return new ModelAndView("thymeleaf/index");
		} else {
			log.debug("CustomErrorControl ler[error] : " + status);
			throw new CServiceException(String.valueOf(status));
		}
	}
}