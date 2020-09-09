package com.zlgspace.easyreqpermission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface NeedPermission {
    String[] permissions() default {};
    String identifier() default "";
}
