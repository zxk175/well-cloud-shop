package com.zxk175.well.module.service.impl.gateway;

import com.zxk175.well.module.service.gateway.DynamicRouteService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author zxk175
 * @since 2019-08-29 15:30
 */
@Service
@AllArgsConstructor
public class DynamicRouteServiceImpl implements DynamicRouteService, ApplicationEventPublisherAware {

    private RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher publisher;


    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public boolean save(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return true;
    }

    @Override
    public boolean modify(RouteDefinition definition) {
        try {
            boolean flag = remove(definition.getId());
            if (flag) {
                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }

            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        Mono<Boolean> flag = this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(true)))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(false));

        return flag.blockOptional().orElse(false);
    }
}