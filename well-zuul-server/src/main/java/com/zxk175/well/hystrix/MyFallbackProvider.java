package com.zxk175.well.hystrix;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.zxk175.well.common.consts.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author zxk175
 * @since 2019/05/31 18:12
 */
@Slf4j
@Component
public class MyFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return this.response(HttpStatus.GATEWAY_TIMEOUT, route, cause);
        } else {
            return this.response(route, cause);
        }
    }

    private ClientHttpResponse response(String route, Throwable cause) {
        return this.response(HttpStatus.OK, route, cause);
    }

    private ClientHttpResponse response(HttpStatus httpStatus, String route, Throwable cause) {
        log.info("route：{}", route);
        cause.printStackTrace();

        return new ClientHttpResponse() {

            @Override
            @NonNull
            public HttpStatus getStatusCode() {
                return httpStatus;
            }

            @Override
            public int getRawStatusCode() {
                return this.getStatusCode().value();
            }

            @Override
            @NonNull
            public String getStatusText() {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            @NonNull
            public InputStream getBody() {
                String msg = "服务不可用";
                log.error("调用：{}，异常：{}", route, msg);
                return new ByteArrayInputStream(msg.getBytes(Const.UTF_8_OBJ));
            }

            @Override
            @NonNull
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
}