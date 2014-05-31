package com.hexamite.hx19

import com.hexamite.cdi.config.Config
import com.hexamite.cdi.WeldContext
import com.hexamite.cdi.config.Producer
import com.hexamite.trilaterate.Point
import com.hexamite.trilaterate.Trilaterator
import org.zeromq.ZMQ

import javax.inject.Inject
import java.util.logging.Logger

/**
 * Subscribes to a stream of distance measurements from HX19 receivers, parses them, collects them into blocks corresponding
 * all the distances emanating from one transmitted ultrasound pulse, and calculates the position of the transmitter
 * and publishes it on a ZeroMQ socket.
 *
 * Limitation: Only 3 distance measures are considered and they must be from the same receivers as configured in
 * `fixedPoints`.
 * */
class PositionProvider {

    static {
        System.properties.host = 'localhost'
    }

    private static final int PORT_FROM_SERIAL = 5555
    private static final int PORT_POSITIONS = 5558

    @Inject private Logger logger
    @Inject private Trilaterator trilaterator
    @Inject private Parser parser
    @Inject @Config private String host

    @Inject private ZMQ.Context context
    private ZMQ.Socket distanceSubscriber
    private ZMQ.Socket positionPublisher

    static void main(args) {
        def provider = WeldContext.INSTANCE.getBean(PositionProvider)
        assert provider.host == 'localhost'
    }

    /**
     * Subscribes to distance messages from the serial server, forever receives messages,
     * calculates positions and publishes them on the positions port.
     * The format of the output is 'transmitter x y z' as bytes.
     * */
    def start(close) {
        logger.info 'connecting...'

        distanceSubscriber = context.socket(ZMQ.SUB)
        distanceSubscriber.connect("tcp://$host:$PORT_FROM_SERIAL")
        distanceSubscriber.subscribe(ZMQ.SUBSCRIPTION_ALL)

        positionPublisher = context.socket(ZMQ.PUB);
        positionPublisher.bind("tcp://*:$PORT_POSITIONS");

        parser = new Parser()
        parser.consume = { transmitter, distances -> consumeBlock(transmitter, distances)}

        while (!close()) {
            logger.info 'Receiving...'
            byte[] bytes = distanceSubscriber.recv()
            logger.info "Received ${new String(bytes)}"
            parser.parse(new String(bytes))
        }
    }

    def private consumeBlock(int transmitter, Map distances) {
        Point p = trilaterator.trilaterate(distances.sort().values().toList())
        println "publishing distanes $transmitter $p.x $p.y $p.z"
        positionPublisher.send("$transmitter $p.x $p.y $p.z".bytes)
    }
}
