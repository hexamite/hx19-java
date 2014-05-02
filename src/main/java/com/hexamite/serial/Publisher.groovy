package com.hexamite.serial

@Grab(group='org.zeromq', module='jeromq', version='0.3.2')
import org.zeromq.ZMQ

ZMQ.Context context = ZMQ.context(1)
ZMQ.Socket fromSerial = context.socket(ZMQ.PUB);
fromSerial.bind("tcp://*:5555");

int n
while(!Thread.currentThread().isInterrupted()) {
    fromSerial.send(("message " + n).getBytes())
    n++
    Thread.sleep(1000)
}

