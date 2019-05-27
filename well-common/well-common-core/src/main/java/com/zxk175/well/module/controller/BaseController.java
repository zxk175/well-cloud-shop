package com.zxk175.well.module.controller;

import com.zxk175.well.common.http.HttpMsg;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.util.jwt.JwtAuthUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author zxk175
 * @since 2019/03/16 17:17
 */
@Data
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected HttpServletRequest request;
    protected HttpServletResponse response;


    @ModelAttribute
    public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    protected String redirectStr(String url) {
        return "redirect:" + url;
    }

    protected ModelAndView redirectView(String url) {
        return new ModelAndView(new RedirectView(url));
    }

    protected ModelAndView toView(String url) {
        return new ModelAndView(url);
    }

    protected Response ok() {
        return Response.ok();
    }

    protected Response ok(HttpMsg httpMsg) {
        return Response.ok(httpMsg);
    }

    protected Response ok(Object data) {
        return Response.ok(data);
    }

    protected Response ok(Object data, Object extra) {
        return Response.ok(data, extra);
    }

    protected Response ok(HttpMsg ok, Object data, Object extra) {
        return Response.ok(ok, data, extra);
    }

    protected Response fail() {
        return Response.fail();
    }

    protected Response fail(HttpMsg httpMsg) {
        return Response.fail(httpMsg);
    }

    protected Response fail(Object data) {
        return Response.fail(data);
    }

    protected Response fail(String msg) {
        return Response.fail(msg);
    }

    protected Response fail(Integer code, String msg) {
        return Response.fail(code, msg);
    }

    protected Response saveReturn(boolean flag) {
        return Response.saveReturn(flag);
    }

    protected Response saveReturn(boolean flag, Object data) {
        return Response.saveReturn(flag, data);
    }

    protected Response removeReturn(boolean flag) {
        return Response.removeReturn(flag);
    }

    protected Response removeReturn(boolean flag, String errMsg) {
        return Response.removeReturn(flag, errMsg);
    }

    protected Response modifyReturn(boolean flag) {
        return Response.modifyReturn(flag);
    }

    protected Response modifyReturn(boolean flag, String errMsg) {
        return Response.modifyReturn(flag, errMsg);
    }

    protected Response diyReturn(boolean flag, String sucMsg, String errMsg) {
        return Response.diyReturn(flag, sucMsg, errMsg);
    }

    protected Response objectReturn(Object data) {
        return Response.objectReturn(data);
    }

    protected Response collReturn(Collection<?> data) {
        return Response.collReturn(data);
    }

    protected SysSubjectDTO getUser() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        return JwtAuthUtil.sysSubject(token);
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }
}
