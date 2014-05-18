package com.hexamite.trilat

import org.junit.*

class TrilatTest {

    @Test
    void testTrilaterate() {

        // The locations of the fixed points

        def fixed = [
                new Point(1000, 1000, 0),
                new Point(2140, 1000, 0),
                new Point(1000, 2140, 0)
        ]

        // Distance of the unknown point to the fixed points

        def dists = [
                3217,
                3158,
                3073
        ]

        assert new Trilat().trilaterate(fixed, dists) == new Point(1734, 1967, 2979)

    }
}
