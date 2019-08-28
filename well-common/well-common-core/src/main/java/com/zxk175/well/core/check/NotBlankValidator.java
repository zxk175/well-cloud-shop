package com.zxk175.well.core.check;

import com.zxk175.well.base.util.MyStrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zxk175
 * @since 2018/8/11 18:01
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {

    @Override
    public void initialize(NotBlank constraintAnnotation) {
        // to do nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return MyStrUtil.isNotBlank(value);
    }
}
