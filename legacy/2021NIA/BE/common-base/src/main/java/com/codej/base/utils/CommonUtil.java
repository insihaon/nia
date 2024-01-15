package com.codej.base.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

    private CommonUtil() {
    }

    public static String format(String in_s, Object... var2) {
        String s = in_s;
        int i = 0;
        while (s.contains("{}")) {
            s = s.replaceFirst(Pattern.quote("{}"), "{" + i++ + "}");
        }
        return MessageFormat.format(s, var2);
    }

    public static String getStackTrace(String in_format) {
        String format = in_format;
        if (format == null) {
            format = "[$FILE:$LINE]";
        }
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String fullClassName = ste.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = ste.getMethodName();
        int lineNumber = ste.getLineNumber();
        format = format
                .replaceAll("\\$FILE", ste.getFileName())
                .replaceAll("\\$LINE", String.valueOf(lineNumber))
                .replaceAll("\\$METHOD", methodName)
                .replaceAll("\\$CLASS", className)
                .replaceAll("\\$FULLCLASS", fullClassName);

        return String.format(format, ste.getFileName(), ste.getLineNumber());
    }

    public static String mapToString(HashMap<String, Object> dataMap, int limit) {
        StringBuilder message = new StringBuilder("\n\n");
        for (String key : dataMap.keySet()) {
            Object value = dataMap.get(key);
            if (limit > 0) {
                if (value instanceof String) {
                    int length = ((String) value).length();
                    if (length > limit) {
                        value = ((String) value).substring(0, limit) + " ... more ...";
                    }
                }
            }
            message.append(key).append(": ").append(value).append("\n");
        }
        return message.toString();
    }
}
