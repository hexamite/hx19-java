package com.hexamite.hx19;

// import gnu.io.CommPort;
// import gnu.io.CommPortIdentifier;
// import gnu.io.SerialPort;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialComm {
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;

    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 250000;

    public SerialComm() {
        super();
    }

    void testSerialComm1() {
        try {
            SerialPort serialPort = new SerialPort("/dev/ttyUSB0");
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(250000, 8, 1, 0));
            System.out.println("\"Hello World!!!\" successfully writen to port: " + serialPort.writeBytes("M&$/97\r".getBytes()));
            
            
            // while(true) {
            //       // System.out.print(".");
            //       serialPort.writeBytes("U".getBytes());
            //       // Thread.sleep(100);
            // }
            
            
            
            
            System.out.println("Port closed: " + serialPort.closePort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
           
    public void testSerialComm() {
        SerialPort serialPort = new SerialPort("/dev/ttyUSB0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(250000, 8, 1, 0);//Set params.
            byte[] buffer;
            do {
                buffer = serialPort.readBytes(1);//Read 1 byte from serial port
                System.out.print(new String(buffer));
            } while(buffer[0] != '\r');
            serialPort.closePort();//Close serial port
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

            
    // void testSerialComm0() {
    //     try {
    //         CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
    //         if (portIdentifier.isCurrentlyOwned()) {
    //             System.out.println("Error: Port is currently in use");
    //         } else {
    //             CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
    //             if (commPort instanceof SerialPort) {
    //                 SerialPort serialPort = (SerialPort) commPort;
    //                 serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    //                 System.out.println("speed: " + serialPort.getBaudRate());
    // 
    //                 InputStream in_ = serialPort.getInputStream();
    //                 
    //                 // byte[] buffer = new byte[1024];
    //                 // int len = -1;
    //                 // int i = 0;
    //                 // while (i++ < 4 && (len = in_.read(buffer)) > -1) {
    //                 //     System.out.print(new String(buffer, 0, len));
    //                 // }
    //                 
    //                 // def c = in_.read()
    //                 // for(int i=0; i < 4 && c >= -1; i++) {
    //                 //     println "> $c"
    //                 //     c = in_.read()
    //                 // }
    //                 
    //                 OutputStream out = serialPort.getOutputStream();
    //                 out.write("M&$/97\r".getBytes());
    //                 // while(true) {
    //                 //       // System.out.print(".");
    //                 //       out.write("U".getBytes());
    //                 //       // Thread.sleep(100);
    //                 // }
    //                 out.flush();
    //                 out.close();
    //             } else {
    //                 System.out.println("Error: Only serial ports are handled by this example.");
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        try {
            // (new SerialComm()).connect("/dev/ttyUSB0");
            new SerialComm().testSerialComm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}