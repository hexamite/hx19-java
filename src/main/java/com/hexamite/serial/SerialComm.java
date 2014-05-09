package com.hexamite.serial;

import jssc.SerialPort;
import jssc.SerialPortException;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

// sudo chown tk /dev/ttyUSB0

/**
 * Maps serialport i/o to a pair of zeroMq sockets.
 * */
public class SerialComm {

    private Semaphore semaphore = new Semaphore(1);

    private final static int PORT_FROM_SERIAL = 5555; // pub
    private final static int PORT_TO_SERIAL = 5556; // sub
    private final static int PORT_STOP = 5557;

    private Context context = null;
    private String comport;

    public static void main(String[] args) {
        try {
            assert args.length == 1 : "Usage: SerialComm com-port";
            new SerialComm(args[0]).publishSerial();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SerialComm(String comport) {
        this.comport = comport;
    }

    /**
     * Publish any incoming bytes from the serial port to zeromq port `FROM_SERIAL`.
     * */
    public void publishSerial() throws InterruptedException, IOException {

        SerialPort serialPort = null;
        Socket fromSerial = null;
        Socket toSerial = null;


        try {
            System.out.println("tcp://*:5556 (SUB) -> " + comport + " -> tcp://*:5555 (PUB)");
            context = ZMQ.context(1);

            fromSerial = context.socket(ZMQ.PUB);
            fromSerial.bind("tcp://*:5555");

            toSerial = context.socket(ZMQ.SUB);
            toSerial.bind("tcp://*:5556");
            toSerial.subscribe(ZMQ.SUBSCRIPTION_ALL);
            Thread.sleep(10);

            serialPort = new SerialPort(comport);
            serialPort.openPort();
            serialPort.setParams(250000, 8, 1, 0);
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(new SerialPortReader(serialPort, fromSerial));
            // serialPort.writeBytes(pack("M&$"));
            map(toSerial, serialPort);

            Thread.sleep(60000 * 60 * 24);

        } catch (SerialPortException e) {
            e.printStackTrace();
        } finally {
            if(serialPort != null) {
                try {
                    serialPort.closePort();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
            if(toSerial != null) {
                toSerial.close();
            }
            if(fromSerial != null) {
                fromSerial.close();
            }
            if(context != null) {
                context.term();
            }
        }
    }

    private void map(Socket toSerial, SerialPort serialPort) throws SerialPortException, IOException {
        while(!Thread.currentThread().isInterrupted()) {
            byte[] bytes = toSerial.recv();
            byte[] packed = pack(bytes);
            serialPort.writeBytes(packed);

            System.out.println(" bytes: " + new String(bytes).replaceAll(".", " $0").replace('\r', '£'));
            System.out.print(" bytes: ");
            for(byte b: bytes) {
                System.out.format("%02x", b);
            }
            System.out.println();

            System.out.println("packed: " + new String(packed).replaceAll(".", " $0").replace('\r', '£'));
            System.out.print("packed: ");
            for(byte b: packed) {
                System.out.format("%02x", b);
            }

            System.out.println();
        }
    }

    /**
     * Package `payload` into a valid HX19 package.
     * Appends a slash, a hexadecimal checksum and carriage return to `payload`.
     * */
    private byte[] pack(String payload) throws IOException {
        return pack(payload.getBytes());
    }

    /**
     * Package `payload` into a valid HX19 package.
     * Appends a slash, a hexadecimal checksum and carriage return to `payload`.
     * */
    private byte[] pack(byte[] payload) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int sum = 0;
        for(byte b: payload) {
            sum += b;
        }
        out.write(payload);
        out.write((byte) '/');
        out.write(String.format("%X", sum).getBytes());
        out.write((byte) '\r');
        return out.toByteArray();
    }

    public static class SerialPortReader implements SerialPortEventListener {

        private SerialPort serialPort;
        private Socket fromSerial;

        private ByteArrayOutputStream received = new ByteArrayOutputStream();

        SerialPortReader(SerialPort serialPort, Socket fromSerial) {
            this.serialPort = serialPort;
            this.fromSerial = fromSerial;
        }

        public void serialEvent(SerialPortEvent event) {

            try {
                System.out.println("Event:: type: " + event.getEventType() + ", value: " + event.getEventValue());
                if(event.isRXCHAR()){
                    int n = event.getEventValue();
                    byte bytes[] = serialPort.readBytes(n);
                    for(byte b: bytes) {
                        if(b == '\r') {
                            System.out.print("\n");
                            fromSerial.send(received.toByteArray());
                            System.out.println("SerialComm: fromSerial: '" + new String(received.toByteArray()) + "'");
                            received.reset();
                        } else {
                            System.out.print((char) b);
                            received.write(b);
                        }
                    }
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

}

