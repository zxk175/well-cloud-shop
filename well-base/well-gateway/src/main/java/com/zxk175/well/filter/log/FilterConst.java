package com.zxk175.well.filter.log;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;

/**
 * @author zxk175
 * @since 2019-08-11 01:28
 */
public class FilterConst {

    /**
     * Gateway Context Filter
     */
    static final int GATEWAY_CONTEXT_FILTER = Integer.MIN_VALUE;
    /**
     * Request Log Filter
     */
    static final int REQUEST_LOG_FILTER = Integer.MIN_VALUE + 2;
    /**
     * Cache Response Data Filter
     */
    static final int RESPONSE_DATA_FILTER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    /**
     * 在向业务服务转发前执行 NettyRoutingFilter 或 WebClientHttpRoutingFilter
     */
    static final int ROUTE_REQUEST_LOG_FILTER = Ordered.LOWEST_PRECEDENCE - 10;

    public static final String START_TIME = "startTime";
}