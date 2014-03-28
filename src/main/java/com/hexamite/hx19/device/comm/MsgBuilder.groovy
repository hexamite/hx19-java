package com.hexamite.hx19.device.comm

import groovy.lang.Closure;

class MsgBuilder {
	
	int depth = 0
	
	String code = "" 
	
	private int asNum(value) { value ? 1 : 0 }
	
	def getOn() { true }
	
	def getOff() { false }
	
    def acquisitionRate(int value) { code += " a$value}" }

	def countRecords(boolean value) { code += " mc${asNum(value)}" }
	
	def deepSleep(boolean value) { code += " h${asNum(value)}" }
	
	def directNetworkAccess(boolean value) { code += " mn${asNum(value)}" }
	
	def doppler(boolean value) { code += " ms${asNum(value)}" }
	
	def led(boolean value) { code += " md${asNum(value)}" }
	
	def monitorBattery(boolean value) { code += " mb${asNum(value)}" }
	
	def noiceRecovery(boolean value) { code += " mn${asNum(value)}" }
	
	def powerSavings(boolean value) { code += " mp${asNum(value)}" }
	
	def rfidOn(boolean value) { code += " mx${asNum(value)}" }
	
	/** Turn the serial pin on or off. */
	def serialPinOn(boolean value) { code += " mp${asNum(value)}" }
	
	/** Turn sync stroping on or off. */
	def syncStrope(boolean value) { code += value ? '$' : '%' }  
	
	def serial(String value) { code += " <$value>"}
	
	def firstTagInQueue(int value) { code += " f$value}" }
	
	def inputChannel(int value) { assert value in 1..25; code += " r$value}" }
	
	def outputChannel(int value) { assert value in 1..25; code += " t$value}" }
	
	def signalPower(int value) { assert value in 0..3; code += " p$value" }
	
	def getVersion() { code += ' v' }
	
	def getBatteryStatus() { code += ' b' }
	
	def getStore() { code += " ee" }  
	
    def getRegisters() { code += ' w' }
	
	def message(closure) {
		message('!', closure)
	}
	
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
