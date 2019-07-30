package com.zxk175.well.generator;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019/03/13 12:09
 */
@Data
public class MyCodeGenerator {

    private String basePath;
    private String controllerBasePath;
    private String javaBasePath;
    private String javaPath;
    private String xmlPath;
    private String[] include;


    public MyCodeGenerator(String basePath, String controllerBasePath) {
        this.basePath = basePath;
        this.controllerBasePath = controllerBasePath;
        this.javaBasePath = "com/zxk175/well/module/";
        this.javaPath = basePath + "/java/" + javaBasePath;
        this.xmlPath = basePath + "/resources/";
        this.include = null;
    }

    public MyCodeGenerator(String basePath, String controllerBasePath, List<String> include) {
        this.basePath = basePath;
        this.controllerBasePath = controllerBasePath;
        this.javaBasePath = "com/zxk175/well/module/";
        this.javaPath = basePath + "/java/" + javaBasePath;
        this.xmlPath = basePath + "/resources/";
        this.include = CollUtil.isEmpty(include) ? null : include.toArray(new String[0]);
    }

    public static String getProjectBasePath(boolean isParent) {
        String srcPath = System.getProperty("user.dir");
        srcPath = srcPath.replaceAll("\\\\", "/");
        return isParent ? new File(srcPath).getParent() : srcPath;
    }

    public void init() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = globalConfig();
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = dataSourceConfig();
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = packageConfig();
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = strategyConfig();
        mpg.setStrategy(strategy);

        // 模板配置
        TemplateConfig templateConfig = templateConfig(true);
        mpg.setTemplate(templateConfig);

        // 自定义配置
        mpg.setCfg(injectionConfig());

        // 模板引擎
        mpg.setTemplateEngine(new MyFreemarkerTemplateEngine());

        mpg.execute();

        System.exit(0);
    }

    private GlobalConfig globalConfig() {
        GlobalConfig gc = new GlobalConfig();
        // 是否覆盖已有文件
        gc.setFileOverride(false);
        // 开启 activeRecord 模式
        gc.setActiveRecord(true);
        // 是否打开输出目录
        gc.setOpen(false);
        // 开启Swagger2
        gc.setSwagger2(true);
        // xml 二级缓存
        gc.setEnableCache(false);
        // xml resultMap
        gc.setBaseResultMap(false);
        // xml columnList
        gc.setBaseColumnList(false);
        // IdType
        gc.setIdType(IdType.ID_WORKER);
        // 作者
        gc.setAuthor("zxk175");

        // 自定义文件命名
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");

        return gc;
    }

    private DataSourceConfig dataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);

        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/well_data?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&autoReconnectForPools=true&failOverReadOnly=false");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("well");
        dsc.setPassword("123456");

        return dsc;
    }

    private PackageConfig packageConfig() {
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("module");
        pc.setParent("com.zxk175.well");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setXml("mapper");

        return pc;
    }

    private StrategyConfig strategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        // 表前缀
        strategy.setTablePrefix("t_");
        // 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // controller父类
        strategy.setSuperControllerClass("com.zxk175.well.module.controller.BaseController");
        // 是否为lombok模型
        strategy.setEntityLombokModel(true);
        // 自定义需要填充的字段
        List<TableFill> tableFillList = Lists.newArrayList();
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
        strategy.setTableFillList(tableFillList);

        strategy.setInclude(include);

        return strategy;
    }

    private TemplateConfig templateConfig(boolean isNull) {
        String tmpBasePath = "templates";
        return new TemplateConfig()
                .setController(isNull ? null : tmpBasePath + "/controller.java.ftl")
                .setEntity(isNull ? null : tmpBasePath + "/entity.java.ftl")
                .setMapper(isNull ? null : tmpBasePath + "/mapper.java.ftl")
                .setXml(isNull ? null : tmpBasePath + "/mapper.xml.ftl")
                .setService(isNull ? null : tmpBasePath + "/service.java.ftl")
                .setServiceImpl(isNull ? null : tmpBasePath + "/serviceImpl.java.ftl");
    }

    private InjectionConfig injectionConfig() {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = Maps.newHashMap();
                map.put("since", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                this.setMap(map);
            }
        };

        cfg.setFileOutConfigList(fileOutConfigs(cfg));

        return cfg;
    }

    private List<FileOutConfig> fileOutConfigs(InjectionConfig cfg) {
        List<FileOutConfig> focList = Lists.newArrayList();

        PackageConfig packageConfig = packageConfig();
        TemplateConfig templateConfig = templateConfig(false);

        String controllerJavaBasePath = controllerBasePath + "/java/" + javaBasePath;
        String packagePath = javaBasePath.replace("/", ".");

        String controllerPath = controllerJavaBasePath + packageConfig.getController();
        String entityPath = javaPath + packageConfig.getEntity();
        String daoPath = javaPath + packageConfig.getMapper();
        String servicePath = javaPath + packageConfig.getService();
        String serviceImplPath = javaPath + packageConfig.getServiceImpl();

        focList.add(buildFileOutConfig(cfg, xmlPath + packageConfig.getXml(), packagePath, templateConfig.getXml(), "Mapper.xml"));

        focList.add(buildFileOutConfig(cfg, controllerPath, packagePath, templateConfig.getController(), "Controller.java"));

        focList.add(buildFileOutConfig(cfg, entityPath, packagePath, templateConfig.getEntity(false), ".java"));

        focList.add(buildFileOutConfig(cfg, daoPath, packagePath, templateConfig.getMapper(), "Dao.java"));

        focList.add(buildFileOutConfig(cfg, servicePath, packagePath, templateConfig.getService(), "Service.java"));

        focList.add(buildFileOutConfig(cfg, serviceImplPath, packagePath, templateConfig.getServiceImpl(), "ServiceImpl.java"));

        return focList;
    }

    private FileOutConfig buildFileOutConfig(InjectionConfig cfg, String javaPath, String packagePath, String templatePath, String suffix) {
        return new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String[] tableNames = tableInfo.getName().split("_");
                String moduleName = tableNames[1];

                ConfigBuilder config = cfg.getConfig();
                Map<String, String> packageInfo = config.getPackageInfo();
                String controller = packageInfo.get(ConstVal.CONTROLLER);
                if (!controller.contains(moduleName)) {
                    String modulePackName = "." + moduleName;
                    PackageConfig packageConfig = packageConfig();
                    packageInfo.put(ConstVal.CONTROLLER, packagePath + packageConfig.getController() + modulePackName);
                    packageInfo.put(ConstVal.ENTITY, packagePath + packageConfig.getEntity() + modulePackName);
                    packageInfo.put(ConstVal.SERVICE, packagePath + packageConfig.getService() + modulePackName);
                    packageInfo.put(ConstVal.SERVICE_IMPL, packagePath + packageConfig.getServiceImpl() + modulePackName);
                    packageInfo.put(ConstVal.MAPPER, packagePath + packageConfig.getMapper() + modulePackName);
                }

                return javaPath + "/" + moduleName + "/" + tableInfo.getEntityName() + suffix;
            }
        };
    }
}
