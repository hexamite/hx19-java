package com.hexamite.cdi.config

import javax.enterprise.util.Nonbinding
import javax.inject.Qualifier
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.*
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Qualifier
@Target([TYPE, METHOD, FIELD, PARAMETER])
@Retention(RUNTIME)
public @interface Script {
    @Nonbinding String file() default ""
}