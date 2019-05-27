package com.zxk175.well;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author zxk175
 * @since 2019/05/27 16:10
 */
@Slf4j
@Component
public class EurekaStateChangeListener {

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info(instanceInfo.getAppName().toLowerCase() + "，进行注册");
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("{}，服务下线", event.getServerId());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{}，服务进行续约", event.getServerId());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("注册中心启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("Eureka Server 启动");
    }
}
