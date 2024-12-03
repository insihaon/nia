package com.codej.web.cached;

public class RequestBodyHolder {
    private static final ThreadLocal<String> requestBodyThreadLocal = new ThreadLocal<>();

    public static void set(String body) {
        requestBodyThreadLocal.set(body);
    }

    public static String get() {
        return requestBodyThreadLocal.get();
    }

    public static void clear() {
        requestBodyThreadLocal.remove();
    }
}
