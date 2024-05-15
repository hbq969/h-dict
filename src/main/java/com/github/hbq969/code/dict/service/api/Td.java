package com.github.hbq969.code.dict.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/15 20:48
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Td {
    /**
     * 是否支持转义
     *
     * @return
     */
    boolean enable() default true;

    /**
     * 转义后的字段名称
     *
     * @return
     */
    String fmtFieldName();
}
