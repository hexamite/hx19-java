package com.hexamite.cdi

import org.zeromq.ZMQ

import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.InjectionPoint
import java.util.logging.Logger

class Producer {
    public @Produces Logger logger(InjectionPoint p) {
        Logger.getLogger(p.member.declaringClass.name);
    }
    public @Produces ZMQ.Context zmqContext(InjectionPoint p) {
        Logger.getLogger(p.member.declaringClass.name);
    }
}
