package com.zxk175.well.generate;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zxk175
 * @since 2019-08-29 14:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
class MyTemplateConfig extends TemplateConfig {
    
}
