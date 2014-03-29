#hx19-java

## Hexamite hx19 positioning system java library

The code here will eventually provide a java library and user interface to control and read Hexamites hx19 ultrasonic position devices.

## Status

  - proof of concept for reading and writing to the device using 'java simple serial comm' library.
  - Configuration syntax
  - Basic object representation of a positioning system
  - UI showing the state of all devices

Before building, [jssc.jar](https://github.com/scream3r/java-simple-serial-connector/releases/) must be downloaded and placed in the lib/ folder.

The code is tested on Ubuntu Linux 12.4 with Java 7 but should work on any recent Linux, Mac or Windows / Java combination.

## Configuration

Configuration of the system is provided using a simple domain specific language.

### Example config

    { ->
        monitor(name: 'M40')
        frame(x: 4343, y: 3232, z: 4343, ax:0, ay: 180, az: 270) {  // optional
            soundEmitter(name: 'S41', x: 1245, y: 2345, z: 3456, inputChannel: 25, signalPower: 3)
            soundEmitter(name: 'S42', x: 2345, y: 3456, z: 4567, inputChannel: 25)
            soundEmitter(name: 'S43', x: 3456, y: 4567, z: 5678, inputChannel: 25)
            soundListener(name: 'R44', inputChannel: 25)
        }
    }


### Description

Line comments `// comment` and range comments `/* comment */` can be place anywhere in the file, but range comments cannot be nested.

Apart from the configuration itself the configuration file may contain any valid groovy code, for example initial calculation or constants. 
Variables defined in that way can be used instead of literals, anywhere in the configuration. 

### Specification

The exact syntax of the configuration is restricted in the follwing way:

The root element must be 'positionProvider'. This element can contain any number of device configurations, 
that is `soundEmitter`, `soundListener` and `monitor` configurations. It can also contain `frame` elements which can
make it easier to configure a set of devices mounted on a frame where only the position and orientation of 
the frame changes, but the relative position of the devices is constant.

Emitters and listeners can be given 3-dimenstional positions, with the positions of the remaining devices to be determined.

## Low level message format

The raw format of messages sent to HX19 devices is:

    <message> ::= <address> ( <command> | <serial> | <forward> )*
    
In other words, a message begins with and address followed by zero or more commands, serial outputs of forwarded messages.

    <address> ::= ( <start-of-a-name> | '!' ) '&'
    
That is, an address is the start of a device name or an exclamation sign denoting all devices. Either is followed by '&'

    <serial> ::= '<' <text> '>'
    
That is, any text enclosed in '<' and '>'. The only restriction on text is that it cannot contian '<' or '>'.

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

In code messages can be constructed in a groovy script using a 'builder' that provides benefits such as auto-completions and context sensitive help.

Example: 

    message('R21') {
        serial 'xyz'
        store
        batteryStatus
        signalPower 3
        doppler off
        sync
        message {
            serial 'abc'
            message('T') {
                store
                registers
                signalPower 1
            }
        }
    }    

This is converted to the following low level format before it is sent:

    R21& <xyz> ee b p3 ms0 [!& <abc> [T& ee w p1]]
