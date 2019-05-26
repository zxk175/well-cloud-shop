package com.zxk175.well.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zxk175
 * @since 2019/03/23 20:27
 */
public class JwtToken implements AuthenticationToken {

    private String token;


    JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
