package com.hexamite.hx19

import com.hexamite.cdi.config.Producer
import com.hexamite.hx19.Parser
import org.jglue.cdiunit.AdditionalClasses
import org.jglue.cdiunit.CdiRunner
import org.junit.Test
import org.junit.runner.RunWith

import javax.inject.Inject

@RunWith(CdiRunner)
@AdditionalClasses([Producer])
class ParserTest {

    @Inject Parser parser

    @Test
    void testParse() {
        boolean consumed = false
        parser.consume = { transmitter, distances ->
            assert [transmitter, distances] == [31, [21:1234, 22:2234, 23:3234]]
            consumed = true
        }
        parser.parse("R21 P31_123 A1234")
        parser.parse("R22 P31_123 A2234")
        parser.parse("R23 P31_123 A3234")
        parser.parse("R24 P31_124 A4234") // <-- block done, consume gets called
        assert consumed
    }


}
