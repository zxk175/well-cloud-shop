package com.zxk175.well.generator;

import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author zxk175
 * @since 2019/03/29 14:11
 */
public class UserGenerateTest {

    public static void main(String[] args) {
        UserGenerateTest userGenerateTest = new UserGenerateTest();
        userGenerateTest.generateTestOne();
    }

    private void generateTestOne() {
        String projectBasePath = MyCodeGenerator.getProjectBasePath(true);
        String basePath = projectBasePath + "/well-cloud-shop/well-provider/well-provider-user/src/main";

        List<String> include = Lists.newArrayList();
        include.add("t_sys_user");
        include.add("t_sys_user_role");
        include.add("t_sys_role");
        include.add("t_sys_role_menu");
        include.add("t_sys_menu");
        include.add("t_sys_user_token");

        MyCodeGenerator myCodeGenerator = new MyCodeGenerator(basePath, basePath, include);

        myCodeGenerator.init();
    }
}
