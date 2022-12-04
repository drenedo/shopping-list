package me.renedo.shared;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * A wrapper for Spring Service annotation. It's used to avoid the explicits references to Spring framework.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.stereotype.Service
public @interface Service {
    @AliasFor(annotation = org.springframework.stereotype.Service.class)
    String value() default "";
}
