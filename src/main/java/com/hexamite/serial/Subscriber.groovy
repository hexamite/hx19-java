package com.hexamite.serial

@Grab(group='org.zeromq', module='jeromq', version='0.3.2')
import org.zeromq.ZMQ

ZMQ.Context context = ZMQ.context(1)
ZMQ.Socket fromSerial = context.socket(ZMQ.SUB)
fromSerial.connect("tcp://*:5555")
fromSerial.subscribe(ZMQ.SUBSCRIPTION_ALL)

while(!Thread.currentThread().isInterrupted()) {
    byte[] bytes = fromSerial.recv()
    println "UserInterface: fromSerial: " + new String(bytes)
}

fromSerial.close();
context.term();

