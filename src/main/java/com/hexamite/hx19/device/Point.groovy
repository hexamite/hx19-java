package com.hexamite.hx19.device

import groovy.transform.Canonical;

@Canonical
class Point {
	int x
	int y
	int z
	
	String toString() { "Point(x: $x, y: $y, z: $z)" }
	
}
