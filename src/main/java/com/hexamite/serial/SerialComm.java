package com.hexamite.serial;

import jssc.SerialPort;
import jssc.SerialPortException;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import java.util.concurrent.Semaphore;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

// sudo chown tk /dev/ttyUSB3

/**
 * Maps serialport i/o to a pair of zeroMq sockets.
 * */
public class SerialComm {

    private Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        try {
            new SerialComm().testSerialComm3();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSerialComm3() throws InterruptedException {

        SerialPort serialPort = null;
        Context context = null;
        Socket fromSerial = null;
        Socket toSerial = null;


        try {
            context = ZMQ.context(1);

            fromSerial = context.socket(ZMQ.PUB);
            fromSerial.bind("tcp://*:5555");

            toSerial = context.socket(ZMQ.SUB);
            toSerial.connect("tcp://*:5556");
            toSerial.subscribe(ZMQ.SUBSCRIPTION_ALL);

            serialPort = new SerialPort("/dev/ttyUSB0");
            serialPort.openPort();
            serialPort.setParams(250000, 8, 1, 0);
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(new SerialPortReader(serialPort, fromSerial));

            serialPort.writeBytes(pack("M& <send this huuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuge message>"));

            // map(toSerial, serialPort);

            Thread.sleep(60000);

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

    private void map(Socket toSerial, SerialPort serialPort) throws SerialPortException {
        while(!Thread.currentThread().isInterrupted()) {
            byte[] bytes = toSerial.recv();
            serialPort.writeBytes(bytes);
            System.out.println("SerialComm: toSerial: " + bytes.toString());
        }
    }

    /**
     * Package `payload` into a valid HX19 package.
     * Appends a slash, a hexadecimal checksum and a carrige return character to `payload`.
     * */
    private byte[] pack(String payload) {
        int sum = 0;
        for(byte b: payload.getBytes()) {
            sum += b;
        }
        return (payload + "/" + String.format("%X", sum) + "\r").getBytes();
    }

    public static class SerialPortReader implements SerialPortEventListener {

        private SerialPort serialPort;
        private Socket fromSerial;

        SerialPortReader(SerialPort serialPort, Socket out) {
            this.serialPort = serialPort;
            this.fromSerial = out;
        }

        public void serialEvent(SerialPortEvent event) {
            try {
                if(event.isRXCHAR()){
                    int n = event.getEventValue();
                    byte buffer[] = serialPort.readBytes(n);
                    System.out.println("SerialComm: fromSerial: " + new String(buffer));
                    fromSerial.send(new String(buffer));
                    System.out.println("SerialComm: Sent to socket.");
                } else if(event.isCTS()){
                    if(event.getEventValue() == 1){
                        System.out.println("SerialComm: CTS - ON");
                    } else {
                        System.out.println("SerialComm: CTS - OFF");
                    }
                } else if(event.isDSR()){
                    if(event.getEventValue() == 1){
                        System.out.println("SerialComm: DSR - ON");
                    } else {
                        System.out.println("SerialComm: DSR - OFF");
                    }
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

}

