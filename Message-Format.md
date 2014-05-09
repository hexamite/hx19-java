# HX19 Message format

The messages sent between MX19 devices

The devices can send messages to each other containing:

  - commands - set or query operating parameters or perform an action.
  - serial output - strings to send to the recipient's serial port.
  - message forwards - embedded message to transmit.

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
