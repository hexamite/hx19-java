
 ZmqSocket.js
 ============

 With ZmqSocket you can talk to zmq sockets 
 from your JavaScript code. You can connect,
 send and receive string messages. As JavaScript 
 does not support raw TCP connections, it uses 
 Flash as a bridge. 

 
 Download
 --------
 Sources are located at http://code.google.com/p/zmqsocket-js/
 Precompiled flash object is available at downloads section
 http://code.google.com/p/zmqsocket-js/downloads/detail?name=ZmqSocketMain.swf&can=2&q=
 To build flash object from sources, you will need ZmqSocket.as
 from http://code.google.com/p/zmqsocket-as/source/browse/ZmqSocket.as

 
 Features
 --------
 1. Identities supported.
 2. Multipart messages supported.
 3. String messages supported.


 Limitations
 -----------
 1. Maximum message size is 2^32-10 bytes.
 2. Binary messages not supported.
 

 Usage
 -----
 1. To allow Flash to connect to a TCP socket, a socket
    policy file must be available on the same domain
    on port 843. To learn more, read 
    http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c60.html#WS5b3ccc516d4fbf351e63e3d118a9b90204-7c63 
    and http://www.lightsphere.com/dev/articles/flash_socket_policy.html
    The latter has a simple Perl socket server script
    socketpolicy.pl, which must be started by root.
 2. For test code using ZmqSocket, see 
    http://code.google.com/p/zmqsocket-js/source/browse/Test.html



 License
 -------
 GNU GPL v3


 Author
 ------
 Copyright 2011 by Artur Brugeman

  