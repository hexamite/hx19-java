package com.hexamite.trilaterate

import org.jglue.cdiunit.CdiRunner
import org.junit.*
import org.junit.runner.RunWith

import javax.inject.Inject

@RunWith(CdiRunner)
class TrilateratorTest {

    @Inject Trilaterator trilaterator

    @Test
    void testTrilaterate() {

        // Distance of the unknown point to the fixed points

        def dists = [
                3217,
                3158,
                3073
        ]

        assert trilaterator.trilaterate(dists) == new Point(1734, 1967, 2979)

    }

    @Test
    void testExample() {

        def p = new Point(1734, 1967, 2979)

        def rx = new Point(1000, 1000, 0)
        def ry = new Point(2140, 1000, 0)
        def rz = new Point(1000, 2140, 0)


        def dx = (p - rx).abs()
        def dy = (p - ry).abs()
        def dz = (p - rz).abs()

        println "dist = $dx, $dy, $dz"

    }
}
