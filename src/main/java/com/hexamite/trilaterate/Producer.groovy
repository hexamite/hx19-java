package com.hexamite.trilaterate

import org.zeromq.ZMQ

import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.InjectionPoint

class Producer {
    public @Produces ZMQ.Context zmqContext(InjectionPoint p) {
        ZMQ.context(1);
    }
}
