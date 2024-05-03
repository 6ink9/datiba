package com.datiba.config.annotation;

import com.datiba.util.IsPhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = { IsPhoneNumberValidator.class }
)
public @interface IsPhoneNumber {
 
    boolean required() default true;
 
    String message() default "手机号码格式错误";
 
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
 
}