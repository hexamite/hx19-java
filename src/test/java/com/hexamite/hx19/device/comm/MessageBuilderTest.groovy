package com.hexamite.hx19.device.comm

import org.junit.*

class MessageBuilderTest {

	@Test
	void testMessage() {
        /*
		def builder = new MessageBuilder()
		builder.message('R21') {
			serial 'xyz'
			store
			batteryStatus
			signalPower 3
			doppler off
			message {
				serial 'abc'
				message('T') {
					store
					registers
					signalPower 1
				}
			}
		}
		assert builder.code == 'R21& <xyz> ee b p3 ms0 [!& <abc> [T& ee w p1]]'
		*/
	}
}
