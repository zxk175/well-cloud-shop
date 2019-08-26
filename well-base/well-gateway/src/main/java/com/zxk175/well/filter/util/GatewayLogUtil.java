package com.zxk175.well.filter.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.zxk175.well.base.util.MyStrUtil;
import com.zxk175.well.base.util.id.ClockUtil;
import com.zxk175.well.base.util.json.FastJsonUtil;
import com.zxk175.well.base.util.json.JsonFormatUtil;
import com.zxk175.well.filter.log.FilterConst;
import com.zxk175.well.filter.log.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-11 01:28
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
                logBuffer.append(name).append("：").append(value).append('\n');
            }
        }
    }

    public static Flux<DataBuffer> packByte(ServerWebExchange exchange, DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);

        return Flux.defer(() -> {
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            DataBufferUtils.retain(buffer);
            return Mono.just(buffer);
        });
    }

    public static boolean isRecorder(ServerHttpRequest httpRequest, URI uri) {
        // 只记录 HTTP 请求
        String http = "http";
        String https = "https";
        String scheme = uri.getScheme();
        if (MyStrUtil.neIgnoreCase(http, scheme) && MyStrUtil.neIgnoreCase(https, scheme)) {
            return true;
        }

        String websocket = "websocket";
        String upgrade = httpRequest.getHeaders().getUpgrade();
        return websocket.equalsIgnoreCase(upgrade);
    }

    public static void recorderOriginRequest(ServerWebExchange exchange) {
        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append("\n-----------------------------\n");

        ServerHttpRequest request = exchange.getRequest();
        recorderRequest(exchange, request.getURI(), logBuffer.append("原始请求\n"));
    }

    public static void recorderRouteRequest(ServerWebExchange exchange) {
        long startTime = ClockUtil.now();
        exchange.getAttributes().put(FilterConst.START_TIME, startTime);

        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append("\n-----------------------------\n");

        URI requestUri = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        recorderRequest(exchange, requestUri, logBuffer.append("代理请求\n"));
    }

    private static void recorderRequest(ServerWebExchange exchange, URI uri, StringBuilder logBuffer) {
        long startTime = ClockUtil.now();
        exchange.getAttributes().put(FilterConst.START_TIME, startTime);

        ServerHttpRequest httpRequest = exchange.getRequest();
        HttpMethod method = httpRequest.getMethod();

        if (ObjectUtil.isNotNull(method)) {
            logBuffer.append(method.name()).append(' ').append(uri.toString());
            MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
            if (CollUtil.isNotEmpty(queryParams)) {
                logBuffer.append("\nQuery：\n").append(FastJsonUtil.jsonPrettyFormat(queryParams));
            }
        }

        logBuffer.append("\n------------请求头------------\n");
        HttpHeaders headers = httpRequest.getHeaders();
        resolveHeader(logBuffer, headers);

        if (hasBody(method)) {
            logBuffer.append("------------请求体------------\n");
            GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
            String body = "";
            if (ObjectUtil.isNotNull(gatewayContext)) {
                body = gatewayContext.getRequestBody();
            }

            if (MyStrUtil.isBlank(body)) {
                logBuffer.append("请求体为空");
            } else {
                MediaType contentType = headers.getContentType();
                if (ObjectUtil.isNotNull(contentType)) {
                    String subType = contentType.getSubtype();

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

    public static void recorderResponse(ServerWebExchange exchange) {
        StringBuilder logBuffer = new StringBuilder();
        ServerHttpResponse httpResponse = exchange.getResponse();
        HttpStatus statusCode = httpResponse.getStatusCode();
        logBuffer.append("\n-----------------------------\n");
        if (statusCode == null) {
            logBuffer.append("返回异常").append("\n------------ end ------------\n\n");
            return;
        }

        Long startTime = exchange.getAttribute(FilterConst.START_TIME);
        long executeTime = 0L;
        if (ObjectUtil.isNotNull(startTime)) {
            executeTime = (ClockUtil.now() - startTime);
        }

        logBuffer.append("响应\n")
                .append(statusCode.value())
                .append(" ").append(statusCode.getReasonPhrase())
                .append(" ").append(executeTime).append("ms");

        HttpHeaders headers = httpResponse.getHeaders();
        logBuffer.append("\n------------响应头------------\n");
        resolveHeader(logBuffer, headers);

        logBuffer.append("------------响应体------------\n");

        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        String body = "";
        if (ObjectUtil.isNotNull(gatewayContext)) {
            body = gatewayContext.getResponseBody();
        }

        if (MyStrUtil.isBlank(body)) {
            logBuffer.append("响应体为空");
        } else {
            MediaType contentType = headers.getContentType();
            if (ObjectUtil.isNotNull(contentType)) {
                String subType = contentType.getSubtype();

                String json = "json";
                if (subType.equals(json)) {
                    String newBody = JsonFormatUtil.formatJsonStr(body);
                    logBuffer.append(newBody);
                }
            }
        }

        logBuffer.append("\n------------ end ------------\n\n");

        log.info(logBuffer.toString());
    }
}
