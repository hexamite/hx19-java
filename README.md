#hx19-java

## Hexamite hx19 positioning system library for the java virtual machine

The code here will eventually provide a java library and user interface to control and read Hexamites hx19 ultrasonic position devices.

The source is mostly in Groovy, but can be used from other JVM languages such as Java, JPython, JRuby, Clojure, etc.

## Status

  - proof of concept for reading and writing to the device using 'java simple serial comm' library.
  - Configuration builder
  - Basic object representation of a positioning system
  - Partial implementaion of UI showing the state of all devices

As is the the system can by used to: 

  - Low level communications with the system throught the serial port using hx19 native command set.
  - Configure the system, that is, constructing an object graph that represents the participating devices.
  - Construct hx19 messages in a higer level syntax.
  
The code is tested on Ubuntu Linux 12.4 with Java 7 but should work on any recent Linux, Mac or Windows / Java combination.

## Configuration

Configuration of the system is provided using a simple domain specific language.

### Example config

    { ->
        monitor(name: 'M40')
        soundEmitter(name: 'S41', x: 1245, y: 2345, z: 3456, inputChannel: 25, signalPower: 3)
        soundEmitter(name: 'S42', x: 2345, y: 3456, z: 4567, inputChannel: 25)
        soundEmitter(name: 'S43', x: 3456, y: 4567, z: 5678, inputChannel: 25)
        soundListener(name: 'R44', inputChannel: 25)
    }


### Description

Line comments `// comment` and range comments `/* comment */` can be place anywhere in the file, but range comments cannot be nested.

Apart from the configuration itself the configuration file may contain any valid groovy code, for example initial calculation or constants. 
Variables defined in that way can be used instead of literals, anywhere in the configuration. 

### Specification

The exact syntax of the configuration is restricted in the follwing way:

The root element must be 'positionProvider'. This element can contain any number of device configurations, 
that is `soundEmitter`, `soundListener` and `monitor` configurations. 

Emitters and listeners can be given 3-dimenstional positions, with the positions of the remaining devices to be determined.

## Message between HX19 devices

The devices can send messages to each other containing:

  - commands - set or query operating parameters or perform an action. 
  - serial output - strings to send to the recipient's serial port.
  - message forwards - embedded message to transmit.

### Low level message format

The raw format of messages sent to HX19 devices is:

    <message> ::= <address> ( <command> | <serial> | <forward> )*
    
In other words, a message begins with and address followed by zero or more commands, serial outputs or forwarded messages.

    <address> ::= ( <start-of-a-name> | '!' ) '&'
    
That is, an address is the start of a device name or an exclamation sign denoting all devices. Either is followed by '&'. 

    <serial> ::= '<' <text> '>'
    
That is, any text enclosed in '<' and '>' should be sent to the serial port. The text cannot contain '<' or '>'.

    <forward> ::= '[' <message> ']'
    
That is, a forward is a message that the recipient is expected to send. It is enclosed in square brackets. Messages can be nested this way to any depth.

    <command> ::= 
         '$'         //  syncStrobe: true                 M    
       | '%'         //  syncStrobe: false                M    
       | 'a' #:      //  acquisitionRate: <integer>       MRT  
       | 'bt'        //  batteryStatus                    MRT  
       | 'ee'        //  store                            MRT  
       | 'f' #       //  firstTagInQueue: <integer>       M    
       | 'h'         //  deepSleep: 0|1                   MRT  
       | 'mb' #      //  monitorBattery: 0|1                T  
       | 'mc' #      //  countRecords: 0|1                  T  
       | 'md' #      //  ledOn: 0|1                       MRT  
       | 'mn' #      //  noiceRecovery: 0|1                R   
       | 'mn' #      //  directNetworkAccess: 0|1           T  
       | 'mp' #      //  powerSavings: 0|1                 RT  
       | 'mp' #      //  serialPinOn: 0|1                   T  
       | 'ms' #      //  doppler: 0|1                      R   
       | 'mx' #      //  rfidOn: 0|1                        T  
       | 'p' #       //  signalPower: 0|1|2|3              RT  
       | 'px' #      //  signalPower: 0|1|2|3             M    
       | 'q' #       //  receiverOutputResultQueue        MRT  
       | 'r' #       //  inputChannel: 1..125             MRT  
       | 's' #       //  numTags                          M    
       | 't' #       //  outputChannel: 1..125            MRT  
       | 'v'         //  version: 0|1                     MRT  
       | 'w'         //  workRegisters                    MRT  

    # ::= <integer>
    
The comments after each command describe the purpose of the command, the values of their parameters and the device types for which they are valid.

### High level message builder

In code, messages can be constructed using a groovy 'builder' that, apart from being more readily understandable,
provides benefits such as suggestions, auto-completions and context sensitive help when used inside an IDE such as
Eclipse, Netbeans or Idea.

The following example is illustrates the syntax, it is not necessarily meaningful for any specific application.

    message('R21') {
        serial 'xyz'
        store
        batteryStatus
        signalPower 3
        doppler off
        message {
            serial 'abc'
            message('T') {
                store
                registers
                signalPower 1
            }
        }
    } 
    
In other words, we are asking device 'R21' to

  - send 'xyz' to its serial port
  - store current settings in eeprom
  - give us battery status
  - use signal power of 3
  - send an embedded message to all devices
  
The embedded message in the last point is addressed to every device and asks them to do following:

  - send 'abc' to their serial ports
  - send an embedded message to all devices that have name starting with 'T' (sound transmitters).
  
That embedded message to all 'R' devices asks the following

  - store current settings in eeprom
  - broadcast the content of work registers
  - set signal power to 1

This is converted to the following low level format before it is sent:

    R21& <xyz> ee b p3 ms0 [!& <abc> [T& ee w p1]]
