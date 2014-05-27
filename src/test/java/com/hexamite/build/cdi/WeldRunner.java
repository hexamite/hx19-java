package com.hexamite.build.cdi;

import com.hexamite.cdi.WeldContext;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldRunner  extends BlockJUnit4ClassRunner {

    public WeldRunner(Class<Object> clazz) throws InitializationError, InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() {
        final Class<?> test = getTestClass().getJavaClass();
        return WeldContext.INSTANCE.getBean(test);
    }
}
