package com.hexamite.cdi.config

import com.hexamite.util.Evaluate

import java.util.logging.Logger
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class Producer {

    @Produces @Config @Script Object getConfigurationScript(InjectionPoint p) {
        def annotation = p.getAnnotated().getAnnotation(Script)
        File file = annotation.file() as File
        new Evaluate(file.text).eval()
    }

    @Produces @Config String getStringConfiguration(InjectionPoint p) {
        return (String) getConfiguration(p);
    }

    def getConfiguration(InjectionPoint p) {
        def annotation = p.getAnnotated().getAnnotation(Config)
        String name = annotation.name() ?: p.member.declaringClass.name + "." + p.member.name
        String value = annotation.value();
        getProperty(String, name, value)
    }

    public String getProperty(name) {
        getProperty(String, name, null);
    }

    public <T> T getProperty(Class<T> clazz, String name, String defaultValue) {
        try {
            T result = null;
            String value = findMatchingSystemProperty(name);
            if(clazz != String.class) {
                result = clazz.getConstructor(String).newInstance(value);
            } else {
                result = (T) value;
            }
            Logger.getLogger(Producer.name).info(name + "=" + result);
            return result;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String findMatchingSystemProperty(String name) {
        def value = null
        def steps = name.split(/\./)[0..-1]
        def simpleName = steps[-1]
        for(def i = steps.size() - 2; !value && i>=0; i--) {
            value = System.properties[steps[0..i].join('.') + '.' + simpleName]
        }
        if(value != null) {
            return value
        }
        return System.properties[simpleName]
    }
}