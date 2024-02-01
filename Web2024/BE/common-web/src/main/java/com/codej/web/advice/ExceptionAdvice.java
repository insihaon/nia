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
import com.codej.base.exception.CAuthenticationException;
import com.codej.base.exception.CCommunicationException;
import com.codej.base.exception.CForbiddenRequestException;
import com.codej.base.exception.CHttpRelayServiceFail;
import com.codej.base.exception.CMybatisException;
import com.codej.base.exception.CNotOwnerException;
import com.codej.base.exception.CResourceNotExistException;
import com.codej.base.exception.CServiceException;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CServiceNotImplement;
import com.codej.base.exception.CSigninFailedException;
import com.codej.base.exception.CUnknowException;
import com.codej.base.exception.CUserExistException;
import com.codej.base.exception.CUserNotFoundException;
import com.codej.web.service.ResponseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.createFailResponse(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"), e.getMessage());
    }

    @ExceptionHandler(CUnknowException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse unknowException(HttpServletRequest request, CUnknowException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"), e.getMessage());
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.message"), e.getMessage());
    }

    @ExceptionHandler(CSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse emailSigninFailed(HttpServletRequest request, CSigninFailedException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.message"), e.getMessage());
    }

    @ExceptionHandler(CAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse authenticationEntryPointException(HttpServletRequest request, CAuthenticationException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.message"), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.message"), e.getMessage());
    }

    @ExceptionHandler(CCommunicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse communicationException(HttpServletRequest request, CCommunicationException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("communicationError.code")), getMessage("communicationError.message"), e.getMessage());
    }

    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse communicationException(HttpServletRequest request, CUserExistException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("existingUser.code")), getMessage("existingUser.message"), e.getMessage());
    }

    @ExceptionHandler(CNotOwnerException.class)
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public BaseResponse notOwnerException(HttpServletRequest request, CNotOwnerException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("notOwner.code")), getMessage("notOwner.message"), e.getMessage());
    }

    @ExceptionHandler(CResourceNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse resourceNotExistException(HttpServletRequest request, CResourceNotExistException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("resourceNotExist.code")), getMessage("resourceNotExist.message"), e.getMessage());
    }

    @ExceptionHandler(CForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse forbiddenRequestException(HttpServletRequest request, CForbiddenRequestException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("forbiddenRequest.code")), getMessage("forbiddenRequest.message", new Object[]{e.getMessage()}));
    }

    @ExceptionHandler(CServiceNotImplement.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public BaseResponse serviceNotImplementException(HttpServletRequest request, CServiceNotImplement e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("serviceNotImplement.code")), getMessage("serviceNotImplement.message"), e.getMessage());
    }

    @ExceptionHandler(CServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse serviceException(HttpServletRequest request, CServiceException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("serviceException.code")), getMessage("serviceException.message"), e.getMessage());
    }

    @ExceptionHandler(CServiceIncorrectUse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse serviceIncorrectUseException(HttpServletRequest request, CServiceIncorrectUse e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("serviceIncorrectUse.code")), getMessage("serviceIncorrectUse.message"), e.getMessage());
    }

    @ExceptionHandler(CHttpRelayServiceFail.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public BaseResponse httpRelayServiceFailException(HttpServletRequest request, CHttpRelayServiceFail e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("httpRelayServiceFail.code")), getMessage("httpRelayServiceFail.message"), e.getDetailMessage());
    }

    @ExceptionHandler(CMybatisException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse mybatisException(HttpServletRequest request, CMybatisException e) {
        return responseService.createFailResponse(Integer.valueOf(getMessage("mybatisException.code")), getMessage("mybatisException.message"), e.getMessage());
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
