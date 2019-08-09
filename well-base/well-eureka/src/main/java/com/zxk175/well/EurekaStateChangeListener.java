package com.zxk175.well;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
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
        log.info("{}，注册成功", instanceInfo.getAppName().toLowerCase());
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("{}，服务下线", event.getServerId());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{}，服务续约", event.getServerId());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("Eureka Server 启动");
    }
}
