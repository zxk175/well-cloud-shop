package com.zxk175.well.common.util;

import com.google.common.collect.Maps;
import com.zxk175.well.common.util.spring.SpringContextUtil;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
import java.util.Properties;

/**
 * @author zxk175
 * @since 17/7/12
 */
public class YmlUtil {

    public static Map<String, Object> yml2Map(String path) {
        try {
            YamlMapFactoryBean yml = new YamlMapFactoryBean();
            yml.setResources(new ClassPathResource(path));
            return yml.getObject();
        } catch (Exception e) {
            return getPropertiesByEnv();
        }
    }

    public static Properties yml2Properties(String path) {
        try {
            YamlPropertiesFactoryBean yml = new YamlPropertiesFactoryBean();
            yml.setResources(new ClassPathResource(path));
            return yml.getObject();
        } catch (Exception e) {
            Map<String, Object> data = getPropertiesByEnv();

            Properties properties = new Properties();
            properties.putAll(data);

            return properties;
        }
    }

    private static Map<String, Object> getPropertiesByEnv() {
        String key = "spring.profiles.active";

        Environment env = SpringContextUtil.getBean(Environment.class);
        String active = env.getProperty(key);

        Map<String, Object> data = Maps.newHashMap();
        data.put(key, active);

        return data;
    }
}
