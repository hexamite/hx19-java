# HX19 Message format

    message =
        command-message                // idle mode
      | trigger-message                // syncronous mode
      | start-of-pulse-message
      | distance-message               // idle or syncronous mode
      | upload-message                 // upload mode

    --

    command-message = command-address command-payload '/' checksum '\r'

    command-address = anyone | ( device-class [ device-id ] ) '&'

    anyone = '!'

    device-class =
        'M'      // Monitor
      | 'R'      // Receiver (of ultrasound pulses)
      | 'T'      // Transmitter (of ultrasound pulses)

    device-id = integer

    command-payload = { command | serial | forward }

    command =
         '$'              //  syncStrobe: true                 M
       | '%'              //  syncStrobe: false                M
       | 'a' integer      //  acquisitionRate: <integer>       MRT
       | 'bt'             //  batteryStatus                    MRT
       | 'ee'             //  store                            MRT
       | 'f' integer      //  firstTagInQueue: <integer>       M
       | 'h'              //  deepSleep: 0|1                   MRT
       | 'mb' integer     //  monitorBattery: 0|1                T
       | 'mc' integer     //  countRecords: 0|1                  T
       | 'md' integer     //  ledOn: 0|1                       MRT
       | 'mn' integer     //  noiceRecovery: 0|1                R
       | 'mn' integer     //  directNetworkAccess: 0|1           T
       | 'mp' integer     //  powerSavings: 0|1                 RT
       | 'mp' integer     //  serialPinOn: 0|1                   T
       | 'ms' integer     //  doppler: 0|1                      R
       | 'mx' integer     //  rfidOn: 0|1                        T
       | 'p' integer      //  signalPower: 0|1|2|3              RT
       | 'px' integer     //  signalPower: 0|1|2|3             M
       | 'q' integer      //  receiverOutputResultQueue        MRT
       | 'r' integer      //  inputChannel: 1..125             MRT
       | 's' integer      //  numTags                          M
       | 't' integer      //  outputChannel: 1..125            MRT
       | 'v'              //  version: 0|1                     MRT
       | 'w'              //  workRegisters                    MRT

    forward = '[' command-payload ']'

    serial = '<' text '>'  // Cannot contain '>'

    --

    trigger-message = 'T' transmitter-id '/' checksum '\r'

    transmitter-id = integer

    --

    start-of-pulse-message = 'X' transmitter-id '/' checksum '\r'

    transmitter-id = integer

    --

    distance-message = 'R' receiver-id space 'P' transmitter-id space 'A' distance

    receiver-id = device-id

    transmitter-id = device-id

    --

    upload-message = upload-header | upload-line | upload-footer

    upload-header = 'MF&uf{'

    upload-line = 'MF&uf' text '/' checksum '\r'

    upload-footer = 'MF&uf' '/' file-checksum '/' checksum '\r'
