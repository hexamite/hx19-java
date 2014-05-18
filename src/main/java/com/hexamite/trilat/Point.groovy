package com.hexamite.trilat

import groovy.transform.Canonical

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