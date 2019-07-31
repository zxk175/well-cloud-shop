package com.zxk175.well.filter.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.json.FastJsonUtil;
import com.zxk175.well.common.util.json.JsonFormatUtil;
import com.zxk175.well.filter.log.MyResponseDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zxk175
 * @since 2019/7/25 20:04
 */
@Slf4j
public class GatewayLogUtil {

    private static boolean hasBody(HttpMethod method) {
        // 只记录这3种body
        return (method == HttpMethod.GET || method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH);
    }

    private static void resolveHeader(StringBuilder logBuffer, HttpHeaders headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                logBuffer.append(name).append(":").append(value).append('\n');
            }
        }
    }

    private static String resolveBody(Flux<DataBuffer> body) {
        AtomicReference<String> bodyReference = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = Const.UTF_8_OBJ.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyReference.set(charBuffer.toString());
        });

        // 获取body
        return bodyReference.get();
    }

    public static boolean isRecorder(ServerHttpRequest httpRequest, URI uri) {
        // 只记录 HTTP 请求
        String http = "http";
        String https = "https";
        String scheme = uri.getScheme();
        if (MyStrUtil.ne(http, scheme) && MyStrUtil.ne(https, scheme)) {
            return true;
        }

        String websocket = "websocket";
        String upgrade = httpRequest.getHeaders().getUpgrade();
        return websocket.equalsIgnoreCase(upgrade);
    }

    public static void recorderRequest(ServerHttpRequest httpRequest) {
        URI uri = httpRequest.getURI();
        HttpMethod method = httpRequest.getMethod();

        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append("\n-----------------------------\n");
        if (ObjectUtil.isNotNull(method)) {
            logBuffer.append(method.name()).append(' ').append(uri.toString());
        }

        logBuffer.append("\n------------请求头------------\n");
        HttpHeaders headers = httpRequest.getHeaders();
        resolveHeader(logBuffer, headers);

        if (hasBody(method)) {
            logBuffer.append("------------请求体------------\n");
            long length = headers.getContentLength();
            if (length <= 0) {
                logBuffer.append("请求体为空");
            } else {
                MediaType contentType = headers.getContentType();
                if (ObjectUtil.isNotNull(contentType)) {
                    String subType = contentType.getSubtype();
                    String body = resolveBody(httpRequest.getBody());

                    String xml = "xml";
                    if (subType.equals(xml)) {
                        Map<String, Object> result = XmlUtil.xmlToMap(body);
                        body = FastJsonUtil.jsonPrettyFormat(result);
                    }

                    String json = "json";
                    if (subType.equals(json)) {
                        body = JsonFormatUtil.formatJsonStr(body);
                    }

                    logBuffer.append(body);
                }
            }
        }

        logBuffer.append("\n------------ end ------------\n\n");

        log.info(logBuffer.toString());
    }

    public static void recorderResponse(ServerWebExchange exchange, ServerHttpResponse httpResponse) {
        StringBuilder logBuffer = new StringBuilder();
        HttpStatus statusCode = httpResponse.getStatusCode();
        logBuffer.append("\n-----------------------------\n");
        if (statusCode == null) {
            logBuffer.append("返回异常").append("\n------------ end ------------\n\n");
            return;
        }

        logBuffer.append("响应：").append(statusCode.value()).append(" ").append(statusCode.getReasonPhrase());

        HttpHeaders headers = httpResponse.getHeaders();
        logBuffer.append("\n------------响应头------------\n");
        resolveHeader(logBuffer, headers);

        logBuffer.append("------------响应体------------\n");

        MyResponseDecorator myResponseDecorator = (MyResponseDecorator) exchange.getResponse();
        String body = resolveBody(myResponseDecorator.copy());

        if (MyStrUtil.isBlank(body)) {
            logBuffer.append("响应体为空");
        } else {
            body = JsonFormatUtil.formatJsonStr(body);
            logBuffer.append(body);
        }

        logBuffer.append("\n------------ end ------------\n\n");

        log.info(logBuffer.toString());
    }
}
