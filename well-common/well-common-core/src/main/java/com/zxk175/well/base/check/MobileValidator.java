package com.zxk175.well.base.check;

import com.zxk175.well.core.util.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zxk175
 * @since 2018/8/11 18:01
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public void initialize(Mobile constraintAnnotation) {
        // to do nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return RegexUtil.isMobile(value);
    }
}

