# HX19 Message format

The messages sent between MX19 devices

The devices can send messages to each other containing:

  - commands - set or query operating parameters or perform an action.
  - serial output - strings to send to the recipient's serial port.
  - message forwards - embedded message to transmit.

The raw format of messages sent to HX19 devices is (in loose [EBNF](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form)):

    message = address { command | serial | forward } '/' checksum '\r'

A message begins with an address followed by zero or more commands, serial outputs or forwards. Messages are terminated
by a slash, a hexadecimal checksum and a carriage return.

    address = ( class [ start-of-a-name ] | '!' ) '&'

An address is either a class, optionally followed by the start of a device name or an exclamation sign denoting all
devices. Either is followed by '&'.

    class =
        M      # Monitor
      | R      # Receiver (of ultrasound pulses)
      | T      # Transmitter (of ultrasound pulses)

A device class is one of M, R or T for monitor, receiver and transmitter.

Start of a name means that multiple devices of the same class can be addressed at once if their names start with the
same string. The names are usually two digit integers, but don't have to be. The full name can be given to address
an individual device.

    serial = '<' text '>'

Serial is text enclosed in '<' and '>'. It will be sent to the serial port on receiving devices. The text must be
printable cannot contain '<' or '>'.

    forward = '[' message ']'

A forward is a string that the recipient is expected to broadcast. It can be a valid message but doesn't have to be.
It is enclosed in square brackets. Messages can be nested this way to any depth. This makes it possible to relay
messages through a chain of devices. Vertical bars in the forward string will be replaced by carriage return in the
broadcast message.

    command =
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

    # = integer

The comments after each command describe the purpose of the command, the values of their parameters and the device
classes for which they are valid.
