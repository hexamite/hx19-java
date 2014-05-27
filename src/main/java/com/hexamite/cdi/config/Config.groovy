package com.hexamite.cdi.config

import static java.lang.annotation.ElementType.*
import static java.lang.annotation.RetentionPolicy.RUNTIME

import java.lang.annotation.Retention
import java.lang.annotation.Target

import javax.enterprise.util.Nonbinding
import javax.inject.Qualifier

@Qualifier
@Target([TYPE, METHOD, FIELD, PARAMETER])
@Retention(RUNTIME)
public @interface Config {
    @Nonbinding String name() default ""
    @Nonbinding String value() default ""
}