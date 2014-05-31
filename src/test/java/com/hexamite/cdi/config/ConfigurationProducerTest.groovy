package com.hexamite.cdi.config

import org.jglue.cdiunit.AdditionalClasses
import org.jglue.cdiunit.CdiRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.zeromq.ZMQ
import javax.inject.Inject

@RunWith(CdiRunner)
class ConfigurationProducerTest {

    static {
        System.properties['configString'] = 'configValue'
    }

    @Inject Producer producer
    @Inject @Config String configString
    @Inject @Config @ScriptFile('dist/conf/fixedPoints.groovy') def fixed

    @Test
    void testInjectedConfigParameter() {
        assert configString == 'configValue'
    }

    @Test
    void testInjectedConfigScriptParameter() {
        assert fixed instanceof List
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
            assert value == producer.findMatchingSystemProperty(key)
        } finally {
            System.properties.remove(key)
        }
    }

}
