package com.zxk175.well.common.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.zxk175.well.common.consts.Const;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author zxk175
 * @since 2019/03/06 17:59
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Response implements Serializable {

    @ApiModelProperty(value = "消息代码", example = "0")
    private Integer code;

    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "消息提示", example = "请求成功")
    private String msg;

    @JSONField(jsonDirect = true)
    @ApiModelProperty(value = "额外数据", example = "{}")
    private Object extra;

    @JSONField(jsonDirect = true)
    @ApiModelProperty(value = "返回数据", example = "{}")
    private Object data;


    public static Response ok() {
        return setOk(HttpMsg.OK.code(), HttpMsg.OK.msg());
    }

    public static Response ok(Object data) {
        return ok().setData(data);
    }

    public static Response ok(HttpMsg httpMsg) {
        return ok(httpMsg.code(), httpMsg.msg());
    }

    public static Response ok(Integer code, String msg) {
        return setOk(code, msg);
    }

    public static Response ok(Object data, Object extra) {
        return ok(data).setExtra(extra);
    }

    public static Response ok(HttpMsg ok, Object data, Object extra) {
        return setOk(ok.code(), ok.msg()).setData(data).setExtra(extra);
    }

    public static Response fail() {
        return setFail(HttpMsg.FAIL.code(), HttpMsg.FAIL.msg());
    }

    public static Response fail(HttpMsg httpMsg) {
        return setFail(httpMsg.code(), httpMsg.msg());
    }

    public static Response fail(Object data) {
        return fail().setData(data);
    }

    public static Response fail(String msg) {
        return setFail(Const.FAIL_CODE, msg);
    }

    public static Response fail(Integer code, String msg) {
        return setFail(code, msg);
    }

    public static Response fail(HttpMsg httpMsg, Object data) {
        return setFail(httpMsg.code(), httpMsg.msg()).setData(data);
    }

    public static Response saveReturn(boolean flag) {
        return flag ? ok(HttpMsg.DB_ADD_SUCCESS) : fail(HttpMsg.DB_ADD_ERROR);
    }

    public static Response saveReturn(boolean flag, Object data) {
        return flag ? ok(HttpMsg.DB_ADD_SUCCESS, data) : fail(HttpMsg.DB_ADD_ERROR);
    }

    public static Response removeReturn(boolean flag) {
        return flag ? ok(HttpMsg.DB_DELETE_SUCCESS) : fail(HttpMsg.DB_DELETE_ERROR);
    }

    public static Response removeReturn(boolean flag, String errMsg) {
        return flag ? ok(HttpMsg.DB_DELETE_SUCCESS) : fail(Const.FAIL_CODE, errMsg);
    }

    public static Response modifyReturn(boolean flag) {
        return flag ? ok(HttpMsg.DB_MODIFY_SUCCESS) : fail(HttpMsg.DB_MODIFY_ERROR);
    }

    public static Response modifyReturn(boolean flag, String errMsg) {
        return flag ? ok(HttpMsg.DB_MODIFY_SUCCESS) : fail(Const.FAIL_CODE, errMsg);
    }

    public static Response diyReturn(boolean flag, String sucMsg, String errMsg) {
        return flag ? ok(Const.OK_CODE, sucMsg) : fail(Const.FAIL_CODE, errMsg);
    }

    public static Response objectReturn(Object data) {
        return ObjectUtil.isNull(data) ? Response.fail(HttpMsg.NO_DATA) : Response.ok(data);
    }

    public static Response collReturn(Collection<?> data) {
        return CollUtil.isEmpty(data) ? Response.fail(HttpMsg.NO_DATA) : Response.ok(data);
    }

    private static Response setOk(Integer code, String msg) {
        return setResponse(code, msg, true);
    }

    private static Response setFail(Integer code, String msg) {
        return setResponse(code, msg, false);
    }

    private static Response setResponse(Integer code, String msg, boolean success) {
        return new Response()
                .setCode(code)
                .setMsg(msg)
                .setSuccess(success);
    }
}
