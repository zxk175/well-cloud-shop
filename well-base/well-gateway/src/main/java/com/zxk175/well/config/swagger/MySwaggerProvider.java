package com.zxk175.well.config.swagger;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
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

    private final InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository;


    @Override
    public List<SwaggerResource> get() {
        String lb = "lb://";
        String path = "Path";
        String apiUrl = "/v2/api-docs";
        List<SwaggerResource> swaggerResources = new ArrayList<>();

        inMemoryRouteDefinitionRepository.getRouteDefinitions().subscribe(routeDefinition -> {
            // webSocket代理路由
            URI uri = routeDefinition.getUri();
            if (uri.toString().startsWith(lb)) {
                List<PredicateDefinition> predicates = routeDefinition.getPredicates();
                predicates.forEach(predicateDefinition -> {
                    String name = predicateDefinition.getName();
                    if (path.equalsIgnoreCase(name)) {
                        String url = predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", apiUrl);
                        swaggerResources.add(swaggerResource(routeDefinition.getId(), url));
                    }
                });
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