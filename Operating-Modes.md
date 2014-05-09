# HX19 modes

## Synchronous Measurement Mode

In this mode the system operates at high frequency and each measurement cycle is initiated by the monitor.
    
     Coord-Subscriber   Raw-Subscriber      Serial server        Monitor 11      Transmitter 21     Transmitter 22    Receiver 31        Receiver 32        Receiver 33       Time
                                                                                                                                                                      
             │                 │                  │                  │                  │                  │                │                  │                  │                
    1        │                 │                  │                 T21---------------->│                  │                │                  │                  │            ┌ 0 ms     
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    2        │                 │<-----------------│<-----------------│<----------------X21--------------------------------->│----------------->│----------------->│            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    3        │                 │<-----------------│<-----------------│<------------------------------------------------dist(T21,R31)           │                  │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    4        │                 │<-----------------│<-----------------│<-------------------------------------------------------------------dist(T21,R32)           │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    5        │                 │<-----------------│<-----------------│<--------------------------------------------------------------------------------------dist(T21,R33)     │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    6        │<--------------------------------xyz(T21)              │                  │                  │                │                  │                  │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            ├ 62 ms
    1        │                 │                  │                 T22---------------->│                  │                │                  │                  │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    2        │                 │<-----------------│<-----------------│<----------------X22--------------------------------->│----------------->│----------------->│            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    3        │                 │<-----------------│<-----------------│<------------------------------------------------dist(T22,R31)           │                  │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    4        │                 │<-----------------│<-----------------│<-------------------------------------------------------------------dist(T22,R32)           │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    5        │                 │<-----------------│<-----------------│<--------------------------------------------------------------------------------------dist(T22,R33)     │   
             │                 │                  │                  │                  │                  │                │                  │                  │            │   
    6        │<-------------------------------xyz(T22)               │                  │                  │                │                  │                  │            │   
             │                 │                  │                  │                  │                  │                │                  │                  │            ├ 124 ms
             v                 v                  v                  v                  v                  v                v                  v                  v            v
            

1:   The monitor connected to the PC initiates the synchronous measurement cycle by sending out the class and address of the first transmitter in the system.
  
2:   The transmitter transmits an ultrasound pulse and sends out an 'X' followed by its ID. Each receiver pickus up both the pulse and the X<ID> in the 
     system and calculates the distance from difference between the sound and the radio frequency signal. The monitor pics up the signal as well and forwards it 
     through the serial server on the PC to any ZeroMQ raw-subscribers.
  
3-5: Each receiver waits for its predetermined interval and the sends out the distance it calculated in step 2. The distance flows through the monitor, and the
     serial server to any ZeroMQ distance subscribers.
       
6:   (Not available yet, see [roadmap](README.md#roadmap)) The serial server calculates the position coordinates of the movable elements of the system from the known positions of the fixed elements and the distances
     between receivers and transmitters. In the case depicted above it would make sence to have the transmitters movable and the receivers fixed, but any configuration
     is possible as long as there are three distances from fixed positions available for each movable device.


## Idle Mode

Idle mode identical to the Synchrounous mode except that the transmitters themselves initiate the measurement
cycle and the interval between the cycles is randomly chosen each time by each tranmitter to be either 4 or 8 seconds.
The randomness makes collisions a little bit less likely. Collisions result in a lost measurement but are very rare
in practice.

## Upload Mode

Upload mode allows the uploader to send new code to a class of devices through the monitor.
    
        Uploader        SerialComm          Monitor         Transmitter 21     Transmitter 22    Receiver 31        Receiver 32        Receiver 33
                                                                                                                                                  
           │                  │                  │                  │                  │                │                  │                  │                
    1 start (T)-------------->│----------------->│----------------->│----------------->│                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
    2      │<-----------------│<----------------'#' or [1]          │                  │                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
    3    line --------------->│----------------->│----------------->│----------------->│                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
    4      │<-----------------│<----------------'#' or [1]          │                  │                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
    5  stop (T)-------------->│----------------->│----------------->│----------------->│                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
    6      │<-----------------│<----------------'#' or [1]          │                  │                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │                
        success
           
        
    Alternative [1]
        
    7      │<-----------------│<----------------'*'                 │                  │                │                  │                  │                
           │                  │                  │                  │                  │                │                  │                  │  
        failure
    
    
1: The upload command sends a header line through a ZeroMQ socket to the serial server which forwards it to the monitor which transmits it over
   the air where it is picked up by the devices that match the address of the message.
   
2: The monitor responds with a '#' if the checksum matched.
   
3: Each line of the code is sent in turn as in step 1.

4: The monitor responds with '#' if the checksum matched.

5: The upload command sends a footer line with a checksum for the whole file as well as for that line.

6: The monitor responds with '#' if both checksums matched.

7: Alternatively to steps 2, 4 and 6, the monitor responds with '*' if the checksums don't match and the uploading halts.














