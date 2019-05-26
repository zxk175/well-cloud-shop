package com.zxk175.well.common.util.net;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.json.FastJsonUtil;

import java.util.Map;

/**
 * @author zxk175
 * @since 2018/7/25 16:42
 */
public class HttpUtil {

    public static String get(String url, Map<String, String> params) {
        return HttpClient
                .get(url)
                .queryString(params)
                .asString();
    }

    public static JSONObject getJsonObject(String url) {
        return getJsonObject(url, null);
    }

    public static JSONObject getJsonObject(String url, Map<String, String> params) {
        String result = get(url, params);

        return toJSONObject(result);
    }

    public static JSONObject post(String url, String json) {
        String result = postJson(url, json);

        return toJSONObject(result);
    }

    private static String postJson(String url, String json) {
        return HttpClient
                .textBody(url)
                .json(json)
                .charset(Const.UTF_8)
                .asString();
    }

    public static String postXml(String url, String xml) {
        return HttpClient
                .textBody(url)
                .xml(xml)
                .charset(Const.UTF_8)
                .asString();
    }

    private static JSONObject toJSONObject(String result) {
        return FastJsonUtil.toObject(result, JSONObject.class);
    }
}

