package com.zxk175.well.common.util.common;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.consts.enums.IdentityType;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.dto.PageBeanDTO;
import com.zxk175.well.common.model.param.PageParam;
import com.zxk175.well.common.util.DateUtil;
import com.zxk175.well.common.util.ExceptionUtil;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.spring.SpringActiveUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zxk175
 * @since 2019/04/01 16:28
 */
public class CommonUtil {

    public static String getRandom(boolean numberFlag, int length) {
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyzABCDEFGHIJKMNPQRSTUVWXYZ";
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }

        int len = strTable.length();
        final ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            int number = current.nextInt(len);
            sb.append(strTable.charAt(number));
        }
        return sb.toString();
    }

    public static void buildPageParam(PageParam param) {
        int[] ints = transToStartEnd(param.getPage(), param.getSize());

        param.setPage(ints[0]);
        param.setSize(ints[1]);
    }

    public static boolean hasSupper(Integer identity) {
        return IdentityType.SUPER.value().equals(identity);
    }

    public static Response putPageExtraTrue(Object data, Long count, PageParam param) {
        return putPageExtra(data, count, param, null, true);
    }

    public static Response putPageExtraTrue(Object data, Long count, PageParam param, Map<String, Object> extra) {
        return putPageExtra(data, count, param, extra, true);
    }

    public static Response putPageExtraFalse(Object data, Long count, PageParam param) {
        return putPageExtra(data, count, param, null, false);
    }

    public static Response putPageExtraFalse(Object data, Long count, PageParam param, Map<String, Object> extra) {
        return putPageExtra(data, count, param, extra, false);
    }

    private static Response putPageExtra(Object data, Long count, PageParam param, Map<String, Object> extra, boolean removeTotal) {
        PageBeanDTO pageBeanDTO = new PageBeanDTO()
                .setPage((long) (param.getSize() + 1))
                .setSize((long) param.getSize());

        pageBeanDTO.put("total", count);

        pageBeanDTO.put("hasPre", pageBeanDTO.isHasPre());
        pageBeanDTO.put("hasNext", pageBeanDTO.isHasNext());
        pageBeanDTO.put("page", pageBeanDTO.getPage());
        pageBeanDTO.put("size", pageBeanDTO.getSize());

        if (removeTotal) {
            pageBeanDTO.remove("total");
        } else {
            pageBeanDTO.put("pageTotal", pageBeanDTO.getPageTotal());
        }

        if (CollUtil.isNotEmpty(extra)) {
            pageBeanDTO.putAll(extra);
        }

        return Response.ok(data, pageBeanDTO);
    }

    private static int[] transToStartEnd(int page, int size) {
        // 最多一次100条数据
        int tmp = 100;
        if (size > tmp) {
            size = tmp;
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 0;
        }

        int start = (page - 1) * size;
        int end = size;

        return new int[]{start, end};
    }

    public static String requestLimitKey(String url, String ip) {
        // 拼接url和ip
        return "req_limit_" + url + ip;
    }

    public static String getFileName(String imgUrl) {
        if (MyStrUtil.isNotBlank(imgUrl) && imgUrl.contains("zxk175")) {
            return imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.lastIndexOf('.'));
        }

        return "";
    }

    public static String buildErrorPath(String prefix) {
        return prefix + MyStrUtil.SLASH + SpringActiveUtil.getActive() + MyStrUtil.SLASH + DateUtil.now(Const.DATE_FORMAT_NO_TIME) + MyStrUtil.SLASH;
    }

    public static InputStream createExceptionHTML(String title, Exception ex) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(CommonUtil.class, "/template/");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", title);
        String exDetailInfo = ExceptionUtil.getExceptionDetail(ex);
        data.put("ex", exDetailInfo);

        Template template = configuration.getTemplate("error.ftl");
        StringWriter out = new StringWriter();
        template.process(data, out);
        return new ByteArrayInputStream(out.toString().getBytes(Const.UTF_8_OBJ));
    }

    public static String getLoginUrl(HttpServletRequest httpRequest) {
        return httpRequest.getContextPath() + "/login.html";
    }
}
