package com.codej.web.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * A 서버: 세션을 주는 서버, 로그인 인증을 한 서버
 * B 서버: 세션을 받는 서버, 현재 컨트롤러가 있는 서버, 세션을 동기화 하려는 서버, 세션을 유지한 채 서비스를 이용하려는 서버
 * 
 *  A WAS서버와 B WAS서버는 session 을 redis에 의해 공유하게 구성되어있다
 *  A WAS서버와 B WAS서버는 다른 서비스를 한다 
 *  A 서버에서 B 서버에 접속하는 URL 로 리다이렉트 링크를 만들고 세션정보를 넘기는 방법은?
 *  B 서버 서비스에 인증 과정을 생략하고 A 서버 서비스에 사용되었던 세션 그대로 B 서버 서비스를 이용하도록 하는 방법
 *  
 *  요약하면 A 서비스의 세션을 그래도 B서비스에서 계속 같은 세션으로 서비스를 이용하는 방법은?
 */

@Controller
@ConditionalOnExpression("'${spring.redis.enabled:true}' == 'true'")
@ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "redis") 
public class SessionController {

    // Redis에 저장된 세션을 관리하는 Repository를 주입합니다.
    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    /**
     * A 서버에서 전달받은 sid 파라미터를 이용하여 Redis 세션 정보를 현재 세션에 적용합니다.
     *
     * @param sid      A 서버에서 전달된 세션 ID
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @return auto-login 성공 여부 메시지
     */
    @GetMapping("/ss")
    @ResponseBody
    public Map<String,Object> syncSession(@RequestParam("sid") String sid, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> resultMap = new LinkedHashMap<String, Object>();
        // Redis에서 sid에 해당하는 세션 정보를 조회합니다.
        // 만료된 세션은 허용하지 않으므로 두 번째 인자로 false 전달
        Session redisSession = sessionRepository.findById(sid);
        if (redisSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resultMap.put("resultCd", "ERROR");
            resultMap.put("resultMsg", "유효하지 않은 세션입니다.");
            return resultMap;
        }

        // 현재 사용자의 세션을 가져오거나 없으면 새로 생성합니다.
        HttpSession currentSession = request.getSession();

        // 기존 세션의 모든 속성을 제거합니다.
        Enumeration<String> attributeNames = currentSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attrName = attributeNames.nextElement();
            currentSession.removeAttribute(attrName);
        }

        // Redis에서 조회한 세션의 모든 속성을 현재 세션에 복사합니다.
        for (String attrName : redisSession.getAttributeNames()) {
            Object value = redisSession.getAttribute(attrName);
            currentSession.setAttribute(attrName, value);
        }

        // 현재 세션의 JSESSIONID 쿠키를 Redis에서 조회한 sid로 재설정합니다.
        Cookie sessionCookie = new Cookie("JSESSIONID", sid);
        sessionCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        sessionCookie.setHttpOnly(true);
        // HTTPS 사용 시 sessionCookie.setSecure(true); 를 활성화하세요.
        response.addCookie(sessionCookie);

        resultMap.put("resultCd", "SUCCESS");
        resultMap.put("resultMsg", "공유 세션 적용 성공. Redis 세션 정보로 업데이트되었습니다.");
        return resultMap;
    }
}
