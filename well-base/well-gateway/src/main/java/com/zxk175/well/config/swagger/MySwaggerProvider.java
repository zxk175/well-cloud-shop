package com.zxk175.well.config.swagger;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zxk175
 * @since 2019-07-11 10:33
 */
@Primary
@Component
@AllArgsConstructor
public class MySwaggerProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";
    private PropertiesRouteDefinitionLocator propertiesRouteDefinition;


    @Override
    public List<SwaggerResource> get() {
        String lb = "lb://";
        List<SwaggerResource> swaggerResources = new ArrayList<>();

        // 取出application.yml配置的route
        propertiesRouteDefinition.getRouteDefinitions().subscribe(routeDefinition -> {
            // webSocket代理路由
            URI uri = routeDefinition.getUri();
            if (uri.toString().startsWith(lb)) {
                swaggerResources.add(swaggerResource(routeDefinition.getId(), "/" + uri.getHost().toLowerCase() + API_URI));
            }
        });

        return swaggerResources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}