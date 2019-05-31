package com.zxk175.well.hystrix;

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
        log.info("route：{}", route);
        cause.printStackTrace();

        return new ClientHttpResponse() {

            @Override
            @NonNull
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return 200;
            }

            @Override
            @NonNull
            public String getStatusText() {
                return "ok";
            }

            @Override
            public void close() {

            }

            @Override
            @NonNull
            public InputStream getBody() {
                return new ByteArrayInputStream("Ops Error, I'm the fallback.".getBytes(Const.UTF_8_OBJ));
            }

            @Override
            @NonNull
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}