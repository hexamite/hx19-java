package com.hexamite.hx19

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import org.junit.*;

import static org.junit.Assert.*;

class SerialCommSTest {
    
    @Test
    void testSerialComm() {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier('/dev/ttyUSB0');
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(18, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in_ = serialPort.getInputStream();
                
                // byte[] buffer = new byte[1024];
                // int len = -1;
                // int i = 0
                // while (i++ < 4 && (len = in_.read(buffer)) > -1) {
                //     print(new String(buffer, 0, len));
                // }
                
                // def c = in_.read()
                // for(int i=0; i < 4 && c >= -1; i++) {
                //     println "> $c"
                //     c = in_.read()
                // }
                
                OutputStream out = serialPort.getOutputStream();
                out << 'M&$/97\r'
                
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        } 
    }
    
}
