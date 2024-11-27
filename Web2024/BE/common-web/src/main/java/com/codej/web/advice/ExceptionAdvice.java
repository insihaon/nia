package com.codej.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codej.base.dto.response.BaseResponse;
import com.codej.base.exception.CBaseException;
import com.codej.web.service.ResponseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    private static String getStackTrace(Exception e) {
        // System.out.println("Exception Type: " + e.getClass().getName());
        // System.out.println("Message: " + e.getMessage());

        // // 스택 추적 정보 출력
        StringBuffer sb = new StringBuffer(" StackTrace: ");
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (int i = 0; i < Math.min(5, stackTrace.length); i++) {
            StackTraceElement element = stackTrace[i];
            sb.append(String.format("%s:%d <- ", element.getFileName(), element.getLineNumber()));
        }

        return sb.toString();

        // 전체 스택 추적을 문자열로 출력 (선택적)
        // StringWriter sw = new StringWriter();
        // e.printStackTrace(new PrintWriter(sw));
        // String fullStackTrace = sw.toString();
        // return fullStackTrace;
    }

  

    @ExceptionHandler(CBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse untrustedRequestException(HttpServletRequest request, CBaseException e) {
        Integer code = Integer.valueOf(getMessage(e.getCodeKey()));
        String msg = getMessage(e.getMessageKey(), new Object[]{e.getMessage()});
        String detail = e.getMessage() != null ? e.getMessage() : getMessage(e.getDetailMessageKey()) + getStackTrace(e);
        return responseService.createFailResponse(code, msg, detail);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        Integer code = Integer.valueOf(getMessage("unKnown.code"));
        String msg = getMessage("unKnown.message");
        String detail = e.getMessage() + getStackTrace(e);
        return responseService.createFailResponse(code, msg, detail);
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        Integer code = Integer.valueOf(getMessage("accessDenied.code"));
        String msg = getMessage("accessDenied.message");
        String detail = e.getMessage() + getStackTrace(e);
        return responseService.createFailResponse(code, msg, detail);
    }

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
