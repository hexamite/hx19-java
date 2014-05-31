#! /usr/bin/env groovy

/**
 * Subscribe to a stream of distance measurements an publish a stream of locations.
 */

@Grab(group='org.zeromq', module='jeromq', version='0.3.2')
import org.zeromq.ZMQ
import java.util.concurrent.Semaphore
import groovy.transform.Field

@Field final static int PORT_FROM_SERIAL = 5555
@Field final static int PORT_POSITIONS = 5558
@Field ZMQ.Context context
@Field Semaphore semaphore

try {

    if('-h' in args) {
        println usage()
        System.exit(0)
    }

    assert args.length in 0..1 : "Wrong number of arguments: $args.length\n" + usage()
    def host =args.getAt(0) ?: 'localhost'

    semaphore = new Semaphore(1)
    println "Acquiring sempahore 1"
    semaphore.acquire()

    context = ZMQ.context(1)

    subscribe(host, PORT_FROM_SERIAL) { bytes ->
        println new String(bytes)
        true
    }

    println "Acquiring sempahore 2"
    semaphore.acquire()

} finally {
    context?.term()
}
// Methods

/**
 * Respond to each message on tcp://`host`:`port` with `closure` until the closure returns 'false-ish' (null, 0, etc).
 * */
def subscribe(host, port, closure) {
    Thread.start {
        ZMQ.Socket subscriber = null
        try {
            subscriber = context.socket(ZMQ.SUB)
            subscriber.connect("tcp://$host:$port")
            subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL)
            boolean proceed = true
            while (proceed) {
                byte[] bytes = subscriber.recv()
                proceed = closure(bytes)
            }
            println "Releasing sempahore"
            semaphore.release()
        } catch(Exception e) {
            e.printStackTrace()
        } finally {
            subscriber.close()
        }
    }
}


def usage() {
    """\
    Usage: trilaterate -h | [ host ]

    ... serial port on `host`. No error is reported if no serial server is running on `host`.

    Options

      -h        Prints this message

    Arguments

      host:     IP number or address of serial server to subscribe to. Default is localhost.

    Examples:

      trilaterate                  # ... SerialComm server on localhost.

      trilaterate my.example.com   # ... SerialComm server on host my.example.com.

      trilaterate -h               # Print this message

    """.stripIndent()

}
