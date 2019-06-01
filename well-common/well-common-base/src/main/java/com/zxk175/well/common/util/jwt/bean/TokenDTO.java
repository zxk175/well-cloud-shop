package com.zxk175.well.common.util.jwt.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zxk175
 * @since 2019/03/23 21:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String iat;

    private Long ttl;

    private String token;

    private Long expireIn;


    public Long getTtl() {
        return ttl / 1000;
    }
}
