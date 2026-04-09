package com.ipsdn_opt.app.component;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class Interceptor implements HandlerInterceptor  {
    private final ObjectMapper objectMapper;

    // controller로 보내기 전에 처리하는 인터셉터
	// 반환이 false라면 controller로 요청을 안함
	// 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler )
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
    // controller의 handler가 끝나면 처리됨
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
	}

	// view까지 처리가 끝난 후에 처리됨
	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) return;

        Principal principal = request.getUserPrincipal();

        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
        if (cachingRequest.getContentType() != null && cachingRequest.getContentType().contains("application/json")) {
            if (cachingRequest.getContentAsByteArray() != null && cachingRequest.getContentAsByteArray().length != 0) {
                String requestBody = objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString();
            }
        }
        if (cachingResponse.getContentType() != null && cachingResponse.getContentType().contains("application/json")) {
            if (cachingResponse.getContentAsByteArray() != null && cachingResponse.getContentAsByteArray().length != 0) {

            }
        }
    }

}
