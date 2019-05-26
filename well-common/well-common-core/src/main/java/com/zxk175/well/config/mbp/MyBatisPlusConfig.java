package com.zxk175.well.config.mbp;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.google.common.collect.Lists;
import com.zxk175.well.common.util.spring.SpringActiveUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @author zxk175
 * @since 2017/10/19 10:41
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.zxk175.well.**.dao.**")
public class MyBatisPlusConfig {

    @Bean(name = "druidDataSource", initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.datasource.druid")
    public DruidDataSource druidDataSource() {
        return DruidDataSourceBuilder
                .create()
                .build();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        List<ISqlParser> sqlParserList = Lists.newArrayList();
        // 攻击SQL 阻断解析器、加入解析链
        sqlParserList.add(new MyBlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;
    }

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        if (SpringActiveUtil.getBoolean()) {
            // 设置 dev test 环境开启
            return new PerformanceInterceptor()
                    .setWriteInLog(true)
                    .setMaxTime(100)
                    .setFormat(true);
        }

        return null;
    }

    @Bean("transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("druidDataSource") DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }
}
