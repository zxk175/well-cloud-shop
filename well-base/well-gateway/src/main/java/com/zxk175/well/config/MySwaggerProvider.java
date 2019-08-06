package com.zxk175.well.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zxk175
 * @since 2019-07-11 10:33
 */
@Primary
@Component
@AllArgsConstructor
public class MySwaggerProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";
    private PropertiesRouteDefinitionLocator routeDefinitionLocator;


    @Override
    public List<SwaggerResource> get() {
        String lb = "lb://";
        List<SwaggerResource> resources = new ArrayList<>();

        // 取出application.yml配置的route
        routeDefinitionLocator.getRouteDefinitions().subscribe(new Consumer<RouteDefinition>() {
            @Override
            public void accept(RouteDefinition routeDefinition) {
                // webSocket代理路由
                URI uri = routeDefinition.getUri();
                if (uri.toString().startsWith(lb)) {
                    resources.add(swaggerResource(routeDefinition.getId(), "/" + uri.getHost().toLowerCase() + API_URI));
                }
            }
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}