package com.datiba.util;

import com.datiba.config.annotation.IsPhoneNumber;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/4/2
 */
public class IsPhoneNumberValidator implements ConstraintValidator<IsPhoneNumber, String> {

    // 是否为必填
    private boolean required = false;

    @Override
    public void initialize(IsPhoneNumber constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return RegexUtils.isPhoneNumberValid(s);
        }
        else{
            if(StringUtils.isEmpty(s)){
                return true;
            }
            else {
                return RegexUtils.isPhoneNumberValid(s);
            }
        }
    }
}