package com.hexamite.hx19.device.comm

import groovy.lang.Closure;


/**
 * Allows a structural, declarative definition of HX19 messages.
 *
 * Benefits: Easier reading, IDE suggestions, auto-completion, syntax highlighting, error highlighting and context sensitive help.
 *
 * @See test case for an example.
 *
 * */
class MessageBuilder {
	
	private int depth = 0
	
	String code = "" 
	
	private int asNum(value) { value ? 1 : 0 }

    /** 'on' is alias for true. */
	def getOn() { true }

    /** 'off' is alias for false. */
	def getOff() { false }

    /** Set the rate at which the ... provides positioning information. */
    def acquisitionRate(int value) { code += " a$value}" }

    /** Prepend a sequential value to messages. */
	def countRecords(boolean value) { code += " mc${asNum(value)}" }

    /** Go into deep sleep. The rest of the message will be ignored. */
	def deepSleep(boolean value) { code += " h${asNum(value)}" }

    /** */
	def directNetworkAccess(boolean value) { code += " mn${asNum(value)}" }

    /** Send doppler value as well as distance value. */
	def doppler(boolean value) { code += " ms${asNum(value)}" }

    /** Blink the led when it sends or receives messages. */
	def led(boolean value) { code += " md${asNum(value)}" }

    /***/
	def monitorBattery(boolean value) { code += " mb${asNum(value)}" }

    /***/
	def noiceRecovery(boolean value) { code += " mn${asNum(value)}" }
	
	def powerSavings(boolean value) { code += " mp${asNum(value)}" }
	
	def rfidOn(boolean value) { code += " mx${asNum(value)}" }
	
	/** Turn the serial pin on or off. */
	def serialPinOn(boolean value) { code += " mp${asNum(value)}" }
	
	/** Turn sync stroping on or off. */
	def syncStrope(boolean value) { code += value ? ' $' : ' %' }

    /** Send `value` to the serial port. */
	def serial(String value) { code += " <$value>"}

    /**  */
	def firstTagInQueue(int value) { code += " f$value}" }

    /** Set the input channel. */
	def inputChannel(int value) { assert value in 1..25; code += " r$value}" }

    /** Set the output channel. */
	def outputChannel(int value) { assert value in 1..25; code += " t$value}" }

    /** Set the signal power. */
	def signalPower(int value) { assert value in 0..3; code += " p$value" }

    /** Broadcast version number. */
	def getVersion() { code += ' v' }

    /** Broadcast battery status. */
	def getBatteryStatus() { code += ' b' }

    /** Store current settings in non-volatile memory. */
	def getStore() { code += " ee" }  

    /** Broadcast the value in work registers. */
    def getRegisters() { code += ' w' }

    /** Broadcast an embedded message to all devices. */
	def message(closure) {
		message('!', closure)
	}

    /** Broadcast an embedded message to `recipient`. */
	def message(recipient, closure) {
		depth++
		code += (depth > 1 ? ' [' : '') + recipient + '&'
		Closure clone = closure.clone() 
		clone.delegate = this
		clone.resolveStrategy = Closure.DELEGATE_ONLY
		clone()
		code += depth > 1 ? ']' : ''
		depth--
	}
	
}
