#! /usr/bin/env groovy

/**
 * Upload new code from standard in to HX-19 devices.
 * Usage: See usage()
 * */

@Grab(group='org.zeromq', module='jeromq', version='0.3.2')
import org.zeromq.ZMQ
import groovy.transform.Field

final int PORT_FROM_SERIAL = 5555
final int PORT_TO_SERIAL = 5556

ZMQ.Context context = ZMQ.context(1)
ZMQ.Socket toSerial = null
ZMQ.Socket fromSerial = null

try {

    if('-h' in args) {
        println usage()
        System.exit(0)
    }

    assert args.length == 2 : "Wrong number of arguments\n" + usage()
    def host = args[0]
    def file = args[1] as File

    context = ZMQ.context(1)

    toSerial = context.socket(ZMQ.PUB)
    toSerial.connect("tcp://$host:$PORT_TO_SERIAL")

    fromSerial = context.socket(ZMQ.SUB)
    fromSerial.connect("tcp://$host:$PORT_FROM_SERIAL")
    fromSerial.subscribe(ZMQ.SUBSCRIPTION_ALL)

    file.withReader { reader ->
        String line = reader.readLine()
        while(line != null) {
            println "> $line"
            toSerial.send(line)
            def confirmation = new String(fromSerial.recv())
            println "< $confirmation"
            assert confirmation?.startsWith('#') : "Unexpected response form serial port: '$confirmation'"
            line = reader.readLine()
        }
    }

} finally {
    fromSerial?.close()
    toSerial?.close()
    context?.term()
    log "Done."
}

// Methods

def log(message) {
    println "${getClass().simpleName}: $message"
}

def usage() {
    """\
    Usage: uploadCode -h | host file

    Upload file through a serial server on `host` into HX19 devices.

    The code will go over the air to any appropriate devices that are turned on are in
    upload mode (weak blue light). Run `device -h` to see how to put the device into upload mode.

    Options

      -h             Prints this message

    Arguments

      file:     The file with code to upload.

      host:     IP number or address of serial server to send to.

    Examples:

      upload my.example.com someCode.btl  # Upload someCode.btl to receivers via serial server on
                                          # my.example.com

      upload -h                           # Print this message

    """.stripIndent()

}
