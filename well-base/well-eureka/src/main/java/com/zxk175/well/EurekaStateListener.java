package com.zxk175.well;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zxk175
 * @since 2019-08-11 00:37
 */
@Slf4j
@Component
public class EurekaStateListener {

    private static final String FORMAT6 = "===>";


    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info("{} {}，注册成功", FORMAT6, instanceInfo.getAppName().toLowerCase());
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("{} {}，服务下线", FORMAT6, event.getServerId());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{} {}，服务续约", FORMAT6, event.getServerId());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        long timestamp = event.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("{} 当前时间：{}，EurekaServer启动成功 ", FORMAT6, sdf.format(new Date(timestamp)));
    }
}
