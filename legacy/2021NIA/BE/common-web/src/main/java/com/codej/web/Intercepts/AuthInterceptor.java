package com.codej.web.Intercepts;


import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.codej.base.exception.CForbiddenRequestException;
import com.codej.base.utils.EncryptUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	public String getClientIp(HttpServletRequest request) {
		String[] headers = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_CLIENT_IP",
			"HTTP_X_FORWARDED_FOR",
			"X-Real-IP"
		};
	
		for (String header : headers) {
			String ip = request.getHeader(header);
			// log.debug("Header['{}']{}", header, ip);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip.split(",")[0].trim();  // 여러 IP가 있을 경우 첫 번째 IP 반환
			}
		}
	
		return request.getRemoteAddr();  // 헤더에서 IP를 찾지 못하면 기본 IP 반환
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
				
		log.debug("요청({}): {} => {}", getClientIp(request), request.getRequestURL(), handler.toString());
		try {
			if (handler instanceof HandlerMethod) {
				// HandlerMethod 는 후에 실행할 컨트롤러의 메소드 입니다.
				// 메소드명, 인스턴스, 타입, 사용된 Annotation 들을 확인할 수 있습니다.
				HandlerMethod method = (HandlerMethod) handler;
				log.info("handler method name : " + method.getMethod().getName());

				Object controller = method.getBean();
				Class<?> clazz = controller.getClass();
				Method thisMethod = clazz.getMethod("canAccess");
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

				if(controller instanceof com.codej.web.controller.BaseDataController) {
					long now = new Date().getTime();
					String ts = EncryptUtil.decryptText(request.getHeader("_t"));
					if(ts == null || Math.abs((now - Long.valueOf(ts)) / 1000) > 10) {
						throw new CForbiddenRequestException("출처가 확인되지 않은 요청입니다.");
					}
				}
			}
		} catch (CForbiddenRequestException e) {
            log.error(e.toString());
			throw e;
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