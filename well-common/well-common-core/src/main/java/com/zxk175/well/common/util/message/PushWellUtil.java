package com.zxk175.well.common.util.message;

import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.ThreadUtil;
import com.zxk175.well.common.util.json.FastJsonUtil;
import com.zxk175.well.common.util.net.HttpUtil;
import com.zxk175.well.common.util.spring.SpringActiveUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author zxk175
 * @since 2019/04/13 15:52
 */
@Slf4j
public class PushWellUtil {

    public static void sendNotify(String title, StringBuilder msg) {
        send(title, msg);
    }

    private static void send(String title, StringBuilder msg) {
        ExecutorService singleThreadPool = ThreadUtil.newExecutor(5, "notify");
        boolean flag = SpringActiveUtil.getBoolean();

        singleThreadPool.execute(() -> {
            String active2ChineseStr = SpringActiveUtil.getChineseStr(flag);
            try {
                Map<String, String> params = Maps.newHashMap();
                params.put("title", active2ChineseStr + "=" + title);
                params.put("content", msg.toString());
                params.put("sendKey", Const.MSG_KEY);

                String jsonStr = FastJsonUtil.jsonStr(params);
                HttpUtil.post(Const.WE_CHAT_MSG_URL, jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        singleThreadPool.shutdown();
    }
}
