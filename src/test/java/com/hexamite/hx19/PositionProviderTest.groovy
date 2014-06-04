package com.hexamite.hx19

import com.hexamite.cdi.config.Config
import com.hexamite.cdi.config.Producer
import org.jglue.cdiunit.AdditionalClasses
import org.jglue.cdiunit.CdiRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.zeromq.ZMQ
import org.zeromq.ZMQException

import javax.inject.Inject

@RunWith(CdiRunner)
@AdditionalClasses([Producer])
class PositionProviderTest {

    static {
        System.properties['host1'] = 'localhost'
        // date, source, logger, level, message, thrown
        System.properties['java.util.logging.SimpleFormatter.format']='%4$s %2$s%n %1$tM:%1$tS %5$s %6$s%n'
    }

    private final static int PORT_FROM_SERIAL = 5555; // pub
    private static final int PORT_POSITIONS = 5558

    @Inject @Config private String host1
    @Inject PositionProvider positionProvider
    @Inject private ZMQ.Context context
    ZMQ.Socket distancePublisher
    ZMQ.Socket positionSubscriber

    @Test
    void testProvidePosition() {
        assert host1
        assert positionProvider
        long tStop = System.currentTimeMillis() + 3000
        publishDistances()
        subscribeToPositions()
        positionProvider.start({-> System.currentTimeMillis() > tStop})
    }

    def publishDistances() {
        new Thread().start {
            distancePublisher = context.socket(ZMQ.PUB);
            distancePublisher.bind("tcp://*:$PORT_FROM_SERIAL");
            (1..5).each { i ->
                Thread.sleep 1000
                [
                        "X2$i",
                        "R50 P2${i}_$i A3217",
                        "R51 P2${i}_$i A3158",
                        "R52 P2${i}_$i A3073",
                        "R53 P2${i}_$i A3015",
                ].each {
                    println "sending $it"
                    distancePublisher.send(it.bytes)
                    println "send $it"
                }
            }
            distancePublisher.close()
        }
    }


    def subscribeToPositions() {
        new Thread().start {
            positionSubscriber = context.socket(ZMQ.SUB);
            positionSubscriber.connect("tcp://*:$PORT_POSITIONS");
            forTime(3000) {
                println("##################### Receiving position ...")
                byte[] bytes = positionSubscriber.recv()
                if(bytes) {
                    println("##################### Received position ${new String(bytes)}")
                }
                Thread.sleep(1000)
            }
            positionSubscriber.close()
        }
    }

    def now() { System.currentTimeMillis()}

    def forTime(long interval, Closure closure) {
        long t0 = now()
        while(now() < t0 + interval) {
            closure()
        }
    }
}
