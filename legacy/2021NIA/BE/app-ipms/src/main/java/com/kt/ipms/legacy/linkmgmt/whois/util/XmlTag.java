package com.kt.ipms.legacy.linkmgmt.whois.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface XmlTag {

	String XmlName() default "";
	boolean IsList() default false;
	
}
