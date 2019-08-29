package com.zxk175.well.module.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-29 16:42
 */
@Data
public class GatewayPredicateDefinition {

    private String name;
    
    private Map<String, String> args = new LinkedHashMap<>();
}
