package com.zxk175.well.config.exception;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.HttpMsg;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.dto.ErrorDTO;
import com.zxk175.well.common.util.ExceptionUtil;
import com.zxk175.well.common.util.net.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理
 *
 * @author zxk175
 * @since 2019/03/23 18:13
 */
@Slf4j
@Order(-1000)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        buildExceptionInfo(ex, "请求方式处理不支持");
        final HttpServletRequest request = RequestUtil.request();
        return String.format("Cannot %s %s", request.getMethod(), RequestUtil.requestUrl(request, true));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response handleNoHandlerFoundException(NoHandlerFoundException ex) {
        buildExceptionInfo(ex, "请求地址不存在");
        String msg = "请求地址不存在：" + RequestUtil.requestUrl(true, false);
        return Response.fail(Const.FAIL_CODE, msg);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        ErrorDTO errorDTO;
        List<ErrorDTO> errors = Lists.newArrayListWithCapacity(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            errorDTO = new ErrorDTO()
                    .setField(fieldError.getField())
                    .setMessage(fieldError.getDefaultMessage())
                    .setRejectedValue(fieldError.getRejectedValue());

            errors.add(errorDTO);
        }

        Map<Object, Object> data = Maps.newHashMap();
        data.put("errors", errors);

        buildExceptionInfo(ex, "bean参数校验异常");
        return Response.fail(HttpMsg.PARAM_NOT_COMPLETE, data);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> it = violations.iterator();

        ErrorDTO errorDTO;
        List<ErrorDTO> errors = Lists.newLinkedList();
        while (it.hasNext()) {
            ConstraintViolation<?> violation = it.next();
            PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
            NodeImpl leafNode = propertyPath.getLeafNode();
            int parameterIndex = leafNode.getParameterIndex();

            errorDTO = new ErrorDTO()
                    .setField(leafNode.getName())
                    .setMessage(violation.getMessage())
                    .setRejectedValue(violation.getInvalidValue())
                    .setIndex(parameterIndex);

            errors.add(errorDTO);
        }

        // 排序从小到大
        errors.sort(Comparator.comparingInt(ErrorDTO::getIndex));

        Map<Object, Object> data = Maps.newHashMap();
        data.put("errors", errors);

        buildExceptionInfo(ex, "单个参数校验异常");
        return Response.fail(HttpMsg.PARAM_NOT_COMPLETE, data);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception ex) {
        ex.printStackTrace();
        log.error("未知异常", ex);
        return buildExceptionInfo(ex, "未知异常");
    }

    private Response buildExceptionInfo(Exception ex, String title) {
        return ExceptionUtil.buildExceptionInfo(ex, title);
    }
}
