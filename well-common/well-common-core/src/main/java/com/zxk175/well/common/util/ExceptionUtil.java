package com.zxk175.well.common.util;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.util.message.PushWellUtil;
import com.zxk175.well.common.util.net.IpUtil;
import com.zxk175.well.common.util.net.RequestUtil;
import com.zxk175.well.common.util.spring.SpringActiveUtil;
import com.zxk175.well.common.util.upload.UploadUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

/**
 * @author zxk175
 * @since 2018/7/16 11:54
 */
public class ExceptionUtil {

    private static final String FORMAT1 = Const.FORMAT1;
    private static final String FORMAT2 = Const.FORMAT2;
    private static final String FORMAT4 = Const.FORMAT4;
    private static final String FORMAT5 = Const.FORMAT5;
    private static final String FORMAT_DEFAULT = Const.DATE_FORMAT_CN;


    private static void sendRequestInfo(String title, StringBuilder sb) {
        StringBuilder msg = new StringBuilder(16);

        String now = DateUtil.now(FORMAT_DEFAULT);
        msg.append(now);
        msg.append(sb);

        msg.append(sendRequestInfo());

        PushWellUtil.sendNotify(title, msg);
    }

    private static StringBuilder sendRequestInfo() {
        StringBuilder msg = new StringBuilder(16);

        HttpServletRequest request = RequestUtil.request();
        if (ObjectUtil.isNull(request)) {
            msg.append(FORMAT2);
            msg.append("未获取到请求信息");
        } else {
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
            int headCount = 1;
            int headSize = headersInfo.size();
            for (Map.Entry<String, String> entries : headersInfo.entrySet()) {
                msg.append(entries.getKey()).append("：").append(entries.getValue());
                if (headCount < headSize) {
                    msg.append(FORMAT2);
                }

                ++headCount;
            }

            msg.append(FORMAT1);
            msg.append("请求参数");

            buildParam(msg, request);
        }

        return msg;
    }

    private static void buildParam(StringBuilder msg, HttpServletRequest request) {
        String body;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            body = IoUtil.read(reader);

            if (MyStrUtil.isNotBlank(body)) {
                String contentType = request.getContentType();
                if (contentType.contains("multipart")) {
                    msg.append(FORMAT2);
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
                }

                if (contentType.contains("xml")) {
                    Map<String, String> result = XmlUtil.xmlToMap(body);
                    body = JSON.toJSONString(result);
                }

                if (contentType.contains("json")) {
                    body = JsonFormatUtil.formatJsonStr(body);
                }

                if (contentType.contains("json")) {
                    msg.append(FORMAT4).append(body).append(FORMAT5);
                }
            } else {
                msg.append(FORMAT2).append("参数为空");
            }
        } catch (Exception ex) {
            msg.append(FORMAT2).append("解析异常");
            msg.append(FORMAT2).append("exception：").append(ex.toString());
            msg.append(FORMAT2).append("stackTrace[0]：").append(ex.getStackTrace()[0]);
            msg.append(FORMAT2).append("stackTrace[1]：").append(ex.getStackTrace()[1]);
        }
    }

    public static Response buildExceptionInfo(Throwable ex, String title) {
        return buildExceptionInfo(ex, new StringBuilder(), title);
    }

    private static Response buildExceptionInfo(Throwable ex, StringBuilder msg, String title) {
        msg.append(FORMAT1);
        msg.append("异常信息");
        msg.append(FORMAT2);
        msg.append(ObjectUtil.isNull(ex) ? "没有错误信息" : ex.getMessage());

        try {
            InputStream inputStream = createExceptionHtml(title, ex);

            String dir = buildErrorPath();
            String ossUrl = UploadUtil.single(inputStream, dir, "html");
            msg.append(FORMAT1);
            msg.append("异常详细信息地址");
            msg.append(FORMAT2);
            msg.append(ossUrl);

            ExceptionUtil.sendRequestInfo(title, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response result = Response.fail(title);
        Map<String, String> extra = Maps.newHashMap();
        extra.put("msg", ex.getMessage());
        result.setExtra(extra);

        return result;
    }

    private static InputStream createExceptionHtml(String title, Throwable ex) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(ExceptionUtil.class, "/template/");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", title);
        String exDetailInfo = getExceptionDetail(ex);
        data.put("ex", exDetailInfo);

        Template template = configuration.getTemplate("error.ftl");
        StringWriter out = new StringWriter();
        template.process(data, out);
        return new ByteArrayInputStream(out.toString().getBytes(Const.UTF_8_OBJ));
    }

    private static String getExceptionDetail(Throwable ex) {
        if (ObjectUtil.isNull(ex)) {
            return "";
        }

        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        try {
            ex.fillInStackTrace().printStackTrace(new PrintStream(outputStream, true));
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return outputStream.toString(Const.UTF_8_OBJ);
    }

    private static String buildErrorPath() {
        return "error/" + SpringActiveUtil.getActive() + MyStrUtil.SLASH + DateUtil.now(Const.DATE_FORMAT_NO_TIME) + MyStrUtil.SLASH;
    }
}
