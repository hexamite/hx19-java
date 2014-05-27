package com.hexamite.trilaterate

import groovy.transform.Canonical

/**
 * A Cartesian point with with basic vector addition and subtraction.
 * */
@Canonical
class Point {
    int x, y, z

    Point() { }

    Point(x, y, z) {
        this.x = x;
        this.y = y;
        this.z = z
    }

    def plus(Point p) {
        new Point(
                x + p.x,
                y + p.y,
                z + p.z
        )
    }

    def minus(Point p) {
        new Point(
                x - p.x,
                y - p.y,
                z - p.z
        )
    }
}