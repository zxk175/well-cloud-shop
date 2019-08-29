package com.zxk175.well.generate;


import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author zxk175
 * @since 2019-08-29 14:15
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class GatewayGenerateTest {

    @Test
    public void generateProd() {
        String projectBasePath = MyCodeGenerate.getProjectBasePath(true);
        String basePath = projectBasePath + "/well-base/well-gateway/src/main";
        String controllerBasePath = projectBasePath + "/well-base/well-gateway/src/main";

        List<String> include = Lists.newArrayList();
        include.add("t_region");
        include.add("t_sys_user_token");

        MyCodeGenerate myCodeGenerate = new MyCodeGenerate(basePath, controllerBasePath, include);
        myCodeGenerate.init();
    }
}