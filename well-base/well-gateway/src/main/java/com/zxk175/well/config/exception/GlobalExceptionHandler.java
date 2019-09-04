package com.zxk175.well.config.exception;

import com.google.common.collect.Maps;
import com.zxk175.well.base.http.HttpMsg;
import com.zxk175.well.base.http.Response;
import com.zxk175.well.base.util.MyStrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-27 14:14
 */
@Slf4j
class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {

    private final ErrorProperties errorProperties;


    GlobalExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        this.errorProperties = errorProperties;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions
                .route(acceptsTextHtml(), this::renderErrorView)
                .andRoute(RequestPredicates.all(), this::renderErrorResponse);
    }

    @NonNull
    @Override
    protected Mono<ServerResponse> renderErrorView(ServerRequest request) {
        boolean includeStackTrace = isIncludeStackTrace(request, MediaType.TEXT_HTML);
        Map<String, Object> error = getErrorAttributes(request, includeStackTrace);
        HttpStatus errorStatus = getHttpStatus(error);
        ServerResponse.BodyBuilder responseBody = ServerResponse.status(errorStatus).contentType(MediaType.TEXT_HTML);

        return Flux
                .just("error")
                .flatMap((viewName) -> renderErrorView(viewName, responseBody, error))
                .switchIfEmpty(this.errorProperties.getWhitelabel().isEnabled() ? renderDefaultErrorView(responseBody, error) : Mono.error(getError(request)))
                .next();
    }

    @NonNull
    @Override
    public Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, true);
        HttpStatus httpStatus = getHttpStatus(errorAttributes);
        Response<Map<String, Object>> response = Response.fail();

        Integer notFound = 404;
        Integer value = httpStatus.value();
        Throwable error = super.getError(request);
        if (error instanceof NotFoundException && notFound.equals(value)) {
            response = Response.fail(HttpMsg.REQ_URI_NOT_FOUND);
        } else if (error instanceof ResponseStatusException && notFound.equals(value)) {
            response = Response.fail(HttpMsg.REQ_URI_NOT_FOUND);
        } else if (error instanceof TimeoutException) {
            response = Response.fail(HttpMsg.REQ_TIMEOUT);
        } else if (error instanceof ResponseStatusException) {
            httpStatus = ((ResponseStatusException) error).getStatus();
        } else {
            String message = error.getMessage();
            response = Response.fail(MyStrUtil.isBlank(message) ? error.toString() : message);
        }

        error.printStackTrace();

        Map<String, Object> data = Maps.newHashMapWithExpectedSize(8);
        data.put("status", httpStatus.toString());
        data.put("method", request.methodName());
        data.put("path", request.uri().toString());

        response.setData(data);

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(response));
    }
}