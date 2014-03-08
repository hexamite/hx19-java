#hx19-java

## Hexamite hx19 positioning system java library

The code here will eventually provide a java library and user interface to control and read Hexamites hx19 ultrasonic position devices.

## Status

  - proof of concept for reading and writing to the device using 'java simple serial comm' library.
  - Configuration syntax
  - Basic object representation of a positioning system

Before building, [jssc.jar](https://github.com/scream3r/java-simple-serial-connector/releases/) must be downloaded and placed in the lib/ folder.

The code is tested on Ubuntu Linux 12.4 with Java 7 but should work on any recent Linux, Mac or Windows / Java combination.

## Configuration

Configuration to the script is provided using a simple domain specific language.

### Example

    positionProvider {
    
        monitor(name: 'M40')
        frame(x: 4343, y: 3232, z: 4343, ax:0, ay: 180, az: 270) {  // optional
            soundEmitter(name: 'S41', x: 1245, y: 2345, z: 3456)
            soundEmitter(name: 'S42', x: 2345, y: 3456, z: 4567)
            soundEmitter(name: 'S43', x: 3456, y: 4567, z: 5678)
            soundListener(name: 'R44')
        }
        
    }


### Description

The configuration language maps in general to XML in a simple way:

    elment(attribute1: value1, attribute2: value2, ...) {
        body(...) {
            ...
        }
        ...
    }

`body` can of course have its own attributes and body and so on. An empty element must be followed by either empty parantheses or empty curly braces.

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




