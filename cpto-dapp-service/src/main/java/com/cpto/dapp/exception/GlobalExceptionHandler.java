package com.cpto.dapp.exception;

import com.cpto.dapp.constant.ErrorConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author sunli
 * @date 2018/12/07
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 业务异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity handleBusinessException(BusinessException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({ParameterException.class})
    public ResponseEntity handleParameterException(ParameterException e) {
        log.warn(ErrorConstant.PARAMETER, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 登录异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({LoginException.class})
    public ResponseEntity handleLoginException(LoginException e) {
        log.warn(ErrorConstant.LOGIN, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 权限异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({AuthorizedException.class})
    public ResponseEntity handleAuthorizedException(AuthorizedException e) {
        log.warn(ErrorConstant.AUTHORIZED, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.FORBIDDEN);
    }

    /**
     * 支付密码未设置异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({PayPasswordNotSetException.class})
    public ResponseEntity handlePayPasswordNotSetException(PayPasswordNotSetException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 支付密码异常处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({PayPasswordErrorException.class})
    public ResponseEntity handlePayAuthException(PayPasswordErrorException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 验证码错误异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({VerificationCodeException.class})
    public ResponseEntity handleVerificationCodeException(VerificationCodeException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 验证码失效异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({VerificationCodeInvalidException.class})
    public ResponseEntity handleVerificationCodeInvalidException(VerificationCodeInvalidException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 验证码失效异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({RequestOverLimitException.class})
    public ResponseEntity handleRequestLimitException(RequestOverLimitException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * 数据已存在异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({DataExistedException.class})
    public ResponseEntity handleDataExistedException(DataExistedException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.CONFLICT);
    }

    /**
     * 数据已被修改异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({DataExpiredException.class})
    public ResponseEntity handleDataExpiredException(DataExpiredException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.CONFLICT);
    }

    /**
     * 数据未找到处理
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity handleDataNotFoundException(DataNotFoundException e) {
        log.warn(ErrorConstant.BUSINESS, e);
        return new ResponseEntity(e.getErrorInfo(), HttpStatus.NOT_FOUND);
    }

    /**
     * 接口请求参数异常
     *
     * @param e 异常类型
     * @return 响应结果
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        log.error(ErrorConstant.PARAMETER, e);
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 接口请求参数异常
     * 重写父类方法，捕获MethodArgumentNotValidException
     *
     * @param ex      异常类型
     * @param headers 响应头信息
     * @param status  响应状态码
     * @param request 请求信息
     * @return 响应结果
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // 设置错误信息
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        String errorMessage;
        for (FieldError fieldError : fieldErrors) {
            errorMessage = fieldError.getField().concat(":").concat(fieldError.getDefaultMessage());
            errorMessages.add(errorMessage);
        }

        return this.handleExceptionInternal(ex, errorMessages, headers, status, request);
    }
}