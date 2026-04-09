package com.codej.web.bean;

import org.springframework.stereotype.Component;

@Component
public class Receiver {
    /**
     * Redis로부터 메시지를 수신했을 때 호출되는 메서드
     * @param message Redis에서 전달된 문자열 메시지
     */
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        // TODO: 실제 비즈니스 로직 처리
    }
}