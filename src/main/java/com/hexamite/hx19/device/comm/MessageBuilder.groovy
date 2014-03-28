package com.hexamite.hx19.device.comm

import groovy.transform.*


class MessageBuilder {
	
	Message message
	private Boolean passengersMode = false

	Message message(Closure definition) {
		message(null, definition)
	}
	
	Message message(Map params, Closure definition) {
		assert params?.to: '(to: <recipient>) is required for message'
		message = new Message()
		params?.each { key, value ->
			assert message.hasProperty(key)
			message[key] = value
		}
		runClosure definition
		message
	}

	void passengers(Closure names) {
		passengersMode = true
		runClosure names
		passengersMode = false
	}

	void name(String personName) {
		if(passengersMode) {
			message.passengers << new Person(name: personName)
		} else {
			throw new IllegalStateException("name() only allowed in passengers context.")
		}
	}

	def methodMissing(String name, arguments) {
		if(name in ['to', 'from']) {
			def airport = arguments[0].split(',')
			def airPortname = airport[0].trim()
			def city = airport[1].trim()
			message.flight."$name" = new Airport(name: airPortname, city: city)
		}
	}

	def propertyMissing(String name) {
		if(name == 'retourFlight') {
			message.retourFlight = true
		}
	}

	private runClosure(Closure closure) {
		Closure clone = closure.clone() // Create clone of closure for threading access
		clone.delegate = this
		clone.resolveStrategy = Closure.DELEGATE_ONLY
		clone()
	}
	
	static void main(String[] args) {
		def message = new MessageBuilder().message(to: '!') {
			
			message(to: 'R') {
			
			}
			
			passengers {
				name 'mrhaki'
				name 'Hubert A. Klein Ikkink'
			}
			from 'Schiphol, Amsterdam'
			to 'Kastrup, Copenhagen'
			retourFlight
		}
		
		println message
		
		assert message.flight.from == new Airport(name: 'Schiphol', city: 'Amsterdam')
		assert message.flight.to == new Airport(name: 'Kastrup', city: 'Copenhagen')
		assert message.passengers.size() == 2
		assert message.passengers == [new Person(name: 'mrhaki'), new Person(name: 'Hubert A. Klein Ikkink')]
		assert message.retourFlight
	}
}

@Canonical
class Message {
	String to
	Boolean store
	Flight flight = new Flight()
	List<Person> passengers = []
	Boolean retourFlight = false
	String toDeviceFormat() {
		"${to == 'all' ? '!' : "${to}&"}" +
		"${store ? 'ee' : ''}" 
	}
}



@Canonical
class Device { 
	Integer aquisitionRate
	Boolean deepSleep
	Boolean monitorBattery
	Integer signalPower
	Integer outputChannel
	
	/*
	MRT  !    message.to: 'all'
	M    $    device.syncStrobe: true
	M    %    device.syncStrobe: false
	MRT  < >  serial
	MRT  R#&  message.to: 'R#'
	MRT  R&   message.to: 'R'
	MRT  [    message()  { }                                        send the message
	MRT  a#:  device.acquisitionRate: <integer>
	MRT  bt   query.batteryStatus
	MRT  ee   device.store: true|false
	M    f#   device.firstTagInQueue: <integer>
	MRT  h    device.deepSleep: true|false
	  T  mb#  device.monitorBattery: true|false
	  T  mc#  device.countRecords: true|false
	MRT  md#  device.ledOn: true|false
	 R   mn#  device.noiceRecovery: true|false
	  T  mn#  device.directNetworkAccess: true|false
	 RT  mp#  device.powerSavings: true|false
	  T  mp#  device.serialPinOn: true|false
	 R   ms#  device.doppler: true|false
	  T  mx#  device.rfidOn: true|false
	  T  n#
	 RT  p#   device.signalPower: 0|1|2|3
	M    px#  device.signalPower: 0|1|2|3
	MRT  q#   device.receiverOutputResultQueue
	MRT  r#   device.inputChannel: 1..125
	M    s#   device.numTags
	MRT  t#   device.outputChannel: 1..125
	MRT  v    query.version: true|false
	MRT  w    query.workRegisters
	*/
}

@Canonical
class Person { String name }

@Canonical
class Airport { String name, city }

@Canonical
class Flight { Airport from, to }
