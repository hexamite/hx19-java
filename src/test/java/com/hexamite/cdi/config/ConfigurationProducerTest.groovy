package com.hexamite.cdi.config

import com.hexamite.cdi.config.Config
import com.hexamite.cdi.config.Producer
import com.hexamite.trilaterate.Point
import org.jglue.cdiunit.CdiRunner
import org.junit.Test
import org.junit.runner.RunWith

import javax.inject.Inject

@RunWith(CdiRunner)
class ConfigurationProducerTest {

    @Inject Producer configurationProducer

    static {
        System.properties['configString'] = 'configValue'
    }

    @Inject @Config String configString
    @Inject @Config @Script(file='example/conf/fixedPoints.groovy') def fixed

    @Test
    void testInjectedConfigParameter() {
        assert configString == 'configValue'
        assert fixed instanceof Map
    }

    @Test
    void testInjectedConfigScriptParameter() {
        assert configString == 'configValue'
        assert fixed instanceof Map
    }

    @Test
	void testFindMatchingSystemProperty() {
        testFindMatchingSystemProperty('a.b.C.x', 'y')
        testFindMatchingSystemProperty('a.b.x', 'y')
        testFindMatchingSystemProperty('a.x', 'y')
        testFindMatchingSystemProperty('x', 'y')
    }

    def testFindMatchingSystemProperty(key, value) {
        System.properties[key] = value
        try {
            assert value == configurationProducer.findMatchingSystemProperty(key)
        } finally {
            System.properties.remove(key)
        }
    }

}
