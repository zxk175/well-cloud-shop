package com.zxk175.well.base.http;

import com.google.common.collect.Lists;
import com.zxk175.well.base.consts.Const;

import java.util.List;

/**
 * @author zxk175
 * @since 2019-08-11 00:43
 */
public enum HttpMsg {

    // 请求状态
    OK(Const.OK_CODE, Const.OK_TXT),
    FAIL(Const.FAIL_CODE, Const.FAIL_TXT),
    NO_DATA(2, "暂无数据"),

    // 参数错误 10001-19999
    PARAM_NOT_COMPLETE(10001, "参数缺失"),
    TOKEN_NOT_ERROR(10002, "请在请求头添加token信息"),
    TOKEN_TIMEOUT_ERROR(10003, "登录状态已失效，请重新登录"),
    TOKEN_FORMAT_ERROR(10004, "token格式错误"),
    TOKEN_CHECK_ERROR(10005, "您的手机号已在其它设备登录，请重新登录"),

    // 用户错误 20001-29999
    USER_NOT_LOGIN(20001, "用户未登录"),
    USER_LOGIN_ERR(20002, "账号不存在或密码错误"),
    USER_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),

    // 系统相关 50001-59999
    DB_ADD_SUCCESS(50001, "添加成功"),
    DB_ADD_ERROR(50002, "添加失败"),
    DB_DELETE_SUCCESS(50003, "删除成功"),
    DB_DELETE_ERROR(50004, "删除失败"),
    DB_MODIFY_SUCCESS(50005, "修改成功"),
    DB_MODIFY_ERROR(50006, "修改失败"),

    ;


    private Integer code;
    private String msg;


    HttpMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public static void main(String[] args) {
        HttpMsg[] httpMsgArr = HttpMsg.values();
        List<Integer> codes = Lists.newArrayList();
        for (HttpMsg httpMsg : httpMsgArr) {
            if (codes.contains(httpMsg.code)) {
                System.out.println("重复的code值：" + httpMsg.code);
            } else {
                codes.add(httpMsg.code);
            }
        }
    }
}
