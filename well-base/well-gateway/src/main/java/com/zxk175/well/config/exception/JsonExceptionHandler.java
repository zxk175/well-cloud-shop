package com.zxk175.well.config.exception;

import com.google.common.collect.Maps;
import com.zxk175.well.base.http.HttpMsg;
import com.zxk175.well.base.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-27 14:14
 */
@Slf4j
public class JsonExceptionHandler extends AbstractErrorWebExceptionHandler {

    JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, true);
        HttpStatus httpStatus = getHttpStatus(errorAttributes);
        Response response = Response.fail();

        Throwable error = super.getError(request);
        if (error instanceof NotFoundException) {
            response = Response.fail(HttpMsg.REQ_URI_NOT_FOUND);
        }
        if (error instanceof TimeoutException) {
            response = Response.fail(HttpMsg.REQ_TIMEOUT);
        }
        if (error instanceof ResponseStatusException) {
            httpStatus = ((ResponseStatusException) error).getStatus();
        }

        Map<String, Object> extra = Maps.newHashMapWithExpectedSize(8);
        extra.put("status", httpStatus.toString());
        extra.put("method", request.methodName());
        extra.put("path", request.uri().toString());

        response.setExtra(extra);

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(response));
    }

    private HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("status");
        return HttpStatus.valueOf(statusCode);
    }
}