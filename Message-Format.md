# HX19 Message formats

The format of messages sent between HX19 devices is (in loose
[EBNF](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form))

Everything received by a monitor over the air is sent to the serial port and vice versa, everything that is sent to the
Monitor over the serial port is broadcast over the air.

    message =
        command-message                // idle mode
      | trigger-message                // syncronous mode
      | start-of-pulse-message
      | distance-message               // idle or syncronous mode
      | upload-message                 // upload mode

## Command Message

Command messages are typically sent from the PC through the attached monitor device over the air and are picked up and acted upon by devices with matching addresses.

    command-message = command-address command-payload '/' checksum '\r'
  
That is, the actual content of the message is preceded by the device address and followed by a slash, a checksum of the
address and checksum in hexadecimal and a carriage return.

    command-address = anyone | ( device-class [ device-id ] ) '&'

A `command-address` is either a device class, optionally followed by the start of a device name or an exclamation sign denoting all
devices. The address is always terminated by ampersand.

    anyone = '!'

    device-class =
        'M'      // Monitor
      | 'R'      // Receiver (of ultrasound pulses)
      | 'T'      // Transmitter (of ultrasound pulses)

A device class is one of 'M' for monitor, 'R' for receiver or 'T' for transmitter. 

Start of a name means that multiple devices of the same class can be addressed at the same time if their names start with the
same string. The names are usually two digit integers, but don't have to be. The full name can be given to address
an individual device.

    device-id = integer

    command-payload = { command | serial | forward }

The payload of a command message contains zero or more commands, serial outputs or forwards.

    command =
         '$'              //  syncStrobe: true                 M
       | '%'              //  syncStrobe: false                M
       | 'a' integer      //  acquisitionRate: <integer>       M R T
       | 'bt'             //  batteryStatus                    M R T
       | 'ee'             //  store                            M R T
       | 'f' integer      //  firstTagInQueue: <integer>       M
       | 'h'              //  deepSleep: 0|1                   M R T
       | 'mb' integer     //  monitorBattery: 0|1                  T
       | 'mc' integer     //  countRecords: 0|1                    T
       | 'md' integer     //  ledOn: 0|1                       M R T
       | 'mn' integer     //  noiceRecovery: 0|1                 R
       | 'mn' integer     //  directNetworkAccess: 0|1             T
       | 'mp' integer     //  powerSavings: 0|1                  R T
       | 'mp' integer     //  serialPinOn: 0|1                     T
       | 'ms' integer     //  doppler: 0|1                       R
       | 'mx' integer     //  rfidOn: 0|1                          T
       | 'p' integer      //  signalPower: 0|1|2|3               R T
       | 'px' integer     //  signalPower: 0|1|2|3             M
       | 'q' integer      //  receiverOutputResultQueue        M R T
       | 'r' integer      //  inputChannel: 1..125             M R T
       | 's' integer      //  numTags                          M
       | 't' integer      //  outputChannel: 1..125            M R T
       | 'v'              //  version: 0|1                     M R T
       | 'w'              //  workRegisters                    M R T

The comments after each command describe the purpose of the command, the values of their parameters and the device
classes for which they are valid.

    forward = '[' text ']'

A forward is a string that the recipient is expected to broadcast. It can be a valid message but doesn't have to be.
It is enclosed in square brackets. Messages can be nested this way to any depth. This makes it possible to relay
messages through a chain of devices. Vertical bars in the forward string will be replaced by carriage return in the
broadcast message.

    serial = '<' text '>'  // Cannot contain '>'

Serial output is text enclosed in '<' and '>'. It will be sent to the serial port on receiving devices. The text must be
printable and cannot contain '>'.

## Trigger Message

    trigger-message = 'T' transmitter-id '/' checksum '\r'

    transmitter-id = integer

## Start-of-pulse Message

    start-of-pulse-message = 'X' transmitter-id '/' checksum '\r'

    transmitter-id = integer

## Distance Message

    distance-message = 'R' receiver-id space 'P' transmitter-id space 'A' distance

    receiver-id = device-id

    transmitter-id = device-id

## Upload Message

    upload-message = upload-header | upload-line | upload-footer

    upload-header = 'MF&uf{'

    upload-line = 'MF&uf' text '/' checksum '\r'

    upload-footer = 'MF&uf' '/' file-checksum '/' checksum '\r'
