#! /usr/bin/env groovy

/**
 * Send a message to a hx19 device. See usage() for details.
 * */

@Grab(group='org.zeromq', module='jeromq', version='0.3.2')
import org.zeromq.ZMQ
import groovy.transform.Field

final int PORT_TO_SERIAL = 5556

final ZMQ.Context context = ZMQ.context(1)
final ZMQ.Socket toSerial
final ZMQ.Socket fromSerial

try {

    if('-h' in args) {
        println usage()
        System.exit(0)
    }

    assert args.length == 2 : "Wrong number of arguments\n" + usage()

    def host =args[0]
    def message = args[1]

    context = ZMQ.context(1)

    toSerial = context.socket(ZMQ.PUB)
    toSerial.connect("tcp://$host:$PORT_TO_SERIAL")
    println "connected to tcp://$host:$PORT_TO_SERIAL"
    Thread.sleep(10)
    toSerial.send(message)
    println "Sent $message"
    Thread.sleep(10)

} finally {
    fromSerial?.close()
    toSerial?.close()
    context?.term()
}

// Methods

def log(message) {
    println "${getClass().simpleName}: $message"
}

def usage() {
    """\
    Usage: message ( -h | host message )

    Send `message` to HX19 `device`(s) through a serial server on `host`. No error is reported if no serial server
    is running on host.

    Options

      -h             Prints this message

    Arguments

      host:        IP number or address of ZeroMQ socket (SerialComm server) to send to. Default is localhost.

      message:     The message to send.

    Examples:

      message localhost T&g               # Send '&g' to transmitters through a SerialComm server on localhost.

      message my.examplle.com R2&g        # Send '&g' to receivers with names that start with 'R2' through a serial
                                          # server on my.example.com.

      message -h                          # Print this message

    """.stripIndent()

}
