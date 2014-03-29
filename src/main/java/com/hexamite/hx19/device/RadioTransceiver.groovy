package com.hexamite.hx19.device

import groovy.transform.Canonical;

/**
 * Repesents  an radio transceiver, in the HX19 positioning system.
 * */
@Canonical
class RadioTransceiver {      
	String name
	String version
	
	boolean syncStrobe
	int acquisitionRate
	int batteryStatus
	int firstTagInQueue
	boolean deepSleep
	boolean monitorBattery
	boolean countRecords
	boolean led
	boolean noiceRecovery
	boolean directNetworkAccess
	boolean powerSavings
	boolean serialPin
	boolean doppler
	boolean rfid
	int signalPower
	int inputChannel
	boolean numTags
	int outputChannel
	boolean workRegisters
}


/*
MRT  !    message.to: 'all'
M    $    syncStrobe: true
M    %    syncStrobe: false
MRT  < >  serial
MRT  R#&  message.to: 'R#'
MRT  R&   message.to: 'R'
MRT  [    message()  { }                                        send the message
MRT  a#:  acquisitionRate: <integer>
MRT  b    expandMemoryRefs: true|false                   expand ^# to # chars from memory                                  
MRT  bt   batteryStatus
MRT  ee   store  
M    f#   firstTagInQueue: <integer>
MRT  h    deepSleep: true|false
  T  mb#  monitorBattery: true|false
  T  mc#  countRecords: true|false
MRT  md#  ledOn: true|false
 R   mn#  noiceRecovery: true|false
  T  mn#  directNetworkAccess: true|false
 RT  mp#  powerSavings: true|false
  T  mp#  serialPinOn: true|false
 R   ms#  doppler: true|false
  T  mx#  rfidOn: true|false
  T  n#   
 RT  p#   signalPower: 0|1|2|3
M    px#  signalPower: 0|1|2|3
MRT  q#   receiverOutputResultQueue
MRT  r#   inputChannel: 1..125
M    s#   numTags
MRT  t#   outputChannel: 1..125
MRT  v    version: true|false
MRT  w    workRegisters
 
*/
 