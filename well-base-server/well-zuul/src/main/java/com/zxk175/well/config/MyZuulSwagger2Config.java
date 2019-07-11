package com.zxk175.well.config;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;

/**
 * @author zxk175
 * @since 2019/06/01 13:35
 */
@Component
@Primary
@AllArgsConstructor
public class MyZuulSwagger2Config implements SwaggerResourcesProvider {

    private RouteLocator routeLocator;


    @Override
    public List<SwaggerResource> get() {
        List<Route> routes = routeLocator.getRoutes();
        List<SwaggerResource> resources = Lists.newArrayList();
        for (Route route : routes) {
            SwaggerResource swaggerResource = swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"));
            resources.add(swaggerResource);
        }

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