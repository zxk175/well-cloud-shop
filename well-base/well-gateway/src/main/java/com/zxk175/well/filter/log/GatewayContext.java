package com.zxk175.well.filter.log;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author zxk175
 * @since 2019-08-11 01:28
 */
@Data
public class GatewayContext {

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    /**
     * cache request body
     */
    private String requestBody;
    /**
     * cache response body
     */
    private String responseBody;
    /**
     * cache form data
     */
    private MultiValueMap<String, String> formData;
    /**
     * cache all request data include:form data and query param
     */
    private MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);
}