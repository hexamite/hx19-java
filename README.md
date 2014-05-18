# HX19 interface

Command line utilities and Java library for operating, maintaining and interfacing HX19 ultrasound positioning devices.

## About HX19 Ultrasound Positioning Devices

The HX19 devices allow positioning of robots, robot arms, quadcopthers, vehicles, etc, with high precision and
frequency.

The system consists of a few Transmitters, Receivers and at least one Monitor attached to a PC to communicate with the
system.

With at least 3 of the devices in a fixed position (or just 2 for 2D positioning) the position of the rest can be
calculated.

## About this project

The code provided in this project provides command line tools and a Java library to operate and interface with the HX19
ultrasound positioning devices.

The tools are written in Java and Groovy and leverage [ZeroMQ](http://zeromq.org) to allow local or network access to
devices from a number of [programming languages and platforms](http://zeromq.org/bindings:_start).

## Requirements

[Java 6](http://openjdk.java.net/) or higher.

[Groovy 2.0](http://groovy.codehaus.org/) or higher.

## Dependencies

jssc-2.8.0.jar - Serial pot access.
jeromq-0.3.2.jar - ZeroMQ pure Java implementation.

## Building and running

We use Gradle as build system. We use the wrapper, so the there are no separate manual downloads. Both Gradle itself and
all dependences are automatically downloaded on the first build. Below are detailed instructions for Linux.

Install git

    sudo apt-get install git

Clone the repository

    git clone https://github.com/hexamite/hx19-java.git

Build

    cd hx19-java
    ./gradlew build

Plug the HX19 Monitor device to a USB port. The serial port name will appear in `/dev` typically ttyUSB0 or ttyUSB1.

Own the serial port.

    sudo chown <your-user-name> /dev/ttyUSB0

Run the serial server.

    ./serial-server.sh /dev/<port-name>

Now you can run the command line utilities to montior, send messages to and upload files to the devices. Groovy is
required so have that [installed](http://groovy.codehaus.org/) first. Make sure the groovy command itself is on the
executable path. Now you can run

    src/util/groovy/monitor -h

    src/util/groovy/message -h

    src/util/groovy/upload -h

The easiest way to experiment with the system is to copy and modify the monitor Groovy script. ZeroMQ allows multiple
subscribers to attach to the same port.

## Status

The tools provided in this project are ready to use and provide an alternative to the VB6 programs previously used, for
those who do their own [3D calculations](https://en.wikipedia.org/wiki/Trilateration). They are still not widely tested
and they are still missing the position calculations. They provide only the distances between Receivers and Transmitters.

## Roadmap

In the next few weeks we will add positioning calculations so that a stream of device coordinates can be pushed
to listening application(s).

Longer term (a few months) we want to provide a GUI based interface to the system that leverages these tools.

## See also

Visit [Hexamite](http://hexamite.com) for more details..