package com.hexamite.trilaterate

import com.hexamite.cdi.config.Config
import com.hexamite.cdi.WeldContext
import org.zeromq.ZMQ

import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * Subscribes to a stream of distance measurements from HX19 receivers, parses them, collects them into blocks corresponding
 * all the distances emanating from one transmitted ultrasound pulse, and calculates the position of the transmitter
 * and publishes it on a ZeroMQ socket.
 *
 * Limitation: Only 3 distance measures are considered and they must be from the same receivers as configured in
 * `fixedPoints`.
 * */
class PositionProvider {

    private static final int PORT_FROM_SERIAL = 5555
    private static final int PORT_POSITIONS = 5558

    @Inject private Trilaterator trilaterator
    @Inject private Parser parser
    @Inject @Config private String host

    @Inject private ZMQ.Context context
    private ZMQ.Socket distanceSubscriber
    private ZMQ.Socket positionPublisher

    def fixedPoints = Eval.eval('/home/tk/workspace/hx19-java/example/conf/fixedPoints.groovy' as File)

    static void main(args) {
        def provider = WeldContext.INSTANCE.getBean(PositionProvider)
    }

    PositionProvider(/**String host**/) {
        this.host = 'localhost' //host
    }

    /**
     * Subscribes to distance messages from the serial server, forever receives messages,
     * calculates positions and publishes them on the positions port.
     * The format of the output is 'transmitter x y z' as bytes.
     * */
    @PostConstruct
    def connect() {
        println 'connecting...'
        context =  ZMQ.context(1)

        distanceSubscriber = context.socket(ZMQ.SUB)
        distanceSubscriber.connect("tcp://$host:$PORT_FROM_SERIAL")
        distanceSubscriber.subscribe(ZMQ.SUBSCRIPTION_ALL)

        positionPublisher = context.socket(ZMQ.PUB);
        positionPublisher.bind("tcp://*:$PORT_POSITIONS");

        trilaterator = new Trilaterator(fixedPoints.sort().values().toList())

        parser = new Parser()
        parser.consume = { transmitter, distances -> consumeBlock(transmitter, distances)}

        while (true) {
            byte[] bytes = distanceSubscriber.recv()
            parser.parse(new String(bytes))
        }
    }

    def consumeBlock(int transmitter, Map distances) {
        Point p = trilaterator.trilaterate(distances.sort().values())
        positionPublisher.send("$transmitter $p.x $p.y $p.z".toByteArray())
    }

    def eval(file) {

    }

}
