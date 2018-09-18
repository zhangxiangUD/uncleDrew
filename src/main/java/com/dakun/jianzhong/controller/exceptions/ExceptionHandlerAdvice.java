package com.dakun.jianzhong.controller.exceptions;

import com.dakun.jianzhong.web.exceptions.BaseExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * <p>User: wangjie
 * <p>Date: 11/3/2017
 * @author wangjie
 */
@ControllerAdvice
@Order(0)
public class ExceptionHandlerAdvice extends BaseExceptionHandler {

}
