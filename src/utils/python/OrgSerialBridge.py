import serial, argparse, socket, zmq, threading, logging

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
            while True:
                data = self.port.readline()
                logging.debug("Received from serial: %s "% data)
                self.pubSocket.send_multipart(['/'.join([self.topic,'from']), data])
        except KeyboardInterrupt:
            pass

def main(args):
    context= zmq.Context()
    pubs = context.socket(zmq.PUB)
    pubs.connect(args.pub_socket)

    subs = context.socket(zmq.SUB)
    subs.connect(args.sub_socket)
    topic = '%s@%s'%(args.serial_port, socket.gethostname())
    logging.info("Publishing to " + '/'.join([topic,'to']))
    subs.setsockopt(zmq.SUBSCRIBE, '/'.join([topic,'to']))

    port = serial.Serial(args.serial_port, args.baudrate)
    sp = SerialThread(port, pubs)
    sp.topic = topic
    sp.setDaemon(True)
    sp.start()

    try:
        while True:
            topic, msg = subs.recv_multipart()
            logging.debug("Received from zmq: %s (%s)" % (msg, topic))
            port.write(msg)
    except KeyboardInterrupt:
        pass

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Bridges a serial port and ZeroMQ')
    parser.add_argument('serial_port', action='store')
    parser.add_argument('pub_socket', action='store')
    parser.add_argument('sub_socket', action='store')
    parser.add_argument('-d', help='activate debug', action='store_true', default=False, dest='debug')
    parser.add_argument('-b', action='store', default=38400, dest='baudrate')
    args = parser.parse_args()

    import logging
    logging.basicConfig(level = logging.DEBUG if args.debug else logging.INFO)
    main(args)