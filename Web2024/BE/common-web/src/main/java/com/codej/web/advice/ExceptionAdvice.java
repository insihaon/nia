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

    @ExceptionHandler(CBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse untrustedRequestException(HttpServletRequest request, CBaseException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage(e.getCodeKey())), getMessage(e.getMessageKey(), new Object[]{e.getMessage()}), e.getMessage() != null ? e.getMessage() : getMessage(e.getDetailMessageKey()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.createFailResponse(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"), e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.message"), e.getMessage());
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
