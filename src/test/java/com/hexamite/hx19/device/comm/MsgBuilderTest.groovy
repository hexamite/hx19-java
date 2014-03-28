package com.hexamite.hx19.device.comm;

import static org.junit.Assert.*
import org.junit.*

class MsgBuilderTest {

	@Test
	void testMessage() {
		def b = new MsgBuilder()
		b.message('R21') {
			serial 'xyz'
			store
			batteryStatus
			signalPower 3
			doppler off
			sync
			message {
				serial 'abc'
				message('T') {
					store
					registers
					signalPower 1
				}
			}
		}
		assert b.code == 'R21& <xyz> ee b p3 ms0 [!& <abc> [T& ee w p1]]'
	}
}
