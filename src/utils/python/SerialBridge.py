import serial, argparse, socket, zmq, threading, logging

# Adapted from https://github.com/eagleamon/serial2zmq

class SerialThread(threading.Thread):

    def __init__(self, port, pubSocket):
        threading.Thread.__init__(self)
        self.port = port
        self.pubSocket = pubSocket

    def close():
        self.port.close()

    def run(self):
        self.port.flushInput()

        try:
            message = b''
            while True:
                data = self.port.read()
                logging.debug("<~ %s "% data[0])
                if data[0] == 13:
                    logging.debug("ser > pub: %s "% message)
                    self.pubSocket.send(message)
                    message = b''
                else:
                    message += data
        except KeyboardInterrupt:
            pass


# Packages `payload` into a valid HX19 package.
# Appends a slash, a hexadecimal checksum and carriage return to `payload`.
def pack(payload):

    sum = 0
    for b in payload:
        sum += b
    hex = '{:02X}'.format(sum).encode('utf-8')
    return payload + b'/' + hex + b'\r'


def main(args):

    context= zmq.Context()

    pubs = context.socket(zmq.PUB)
    pubs.bind("tcp://*:5555")

    subs = context.socket(zmq.SUB)
    subs.bind("tcp://*:5556")
    subs.setsockopt(zmq.SUBSCRIBE, b'')

    port = serial.Serial(args.serial_port, args.baudrate, )
    sp = SerialThread(port, pubs)
    sp.setDaemon(True)
    sp.start()

    try:

        while True:
            msg = subs.recv()
            msg = pack(msg)
            logging.debug("sub > ser: %s" % (msg))
            port.write(msg)

    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Bridges a serial port and ZeroMQ')
    parser.add_argument('serial_port', action='store')
    parser.add_argument('pub_socket', action='store')
    parser.add_argument('sub_socket', action='store')
    parser.add_argument('-d', help='activate debug', action='store_true', default=False, dest='debug')
    parser.add_argument('-b', action='store', default=250000, dest='baudrate')
    args = parser.parse_args()

    import logging
    logging.basicConfig(level = logging.DEBUG if args.debug else logging.INFO)
    main(args)