package com.codej.base.dto.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ToStringFormatter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String toString() {
        return toString(this, 0);
    }
    private String toString(Object obj, int indentLevel) {
        if (obj == null) {
            return "null";
        }

        // 기본 타입 또는 문자열
        if (obj instanceof Number || obj instanceof Boolean || obj instanceof Character || obj instanceof String) {
            return obj.toString();
        }

        // 배열, 컬렉션, 맵 처리
        if (obj.getClass().isArray()) {
            return arrayToString(obj, indentLevel);
        }
        if (obj instanceof Collection) {
            return collectionToString((Collection<?>) obj, indentLevel);
        }
        if (obj instanceof Map) {
            return mapToString((Map<?, ?>) obj, indentLevel);
        }

        // 일반 객체 처리
        return convertToJson(this);
    }

    private String convertToJson(Object obj) {
        try {
            return String.format("%s %s", obj.getClass().getSimpleName(), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (Exception e) {
            StringBuilder result = new StringBuilder();
            String indent = createIndent(0);
            result.append(obj.getClass().getSimpleName()).append(" {").append(System.lineSeparator());

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                result.append(indent).append("  ").append(field.getName()).append(": ");
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    result.append(value);
                } catch (IllegalAccessException e2) {
                    result.append("(access denied)");
                }
                result.append(System.lineSeparator());
            }
            result.append(indent).append("}");
            return result.toString();
        }
    }

    private String arrayToString(Object array, int indentLevel) {
        if (array == null) return "null";
        int length = java.lang.reflect.Array.getLength(array);
        if (length == 0) return "0건 []";

        String indent = createIndent(indentLevel);
        String nextIndent = createIndent(indentLevel + 1);
        StringBuilder sb = new StringBuilder();

        sb.append(length).append("건 [").append(System.lineSeparator());
        int count = Math.min(2, length);
        for (int i = 0; i < count; i++) {
            sb.append(nextIndent)
                    .append(toString(java.lang.reflect.Array.get(array, i), indentLevel + 1))
                    .append(",").append(System.lineSeparator());
        }
        if (length > 2) {
            sb.append(nextIndent).append("...").append(System.lineSeparator());
        }
        sb.append(indent).append("]");
        return sb.toString();
    }

    private String collectionToString(Collection<?> collection, int indentLevel) {
        if (collection == null) return "null";
        if (collection.isEmpty()) return "0건 []";

        String indent = createIndent(indentLevel);
        String nextIndent = createIndent(indentLevel + 1);
        StringBuilder sb = new StringBuilder();

        sb.append(collection.size()).append("건 [").append(System.lineSeparator());
        int count = 0;
        for (Object item : collection) {
            if (count >= 2) break;
            sb.append(nextIndent)
                    .append(toString(item, indentLevel + 1))
                    .append(",").append(System.lineSeparator());
            count++;
        }
        if (collection.size() > 2) {
            sb.append(nextIndent).append("...").append(System.lineSeparator());
        }
        sb.append(indent).append("]");
        return sb.toString();
    }

    private String mapToString(Map<?, ?> map, int indentLevel) {
        if (map == null) return "null";
        if (map.isEmpty()) return "{}";

        String indent = createIndent(indentLevel);
        String nextIndent = createIndent(indentLevel + 1);
        StringBuilder sb = new StringBuilder();

        sb.append("{").append(System.lineSeparator());
        Map<?, ?> orderedMap = (map instanceof LinkedHashMap) ? map : new LinkedHashMap<>(map);

        boolean isFirst = true;
        for (Map.Entry<?, ?> entry : orderedMap.entrySet()) {
            if (!isFirst) sb.append(",").append(System.lineSeparator());
            sb.append(nextIndent)
                    .append("\"").append(entry.getKey()).append("\": ")
                    .append(toString(entry.getValue(), indentLevel + 1));
            isFirst = false;
        }
        sb.append(System.lineSeparator()).append(indent).append("}");
        return sb.toString();
    }

    // 들여쓰기 생성
    private String createIndent(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }
        return indent.toString();
    }
}
