package com.zxk175.well.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.core.io.IOUtils;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.message.PushWellUtil;
import com.zxk175.well.common.util.net.IpUtil;
import com.zxk175.well.common.util.net.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

/**
 * @author zxk175
 * @since 2018/7/16 11:54
 */
public class ExceptionUtil {

    private static final String FORMAT1 = Const.FORMAT1;
    private static final String FORMAT2 = Const.FORMAT2;
    private static final String FORMAT_DEFAULT = Const.DATE_FORMAT_CN;


    public static String getExceptionDetail(Exception e) {
        String result = "";
        if (ObjectUtil.isNotNull(e)) {
            FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            result = out.toString(Const.UTF_8_OBJ);
            pout.close();
            try {
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    public static void sendRequestInfo(String title, StringBuilder sb) {
        StringBuilder msg = new StringBuilder(16);

        String now = DateUtil.getNow(FORMAT_DEFAULT);
        msg.append(now);
        msg.append(sb);
        msg.append(sendRequestInfo(false));

        PushWellUtil.sendNotify(title, msg.toString());
    }

    public static StringBuilder sendRequestInfo(boolean flag) {
        HttpServletRequest request = RequestUtil.request();
        return sendRequestInfo(request, flag);
    }

    public static StringBuilder sendRequestInfo(HttpServletRequest request, boolean flag) {
        StringBuilder msg = new StringBuilder(16);

        if (flag) {
            String now = DateUtil.getNow(FORMAT_DEFAULT);
            msg.append(now);
        }

        String ip = IpUtil.getClientIp(request);
        msg.append(FORMAT1);
        msg.append("请求者IP地址");
        msg.append(FORMAT2);
        msg.append(ip);
        msg.append(FORMAT1);
        msg.append("请求者IP地域信息");
        msg.append(FORMAT2);
        msg.append(IpUtil.getAddressByIp(ip));
        msg.append(FORMAT1);
        msg.append("请求地址");
        msg.append(FORMAT2);
        String method = request.getMethod();
        String url = RequestUtil.requestUrl(request);
        msg.append(method).append("：").append(url);
        msg.append(FORMAT1);
        msg.append("Head参数");
        msg.append(FORMAT2);
        Map<String, String> headersInfo = RequestUtil.getHeaders(request);
        int count = 1;
        int size = headersInfo.size();
        for (Map.Entry<String, String> entries : headersInfo.entrySet()) {
            msg.append(entries.getKey()).append("：").append(entries.getValue());
            if (count < size) {
                msg.append(FORMAT2);
            }

            ++count;
        }

        String get = "GET";
        if (MyStrUtil.eqIgnoreCase(get, method)) {
            msg.append(FORMAT1);
            msg.append("请求参数");
            msg.append(FORMAT2);

            String body = "";
            JSONObject data = null;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                body = IOUtils.toString(reader);
                String contentType = request.getContentType();

                if (contentType.contains("json")) {
                    data = JSONObject.parseObject(body);
                } else if (contentType.contains("xml")) {
                    Map<String, String> result = XmlUtil.xmlToMap(body);
                    data = new JSONObject();
                    data.putAll(result);
                } else if (contentType.contains("multipart")) {
                    final Collection<Part> parts = request.getParts();
                    for (Part part : parts) {
                        Collection<String> headerNames = part.getHeaderNames();
                        for (String name : headerNames) {
                            Collection<String> headers = part.getHeaders(name);
                            // fix 中文乱码
                            String head = new String(headers.toString().getBytes(Const.ISO_8859_1), Const.UTF_8_OBJ);
                            msg.append(name).append("：").append(head).append(FORMAT2);
                        }
                    }
                } else {
                    msg.append(MyStrUtil.isNotBlank(body) ? body : "参数为空");
                }

                if (CollUtil.isNotEmpty(data)) {
                    for (Map.Entry<String, Object> entries : data.entrySet()) {
                        msg.append(entries.getKey()).append("：").append(entries.getValue()).append(FORMAT2);
                    }
                }
            } catch (Exception e) {
                msg.append(MyStrUtil.isNotBlank(body) ? body : "参数为空");
            }
        }

        return msg;
    }
}
