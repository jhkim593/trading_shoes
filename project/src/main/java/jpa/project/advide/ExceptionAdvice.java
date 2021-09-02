package jpa.project.advide;

import jpa.project.service.ResponseService;
import jpa.project.advide.exception.*;
import jpa.project.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    private final MessageSource messageSource;
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")),getMessage("unKnown.msg"));
//    }

    @ExceptionHandler(CLoginFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult loginFailureException(){
        return responseService.getFailResult(Integer.valueOf(getMessage("loginFail.code")),getMessage("loginFail.msg"));
    }
    @ExceptionHandler(CUserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userAlreadyExistException(){
        return responseService.getFailResult(Integer.valueOf(getMessage("alreadyUserExist.code")),getMessage("alreadyUserExist.msg"));
    }


    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult userNotFoundException(HttpServletRequest request,CUserNotFoundException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")),getMessage("userNotFound.msg"));

    }
    @ExceptionHandler(CUsernameSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected  CommonResult usernameSigninFailedException(HttpServletRequest request,CUsernameSigninFailedException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("usernameSigninFailed.code")),getMessage("usernameSigninFailed.msg"));
    }
    @ExceptionHandler(CResourceNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected  CommonResult resourceNotExistException(HttpServletRequest request,CResourceNotExistException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("resourceNotExist.code")),getMessage("resourceNotExist.msg"));
    }
    @ExceptionHandler(CNotOwnerException.class)
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    protected  CommonResult notOwnerException(HttpServletRequest request,CNotOwnerException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("notOwner.code")),getMessage("notOwner.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    protected CommonResult authenticationEntryPointException(HttpServletRequest request,CAuthenticationEntryPointException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")),getMessage("entryPointException.msg"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    protected CommonResult accessDeniedException(HttpServletRequest request,AccessDeniedException e){
            return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));

    }
    @ExceptionHandler(COrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult orderNotFound(HttpServletRequest request,AccessDeniedException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("orderNotFound.code")), getMessage("orderNotFound.msg"));

    }
    @ExceptionHandler(CNotEnoughStockException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult notEnoughStock(){
        return responseService.getFailResult(Integer.valueOf(getMessage("notEnoughStock.code")), getMessage("notEnoughStock.msg"));

    }
    @ExceptionHandler(CShoesAlreadyExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult shoesAlreadyExist(){
        return responseService.getFailResult(Integer.valueOf(getMessage("shoesAlreadyExist.code")), getMessage("shoesAlreadyExist.msg"));

    }
    @ExceptionHandler(CCommunicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult communicationException(HttpServletRequest request, CCommunicationException e){
        return responseService.getFailResult(Integer.valueOf(getMessage("communicationError.code")), getMessage("communicationError.msg"));
    }
//    @ExceptionHandler(CUserNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    protected CommonResult messageNotFoundException(HttpServletRequest request,CUserNotFoundException e){
//        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")),getMessage("userNotFound.msg"));
//
//    }




    private String getMessage(String code){
        return getMessage(code,null);
    }
    private String getMessage(String code, Object[] args){
        return messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
    }



}