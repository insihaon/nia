package com.kt.ipms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 애너테이션이 런타임 시점에 유지되어야 AOP에서 인식 가능
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // 메서드에만 적용
public @interface EncryptResponse {
}