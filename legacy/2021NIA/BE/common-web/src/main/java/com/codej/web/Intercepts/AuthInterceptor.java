package com.codej.web.Intercepts;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.codej.base.controller.BaseController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("controller call: {} => {}", request.getRequestURL(), handler.toString());
		try {
			if (handler instanceof HandlerMethod) {
				// HandlerMethod 는 후에 실행할 컨트롤러의 메소드 입니다.
				// 메소드명, 인스턴스, 타입, 사용된 Annotation 들을 확인할 수 있습니다.
				HandlerMethod method = (HandlerMethod) handler;
				log.info("handler method name : " + method.getMethod().getName());

				Object controller = method.getBean();
				Method thisMethod = controller.getClass().getMethod("canAccess");
				Boolean canAccess = (Boolean) thisMethod.invoke(controller);
				if (canAccess != true) {
					HttpServletRequest httpRequest = (HttpServletRequest)request;
					HttpServletResponse httpResponse = (HttpServletResponse)response;
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					httpResponse.setContentType("application/json");
					httpResponse.setCharacterEncoding("UTF-8");

					JSONObject json = new JSONObject();
					json.put("code", 401);
					json.put("message","Invalid token");

					httpResponse.getWriter().write(json.toString());
					return false;
				}
			}
		} catch (Exception e) {
            log.info("권한 처리중 오류 발생 : {}, {}", handler.toString(), e.toString());
		}
        
        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.debug( "controller exit", handler.toString());
	}


}