package com.zxk175.well.config.mbp;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zxk175
 * @since 2019/03/26 15:19
 */
@Slf4j
public class MyMybatisMapperRefresh {

    private final Resource[] mapperLocations;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * xml文件目录
     */
    private Set<String> fileSet;
    private Configuration configuration;


    public MyMybatisMapperRefresh(String mapperLocations, SqlSessionFactory sqlSessionFactory) throws IOException {
        this.mapperLocations = getResources(mapperLocations).clone();
        this.sqlSessionFactory = sqlSessionFactory;
        this.configuration = sqlSessionFactory.getConfiguration();
        this.watchThreadRun();
        log.debug(">>> MyMybatisMapperRefresh initialized... (MyBatis xml文件热部署模块初始完成，生产模式需要移除)");
    }

    private Resource[] getResources(String mapperLocations) throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(mapperLocations);
    }

    private void watchThreadRun() {
        new Thread(() -> {
            if (fileSet == null) {
                fileSet = Sets.newHashSet();
                if (mapperLocations != null) {
                    startWatchService();
                }
            }
        }, "MBP-Xml-Refresh").start();
    }

    private void startWatchService() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Set<String> watchPaths = listWatchPaths();
            for (String path : watchPaths) {
                try {
                    log.info("监控文件夹：" + path);
                    Paths.get(path).register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
                } catch (Exception e) {
                    throw new RuntimeException("ERROR：注册xml监听事件", e);
                }
            }

            while (true) {
                WatchKey watchKey = watcher.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> watchEvent : watchEvents) {
                    WatchEvent.Kind<?> kind = watchEvent.kind();
                    // 当前磁盘不可用
                    if (StandardWatchEventKinds.OVERFLOW == kind) {
                        continue;
                    }

                    Path path = (Path) watchKey.watchable();
                    Path fileName = (Path) watchEvent.context();
                    String filePath = path.toString() + File.separator + fileName;
                    // 重新加载xml
                    refresh(new FileSystemResource(filePath));
                    log.debug("Xml文件改变：" + filePath);
                }

                boolean reset = watchKey.reset();
                if (reset) {
                    log.debug("watchKey reset");
                } else {
                    // 已经关闭了进程
                    log.debug("exit watch server");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Mybatis xml监控失败");
        }
    }

    /**
     * 获取监控文件夹路径
     * <p>
     * https://blog.csdn.net/chao_1990/article/details/85116284
     *
     * @return ignore
     * @throws Exception ignore
     */
    private Set<String> listWatchPaths() throws Exception {
        Set<String> set = Sets.newHashSet();

        for (Resource resource : this.mapperLocations) {
            set.add(resource.getFile().getParentFile().getAbsolutePath());
        }

        return set;
    }

    private void refresh(Resource resource) {
        this.configuration = sqlSessionFactory.getConfiguration();
        Class<? extends Configuration> configurationClass = configuration.getClass();
        Class<?> superclass = configurationClass.getSuperclass();
        try {
            XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
            XNode context = xPathParser.evalNode("/mapper");

            removeConfiguration(superclass, resource, context);

            new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments()).parse();

            log.debug("Xml文件改变，重新加载：'" + resource + "', 成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Refresh Exception：" + e.getMessage());
        } finally {
            ErrorContext.instance().reset();
        }
    }

    /**
     * 删除不必要的配置项.
     *
     * @param configurationClass ignore
     * @throws Exception ignore
     */
    private void removeConfiguration(Class<?> configurationClass, Resource resource, XNode context) throws Exception {
        // 清理原有资源
        clearSet(configurationClass, resource);

        // 还可以清除 "caches", "resultMaps", "sqlFragments", "parameterMaps", "keyGenerators"
        String namespace = context.getStringAttribute("namespace");
        clearMap(configurationClass, context.getChildren(), namespace);
    }

    private void clearMap(Class<?> configurationClass, List<XNode> list, String namespace) throws Exception {
        Field field = getConfigurationField(configurationClass, "mappedStatements");

        Map mapConfig = (Map) field.get(configuration);
        for (XNode node : list) {
            String id = node.getStringAttribute("id");
            mapConfig.remove(id);
            mapConfig.remove(namespace + StringPool.DOT + id);
        }
    }

    private void clearSet(Class<?> configurationClass, Resource resource) throws Exception {
        Field field = getConfigurationField(configurationClass, "loadedResources");

        Set loadedResourcesSet = (Set) field.get(configuration);
        loadedResourcesSet.remove(resource.toString());
    }

    private Field getConfigurationField(Class<?> classConfigClass, String fieldName) throws Exception {
        boolean isSupper = classConfigClass == MybatisConfiguration.class;
        if (isSupper) {
            classConfigClass = classConfigClass.getSuperclass();
        }

        Field field = classConfigClass.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field;
    }
}