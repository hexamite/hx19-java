package com.hexamite.hx19.device

import groovy.transform.Canonical;


/**
 * Configures and controls a set of HX19 devices and provides a xyz position.
 * */
@Canonical
class Tracker {
	
	Monitor monitor
	List soundEmitters = []
	List soundListeners = []
	
	static void main(args) {
		// assert args.size() == 1 : usage()
		
		def builder = new ObjectGraphBuilder()
		builder.classNameResolver = "com.hexamite.hx19.device"
		
		def monitor = 	builder.tracker {			monitor(name: 'S21')
			soundEmitter(name: 'S21') { point(x: 12345, y: 23456, z: 34567) }
			soundEmitter(name: 'S22') { point(x: 12345, y: 23456, z: 34567) }
			soundEmitter(name: 'S23') { point(x: 12345, y: 23456, z: 34567) }
			soundListener(name: 'S41')
		}
		println monitor
	}
	
	String toString() {
		"""\
        ${monitor}
        ${soundEmitters.join('\n')}
        ${soundListeners .join('\n')}""".replaceAll(/(?m)^ +/, '')
        
	}
	
	def usage() {
		println """\
        java -cp <path-to-com.hexamite.hx19.jar> Tracker <path-to-config-file>
        """.stripIndent()
	}
    
	
}
