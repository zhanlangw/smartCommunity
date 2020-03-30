package cn.bytecloud.smartCommunity.annotation;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({TYPE,METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {
    String value() default "";

}