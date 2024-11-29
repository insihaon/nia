package com.codej.web.cached;

import java.util.ArrayList;
import java.util.List;

public class SqlHolder {
    // ThreadLocal을 사용하여 쓰레드 안전한 리스트 저장소 생성
    private static final ThreadLocal<List<String>> sqlThreadLocal = ThreadLocal.withInitial(ArrayList::new);

    // SQL 문자열을 추가
    public static void add(String sql) {
        sqlThreadLocal.get().add(sql);
    }

    // 저장된 SQL 문자열 배열을 반환하고 리스트 초기화
    public static String[] pull() {
        List<String> sqlList = sqlThreadLocal.get();
        String[] sqlArray = sqlList.toArray(new String[0]);
        sqlList.clear();
        return sqlArray;
    }

    // 내부 리스트를 강제로 비움
    public static void clear() {
        sqlThreadLocal.get().clear();
    }
}