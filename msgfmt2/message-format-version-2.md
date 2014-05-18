# HX19 Message format (version 2)

    envelope = address message '/' checksum '\r'
    
    address = anyone | ( device-class [ device-id ] ) '&'

    anyone = '!'

    device-class =
        'M'      // Monitor
      | 'R'      // Receiver (of ultrasound pulses)
      | 'T'      // Transmitter (of ultrasound pulses)

    device-id = integer
    
    message =
        command-message                // idle mode
      | trigger-message                // syncronous mode
      | start-of-pulse-message
      | distance-message               // idle or syncronous mode
      | upload-message                 // upload mode
      
    command-message = { command | serial | forward | trigger-pulse | upload-start | upload-line | upload-stop }
    
    forward = '[' command-message ']'

    serial = '<' text '>'  // Cannot contain '>'



    command =                  
         '$'                        //  syncStrobe: true                 M
       | '%'                        //  syncStrobe: false                M
       | 'a' integer                //  acquisitionRate: <integer>       MRT
       | 'bt'                       //  batteryStatus                    MRT
       | 'ee'                       //  store                            MRT
       | 'f' integer                //  firstTagInQueue: <integer>       M
       | 'h'                        //  deepSleep: 0|1                   MRT
       | 'mb' integer               //  monitorBattery: 0|1                T
       | 'mc' integer               //  countRecords: 0|1                  T
       | 'md' integer               //  ledOn: 0|1                       MRT
       | 'mn' integer               //  noiceRecovery: 0|1                R
       | 'mn' integer               //  directNetworkAccess: 0|1           T
       | 'mp' integer               //  powerSavings: 0|1                 RT
       | 'mp' integer               //  serialPinOn: 0|1                   T
       | 'ms' integer               //  doppler: 0|1                      R
       | 'mx' integer               //  rfidOn: 0|1                        T
       | 'p' integer                //  signalPower: 0|1|2|3              RT
       | 'px' integer               //  signalPower: 0|1|2|3             M
       | 'q' integer                //  receiverOutputResultQueue        MRT
       | 'r' integer                //  inputChannel: 1..125             MRT
       | 's' integer                //  numTags                          M
       | 't' integer                //  outputChannel: 1..125            MRT
       | 'v'                        //  version: 0|1                     MRT
       | 'w'                        //  workRegisters                    MRT
       
    trigger-pulse-message = '$'     // Sent from monitor to transmitter in syncronous mode.
    
    start-of-pulse-message = '$'    // Sent from transmitter at the start of sound pulse
       
    distance-message = 'R' receiver-id space 'P' transmitter-id space 'A' distance
    
    receiver-id = device-id
    
    transmitter-id = device-id
    
    upload-message = 
       | 'u{'                 //  upload start                     MRT
       | 'u' upload-line      //  upload line                      MRT
       | 'u}' file-checksum   //  upload stop                      MRT
       
    upload-line = text        // upto 32 printable characters

