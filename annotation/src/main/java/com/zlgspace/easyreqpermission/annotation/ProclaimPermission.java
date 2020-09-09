package com.zlgspace.easyreqpermission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 宣告
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ProclaimPermission {
    String[] permissions() default {};
    String identifier() default "";
}
