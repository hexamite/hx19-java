# Usage: serial-server.sh <comport>
#
# For example
# 
#    serial-server.sh /dev/ttyUSB0      # Linux
#
#    serial-server.sh com1              # Windows
#
# Bridge between serial port and a pair ZeroMQ sockets.
#
# In case of insufficient permissions to the serial port on Linux run:
#
#     sudo chown <your-user-name> <serial-port>
#
# For example 
#
#     sudo chown tk /dev/ttyUSB0
#

java -cp 'lib/*:build/libs/hx19-java.jar' com.hexamite.serial.SerialComm $1
